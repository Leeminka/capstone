package kr.ac.kookmin.cs.cap3.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DBConnector {

	private String insertDBUrl = "";
	private String getDBURL = "http://112.108.40.125:8080/gcm-demo/return_json.jsp";

	
	private String DBurl;
	
	public DBConnector(){}
	public DBConnector(String url){
		this.DBurl = url;
	}
	// private String

	public void insertDB(String query,String serial, String username, String member,
			String number) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(DBurl);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("query", query));
		//	nameValuePairs.add(new BasicNameValuePair("pid", serial));
		//	nameValuePairs.add(new BasicNameValuePair("user", username));
		//	nameValuePairs.add(new BasicNameValuePair("member", member));
		//	nameValuePairs.add(new BasicNameValuePair("pnum", number));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			
			Log.d("JANG", response.toString());
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));

			String line = null;
			String result = new String();
			while((line = buffer.readLine())!= null){
				result += line;
			}
			
			Log.d("Befor return", result);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d("Befor return", "");
		}

	}

	public String getDB(String query) { //���� ��û�� ���� �� ������ �Ǿ����? �ȵǾ����? Ȯ��, ���� �����ڵ尡 1�Ͻ� Alertâ�� �߰� �㾾�� ����̽�? ���̵� ���? 
		
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(DBurl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("query", query));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.d("JANG QUERY", query);
			HttpResponse response = httpclient.execute(httppost);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));

			String line = null;
			String result = new String();
			while((line = buffer.readLine())!= null){
				result += line;
			}
			
			Log.d("Befor return", result);
			return result;
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("error JANG", "");
			return "0";
		}
		
	}
	
	public String parseData(String json){
		String result = "0";
		try {
			JSONArray jArray = new JSONArray(json);
			for(int i = 0 ; i <jArray.length(); i++){
				JSONObject json_data = jArray.getJSONObject(i);
				result = json_data.getString("admission");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("JANG", "JSON FAIL");
		}
		
		return result;
	}

}
