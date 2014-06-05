package capstone.gcm.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class getApkServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final String apiKey = "AIzaSyB-6Qtym0NlJ3Ie6JowpjyFU4-xcTPp6W4";
	private static final long serialVersionUID = 6177597294505073859L;
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/Metasploit";
	private static final String id = "root";
	private static final String pw = "toor";
	String status = "";
	
	String DeviceId="";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// response to Android
		PrintWriter out = resp.getWriter();
		
		System.out.println("Start");
		DeviceId=req.getParameter("DeviceId");
		System.out.println(DeviceId);
		System.out.println("dddd");
		String select ="select * from `"+DeviceId+"`";
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(dbUrl, id, pw);
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(select);
			
			while(rs.next()){
				out.println(rs.getString("Apkname"));
				System.out.println(rs.getString("Apkname"));
			}
		}catch(Exception e){
			System.out.println("DB fail");
		}
		
		
		
	}
}
