package model;

/**
 * Created by zsq on 16/12/2.
 */
public class ResultJson {

    private boolean result;
    private String message;
    private int code;

    public ResultJson() {
        super();
    }

    public ResultJson(boolean result) {
        this.result = result;
    }

    public ResultJson(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public ResultJson(boolean result, String message, int code) {
        this.result = result;
        this.message = message;
        this.code = code;
    }

    public ResultJson(boolean result, int code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    public ResultJson(boolean result, int code) {
        this.result = result;
        this.code = code;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        StringBuffer rs = new StringBuffer(100);
        rs.append("{result:")
                .append(result)
                .append(",message:\"")
                .append(message)
                .append("\",code:")
                .append(code)
                .append("}");
        return rs.toString();
    }
}
