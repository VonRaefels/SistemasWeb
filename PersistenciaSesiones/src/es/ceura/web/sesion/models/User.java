package es.ceura.web.sesion.models;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 2317234742757756794L;
	private String name;
	private String pwd;

	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setName(String name) {
		this.name = name;
	}
}
