package fr.hahka.travel5a.proximity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

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

            /*Double longitude = getArguments().getDouble("long");
            Double latitude = getArguments().getDouble("lat");
            System.out.println(longitude + " / " + latitude);
            CameraUpdate center
                    = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));*/
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);

            //map.moveCamera(center);
            map.animateCamera(zoom);
        }

        addMarker(map, 48.814405, 2.377908, "ESIEA",
                "Ecole d'ingé en carton");
        /*addMarker(map, 40.76866299974387, -73.98268461227417,
                R.string.lincoln_center, R.string.lincoln_center_snippet);
        addMarker(map, 40.765136435316755, -73.97989511489868,
                R.string.carnegie_hall, R.string.practice_x3);
        addMarker(map, 40.70686417491799, -74.01572942733765,
                R.string.downtown_club, R.string.heisman_trophy);*/
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           int title, int snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(getString(title))
                .snippet(getString(snippet)));
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, String snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(snippet));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getActivity(),
                "MapsAPI : Connected",
                Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 17));
        }
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

}
