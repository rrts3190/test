import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@CamelSpringBootTest
@SpringBootTest(classes = PulsarToFileApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PulsarToFileRouteTest {

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:fileOutput")
    private MockEndpoint mockEndpoint;

    @Autowired
    private ProducerTemplate producerTemplate;

    private final Path outputPath = Paths.get(System.getProperty("user.home"), "output", "output.txt");

    @BeforeEach
    public void setUp() throws Exception {
        if (Files.exists(outputPath)) {
            Files.delete(outputPath);
        }
        // Advise the route to mock the file endpoint
        camelContext.getRouteDefinition("PulsarToFileRoute")
            .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    interceptSendToEndpoint("file:*")
                        .to("mock:fileOutput")
                        .skipSendToOriginalEndpoint();
                }
            });
        mockEndpoint.reset();
    }

    @Test
    public void testPulsarToFileRoute() throws Exception {
        String message = "Hello, Pulsar!";

        // Send a message to the Pulsar topic
        producerTemplate.sendBody("pulsar:my-topic?subscriptionName=my-subscription", message);

        // Assert that the message was received by the mock endpoint
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.message(0).body().isEqualTo(message);
        mockEndpoint.assertIsSatisfied();

        // Additionally, assert that the file was created and contains the expected message
        assertTrue(Files.exists(outputPath));
        String fileContent = Files.readString(outputPath);
        assertTrue(fileContent.contains(message));
    }
}
