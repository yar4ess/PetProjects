package CommandManager;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    private int exitCode;
    private String message;
    private Object messageObj;

    public CommandMessage(int code, String s, Object obj) {
        exitCode = code;
        message = s;
        messageObj = obj;
    }

    public CommandMessage(int code, String s) {
        this(code, s, null);
    }

    public CommandMessage(String s, Object obj) {
        this(200, s, obj);
    }

    public CommandMessage(String s) {
        this(200, s, null);
    }
    ///НЕЛЬЗЯ УДАЛЯТЬ
    public int getExitCode() { return exitCode; }
    public String getMessage() { return message; }
    public Object getCommandMessageObj() { return messageObj; }
    public Object getMessageObj() { return messageObj; }
    public String toString() { return String.valueOf(exitCode)+";"+message+";"+(messageObj==null?"null":messageObj.toString()); }
}