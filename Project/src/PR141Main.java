import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PR141Main {
    public static void main(String[] args) {
        try {
            // Crear el documento XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Elemento raíz
            Element biblioteca = doc.createElement("biblioteca");
            doc.appendChild(biblioteca);

            // Elemento libro
            Element libro = doc.createElement("llibre");
            libro.setAttribute("id", "001");
            biblioteca.appendChild(libro);

            // Elementos del libro
            crearElemento(doc, libro, "titol", "El viatge dels venturons");
            crearElemento(doc, libro, "autor", "Joan Pla");
            crearElemento(doc, libro, "anyPublicacio", "1998");
            crearElemento(doc, libro, "editorial", "Edicions Mar");
            crearElemento(doc, libro, "genere", "Aventura");
            crearElemento(doc, libro, "pagines", "320");
            crearElemento(doc, libro, "disponible", "true");

            // Guardar el documento XML en un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("data/biblioteca.xml"));
            transformer.transform(source, result);

            System.out.println("El archivo biblioteca.xml se ha creado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para crear elementos en el documento XML
    private static void crearElemento(Document doc, Element parent, String nombreElemento, String valor) {
        Element elemento = doc.createElement(nombreElemento);
        elemento.appendChild(doc.createTextNode(valor));
        parent.appendChild(elemento);
    }
}
