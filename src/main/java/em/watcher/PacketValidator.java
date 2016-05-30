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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean validatePacket(Map<String, String> params, WatcherPacketDef packetDef) throws IllegalArgumentException {
        Set<String> fieldSet = new HashSet<>(packetDef.getField());
        Set<String> testSet = new HashSet<>();
        testSet.add(AUTH_ID);
        testSet.add(AUTH_KEY);
        testSet.add(DEVICE_ID);
        if (packetDef instanceof ReportDef)
            testSet.add(REPORT_ID);
        else if (packetDef instanceof ControlDef) {
            testSet.add(CONTROL_ID);
            testSet.add(SR);
            if (!params.keySet().contains(SR))
                throw new IllegalArgumentException("Parameter format is not correct. Expect sr");
            String sr = params.get(SR);
            if (!sr.equals(ControlPacket.Recv) && !sr.equals(ControlPacket.Send))
                throw new IllegalArgumentException("SR is not S or R.");
            if (sr.equals(ControlPacket.Recv))
                fieldSet = new HashSet<>();
            else
                testSet.add(TARGET_ID);
        }
        testSet.addAll(fieldSet);
        Set<String> requestSet = params.keySet();
        if (!Objects.equals(testSet, requestSet))
            throw new IllegalArgumentException("Parameter format is not correct. Expect " + testSet + ". But receive " + requestSet);
        for (String field : fieldSet) {
            String value = params.get(field);
            if (!contentMatcher.validate(value))
                throw new IllegalArgumentException("Parameter contains invalid character.");
            switch (packetDef.getType(field)) {
                case WatcherPacketDef.TYPE_CHAR:
                    if (value.length() != 1)
                        throw new IllegalArgumentException("String length is not same with definition.");
                    break;
                case WatcherPacketDef.TYPE_FLOAT:
                    Float.valueOf(value);
                    break;
                case WatcherPacketDef.TYPE_INT:
                    Integer.valueOf(value);
                    break;
                case WatcherPacketDef.TYPE_STRING:
                    if (packetDef.getLength(field) < value.length())
                        throw new IllegalArgumentException("String length is not same with definition.");
                    break;
            }
        }
        return true;
    }
}
