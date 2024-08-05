import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.pulsar.PulsarComponent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PulsarCamelExample {

    public static void main(String[] args) throws Exception {
        BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(10000); // Queue to store messages

        CamelContext camelContext = new DefaultCamelContext();

        // Configure Pulsar component
        PulsarComponent pulsarComponent = new PulsarComponent();
        pulsarComponent.setServiceUrl("pulsar://localhost:6650");
        camelContext.addComponent("pulsar", pulsarComponent);

        // Add Camel route
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("pulsar:my-topic?subscriptionName=my-subscription&consumerQueueSize=1000")
                    .routeId("pulsarRoute")
                    .threads(10)
                    .process(exchange -> {
                        String message = exchange.getIn().getBody(String.class);
                        messageQueue.put(message); // Add message to the blocking queue
                    })
                    .to("seda:processMessages");

                from("seda:processMessages")
                    .routeId("fileWriteRoute")
                    .process(exchange -> {
                        while (!messageQueue.isEmpty()) {
                            String message = messageQueue.take();
                            exchange.getIn().setBody(message + "\n");
                        }
                    })
                    .to("file://path/to/output/directory?fileName=messages.txt&fileExist=Append");
            }
        });

        // Start Camel context
        camelContext.start();
        Thread.sleep(5000); // Allow time for messages to be processed
        camelContext.stop();
    }
}
