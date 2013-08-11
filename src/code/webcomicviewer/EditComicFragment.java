package code.webcomicviewer;

import java.util.Date;

import comicCode.Comic;

import dataCode.DataBaseHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import asyncTasks.SingleComicUpdater;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link EditComicFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link EditComicFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class EditComicFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Comic mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public EditComicFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			Log.d("EditComicFragment", "Loading new comic data");
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			DataBaseHandler db = new DataBaseHandler(getActivity());
			Log.d("ComicDeatilFragment", getArguments().getString(ARG_ITEM_ID));
			mItem = db.getComic(getArguments().getString(ARG_ITEM_ID));
			
			
			//String[] comicData = new String[] {"Name", "URL", "HtmlImageTag URL", "Alt Text"};
			
			//ArrayAdapter<String> comicAdapter = new ArrayAdapter<String>(getActivity(),
	        		//android.R.layout.simple_list_item_1, comicData);
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit_comic,
				container, false);

	
		if (mItem != null) {
			
			//Our Comic fields to populate
			
			final TextView name     = (TextView) rootView.findViewById(R.id.nameValueFragment);
			final TextView url      = (TextView) rootView.findViewById(R.id.urlValueFragment);
			final TextView imageurl = (TextView) rootView.findViewById(R.id.imageValueFragment);
			final TextView altText  = (TextView) rootView.findViewById(R.id.altTextValueFragment);
			final TextView updated  = (TextView) rootView.findViewById(R.id.updatedSinceValueFragment);
			
			name    .setText(mItem.getName());
			url     .setText(mItem.getUrl());
			imageurl.setText(mItem.getImageUrl());
			altText .setText(mItem.getAltText());
			
			Date updatedDate = new Date(Long.valueOf(mItem.getUpdatedSince()));
			
			updated .setText(updatedDate.toString());
			
			Button saveChanges = (Button) rootView.findViewById(R.id.saveChanges);
			
			saveChanges.setOnClickListener(new View.OnClickListener() {                     
	    
				@Override
				public void onClick(View v) {
					
					mItem.setName(name.getText().toString());
					mItem.setUrl(url.getText().toString());
					mItem.setImageUrl(imageurl.getText().toString());
					mItem.setAltText(altText.getText().toString());
					
					SingleComicUpdater Updater = new SingleComicUpdater(getActivity(),mItem);
					Updater.execute();
				} 
	        });
			
			
			
					
		}
		
		return rootView;
	}
}
