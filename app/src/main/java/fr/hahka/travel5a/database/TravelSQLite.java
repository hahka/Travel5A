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
    public static final int DATABASE_VERSION = 1;

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
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        UserSQLite.create(db);
        PointOfInterestSQLite.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        UserSQLite.upgrade(db, oldVersion, newVersion);
        PointOfInterestSQLite.upgrade(db, oldVersion, newVersion);
        onCreate(db);
    }

}