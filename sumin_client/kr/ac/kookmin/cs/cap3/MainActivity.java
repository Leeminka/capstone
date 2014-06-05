package kr.ac.kookmin.cs.cap3;

import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.EXTRA_MESSAGE;
import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.SENDER_ID;
import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.SERVER_URL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import kr.ac.kookmin.cs.cap3.adapter.ServerUtilities;
import kr.ac.kookmin.cs.cap3.reporting.WebviewActivity;

import org.apache.http.util.ByteArrayBuffer;

import result.CacheView;
import result.DBresult;
import result.Result;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import cs.kookmin.capston.omeja.About;
import cs.kookmin.capston.omeja.OneItem;

public class MainActivity extends Activity {

	// Reporting ListView
	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	public static ArrayList<String> filename = new ArrayList<String>();
	public static String packageName;
	ExpandableListView expandableList;
	MyExpandableAdapter adapter;

	private SharedPreferences mPreference;
	private String preName = "is_updated";

	private ProgressDialog mProgressDialog;
	
	
	private String checkingDatabse = "";
	
	// SETTING ITEM

	

	TextView tv_new = null;
	TextView tv_about = null;
	TextView tv_main = null;
	OnClickListener mainClickListener;

	public static String DeviceId;
	public static String reg_id;
	AsyncTask<Void, Void, Void> mRegisterTask; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		
		// Device ID
		
		DeviceId = getDeviceId();
	//	checkingDatabse = CheckDB(DeviceId);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Layout Setting
		initListView();

		// Set the Items of Parent
		
		
		Log.d("START PREF", getMySharedPreferences("isUpdated"));
		
		
		Log.d("checking DB", checkingDatabse);
		
		setGroupParents();
		setChildData();
		
		
		GCM_Func();

		{
			tv_new = (TextView) findViewById(R.id.main_new);
			tv_about = (TextView) findViewById(R.id.main_about);
			tv_main = (TextView)findViewById(R.id.main_tv);
			
			
			mainClickListener = new mainClickListener();
			tv_new.setOnClickListener(mainClickListener);
			tv_main.setOnClickListener(mainClickListener);
			tv_about.setOnClickListener(mainClickListener);
			// Create the Adapter
			adapter = new MyExpandableAdapter(parentItems, childItems);

			adapter.setInflater(
					(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
					this);

			// Set the Adapter to expandableList
			expandableList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			// expandableList.setOnChildClickListener(this);
		}

		// ??��?��?? ???�???��?? ??????

	}

	// 리�?��?�뷰 ??????�???��?? ??????
		public  void listAnimation() {
			AnimationSet set = new AnimationSet(true);
			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(500);
			set.addAnimation(animation);

			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

			animation.setDuration(100);
			set.addAnimation(animation);

			LayoutAnimationController controller = new LayoutAnimationController(
					set, 0.5f);
			expandableList.setLayoutAnimation(controller);
		}
	
	
	private String getDeviceId() {
		TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return mgr.getDeviceId();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void GCM_Func() {

		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// SharedPreferences
		// pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);
		// SharedPreferences.Editor editor=pref.edit();

		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("regId", regId);
		reg_id = GCMRegistrar.getRegistrationId(this);

		// editor.putString("regid_pref", regId);
		// editor.commit();

		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			reg_id = GCMRegistrar.getRegistrationId(this);
			Log.d("juvhak", reg_id);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						/*
						 * boolean registered =
						 * ServerUtilities.register(context, regId); if
						 * (!registered) { GCMRegistrar.unregister(context); }
						 */
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}


	
	@Override
	public void onDestroy() {
	   super.onDestroy();
	   if (mHandleMessageReceiver != null) {
	      unregisterReceiver(mHandleMessageReceiver);
	      // The important bit here is to set the receiver
	      // to null once it has been unregistered.
	   }
	}
	
	
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}


