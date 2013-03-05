package code.webcomicviewer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Front_Page extends Activity implements OnClickListener, OnItemClickListener,
		OnItemLongClickListener {

	protected ArrayList<Comic> Comics;
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
        Comics = db.getAllComics();
        String[] comicNames = new String[Comics.size()];
        for(int i = 0; i < Comics.size(); i++)
        {
        	comicNames[i] = Comics.get(i).getName();
        }
        
        ArrayAdapter<String> comicAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, comicNames);
        
        comicList.setAdapter(comicAdapter);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	ListView comicList = (ListView) findViewById(R.id.listView1);
    	DataBaseHandler db = new DataBaseHandler(this);
        Comics = db.getAllComics();
        String[] comicNames = new String[Comics.size()];
        for(int i = 0; i < Comics.size(); i++)
        {
        	comicNames[i] = Comics.get(i).getName();
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
				Intent view = new Intent(this, MainActivity.class);
				startActivity(view);
				break;
			case R.id.AddNewComicFront:
				Log.d("Add", "Add Comic Pushed");
	        	Intent intent = new Intent(this, Add_Comic.class);
	        	startActivity(intent);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Intent intent = new Intent(this, View_Comic.class);
		intent.putExtra("Name", Comics.get(position).getName());
		intent.putExtra("ImageUrl", Comics.get(position).getImageUrl());
		intent.putExtra("Url", Comics.get(position).getUrl());
		startActivity(intent);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Dialog dialog = new Dialog(this);
		Button Edit = new Button(this);
		Edit.setText("Edit Comic Info");
		dialog.addContentView(Edit, new ViewGroup.LayoutParams(-1,-1));
		dialog.show();
		return false;
	}
}
