package code.webcomicviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateReceiver extends BroadcastReceiver {
	
	public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";
	
	public UpdateReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast toast = Toast.makeText(context, "Updating Complete", Toast.LENGTH_LONG);
		toast.show();
	}
}
