package em.watcher.device;

public class Device {
    private int deviceId;
    private char[] deviceKey;
    private boolean isOnline;

    public Device(int deviceId) {
        this.deviceId = deviceId;
        isOnline = false;
    }

    public void authenticate(char[] deviceKey) {
        isOnline = true;
    }

}
