package fr.hahka.travel5a.poi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.hahka.travel5a.POIOptionsDialog;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.StringUtils;
import fr.hahka.travel5a.utils.download.FtpDownloadUtils;

/**
 * Created by thibautvirolle on 08/11/15.
 */
public class PointOfInterestFragment extends Fragment {

    /**
     * TODO
     */
    public static final int DIALOG_FRAGMENT = 1;

    /**
     * LogCat tag
     */
    private static final String TAG = PointOfInterestFragment.class.getSimpleName();

    private ArrayList<PointOfInterest> listData;
    private PointOfInterestListAdapter poiAdapter;


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


        final ListView listView = (ListView) rootView.findViewById(R.id.momentsListView);

        poiAdapter = new PointOfInterestListAdapter(getActivity(), listData, true);
        listView.setAdapter(poiAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                // TODO : Activity pour afficher en détail la publication
                PointOfInterest poi = (PointOfInterest) listView.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected :" + " " + poi, Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                PointOfInterest newsData = (PointOfInterest) listView.getItemAtPosition(position);
                showDialog(newsData.getId());

                return false;
            }

        });


        return rootView;
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


    /**
     * TODO : A expliquer
     */
    private void downloadFileFtp(final String downloadUrl, final String user, final String password,
                                 final String path) {


        class DownloadImage extends AsyncTask<String, Void, File> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getActivity(), "Downloading image", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(File f) {
                super.onPostExecute(f);
                if (f == null) {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "ok : not null", Toast.LENGTH_LONG).show();
                }

                if (f != null) {
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = (width * bitmap.getHeight()) / bitmap.getWidth();
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            protected File doInBackground(String... params) {
                String ftpUrlParam = params[0];

                try {
                    file = new File(
                            FtpDownloadUtils.downloadFile(
                                    ftpUrlParam,
                                    Environment.getExternalStorageDirectory().getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }
        }

        String ftpurl = StringUtils.getFtpUrl(user, password, downloadUrl, path);

        DownloadImage ui = new DownloadImage();
        Toast.makeText(getActivity(), ftpurl, Toast.LENGTH_LONG).show();
        ui.execute(ftpurl);



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


                    } else {
                        Toast.makeText(getActivity(), "nope", Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "OK");
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                    Toast.makeText(getActivity(), "KO", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }


}

