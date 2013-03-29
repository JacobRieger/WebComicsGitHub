package code.webcomicviewer;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Front_Page extends Activity implements OnClickListener, OnItemClickListener,
		OnItemLongClickListener {

	//protected ArrayList<Comic> Comics;
	protected ArrayList<String> ComicNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front__page);
        
        Button viewComics = (Button) findViewById(R.id.viewComics);
        Button updateComics = (Button) findViewById(R.id.updateAll);
        Button addComic = (Button) findViewById(R.id.AddNewComicFront);
        ListView comicList = (ListView) findViewById(R.id.listView1);
        
        comicList.setOnItemClickListener(this);
        comicList.setOnItemLongClickListener(this);
        viewComics.setOnClickListener(this);
        updateComics.setOnClickListener(this);
        addComic.setOnClickListener(this);
        
        DataBaseHandler db = new DataBaseHandler(this);
        ComicNames = db.getAllComicNames();
        String[] comicNames = new String[ComicNames.size()];
        for(int i = 0; i < ComicNames.size(); i++)
        {
        	comicNames[i] = ComicNames.get(i);
        }
        
        ArrayAdapter<String> comicAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, comicNames);
        
        comicList.setAdapter(comicAdapter);
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
				Log.d("Add", "Add Comic Pushed");
	        	Intent intent = new Intent(this, Add_Comic.class);
	        	startActivity(intent);
	        	break;
			case R.id.updateAll:
				Log.d("Updateall", "Update all pushed");
				ComicUpdater updater = new ComicUpdater(this);
				updater.execute();
				break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Intent intent = new Intent(this, View_Comic.class);
		intent.putExtra("Name", ComicNames.get(position));
		//intent.putExtra("ImageUrl", Comics.get(position).getImageUrl());
		//intent.putExtra("Url", Comics.get(position).getUrl());
		startActivity(intent);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		
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
            	Intent intent = getIntent();
            	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            	startActivity(intent);
            } 
        });
		
		builder.show();
		
		return false;
	}
}
