import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ReconReductedParser {
    public static void main(String[] args) {
        // Map to store parsed elements
        Map<String, String> reconData = new HashMap<>();
        
        try {
            // 1. Use BufferedReader for efficient reading
            BufferedReader reader = new BufferedReader(new FileReader("reconReducted.xml"));
            
            // 2. Set up the StAX parser
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(reader);

            String currentElement = "";
            
            // 3. Process the XML
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    // Store the name of the current element
                    currentElement = event.asStartElement().getName().getLocalPart();
                }

                if (event.isCharacters() && !event.asCharacters().isWhiteSpace()) {
                    // Only store non-empty elements
                    String data = event.asCharacters().getData().trim();
                    reconData.put(currentElement, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Output the parsed data
        reconData.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
