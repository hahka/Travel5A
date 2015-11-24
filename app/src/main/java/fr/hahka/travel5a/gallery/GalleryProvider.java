package fr.hahka.travel5a.gallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.R;

/**
 * Created by thibautvirolle on 22/11/15.
 */
public class GalleryProvider extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        context = GalleryProvider.this;

        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        //String selection = MediaStore.Images.Media.LATITUDE + " NOT NULL";
        String selection = null;
        String [] selectionArgs = null;
        Cursor mImageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);

        /*if (mImageCursor != null && mImageCursor.moveToFirst()) {
            int columnIndex = mImageCursor.getColumnIndex(filePathColumn[0]);
            String filePath = mImageCursor.getString(columnIndex);
            mImageCursor.close();
            return Uri.parse(filePath);
        } else {
            mImageCursor.close();
            return Uri.parse("");
        }*/

        // Find ListView to populate
        RecyclerView lvItems = (RecyclerView) findViewById(R.id.galleryGridView);
        lvItems.setHasFixedSize(true);
        /*LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvItems.setLayoutManager(llm);*/

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        lvItems.setLayoutManager(mLayoutManager);

        // Setup cursor adapter using cursor from last step

        ArrayList<File> files = new ArrayList<File>();
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

        GalleryAdapter todoAdapter = new GalleryAdapter(this, files);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);

    }



}
