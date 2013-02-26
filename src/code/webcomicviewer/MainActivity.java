package code.webcomicviewer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if(extras != null)
        {
        	String Name;
        	String Url;
        	String ImageUrl;
        	Name = extras.getString("Name");
        	Url = extras.getString("Url");
        	ImageUrl = extras.getString("ImageUrl");
        	//These are all gotten from the activity Add_Comic
        	//Fix for bug when screen rotates, or it's created again, it will try to 
        	//add the comic again. This insures it only adds once
        	Comic newComic = new Comic(Name, Url, ImageUrl);
        	if(!Comics.contains(newComic))
        	{
        		Comics.add(newComic);
        	}
        }
        
        if(Comics.size() == 0)
        {
        	Comics.add(new Comic("DMN", "http://www.drmcninja.com", "http://drmcninja.com/comics/2013-02-23-25p80.jpg"));
        	Comics.add(new Comic("SMBC", "http://www.smbc-comics.com", "http://www.smbc-comics.com/comics/20130225.gif"));
        	Comics.add(new Comic("MA3", "http://www.menagea3.net", "http://zii.ma3comic.com/comics/mat20130223.png"));
        	Comics.add(new Comic("QC", "http://www.questionablecontent.net", "http://www.questionablecontent.net/comics/2392.png"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
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
        	for(int j = 0; j < Comics.size(); j++)
        	{
        		updateAll = new ComicUpdater(j, this);
        		updateAll.execute();
        	}
        	return true;
        case R.id.Add:
        	//Starts our Add_Comic Activity
        	Log.d("Add", "Add Comic Pushed");
        	Intent intent = new Intent(this, Add_Comic.class);
        	startActivity(intent);
        	return true;

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
            	CFs = new ArrayList<Fragment>();
            }
        }
                
        public void removeFragment(Fragment item)
        {
        	CFs.remove(item);
        }
        
        public Fragment getFragment(int position)
        {
        	return CFs.get(position);
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
            //if(Current.isnewFileNeeded()){
	            imageDownloader downloader = new imageDownloader(imageView, Current, getActivity());
	            downloader.execute();
	            Log.d("onCreateView", "downloader exectuted");
            //}
            imageView.setImageBitmap(Current.getComicBitmap());
            IV = imageView;
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
        		if(Current.isnewFileNeeded())
        		{
        			Bitmap mybitmap = BitmapFactory.decodeResource(ourContext.getResources(), R.drawable.loading);
        			Current.setComicBitmap(mybitmap);
        		}
        	}

			@Override
			protected Bitmap doInBackground(Integer... params) {
			
				//This checks if we need to load a new image (networking)
				if(Current.isnewFileNeeded())
				{
					//If it is needed, we retrieve that image
					//This function then sets the new bitmap to the comic variable
					Current.retrieveImageBitmap();
					//We just loaded so this is now false
					Current.setFileNeeded(false);
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
    	int position;
    	ComicFragment frag;
    	Context ourContext;
    	
    	public ComicUpdater(int i, Context context)
    	{
    		position = i;
    		frag = (ComicFragment) mSectionsPagerAdapter.getFragment(position);
    		ourContext = context;
    	}
    	
    	@Override
    	protected void onPreExecute()
    	{
    		Bitmap mybitmap = BitmapFactory.decodeResource(ourContext.getResources(), R.drawable.loading);
    		Comics.get(position).setComicBitmap(mybitmap);
    		frag.getIV().setImageBitmap(Comics.get(position).getComicBitmap());
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
	
			Comics.get(position).Update();
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			
			//System.out.println(mSectionsPagerAdapter.getFragment(position).getIV());
			frag.getIV().setImageBitmap(Comics.get(position).getComicBitmap());
	    }
    }

	@Override
	public boolean onLongClick(View view) {
		Log.d("OnLongClick", view.toString());
		
		if (view.getClass() == ImageView.class) {
			Log.d("OnLongClick", "About to start new activity");
			Comic Selected = Comics.get(mViewPager.getCurrentItem());
			Intent intent = new Intent(this, View_Comic.class);
			intent.putExtra("ImageUrl", Selected.getImageUrl());
			intent.putExtra("Name", Selected.getName());
			startActivity(intent);
		}
		return false;
	}
}
