package em.watcher;

import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes({Sessionable.Status, Sessionable.User})
public interface Sessionable {
    String Status = "curStatus";
    String User = "curUser";
}
