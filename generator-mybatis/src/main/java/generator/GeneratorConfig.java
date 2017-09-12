package generator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class GeneratorConfig {

	public static String rootPath;

	public static String xmlPath;

	public static String mapperPath;

	public static String servicePath;

	public static String controllerPath;

	public static String modelPath;

	public static String beanPath;
	
	public static String atomicityPath;
	
	public static String apiVersionPath;
	
	static {
		try {

			URL url = Thread.currentThread().getContextClassLoader().getResource("generator.properties");

			InputStream in = url.openStream();

			Properties p = new Properties();

			p.load(in);

			rootPath = p.getProperty("gnt.root");
			if (rootPath == null || "".equals(rootPath)) {
				URL rootURL = Thread.currentThread().getContextClassLoader().getResource("");
				rootPath = rootURL.getPath();
				rootPath = rootPath.substring(1, rootPath.indexOf("target"));
				
				System.out.println(rootPath);
			}

			modelPath = p.getProperty("gnt.model");
			
			beanPath = p.getProperty("gnt.bean");
			
			atomicityPath = p.getProperty("gnt.atomicity");

			xmlPath = p.getProperty("gnt.xml");

			mapperPath = p.getProperty("gnt.mapper");

			servicePath = p.getProperty("gnt.service");

			controllerPath = p.getProperty("gnt.controller");
			
			apiVersionPath = p.getProperty("gnt.apiVersionPath");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String []args) {
		//System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
		
		System.out.println("com/bmind/dd/".replaceAll("/|\\\\", "."));
	}
}
