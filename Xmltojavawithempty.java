import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

public class ReconReductedParserToClass {
    public static void main(String[] args) {
        // XML message as a string
        String xmlMessage = """
            <?xml version="1.0" encoding="UTF-8"?>
            <reconReducted>
                <messageId>Test_ID123</messageId>
                <uii>123</uii>
                <usi />
                <uiiVersion>1</uiiVersion>
                <migrationType>None</migrationType>
                <IsdaProduct>xxx</IsdaProduct>
                <DataModel />
                <OriginatingSourceSystem />
                <ProductTaxonomy />
                <AggregationLevel />
                <SourceSystem>UTP</SourceSystem>
                <BookingApprovalState>None</BookingApprovalState>
                <ReportType>nonpublicExecutionReport</ReportType>
                <NomuraOrgIdRdm />
                <GeneratedBy />
                <SupplementTo />
                <Pty1RdmId>xxx</Pty1RdmId>
                <EventType>xxx</EventType>
                <AssetClass>xxxx</AssetClass>
                <PDMReportType />
                <Action>xxx</Action>
            </reconReducted>
            """;

        // Create an instance of ReconMessage
        ReconMessage reconMessage = new ReconMessage();

        try {
            // Use StringReader to parse the XML from a string
            StringReader stringReader = new StringReader(xmlMessage);

            // Set up the StAX parser
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(stringReader);

            String currentElement = "";

            // Process the XML
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    // Store the name of the current element
                    currentElement = event.asStartElement().getName().getLocalPart();
                }

                if (event.isCharacters() && !event.asCharacters().isWhiteSpace()) {
                    // Get the element's value and populate the object
                    String data = event.asCharacters().getData().trim();

                    switch (currentElement) {
                        case "messageId" -> reconMessage.setMessageId(data);
                        case "uii" -> reconMessage.setUii(data);
                        case "usi" -> reconMessage.setUsi(data);
                        case "uiiVersion" -> reconMessage.setUiiVersion(data);
                        case "migrationType" -> reconMessage.setMigrationType(data);
                        case "IsdaProduct" -> reconMessage.setIsdaProduct(data);
                        case "DataModel" -> reconMessage.setDataModel(data);
                        case "OriginatingSourceSystem" -> reconMessage.setOriginatingSourceSystem(data);
                        case "ProductTaxonomy" -> reconMessage.setProductTaxonomy(data);
                        case "AggregationLevel" -> reconMessage.setAggregationLevel(data);
                        case "SourceSystem" -> reconMessage.setSourceSystem(data);
                        case "BookingApprovalState" -> reconMessage.setBookingApprovalState(data);
                        case "ReportType" -> reconMessage.setReportType(data);
                        case "NomuraOrgIdRdm" -> reconMessage.setNomuraOrgIdRdm(data);
                        case "GeneratedBy" -> reconMessage.setGeneratedBy(data);
                        case "SupplementTo" -> reconMessage.setSupplementTo(data);
                        case "Pty1RdmId" -> reconMessage.setPty1RdmId(data);
                        case "EventType" -> reconMessage.setEventType(data);
                        case "AssetClass" -> reconMessage.setAssetClass(data);
                        case "PDMReportType" -> reconMessage.setPdmReportType(data);
                        case "Action" -> reconMessage.setAction(data);
                        // Handle empty elements by defaulting to empty string if necessary
                        default -> {}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Output the populated reconMessage object
        System.out.println("Recon Message Details:");
        System.out.println("Message ID: " + reconMessage.getMessageId());
        System.out.println("UII: " + reconMessage.getUii());
        System.out.println("USI: " + reconMessage.getUsi());
        System.out.println("UII Version: " + reconMessage.getUiiVersion());
        System.out.println("Migration Type: " + reconMessage.getMigrationType());
        System.out.println("ISDA Product: " + reconMessage.getIsdaProduct());
        System.out.println("Data Model: " + reconMessage.getDataModel());
        System.out.println("Originating Source System: " + reconMessage.getOriginatingSourceSystem());
        System.out.println("Product Taxonomy: " + reconMessage.getProductTaxonomy());
        System.out.println("Aggregation Level: " + reconMessage.getAggregationLevel());
        System.out.println("Source System: " + recon
