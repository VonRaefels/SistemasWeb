package es.ceura.web.quiz.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ceura.web.quiz.models.user.UserAnswer;
import es.ceura.web.quiz.models.user.UserResult;
import es.ceura.web.quiz.utils.CookieUtilities;

@WebServlet("/ver-intentos")
public class Intentos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

	public Intentos() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserResult result = getResult(request);
		response = prepareResponse(response);
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder = writeHtmlHeader(htmlBuilder);
		if (result.isEmpty()) {
			htmlBuilder.append("<h2>No hay intentos</h2>");
		} else {
			htmlBuilder = writeResponses(htmlBuilder, result);
		}
		htmlBuilder = writeHtmlFooter(htmlBuilder);
		response.getWriter().write(htmlBuilder.toString());
	}

	private UserResult getResult(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserResult result = (UserResult) session.getAttribute("result");
		if (result == null) {
			result = new UserResult();
		}
		return result;
	}

	private HttpServletResponse prepareResponse(HttpServletResponse response) {
		response.setContentType("text/html");
		return response;
	}

	private StringBuilder writeHtmlHeader(StringBuilder htmlBuilder) {
		return htmlBuilder.append("<html><head><title>Intentos!</title></head><body><h1>Tus Intentos!</h1>");
	}

	private StringBuilder writeHtmlFooter(StringBuilder htmlBuilder) {
		return htmlBuilder.append("</body></html>");
	}

	private StringBuilder writeResponses(StringBuilder htmlBuilder, UserResult result) {
		for (UserAnswer answer : result) {
			htmlBuilder.append("<div class=\"response\"><label>");
			htmlBuilder.append(dateFormater.format(answer.getDate()) + " - " + Integer.toString(answer.getHits())
					+ " Acierto(s)");
			htmlBuilder.append("</label></div></br>");
		}
		return htmlBuilder;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
	}

}
