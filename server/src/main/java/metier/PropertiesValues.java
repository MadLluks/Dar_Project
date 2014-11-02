package main.java.metier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesValues {
	public final static String KEYS_PROPERTIES = "keys.properties";	
	private static Properties propertiesResource = null;

	private PropertiesValues(){}

	public static String getRessource(String keyname) {
		return getPropertiesResource().getProperty(keyname);
	}

	private static Properties getPropertiesResource() {
		if (propertiesResource == null) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream(KEYS_PROPERTIES);

			if (is != null) {
				propertiesResource = new Properties();
				try {
					propertiesResource.load(is);
				} catch (IOException e) { 
				}
			}

		}
		return propertiesResource;
	}	
}
