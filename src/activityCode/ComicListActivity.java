package activityCode;

import java.util.ArrayList;

import code.webcomicviewer.ComicDetailActivity;
import code.webcomicviewer.ComicDetailFragment;
import code.webcomicviewer.ComicEditActivity;
import code.webcomicviewer.ComicListFragment;
import code.webcomicviewer.EditComicFragment;
import code.webcomicviewer.IntentServiceUpdater;
import code.webcomicviewer.R;
import code.webcomicviewer.UpdateReceiver;
import dataCode.DataBaseHandler;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import asyncTasks.ComicsInitializer;

/**
 * An activity representing a list of Comics. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ComicDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ComicListFragment} and the item details (if present) is a
 * {@link ComicDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ComicListFragment.Callbacks} interface to listen for item selections.
 */
public class ComicListActivity extends FragmentActivity implements
		ComicListFragment.Callbacks {
	

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private boolean comicVisible = false;
	private String currentComic = "unset";
	private UpdateReceiver receiver;
	private AlarmManager alarmManager;
	PendingIntent pi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		//Debugging the DataBase
		DataBaseHandler db = new DataBaseHandler(this);
		Log.d("DataBase", String.valueOf(db.getComicsCount()));
		
		//TODO This needs to be commented out before release
		if(db.getComicsCount() < 1)
		{
			ComicsInitializer initializer = new ComicsInitializer(this);
			initializer.execute();
		}
		
		
		ArrayList<String> Names =  db.getAllComicNames();
		
		for(int i = 0; i < Names.size(); i++)
		{
			Log.d("DataBase", String.valueOf(i) + " " + Names.get(i));
		}
		
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			if(extras.get("Removed") != null)
			{
				currentComic = "none";
				
			}
		}
		
		setContentView(R.layout.activity_comic_list);
		
		if(savedInstanceState != null)
		{
			comicVisible = savedInstanceState.getBoolean("comicVisible");
			currentComic = savedInstanceState.getString("currentComic");
		}
		
		if (findViewById(R.id.comic_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ComicListFragment) getSupportFragmentManager().findFragmentById(
					R.id.comic_list)).setActivateOnItemClick(true);
			
		}
		
		pi = PendingIntent.getBroadcast( this, 0, new Intent(UpdateReceiver.ACTION_RESP),
				0 );
		
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		unregisterReceiver(receiver);
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(alarmManager != null) alarmManager.cancel(pi);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		IntentFilter filter = new IntentFilter(UpdateReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new UpdateReceiver();
        registerReceiver(receiver, filter);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putBoolean("comicVisible", comicVisible);
	  savedInstanceState.putString("currentComic", currentComic);
	  // etc.
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();

	    inflater.inflate(R.menu.activity_twopane, menu);
	   
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		menu.clear();
		if(!comicVisible)
		{
			inflater.inflate(R.menu.activity_twopane, menu);
		}
		else
		{
			inflater.inflate(R.menu.activity_twopane_selected, menu);
		}
		
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
		
		case R.id.Settings:
			//Intent intent2 = new Intent(this, SettingsActivity.class);
			//startActivity(intent2);
			break;
		
		case R.id.ViewAltText:
			DataBaseHandler db = new DataBaseHandler(this);
			String alt = db.getComic(currentComic).getAltText();
			Toast.makeText(this, alt, Toast.LENGTH_LONG).show();
			break;
		
		case R.id.ViewBrowser:
			DataBaseHandler db2 = new DataBaseHandler(this);
			String Url = db2.getComic(currentComic).getUrl();
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
    		startActivity(browserIntent);
			break;
		case R.id.mfUpdate:
			
			Log.d("Front_Page", "Update all launched");
			//ComicUpdater updater = new ComicUpdater(this);
			//updater.execute();
			Intent updateIntent = new Intent(this, IntentServiceUpdater.class);
			startService(updateIntent);
			//alarmManager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 
				//	SIXTY_SECONDS, pi );
			
			break;
			
		case R.id.fViewAllComics:
			Intent intent = new Intent(this, View_Comics.class);
			startActivity(intent);
			break;
			
		case R.id.mfAddComic:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Options");
			
			builder.setPositiveButton("Quality", new DialogInterface.OnClickListener() {                     
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	Intent intent = new Intent(getApplicationContext(), Add_Comic.class);
					startActivity(intent);
	            } 
	        });
			
			builder.setNegativeButton("Quick", new DialogInterface.OnClickListener() {                     
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	Intent intent = new Intent(getApplicationContext(), Add_Comic_Webview.class);
	            	
	            	startActivity(intent);
	            } 
	        });
			
			builder.show();
			break;
		}
		return false;
	}
	
	

	/**
	 * Callback method from {@link ComicListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ComicDetailFragment.ARG_ITEM_ID, id);
			//Log.d("onItemSelected", id);
			ComicDetailFragment fragment = new ComicDetailFragment();
			fragment.setArguments(arguments);
			
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.comic_detail_container, fragment).commit();
			
			currentComic = id;
			comicVisible = true;
			invalidateOptionsMenu();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ComicDetailActivity.class);
			detailIntent.putExtra(ComicDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public void onItemEditSelected(String id)
	{
		if (mTwoPane)
		{
			Bundle arguments = new Bundle();
			arguments.putString(ComicDetailFragment.ARG_ITEM_ID, id);
			//Log.d("onItemSelected", id);
			EditComicFragment fragment = new EditComicFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.comic_detail_container, fragment).commit();
		}
		else
		{
			Intent editIntent = new Intent(this, ComicEditActivity.class);
			editIntent.putExtra(ComicDetailFragment.ARG_ITEM_ID, id);
			startActivity(editIntent);
		}
	}
}
