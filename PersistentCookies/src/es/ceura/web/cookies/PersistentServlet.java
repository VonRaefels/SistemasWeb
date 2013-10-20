package es.ceura.web.cookies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index")
public class PersistentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Properties sessionData;

	public PersistentServlet() {
		super();
		sessionData = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(
					"/home/jorge-mint/Documents/Java2EE/Cookies/PersistentCookies/session.properties");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			if (inputStream != null) {
				sessionData.load(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String clientIP = request.getRemoteAddr();
		Cookie cookie = CookieUtilities.getCookie(request, "sessionCount");
		int sessionCount;
		if (cookie == null) {
			sessionCount = Integer.parseInt(sessionData.getProperty(clientIP,
					"0"));
		} else {
			sessionCount = Integer.parseInt(cookie.getValue());
		}
		sessionCount++;
		sessionData.setProperty(clientIP, String.valueOf(sessionCount));
		saveProperties();
		response.addCookie(new Cookie("sessionCount", String
				.valueOf(sessionCount)));
		response.setContentType("text/html");
		response.getWriter().write("<h1>" + sessionCount + "</h1>");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void saveProperties() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(
					new File(
							"/home/jorge-mint/Documents/Java2EE/Cookies/PersistentCookies/session.properties"));
			sessionData.store(out, "");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
