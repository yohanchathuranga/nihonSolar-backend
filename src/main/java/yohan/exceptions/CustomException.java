package yohan.exceptions;

/**
 *
 * @author yohanr
 */
public class CustomException extends Exception {

    final static int ALREADY_EXIST = 501;
    final static int INVALID_INPUT = 506;
    final static int DATABASE_FAILED = 511;
    final static int DOES_NOT_EXIST = 512;
    final static int UNKNOWN_FAILED = 513;
    final static int SERVER_FAILED = 510;
    final static int LOGIN_FAILED = 514;

    int errorNo;
    String errorMsg;
    String errorType;
    String error;

    public CustomException(int errorNo, String errorMsg, String errorType, String error) {
        super(error + ";" + errorMsg);

        this.errorNo = errorNo;
        this.errorMsg = errorMsg;
        this.errorType = errorType;
        this.error = error;

    }

    public int getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(int errorNo) {
        this.errorNo = errorNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
