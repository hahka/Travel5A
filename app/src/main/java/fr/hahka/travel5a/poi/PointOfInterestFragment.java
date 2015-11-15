package fr.hahka.travel5a.poi;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.utils.StringUtils;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.download.FtpDownloadUtils;

/**
 * Created by thibautvirolle on 08/11/15.
 */
public class PointOfInterestFragment extends Fragment {

    /**
     * LogCat tag
     */
    private static final String TAG = PointOfInterestFragment.class.getSimpleName();

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

        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        downloadFileFtp(Config.HOST,
                "thibautvuh",
                "23tivi03SOAD",
                "www/projet5atravel/images/IMG_20151111_115324.bmp");

        return rootView;
    }




    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
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

}
