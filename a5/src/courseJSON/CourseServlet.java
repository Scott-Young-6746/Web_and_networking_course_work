package courseJSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Arrays;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

import static java.lang.String.valueOf;

/**
 *
 * Prints course info.
 *
 * @author Rod Byrne, Scott Young
 */
public class CourseServlet extends HttpServlet {
    private util.XmlTemplate template;
    private String courseDataPath;
    private Gson gson = new Gson();
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document doc;
	private Element root;
	
	private void storePatientData( med.Course p ) {
        try {
            synchronized ( courseDataPath ) {
                FileOutputStream fs = new FileOutputStream( courseDataPath );
                ObjectOutputStream oos = new ObjectOutputStream( fs );
                oos.writeObject( p );
                oos.close();
                // XXX closing oos on Exception
            }
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
        }
    }

    private med.Course retrievePatientData() {
        try {
            synchronized ( courseDataPath ) {
                File f = new File( courseDataPath );
                if ( ! f.exists() ) {
                    return null;
                }
                FileInputStream fs = new FileInputStream( f );
                ObjectInputStream ios = new ObjectInputStream( fs );
                med.Course p = (med.Course)ios.readObject();
                ios.close();
                return p;
                // XXX closing ios on Exception
            }
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
        }
        return null;
    }
	
	@Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        log( request.getRequestURI() );
        // ensures char set is utf-8 for print writer
        response.setContentType("text/html;charset=utf-8");
        util.HTTPUtils.nocache( response );
        PrintWriter out = response.getWriter();

        util.XmlTemplate copy = null;
        synchronized( template ) {
            copy = template.makeCopy();
        }
        med.Course p = retrievePatientData();
        if ( p != null ) {
            updateForm( copy, p );
        }

        try {
            copy.generate( out, "html" );
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        log( request.getRequestURI() );
        response.setContentType("application/json; charset=utf-8");
        util.HTTPUtils.nocache( response );
        PrintWriter out = response.getWriter();
        BufferedReader rd = request.getReader();

        // XXX handle JsonParseException
        med.Course p = gson.fromJson(rd, med.Course.class);
        log("course = " + p );
        util.XmlTemplate copy = null;
        synchronized( template ) {
            copy = template.makeCopy();
        }
		String textToGoOnWeb = "";
		NodeList list = root.getElementsByTagName("course");
		for(int i=0; i<list.getLength(); i++){

            Element element = (Element)list.item(i);
			String liText = "";
            if(element.getAttribute("subject").equals(p.getName()) && element.getAttribute("cnum").equals(p.getNumber())){
                
                NodeList sections = element.getElementsByTagName("section");
                for(int j=0; j<sections.getLength(); j++){
                    Element e = (Element)sections.item(j);
					liText = liText.concat("<ul>Section: ");
					liText = liText.concat(e.getAttribute("seq"));
                    NodeList meetings = e.getElementsByTagName("meeting");
                    for(int k=0; k<meetings.getLength(); k++){
                        liText = liText.concat("<li>Meeting: ");
						Element m = (Element)meetings.item(k);
						liText = liText.concat(" slot: " + m.getAttribute("slot") + " days: " + m.getAttribute("days") + " start: " + m.getAttribute("start") + " end: " + m.getAttribute("end") + " bc: " + m.getAttribute("bc") + " room: " + m.getAttribute("room"));
						liText = liText.concat("</li>");
                    }
					liText = liText.concat("</ul>");
                }
            }
			textToGoOnWeb = textToGoOnWeb.concat(liText);
        }
		
		try {
			if(textToGoOnWeb.length() > 0){
				out.print(textToGoOnWeb);
				out.close();
			}
			else{
				out.print("No class found");
				out.close();
			}
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }

    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init( config ); // super.init call is required
        try {
            courseDataPath = getInitParameter("course-data");
			if ( courseDataPath == null ) {
                courseDataPath = "war/course.data";
            }
            String formPath = getInitParameter("course-form");
            if ( formPath == null ) {
                formPath = "war/course-form-json.html";
            }
            template = new util.XmlTemplate( formPath );
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse("./winter2014.xml");
			root = doc.getDocumentElement();
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }
    }
	private void updateForm(util.XmlTemplate copy, med.Course p ) {
        copy.setAttribute("course-name", "value", p.getName() );
        copy.setAttribute("course-number", "value", p.getNumber() );
		
    }
}
