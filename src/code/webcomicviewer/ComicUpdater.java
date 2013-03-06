package code.webcomicviewer;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ComicUpdater extends AsyncTask<Void, Void, Void> {

	Context context;
	ProgressDialog pdialog;
	DataBaseHandler db;
	
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
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.setMax(db.getAllComics().size());
		pdialog.setMessage("Updating Comics");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		ArrayList<Comic> Comics = db.getAllComics();
		for(int i = 0; i < Comics.size(); i++)
		{
			Comic current = Comics.get(i);
			current.Update();
			db.updateComic(current);
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
