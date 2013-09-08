package code.webcomicviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import activityCode.ComicListActivity;
import comicCode.Comic;
import dataCode.DataBaseHandler;

/**
 * An activity representing a single Comic detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ComicListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ComicDetailFragment}.
 */
public class ComicDetailActivity extends FragmentActivity {

	String ImageUrl;
	String Name;
	String Url;
	Comic  viewed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setTheme(R.style.CustomTheme2); // Set the custom theme which has the action bar
		
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_comic_detail);
		
		//System.out.println(getTheme());
		
		
		
		
		DataBaseHandler db = new DataBaseHandler(this);
        Bundle extras = getIntent().getExtras();
        Name = extras.getString(ComicDetailFragment.ARG_ITEM_ID);
        Log.d("ComicDetailActivity","Getting comic named : " + Name);
        viewed = db.getComic(Name);
        Url  = viewed.getUrl();
        
        Log.d("ComicDetail Activity", "Comic name is " + Name);

		// Show the Up button in the action bar.
		//This was here originally, got null reference exception
		//getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ComicDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(ComicDetailFragment.ARG_ITEM_ID));
			
			ComicDetailFragment fragment = new ComicDetailFragment();
			
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.comic_detail_container, fragment).commit();
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view__comic, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					ComicListActivity.class));
			return true;
			
		case R.id.ViewAltText:
    		Toast.makeText(this, viewed.getAltText(), Toast.LENGTH_LONG).show();
    		break;
    		
    	case R.id.ViewBrowser:
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
    		startActivity(browserIntent);
    		break;
    	
    	
    		
		}
		return super.onOptionsItemSelected(item);
	}
}
