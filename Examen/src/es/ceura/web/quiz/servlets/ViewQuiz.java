package es.ceura.web.quiz.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ceura.web.quiz.loaders.QuizLoader;
import es.ceura.web.quiz.loaders.TemplateLoader;
import es.ceura.web.quiz.models.Answer;
import es.ceura.web.quiz.models.Question;
import es.ceura.web.quiz.models.Quiz;
import es.ceura.web.quiz.utils.CookieUtilities;

@WebServlet("/view-quiz")
public class ViewQuiz extends HttpServlet {

	private static final long serialVersionUID = -7642337079926751380L;
	private static Quiz quiz;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ArrayList<Integer> correctQuestions = parseQuestionsCookie(request);
		final boolean isNewQuiz = CookieUtilities.getCookie(request, "correct-questions") == null;
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder = prepareHtmlHeader(htmlBuilder, isNewQuiz);
		htmlBuilder = prepareHtmlBody(htmlBuilder, isNewQuiz, correctQuestions);
		htmlBuilder = prepareHtmlFooter(htmlBuilder, isNewQuiz);
		response = prepareResponse(response);
		writeResponse(response, htmlBuilder.toString());
	}

	private ArrayList<Integer> parseQuestionsCookie(HttpServletRequest request) {
		ArrayList<Integer> correctQuestions = new ArrayList<>();
		String correctQuestionsString = CookieUtilities.getCookieValue(request, "correct-questions", "");
		String[] questions = "".equals(correctQuestionsString) ? new String[0]: correctQuestionsString.split(",");
		for (String question : questions) {
			correctQuestions.add(Integer.parseInt(question));
		}
		return correctQuestions;
	}

	private HttpServletResponse prepareResponse(HttpServletResponse response) {
		response.setContentType("text/html");
		return response;
	}

	private StringBuilder prepareHtmlHeader(StringBuilder html, boolean isNewQuiz) throws IOException {
		html.append("<html><head><title>Quiz!</title></head><body>");
		html.append("<h1>Quiz!</h1>");
		if (isNewQuiz) {
			html.append("<form action=\"validar\" method=\"POST\">");
		}
		return html;
	}

	private StringBuilder prepareHtmlBody(StringBuilder html, boolean isNewQuiz, ArrayList<Integer> correctQuestions)
			throws IOException {
		String preguntaTemplate = isNewQuiz ? TemplateLoader.loadEmptyTemplate(this) : TemplateLoader
				.loadCorrectedTemplate(this);
		String respuestaTemplate = isNewQuiz ? TemplateLoader.loadEmptyAnswerTemplate(this) : TemplateLoader
				.loadCorrectedAnswerTemplate(this);
		String htmlBody = createQuestionsHtml(preguntaTemplate, respuestaTemplate, correctQuestions, isNewQuiz);
		html.append(htmlBody);
		return html;
	}

	private String createQuestionsHtml(String preguntaTemplate, String respuestaTemplate,
			ArrayList<Integer> correctQuestions, boolean isNewQuiz) {
		StringBuilder examenBuilder = new StringBuilder();
		for (Question pregunta : quiz) {
			String preguntaHtml = parsePreguntaTemplate(preguntaTemplate, pregunta, correctQuestions, isNewQuiz);
			String respuestasHtml = parseRespuestasTemplate(respuestaTemplate, pregunta);
			preguntaHtml = preguntaHtml.replaceAll("\\{\\{respuestas\\}\\}", respuestasHtml);
			examenBuilder.append(preguntaHtml);
		}
		return examenBuilder.toString();
	}

	private String parsePreguntaTemplate(String preguntaTemplate, Question pregunta,
			ArrayList<Integer> correctQuestions, boolean isNewQuiz) {
		String color = "white";
		if (!isNewQuiz) {
			color = "red";
			if (correctQuestions.contains(pregunta.getId())) {
				color = "green";
			}
		}
		String preguntaHtml = preguntaTemplate.replaceAll("\\{\\{pregunta\\}\\}", pregunta.getEnunciado());
		preguntaHtml = preguntaHtml.replaceAll("\\{\\{idPregunta\\}\\}", Integer.toString(pregunta.getId()));
		preguntaHtml = preguntaHtml.replaceAll("\\{\\{puntuacion\\}\\}", Double.toString(pregunta.getPuntuacion()));
		preguntaHtml = preguntaHtml.replaceAll("\\{\\{color\\}\\}", color);
		return preguntaHtml;
	}

	private String parseRespuestasTemplate(String respuestaTemplate, Question pregunta) {
		int contador = 0;
		StringBuilder respuestasBuilder = new StringBuilder();
		for (Answer respuesta : pregunta) {
			String respuestaHtml = respuestaTemplate.replaceAll("\\{\\{respuesta\\}\\}", respuesta.getText());
			respuestaHtml = respuestaHtml.replaceAll("\\{\\{idRespuesta\\}\\}", Integer.toString(contador));
			respuestaHtml = respuestaHtml.replaceAll("\\{\\{idPregunta\\}\\}", Integer.toString(pregunta.getId()));
			respuestasBuilder.append(respuestaHtml);
			contador++;
		}
		return respuestasBuilder.toString();
	}

	private StringBuilder prepareHtmlFooter(StringBuilder htmlBuilder, boolean isNewQuiz) {
		htmlBuilder.append("</br>");
		if (isNewQuiz) {
			htmlBuilder.append("<input type=\"submit\" value=\"submit\">");
			htmlBuilder.append("</form>");
		} else {
			// invalidateCookie();
			htmlBuilder.append("<a href=\"repetir-test\">Repetir Test!</a>");
			htmlBuilder.append("</br></br>");
		}
		htmlBuilder.append("<a href=\"ver-intentos\">Ver intentos!</a>");
		htmlBuilder.append("</body></html>");
		return htmlBuilder;
	}

	private void writeResponse(HttpServletResponse response, String html) throws IOException {
		response.getWriter().write(html);
	}

	public static Quiz getCurrentQuiz() {
		return quiz;
	}
}
