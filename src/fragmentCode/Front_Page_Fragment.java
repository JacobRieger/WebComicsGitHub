package fragmentCode;


import code.webcomicviewer.R;


import activityCode.Add_Comic;
import activityCode.Add_Comic_Webview;
import activityCode.View_Comics;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import asyncTasks.ComicUpdater;

public class Front_Page_Fragment extends Fragment implements OnClickListener {

	public Front_Page_Fragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_buttons,
				container, false);
		
		Button update     = (Button) rootView.findViewById(R.id.fUpdateAll);
		Button addComic   = (Button) rootView.findViewById(R.id.fAddComic);
		Button viewComics = (Button) rootView.findViewById(R.id.fViewAllComics);
		
		update.setOnClickListener(this);
		addComic.setOnClickListener(this);
		viewComics.setOnClickListener(this);
		

		
		return rootView;
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.fUpdateAll:
				
				Log.d("Front_Page", "Update all launched");
				ComicUpdater updater = new ComicUpdater(getActivity());
				updater.execute();
				break;
				
			case R.id.fViewAllComics:
				Intent intent = new Intent(getActivity(), View_Comics.class);
				getActivity().startActivity(intent);
				break;
				
			case R.id.fAddComic:
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Options");
				
				builder.setPositiveButton("Quality", new DialogInterface.OnClickListener() {                     
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	
		            	Intent intent = new Intent(getActivity(), Add_Comic.class);
						startActivity(intent);
		            } 
		        });
				
				builder.setNegativeButton("Quick", new DialogInterface.OnClickListener() {                     
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	Intent intent = new Intent(getActivity(), Add_Comic_Webview.class);
		            	
		            	startActivity(intent);
		            } 
		        });
				
				builder.show();
				break;
			
		}
	
		
	}

	
	

}
