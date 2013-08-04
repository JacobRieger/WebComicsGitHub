package dataCode;

import java.util.List;

import comicCode.Comic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ComicListAdapter extends BaseAdapter {
     
	private DataBaseHandler       db;
	private Context               _context;
	private static LayoutInflater inflater=null;
	private List<String>          names;
	private int                   comicsCount;

    public ComicListAdapter(Context context, Activity activity) {
        
    	db = new DataBaseHandler(context);
        _context = context;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        names    = db.getAllComicNames();
        comicsCount = db.getComicsCount();
    }

    public int getCount() {
        return comicsCount;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
       
    	View vi;
        
    	
    	vi = inflater.inflate(android.R.layout.simple_list_item_1, null);
    	//db       = new DataBaseHandler(_context);
    	//Comic Current = db.getComic(position+1);
        //String comicName   = names.get(position+1);
        TextView text      = (TextView) vi.findViewById(android.R.id.text1);
        text.setText(names.get(position));
        //ComicNameLoader nameLoader = new ComicNameLoader(text, _context);
        //nameLoader.execute(position+1);
        /**
        if(Current.isUpdated())
        {
        	text.setTextColor(Color.WHITE);
        }
        else
        {
        	text.setTextColor(Color.GRAY);
        }
        **/
        return vi;
    }
}
