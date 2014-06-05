package capstone.gcm.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

public class AcceptServlet extends BaseServlet {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/Metasploit";
	private static final String id = "root";
	private static final String pw = "toor";
	String status = "";
	private static final String apiKey = "AIzaSyB-6Qtym0NlJ3Ie6JowpjyFU4-xcTPp6W4";

	private static String COLLAPSE_KEY = String
			.valueOf(Math.random() % 100 + 1);
	private static boolean DELAY_WHILE_IDLE = true;
	private static int TIME_TO_LIVE = 3;
	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	private Sender sender;

	/**
	 * Creates the {@link Sender} based on the servlet settings.
	 */
	protected Sender newSender(ServletConfig config) {
		return new Sender(apiKey);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sender = newSender(config);
	}

	/**
	 * Displays the existing messages and offer the option to send a new one.
	 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		doPost(req, resp);

	}*/

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String regId = req.getParameter("regId");
		String pId = req.getParameter("DeviceId");
		System.out.println("regID:::::::::::::::::::"+regId);
		System.out.println("regID:::::::::::::::::::"+pId);
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(dbUrl, id, pw);
			Statement stmt = conn.createStatement();

			System.out.println("eeeee");
			stmt.executeUpdate("update  User_Information set admission = 1 where DeviceId='"
							+ pId + "'");

			if (regId == null) {
				System.out.println("regNULL");
			} else {
				if (regId == "1")
					return;

				System.out.println("SEND");

				Message message = new Message.Builder().collapseKey(COLLAPSE_KEY)
						.delayWhileIdle(DELAY_WHILE_IDLE).timeToLive(TIME_TO_LIVE)
						.addData("title", "LINUX SERVER VUNL ALARM SYSTEM")
						.addData("msg", "ACCEPT COMPLETE!")
						.addData("accept", "Please Setting").build();
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
