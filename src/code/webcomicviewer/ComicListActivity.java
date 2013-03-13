package code.webcomicviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ComicListActivity extends FragmentActivity
        implements ComicListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_list);

        if (findViewById(R.id.comic_detail_container) != null) {
            mTwoPane = true;
            ((ComicListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.comic_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ComicDetailFragment.ARG_ITEM_ID, id);
            ComicDetailFragment fragment = new ComicDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.comic_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ComicDetailActivity.class);
            detailIntent.putExtra(ComicDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
