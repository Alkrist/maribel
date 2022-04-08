package com.alkrist.maribel.utils;

/**
 * A utility class that helps to find directories of the engine.
 * 
 * @author Mikhail
 *
 */
public class FileUtil {

	/**
	 *
	 * @return if the current OS is Windows
	 */
	public static boolean isWindows() {
		if((System.getProperty("os.name").toUpperCase().contains("WIN"))) {
			return true;
		}
			
		return false;
	}
	
	/**
	 * 
	 * @return if the current OS is Mac
	 */
	public static boolean isMac() {
		if((System.getProperty("os.name").toUpperCase().contains("MAC")))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return if the current OS is Linux-based
	 */
	public static boolean isLinux() {
		if((System.getProperty("os.name").toUpperCase().contains("NUX")))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return base path for the game files
	 */
	public static String getBasePath() {
		
		if(isWindows())
			return System.getenv("APPDATA") + "\\.maribel\\";		
		else
			return getUserPath() + "/.maribel/";
	}
	
	private static String getUserPath() {
		return System.getProperty("user.home");
	}
	
}
