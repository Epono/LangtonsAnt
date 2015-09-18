package main;

public class ReturnObject<T> {
    private T      result;
    private String optionalMessage;

    public ReturnObject() {
        super();
    }

    public ReturnObject(T result, String optionalMessage) {
        super();
        this.result = result;
        this.optionalMessage = optionalMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getOptionalMessage() {
        return optionalMessage;
    }

    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }
}
