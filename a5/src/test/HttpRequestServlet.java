package test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Arrays;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

/**
 *
 * HttpRequestServlet shows the http protocol.
 *
 * @author Rod Byrne
 */
//@snipit HttpRequestServlet_def
public class HttpRequestServlet extends HttpServlet {
//@snipit-end HttpRequestServlet_def
    private util.XmlTemplate template;

    //@snipit HttpRequestServlet.doGet
    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        log( request.getRequestURI() );
        // ensures charset is utf-8 for PrintWriter
        response.setContentType("text/html;charset=utf-8");
        util.HTTPUtils.nocache( response );
        PrintWriter out = response.getWriter();

        util.XmlTemplate copy = null;
        synchronized( template ) {
            copy = template.makeCopy();
        }

        makeRequestSessionReport( copy, request, session );
        try {
            copy.generate( out, "html" );
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }
    }
    //@snipit-end HttpRequestServlet.doGet

    //@snipit HttpRequestServlet.doPost
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        log( request.getRequestURI() );
        response.setContentType("text/html;charset=utf-8");
        util.HTTPUtils.nocache( response );
        PrintWriter out = response.getWriter();

        util.XmlTemplate copy = null;
        synchronized( template ) {
            copy = template.makeCopy();
        }

        makeRequestSessionReport( copy, request, session );
        try {
            copy.generate( out, "html" );
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }
    }
    //@snipit-end HttpRequestServlet.doPost

    //@snipit HttpRequestServlet.init
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init( config ); // super.init call is required
        try {
            template = new util.XmlTemplate("war/req.html");
        }
        catch( Exception ex ) {
            log( ex.getMessage() );
            throw new ServletException(ex);
        }
    }
    //@snipit-end HttpRequestServlet.init

    //@snipit HttpRequestServlet.makeRequestSessionReport
    private void makeRequestSessionReport(
        util.XmlTemplate copy,
        HttpServletRequest request,
        HttpSession session )
    {
        Element e = makeRemoteInfo( copy, request);
        copy.replaceChildren( "remote-spot", e );

        e = makeUrlInfo( copy, request);
        copy.replaceChildren( "url-spot", e );

        e = makeHeaderList( copy, request);
        copy.replaceChildren( "header-spot", e );

        e = makeParamList( copy, request);
        copy.replaceChildren( "parameter-spot", e );

        e = makeCookieList( copy, request );
        copy.replaceChildren( "cookies-spot", e );

        e = makeSessionInfo( copy, session );
        copy.replaceChildren( "session-spot", e );
    }
    //@snipit-end HttpRequestServlet.makeRequestSessionReport

    //@snipit HttpRequestServlet.makeSessionInfo
    private Element makeSessionInfo(
        util.XmlTemplate copy, HttpSession session )
    {
        Date createTime = new Date(session.getCreationTime());
        Date lastAccessTime = new Date(session.getLastAccessedTime());
        boolean isNew = session.isNew();

        if ( isNew ) {
            session.setAttribute("count", new Integer(0) );
        }
        else {
            Integer i = (Integer)session.getAttribute("count" );
            i = i + 1;
            session.setAttribute("count", i );
        }
        Element ul = copy.createElement("ul");
        addItem( copy, ul, "create: " + createTime );
        addItem( copy, ul, "last: " + lastAccessTime );
        addItem( copy, ul, "new: " + isNew );
        addItem( copy, ul, "count: " + session.getAttribute("count" ) );
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeSessionInfo

    //@snipit HttpRequestServlet.makeHeaderList
    private Element makeHeaderList(
        util.XmlTemplate copy, HttpServletRequest request )
    {
        Element ul = copy.createElement("ul");
        Enumeration<String> headerNames = request.getHeaderNames();
        while ( headerNames.hasMoreElements() ) {
            String name = headerNames.nextElement().toString();
            addItem( copy, ul, name + ": " + request.getHeader(name) );
        }
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeHeaderList

    //@snipit HttpRequestServlet.makeParamList
    private Element makeParamList(
        util.XmlTemplate copy, HttpServletRequest request )
    {
        Element ul = copy.createElement("ul");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String[] values = request.getParameterValues(name);
            String v = Arrays.toString( values );
            addItem( copy, ul, name + ": " + v );
        }
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeParamList

    //@snipit HttpRequestServlet.makeCookieList
    private Element makeCookieList(
        util.XmlTemplate copy, HttpServletRequest request )
    {
        Element ul = copy.createElement("ul");
        Cookie[] cookies = request.getCookies();
        for( Cookie c : cookies ) {
            addItem( copy, ul, c.getName() + ": " + c.getValue() );
        }
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeCookieList

    //@snipit HttpRequestServlet.makeUrlInfo
    private Element makeUrlInfo(
        util.XmlTemplate copy, HttpServletRequest request )
    {
        Element ul = copy.createElement("ul");
        addItem( copy, ul, "url: " + request.getRequestURL() );
        addItem( copy, ul, "uri: " + request.getRequestURI() );
        addItem( copy, ul, "method: " + request.getMethod() );
        addItem( copy, ul, "servlet-path: " + request.getServletPath() );
        addItem( copy, ul, "path-info: " + request.getPathInfo() );
        String q = request.getQueryString();
        if ( q != null ) {
            addItem( copy, ul, "query: " + q );
        }
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeUrlInfo

    //@snipit HttpRequestServlet.makeRemoteInfo
    private Element makeRemoteInfo(
        util.XmlTemplate copy, HttpServletRequest request )
    {
        Element ul = copy.createElement("ul");
        addItem( copy, ul, "protocol: " + request.getProtocol() );
        addItem( copy, ul, "host: " + request.getRemoteHost() );
        addItem( copy, ul, "port: " + request.getRemotePort() );
        addItem( copy, ul, "char encoding: " + request.getCharacterEncoding() );
        addItem( copy, ul, "welcome: " + "\u6B22\u8FCE" );
        return ul;
    }
    //@snipit-end HttpRequestServlet.makeRemoteInfo

    //@snipit HttpRequestServlet.addItem
    private void addItem( util.XmlTemplate tmpl, Element ul, String s ) {
        Element li = tmpl.createElement("li");
        li.appendChild( tmpl.createTextNode( s ));
        ul.appendChild( li );
    }
    //@snipit-end HttpRequestServlet.addItem
}
