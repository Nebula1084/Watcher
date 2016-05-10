package em.watcher;

import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.report.ReportDef;
import em.watcher.user.ContentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static em.watcher.conroller.PacketController.*;

@Component
public class PacketValidator {
    @Autowired
    ContentMatcher contentMatcher;

    public boolean validatePacket(Map<String, String> params, WatcherPacketDef packetDef) throws IllegalArgumentException {
        Set<String> fieldSet = packetDef.getField();
        Set<String> testSet = new HashSet<>();
        testSet.add(AUTH_ID);
        testSet.add(AUTH_KEY);
        testSet.add(DEVICE_ID);
        if (packetDef instanceof ReportDef)
            testSet.add(REPORT_ID);
        else if (packetDef instanceof ControlDef) {
            testSet.add(CONTROL_ID);
            testSet.add(SR);
        }
        testSet.addAll(fieldSet);
        Set<String> requestSet = params.keySet();
        if (!Objects.equals(testSet, requestSet))
            throw new IllegalArgumentException("Parameter format is not correct. Expect " + testSet + ". But receive " + requestSet);
        for (String field : fieldSet) {
            String value = params.get(field);
            if (!contentMatcher.validate(value))
                throw new IllegalArgumentException("Parameter contains invalid character.");
        }
        if (packetDef instanceof ControlDef) {
            String sr = params.get(SR);
            if (!sr.equals(ControlPacket.Recv) && !sr.equals(ControlPacket.Send))
                throw new IllegalArgumentException("SR is not S or R.");
        }
        return true;
    }
}
