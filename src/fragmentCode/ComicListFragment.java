package fragmentCode;

import dataCode.DataBaseHandler;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ComicListFragment extends ListFragment {
	
		private DataBaseHandler db;
		private Context context;
	 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        /** Creating an array adapter to store the list of countries **/
	    	Context c = getActivity().getApplicationContext();
	    	
	        /** Setting the list adapter for the ListFragment */
	       // setListAdapter(adapter);
	 
	        return super.onCreateView(inflater, container, savedInstanceState);
	    }

}
