package code.webcomicviewer;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class SingleComicUpdater extends AsyncTask<Void, Void, Void> {

	Context context;
	ProgressDialog pdialog;
	DataBaseHandler db;
	Bitmap ComicBitmap;
	Comic comic;
	
	public SingleComicUpdater(Context ourcontext, Comic comic_)
	{
		context = ourcontext;
		db = new DataBaseHandler(context);
		comic = comic_;
	}
	
	@Override
	protected void onPreExecute()
	{
		pdialog = new ProgressDialog(context);
		pdialog.setCancelable(false);
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdialog.setMessage("Updating Comic");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		
		Comic current = comic;
		if(current.modified())
		{
			current.Update();
			db.updateComic(current);
		}
		else
		{
			
		}
			
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		pdialog.dismiss();
	}

}
