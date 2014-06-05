package result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import kr.ac.kookmin.cs.cap3.R;
import cs.kookmin.capston.omeja.MainActivity;
import cs.kookmin.capston.omeja.OneItem;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CacheView extends Activity implements OnClickListener {

	private String cachePosition = Environment.getExternalStorageDirectory()
			+ "/Android/data/" + kr.ac.kookmin.cs.cap3.MainActivity.packageName + "/";
	ArrayList<String> realCacheNames = new ArrayList<String>();
	ListView listView;
	ArrayAdapter<String> adapter;
	String SaveFilePath = Environment.getExternalStorageDirectory()
			+ "/testfolder/" + kr.ac.kookmin.cs.cap3.MainActivity.packageName + "/cache";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!(new File(cachePosition + "cache")).exists()) {
			Log.e("minka", "cache�놁뼱!");
			realCacheNames.add("캐쉬가 없습니다.");
		} else {
			try {
				cachePosition += "cache";

				File CopyFile = new File(SaveFilePath);
				if (!CopyFile.exists())
					CopyFile.mkdirs();

				searchDirList(cachePosition);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

		setContentView(R.layout.cache_result);
		if (realCacheNames.size() == 0) {
			realCacheNames.add("캐쉬가 없습니다.");
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, realCacheNames);
		listView = (ListView) findViewById(R.id.cache_result_list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String value = (String) adapter.getItem(position);
				if (value.equals("캐쉬가 없습니다."))
					Toast.makeText(getBaseContext(), "캐쉬가 없습니다.",
							Toast.LENGTH_SHORT).show();
				else {
					String CacheFileName = (String) adapter.getItem(position);
					// File filePath = new File(CacheFileName);
					String map[] = CacheFileName.toString().split("/");
					String realfileName = map[map.length - 1];

					realfileName = SaveFilePath + "/" + realfileName + ".jpg";

					File openFile = new File(realfileName);
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(openFile), "image/*");
					startActivity(intent);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void searchDirList(String source) throws IOException {
		Log.e("source", "source= " + source);
		File dir = new File(source);
		File[] fileList = dir.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {

				if (file.length() > 609600) {

					Log.e("file.getpath()=", file.getPath());
					realCacheNames.add(file.getPath());

					String map[] = file.toString().split("/");
					String cachefileName = map[map.length - 1] + ".jpg";
					String realSaveFilePath = SaveFilePath + "/"
							+ cachefileName;

					File in = new File(file.toString());
					File out = new File(realSaveFilePath);
					Log.e("save", "realSaveFilePath = " + realSaveFilePath);
					InputStream fis = new FileInputStream(in);
					OutputStream newfos = new FileOutputStream(out);

					int readcount = 0;
					byte[] buffer = new byte[1024];
					while ((readcount = fis.read(buffer, 0, 1024)) != -1) {
						newfos.write(buffer, 0, readcount);
						// Log.e("minka", "while臾�);
					}
					//Log.e("minka", "�뚯씪蹂듭궗");
					newfos.close();
					fis.close();

					/*
					 * Log.e("cache", "異붿텧���좊뱾 �뚯씪 寃쎈줈 = " + file.toString());
					 * 
					 * realCacheNames.add(file.toString());
					 * 
					 * String map[] = file.toString().split("/"); String
					 * cachefileName = map[map.length - 1];
					 * 
					 * File CopyFile = new File(SaveFilePath);
					 * if(!CopyFile.exists()) CopyFile.mkdirs();
					 * 
					 * SaveFilePath = SaveFilePath + "/"+cachefileName +".jpg";
					 * 
					 * File in = new File(file.toString()); File out = new
					 * File(SaveFilePath); Log.e("save","SaveFilePath = " +
					 * SaveFilePath); InputStream fis = new FileInputStream(in);
					 * OutputStream newfos = new FileOutputStream(out);
					 * 
					 * int readcount = 0; byte[] buffer = new byte[1024]; while
					 * ((readcount = fis.read(buffer, 0, 1024)) != -1) {
					 * newfos.write(buffer, 0, readcount); //Log.e("minka",
					 * "while臾�); } Log.e("minka", "�뚯씪蹂듭궗"); newfos.close();
					 * fis.close();
					 */
				}
			} else if (file.isDirectory()) {
				// Log.e("cache", "�붾젆�좊━�대쫫 = " + file.getName());
				searchDirList(file.getCanonicalPath().toString());
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("minka", "CacheView Destroy");

		File file = new File(Environment.getExternalStorageDirectory()
				+ "/testfolder/" + kr.ac.kookmin.cs.cap3.MainActivity.packageName
				+ "/cache");
		if (file.exists()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				childFile.delete();
				Log.e("minka", "delete copyed cache complete");
			}
			file.delete();
		}
	}

}
