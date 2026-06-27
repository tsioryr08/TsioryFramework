package mg.tsiory.framework.utils;

import mg.tsiory.framework.annotation.Controller;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import mg.tsiory.framework.annotation.Url;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

public class Utilitaires {

  public static List<Class<?>> findAllClassesIn(String packageNames) throws Exception {
    List<Class<?>> classes = new ArrayList<>();
    
    for (String packageName : packageNames.split(",")) {
        packageName = packageName.trim();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                for (File file : directory.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
    }
    return classes;
}

    public static List<Class<?>> findClassesWithAnnotation(String packageName) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        List<Class<?>> allClasses = findAllClassesIn(packageName);

        for (Class<?> clazz : allClasses) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                result.add(clazz);
            }
        }
        return result;
    }

    public static Map<String, Mapping> findUrlMappings(String packageName) throws Exception {
    Map<String, Mapping> urlMappings = new HashMap<>();
    List<Class<?>> controllers = findClassesWithAnnotation(packageName);

    for (Class<?> classe : controllers) {
        for (Method methode : classe.getMethods()) {
            if (methode.isAnnotationPresent(Url.class)) {
                String url = methode.getAnnotation(Url.class).value();
                urlMappings.put(url, new Mapping(classe, methode));
            }
        }
    }
    return urlMappings;
}
}