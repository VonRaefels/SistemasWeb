package es.ceura.web.quiz.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ceura.web.quiz.models.Quiz;
import es.ceura.web.quiz.utils.CookieUtilities;

/**
 * Servlet implementation class Validar
 */
@WebServlet("/validar")
public class Validar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Validar() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Quiz quiz = (Quiz) request.getSession().getAttribute("examen");
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String paramValue = request.getParameter(names.nextElement());
			String[] data = paramValue.split("|");
			int idPregunta = Integer.parseInt(data[1]);
			int idRespuesta = Integer.parseInt(data[3]);
			if (quiz.getQuestion(idPregunta).getAnswer(idRespuesta).isCorrect()) {
				request.getSession().setAttribute(data[0], data[1]);
			}
		}
		int intentos = Integer.parseInt(CookieUtilities.getCookieValue(request,
				"intentos", "0"));
		intentos++;
		response.addCookie(new Cookie("intentos", Integer.toString(intentos)));
		request.getSession().setAttribute("validated", true);
		response.sendRedirect("view-quiz");
	}
}
