package nc.opt.core.logging;

/**
 * Created by 2816ARN on 06/09/2017.
 */
public class LogObject {

    private String endpoint;

    private long size;

    public LogObject(String endpoint, long size) {
        this.endpoint = endpoint;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
