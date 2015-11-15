package fr.hahka.travel5a.publication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.RequestHandler;
import fr.hahka.travel5a.utils.FileUtils;
import fr.hahka.travel5a.utils.ImageUtils;

/**
 * Activité pour publier une nouvelle photo (et enregistrement sonore)
 */
public class NewPublicationActivity extends Activity {

    /**
     *LogCat tag
     */
    private static final String TAG = NewPublicationActivity.class.getSimpleName();

    /**
     * TODO : à expliquer
     */
    public static final String UPLOAD_KEY = "image";

    /**
     * Bitmap qui sera utilisé pour afficher l'image que l'utilisateur veut publier
     */
    private Bitmap bitmap;

    /**
     * Image bitmap non redimentionnée (taille originale)
     */
    private Bitmap fullBitmap;

    /**
     * Bouton pour uploader la publication, avec état d'avancement (cercle animé)
     */
    private FloatingActionButton uploadButton;

    /**
     *
     */
    private File image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);

        /**
         * ImageView pour contenir l'image prise par l'utilisateur
         */
        ImageView imageView = (ImageView) findViewById(R.id.newPublicationImageView);

        uploadButton = (FloatingActionButton) findViewById(R.id.fab_upload);


        /**
         * Capture image button click event
         */
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO : Envoi de la publication complète
                // Envoi de la publication (image) au serveur
                uploadBitmap();
            }
        });

        Bundle bundle = getIntent().getExtras();

        String fileUri = bundle.getString("fileuri");

        Log.d(TAG, fileUri);
        File sd = Environment.getExternalStorageDirectory();
        Log.d(TAG, sd.toString());
        image = null;
        if (fileUri != null) {
            image = new File(fileUri.split(":")[1]);

            bitmap = ImageUtils.getOrientedAndScaledBitmap(image, getResources().getDisplayMetrics().widthPixels);

            imageView.setImageBitmap(bitmap);
        }

    }

    /**
     * TODO : A expliquer
     */
    private void uploadImage() {

        class UploadImage extends AsyncTask<File, Void, String> {

            private RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploadButton.setShowProgressBackground(true);
                uploadButton.setIndeterminate(true);
                Toast.makeText(getApplicationContext(), "Uploading image", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploadButton.setIndeterminate(false);
                uploadButton.setProgress(100, false);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(File... params) {
                File imageParam = params[0];
                String uploadImage;

                HashMap<String, String> data = new HashMap<>();
                try {
                    uploadImage = FileUtils.getEncodedFile(imageParam);

                    data.put(UPLOAD_KEY, uploadImage);
                    data.put("image_name", image.getName());
                    data.put("image_name", image.getName());
                    data.put("image_name", image.getName());
                    data.put("image_name", image.getName());
                    data.put("image_name", image.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return rh.sendPostRequest(Config.UPLOAD_URL, data);
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(image);

    }

    /**
     * TODO : A expliquer
     */
    private void uploadBitmap() {

        class UploadBitmap extends AsyncTask<Bitmap, Void, String> {

            private RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploadButton.setShowProgressBackground(true);
                uploadButton.setIndeterminate(true);
                Toast.makeText(getApplicationContext(), getString(R.string.uploadingPublication), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploadButton.setIndeterminate(false);
                uploadButton.setProgress(100, false);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put(UPLOAD_KEY, FileUtils.getEncodedBitmap(params[0]));
                data.put("image_name", image.getName().replace(".jpg", ".bmp"));

                return rh.sendPostRequest(Config.UPLOAD_URL, data);
            }
        }

        UploadBitmap ui = new UploadBitmap();
        ui.execute(bitmap);

    }

}
