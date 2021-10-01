package yohan.exceptions;

/**
 *
 * @author yohanr
 */
public class UnknownException   extends CustomException{
    
    public UnknownException() {
        super(UNKNOWN_FAILED, "Unknown Exception.", "UnknownException", "Unknown Exception");
    }
    
    public UnknownException(String message) {
        super(UNKNOWN_FAILED, message, "UnknownException", "Database Exception");
    }
    
}
