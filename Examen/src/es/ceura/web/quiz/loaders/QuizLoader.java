package es.ceura.web.quiz.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import es.ceura.web.quiz.models.Answer;
import es.ceura.web.quiz.models.Question;
import es.ceura.web.quiz.models.Quiz;

public class QuizLoader {

	public static Quiz loadQuiz(String path) throws FileNotFoundException,
			IOException {
		checkIfQuizExists(path);
		Quiz quiz = createQuiz(path);
		return quiz;
	}

	private static void checkIfQuizExists(String path)
			throws FileNotFoundException {
		if (!new File(path).exists()) {
			throw new FileNotFoundException();
		}
	}

	private static Quiz createQuiz(String path) throws IOException {
		FileReader fileReader = null;
		BufferedReader br = null;
		Quiz quiz = new Quiz();
		try {
			fileReader = new FileReader(new File(path));
			br = new BufferedReader(fileReader);

			while (hasNextQuestion(br.readLine())) {
				Question question = parseQuestion(br);
				quiz.addQuestion(question);
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			closeResources(fileReader, br);
		}
		return quiz;
	}

	private static boolean hasNextQuestion(String line) {
		return line != null && "start_question".equals(line);
	}

	private static Question parseQuestion(BufferedReader br)
			throws NumberFormatException, IOException {

		int id = Integer.parseInt(br.readLine());
		String enunciado = br.readLine();
		double puntuacion = Double.parseDouble(br.readLine());
		int validAnswer = Integer.parseInt(br.readLine());

		Question question = new Question(id, enunciado, puntuacion);
		question = readAnswersIntoQuestion(br, question, validAnswer);
		return question;
	}

	private static Question readAnswersIntoQuestion(BufferedReader br, Question question,
			int correctAnswerIndex) throws IOException {
		String line;
		int counter = 1;
		Answer answer;
		while (hasMoreAnswers(line = br.readLine())) {
			boolean isCorrectAnswer = isCorrectAnswer(counter,
					correctAnswerIndex);
			answer = new Answer(line, isCorrectAnswer);
			question.addAnswer(answer);
			counter++;
		}
		return question;
	}

	private static boolean isCorrectAnswer(int counter, int correctAnswerIndex) {
		return counter == correctAnswerIndex;
	}

	private static boolean hasMoreAnswers(String line) {
		return line != null && !"end_question".equals(line);
	}

	private static void closeResources(FileReader fr, BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException ignore) {
			}
		}
		if (fr != null) {
			try {
				fr.close();
			} catch (IOException ignore) {
			}
		}
	}

}
