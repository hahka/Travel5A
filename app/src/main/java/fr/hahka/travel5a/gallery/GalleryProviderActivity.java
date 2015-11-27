package fr.hahka.travel5a.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.publication.NewPublicationActivity;
import fr.hahka.travel5a.utils.ScreenUtils;

/**
 * Created by thibautvirolle on 22/11/15.
 */
public class GalleryProviderActivity extends Activity {

    private static final String TAG = GalleryProviderActivity.class.getSimpleName();
    private Context context;

    private ArrayList<File> files;

    private RecyclerView lvItems;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        context = GalleryProviderActivity.this;

        userId = getIntent().getStringExtra(Config.USER_ID);

        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.LATITUDE + " NOT NULL";
        String [] selectionArgs = null;
        Cursor mImageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);

        // Setup cursor adapter using cursor from last step

        files = new ArrayList<File>();
        File data;
        if (mImageCursor != null) {
            while (mImageCursor.moveToNext()) {
                data = new File(mImageCursor.getString(
                        mImageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                ));
                files.add(data);
            }
            mImageCursor.close();
        }

        // Find ListView to populate
        lvItems = (RecyclerView) findViewById(R.id.galleryGridView);
        lvItems.setHasFixedSize(true);

        double displayWidth = ScreenUtils.getScreenWidthInInchWithOrientation(getApplicationContext());

        int numberPerRow = 0;

        while ((displayWidth / (numberPerRow + 1)) > 0.8) {
            numberPerRow++;
        }

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, numberPerRow);
        lvItems.setLayoutManager(mLayoutManager);


        GalleryAdapter todoAdapter = new GalleryAdapter(this, files);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);

        lvItems.addOnItemTouchListener(
                new GalleryItemClicker(context, new GalleryItemClicker.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent newPublicationIntent = new Intent(
                                GalleryProviderActivity.this,
                                NewPublicationActivity.class);
                        newPublicationIntent.putExtra("fileuri", "file:///" + files.get(position).getPath());
                        newPublicationIntent.putExtra(Config.USER_ID, userId);
                        startActivityForResult(newPublicationIntent, Config.NEW_PUBLICATION_CODE);

                    }
                }));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Config.USER_ID, userId);
        setResult(RESULT_OK, intent);
        finish();
    }

}
