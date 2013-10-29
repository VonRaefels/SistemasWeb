package es.ceura.web.quiz.models.user;

import java.io.Serializable;
import java.util.Date;

public class UserAnswer implements Serializable {
	private static final long serialVersionUID = 6364844588739413006L;
	private final Date date;
	private final int hits;

	public UserAnswer(Date date, int hits) {
		this.date = date;
		this.hits = hits;
	}

	public Date getDate() {
		return date;
	}

	public int getHits() {
		return hits;
	}
}
