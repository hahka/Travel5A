package fr.hahka.travel5a.poi;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.utils.FileUtils;

/**
 * Created by thibautvirolle on 14/11/15.
 */
public final class PointOfInterestSQLite {


    /**
     * Nom de la table SQLite
     */
    public static final String TABLE_NAME = "point_of_interest";

    /**
     * Colonne ID : sert à identifier la ressource
     */
    public static final String COL_ID = "ID";

    /**
     * Colonne USER_ID : sert à identifier l'utilisateur auteur de cette publication
     */
    public static final String COL_USER_ID = "USER_ID";

    /**
     * Colonne DESCRIPTION : description de la publication
     */
    public static final String COL_DESCRIPTION = "DESCRIPTION";

    /**
     * Colonne IMAGE_PATH : chemin de l'image stockée en locale ou sur le serveur
     */
    public static final String COL_IMAGE_PATH = "IMAGE_PATH";

    /**
     * Colonne SOUND_PATH : inutilisée pour l'instant
     * TODO : à ajouter à l'application
     */
    public static final String COL_SOUND_PATH = "SOUND_PATH";

    /**
     * Colonne LATITUDE : latitude d'où a été posté la publication
     */
    public static final String COL_LATITUDE = "LATITUDE";

    /**
     * Colonne LONGITUDE : longitude d'où a été posté la publication
     */
    public static final String COL_LONGITUDE = "LONGITUDE";

    /**
     * Colonne NULLABLE : Colonne de "secours"
     */
    public static final String COL_NULLABLE = "NULLABLE";

    /**
     * Colonne NULLABLE : Colonne servant à savoir si les données sont sur le serveur,
     * et à les synchroniser avec le serveur le cas échéant
     */
    public static final String COL_ID_SERVER = "ID_SERVER";

    /**
     * Requète SQL pour créer la table POI
     */
    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DESCRIPTION + " TEXT NOT NULL, "
            + COL_LATITUDE + " DOUBLE NOT NULL, "
            + COL_LONGITUDE + " DOUBLE NOT NULL, "
            + COL_IMAGE_PATH + " TEXT NOT NULL, "
            + COL_SOUND_PATH + " TEXT, "
            + COL_NULLABLE + " INTEGER, "
            + COL_ID_SERVER + " INTEGER, "
            + COL_USER_ID + " INTEGER NOT NULL REFERENCES USER(ID));";


    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public ou par défaut
     */
    private PointOfInterestSQLite() { }

    /**
     * Fonction appelée lors de la création de la BDD
     * @param db : la BDD créée
     */
    public static void create(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    /**
     * Fonction appelée lors de la mise à jour de la BDD
     * @param db : la BDD à mettre à jour
     * @param oldVersion : ancienne version de la BDD
     * @param newVersion : nouvelle version de la BDD
     */
    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {

            case 2:

                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_ID_SERVER + " INTEGER;");

                File directory = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        Config.IMAGE_DIRECTORY_NAME);

                // Creation du repertoire s'il n'existe pas
                if (!FileUtils.createFolderIfNotExists(directory))
                    return;

                ArrayList<PointOfInterest> list = PointOfInterestDAO.getLocalPointOfInterests(db);

                for (int i = 0; i < list.size(); i++) {

                    ContentValues args = new ContentValues();
                    args.put(COL_IMAGE_PATH,
                            directory.getPath() + File.separator + list.get(i).getImagePath());
                    db.update(TABLE_NAME, args, COL_ID + "=" + list.get(i).getId(), null);

                }

                break;

            default:
                break;

        }
    }


}
