package capstone.gcm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@SuppressWarnings("serial")
public class ApkSaveServlet extends BaseServlet {
	private Sender sender;
	String DeviceId;
	String apkname;
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/Metasploit";
	private static final String id = "root";
	private static final String pw = "toor";
	String status = "";
	String regId=null;

	private static String COLLAPSE_KEY = String
			.valueOf(Math.random() % 100 + 1);
	private static boolean DELAY_WHILE_IDLE = true;
	private static int TIME_TO_LIVE = 3;
	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	private static final String apiKey = "AIzaSyB-6Qtym0NlJ3Ie6JowpjyFU4-xcTPp6W4";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sender = newSender(config);
	}

	/**
	 * Creates the {@link Sender} based on the servlet settings.
	 */
	protected Sender newSender(ServletConfig config) {
		return new Sender(apiKey);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DeviceId = req.getParameter("DeviceId");
		apkname = req.getParameter("apk");
		System.out.println("LLLLLLLLLLLLLLLLLLLO");
		System.out.println("DeviceID:" + DeviceId);
		System.out.println("apkname:" + apkname);
		try {
			Datastore.apksave(DeviceId, apkname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	Process p = new ProcessBuilder("/bin/sh", "-c",
				"/opt/android-sdk-linux/platform-tools/adb install /opt/ARE/"
						+ apkname + "/" + apkname + ".apk").start();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String s = "";
		System.out.println(s);
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}

			p = new ProcessBuilder("/bin/sh", "-c",
				"sh /opt/ARE/server_start2.sh " + apkname+".apk").start();
		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		s = "";
		System.out.println(s);
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		p = new ProcessBuilder("/bin/sh", "-c",
				"sh /opt/ARE/stack_start.sh " + apkname+".apk").start();
		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		s = "";
		System.out.println(s);
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		p = new ProcessBuilder("/bin/sh", "-c",
				"sh /opt/ARE/PreProcessing/decompile.sh " + apkname).start();
		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		s = "";
		System.out.println(s);
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}

		
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(dbUrl, id, pw);
			Statement stmt = conn.createStatement();

			String select = "Select * from User_Information where DeviceId='"+DeviceId+"'";
			ResultSet rs = stmt.executeQuery(select);
			
			if(rs.next()){
				regId = rs.getString("regId");
			}
			System.out.println("eeeee");
			
			if (regId == null) {
				System.out.println("regNULL");
			} else {
				if (regId == "1")
					return;

				System.out.println("SEND");

				Message message = new Message.Builder().collapseKey(COLLAPSE_KEY)
						.delayWhileIdle(DELAY_WHILE_IDLE).timeToLive(TIME_TO_LIVE)
						.addData("title", "APPLICATION VUNL ALARM SYSTEM")
						.addData("msg", "SENT REPORTING")
						.addData("accept", "Checking Please").build();
				/* Message message = new Message.Builder().build(); */
				Result result = sender.send(message, regId, 5);
				status = "Sent message to one device: " + result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(status);
		System.out.println("SEND");

		req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
		getServletContext().getRequestDispatcher("/home").forward(req, resp);
	}
}
