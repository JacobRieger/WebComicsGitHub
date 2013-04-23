package code.webcomicviewer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicListAdapter extends BaseAdapter {
     
	private DataBaseHandler db;
	private static LayoutInflater inflater=null;
	private Context _context;
	private ArrayList<Bitmap> ComicBitmaps;

    
    public ComicListAdapter(Context context, Activity activity) {
        db = new DataBaseHandler(context);
        _context = context;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return db.getComicsCount();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        DataBaseHandler db = new DataBaseHandler(_context);
    	View vi = convertView;
        
        if(convertView == null) {
	        
	        vi = inflater.inflate(R.layout.item, null);
        }
        String comicName = db.getComicName(position+1);
        TextView text=(TextView)vi.findViewById(R.id.text);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(comicName);
        
        
        
        /*text.setText(db.getComicName(position+1));
        byte[] bytes = db.getComicBitmapBytes(position+1);
        image.setImageBitmap(Bitmap.createBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),
        		0, 0, 75, 75));*/
        
        
        ComicLoader loader = new ComicLoader(image, _context, true);
        loader.execute(comicName);
        
        
      
        return vi;
    }
}
