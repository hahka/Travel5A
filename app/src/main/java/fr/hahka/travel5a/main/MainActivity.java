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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.poi.PointOfInterestFragment;
import fr.hahka.travel5a.proximity.MapsFragment;
import fr.hahka.travel5a.publication.NewPublicationActivity;
import fr.hahka.travel5a.utils.MediaHelper;


/**
 * MainActivity : Classe principale de l'application
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

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

        btnCapturePicture = (FloatingActionButton) findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (FloatingActionButton) findViewById(R.id.btnRecordVideo);



        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(areLocationServicesEnabled()) {
                    captureImage();
                }
            }
        });

        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(areLocationServicesEnabled()) {
                    recordVideo();
                }
            }
        });


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
     * Lancement de l'application camera pour capturer une vidéo
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = MediaHelper.getOutputMediaFileUri(Config.MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the video capture Intent
        startActivityForResult(intent, Config.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Mise à jour du contenu principal en remplaçant le fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = new Fragment();

        switch (position)
        {
            case 0:
                fragment = new PointOfInterestFragment();
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

    /**
     * TODO: A expliquer
     * @param number :
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            default:
                mTitle = "toto";
                break;
        }
    }

    /**
     * TODO: A expliquer
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onResume();


        if (requestCode == Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            /*File sd = Environment.getExternalStorageDirectory();
            Log.d(TAG, fileUri.toString());
            Log.d(TAG, sd.getPath()+fileUri.toString());
            File image = new File(fileUri.getPath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);*/
            //imageView.setImageURI(fileUri);
            /*try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //imageView.setImageBitmap(imageBitmap);

            Intent newPublicationIntent = new Intent(MainActivity.this, NewPublicationActivity.class);
            newPublicationIntent.putExtra("fileuri", fileUri.toString());
            startActivityForResult(newPublicationIntent, Config.NEW_PUBLICATION_CODE);
            /*Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Config.PICK_IMAGE_REQUEST);*/
        }

    }



    public boolean areLocationServicesEnabled() {

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }

    }



}
