package code.webcomicviewer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Comic_Gallery extends Activity {

	private ArrayList<String> ComicNames;
	private int CurrentPosition;
	private int max;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic__gallery);
        DataBaseHandler db = new DataBaseHandler(this);
        ComicNames = db.getAllComicNames();
        CurrentPosition = 0;
        max = ComicNames.size();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_comic__gallery, menu);
        return true;
    }
}
