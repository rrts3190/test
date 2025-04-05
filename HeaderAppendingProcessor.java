import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeaderAppendingProcessor implements Processor {
    private static final String FILE_PATH = "output/output.csv";
    private static final String HEADER = "Name,Age\n";
    private static final AtomicBoolean headerWritten = new AtomicBoolean(false);

    @Override
    public void process(Exchange exchange) throws Exception {
        // Only do the header logic once
        if (!headerWritten.get()) {
            synchronized (HeaderAppendingProcessor.class) {
                if (!headerWritten.get()) {
                    File file = new File(FILE_PATH);
                    file.getParentFile().mkdirs();
                    if (!file.exists() || file.length() == 0) {
                        // Prepend the header to the body
                        String body = exchange.getIn().getBody(String.class);
                        exchange.getIn().setBody(HEADER + body);
                    }
                    headerWritten.set(true);
                }
            }
        }
    }
}
