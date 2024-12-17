import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class MyAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        List<Exchange> batch;
        if (oldExchange == null) {
            batch = new ArrayList<>();
            batch.add(newExchange);
            newExchange.getIn().setBody(batch);
            return newExchange;
        } else {
            batch = oldExchange.getIn().getBody(List.class);
            batch.add(newExchange);
            return oldExchange;
        }
    }
}
