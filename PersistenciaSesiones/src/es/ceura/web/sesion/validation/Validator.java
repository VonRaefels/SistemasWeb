package es.ceura.web.sesion.validation;

import es.ceura.web.sesion.models.User;

public class Validator {
	
	public void validate(User user) throws InvalidUserException{
		if(!"demo".equals(user.getName()) || !"demo".equals(user.getPwd())){
			throw new InvalidUserException("Invalid User");
		}
	}
}
