package fr.hahka.travel5a.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.hahka.travel5a.poi.PointOfInterestSQLite;
import fr.hahka.travel5a.user.UserSQLite;


/**
 * Created by thibautvirolle on 14/11/15.
 */
public class TravelSQLite extends SQLiteOpenHelper {

    /**
     * Version actuelle de la base de données
     * En mettant à jour ce champ, la base sera mise à jour automatiquement
     * selon la fonction onUpgrade définie plus bas
     */
    public static final int DATABASE_VERSION = 2;

    /**
     * Nom de la base de données pour la différencier
     */
    public static final String DATABASE_NAME = "Travel5A.db";

    /**
     * Constructeur de l'objet TravelSQLite
     * @param context context de l'activité en cours
     */
    public TravelSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // On appelle les fonctions de creation des deux classe SQL
        UserSQLite.create(db);
        PointOfInterestSQLite.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On appelle les fonctions d'upgrade des deux classe SQL
        UserSQLite.upgrade(db, oldVersion, newVersion);
        PointOfInterestSQLite.upgrade(db, oldVersion, newVersion);
    }

}