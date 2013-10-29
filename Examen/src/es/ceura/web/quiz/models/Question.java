package es.ceura.web.quiz.models;

import java.util.ArrayList;
import java.util.Iterator;

public class Question implements Iterable<Answer>{

	private final String enunciado;
	private final double puntuacion;
	private final int id;
	private final ArrayList<Answer> answers;

	public Question(int id, String text, double puntuacion) {
		this.id = id;
		this.enunciado = text;
		this.puntuacion = puntuacion;
		this.answers = new ArrayList<>();
	}

	public String getEnunciado() {
		return enunciado;
	}

	public double getPuntuacion() {
		return puntuacion;
	}
	
	public void addAnswer(Answer answer){
		answers.add(answer);
	}
	
	public Answer getAnswer(int index){
		return answers.get(index);
	}

	public int getId() {
		return id;
	}
	
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	@Override
	public Iterator<Answer> iterator() {
		return answers.iterator();
	}
}
