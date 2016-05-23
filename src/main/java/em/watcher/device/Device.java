package em.watcher.device;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Entity
public class Device {

    final static public String CACHE = "Devices";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userAccount;
    private String name;
    private String secret;

    public Device() {

    }

    public Device(String name) {
        this.name = name;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
        String tmp = "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(userAccount.getBytes());
            tmp = byte2hex(md5.digest()) + name;
            md5.update(tmp.getBytes());
            tmp = byte2hex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.secret = tmp;
    }

    public String getUserAccount() {
        return userAccount;
    }

    private static String byte2hex(byte[] buffer) {
        String h = "";

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + temp;
        }
        return h;

    }

    public boolean authenticate(String key) {
        return getSecret().equals(key);
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) return false;
        Device device = (Device) obj;
        return Objects.equals(this.id, device.id);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public String toString() {
        return String.format("{id : %s, name : %s, user : %s, secret : %s}", id, name, userAccount, secret);
    }
}
