package com.youthfimodd.elenges.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Nom de la base de données
    public static final String DATABASE_NAME = "Elenge.db";
    //Noms des tables
    public static final String TABLE_NAME = "Users_table";
    public static final String TABLE_CHAT = "Messages_table";
    public static final String TABLE_SSR = "SSR_table";
    public static final String TABLE_FORUM = "Forum_table";
    //attributs de la table Users_table
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "VILLE";
    public static final String COL_4 = "NUMBER";
    public static final String COL_5 = "EMAIL";
    public static final String COL_6 = "TYPUSEUR";
    public static final String COL_7 = "PROFIL";
    //attribue de la table SSR_table
    public static final String Id = "ID";
    public static final String Titre = "TITRE";
    public static final String Descr = "DESCRIP";
    // public static final String Logo = "LOGO";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, VILLE TEXT, NUMBER INTEGER, PROFIL BLOB, EMAIL TEXT, TYPUSEUR TEXT )");
        db.execSQL("CREATE TABLE " + TABLE_SSR + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITRE TEXT, DESCRIP TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SSR);
    }

    public boolean insertData(String name, String ville, String number/*, byte[] profil*/, String email, String user) throws SQLException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, ville);
        contentValues.put(COL_4, number);
        // contentValues.put(COL_5, profil);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, user);
        long result = database.insert(TABLE_NAME, null, contentValues);
        database.close();

        //To Check where Data is Inserted in DataBase
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }
    //get data to Users_table
    public Cursor getData(/*String nom*/){
        SQLiteDatabase database = this.getWritableDatabase();
        String query ="SELECT * FROM " + TABLE_NAME/* + " WHERE name = '"+nom+"'"*/;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}
