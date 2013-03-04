package code.webcomicviewer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity implements OnLongClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public static ArrayList<Comic> Comics;
    public BookmarkList Bookmarks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Our database on the  phone
        DataBaseHandler db = new DataBaseHandler(this);
    
        // Create the adapter that will return a fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Here we set some of the Title colors / sizes
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(1, 25);
        pagerTitleStrip.setTextColor(Color.WHITE);
        pagerTitleStrip.setBackgroundColor(Color.BLACK);
           
        //Checking to see if we're adding a new comic
        Bundle extras = getIntent().getExtras();
        //If there are extras, they are from the Add Comic Activity
        if(extras != null)
        {
        	//Basic Values gotten from Add_Comic
        	String Name = extras.getString("Name");
        	String Url = extras.getString("Url");
        	String ImageUrl = extras.getString("ImageUrl");
        	//Create the new comic
        	Comic newComic = new Comic(Name, Url, ImageUrl);
        	//This insures that the comic image is loaded up
        	newComic.setUpdated(true);
        	//Adds the comic to the database, if it doesn't already exist
        	if(!db.doesComicExist(newComic))
        	{
        		System.out.println("Comic Added");
        		db.addComic(newComic);
        		Comics.add(newComic);
        	}
        }
        
        if(Comics.size() == 0)
        {
        	//This is for when the app is destroyed
        	//Loads all comics from the database to the current activity
        	Comics = db.getAllComics();
        	for(int i = 0; i < Comics.size(); i++)
            {
            	Comics.get(i).setUpdated(true);
            }
        }
        
        if (Bookmarks == null) {
        	
        	Bookmarks = new BookmarkList();
        	
			Cursor cursor = getContentResolver().query(Browser.BOOKMARKS_URI,
					null, null, null, null);
			cursor.moveToFirst();
			int titleIdx = cursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
			int urlIdx = cursor.getColumnIndex(Browser.BookmarkColumns.URL);
			int bookmark = cursor
					.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);
			while (cursor.isAfterLast() == false) {

				if (cursor.getInt(bookmark) > 0) {
					//Log.d("Adding Bookmark", cursor.getString(titleIdx));
					Bookmarks.add(new Bookmark(cursor.getString(titleIdx),
							cursor.getString(urlIdx)));
				}
				cursor.moveToNext();
			}
		}
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        //Adding two menu items that change at runtime
        //Allows to switch view
        SubMenu GoTo = menu.addSubMenu("GoTo");
        //Removes a comic from the list
        SubMenu Remove = menu.addSubMenu("Remove Comic");
        MenuItem BookMark = (MenuItem) menu.findItem(R.id.Add_Bookmark);
        SubMenu BookMarkSub = BookMark.getSubMenu();
        
        for(int i = 0; i < Comics.size(); i++)
        {
        	//Set the button names in the submenus
        	//Can't have them both be the same, still need to investigate
        	GoTo.add("Skip to ".concat(Comics.get(i).getName()));
        	Remove.add(Comics.get(i).getName());
        }
        for(int x = 0; x < Bookmarks.size(); x++)
        {
        	BookMarkSub.add(32, 0, 0, Bookmarks.get(x).getName());
        	Log.d("added", Bookmarks.get(x).getName());
        }
        
        return super.onCreateOptionsMenu(menu);
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	String Title = item.getTitle().toString();
    	
    	if(item.getGroupId() == 32)
    	{
    		Intent intent = new Intent(this, Add_Comic.class);
    		Bookmark current = Bookmarks.find(item.getTitle().toString());
    		intent.putExtra("Name", item.getTitle());
    		intent.putExtra("Url", current.getUrl());
    		startActivity(intent);
    		return true;
    	}
    	
    	for(int i = 0; i < Comics.size(); i++)
    	{
    		//Checking to see if item clicked was our remove comic button
    		if(Title.equals(Comics.get(i).getName()))
    		{
    			//Delete it from the Database and our current comic list
    			DataBaseHandler db = new DataBaseHandler(this);
    			db.deleteComic(Comics.get(i));
    			Comics.remove(i);
    			//Set our view back to the front
    			mViewPager.setCurrentItem(0);
    		}
    		if(Title.equals("Skip to ".concat(Comics.get(i).getName())))
    		{
    			//Sets our view to the selected comic
    			mViewPager.setCurrentItem(i);
    		}
    	}
    	
        switch (item.getItemId()) {
        case R.id.Update:
        	//This returns the comic that were currently viewing's position
        	int i = mViewPager.getCurrentItem();
        	//Async Task that updates the comic / imageView
        	ComicUpdater comicUpdater = new ComicUpdater(i, this);
        	comicUpdater.execute();
            return true;
            //bla
            
        case R.id.UpdateAll:
        	ComicUpdater updateAll;
        	//Uses the comicUpdater UpdateAll functionality
        	updateAll = new ComicUpdater(this, true);
        	updateAll.execute();
        	return true;
        case R.id.Add:
        	//Starts our Add_Comic Activity
        	Log.d("Add", "Add Comic Pushed");
        	Intent intent = new Intent(this, Add_Comic.class);
        	startActivity(intent);
        	return true;
        	
        case R.id.Add_Bookmark:
        	Log.d("Add", "Bookmarks");
        	
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	
    	List<Fragment> CFs;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            
            if(Comics == null)
            {
            	Comics = new ArrayList<Comic>();
            }
            if(CFs == null)
            {
            	//Our list of all comic Fragments
            	CFs = new ArrayList<Fragment>();
            }
        }
                
        public void removeFragment(Fragment item)
        {
        	CFs.remove(item);
        }
        
        public Fragment getFragment(int position)
        {
        	if(CFs.size() != 0)
        	{
        		return CFs.get(position);
        	}
        	return null;
        }

        @Override
        public Fragment getItem(int i) {
        	//Create our new fragment
            Fragment fragment = new ComicFragment();
            Bundle args = new Bundle();
            //We put into the bundle the section it's placed in
            args.putInt(ComicFragment.ARG_SECTION_NUMBER, i);
            //args.putString("Url", Comics.get(i).getImageUrl());
            fragment.setArguments(args);
            CFs.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return Comics.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	//This sets the top of the page to the comics name
        	return Comics.get(position).getName();
        }
    }
   
    public static class ComicFragment extends Fragment{
        public ComicFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";
        private ImageView IV;
        
        public ImageView getIV()
        {
        	return IV;
        }
        
        @Override
        public void onDestroy()
        {
        	super.onDestroy();
        	mSectionsPagerAdapter.removeFragment(this);
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	//Our DoubleTap listener
        	//We create our new imageview to be displayed
        	ImageView imageView = new ImageView(getActivity());
        	//ImageViewTouch imageView = new ImageViewTouch(getActivity(), null);
        	//Get the arguments
        	//Position of comicList
            Bundle args = getArguments();
            //Set the onClickListener
            //imageView.setOnClickListener(this);
            //This is the comic that will be shown in the position given
            Comic Current = Comics.get(args.getInt(ARG_SECTION_NUMBER));
    
            //This is what will download the image when needed
            
            imageDownloader downloader = new imageDownloader(imageView, Current, getActivity());
            downloader.execute();
            Log.d("onCreateView", "downloader exectuted");
           
            //Sets the imageview to the current bitmap
            imageView.setImageBitmap(Current.getComicBitmap());
            //Saves our IV for later reference
            IV = imageView;
            //OnLong click for enabling zoom and pan 
            imageView.setOnLongClickListener((MainActivity) getActivity());
            return imageView;
        }
             
        private class imageDownloader extends AsyncTask<Integer, Void, Bitmap>
        {
        	//Our Reference to the imageView
        	private final WeakReference<ImageView> imageViewReference;
        	//Our comic
            private Comic Current;
            private Context ourContext;
        	
        	public imageDownloader(ImageView imageView, Comic current, Context context)
        	{
        		//Basic constructor that sets our variables
        		imageViewReference = new WeakReference<ImageView>(imageView);
        		Current = current;
        		ourContext = context;
        	}
        	
        	@Override
        	protected void onPreExecute()
        	{
        		//If the comic changed, we set it to loading
        		if(Current.isUpdated())
        		{
        			Bitmap mybitmap = BitmapFactory.decodeResource(ourContext.getResources(), R.drawable.loading);
        			Current.setComicBitmap(mybitmap);
        		}
        	}

			@Override
			protected Bitmap doInBackground(Integer... params) {
			
				//This checks if we need to load a new image (networking)
				if(Current.isUpdated())
				{
					//If it is needed, we retrieve that image
					//This function then sets the new bitmap to the comic variable
					Current.retrieveImageBitmap();
					//We just loaded so this is now false
					Current.setUpdated(false);
				}
				//Return our newly found / or old and kept bitmap
				return Current.getComicBitmap();
			}
			
			@Override
		    protected void onPostExecute(Bitmap bitmap) {
				//This sets the imageview (if it still exists) to the bitmap returned from
				//doinBackground
		        if (imageViewReference != null && bitmap != null) {
		            final ImageView imageView = imageViewReference.get();
		            if (imageView != null) {
		                imageView.setImageBitmap(bitmap);
		                
		            }
		        }   
		    }
        }

		    }
    
    private class ComicUpdater extends AsyncTask<Void, Void, Void>
    {
    	//This is called to update all comics, or just the current one
    	int position; //This is when the single on is called
    	ComicFragment frag; //How we access the imageview
    	List<ComicFragment> ComicFrags = new ArrayList<ComicFragment>(); // our list for UpdateAll
    	Context ourContext;
    	boolean updateAll; //If we want to update all comics, true, otherwise false
    	
    	public ComicUpdater(int i, Context context)
    	{
    		position = i;
    		frag = (ComicFragment) mSectionsPagerAdapter.getFragment(position);
    		ourContext = context;
    	}
    	
    	public ComicUpdater(Context context, boolean UpdateAll)
    	{
    		updateAll = UpdateAll;
    		for(int i = 0; i < Comics.size(); i++)
    		{
    			frag = (ComicFragment) mSectionsPagerAdapter.getFragment(i);
    			ComicFrags.add(frag);
    		}
    		ourContext = context;
    	}
    	
    	@Override
    	protected void onPreExecute()
    	{
    		if (frag != null) {
				if (updateAll) {
					
					for (int i = 0; i < Comics.size(); i++) {
						//We get our loading bitmap to display while fetching image
						Bitmap mybitmap = BitmapFactory.decodeResource(
								ourContext.getResources(), R.drawable.loading);
						//Set our comic to the new bitmap
						Comics.get(i).setComicBitmap(mybitmap);
						
						//Set the imageview for that comic to the loading bitmap
						frag = ComicFrags.get(i);
						frag.getIV().setImageBitmap(Comics.get(i).getComicBitmap());
					}
				} else {
					//Set bitmap to loading
					Bitmap mybitmap = BitmapFactory.decodeResource(
							ourContext.getResources(), R.drawable.loading);
					//Set the comicbitmap to loading
					Comics.get(position).setComicBitmap(mybitmap);
					//Update our current view
					frag.getIV().setImageBitmap(
							Comics.get(position).getComicBitmap());
				}
			}
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			if(updateAll)
			{
				for(int i = 0; i < Comics.size(); i++)
				{
					//Update all comics
					Comics.get(i).Update();
					frag = ComicFrags.get(i);
					//frag.getIV().setImageBitmap(Comics.get(i).getComicBitmap());
				
				}
			}
			else
			{
				//Update current comic
				Comics.get(position).Update();
			}
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			//Update all imageview to new comic
			if(updateAll)
			{
				for(int i = 0; i < Comics.size(); i++)
				{
					frag = ComicFrags.get(i);
					frag.getIV().setImageBitmap(Comics.get(i).getComicBitmap());
				}
			}
			else
			{
				frag.getIV().setImageBitmap(Comics.get(position).getComicBitmap());
			}
	    }
    }

	@Override
	public boolean onLongClick(View view) {
		Log.d("OnLongClick", view.toString());
		
		if (view.getClass() == ImageView.class) {
			//Our current comic
			Comic Selected = Comics.get(mViewPager.getCurrentItem());
			//We pass extras to know which comic to view
			Intent intent = new Intent(this, View_Comic.class);
			intent.putExtra("ImageUrl", Selected.getImageUrl());
			intent.putExtra("Name", Selected.getName());
			startActivity(intent);
		}
		return false;
	}
}
