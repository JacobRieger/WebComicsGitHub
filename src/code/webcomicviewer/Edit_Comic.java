package code.webcomicviewer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Edit_Comic extends Activity implements OnClickListener {

	String name;
	String newName;
	String newIurl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__comic);
        
        DataBaseHandler db = new DataBaseHandler(this);
        Button Save = (Button) findViewById(R.id.button1);
        Save.setOnClickListener(this);
        
        Bundle extras = getIntent().getExtras();
        String comicName = extras.getString("Comic");
        
        Comic editable = db.getComic(comicName);
        name = editable.getName();
        
        EditText Name = (EditText) findViewById(R.id.editText2);
        EditText Iurl = (EditText) findViewById(R.id.editText1);
        
        Name.setText(editable.getName());
        Iurl.setText(editable.getImageUrl());
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit__comic, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		
		DataBaseHandler db = new DataBaseHandler(this);
		if(v.getId() == R.id.button1)
		{
			EditText Name = (EditText) findViewById(R.id.editText2);
			EditText Iurl = (EditText) findViewById(R.id.editText1);
			
			Comic old = db.getComic(name);
			old.setName(Name.getText().toString());
			old.setImageUrl(Iurl.getText().toString());
			db.updateComic(old);
			
			Intent intent = new Intent(this, Front_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
		
	}
}
