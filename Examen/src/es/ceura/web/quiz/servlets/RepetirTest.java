package es.ceura.web.quiz.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ceura.web.quiz.utils.CookieUtilities;


@WebServlet("/repetir-test")
public class RepetirTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RepetirTest() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie questionCookie = CookieUtilities.getCookie(request, "correct-questions");
		if(questionCookie != null){
			questionCookie.setMaxAge(0);
		}
		response.setContentType("text/html");
		response.addCookie(questionCookie);
		response.sendRedirect("view-quiz");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
