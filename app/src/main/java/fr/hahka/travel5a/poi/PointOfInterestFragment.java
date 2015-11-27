package fr.hahka.travel5a.poi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.POIOptionsDialog;
import fr.hahka.travel5a.R;

/**
 * Created by thibautvirolle on 08/11/15.
 */
public class PointOfInterestFragment extends Fragment
    implements PointOfInterestCustomClicker.OnItemClickListener,
    PointOfInterestCustomClicker.OnItemLongClickListener {

    /**
     * TODO
     */
    public static final int DIALOG_FRAGMENT = 1;

    private OnPoiSelectedListener mCallback;


    private ArrayList<PointOfInterest> listData;
    private PointOfInterestListAdapter poiAdapter;

    private RecyclerView listView;

    private Bitmap bitmap;

    private File file;

    private ImageView imageView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.moments_list_fragment, container, false);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            getActivity().finish();
        }

        listData = PointOfInterestDAO.getLocalPointOfInterests(getActivity());


        listView = (RecyclerView) rootView.findViewById(R.id.momentsListView);

        listView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);

        poiAdapter = new PointOfInterestListAdapter(getActivity(), this, listData, true);
        listView.setAdapter(poiAdapter);

        return rootView;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnPoiSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    /**
     * Vérification de la présence d'un APN sur le device
     */
    private boolean isDeviceSupportCamera() {
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * Méthode pour afficher une dialogBox avec des options telles que "Supprimer"
     * @param id : id de la valeur pour laquelle on veut afficher la dialogBox
     */
    void showDialog(int id) {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);



        DialogFragment dialogFrag = POIOptionsDialog.newInstance(id);
        dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    if (data.getStringExtra("value").equals("delete")) {

                        PointOfInterestDAO.deletePointOfInterestById(getActivity(), data.getIntExtra("dataId", -1));

                        listData = PointOfInterestDAO.getLocalPointOfInterests(getActivity());

                        /* Rafraichissement de la vue avec les nouvelles données */
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                poiAdapter.refreshData(listData);
                            };
                        });

                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view) {

        int position = listView.getChildPosition(view);
        PointOfInterest poi = listData.get(position);

        // Notify the parent activity of selected item
        mCallback.onPoiSelected(poi, position);

    }

    @Override
    public void onItemLongClick(View view) {

        PointOfInterest poi = listData.get(listView.getChildPosition(view));
        showDialog(poi.getId());

    }


    /**
     * The container Activity must implement this interface so the frag can deliver messages
     */
    public interface OnPoiSelectedListener {

        /**
         * Called by HeadlinesFragment when a list item is selected
         * @param poi : poi sélectionné
         * @param position : position du poi sélectionné
         */
        public void onPoiSelected(PointOfInterest poi, int position);
    }
}

