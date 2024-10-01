import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.HashMap;
import java.util.Map;

public class EhcacheProgrammaticExample {

    public static void main(String[] args) {
        // Step 1: Create a CacheManager programmatically
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();

        // Step 2: Create a cache to store Map values
        Cache<String, Map<String, String>> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class,                        // Key type
                        Map.class,                           // Value type (Map)
                        ResourcePoolsBuilder.heap(100))      // Set the heap size (in this case, 100 entries)
        );

        // Step 3: Store a Map in the cache
        Map<String, String> sampleMap = new HashMap<>();
        sampleMap.put("key1", "value1");
        sampleMap.put("key2", "value2");

        myCache.put("myMapKey", sampleMap);

        // Step 4: Retrieve the Map from the cache
        Map<String, String> retrievedMap = myCache.get("myMapKey");

        // Step 5: Print the retrieved Map values
        if (retrievedMap != null) {
            retrievedMap.forEach((key, value) -> System.out.println(key + ": " + value));
        }

        // Close the CacheManager when done
        cacheManager.close();
    }
}
