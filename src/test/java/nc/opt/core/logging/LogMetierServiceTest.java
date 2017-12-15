package nc.opt.core.logging;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 2816ARN on 15/12/2017.
 */
@SpringBootTest(classes = TestConfig.class)
@RunWith(SpringRunner.class)
public class LogMetierServiceTest {

    @Autowired
    private LogMetierService service;

    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private ByteArrayOutputStream myOut;

    private void assertEqualsLog(String objectName, AbstractLogObject log, TestObject... objects) throws Exception {
        Assert.assertNotNull(log);
        Assert.assertNotNull(log.timestamp);
        Assert.assertNotNull(log.version);
        Assert.assertEquals("add log entry for " + objectName, log.message);
        Assert.assertEquals(LogMetierService.LOGGER_NAME, log.loggerName);
        Assert.assertNotNull(log.threadName);
        Assert.assertEquals("INFO", log.level);
        Assert.assertNotNull(log.levelValue);
        if (objects != null) {
            if (objects.length == 1) {
                Assert.assertNotNull(((LogObject) log).test);
                Assert.assertEquals(objects[0].name, ((LogObject) log).test.name);
            } else if (objects.length > 0) {
                Assert.assertNotNull(((LogArray) log).test);
                Assert.assertEquals(objects.length, ((LogArray) log).test.size());
                int i = 0;
                for (TestObject o : objects) {
                    Assert.assertEquals(o.name, ((LogArray) log).test.get(i++).name);
                }
            }
        }
    }

    @Before
    public void init() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    @Test
    public void testLogObject() throws Exception {
        TestObject obj = new TestObject("toto");
        service.logObject("test", obj);

        //{"@timestamp":"2017-12-15T10:41:15.046+11:00","@version":1,"message":"add log entry for test","logger_name":"metiersLogger","thread_name":"main","level":"INFO","level_value":20000,"test":{"name":"toto"}}
        //On vérifie que les logs sont bien en JSON
        final String standardOutput = myOut.toString();
        LogObject log = mapper.readValue(standardOutput, LogObject.class);
        assertEqualsLog("test", log, obj);
    }

    @Test
    public void testLogArray() throws Exception {
        TestObject obj1 = new TestObject("1");
        TestObject obj2 = new TestObject("2");
        service.logArray("test", new TestObject[]{obj1, obj2});

        //{"@timestamp":"2017-12-15T10:55:09.564+11:00","@version":1,"message":"add log entry for test","logger_name":"metiersLogger","thread_name":"main","level":"INFO","level_value":20000,"test":[{"name":"1"},{"name":"2"}]}
        //On vérifie que les logs sont bien en JSON
        final String standardOutput = myOut.toString();
        LogArray log = mapper.readValue(standardOutput, LogArray.class);
        assertEqualsLog("test", log, obj1, obj2);
    }

    @Test
    public void testLogAttributes() throws Exception {
        TestObject obj1 = new TestObject("1");
        service.logAttributes(obj1);

        //{"@timestamp":"2017-12-15T11:36:09.817+11:00","@version":1,"message":"add log entry for class nc.opt.core.logging.LogMetierServiceTest$TestObject","logger_name":"metiersLogger","thread_name":"main","level":"INFO","level_value":20000,"name":"1"}
        //On vérifie que les logs sont bien en JSON
        final String standardOutput = myOut.toString();
        LogObject log = mapper.readValue(standardOutput, LogObject.class);
        assertEqualsLog("class " + TestObject.class.getName(), log);
        Assert.assertEquals("1", log.attributes.get("name"));
    }

    @Test
    public void testLogJson() throws Exception {
        TestObject obj1 = new TestObject("1");
        service.logJson("test", mapper.writeValueAsString(obj1));

        //{"@timestamp":"2017-12-15T11:55:05.818+11:00","@version":1,"message":"add log entry for test","logger_name":"metiersLogger","thread_name":"main","level":"INFO","level_value":20000,"test":{"name":"1"}}
        //On vérifie que les logs sont bien en JSON
        final String standardOutput = myOut.toString();
        LogObject log = mapper.readValue(standardOutput, LogObject.class);
        assertEqualsLog("test", log, obj1);
    }

    @Test
    public void testLogEntries() throws Exception {
        TestObject obj1 = new TestObject("1");
        Map<String, TestObject> entries = new HashMap<>();
        entries.put("test", obj1);
        service.logEntries(entries);

        //{"@timestamp":"2017-12-15T11:59:09.672+11:00","@version":1,"message":"add log entry for map","logger_name":"metiersLogger","thread_name":"main","level":"INFO","level_value":20000,"test":{"name":"1"}}
        //On vérifie que les logs sont bien en JSON
        final String standardOutput = myOut.toString();
        LogObject log = mapper.readValue(standardOutput, LogObject.class);
        assertEqualsLog("map", log, obj1);
    }

    static class TestObject {
        @JsonProperty
        String name;

        TestObject() {
        }

        TestObject(String name) {
            this.name = name;
        }
    }

    static abstract class AbstractLogObject {
        @JsonProperty("@timestamp")
        private Date timestamp;
        @JsonProperty("@version")
        private Integer version;
        @JsonProperty("message")
        private String message;
        @JsonProperty("logger_name")
        private String loggerName;
        @JsonProperty("thread_name")
        private String threadName;
        @JsonProperty("level")
        private String level;
        @JsonProperty("level_value")
        private Integer levelValue;
        public AbstractLogObject() {

        }

    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class LogObject extends AbstractLogObject {
        @JsonProperty
        private TestObject test;

        private Map<String, Object> attributes = new HashMap<>();

        public LogObject() {

        }

        @JsonAnySetter
        public void set(String fieldName, Object value) {
            this.attributes.put(fieldName, value);
        }
    }

    static class LogArray extends AbstractLogObject {
        @JsonProperty
        private List<TestObject> test;

        public LogArray() {

        }
    }

}
