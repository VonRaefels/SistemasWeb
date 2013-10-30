package es.ceura.web.quiz.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ceura.web.quiz.models.Answer;
import es.ceura.web.quiz.models.Question;
import es.ceura.web.quiz.models.Quiz;
import es.ceura.web.quiz.models.user.UserAnswer;
import es.ceura.web.quiz.models.user.UserResult;

@WebServlet("/validar")
public class Validar extends HttpServlet {

	private static final long serialVersionUID = -4472361431625796788L;

	public Validar() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		Quiz quiz = ViewQuiz.getCurrentQuiz();
		HashMap<Integer, Integer> questions = parsePostQuestions(request);
		HashSet<Integer> correctQuestions = validateQuestions(questions, quiz);
		addTryToSession(request, correctQuestions);
		response = addCookie(response, correctQuestions);
		response.sendRedirect("view-quiz");
	}

	private HashMap<Integer, Integer> parsePostQuestions(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		HashMap<Integer, Integer> questions = new HashMap<>();
		while (names.hasMoreElements()) {
			String paramValue = request.getParameter(names.nextElement());
			String[] data = paramValue.split(":");
			int idPregunta = Integer.parseInt(data[0]);
			int idRespuesta = Integer.parseInt(data[1]);
			questions.put(idPregunta, idRespuesta);
		}
		return questions;
	}

	private HashSet<Integer> validateQuestions(HashMap<Integer, Integer> questions, Quiz quiz) {
		HashSet<Integer> correctQuestions = new HashSet<>();
		Iterator<Entry<Integer, Integer>> it = questions.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> questionEntry = (Map.Entry<Integer, Integer>) it.next();
			int idPregunta = questionEntry.getKey();
			int idRespuesta = questionEntry.getValue();
			if (isCorrect(idPregunta, idRespuesta, quiz)) {
				correctQuestions.add(idPregunta);
			}
		}
		return correctQuestions;
	}

	private boolean isCorrect(int idPregunta, int idRespuesta, Quiz quiz) {
		Question question = quiz.getQuestion(idPregunta);
		Answer answer = question.getAnswer(idRespuesta);
		return answer.isCorrect();
	}

	private void addTryToSession(HttpServletRequest request, HashSet<Integer> correctQuestions) {
		HttpSession session = request.getSession();
		Object responsesObject = session.getAttribute("result");
		UserResult userResult;
		if (responsesObject != null) {
			userResult = (UserResult) responsesObject;
		} else {
			userResult = new UserResult();
		}
		session.setAttribute("result", userResult);
		userResult.add(new UserAnswer(new Date(), correctQuestions.size()));
	}

	private HttpServletResponse addCookie(HttpServletResponse response, HashSet<Integer> correctQuestions) {
		Cookie correctQuestionsCookie = new Cookie("correct-questions", "");
		if (!correctQuestions.isEmpty()) {
			String correctQuestionsString = toString(correctQuestions);
			correctQuestionsCookie.setValue(correctQuestionsString);
		}
		response.addCookie(correctQuestionsCookie);
		return response;
	}

	private String toString(HashSet<Integer> questions) {
		StringBuilder correctQuestionsBuilder = new StringBuilder();
		for (Integer questionId : questions) {
			correctQuestionsBuilder.append(Integer.toString(questionId));
			correctQuestionsBuilder.append(",");
		}
		correctQuestionsBuilder = deleteLastComa(correctQuestionsBuilder);
		return correctQuestionsBuilder.toString();
	}

	private StringBuilder deleteLastComa(StringBuilder correctQuestionsBuilder) {
		return correctQuestionsBuilder.deleteCharAt(correctQuestionsBuilder.length() - 1);
	}
}
