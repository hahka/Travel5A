package fr.hahka.travel5a.poi;

import android.database.sqlite.SQLiteDatabase;

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
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    /**
     * Fonction appelée lors de la mise à jour de la BDD
     * @param db : la BDD à mettre à jour
     * @param oldVersion : ancienne version de la BDD
     * @param newVersion : nouvelle version de la BDD
     */
    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_NAME + ";");
    }


}
