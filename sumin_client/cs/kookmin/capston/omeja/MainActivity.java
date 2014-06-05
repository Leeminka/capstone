package cs.kookmin.capston.omeja;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kr.ac.kookmin.cs.cap3.R;
import cs.kookmin.capston.omeja.AppInfo.AppFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	public static Drawable clickedAppIcon;
	public static String clickedAppName = "";
	public static String clickedAppPackageName = "";
	
	private final int MENU_ALL = 1;
	private final int MENU_DOWNLOAD = 0;
	private int MENU_MODE = 0;
	private IAAdapter mAdapter = null;
	private ListView mListView = null;
	private View mLoadingContainer;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_list);

		mLoadingContainer = findViewById(R.id.loading_container);
		mListView = (ListView) findViewById(R.id.listView1);

		mAdapter = new IAAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				clickedAppName = mAdapter.getAppName(paramInt);
				clickedAppIcon = mAdapter.getIcon(paramInt);
				clickedAppPackageName = mAdapter.getAppPackageName(paramInt);
				Intent OneItem = new Intent(MainActivity.this, OneItem.class);
				startActivity(OneItem);
			}

		});
	}

	private class IAAdapter extends BaseAdapter {
		private List<ApplicationInfo> mAppList = null;
		private Context mContext = null;
		private ArrayList<AppInfo> mListData = new ArrayList();

		public IAAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		public final String getAppName(int paramInt) {
			return ((AppInfo) this.mListData.get(paramInt)).getAppName();
		}

		public final String getAppPackageName(int paramInt) {
			return ((AppInfo) this.mListData.get(paramInt)).getAppPackageName();
		}

		public int getCount() {
			return this.mListData.size();
		}

		public final Drawable getIcon(int paramInt) {
			return ((AppInfo) this.mListData.get(paramInt)).getIcon();
		}

		public Object getItem(int paramInt) {
			return this.mListData.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return 0L;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_layout, null);

				holder.mIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				holder.mName = (TextView) convertView
						.findViewById(R.id.app_name);
				holder.mPacakge = (TextView) convertView
						.findViewById(R.id.app_package);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AppInfo data = mListData.get(position);

			if (data.mIcon != null) {
				holder.mIcon.setImageDrawable(data.mIcon);
			}

			holder.mName.setText(data.mAppName);
			holder.mPacakge.setText(data.mAppPackge);

			return convertView;
		}

		public void rebuild() {
			if (this.mAppList == null) {
				Log.d(MainActivity.TAG, "Is Empty Application List");
				pm = MainActivity.this.getPackageManager();
				this.mAppList = pm
						.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_DISABLED_COMPONENTS);
			}
			AppFilter filter;
			switch (MENU_MODE) {
			case MENU_DOWNLOAD:
				filter = AppInfo.THIRD_PARTY_FILTER;
				break;
			default:
				filter = null;
				break;
			}

			if (filter != null) {
				filter.init();
			}
			mListData.clear();

			AppInfo addInfo = null;
			ApplicationInfo info = null;

			for (ApplicationInfo app : mAppList) {
				info = app;

				if (filter == null || filter.filterApp(info)) {
					//
					addInfo = new AppInfo();
					// App Icon
					addInfo.mIcon = app.loadIcon(pm);
					// App Name
					addInfo.mAppName = app.loadLabel(pm).toString();
					// App Package Name
					addInfo.mAppPackge = app.packageName;
					mListData.add(addInfo);
				}
			}
			Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
		}

	}

	private class ViewHolder {
		public ImageView mIcon;
		public TextView mName;
		public TextView mPacakge;
	}

	@Override
	protected void onResume() {
		super.onResume();
		startTask();
	}

	private void startTask() {
		new AppTask().execute();
	}

	private void setLoadingView(boolean isView) {
		if (isView) {
			mLoadingContainer.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.VISIBLE);
			mLoadingContainer.setVisibility(View.GONE);
		}
	}

	private class AppTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			setLoadingView(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			mAdapter.rebuild();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
			setLoadingView(false);
		}

	};
}
