package com.example.btgmain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "Accountsdb";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Users";
    public static final String ID_COL = "userID";
    public static final String Username_COL = "Username";
    public static final String Fname_COL = "FirstName";
    public static final String Lname_COL = "LastName";
    public static final String Password_COL = "Password";

    public DBHandler(LoginPage loginPage) {
        super(loginPage, DB_NAME, null, DB_VERSION);
    }

    public DBHandler(ShowList showList) {
        super(showList, DB_NAME, null, DB_VERSION);
    }

    public DBHandler(RegistrationPage registrationPage) {
        super(registrationPage,DB_NAME,null,DB_VERSION);
    }

    public DBHandler(MainPage mainPage) {
        super(mainPage,DB_NAME,null,DB_VERSION);
    }

    public DBHandler(UpdateUser_Page updateUser_page) {
        super(updateUser_page,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Username_COL + " TEXT,"
                + Fname_COL + " TEXT, "
                + Lname_COL + " TEXT, "
                + Password_COL + " TEXT)";
        db.execSQL(query);
    }

    public void addUSer(String username, String fname, String lname, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Username_COL, username);
        values.put(Fname_COL, fname);
        values.put(Lname_COL, lname);
        values.put(Password_COL, password);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Modal> readUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Modal> ModalArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ModalArrayList.add(new Modal(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ModalArrayList;
    }

    public boolean checkUser(String username) {
        String[] columns = {ID_COL};
        SQLiteDatabase db = this.getReadableDatabase();
        String select = Username_COL + " = ?";
        String[] selectArgs = {username};

        Cursor cursor = db.query(TABLE_NAME,
                columns,
                select,
                selectArgs,
                null,
                null,
                null);
        int cursorcount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorcount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUsers(String Username, String Password) {
        String[] columns = {ID_COL};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = Username_COL + " = ? " + " AND " + Password_COL + " = ?";
        String[] selectionArgs = {Username, Password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public void updateUser(String ogname,String Fname, String Lname, String Username, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Username_COL, Username);
        values.put(Fname_COL, Fname);
        values.put(Lname_COL, Lname);
        values.put(Password_COL, Password);

        db.update(TABLE_NAME, values, Fname_COL + " = ? ", new String[]{ogname});
        db.close();
    }

    public void deleteUser(Modal modes) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " =? ", new String[]{String.valueOf(modes.getID())});
        db.close();

    }
    public String getUsername() throws SQLException{
        String username = "";
        Cursor cursor = this.getReadableDatabase().query(TABLE_NAME,
                new String[]{Username_COL},
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            do{
                username = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return username;
    }


    }

