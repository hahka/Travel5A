package fr.hahka.travel5a.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import fr.hahka.travel5a.poi.PointOfInterestSQLite;
import fr.hahka.travel5a.user.UserSQLite;


/**
 * Created by thibautvirolle on 14/11/15.
 */
public class TravelSQLite extends SQLiteOpenHelper {

    public TravelSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
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