	private void initListView() {
		expandableList = (ExpandableListView) findViewById(R.id.elv_list);
		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);
	}

	// method to add parent Items
	//	APK 추가될 때마다 생겨나는 메쏘드라고 보면됨
	//	여기서 DB통신해야될듯!
	public void setGroupParents() {
		ArrayList<String> data = new ArrayList<String>();
		data = ServerUtilities.getApk(getApplicationContext(), DeviceId);
		
		for(int i=0;i<data.size(); i++){
			parentItems.add(data.get(i));
		}
	}

	// method to set child data of each parent
	public void setChildData() {
		ArrayList<String> child;
		child = new ArrayList<String>();
		child.add("1. Insecure Data Storage");
		child.add("2-1. Weak Server-Side Controls");
		child.add("2-2. Weak WebView Controls");
		child.add("3. Side Channel Data Leakage");
		child.add("4. Improper Session Handling");
		child.add("5. Insufficient Transport Layer Protection");
		
		for(int i=0;i<parentItems.size();i++){
			childItems.add(child);

		}
	}

	// preference ??��??
	private String getMySharedPreferences(String _key) {
		if (mPreference == null) {
			mPreference = getSharedPreferences(preName, MODE_WORLD_READABLE
					| MODE_WORLD_WRITEABLE);
		}
		return mPreference.getString(_key, "");
	}

	private void setMySharedPreferences(String _key, String _value) {
		if (mPreference == null) {
			mPreference = getSharedPreferences(preName, MODE_WORLD_READABLE
					| MODE_WORLD_WRITEABLE);
		}
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putString(_key, _value);
		editor.commit();
	}

	private class mainClickListener implements OnClickListener {

		Intent intent = null;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.main_new:
				showDialog(0);
				break;
			case R.id.main_about:
				Intent aboutActivity = new Intent(MainActivity.this, About.class);
				startActivity(aboutActivity);
				break;
			case R.id.main_tv:
				Intent apkList = new Intent(MainActivity.this, cs.kookmin.capston.omeja.MainActivity.class);
				startActivity(apkList);
				break;
			}
		}
	}

	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog dialogDetails = null;
		switch (id) { 
		default: 
			LayoutInflater inflater = LayoutInflater.from(this); 
			View dialogview = inflater.inflate(R.layout.dialog_regit, null); 
			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this); 
			dialogbuilder.setTitle("Login"); 
			dialogbuilder.setView(dialogview); 
			dialogDetails = dialogbuilder.create(); 

			break; 
		} 
		return dialogDetails; 
	
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		//shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		switch (id) {
		default: // 로그인!
			Log.d("JANG", "ONPRE");
			final AlertDialog alertDialog = (AlertDialog) dialog;
			Button save = (Button) alertDialog.findViewById(R.id.btn_login);
			Button cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
			final EditText et_name = (EditText) alertDialog
					.findViewById(R.id.et_name);
			final EditText et_mem = (EditText) alertDialog
					.findViewById(R.id.et_mem);
			final EditText et_pnum = (EditText) alertDialog
					.findViewById(R.id.et_pnum);
			Log.d("JANG", "Click");
			save.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// TODO Auto-generated method stub
							ServerUtilities.registId(getApplicationContext(),
									reg_id, DeviceId, et_name.getText()
											.toString(), et_mem.getText()
											.toString(), et_pnum.getText()
											.toString());

							return null;
						}

						@Override
						protected void onPostExecute(Void params) {
							

						}

					};
					mRegisterTask.execute(null, null, null);
					
					alertDialog.dismiss();
				}
			});

			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
				}
			});
			break;
		
		}
	}

	private String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private String updateDisplay(int hourOfDay, int minutes) {
		return new StringBuilder().append(pad(hourOfDay)).append(":")
				.append(pad(minutes)).toString();
	}

	/*
	 * ??��?��?��????��?? ???�?�??????��?? ??????�? ??????
	 
	private String CheckDB(String DeviceId) {
		DBConnector db = new DBConnector(
				"http://112.108.40.125:8080/gcm-server/return_json.jsp");
		String query = "select * from User_Information where DeviceId='"
				+ DeviceId + "'";
		String json = db.getDB(query);
		String result = null;
		String name = null;

		try {
			JSONArray jArray = new JSONArray(json);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				result = json_data.getString("admission");
				name = json_data.getString("username");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("JANG", "JSON FAIL AUTH");
			return "AUTH";
		}

		
	}
*/
	// Picker Customizing
	/**
	 * Adapter for countries
	 */
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			@SuppressWarnings("unused")
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

		}
	};

	
	//List View Click Event => 엑티비티 전환을 여기서 모두하면됨.
	private class MyExpandableAdapter extends BaseExpandableListAdapter {

		private Activity activity;
		private ArrayList<Object> childtems;
		private LayoutInflater inflater;
		private ArrayList<String> parentItems, child;

		// constructor
		public MyExpandableAdapter(ArrayList<String> parents,
				ArrayList<Object> childern) {
			this.parentItems = parents;
			this.childtems = childern;
		}

		public void setInflater(LayoutInflater inflater, Activity activity) {
			this.inflater = inflater;
			this.activity = activity;
		}

		// method getChildView is called automatically for each child view.
		// Implement this method as per your requirement
		@Override
		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			child = (ArrayList<String>) childtems.get(groupPosition);

			TextView textView = null;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.child_view, null);
			}

			// get the textView reference and set the value
			textView = (TextView) convertView.findViewById(R.id.textViewChild);
			textView.setText(child.get(childPosition));

			// set the ClickListener to handle the click event on child item
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					Intent web = new Intent(MainActivity.this,WebviewActivity.class);
					web.putExtra("apk", parentItems.get(groupPosition));
				/*	Children View  클릭했을 때! 액티비티 넘겨줘야함.				*/
				/*	함수 - child.get(childPosition) : 내부 리스트뷰 이름 리턴 		*/
				/*		- parentItems.get(groupPosition) : 외부 (APK파일 리턴	*/
				
					
				//	Toast.makeText(activity, child.get(childPosition),
				//			Toast.LENGTH_SHORT).show();
					
					Toast.makeText(activity, "패키지이름:"+parentItems.get(groupPosition),
							Toast.LENGTH_SHORT).show();
					packageName=parentItems.get(groupPosition);
					//	리포팅부분이므로 같이하는것을 추천!
					switch(childPosition){
						// 내부의 리스트 클릭했을 때 이벤트
					case 0:		//1번쨰
						Intent DBresult = new Intent(MainActivity.this, DBresult.class);
						startActivity(DBresult);
						break;
					case 1:		//weak server
						web.putExtra("report", 1);
						startActivity(web);
						break;	
					case 2:	// weak web
						web.putExtra("report", 2);
						startActivity(web);
						break;
					case 3:
						Intent CacheView = new Intent(MainActivity.this, CacheView.class);
						startActivity(CacheView);
						break;
					case 4:
						web.putExtra("report", 4);
						startActivity(web);
						break;
					case 5:
						web.putExtra("report",5);
						startActivity(web);
						break;
					}

				}
			});
			return convertView;
		}

		// method getGroupView is called automatically for each parent item
		// Implement this method as per your requirement
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.parent_view, null);
			}

			((CheckedTextView) convertView).setText(parentItems
					.get(groupPosition));
			
			((CheckedTextView) convertView).setChecked(isExpanded);
			
			
			return convertView;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return ((ArrayList<String>) childtems.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public int getGroupCount() {
			return parentItems.size();
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			super.onGroupCollapsed(groupPosition);
		}

		@Override
		public void onGroupExpanded(int groupPosition) {
			super.onGroupExpanded(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

	
	
	//Reporting Download Module
	private class DownloadFileAsync extends AsyncTask<String, Integer, String> {

		private Context context;
		private ArrayList<String> arr;
		ProgressDialog mProgressDialog;
		

		public DownloadFileAsync(Context context, ArrayList<String> arr) {
			this.context = context;
			this.arr = new ArrayList<String>();
			this.arr = arr;
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Loading List...........");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);

		}
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        mProgressDialog.show();
		    }
		@Override
		protected String doInBackground(String... sUrl) {
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			
			
			
			
			
			
		/*	for(int i = 0 ; i < arr.size(); i++){
				DownloadFromUrl("http://112.108.40.125:8080/gcm-server/mvs/result/"+arr.get(i), arr.get(i));
				
			}*/
			return null;
		}
		 @Override
		    protected void onPostExecute(String result) {
			
		    }
		
		public void DownloadFromUrl(String DownloadUrl, String fileName) {

			   try {
			           File root = android.os.Environment.getExternalStorageDirectory();               

			           File dir = new File (root.getAbsolutePath() + "/mvs");
			           if(dir.exists()==false) {
			                dir.mkdirs();
			           }

			           URL url = new URL(DownloadUrl); //you can write here any link
			           File file = new File(dir, fileName);

			           long startTime = System.currentTimeMillis();
			           Log.d("DownloadManager", "download begining");
			           Log.d("DownloadManager", "download url:" + url);
			           Log.d("DownloadManager", "downloaded file name:" + fileName);

			           /* Open a connection to that URL. */
			           URLConnection ucon = url.openConnection();

			           /*
			            * Define InputStreams to read from the URLConnection.
			            */
			           InputStream is = ucon.getInputStream();
			           BufferedInputStream bis = new BufferedInputStream(is);

			           /*
			            * Read bytes to the Buffer until there is nothing more to read(-1).
			            */
			           ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			           int current = 0;
			           while ((current = bis.read()) != -1) {
			              baf.append((byte) current);
			           }


			           /* Convert the Bytes read to a String. */
			           FileOutputStream fos = new FileOutputStream(file);
			           fos.write(baf.toByteArray());
			           fos.flush();
			           fos.close();
			           Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

			   } catch (IOException e) {
			       Log.d("DownloadManager", "Error: " + e);
			   }

			}
	}

	
}
