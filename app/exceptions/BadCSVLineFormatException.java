package exceptions;

public class BadCSVLineFormatException extends Exception {

    public String reason;

    public BadCSVLineFormatException(String reason, Object... args) {
        this.reason = format(reason);
    }

    @Override
    public String getMessage() {
        return this.reason;
    }

    public static String format(String msg, Object... args) {
        if (args != null && args.length > 0) {
            return String.format(msg, args);
        }
        return msg;
    }
}
