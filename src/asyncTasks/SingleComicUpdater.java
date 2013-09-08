package asyncTasks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import activityCode.ComicListActivity;
import comicCode.Comic;
import dataCode.DataBaseHandler;

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
		pdialog.setMessage("Saving Changes");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
	
		db.updateComic(comic);
		db.close();
			
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		pdialog.dismiss();
		Intent intent = new Intent(context, ComicListActivity.class);
		context.startActivity(intent);
	}

}
