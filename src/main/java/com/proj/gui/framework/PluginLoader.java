package com.proj.gui.framework;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

  
/** 
 * 根据properties中配置的路径把jar和配置文件加载到classpath中。 
 * @author jnbzwm 
 * 
 */  
public final class PluginLoader {  
    /** URLClassLoader的addURL方法 */  
    private static Method addURL = initAddMethod();  
  
    private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();  
  
    /** 
     * 初始化addUrl 方法. 
     * @return 可访问addUrl方法的Method对象 
     */  
    private static Method initAddMethod() {  
        try {  
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });  
            add.setAccessible(true);  
            return add;  
        }  catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * 加载jar classpath。 
     * @throws Exception 
     */  
    public static void loadClasspath() {  
        for (String f : getJarFiles()) {  
            loadClasspath(f);  
        }  
    }  
  
    public static void loadClasspath(String filepath){  
        File file = new File(filepath);  
        loopFiles(file);  
    }  
  
    /**     
     * 循环遍历目录，找出所有的jar包。 
     * @param file 当前遍历文件 
     * @throws Exception 
     */  
    private static void loopFiles(File file){  
        if (file.isDirectory()) {  
            File[] tmps = file.listFiles();  
            for (File tmp : tmps) {  
                loopFiles(tmp);  
            }  
        }  
        else {  
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {  
                addURL(file);  
            }  
        }  
    }  
  
    /** 
     * 通过filepath加载文件到classpath。 
     * @param filePath 文件路径 
     * @return URL 
     * @throws Exception 异常 
     */  
    private static void addURL(File file){  
        try {
						addURL.invoke(classloader, new Object[] { file.toURI().toURL() });
					} catch (Exception e) {
					}  
    }  
  
    /**
     * 从配置文件中得到配置的需要加载到classpath里的路径集合。 
     * @return 
     * @throws IOException 
     */  
    private static List<String>	 getJarFiles() {  
    	// TODO 从properties文件中读取配置信息  如果不想配置 可以自己new 一个List<String> 然后把 jar的路径加进去 然后返回
    			List<String> list = new ArrayList<>();
    			try {
    				Path path = Paths.get("plugins");
    				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
    					@Override
    					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    						String fileString = file.toAbsolutePath().toString();
    						list.add(fileString);
    						return FileVisitResult.CONTINUE;
    					}
    				});
					} catch (Exception e) {
					}
    			return list;  
    }  
}  