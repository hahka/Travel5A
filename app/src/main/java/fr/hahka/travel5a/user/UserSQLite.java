package fr.hahka.travel5a.user;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by thibautvirolle on 14/11/15.
 */
public final class UserSQLite {


    private static final String TABLE_USER = "user";
    private static final String COL_ID = "ID";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_USER + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_USERNAME + " TEXT NOT NULL, "
            + COL_EMAIL + " TEXT NOT NULL, "
            + COL_PASSWORD + " TEXT NOT NULL);";

    /**
     * Les classes utilitaires ne doivent pas avoir de constructeur public ou par défaut
     */
    private UserSQLite() { }

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
    }


}
