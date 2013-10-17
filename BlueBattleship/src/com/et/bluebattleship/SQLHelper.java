package com.et.bluebattleship;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "devices"; 
	private static final int DB_VERSION = 1;
	
	
	public SQLHelper(Context context) { 
		super(context, DB_NAME, null, DB_VERSION);
	}
	
		@Override	
		public void onCreate(SQLiteDatabase db) {
			String sql = ""; 
			sql += "CREATE TABLE device ("; 
			sql += " _id INTEGER PRIMARY KEY,"; 
			sql += " name,";
			sql += " address ";
			sql += ")"; 
			db.execSQL(sql); 
			
		
			
			
		}
		@Override 
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

}
}