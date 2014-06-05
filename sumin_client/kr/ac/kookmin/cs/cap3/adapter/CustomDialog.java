package kr.ac.kookmin.cs.cap3.adapter;

import result.Result;
import kr.ac.kookmin.cs.cap3.MainActivity;
import kr.ac.kookmin.cs.cap3.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {
	private static Button btnOk;
	private static Button btnCancel;
	private static TextView	tv;
	private static Context	cte;
	
	public CustomDialog(Context context, String message, String level) {
		super(context);
		
		cte = context;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	
		setContentView(R.layout.dialog_notification);

		tv = (TextView)findViewById(R.id.noti_msg);
		tv.setText(message);
		Log.d("JANG", message);
		tv.setTextColor(Color.WHITE);
		btnOk = (Button)findViewById(R.id.btn_dlg_ok);
		btnCancel = (Button)findViewById(R.id.btn_dlg_close);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btnOk){
			Intent	intent = new Intent(cte, MainActivity.class);
			cte.startActivity(intent);
			dismiss();
		}
		else if(v==btnCancel){
			dismiss();
		}
		
	}

}