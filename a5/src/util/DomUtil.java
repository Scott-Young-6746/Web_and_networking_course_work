package util;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import java.io.Writer;
import java.io.File;

public class DomUtil {
    public static Document createEmptyDoc( String top ) throws Exception {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        return impl.createDocument(null, top, null);
    }

    public static Document parseDoc( File f ) throws Exception {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(f);
    }

    public static void outputXML( Node e, Writer w ) throws Exception {
        outputXML(e, w, 4 );
    }

    public static void outputXML( Node e, Writer w, int indent )
        throws Exception
    {
        // An identity transformer.
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute("indent-number", new Integer(indent));
        Transformer xformer = tf.newTransformer();

        DOMSource source = new DOMSource( e );
        StreamResult result = new StreamResult( w );
        xformer.setOutputProperty(OutputKeys.METHOD,"xml");
        xformer.setOutputProperty(OutputKeys.INDENT,"yes");
        xformer.transform(source, result);
    }

    public static void outputHTML( Node e, Writer w ) throws Exception {
        outputHTML(e, w, 4 );
    }

    public static void outputHTML( Node e, Writer w, int indent )
        throws Exception
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute("indent-number", new Integer(4));
        Transformer xformer = tf.newTransformer();

        if ( e instanceof Document ) {
            w.write("<!DOCTYPE html>\n");
        }
        DOMSource source = new DOMSource( e );
        StreamResult result = new StreamResult( w );
        xformer.setOutputProperty(OutputKeys.METHOD,"html");
        xformer.setOutputProperty(OutputKeys.INDENT,"yes");
        xformer.transform(source, result);
    }
}
