package kr.ac.kookmin.cs.cap3.adapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//http://112.108.40.125:8080/gcm-server/mvs/result/112.108.40.220_201311182102_vulns.txt
public class DownloadFileAsync extends AsyncTask<String, Integer, String> {

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
		for(int i = 0 ; i < arr.size(); i++){
			DownloadFromUrl("http://112.108.40.125:8080/gcm-server/mvs/result/"+arr.get(i), arr.get(i));
			
		}
		return null;
	}
	 @Override
	    protected void onPostExecute(String result) {
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
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