import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

public class PR142Main {
       static Scanner in = new Scanner(System.in); // System.in és global, Scanner també ho a de ser
    public static void main(String[] args) {
     
        try {
            File file = new File("data/cursos.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            boolean running = true;

            while (running) {
                String menu = "Escull una opció:";
                menu = menu + "\n 1) listar tutores por cursos";
                menu = menu + "\n 2) modulos por cursos";
                menu = menu + "\n 3) listar alumnos por cursos";
                menu = menu + "\n 4) agregar alumno";
                menu = menu + "\n 5) eliminar alumno";
                menu = menu + "\n 0) Sortir";
                System.out.println(menu);

                int opcio = Integer.valueOf(llegirLinia("Opció:"));
                
                try {
                    switch (opcio) {
                        case 1: listarCursosTutoresAlumnos(doc, xpath);              break;
                        case 2: mostrarModulosPorCurso(doc, xpath, "AMS2");               break;
                        case 3: listarAlumnosPorCurso(doc, xpath, "AWS1");                break;
                        case 4: agregarAlumnoACurso(doc, xpath, "AWS1", "NUEVO_ALUMNO, Juan");               break;
                        case 5: eliminarAlumnoDeCurso(doc, xpath, "AWS1", "GONZALEZ, Victor M");              break;
                        case 0: running = false;                       break;
                        default: break;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static public String llegirLinia (String text) {
        System.out.print(text);
        return in.nextLine();
    }

    // Función 1: Listar IDs de cursos, tutores y total de alumnos.
    private static void listarCursosTutoresAlumnos(Document doc, XPath xpath) throws XPathExpressionException {
        System.out.println("Función 1: Listar IDs de cursos, tutores y total de alumnos");
        XPathExpression cursosExpr = xpath.compile("/cursos/curs");
        NodeList cursos = (NodeList) cursosExpr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < cursos.getLength(); i++) {
            Element curso = (Element) cursos.item(i);
            String idCurso = curso.getAttribute("id");
            String tutor = curso.getElementsByTagName("tutor").item(0).getTextContent();
            NodeList alumnos = curso.getElementsByTagName("alumne");
            int totalAlumnos = alumnos.getLength();
            System.out.println("ID del Curso: " + idCurso);
            System.out.println("Tutor: " + tutor);
            System.out.println("Total de Alumnos: " + totalAlumnos);
            System.out.println();
        }
    }

    // Función 2: Mostrar IDs y títulos de los módulos a partir de un ID de curso.
    private static void mostrarModulosPorCurso(Document doc, XPath xpath, String cursoId) throws XPathExpressionException {
        System.out.println("Función 2: Mostrar IDs y títulos de los módulos para el Curso con ID: " + cursoId);
        String modulosPath = "/cursos/curs[@id='" + cursoId + "']/moduls/modul";
        XPathExpression modulosExpr = xpath.compile(modulosPath);
        NodeList modulos = (NodeList) modulosExpr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < modulos.getLength(); i++) {
            Element modulo = (Element) modulos.item(i);
            String idModulo = modulo.getAttribute("id");
            String tituloModulo = modulo.getElementsByTagName("titol").item(0).getTextContent();
            System.out.println("ID del Módulo: " + idModulo);
            System.out.println("Título del Módulo: " + tituloModulo);
            System.out.println();
        }
    }

    // Función 3: Listar alumnos de un curso.
    private static void listarAlumnosPorCurso(Document doc, XPath xpath, String cursoId) throws XPathExpressionException {
        System.out.println("Función 3: Listar alumnos para el Curso con ID: " + cursoId);
        String alumnosPath = "/cursos/curs[@id='" + cursoId + "']/alumnes/alumne";
        XPathExpression alumnosExpr = xpath.compile(alumnosPath);
        NodeList alumnos = (NodeList) alumnosExpr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < alumnos.getLength(); i++) {
            Element alumno = (Element) alumnos.item(i);
            String nombreAlumno = alumno.getTextContent();
            System.out.println("Alumno: " + nombreAlumno);
        }
        System.out.println();
    }

    // Función 4: Agregar un alumno a un curso.
    private static void agregarAlumnoACurso(Document doc, XPath xpath, String cursoId, String nuevoAlumno) throws XPathExpressionException {
        System.out.println("Función 4: Agregar un nuevo alumno al Curso con ID: " + cursoId);
        String alumnosPath = "/cursos/curs[@id='" + cursoId + "']/alumnes";
        Element alumnosElement = (Element) xpath.compile(alumnosPath).evaluate(doc, XPathConstants.NODE);
        Element nuevoAlumnoElement = doc.createElement("alumne");
        nuevoAlumnoElement.setTextContent(nuevoAlumno);
        alumnosElement.appendChild(nuevoAlumnoElement);
        guardarCambios(doc, "data/cursos.xml");
        System.out.println("Nuevo alumno agregado con éxito: " + nuevoAlumno);
    }

    // Función 5: Eliminar un alumno de un curso.
    private static void eliminarAlumnoDeCurso(Document doc, XPath xpath, String cursoId, String alumnoEliminar) throws XPathExpressionException {
        System.out.println("Función 5: Eliminar un alumno del Curso con ID: " + cursoId);
        String alumnoPath = "/cursos/curs[@id='" + cursoId + "']/alumnes/alumne[text()='" + alumnoEliminar + "']";
        Element alumnoElement = (Element) xpath.compile(alumnoPath).evaluate(doc, XPathConstants.NODE);
        if (alumnoElement != null) {
            alumnoElement.getParentNode().removeChild(alumnoElement);
            guardarCambios(doc, "1");
            System.out.println("Alumno eliminado con éxito: " + alumnoEliminar);
        } else {
            System.out.println("No se encontró al alumno: " + alumnoEliminar);
        }
    }

    // Método para guardar cambios en el archivo XML.
    private static void guardarCambios(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}