package capstone.gcm.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Skeleton class for all servlets in this package.
 */

@SuppressWarnings("serial")
abstract class BaseServlet extends HttpServlet {

	// change to true to allow GET calls
	static final boolean DEBUG = true;

	protected final Logger logger = Logger.getLogger(getClass().getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (DEBUG) {
			doPost(req, resp);
		} else {
			super.doGet(req, resp);
		}
	}

	protected String getParameter(HttpServletRequest req, String parameter)
			throws ServletException {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String value = req.getParameter(parameter);
		if (isEmptyOrNull(value)) {
			if (DEBUG) {
				StringBuilder parameters = new StringBuilder();
				Enumeration<String> names = req.getParameterNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					String param = req.getParameter(name);
					parameters.append(name).append("=").append(param).append("\n");
				}
				logger.fine("parameters: " + parameters);
			}
			throw new ServletException("Parameter " + parameter + " not found");
		}
		return value.trim();
	}

	protected String getParameter(HttpServletRequest req, String parameter,
			String defaultValue) {
		try {
			req.setCharacterEncoding("EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String value = req.getParameter(parameter);
		if (isEmptyOrNull(value)) {
			value = defaultValue;
		}
		return value.trim();
	}

	protected void setSuccess(HttpServletResponse resp) {
		setSuccess(resp, 0);
	}

	protected void setSuccess(HttpServletResponse resp, int size) {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");
		resp.setContentLength(size);
	}

	protected boolean isEmptyOrNull(String value) {
		return value == null || value.trim().length() == 0;
	}

}

