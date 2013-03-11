package code.webcomicviewer;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

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
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		pdialog.dismiss();
	}

}
