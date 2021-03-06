package activityCode;


import java.util.ArrayList;

import code.webcomicviewer.R;

import comicCode.Comic;
import dataCode.BookmarkList;
import dataCode.DataBaseHandler;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import asyncTasks.ComicLoader;

public class View_Comics extends FragmentActivity implements OnLongClickListener {

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
    //public static ArrayList<Comic> Comics;
    public static ArrayList<String> ComicNames;
    public BookmarkList Bookmarks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comics);
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
              
        
    	//This is for when the app is destroyed
    	//Loads all comics from the database to the current activity
    	//Comics = db.getAllComics();
        ComicNames = db.getAllComicNames();
        //Log.d("OnCreateFinished", Integer.toString(Comics.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view__comics, menu);
        
        // SubMenu GoTo = menu.addSubMenu("Select Comic");
        // MenuItem select = menu.findItem(R.id.selection);
        SubMenu GoTo = menu.findItem(R.id.selection).getSubMenu();
        for(int i = 0; i < ComicNames.size(); i++)
        {
        	//Set the button names in the submenus
        	//Can't have them both be the same, still need to investigate
        	GoTo.add((ComicNames.get(i)));
        	//select.getSubMenu().add(ComicNames.get(i));
        }
        
        
        
        return super.onCreateOptionsMenu(menu);
        
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	
    	
    	return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	
    	//DataBaseHandler db = new DataBaseHandler(this);
    	String Title = item.getTitle().toString();
    	
    	for(int i = 0; i < ComicNames.size(); i++)
    	{
    		if(Title.equals((ComicNames.get(i))))
    		{
    			//Sets our view to the selected comic
    			mViewPager.setCurrentItem(i);
    		}
    	}
        
        return super.onOptionsItemSelected(item);
        
    }
 
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	
    	
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
            return fragment;
        }

        @Override
        public int getCount() {
        	//DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        	return ComicNames.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	//This sets the top of the page to the comics name
        	//DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        	return ComicNames.get(position);
        }
    }
   
    public static class ComicFragment extends Fragment{
        public ComicFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	//Our DoubleTap listener
        	//We create our new imageview to be displayed
        	ImageView imageView = new ImageView(getActivity());
        	//WeakReference<ImageView> WeakImageView = new WeakReference<ImageView>(imageView);
        	//ImageViewTouch imageView = new ImageViewTouch(getActivity(), null);
        	//Get the arguments
        	//Position of comicList
            Bundle args = getArguments();
            //Set the onClickListener
            
            
            ComicLoader loader = new ComicLoader(imageView, getActivity());
            loader.execute(ComicNames.get(args.getInt(ARG_SECTION_NUMBER)));
            		//.getComicBitmap());
            //Comic temp = Comics.get(args.getInt(ARG_SECTION_NUMBER));
            //Log.d("Comic", temp.getName() + " " + temp.getImageUrl());
            //Log.d("OnCreateView", "ComicLoader executed");
            //ComicLoader Loading = new ComicLoader(WeakImageView, 
            //	ComicNames.get(args.getInt(ARG_SECTION_NUMBER)), getActivity());
            
            //Loading.execute();
            //OnLong click for enabling zoom and pan 
            imageView.setOnLongClickListener((View_Comics) getActivity());
            return imageView;
        	}

        
    }
    
	@Override
	public boolean onLongClick(View view) {
		Log.d("OnLongClick", view.toString());
		DataBaseHandler db = new DataBaseHandler(this);
		
		if (view.getClass() == ImageView.class) {
			//Our current comic
			Comic Selected = db.getComic(mSectionsPagerAdapter.getPageTitle(mViewPager.getCurrentItem()).toString());
			//We pass extras to know which comic to view
			Intent intent = new Intent(this, View_Comic.class);
			intent.putExtra("ImageUrl", Selected.getImageUrl());
			intent.putExtra("Name", Selected.getName());
			intent.putExtra("Url", Selected.getUrl());
			startActivity(intent);
		}
		//Log.d("ViewComics - LongClick", String.valueOf(mViewPager.getCurrentItem()));
		return false;
	}
}
