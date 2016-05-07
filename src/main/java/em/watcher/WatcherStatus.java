package em.watcher;

public class WatcherStatus {
    public int status = 0;
    public static final int ok = 0;
    public static final int account_or_password_error = 1;
    public static final int invalid_char = 2;
    public static final int account_aldeady_exist = 3;

    public WatcherStatus() {
    }

}
