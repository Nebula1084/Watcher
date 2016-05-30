package em.watcher.user;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ContentMatcher {
    private Pattern pattern;

    public ContentMatcher() {
        pattern = Pattern.compile("[a-zA-Z0-9_.]+");
    }

    public boolean validate(String str) {
        return pattern.matcher(str).matches();
    }
}
