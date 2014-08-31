package com.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelp extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "LOCATIONS";

	static final String LOCATION_TABLE = "Location_Table";
//	static final String TABLE_NAME_LOT_LIST = "LotList";

	// Contacts Table Columns names
	public DataBaseHelp(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		// 1. PakingTable
		String Create_Loctable = "CREATE TABLE IF NOT EXISTS LOCATION_TABLE("
				+ "l_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"latcolomn text not null ,"
				+" longcolomn text NUL ,"
				+"address text not null ,"
				+ "time text not null);";//
		db.execSQL(Create_Loctable);

//		String CREATE_C_TABLE = "CREATE TABLE " +LOCATION_TABLE
//				+ "(" + "l_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "latcolomn text NULL ,"
//				+ "longcolomn text NULL,"
//				+")";
	//	db.execSQL(CREATE_C_TABLE);

		

	}

	public void AddLATLONG(String Lat, String Long,String Time,String Address) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("latcolomn", Lat);
		values.put("longcolomn",Long);
		values.put("time",Time);
		values.put("address",Address);
		
		db.insert(LOCATION_TABLE, null, values);
		
		db.close(); // Closing database connection
	}

	public Cursor Get_Curser_of_latlong() {
	Cursor cursor = null;
	try {

		// Select All Query
		String selectQuery = "SELECT  * FROM "+LOCATION_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		cursor = db.rawQuery(selectQuery, null);

		// return contact list

	} catch (Exception e) {
		// TODO: handle exception
		Log.e("all_contact", "" + e);
	}
	return cursor;
}
//	public Cursor Get_Curser_of_CurrencyList() {
//		Cursor cursor = null;
//		try {
//
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_NAME_CURRENCY_LIST;
//
//			SQLiteDatabase db = this.getWritableDatabase();
//			cursor = db.rawQuery(selectQuery, null);
//
//			// return contact list
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e("all_contact", "" + e);
//		}
//
//		return cursor;
//	}

//	public void Delete_Lot(String string) {
//
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		// db.delete(CART, CART_ID + " = ?", new String[] { "0" });
//		// Cursor c=db.rawQuery("SELECT * FROM c WHERE cname = '"+string+"'",
//		// null);
//
//		db.delete(TABLE_NAME_LOT_LIST, "LotList" + " = ?",
//				new String[] { string });
//
//		db.close();
//	}

//	public int Update_Lot(String id, String LotName, String LotCapacity) {
//		// TODO Auto-generated method stub
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//
//		values.put("LotList", LotName);
//		values.put("LotCapacity", LotCapacity);
//
//		// System.out.println("upad"+db.update(TABLE_NAME_LOT_LIST, values,
//		// "LotList" + " = ?",
//		// new String[] {LotName}));
//		// updating row
//		return db.update(TABLE_NAME_LOT_LIST, values, "LotList" + " = ?",
//				new String[] { id });
//	}

//	public Cursor Get_Curser_of_Lot() {
//		Cursor cursor = null;
//		try {
//
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_NAME_LOT_LIST;
//
//			SQLiteDatabase db = this.getWritableDatabase();
//			cursor = db.rawQuery(selectQuery, null);
//
//			// return contact list
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e("all_contact", "" + e);
//		}
//
//		return cursor;
//	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed

		db.execSQL("DROP TABLE IF EXISTS " +LOCATION_TABLE);

		// Create tables again
		onCreate(db);
	}

}