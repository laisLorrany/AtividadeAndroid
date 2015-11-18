package com.example.examplecontentprovider;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void deletarNomes (View view) {
		// delete all the records and the table of the database provider
		String URL = "content://com.example.examplecontentprovider.Nomes/friends";
	        Uri friends = Uri.parse(URL);
		int count = getContentResolver().delete(
				 friends, null, null);
		String countNum = "O total de: "+ count +" nome(s) foi deletado.";
		Toast.makeText(getBaseContext(), 
			      countNum, Toast.LENGTH_LONG).show();
		
	}
	
	 public void addNomes(View view) {
	      // Add a new birthday record
	      ContentValues values = new ContentValues();

	      values.put(BirthProvider.NAME, 
	           ((EditText)findViewById(R.id.name)).getText().toString());

	      Uri uri = getContentResolver().insert(
	    	   BirthProvider.CONTENT_URI, values);
	      
	      Toast.makeText(getBaseContext(), 
	    	   uri.toString() + " Nome Inserido!", Toast.LENGTH_LONG).show();
	   }


	   public void mostrarNomes (View view) {
	      // Show all the birthdays sorted by friend's name
	      String URL = "content://com.example.examplecontentprovider.Nomes/friends";
	      Uri friends = Uri.parse(URL);
	      Cursor c = getContentResolver().query(friends, null, null, null, "name");
	      String result = "Resultados:";
	      
	      if (!c.moveToFirst()) {
	    	  Toast.makeText(this, result+" Não há nenhum nome!", Toast.LENGTH_LONG).show();
	      }else{
	    	  do{
	            result = result + "\n" + c.getString(c.getColumnIndex(BirthProvider.NAME)) + 
	    	            " de Id: " +  c.getString(c.getColumnIndex(BirthProvider.ID));
	          } while (c.moveToNext());
	    	  Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	      }
	     
	   }
}