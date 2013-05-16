package asyncTasks;

import java.util.ArrayList;


import comicCode.Comic;
import dataCode.DataBaseHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ComicUpdater extends AsyncTask<Void, Void, Void> {

	Context context;
	ProgressDialog pdialog;
	DataBaseHandler db;
	Bitmap ComicBitmap;
	
	public ComicUpdater(Context ourcontext)
	{
		context = ourcontext;
		db = new DataBaseHandler(context);
	}
	
	@Override
	protected void onPreExecute()
	{
		pdialog = new ProgressDialog(context);
		pdialog.setCancelable(false);
		//pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.setMax(db.getComicsCount());
		pdialog.setMessage("Updating Comics");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		System.out.println("UPDATING " + db.getComicsCount() + " COMICS");
		ArrayList<String> ComicNames = db.getAllComicNames();
		long st = System.currentTimeMillis();
		for(int i = 0; i < db.getComicsCount(); i++)
		{
			Comic current = db.getComic(ComicNames.get(i));
			if(current.modified())
			{
				current.Update();
				db.updateComic(current);
			}
			else
			{
				
			}
			pdialog.incrementProgressBy(1);
		}
		
		Log.d("ComicUpdater" , "Updated all comics in " + String.valueOf((System.currentTimeMillis() - st)/1000) + " seconds");
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		try
		{
			pdialog.dismiss();
		}
		catch(Exception e)
		{
			Toast alert = Toast.makeText(context, "Comic Updates Complete", Toast.LENGTH_LONG);
			alert.show();
		}
	}

}
