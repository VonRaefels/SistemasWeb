package es.ceura.web.sesion.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ceura.web.sesion.models.User;
import es.ceura.web.sesion.validation.InvalidUserException;
import es.ceura.web.sesion.validation.Validator;

@WebServlet("/check-user")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogIn() {
		super();

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = createUserFrom(request);

		try {
			validate(user);
			prepareSession(request, user);
			writeResponse(response, user);
			
		} catch (InvalidUserException e) {
			response.sendRedirect("error.html");
		}
	}

	private void validate(User user) throws InvalidUserException {
		new Validator().validate(user);
	}

	private User createUserFrom(HttpServletRequest request) {
		final String userName = request.getParameter("nombre");
		final String password = request.getParameter("password");
		return new User(userName, password);
	}

	private void prepareSession(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
	}

	private void writeResponse(HttpServletResponse response, User user) throws IOException {
		PrintWriter writer = response.getWriter();
		response.setContentType("text/html");
		writer.write("<html><head><title>Welcome!</title></head><body>");
		writer.write("<h1>Welcome, " + user.getName() + "</h1>");
		writer.write("<a href=\"./contador\">Continuar!</a></body></html>");
	}
}
