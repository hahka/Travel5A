package fr.hahka.travel5a.poi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import fr.hahka.travel5a.database.TravelSQLite;

/**
 * Created by thibautvirolle on 17/11/15.
 * DAO = Data Access Object
 * C'est ici que seront traités les accès aux POI, que ce soit en local ou depuis le serveur
 */
public final class PointOfInterestDAO {


    private PointOfInterestDAO() { }


    /**
     * Récupère les Point d'intérêts depuis une base de donnée (locale pour l'instant)
     * @param context Le context dans lequel est appelé la fonction
     * @return (ArrayList) Liste de points d'intérêts
     */
    public static ArrayList<PointOfInterest> getLocalPointOfInterests(Context context) {

        ArrayList<PointOfInterest> poiList = new ArrayList<>();

        TravelSQLite travelSQLite = new TravelSQLite(context);
        SQLiteDatabase db = travelSQLite.getReadableDatabase();

        String[] projection = {
                PointOfInterestSQLite.COL_ID,
                PointOfInterestSQLite.COL_DESCRIPTION,
                PointOfInterestSQLite.COL_IMAGE_PATH,
                PointOfInterestSQLite.COL_LATITUDE,
                PointOfInterestSQLite.COL_LONGITUDE,
                PointOfInterestSQLite.COL_USER_ID
        };

        String sortOrder = PointOfInterestSQLite.COL_ID + " DESC";

        Cursor cursor = db.query(
                PointOfInterestSQLite.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (cursor.getCount() <= 0) {
            Toast.makeText(context, "Aucune données", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, cursor.getCount() + " données", Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            while (!cursor.isLast()) {

                poiList.add(createPointOfInterestFromCursor(cursor));
                cursor.moveToNext();
            }

            poiList.add(createPointOfInterestFromCursor(cursor));

        }

        return poiList;
    }


    private static PointOfInterest createPointOfInterestFromCursor(Cursor cursor) {

        PointOfInterest poi = new PointOfInterest();
        poi.setId(cursor.getInt(
                cursor.getColumnIndex(PointOfInterestSQLite.COL_ID)));
        poi.setDescription(cursor.getString(
                cursor.getColumnIndexOrThrow(PointOfInterestSQLite.COL_DESCRIPTION)));
        poi.setLatitude(cursor.getDouble(
                cursor.getColumnIndexOrThrow(PointOfInterestSQLite.COL_LATITUDE)));
        poi.setLongitude(cursor.getDouble(
                cursor.getColumnIndexOrThrow(PointOfInterestSQLite.COL_LONGITUDE)));
        poi.setImagePath(cursor.getString(
                cursor.getColumnIndexOrThrow(PointOfInterestSQLite.COL_IMAGE_PATH)));
        poi.setUserId(cursor.getInt(
                cursor.getColumnIndexOrThrow(PointOfInterestSQLite.COL_USER_ID)));

        return poi;
    }


    /**
     * Insère en base un point d'intérêt
     * @param c Context depuis lequel est appelé la fonctoin
     * @param poi Le point d'intérêt à ajouter en base
     */
    public static void insertPointOfInterest(Context c, PointOfInterest poi) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PointOfInterestSQLite.COL_DESCRIPTION, poi.getDescription());
        values.put(PointOfInterestSQLite.COL_IMAGE_PATH, poi.getImagePath());
        values.put(PointOfInterestSQLite.COL_LATITUDE, poi.getLatitude());
        values.put(PointOfInterestSQLite.COL_LONGITUDE, poi.getLongitude());
        values.put(PointOfInterestSQLite.COL_USER_ID, poi.getUserId());

        TravelSQLite travelSQLite = new TravelSQLite(c);
        SQLiteDatabase db = travelSQLite.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                PointOfInterestSQLite.TABLE_NAME,
                PointOfInterestSQLite.COL_NULLABLE,
                values);

    }

    /**
     * Supprime un point d'intérêts en fonction de son id
     * @param c Le context depuis lequel est appelé la fonction
     * @param rowId L'id de la ligne à supprimer
     */
    public static void deletePointOfInterestById(Context c, int rowId) {

        Toast.makeText(c, String.valueOf(rowId), Toast.LENGTH_SHORT).show();

        TravelSQLite travelSQLite = new TravelSQLite(c);
        SQLiteDatabase db = travelSQLite.getWritableDatabase();

        // Define 'where' part of query.
        String selection = PointOfInterestSQLite.COL_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(rowId)};
        // Issue SQL statement.
        db.delete(PointOfInterestSQLite.TABLE_NAME, selection, selectionArgs);
    }


}
