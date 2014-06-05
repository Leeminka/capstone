package kr.ac.kookmin.cs.cap3.reporting;

import kr.ac.kookmin.cs.cap3.R;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class WebviewActivity extends Activity{
	
	private WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		wv = (WebView)findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		
		Intent in = getIntent();
		String apkname = in.getExtras().getString("apk").toString();
		int report = in.getExtras().getInt("report");
		String title="";
		String filename="";
		
		switch(report){
		case 1:		//server
			filename = "server_vuln.txt";
			title = "Weak Server-Side Controls";
			break;
		case 2:		//web
			filename = "web_vuln.txt";
			title = "Weak WebView Controls";
			break;
		case 4:		//stack
			filename = "stack.txt";
			title = "Improper Session Handling";
			break;
		case 5:		//ssl
			filename = "ssl.txt";
			title = "Improper Session Handling";
			break;
		}
		
		
		
		
		 String url = "http://203.246.112.134:8080/gcm-server/reportview";
		 String postData = "apkname="+apkname+"&report="+title+"&file="+filename;

		 wv.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
		
	}

}
