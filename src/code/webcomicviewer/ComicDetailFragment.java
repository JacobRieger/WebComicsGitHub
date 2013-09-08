package code.webcomicviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activityCode.ComicListActivity;
import comicCode.Comic;
import dataCode.DataBaseHandler;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A fragment representing a single Comic detail screen. This fragment is either
 * contained in a {@link ComicListActivity} in two-pane mode (on tablets) or a
 * {@link ComicDetailActivity} on handsets.
 */
public class ComicDetailFragment extends Fragment {
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
	public ComicDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			Log.d("ComicDeatilFragment", "Loading new comic data");
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			DataBaseHandler db = new DataBaseHandler(getActivity());
			Log.d("ComicDeatilFragment", getArguments().getString(ARG_ITEM_ID));
			mItem = db.getComic(getArguments().getString(ARG_ITEM_ID));
			
			
			//Original mItem setting
			//mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			//	ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comic_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			
			//System.out.println("mItem is not null");
			
			//((TextView) rootView.findViewById(R.id.comic_detail))
				//	.setText(mItem.getName());
			((ImageViewTouch) rootView.findViewById(R.id.ImageView01))
					.setImageBitmap(mItem.getComicBitmap());
		}
		
		return rootView;
	}
}
