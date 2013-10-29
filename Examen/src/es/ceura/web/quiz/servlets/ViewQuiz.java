package es.ceura.web.quiz.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ceura.web.quiz.loaders.QuizLoader;
import es.ceura.web.quiz.loaders.TemplateLoader;
import es.ceura.web.quiz.models.Answer;
import es.ceura.web.quiz.models.Question;
import es.ceura.web.quiz.models.Quiz;
import es.ceura.web.quiz.utils.CookieUtilities;

@WebServlet("/view-quiz")
public class ViewQuiz extends HttpServlet {

	private static final long serialVersionUID = -7642337079926751380L;
	private Quiz quiz;

	public ViewQuiz() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		String quizPath = this.getServletContext().getRealPath("/quiz.txt");
		try {
			quiz = QuizLoader.loadQuiz(quizPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String preguntaTemplate = TemplateLoader.loadEmptyTemplate(this);
		String respuestaTemplate = TemplateLoader.loadEmptyAnswerTemplate(this);
		String color = "white";
		request.getSession().setAttribute("examen", quiz);
		if (!isNewQuiz(request)) {
			preguntaTemplate = TemplateLoader.loadCorrectedTemplate(this);
			respuestaTemplate = TemplateLoader.loadCorrectedAnswerTemplate(this);
			color = "red";
		}
		Integer lastIntento = getLastIntentoValue(request);
		lastIntento = lastIntento + 1;
		response.addCookie(new Cookie("intento", Integer.toString(lastIntento)));
		StringBuilder examenBuilder = new StringBuilder();
		for (Question pregunta : quiz) {
			if(request.getSession().getAttribute(Integer.toString(pregunta.getId()))!= null){
				color = "green";
			}else{
				color = "red";
			}
			String preguntaHtml = preguntaTemplate.replaceAll(
					"\\{\\{pregunta\\}\\}", pregunta.getEnunciado());
			preguntaHtml = preguntaHtml.replaceAll("\\{\\{idPregunta\\}\\}",
					Integer.toString(pregunta.getId()));
			preguntaHtml = preguntaHtml.replaceAll("\\{\\{color\\}\\}",
					color);
			preguntaHtml = preguntaHtml.replaceAll("\\{\\{puntuacion\\}\\}",
					Double.toString(pregunta.getPuntuacion()));
			int contador = 0;
			StringBuilder respuestasBuilder = new StringBuilder();
			for (Answer respuesta : pregunta) {
				String respuestaHtml = respuestaTemplate.replaceAll(
						"\\{\\{respuesta\\}\\}", respuesta.getText());
				respuestaHtml = respuestaHtml.replaceAll(
						"\\{\\{idRespuesta\\}\\}", Integer.toString(contador));
				respuestaHtml = respuestaHtml.replaceAll(
						"\\{\\{idPregunta\\}\\}",
						Integer.toString(pregunta.getId()));
				respuestasBuilder.append(respuestaHtml);
				contador++;
			}
			preguntaHtml = preguntaHtml.replaceAll("\\{\\{respuestas\\}\\}",
					respuestasBuilder.toString());
			examenBuilder.append(preguntaHtml);
		}
		request.getSession().setAttribute("validated", false);
		response.setContentType("text/html");
		writer.write("<html><head><title>Quiz!</title></head><body>");
		writer.write("<h1>Quiz!</h1>");
		writer.write("<form action=\"validar\" method=\"POST\">");
		writer.write(examenBuilder.toString());
		writer.write("</br>");
		if(!isNewQuiz(request)){
			writer.write("<a href=\"ver-intentos\">Ver intentos!</a>");
		}else{
			writer.write("<input type=\"submit\" value=\"submit\">");
		}
		writer.write("</form>");
		writer.write("<a href=\"ver-intentos\">Ver intentos!</a>");
		writer.write("</body></html>");
	}

	private boolean isNewQuiz(HttpServletRequest req) {
		return req.getSession().getAttribute("validated") == null;
	}

	private Integer getLastIntentoValue(HttpServletRequest request) {

		Integer lastIntento = Integer.parseInt(CookieUtilities.getCookieValue(request, "intentos", "0"));
		if (lastIntento == null) {
			lastIntento = 0;
		}
		return lastIntento;
	}
}
