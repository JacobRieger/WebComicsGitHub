package asyncTasks;


import comicCode.Comic;
import dataCode.DataBaseHandler;

import activityCode.ComicListActivity;
import activityCode.Front_Page;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class SingleComicRetriever extends AsyncTask<Void,Void,Void>{

	private Comic ourComic;
	private Context context;
	private ProgressDialog pdialog;
	
	public SingleComicRetriever(Comic comic, Context appContext)
	{
		ourComic = comic;
		context = appContext;
	}
	
	@Override
	protected void onPreExecute()
	{
		pdialog = new ProgressDialog(context);
		pdialog.setCancelable(false);
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdialog.setMessage("Adding Comic...");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		
		
		DataBaseHandler db = new DataBaseHandler(context);
		
		ourComic.retrieveImageBitmap();
		ourComic.setUpdated(true);
		
		db.addComic(ourComic);
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		pdialog.dismiss();
		
		Intent intent = new Intent(context, ComicListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	
	}
	
	

	

}
