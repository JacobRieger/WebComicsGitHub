package code.webcomicviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import activityCode.ComicListActivity;
import asyncTasks.DataBaseUpdater;
import dataCode.DataBaseHandler;



/**
 * A list fragment representing a list of Comics. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ComicDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ComicListFragment extends ListFragment implements OnItemLongClickListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
		public void onItemEditSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}

		@Override
		public void onItemEditSelected(String id) {
			
			
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ComicListFragment() {
	}
	
	private String[] comicNames;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Here is where I put my simple adapter
		
		//Our database of comics
        DataBaseHandler db = new DataBaseHandler(getActivity());
        
        //Loading all the comics cost too much memory
        //So we only load the names for display purposes
        ArrayList<String> ComicNames = db.getAllComicNames();
        
        //Builds our data set to hand over to the adapter
        comicNames = new String[ComicNames.size()];
        for(int i = 0; i < ComicNames.size(); i++)
        {
        	comicNames[i] = ComicNames.get(i);
        }
        
        
        //Our simple adapter to display only names
		//@SuppressWarnings("unused")
		ArrayAdapter<String> comicAdapter = new ArrayAdapter<String>(getActivity(),
        		android.R.layout.simple_list_item_1, comicNames);
		
		//final Activity activity = getActivity();
		//ComicListAdapter cla = new ComicListAdapter(activity,activity);
        
        setListAdapter(comicAdapter);
        //setListAdapter(cla);
		 
		/**
		setListAdapter(new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null, new String[] {
                        Comic2.COL_NAME, Comic2.COL_URL,
                        Comic2.COL_SINCE, Comic2.COL_IMAGE,
                        Comic2.COL_ALT, Comic2.COL_UPDATE, Comic2.COL_BITMAP}, new int[] { 
															android.R.layout.simple_list_item_1 }, 0));
		
        // Load the content
        getLoaderManager().initLoader(0, null, new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            	
            	Loader<Cursor> result = new CursorLoader(getActivity(),
                        ComicContentProvider.URI_COMICS, Comic2.FIELDS, Comic2.COL_NAME, null,
                        null);
            	System.out.println(result.dataToString(data)));
            	
                return result;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
                ((SimpleCursorAdapter) getListAdapter()).swapCursor(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
            }
        });
        **/
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	      getListView().setOnItemLongClickListener(this);
	      
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		DataBaseHandler db = new DataBaseHandler(getActivity());
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		//mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
		Log.d("onListItemCLick", String.valueOf(position));
		TextView tv = (TextView) view;
		tv.setTextColor(Color.GRAY);
		
		DataBaseUpdater DBU = new DataBaseUpdater(getActivity());
		DBU.execute(position+1);
		
		//DatabaseHandler2 db = new DatabaseHandler2(getActivity());
		
		
		
		//if(name != null)
		//{
			//Log.d("onlistItemClick", "Comic clicked is " + name);
		//	mCallbacks.onItemSelected(name);
		//}
		//e/lse
		//{
			//Log.d("onlistItemClick", "name is null");
			//mCallbacks.onItemSelected(name);
		//}
		
		mCallbacks.onItemSelected((String)getListView().getItemAtPosition(position));
		db.close();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
			final long id) {
		//This is if you long click onto an item to show two options
		//Edit Comic || Remove
		
		TextView textview = (TextView) view;
		final String name = textview.getText().toString();
		Log.d("onItemLongClick", "Comic name = " + name);
		final Context context = view.getContext();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(name);
		
		builder.setPositiveButton("Edit Comic", new DialogInterface.OnClickListener() {                     
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	//Intent intent = new Intent(context, Edit_Comic.class);
				//intent.putExtra("Comic", name);
				//startActivity(intent);
            	mCallbacks.onItemEditSelected((String)getListView().getItemAtPosition(position));
            } 
        });
		
		builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {                     
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	DataBaseHandler db = new DataBaseHandler(context);
            	db.deleteComic(db.getComic(name));
            	Intent intent = new Intent(context, ComicListActivity.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			intent.putExtra("Removed", position+1);
            	startActivity(intent);
            } 
        });
		
		builder.show();
		
		return false;
	}
}
