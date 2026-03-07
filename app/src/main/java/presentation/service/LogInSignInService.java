package presentation.service;

import core.engine.Engine;
import presentation.enums.SuccessType;

public class LogInSignInService extends AService{

	public LogInSignInService(Engine engine) {
		super(engine);
	}

	public SuccessType signIn(String userName, String password, String confirmPassword) {
		if(userName.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
			return SuccessType.FAILURE_MISSING_FIELDS;
		}
		if(engine.userAlreadyExists(userName)) {
			return SuccessType.FAILURE_USER_EXISTS;
		}
		if(!password.equals(confirmPassword)) {
			return SuccessType.FAILURE_PASSWORD_MISMATCH;
		}
		if(password.length() < 8) {
			return SuccessType.FAILURE_PASSWORD_TOO_SHORT;
		}
		if(!isStrongPassword(password)) {
			return SuccessType.FAILURE_PASSWORD_MISSING_SYMBOL;
		}

		engine.newUserAccount(userName, password);
		return SuccessType.SIGN_IN_SUCCESS;
	}

	public SuccessType logIn(String userName, String password) {
		if(password.length() == 0 || userName.length() == 0) {
			return SuccessType.FAILURE_MISSING_FIELDS;
		}
		if(!engine.userAlreadyExists(userName)) {
			return SuccessType.FAILURE_USER_NOT_FOUND;
		}
		if(!engine.authenticateUser(userName, password)) {
			return SuccessType.FAILURE_INCORRECT_PASSWORD;
		}

		return SuccessType.LOG_IN_SUCCESS;
	}

    public void resetCurrentUser() {
        engine.setCurrentUser("");
    }

    public SuccessType logInCurrentUser() {
       if(engine.getCurrentUser() != null) {
			return SuccessType.LOG_IN_SUCCESS;
	   } else {
			return SuccessType.FAILURE_CURRENT_USER_NOT_EXIST;
	   }
    }

    public String getCurrentUserName() {
        return engine.getCurrentUserName();
    }

	private boolean isStrongPassword(String password) {
		String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[\\W_]).{8,}$";
		return password.matches(pattern);
	}
}
