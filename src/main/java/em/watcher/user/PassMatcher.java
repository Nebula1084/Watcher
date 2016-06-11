package em.watcher.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class PassMatcher {
    private Pattern pattern;

    public PassMatcher() {
        pattern = Pattern.compile("[a-zA-Z0-9_]+");
    }

    public boolean validate(String str) {
        Set<String> validateSet = new HashSet<>();
        validateSet.add("auth_id");
        validateSet.add("auth_key");
        validateSet.add("device_id");
        validateSet.add("control_id");
        if (validateSet.contains(str)) return false;
//        return pattern.matcher(str).matches();
        return true;
    }
}
