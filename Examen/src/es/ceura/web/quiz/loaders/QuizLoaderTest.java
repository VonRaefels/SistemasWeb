package es.ceura.web.quiz.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;

import es.ceura.web.quiz.models.Answer;
import es.ceura.web.quiz.models.Question;
import es.ceura.web.quiz.models.Quiz;

public class QuizLoaderTest {

	public static void main(String[] args) {
		String path = "/home/jorge/quiz.txt";
		try {
			Quiz quiz = QuizLoader.loadQuiz(path);
			for(Question question : quiz){
				System.out.println("Id: " + question.getId());
				System.out.println("Enunciado: " + question.getEnunciado());
				System.out.println("Puntuación: " + question.getPuntuacion());
				for(Answer answer : question){
					System.out.print("----Respuesta: " + answer.getText());
					if(answer.isCorrect()){
						System.out.print(" - Correcta");	
					}
					System.out.print("\n");
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
