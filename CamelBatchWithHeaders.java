import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.List;

public class CamelBatchWithHeaders {

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();

        // Configure ActiveMQ
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // Configure DataSource
        DataSource dataSource = createDataSource();
        SqlComponent sqlComponent = new SqlComponent();
        sqlComponent.setDataSource(dataSource);
        camelContext.addComponent("sql", sqlComponent);

        // Define Camel Route
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("jms:queue:myQueue")
                    .routeId("BatchInsertWithHeadersRoute")
                    .process(exchange -> {
                        // Set headers dynamically
                        String body = exchange.getIn().getBody(String.class);
                        exchange.getIn().setHeader("recordId", extractRecordId(body));
                        exchange.getIn().setHeader("recordName", extractRecordName(body));
                        exchange.getIn().setHeader("recordType", extractRecordType(body));
                    })
                    .aggregate(constant(true), new MyAggregationStrategy())
                        .completionSize(10) // Batch size of 10
                        .completionTimeout(5000) // Timeout for batching
                    .process(exchange -> {
                        // Prepare stored procedure call
                        List<Exchange> exchanges = exchange.getIn().getBody(List.class);
                        StringBuilder sqlBuilder = new StringBuilder("CALL my_stored_procedure(");

                        for (int i = 0; i < exchanges.size(); i++) {
                            Exchange e = exchanges.get(i);
                            String recordId = e.getIn().getHeader("recordId", String.class);
                            String recordName = e.getIn().getHeader("recordName", String.class);
                            String recordType = e.getIn().getHeader("recordType", String.class);

                            sqlBuilder.append(String.format("'%s', '%s', '%s'", recordId, recordName, recordType));
                            if (i < exchanges.size() - 1) {
                                sqlBuilder.append(", ");
                            }
                        }
                        sqlBuilder.append(")");

                        exchange.getIn().setBody(sqlBuilder.toString());
                    })
                    .to("sql:{{body}}") // Execute the stored procedure
                    .log("Batch processed and inserted successfully.")
                    .end();
            }
        });

        camelContext.start();
        Thread.sleep(10000); // Keep the application running for testing
        camelContext.stop();
    }

    private static DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/your_database");
        dataSource.setUsername("your_username");
        dataSource.setPassword("your_password");
        return dataSource;
    }

    private static String extractRecordId(String body) {
        // Implement logic to extract record ID from the body
        return "12345";
    }

    private static String extractRecordName(String body) {
        // Implement logic to extract record Name from the body
        return "SampleName";
    }

    private static String extractRecordType(String body) {
        // Implement logic to extract record Type from the body
        return "TypeA";
    }
}
