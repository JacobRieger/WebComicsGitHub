package activityCode;

import java.util.ArrayList;

import code.webcomicviewer.R;
import dataCode.DataBaseHandler;
//import dataCode.ComicListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import asyncTasks.ComicUpdater;

public class Front_Page extends Activity implements OnClickListener, OnItemClickListener,
		OnItemLongClickListener {

	//protected ArrayList<Comic> Comics; //Too much memory to load all
	protected ArrayList<String> ComicNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front__page);
        
        //All buttons (View Comics, Update All, Add New)
        Button viewComics   = (Button) findViewById(R.id.viewComics);
        Button updateComics = (Button) findViewById(R.id.updateAll);
        Button addComic     = (Button) findViewById(R.id.AddNewComicFront);
        Button addComicWV   = (Button) findViewById(R.id.AddNewComicWebView);
        
        //List of comics
        ListView comicList = (ListView) findViewById(R.id.listView1);
        
        //Set Click Listeners
        comicList.setOnItemClickListener(this);
        comicList.setOnItemLongClickListener(this);
        viewComics.setOnClickListener(this);
        updateComics.setOnClickListener(this);
        addComic.setOnClickListener(this);
        addComicWV.setOnClickListener(this);
        
        //Our database of comics
        DataBaseHandler db = new DataBaseHandler(this);
        
        //Loading all the comics cost too much memory
        //So we only load the names for display purposes
        ComicNames = db.getAllComicNames();
        
        //Builds our data set to hand over to the adapter
        String[] comicNames = new String[ComicNames.size()];
        for(int i = 0; i < ComicNames.size(); i++)
        {
        	comicNames[i] = ComicNames.get(i);
        }
        
        //Our simple adapter to display only names
       // @SuppressWarnings("unused")
		ArrayAdapter<String> comicAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, comicNames);
        
        //comicList.setAdapter(new ComicListAdapter(this,this));
        comicList.setAdapter(comicAdapter);
        //Log.d("Front Page", comicList.getAdapter().getView(0, null, null));
        //TextView text = (TextView) comicList.getAdapter().getView(0,null,null);
        //text.setTextColor(Color.GREEN);
       
   
        
    }
   

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_front__page, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()){
			case R.id.viewComics:
				Intent view = new Intent(this, View_Comics.class);
				startActivity(view);
				break;
			case R.id.AddNewComicFront:
				Log.d("Front_Page", "Add Comic launched");
	        	Intent intent = new Intent(this, Add_Comic.class);
	        	startActivity(intent);
	        	break;
			case R.id.updateAll:
				Log.d("Front_Page", "Update all launched");
				ComicUpdater updater = new ComicUpdater(this);
				updater.execute();
				break;
			case R.id.AddNewComicWebView:
				Log.d("Front_Page", "Add comic WebView launched");
				Intent intent2 = new Intent(this,Add_Comic_Webview.class);
				startActivity(intent2);
				
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		//This is our onItemClick for our comic list
		//Launches the comic viewer sending the name for loading
		Intent intent = new Intent(this, View_Comic.class);
		intent.putExtra("Name", ComicNames.get(position));
		startActivity(intent);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		//This is if you long click onto an item to show two options
		//Edit Comic || Remove
		
		TextView textview = (TextView) view;
		final String name = textview.getText().toString();
		final Context context = view.getContext();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Options");
		
		builder.setPositiveButton("Edit Comic", new DialogInterface.OnClickListener() {                     
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(context, Edit_Comic.class);
				intent.putExtra("Comic", name);
				startActivity(intent);
            } 
        });
		
		builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {                     
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	DataBaseHandler db = new DataBaseHandler(context);
            	db.deleteComic(db.getComic(name));
            	Intent intent = new Intent(context, Front_Page.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            	startActivity(intent);
            } 
        });
		
		builder.show();
		
		return false;
	}
}
