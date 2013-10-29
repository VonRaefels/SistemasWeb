package es.ceura.web.quiz.models.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class UserResult implements Serializable, Iterable<UserAnswer> {

	private static final long serialVersionUID = 2805088767700069286L;
	private ArrayList<UserAnswer> userAnswers;

	public UserResult() {
		userAnswers = new ArrayList<>();
	}

	public void add(UserAnswer answer) {
		userAnswers.add(answer);
	}

	@Override
	public Iterator<UserAnswer> iterator() {
		return userAnswers.iterator();
	}

	public boolean isEmpty() {
		return userAnswers.isEmpty();
	}

}
