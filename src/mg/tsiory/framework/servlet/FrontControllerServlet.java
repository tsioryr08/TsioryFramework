package mg.tsiory.framework.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import mg.tsiory.framework.utils.Mapping;
import mg.tsiory.framework.utils.Utilitaires;
import java.io.*;
import java.util.List;
import java.util.Map;

@WebServlet("/*")
public class FrontControllerServlet extends HttpServlet {

    private List<Class<?>> controllers;
    private Map<String, Mapping> urlMappings;

    @Override
    public void init() throws ServletException {
        String packageName = getInitParameter("packageName");
        try {
            controllers = Utilitaires.findClassesWithAnnotation(packageName);
            urlMappings = Utilitaires.findUrlMappings(packageName);
        } catch (Exception e) {
            throw new ServletException("Erreur lors du scan des packages: ", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String url = req.getRequestURI();
        out.println("<h1>Ito ilay URL mandalo: " + url + "</h1>");
        out.println("<h2>Liste des Controllers trouves :</h2>");
        out.println("<ul>");
        for (Class<?> c : controllers) {
            out.println("<li>" + c.getName() + "</li>");
        }
        out.println("</ul>");
        out.println("<h2>URL mappings trouves :</h2>");
        out.println("<ul>");
        for (Map.Entry<String, Mapping> entry : urlMappings.entrySet()) {
            out.println("<li>" + entry.getKey() + " → classe nahitana azy: "
                + entry.getValue().getClasse().getSimpleName()
                + " -- methode appelee: " + entry.getValue().getMethode().getName()
                + "</li>");
        }
        out.println("</ul>");
    }
}