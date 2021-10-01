package yohan.exceptions;

/**
 *
 * @author yohanr
 */
public class LoginFailedException   extends CustomException{
    
    public LoginFailedException() {
        super(LOGIN_FAILED, "Login Failed Exception.", "LoginFailedException", "Login Failed Exception");
    }
    
    public LoginFailedException(String message) {
        super(LOGIN_FAILED, message, "LoginFailedException", "Login Failed Exception");
    }
    
}
