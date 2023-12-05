package org.pytorch.demo.objectdetection.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import org.pytorch.demo.objectdetection.models.Building_Info;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "christdb";
    private static final String Table_Building_Info = "blockdata";

    private static final String Table_2 = "block1";


    //  private static final String Table_3 = "flights";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ROOMNO = "room_no";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FLOORS = "floor";

    private static  final String key_blocks="block_name";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Table_Building_Info + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ROOMNO + " TEXT,"  +KEY_TYPE + " TEXT,"+ KEY_FLOORS + " TEXT,"+ key_blocks+" TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Building_Info);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addData(Building_Info contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Building_Info Name
        values.put(KEY_ROOMNO, contact.getRoomNo());

        values.put(KEY_TYPE, contact.getType());
        values.put(KEY_FLOORS, contact.getFloors());
        values.put(key_blocks, contact.getBlocks());

        db.insert(Table_Building_Info, null, values);

        db.close(); // Closing database connection
    }



    // code to get the single contact
    public Building_Info getBuilding_Info(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Table_Building_Info, new String[]{KEY_ID,
                        KEY_NAME, KEY_ROOMNO,KEY_TYPE,KEY_FLOORS,key_blocks}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Building_Info contact = new Building_Info(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5) );
        // return contact
        return contact;
    }





    public Building_Info getBuilding_Info(String blocks) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Table_Building_Info, new String[]{KEY_ID,
                        KEY_NAME, KEY_ROOMNO,KEY_TYPE,KEY_FLOORS}, key_blocks + "=?",
                new String[]{String.valueOf(blocks)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {

            Building_Info contact = new Building_Info(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5) );
            // return contact
            return contact;
        }
        else
            return null;

    }

    // code to get all contacts in a list view
    public List<Building_Info> getAllBuilding_Info(String blocks, String type) {
        List<Building_Info> contactList = new ArrayList<Building_Info>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Table_Building_Info +" WHERE "+ key_blocks+"= '"+blocks+"' and "+KEY_TYPE+" = '"+type+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Building_Info contact = new Building_Info();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setRoomNo(cursor.getString(2));

                contact.setType(cursor.getString(3));
                contact.setFloors(cursor.getString(4));
                contact.setBlocks(cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    // code to update the single contact
//    public int updateBuilding_Info(Building_Info contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_ROOMNO, contact.getPhoneNumber());
//        values.put(hey, contact.getEmail());
//        values.put(KEY_TYPE, contact.getNationality());
//        values.put(KEY_FLOORS, contact.getGender());
//        values.put(key_pass, contact.getPassword());
//
//        // updating row
//        return db.update(Table_Building_Info, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(contact.getID())});
//    }

    // Deleting single contact  
    public void deleteBuilding_Info(Building_Info contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_Building_Info, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }
    public void deleteAllBuilding_Info() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Table_Building_Info);
        db.close();
    }


    // Getting contacts Count  
    public int getBuilding_InfoCount() {
        String countQuery = "SELECT  * FROM " + Table_Building_Info;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count  
        return cursor.getCount();
    }


}