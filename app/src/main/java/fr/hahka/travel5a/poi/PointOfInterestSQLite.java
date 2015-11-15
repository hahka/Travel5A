package fr.hahka.travel5a.poi;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by thibautvirolle on 14/11/15.
 */
public class PointOfInterestSQLite {


    private static final String TABLE_POI = "point_of_interest";
    private static final String COL_ID = "ID";
    private static final String COL_USER_ID = "USER_ID";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_IMAGE_PATH = "IMAGE_PATH";
    private static final String COL_SOUND_PATH = "SOUND_PATH";
    private static final String COL_LATITUDE = "LATITUDE";
    private static final String COL_LONGITUDE = "LONGITUDE";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_POI + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DESCRIPTION + " TEXT NOT NULL, "
            + COL_LATITUDE + " DOUBLE NOT NULL, "
            + COL_LONGITUDE + " DOUBLE NOT NULL, "
            + COL_IMAGE_PATH + " TEXT NOT NULL, "
            + COL_SOUND_PATH + " TEXT, "
            + COL_USER_ID + " INTEGER NOT NULL REFERENCES USER(ID);";

    public static void create(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }


    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_POI + ";");
    }


}
