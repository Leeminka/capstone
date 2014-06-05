package result;

import java.io.File;

import kr.ac.kookmin.cs.cap3.R;
import cs.kookmin.capston.omeja.MainActivity;
import cs.kookmin.capston.omeja.OneItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		Button Result1 = (Button) findViewById(R.id.show_db_result_btn);
		Button Result2 = (Button) findViewById(R.id.result2);
		Button Result3 = (Button) findViewById(R.id.show_cache_result_btn);
		Button Result4 = (Button) findViewById(R.id.result4);
		Button Result5 = (Button) findViewById(R.id.result5);
		
		Result1.setOnClickListener(this);
		Result2.setOnClickListener(this);
		Result3.setOnClickListener(this);
		Result4.setOnClickListener(this);
		Result5.setOnClickListener(this);
		
		ImageView clickedApp = (ImageView)findViewById(R.id.result_app_icon);
		TextView clickedAppName = (TextView)findViewById(R.id.resultAppName);
		TextView clickedAppPackage = (TextView)findViewById(R.id.resultAppPackage);
		
		clickedApp.setImageDrawable(MainActivity.clickedAppIcon);
		clickedAppName.setText(MainActivity.clickedAppName);
		clickedAppPackage.setText(MainActivity.clickedAppPackageName);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.show_db_result_btn:
			Intent DBresult = new Intent(Result.this, DBresult.class);
			startActivity(DBresult);
			break;
		case R.id.show_cache_result_btn:
			Intent CacheResult = new Intent(Result.this, CacheView.class);
			startActivity(CacheResult);
			break;
		}

	}
}
