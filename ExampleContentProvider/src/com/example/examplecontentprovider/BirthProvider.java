package com.example.examplecontentprovider;
import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class BirthProvider extends ContentProvider {
	 static final String PROVIDER_NAME = "com.example.examplecontentprovider.Nomes";
	 static final String URL = "content://" + PROVIDER_NAME + "/friends";
	 static final Uri CONTENT_URI = Uri.parse(URL);
	   
	 static final String ID = "id";
	 static final String NAME = "name";
	 
	 static final int FRIENDS = 1;
	 static final int FRIENDS_ID = 2;
	 
	 DBHelper dbHelper;
	   
	 private static HashMap<String, String> BirthMap;
	 
	 static final UriMatcher uriMatcher;
	   static{
	      uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	      uriMatcher.addURI(PROVIDER_NAME, "friends", FRIENDS);
	      uriMatcher.addURI(PROVIDER_NAME, "friends/#", FRIENDS_ID);
	   }
	   
	   private SQLiteDatabase database;
	   static final String DATABASE_NAME = "Nomes";
	   static final String TABLE_NAME = "nameTable";
	   static final int DATABASE_VERSION = 1;
	   static final String CREATE_TABLE = 
	      " CREATE TABLE " + TABLE_NAME +
	      " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	      " name TEXT NOT NULL); ";
	 
	   
	   private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			 db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(DBHelper.class.getName(),
			        "Atualizando da versão " + oldVersion + " para a "
			            + newVersion + ". A versão antiga será destruida");
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
	        onCreate(db);
		}
		
	   }
	   
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();

	    if(database == null)
	    	return false;
	    else
	    	return true;	
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		 SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		 queryBuilder.setTables(TABLE_NAME);
	      
	      switch (uriMatcher.match(uri)) {
	      
	      case FRIENDS:
	    	  queryBuilder.setProjectionMap(BirthMap);
	         break;
	      case FRIENDS_ID:
	    	  queryBuilder.appendWhere( ID + "=" + uri.getLastPathSegment());
	         break;
	      default:
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      if (sortOrder == null || sortOrder == ""){
	         
	         sortOrder = NAME;
	      }
	      Cursor cursor = queryBuilder.query(database, projection, selection, 
	    		  selectionArgs, null, null, sortOrder);
	   
	      cursor.setNotificationUri(getContext().getContentResolver(), uri);

	      return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long row = database.insert(TABLE_NAME, "", values);
	      
		
	      if(row > 0) {
	         Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
	         getContext().getContentResolver().notifyChange(newUri, null);
	         return newUri;
	      }
	      throw new SQLException("Fail to add a new record into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		 int count = 0;
	      
	      switch (uriMatcher.match(uri)){
	      case FRIENDS:
	         count = database.update(TABLE_NAME, values, selection, selectionArgs);
	         break;
	      case FRIENDS_ID:
	         count = database.update(TABLE_NAME, values, ID + 
	                 " = " + uri.getLastPathSegment() + 
	                 (!TextUtils.isEmpty(selection) ? " AND (" +
	                 selection + ')' : ""), selectionArgs);
	         break;
	      default: 
	         throw new IllegalArgumentException("Unsupported URI " + uri );
	      }
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		
		 switch (uriMatcher.match(uri)){
	      case FRIENDS:
	    	  count = database.delete(TABLE_NAME, selection, selectionArgs);
	    	  break;
	      case FRIENDS_ID:
	      	  String id = uri.getLastPathSegment();	//gets the id
	          count = database.delete( TABLE_NAME, ID +  " = " + id + 
	                (!TextUtils.isEmpty(selection) ? " AND (" + 
	                selection + ')' : ""), selectionArgs);
	          break;
	      default: 
	          throw new IllegalArgumentException("Unsupported URI " + uri);
	      }
	      
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
		
		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)){
	      
	      case FRIENDS:
	         return "vnd.android.cursor.dir/vnd.example.friends";
	      
	      case FRIENDS_ID:
	         return "vnd.android.cursor.item/vnd.example.friends";
	      default:
	         throw new IllegalArgumentException("Unsupported URI: " + uri);
	      }
	}


}
