package es.ceura.web.quiz.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ceura.web.quiz.utils.CookieUtilities;


@WebServlet("/ver-intentos")
public class Intentos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Intentos() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		String intentos = CookieUtilities.getCookieValue(request, "intentos", "0");
		writer.write("<html><head><title>Intentos!</title></head><body>");
		writer.write(intentos);
		writer.write("</body></html>");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
