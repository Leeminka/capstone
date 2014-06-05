package kr.ac.kookmin.cs.cap3.adapter;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ShowMsg  extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Log.d("juvh", "showMSg");
		String title="";
		String msg="";
		String level="";
		
		Bundle bun = getIntent().getExtras();
		title = bun.getString("title");
		Log.d("JANG", title);
		msg = bun.getString("msg");
		level = bun.getString("accept");
		//me
		Log.d("juvh", "showMSg");
		Log.d("JANG", msg);
		Log.d("JANG", level);
		Log.d("JANG", title);
		String message="\""+msg+"\"";
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		CustomDialog	dialog = new CustomDialog(this, message, level);
		
		dialog.show();
		Log.d("AAAAAAAAAA", "Successs");
	}
}
