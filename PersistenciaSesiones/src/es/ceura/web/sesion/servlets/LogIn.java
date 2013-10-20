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
		PrintWriter writer = response.getWriter();

		final String userName = request.getParameter("nombre");
		final String password = request.getParameter("password");
		User user = new User(userName, password);

		Validator validator = new Validator();
		if (validator.validateUser(user)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			response.setContentType("text/html");
			writer.write("<html><head></head><body><h1>Welcome, "
					+ user.getName() + "</h1><a href=\"./contador\">Continuar!</a></body></html>");
		}else{
			response.sendRedirect("./index.html");
		}
	}

}
