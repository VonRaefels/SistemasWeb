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

@WebServlet("/contador")
public class Contador extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		try {
			User user = getUserOrRedirect(session, response);
			int contador = getContadorSession(session);
			writeResponse(user, contador, response);
		} catch (NoSessionException e) {
			response.sendRedirect("index.html");
		}
	}

	private User getUserOrRedirect(HttpSession session,
			HttpServletResponse response) throws IOException, NoSessionException {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			throw new NoSessionException("User session was not found on request");
		}
		return user;
	}

	private int getContadorSession(HttpSession session) {
		Integer contador = (Integer) session.getAttribute("contador");
		if (isNull(contador)) {
			contador = 0;
		}
		contador++;
		session.setAttribute("contador", contador);
		return (int) contador;
	}
	
	private boolean isNull(Integer contador){
		return contador == null;
	}

	private void writeResponse(User user, int contador,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.write("<html><head></head><body>");
		writer.write("<h1>" + user.getName() + ", has visto esta pagina: "
				+ contador + " veces!</h1>");
		writer.write("<a href=\"./logout\">logout</a>");
		writer.write("</body></html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
