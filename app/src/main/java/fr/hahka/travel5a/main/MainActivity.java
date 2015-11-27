package fr.hahka.travel5a.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.gallery.GalleryProviderActivity;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.poi.PointOfInterest;
import fr.hahka.travel5a.poi.PointOfInterestDetailsFragment;
import fr.hahka.travel5a.poi.PointOfInterestFragment;
import fr.hahka.travel5a.poi.PointOfInterestFragmentManager;
import fr.hahka.travel5a.proximity.MapsFragment;
import fr.hahka.travel5a.publication.NewPublicationActivity;
import fr.hahka.travel5a.user.UserLoginActivity;
import fr.hahka.travel5a.utils.MediaHelper;


/**
 * MainActivity : Classe principale de l'application
 */
public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        View.OnClickListener, PointOfInterestFragment.OnPoiSelectedListener  {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Context de l'activité en cours
     */
    private Context context;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * btnCapturePicture : Bouton pour prendre une photo
     */
    private FloatingActionButton btnCapturePicture;

    /**
     * btnRecordVideo : Bouton pour prendre une vidéo
     */
    private FloatingActionButton btnRecordVideo;


    private String userId;


    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //btnCapturePicture = (FloatingActionButton) findViewById(R.id.btnCapturePicture);
        findViewById(R.id.btnCapturePicture).setOnClickListener(this);
        findViewById(R.id.btnOpenGallery).setOnClickListener(this);
        findViewById(R.id.btnSyncServer).setOnClickListener(this);

        if (savedInstanceState == null) {

            if (getIntent().getStringExtra(Config.USER_ID) != null) {
                userId = getIntent().getStringExtra(Config.USER_ID);
            } else {
                Intent loginIntent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivityForResult(loginIntent, Config.AUTH_REQUEST_CODE);
            }

        } else {
            userId = savedInstanceState.getString(Config.USER_ID);
        }


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(Config.USER_ID, userId);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * Lancement de l'application camera pour capturer une image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = MediaHelper.getOutputMediaFileUri(Config.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Lancement de l'application camera pour capturer une image
     */
    private void openGallery() {
        Intent intent = new Intent(MainActivity.this, GalleryProviderActivity.class);
        intent.putExtra(Config.USER_ID, userId);

        startActivityForResult(intent, Config.NEW_PUBLICATION_CODE);
        /*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1234);*/
    }

    /**
     * Lancement de l'application camera pour capturer une image
     */
    private void syncServer() {

    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Mise à jour du contenu principal en remplaçant le fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = new Fragment();

        switch (position)
        {
            case 0:
                fragment = new PointOfInterestFragmentManager();
                break;

            case 1:
                fragment = new MapsFragment();
                break;

            case 2:
                // TODO : Settings
                // fragment = new SettingsFragment();
                break;

            default:
                break;
        }


        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onResume();

        switch (requestCode) {
            case Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    Intent newPublicationIntent = new Intent(MainActivity.this, NewPublicationActivity.class);
                    newPublicationIntent.putExtra("fileuri", fileUri.toString());
                    newPublicationIntent.putExtra(Config.USER_ID, userId);
                    startActivityForResult(newPublicationIntent, Config.NEW_PUBLICATION_CODE);
                }
                break;


            case Config.NEW_PUBLICATION_CODE:
                Intent intent = getIntent();
                intent.putExtra(Config.USER_ID, data.getStringExtra(Config.USER_ID));
                finish();
                startActivity(intent);
                break;

            case Config.AUTH_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    userId = data.getStringExtra(Config.USER_ID);
                    onNavigationDrawerItemSelected(0);
                }
                else
                    finish();
                break;

            default:
                break;
        }

    }


    /**
     * Fonction déterminant si les services de géolocalisation sont activés
     * @return (Boolean) Vrai si les services sont activés, faux sinon
     */
    public boolean areLocationServicesEnabled() {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(
                    context.getResources().getString(R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCapturePicture) {
            captureImage();
        } else if (v.getId() == R.id.btnOpenGallery) {
            openGallery();
        } else if (v.getId() == R.id.btnSyncServer) {
            syncServer();
        }
    }


    /**
     * Fonction appelée lors d'un click sur un POI
     * @param poi : poi cliqué
     * @param position : position du poi cliqué dans la liste
     */
    public void onPoiSelected(PointOfInterest poi, int position) {

        PointOfInterestDetailsFragment poiDetailsFragment = (PointOfInterestDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.container)
                        .getChildFragmentManager().findFragmentById(R.id.poi_details_fragment);

        Log.d(TAG, String.valueOf(poiDetailsFragment != null));

        if (poiDetailsFragment != null) {

            poiDetailsFragment.updatePoiDetailsView(poi, position);

        } else {

            PointOfInterestDetailsFragment newFragment = new PointOfInterestDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(PointOfInterestDetailsFragment.ARG_POSITION, position);
            args.putString(PointOfInterestDetailsFragment.ARG_IMAGE_PATH, poi.getImagePath());
            args.putString(PointOfInterestDetailsFragment.ARG_DESCRIPTION, poi.getDescription());
            args.putDouble(PointOfInterestDetailsFragment.ARG_LATITUDE, poi.getLatitude());
            args.putDouble(PointOfInterestDetailsFragment.ARG_LONGITUDE, poi.getLongitude());
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }
}
