package mg.tsiory.framework.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/*")
public class FrontControllerServlet extends HttpServlet {

    private List<Class<?>> controllers= new ArrayList<>();
    @Override
    public void init() throws ServletException {
        String packageName="mg.tsiory.test.controllers";
        try {
            scanPackage(packageName);

        } catch (Exception e) {
            throw new ServletException("Erreur lors du scan des package: ", e);
        }
    }

    private void scanPackage(String packageName) throws Exception {
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());

            if(directory.exists()) {
                for (File file : directory.listFiles()){
                    if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(mg.tsiory.framework.annotation.Controller.class)) {
                            controllers.add(clazz);
                        }
                    }
                }
            }
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
        out.println("<h1>Ireto avy ireo URL mandalo: " + url + "</h1>");
          out.println("<h2>Liste des Controllers trouves :</h2>");
        out.println("<ul>");
        for (Class<?> c : controllers) {
            out.println("<li>" + c.getName() + "</li>");
        }
        out.println("</ul>");
    }
}