package fr.hahka.travel5a.proximity;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.RequestHandler;
import fr.hahka.travel5a.poi.PointOfInterest;

/**
 * MapsFragment : fragment pour afficher la carte et les points d'intérêts
 */
public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * LogCat TAG
     */
    private static final String TAG = MapsFragment.class.getSimpleName();

    private static final long MIN_TIME_BW_UPDATES = 10000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 150;
    private GoogleMap mMap;

    private boolean needsInit = false;


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private LocationManager locationManager;
    private String bestProvider;



    private ArrayList<PointOfInterest> mMyMarkersArray = new ArrayList<PointOfInterest>();
    private HashMap<Marker, PointOfInterest> mMarkersHashMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            needsInit = true;
        }

        getMapAsync(this);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        // Get the location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // List all providers:
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            Log.d(TAG, provider);
        }

        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            Log.d(TAG, location.toString());
        } else {
            Log.d(TAG, "location is null");
        }

        mMarkersHashMap = new HashMap<Marker, PointOfInterest>();
    }




    /**
     * TODO : A expliquer
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        if (needsInit) {

            mMap = map;

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);

            map.animateCamera(zoom);
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        //Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getActivity(),
                "MapsAPI : Connected",
                Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        if (mLastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(latitude, longitude), 17));
        }

        getNearPoi();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(),
                "MapsAPI : Connection Suspended",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),
                "MapsAPI : Connection Failed",
                Toast.LENGTH_LONG).show();
    }



    /**
     * TODO : A expliquer
     */
    private void getNearPoi() {

        /**
         * Récupération des POIs proches par une asynctask
         */
        class GetNearPoi extends AsyncTask<Bitmap, Void, String> {

            private RequestHandler rh = new RequestHandler();

            private double latitude;

            private double longitude;

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                ArrayList<PointOfInterest> poisArray = new ArrayList<>();

                try {
                    JSONObject result = new JSONObject(s);
                    JSONArray pois = result.getJSONArray("result");

                    for (int i = 0; i < pois.length(); i++) {
                        JSONObject poi = (JSONObject) pois.get(i);
                        PointOfInterest newPoi = new PointOfInterest();
                        newPoi.setId(poi.getInt("poi_id"));
                        newPoi.setDescription(poi.getString("description"));
                        newPoi.setLatitude(poi.getDouble("latitude"));
                        newPoi.setLongitude(poi.getDouble("longitude"));
                        newPoi.setUserId(poi.getInt("user_id"));
                        newPoi.setImagePath(poi.getString("image_path"));
                        newPoi.setUsername(poi.getString("username"));
                        poisArray.add(i, newPoi);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                plotMarkers(poisArray);

            }

            @Override
            protected String doInBackground(Bitmap... params) {

                if (mLastLocation != null) {

                    return rh.sendGetRequest(Config.GET_NEAR_POI_URL
                            + "?lat=" + latitude
                            + "&lon=" + longitude
                            + "&dist=" + 1);

                } else {
                    return null;
                }
            }
        }


        GetNearPoi task = new GetNearPoi();
        task.setLatitude(48.8);
        task.setLongitude(1.9);
        task.execute();

    }


    private void plotMarkers(ArrayList<PointOfInterest> markers)
    {
        if (markers.size() > 0)
        {
            for (PointOfInterest myMarker : markers)
            {
                MarkerOptions markerOption = new MarkerOptions()
                        .position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));

                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }


    /**
     * Custom markers
     */
    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        /**
         * Constructeur par défaut
         */
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v = getActivity().getLayoutInflater().inflate(R.layout.poi_map_marker, null);

            PointOfInterest myPoi = mMarkersHashMap.get(marker);


            TextView markerLabel = (TextView) v.findViewById(R.id.marker_poi_description);

            TextView markerUsername = (TextView) v.findViewById(R.id.marker_poi_username);

            markerLabel.setText(myPoi.getDescription());

            markerUsername.setText(myPoi.getUsername());

            return v;
        }
    }




}
