package asyncTasks;

import dataCode.DataBaseHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class ComicNameLoader extends AsyncTask<Integer, Void, Void> {

	private String   comicName;
	private TextView textView;
	private Context  context;
	
	public ComicNameLoader(TextView textview, Context conText, boolean blah)
	{
		textView  = textview;
		context   = conText;
	}
	
	@Override
	protected Void doInBackground(Integer... position) {
		
		//DataBaseHandler db = new DataBaseHandler(context);
       // comicName = db.getComicName(position[0]);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		textView.setText(comicName);
	}

	

}
