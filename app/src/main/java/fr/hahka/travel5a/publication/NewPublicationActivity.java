package fr.hahka.travel5a.publication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.HashMap;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.RequestHandler;
import fr.hahka.travel5a.poi.PointOfInterest;
import fr.hahka.travel5a.poi.PointOfInterestDAO;
import fr.hahka.travel5a.utils.FileUtils;
import fr.hahka.travel5a.utils.ImageUtils;

/**
 * Activité pour publier une nouvelle photo (et enregistrement sonore)
 */
public class NewPublicationActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * TODO : à expliquer
     */
    public static final String UPLOAD_KEY = "image";

    /**
     *LogCat tag
     */
    private static final String TAG = NewPublicationActivity.class.getSimpleName();

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
     * EditText pour le description de la publication
     */
    private EditText descriptionPublication;

    /**
     *
     */
    private File image;

    private Double latitude;
    private Double longitude;


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);

        userId = getIntent().getStringExtra(Config.USER_ID);
        if (userId == null)
            finish();
        /**
         * ImageView pour contenir l'image prise par l'utilisateur
         */
        ImageView imageView = (ImageView) findViewById(R.id.newPublicationImageView);

        uploadButton = (FloatingActionButton) findViewById(R.id.fab_upload);

        descriptionPublication = (EditText) findViewById(R.id.publicationDescription);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        /**
         * Capture image button click event
         */
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO : Envoi de la publication complète
                // Envoi de la publication (image) au serveur
                uploadPublication();
            }
        });

        Bundle bundle = getIntent().getExtras();

        String fileUri = bundle.getString("fileuri");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        int maxDisplaySize = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        if (getResources().getConfiguration().orientation == 2) {
            maxDisplaySize = (int) (getResources().getDisplayMetrics().widthPixels * 0.4);
        }
        image = null;
        if (fileUri != null) {
            image = new File(fileUri.split(":")[1]);

            bitmap = ImageUtils.getOrientedAndScaledBitmap(image, maxDisplaySize);

            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * TODO : A expliquer
     */
    private void uploadPublication() {

        String filePath = ImageUtils.saveToInternalSorage(bitmap, image.getName().replace(".jpg", ".bmp"));

        PointOfInterest poi = new PointOfInterest();
        poi.setDescription(descriptionPublication.getText().toString());
        poi.setLatitude(mLastLocation.getLatitude());
        poi.setLongitude(mLastLocation.getLongitude());
        poi.setUserId(Integer.parseInt(userId.trim()));
        poi.setImagePath(filePath);

        PointOfInterestDAO.insertPointOfInterest(getApplicationContext(), poi);


        /**
         * Classe pour uploader les publications
         */
        class UploadToServer extends AsyncTask<Bitmap, Void, String> {

            private RequestHandler rh = new RequestHandler();

            private String descriptionPublication;

            public void setDescriptionPublication(String str) {
                descriptionPublication = str;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploadButton.setShowProgressBackground(true);
                uploadButton.setIndeterminate(true);
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.uploadingPublication),
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploadButton.setIndeterminate(false);
                uploadButton.setProgress(100, false);
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                if (mLastLocation != null) {
                    HashMap<String, String> data = new HashMap<>();

                    data.put(UPLOAD_KEY, FileUtils.getEncodedBitmap(params[0]));
                    data.put("image_name", image.getName().replace(".jpg", ".bmp"));
                    data.put("user_id", userId);
                    data.put("description", (descriptionPublication));
                    data.put("latitude", String.valueOf(mLastLocation.getLatitude()));
                    data.put("longitude", String.valueOf(mLastLocation.getLongitude()));

                    return rh.sendPostRequest(Config.UPLOAD_URL, data);

                } else {
                    return null;
                }
            }
        }


        UploadToServer ui = new UploadToServer();
        ui.setDescriptionPublication(descriptionPublication.getText().toString());
        ui.execute(bitmap);

        Intent intent = new Intent();
        intent.putExtra(Config.USER_ID, userId);
        setResult(RESULT_OK, intent);
        finish();

    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //uploadButton.setEnabled(false);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,
                "MapsAPI : Connection Suspended",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "MapsAPI : Connection Failed",
                Toast.LENGTH_LONG).show();
        Log.d(TAG, connectionResult.toString());
        switch (connectionResult.getErrorCode()) {

            case ConnectionResult.SERVICE_MISSING:
                break;

            default:
                break;

        }
    }


    /**
     * TODO : A expliquer
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

}
