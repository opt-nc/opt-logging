package nc.opt.core.logging;

import net.logstash.logback.marker.Markers;
import org.openjdk.jol.info.GraphLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by 2816ARN on 06/09/2017.
 */
@Component
public class LogMetierService {

    public static final String LOGGER_NAME = "metiersLogger";
    public static final String MSG = "add log entry for {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

    public void logObject(String objectName, Object obj) {
        LOGGER.info(Markers.append(objectName, obj), MSG, objectName);
    }

    public void logObject(String objectName, String endPoint, String obj) {
        LOGGER.info(Markers.append(objectName, new LogObject(endPoint, GraphLayout.parseInstance(obj).totalSize())), MSG, objectName);
    }

    public void logAttributes(Object obj) {
        LOGGER.info(Markers.appendFields(obj), MSG, obj.getClass());
    }

    public void logArray(String objectName, Object... obj) {
        LOGGER.info(Markers.appendArray(objectName, obj), MSG, objectName);
    }

    public void logEntries(Map<?, ?> map) {
        LOGGER.info(Markers.appendEntries(map), "add log entry for map");
    }

    public void logJson(String objectName, String json) {
        LOGGER.info(Markers.appendRaw(objectName, json), MSG, objectName);
    }
}
