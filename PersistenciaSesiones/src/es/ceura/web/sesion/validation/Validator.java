package es.ceura.web.sesion.validation;

import es.ceura.web.sesion.models.User;

public class Validator {
	
	public boolean validateUser(User user){
		return "demo".equals(user.getName()) && "demo".equals(user.getPwd());
	}
}
