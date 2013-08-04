package asyncTasks;

import comicCode.Comic;

import dataCode.DataBaseHandler;
import android.content.Context;
import android.os.AsyncTask;

public class DataBaseUpdater extends AsyncTask<Integer, Void, Void> {

	private DataBaseHandler db;
	private Context         context;
	
	public DataBaseUpdater(Context contexT)
	{
		context  = contexT;
	}
	

	@Override
	protected Void doInBackground(Integer... position) {
		/**
		db = new DataBaseHandler(context);
		
		if (db.getComicUpdated(position[0])) 
		{
			Comic current = db.getComic(position[0]);
			current.setUpdated(false);
			db.updateComic(current);
		}
		db.close();
		**/
		return null;
	}
	
}
