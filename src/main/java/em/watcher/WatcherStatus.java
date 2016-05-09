package em.watcher;

public class WatcherStatus {
    public int status = 0;
    private Exception exception;
    public static final int ok = 0;
    public static final int account_or_password_error = 1;
    public static final int invalid_char = 2;
    public static final int account_aldeady_exist = 3;
    public static final int except = 4;

    public WatcherStatus() {
    }

    public void setException(Exception exception) {
        this.status = except;
        this.exception = exception;
    }

    public String getInfo() {
        this.status = ok;
        if (exception == null) {
            return null;
        } else
            return exception.getMessage();
    }
}
