package fr.hahka.travel5a.poi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.GeocoderUtils;
import fr.hahka.travel5a.utils.download.ImageLocalDownloaderTask;

/**
 * Created by thibautvirolle on 27/11/15.
 */
public class PointOfInterestDetailsFragment extends Fragment {

    /**
     * position dans la listview de l'élément
     */
    public static final String ARG_POSITION = "position";

    /**
     * description de l'élément
     */
    public static final String ARG_DESCRIPTION = "description";

    /**
     * latitude
     */
    public static final String ARG_LATITUDE = "latitude";

    /**
     * longitude
     */
    public static final String ARG_LONGITUDE = "longitude";

    /**
     * emplacement de l'image
     */
    public static final String ARG_IMAGE_PATH = "image_path";

    private int mCurrentPosition = -1;

    private ImageView poiImageView;

    private TextView poiDescription;
    private TextView poiAddress;

    private PointOfInterest mPoi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            mPoi = new PointOfInterest();
            mPoi.setDescription(savedInstanceState.getString(ARG_DESCRIPTION));
            mPoi.setImagePath(savedInstanceState.getString(ARG_IMAGE_PATH));
            mPoi.setLatitude(savedInstanceState.getDouble(ARG_LATITUDE));
            mPoi.setLongitude(savedInstanceState.getDouble(ARG_LONGITUDE));

        }

        View rootView = inflater.inflate(R.layout.poi_details_fragment, container, false);

        poiImageView = (ImageView) rootView.findViewById(R.id.poiDetailsImage);

        poiDescription = (TextView) rootView.findViewById(R.id.poiDetailsDescription);

        poiAddress = (TextView) rootView.findViewById(R.id.poiDetailsAddress);

        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {

            PointOfInterest poi = new PointOfInterest();

            poi.setDescription(args.getString(ARG_DESCRIPTION));
            poi.setLatitude(args.getDouble(ARG_LATITUDE));
            poi.setLongitude(args.getDouble(ARG_LONGITUDE));
            poi.setImagePath(args.getString(ARG_IMAGE_PATH));
            updatePoiDetailsView(poi, args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            updatePoiDetailsView(mPoi, mCurrentPosition);
        }
    }


    /**
     * Met à jour le fragment details en fonction du POI cliqué
     * @param poi : poi cliqué
     * @param position : position du poi cliqué
     */
    public void updatePoiDetailsView(PointOfInterest poi, int position) {
        poiAddress.setText(GeocoderUtils.getFormatedAddressFromLocation(
                getActivity(),
                poi.getLatitude(),
                poi.getLongitude(),
                "Ci, Co"));
        poiDescription.setText(poi.getDescription());
        new ImageLocalDownloaderTask(poiImageView, getActivity()).execute(poi.getImagePath());
        mCurrentPosition = position;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
        outState.putString(ARG_DESCRIPTION, mPoi.getDescription());
        outState.putString(ARG_IMAGE_PATH, mPoi.getImagePath());
        outState.putDouble(ARG_LATITUDE, mPoi.getLatitude());
        outState.putDouble(ARG_LONGITUDE, mPoi.getLongitude());
    }


}
