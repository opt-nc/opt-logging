package nc.opt.core.logging;

import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by 2816ARN on 06/09/2017.
 */
@Component
public class LogMetierService {

    private static final Logger LOGGER = LoggerFactory.getLogger("metiersLogger");

    public void logObject(String objectName, Object obj) {
        LOGGER.info(Markers.append(objectName, obj), "add log entry for {}", objectName);
    }

    public void logAttributes(Object obj) {
        LOGGER.info(Markers.appendFields(obj), "add log entry for {}", obj.getClass());
    }

    public void logArray(String objectName, Object... obj) {
        LOGGER.info(Markers.appendArray(objectName, obj), "add log entry for {}", objectName);
    }

    public void logEntries(Map<?, ?> map) {
        LOGGER.info(Markers.appendEntries(map), "add log entry for map");
    }

    public void logJson(String objectName, String json) {
        LOGGER.info(Markers.appendRaw(objectName, json), "add log entry for {}", objectName);
    }
}
