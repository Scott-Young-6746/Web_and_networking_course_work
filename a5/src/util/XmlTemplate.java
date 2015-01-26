package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Rod Byrne
 */

public class XmlTemplate {

    private Document doc;
    private HashMap<String, Element> ids;

    private void removeAll(Node node) {
        NodeList list = node.getChildNodes();
        ArrayList<Node> nodes = new ArrayList<Node>(list.getLength());
        for( int i = 0 ; i < list.getLength(); i++ ) {
            Node n = list.item( i );
            nodes.add( n );
        }
        for( Node n : nodes ) {
            node.removeChild(n);
        }
    }

    private void collectIds( Element e ) {
        String id = e.getAttribute("id");
        if ( id != null ) {
            // XXX should check if already present
            ids.put( id, e );
        }
        NodeList list = e.getChildNodes();
        for( int i = 0; i < list.getLength(); i++ ) {
            Node n = list.item( i );
            if ( n.getNodeType() == Node.ELEMENT_NODE ) {
                collectIds( (Element)n );
            }
        }
    }

    public XmlTemplate( String filename ) throws Exception {
        doc = DomUtil.parseDoc( new File( filename ) );
        ids = new HashMap<String, Element>();
        collectIds( doc.getDocumentElement() );
    }

    public Element createElement( String tag ) throws DOMException {
        return doc.createElement( tag );
    }

    public Text createTextNode( String text ) throws DOMException {
        return doc.createTextNode( text );
    }

    public void replaceTextContent( String id, String text ) {
        if ( !ids.containsKey( id ) ) return; // XXX throw exception
        Element e = ids.get( id );
        e.setTextContent( text );
    }

    public void replaceChildren( String id, Element ne ) {
        if ( !ids.containsKey( id ) ) return; // XXX throw exception
        Element e = ids.get( id );
        removeAll( e ); // remove all children
        e.appendChild( ne );
    }

    public void setAttribute( String id, String name, String value ) {
        if ( !ids.containsKey( id ) ) return; // XXX throw exception
        Element e = ids.get( id );
        e.setAttribute( name, value );
    }

    public void generate( Writer w, String method ) throws Exception {
        if ( method.equals("html") ) {
            DomUtil.outputHTML( doc, w);
        }
        else if ( method.equals("xml") ) {
            DomUtil.outputXML( doc, w);
        }
    }

    private XmlTemplate( Document doc ) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        try { 
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException ex ) {
            throw new RuntimeException( ex.getMessage() );
        }
        Node originalRoot = doc.getDocumentElement();

        Document copiedDocument = db.newDocument();
        Node copiedRoot = copiedDocument.importNode(originalRoot, true);
        copiedDocument.appendChild(copiedRoot);

        this.doc = copiedDocument;
        this.ids = new HashMap<String, Element>();
        collectIds( this.doc.getDocumentElement() );
    }

    public XmlTemplate makeCopy() {
        return new XmlTemplate( this.doc );
    }
}
