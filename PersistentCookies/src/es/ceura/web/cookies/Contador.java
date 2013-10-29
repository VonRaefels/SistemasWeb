package es.ceura.web.cookies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index")
public class Contador extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Properties sessionData;
	private static final String sessionFileName = "session.properties";

	public Contador() {
		super();

	}

	@Override
	public void init() throws ServletException {
		sessionData = new Properties();
		try {
			tryCreateSessionFile();
			loadPropertiesFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void tryCreateSessionFile() throws IOException {
		if (!sessionFileExists()) {
			getSessionFile().createNewFile();
		}
	}

	private boolean sessionFileExists() {
		return getSessionFile().exists();
	}

	private File getSessionFile() {
		return new File(Utils.getSystemHomeFolder(), sessionFileName);
	}

	private void loadPropertiesFile() throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(getSessionFile());
			sessionData.load(inputStream);
		} catch (FileNotFoundException ignore) {
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String clientIP = request.getRemoteAddr();
		Cookie cookie = CookieUtilities.getCookie(request, "sessionCount");

		int sessionCount = getSessionCount(cookie, clientIP);
		sessionCount++;

		saveSessionData(clientIP, sessionCount);

		sendResponse(sessionCount, response);
	}

	private int getSessionCount(Cookie cookie, String clientIP) {
		int sessionCount;
		if (isNull(cookie)) {
			sessionCount = Integer.parseInt(sessionData.getProperty(clientIP,
					"0"));
		} else {
			sessionCount = Integer.parseInt(cookie.getValue());
		}
		return sessionCount;
	}

	private boolean isNull(Cookie cookie) {
		return cookie == null;
	}

	private void saveSessionData(String clientIP, int sessionCount) {
		sessionData.setProperty(clientIP, String.valueOf(sessionCount));
		saveProperties();
	}

	private void sendResponse(int sessionCount, HttpServletResponse response) {
		prepareResponse(response, sessionCount);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write("<html><head><title>PersitentCookies</title></head><body>");
			writer.write("<h1>Session Count: " + sessionCount + "</h1>");
			writer.write("</body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareResponse(HttpServletResponse response, int sessionCount) {
		response.addCookie(new Cookie("sessionCount", String
				.valueOf(sessionCount)));
		response.setContentType("text/html");
	}

	private synchronized void saveProperties() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(getSessionFile());
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
