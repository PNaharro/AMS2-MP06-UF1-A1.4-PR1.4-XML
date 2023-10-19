import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;


public class PR140Main {
    public static void main(String[] args) {
        try {
            File file = new File("data/persones.xml");
            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/persones/persona");

            // Get a NodeList of persona elements
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            // Print column headers
            System.out.format("%-15s %-15s %-5s %-15s%n", "Nombre", "Apellido", "Edad", "Ciudad");
            System.out.println("-------------------------------------------------------");

            // Iterate through the persona elements and print their data
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element persona = (Element) nodeList.item(i);
                String nombre = persona.getElementsByTagName("nom").item(0).getTextContent();
                String apellido = persona.getElementsByTagName("cognom").item(0).getTextContent();
                String edad = persona.getElementsByTagName("edat").item(0).getTextContent();
                String ciudad = persona.getElementsByTagName("ciutat").item(0).getTextContent();

                System.out.format("%-15s %-15s %-5s %-15s%n", nombre, apellido, edad, ciudad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

