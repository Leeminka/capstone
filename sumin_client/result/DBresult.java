package result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kr.ac.kookmin.cs.cap3.R;
import cs.kookmin.capston.omeja.MainActivity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DBresult extends Activity implements OnClickListener {

	public static String DBPosition;
	public static String packageName = null;
	public static String testPackageName = MainActivity.clickedAppPackageName;
	
	ListView listView;
	ArrayList<String> realDBNames = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	private String targetPosition = Environment.getExternalStorageDirectory()
			+ "/testfolder/";
	private String DBexistFolder = "/data/data/" + kr.ac.kookmin.cs.cap3.MainActivity.packageName+ "/databases";
	//private String DBexistFolder = "/data/data/" + testPackageName+ "/databases";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_result);
		// List <String>realDBNames = null;
		packageName = kr.ac.kookmin.cs.cap3.MainActivity.packageName;
		//packageName = testPackageName;

		try {
			DoExportDB();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (!(new File("/data/data/" + Exporter.packageName + "/databases")
					.exists())) {
				
				realDBNames.add("사용하는 DB가 없습니다");
			}
			FileInputStream fis = new FileInputStream(targetPosition + "DBList.txt");
			BufferedReader readList = new BufferedReader(new InputStreamReader(
					fis));

			/*String dbName;
			while ((dbName = readList.readLine()) != null) {
				String map[] = dbName.split("/");
				Log.e("불러오는거!!", dbName);
				Log.e("what", "map[map.length-1] = " + map[map.length - 1]);
				String trash = map[map.length - 1];
				realDBNames.add(trash);
			}
			*/
			Log.e("크기", "realDBNames.size() = " + realDBNames.size());
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, realDBNames);
			listView = (ListView) findViewById(R.id.db_result_list);
			listView.setAdapter(adapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					String value = (String) adapter.getItem(position);
					if (value.equals("사용하는 DB가 없습니다"))
						Toast.makeText(getBaseContext(),
								"사용하는 DB가 없어 결과가 없습니다.", Toast.LENGTH_SHORT)
								.show();
					else {
						String DBfileName = targetPosition
								+ (String) adapter.getItem(position);
						File filePath = new File(DBfileName);
						Intent intent = new Intent();
						Log.e("real","realDBPath = " + DBfileName);
						intent.setDataAndType(Uri.fromFile(filePath), "*/*");
						intent.setAction(android.content.Intent.ACTION_VIEW);

						startActivity(intent);
					}
				}
			});

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// for copy apk to sdk card --> not use for us
	private void DoExportDB() throws IOException {
		Log.e("exporter", "doexportAPK method!");
		Log.e("minkakkkkk","packageName=" + kr.ac.kookmin.cs.cap3.MainActivity.packageName);
		//File DBExsistFolder = new File(targetPosition);
		Log.e("DB", " databases폴더있음");
		//Log.e("db", "DBPosition = " + DBPosition);
		File checkDBFolder = new File(DBexistFolder);

		if (!checkDBFolder.exists()){
			//database없는 경우
			return;
		}
		// 폴더 만들기
		File file = new File(targetPosition +packageName);
		if (!file.exists())
			file.mkdirs();
		targetPosition = targetPosition + packageName + "/";
		//targetPosition="/testfolder/appName/"
		//DBresult.DBPosition = targetPosition;

		// String cmd = "cat " + DBPosition + " > " + targetPosition;

		String mkDBlist = "ls " + DBexistFolder + "/* > " + targetPosition
				+ "DBList.txt";
		BufferedReader reader = null;
		Log.e("su", "실행안됨");
		Process p = Runtime.getRuntime().exec("su");

		DataOutputStream os = new DataOutputStream(p.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		os.writeBytes(mkDBlist); // ->됨 / 루팅시
		os.flush();
		Log.e("su", "실행됨");

		FileInputStream fis = new FileInputStream(targetPosition + "DBList.txt");
		BufferedReader readList = new BufferedReader(new InputStreamReader(fis));
		String dbName;
		Log.e("while", "전");
		while ((dbName = readList.readLine()) != null) {
			String map[];
			Log.e("한줄","full 한줄 = " + dbName);
			map = dbName.split("/");
			String realDB = map[map.length - 1];

			BufferedReader reader2 = null;
			Process p2 = Runtime.getRuntime().exec("su");

			DataOutputStream os2 = new DataOutputStream(p2.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(
					p2.getInputStream()));

			Log.e("여기!!", "realDB="+realDB);
			String cmd = "cat " + dbName + " > " + targetPosition + realDB;
			realDBNames.add(realDB);
			os2.writeBytes(cmd); // ->됨 / 루팅시
			os2.flush();
			Log.e("su", "실행됨");
			Log.e("db 파일명", realDB);
		}

		/*
		 * BufferedReader reader = null; Log.e("su","실행안됨"); Process p
		 * =Runtime.getRuntime().exec("su");
		 * 
		 * 
		 * DataOutputStream os = new DataOutputStream(p.getOutputStream());
		 * reader = new BufferedReader(new
		 * InputStreamReader(p.getInputStream())); os.writeBytes(cmd); //->됨 /
		 * 루팅시 os.flush(); Log.e("su","실행됨");
		 */

		/*
		 * --file copy code --It works File CopyFile = new File(targetPosition);
		 * if (!CopyFile.exists()) { CopyFile.mkdirs(); Log.e("minka",
		 * "폴더 생성함"); } targetPosition += "test.apk";
		 * 
		 * File file = new File(target.toString()); File out = new
		 * File(targetPosition); Log.e("in", "" + target.toString());
		 * Log.e("out", "" + targetPosition);
		 * 
		 * Log.e("minka", "1");
		 * 
		 * InputStream fis = new FileInputStream(file); OutputStream newfos =
		 * new FileOutputStream(targetPosition); Log.e("minka", "2"); int
		 * readcount = 0; byte[] buffer = new byte[1024]; while ((readcount =
		 * fis.read(buffer, 0, 1024)) != -1) { newfos.write(buffer, 0,
		 * readcount); Log.e("minka", "while문"); } newfos.close(); fis.close();
		 */
	}

}
