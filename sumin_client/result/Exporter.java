package result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import kr.ac.kookmin.cs.cap3.MainActivity;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class Exporter {

	public static String packageName = null;
	private String packagePosition = "/data/app/";
	private String DBPosition = "/data/data/";
	private File target;
	private String targetPosition = Environment.getExternalStorageDirectory()
			+ "/testfolder/";
	public String lineEnd = "\r\n";
	public String twoHyphens = "--";
	public String boundary = "*****";
	public String URLString = null;
	public String FileNameString = null;

	public Exporter(String paramString) throws IOException {
		packageName = paramString;

		packagePosition += packageName;

		// DB
		DBPosition = DBPosition + packageName + "/databases/";

		Log.e("Exporter", "Exporter constructor占쏙옙!");
		target = new File(packagePosition + ".apk");
		if (!target.exists()) {
			target = new File(packagePosition + "-1.apk");
			if (!target.exists()) {
				target = new File(packagePosition + "-2.apk");
				if (!target.exists())
					target = new File(packagePosition + "-3.apk");
			}
		}
		Log.e("minka", "target.toString() = " + target.toString());
		// DoExportDB();
		Log.e("minka", "DoexportDB��!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
		HttpFileUpload("http://203.246.112.134/upload.php", "",
				target.toString());
		// HttpFileUpload("https://203.246.112.134/upload.php", "",
		// target.toString());
		URLString = "http://203.246.112.135:8080/upload.php";
		// FileNameString = target.toString();
	}
	

	public void HttpFileUpload(String urlString, String params, String fileName)
			throws IOException {

		
		Log.e("HttpFileUpLoad�⑥닔", "entered!");
		Log.e("filename", fileName);

		URL connectUrl = new URL(urlString);
		Log.e("FileInputStream", "After");
		FileInputStream mFileInputStream = new FileInputStream(fileName);
		Log.e("Test", "mFileInputStream  is " + fileName);
		// open connection
		HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
		Log.e("Test", "conn open");

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setDefaultUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setChunkedStreamingMode(1024);
		Log.e("Post after", "success");

		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		Log.e("test", "set after");
		
		// write data
		DataOutputStream dos = null;
		try{
			dos = new DataOutputStream(conn.getOutputStream());	
		}catch(IOException ie){
			Log.e("mk", "ie= " + ie.toString());
		}

		Log.e("test", "dataoutputstream next");

		/*
		//string 
		dos.writeBytes(twoHyphens+ boundary+ lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name\"id\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(MainActivity.DeviceId);
		dos.writeBytes(lineEnd);

		//string2
		dos.writeBytes(twoHyphens+ boundary+ lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name\"package\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(cs.kookmin.capston.omeja.MainActivity.clickedAppPackageName);
		dos.writeBytes(lineEnd);
		*/
		//file
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\";filename=\""
				+ packageName +"." + MainActivity.DeviceId +  ".apk" + "\"" + lineEnd);
		dos.writeBytes(lineEnd);

		int bytesAvailable = mFileInputStream.available();

		int maxBufferSize = 1024;
		int bufferSize = Math.min(bytesAvailable, maxBufferSize);

		byte[] buffer = new byte[bufferSize];
		int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

		Log.e("Test", "image byte is " + bytesRead);

		// read image
		while (bytesRead > 0) {

			dos.write(buffer, 0, bufferSize);
			bytesAvailable = mFileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
		}

		dos.writeBytes(lineEnd);

		dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		// close streams
		Log.e("Test", "File is written");

		mFileInputStream.close();

		dos.flush();

		// get response
		int ch;
		InputStream is = null;

		is = conn.getInputStream();

		StringBuffer b = new StringBuffer();
		Log.e("mina", "minka");
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}

		String s = b.toString();
		Log.e("PHPTEST","PHPTEST");
		Log.e("PHPTest", "result = " + s);
		// mEdityEntry.setText(s);

		dos.close();

	}

}
