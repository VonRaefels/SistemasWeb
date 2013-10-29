package es.ceura.web.quiz.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Quiz implements Iterable<Question>{

	private HashMap<Integer, Question> questions;
	
	public Quiz(){
		questions = new HashMap<>();
	}
	
	public void addQuestion(Question question){
		questions.put(question.getId(), question);
	}
	
	public Question getQuestion(int id){
		return questions.get(id);
	}

	public ArrayList<Question> getQuestions(){
		return new ArrayList<Question>(questions.values());
	}
	@Override
	public Iterator<Question> iterator() {
		ArrayList<Question> questionList = new ArrayList<>(questions.values());
		return questionList.iterator();
	}
}
