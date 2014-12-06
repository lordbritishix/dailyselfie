package com.quitevis.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private static final int REQUEST_SELFIE = 0;
	public static final String INTENT_PARAM_SELFIE_URI = "selfie_uri";

	private Uri fileUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SelfieAdapter adapter = new SelfieAdapter(getSource(), this);
		setListAdapter(adapter);
		
		Intent myIntent = new Intent(this, AlarmNotificationReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,0);
		
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, 1L, 120000L, pendingIntent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, FullSelfieActivity.class);
		intent.putExtra(INTENT_PARAM_SELFIE_URI, (Uri)((SelfieAdapter)getListAdapter()).getItem(position));
		startActivity(intent);
	}
	
	private	List<Uri> getSource() {
		List<Uri> source = new ArrayList<>();
		File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		
		File[] files = mediaStorageDir.listFiles();
		
		for (File file : files) {
			source.add(Uri.fromFile(file));
		}
		
		return source;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_take_selfie) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				fileUri = getOutputMediaFile(FileColumns.MEDIA_TYPE_IMAGE);
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			    startActivityForResult(intent, REQUEST_SELFIE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/** Create a File for saving an image or video 
	 * @throws IOException */
	private Uri getOutputMediaFile(int type) throws IOException {
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSSS").format(new Date());
	    File image = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		
		return Uri.fromFile(image);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_SELFIE:
			handleRequestSelfie(resultCode, data);
			break;
		default: 
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void handleRequestSelfie(int resultCode, Intent data) {
		switch(resultCode) {
		case RESULT_OK:
			((SelfieAdapter)getListAdapter()).add(fileUri);
			((SelfieAdapter)getListAdapter()).notifyDataSetChanged();

			Toast.makeText(this, "Selfie saved to " + fileUri.getLastPathSegment(), Toast.LENGTH_LONG).show();

			break;
		case RESULT_CANCELED:
			break;
			
		default:
			Toast.makeText(this, "There was an error in taking your selfie", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
