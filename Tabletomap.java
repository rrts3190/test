@Service
public class YourService {

    @Autowired
    private YourEntityRepository repository;

    public Map<String, Map<String, Map<String, List<String>>>> getConsumerPatternProperties() {
        List<Object[]> results = repository.findConsumerPatternProperties();

        Map<String, Map<String, Map<String, List<String>>>> consumerMap = new HashMap<>();

        for (Object[] row : results) {
            String consumername = (String) row[0];
            String patternid = (String) row[1];
            String propertyname = (String) row[2];
            String propertyvalue = (String) row[3];

            consumerMap
                .computeIfAbsent(consumername, k -> new HashMap<>())
                .computeIfAbsent(patternid, k -> new HashMap<>())
                .computeIfAbsent(propertyname, k -> new ArrayList<>())
                .add(propertyvalue);
        }

        return consumerMap;
    }
}
