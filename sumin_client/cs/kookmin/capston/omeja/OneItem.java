package cs.kookmin.capston.omeja;

import java.io.IOException;
import java.util.ArrayList;

import kr.ac.kookmin.cs.cap3.R;
import result.Exporter;
import result.Result;
import Database.DBPermissionListAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OneItem extends Activity implements OnClickListener {

	// private IAAdapter adapter = null;
	private ArrayAdapter<String> mAdapter = null;
	private Button extractBtn;
	private ListView listView;
	ArrayList<String> mList = null;
	DBPermissionListAdapter mPermissionAdapter = new DBPermissionListAdapter(
			this);

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.permission_list);
		TextView AppName = (TextView) findViewById(R.id.clickedAppName);
		TextView AppPackageName = (TextView) findViewById(R.id.clickedAppPackage);
		ImageView AppIcon = (ImageView) findViewById(R.id.clicked_app_icon);
		AppName.setText(MainActivity.clickedAppName);
		AppPackageName.setText(MainActivity.clickedAppPackageName);
		AppIcon.setImageDrawable(MainActivity.clickedAppIcon);
		String[] ClickedAppPermissionList;
		PackageInfo packageInfo = null;

		Log.e("minka", "appName = " + MainActivity.clickedAppName);
		Log.e("minka", "pakage = " + MainActivity.clickedAppPackageName);

		Button ExportBtn = (Button) findViewById(R.id.ExportButton);
		Button DeleteBtn = (Button) findViewById(R.id.DeleteButton);
		DeleteBtn.setOnClickListener(this);
		ExportBtn.setOnClickListener(this);

		try {
			packageInfo = getPackageManager().getPackageInfo(
					MainActivity.clickedAppPackageName,
					PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClickedAppPermissionList = packageInfo.requestedPermissions;

		mList = new ArrayList();
		if (ClickedAppPermissionList == null)
			mList.add("�ъ슜�섎뒗 沅뚰븳���놁뒿�덈떎.");
		else {
			mPermissionAdapter.open();
			for (int i = 0; i < ClickedAppPermissionList.length; i++) {
				Log.e("minka", "requestedPermissions[i] = "
						+ ClickedAppPermissionList[i]);
				String[] LongPermission = ClickedAppPermissionList[i]
						.split("\\.");

				Log.e("minka", "real = "
						+ LongPermission[(-1 + LongPermission.length)]);

				String realPermission = mPermissionAdapter
						.getPermissionName(LongPermission[(-1 + LongPermission.length)]);
				mList.add(realPermission);
			}
			mPermissionAdapter.close();
			Log.e("�섎�", "mList.size() = " + mList.size());
		}

		mAdapter = new ArrayAdapter<String>(this, R.layout.one_item, mList);

		listView = (ListView) findViewById(R.id.permission_list);
		listView.setAdapter(mAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				String str1 = (String) mAdapter.getItem(paramInt);
				String str2 = null;
				if (str1.equals("�ъ슜�섎뒗 沅뚰븳���놁뒿�덈떎."))
					str2 = "�ъ슜�섎뒗 沅뚰븳���놁뒿�덈떎.";
				else {
					mPermissionAdapter.open();
					str2 = mPermissionAdapter.getPermissionInfo(str1);
					if (str2.equals("NOT_FOUND"))
						str2 = "�대떦 沅뚰븳����븳 �곸꽭 �댁슜��李얠쓣 ���놁뒿�덈떎.";
				}
				Toast.makeText(getBaseContext(), str2, 0).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ExportButton:
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			try {
				Exporter mExport = new Exporter(
						MainActivity.clickedAppPackageName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//new DownloadFileAsync(getBaseContext(), null).execute();
			Log.e("export ��", "��");
			Intent Result = new Intent(OneItem.this,
					kr.ac.kookmin.cs.cap3.MainActivity.class);
			startActivity(Result);
			break;
		case R.id.DeleteButton:
			Uri uri = Uri.fromParts("package",
					MainActivity.clickedAppPackageName, null);
			Intent deleteIntent = new Intent(Intent.ACTION_DELETE, uri);
			startActivity(deleteIntent);
			break;
		}

	}

	/*
	 * public void onClick(View v) {
	 * 
	 * switch (v.getId()) { case (R.id.ExportButton): try { Exporter mExporter =
	 * new Exporter(MainActivity.clickedAppPackageName); } catch (IOException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } break; } }
	 */
	// Reporting Download Module
	private class DownloadFileAsync extends AsyncTask<String, Integer, String> {

		private Context context;
		private ArrayList<String> arr;
		ProgressDialog mProgressDialog;

		public DownloadFileAsync(Context context, ArrayList<String> arr) {
			this.context = context;
			this.arr = new ArrayList<String>();
			this.arr = arr;
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Processing...........");
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

			try {
				Exporter mExport = new Exporter(
						MainActivity.clickedAppPackageName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * for(int i = 0 ; i < arr.size(); i++){
			 * DownloadFromUrl("http://112.108.40.125:8080/gcm-server/mvs/result/"
			 * +arr.get(i), arr.get(i));
			 * 
			 * }
			 */
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}
}
