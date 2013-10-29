package es.ceura.web.quiz.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;

public class TemplateLoader {

	private static String emptyTemplate;
	private static String correctedTemplate;
	private static String emptyAnswerTemplate;
	private static String correctedAnswerTemplate;

	private final static String emptyTemplateName = "empty_template";
	private final static String correctedTemplateName = "corrected_template";
	private final static String emptyAnswerTemplateName = "empty_answer_template";
	private final static String correctedAnswerTemplateName = "corrected_answer_template";

	public static String loadEmptyTemplate(HttpServlet servlet)
			throws IOException {
		if (emptyTemplate == null) {
			String path = servlet.getServletContext().getRealPath(
					"/templates/" + emptyTemplateName);
			emptyTemplate = loadTemplate(path);
		}

		return new String(emptyTemplate);
	}

	public static String loadCorrectedTemplate(HttpServlet servlet)
			throws IOException {
		if (correctedTemplate == null) {
			String path = servlet.getServletContext().getRealPath(
					"/templates/" + correctedTemplateName);
			correctedTemplate = loadTemplate(path);
		}

		return new String(correctedTemplate);
	}
	
	public static String loadEmptyAnswerTemplate(HttpServlet servlet) throws IOException{
		if (emptyAnswerTemplate == null) {
			String path = servlet.getServletContext().getRealPath(
					"/templates/" + emptyAnswerTemplateName);
			emptyAnswerTemplate = loadTemplate(path);
		}

		return new String(emptyAnswerTemplate);
	}
	public static String loadCorrectedAnswerTemplate(HttpServlet servlet) throws IOException {
		if (correctedAnswerTemplate == null) {
			String path = servlet.getServletContext().getRealPath(
					"/templates/" + correctedAnswerTemplateName);
			correctedAnswerTemplate = loadTemplate(path);
		}

		return new String(correctedAnswerTemplate);
	}

	private static String loadTemplate(String path) throws IOException {
		String template = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			fileReader = new FileReader(new File(path));
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			template = sb.toString();
		} catch (IOException ex) {
			throw ex;
		} finally {
			closeResources(fileReader, bufferedReader);
		}
		return template;
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
