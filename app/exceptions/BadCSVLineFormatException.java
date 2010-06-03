package exceptions;

import java.util.HashMap;
import java.util.Map;

public class BadCSVLineFormatException extends Exception {

    public String messageKey;
    public String jsonObject;

    public BadCSVLineFormatException(String messageKey, String jsonObject) {
        this.messageKey = messageKey;
        this.jsonObject = jsonObject;
    }

}
