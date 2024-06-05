package com.alkrist.maribel.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

	private static final Path assetsPath = Paths.get("src/main/resources/assets/"); //TODO: make it variable maybe
	
	/**
	 *
	 * @return if the current OS is Windows
	 */
	public static boolean isWindows() {
		if ((System.getProperty("os.name").toUpperCase().contains("WIN")))
			return true;
		return false;
	}

	/**
	 * 
	 * @return if the current OS is Mac
	 */
	public static boolean isMac() {
		if ((System.getProperty("os.name").toUpperCase().contains("MAC")))
			return true;
		return false;
	}

	/**
	 * 
	 * @return if the current OS is Linux-based
	 */
	public static boolean isLinux() {
		if ((System.getProperty("os.name").toUpperCase().contains("NUX")))
			return true;
		return false;
	}
	
	
	// config paths 
	public static String basePath() {
		if (isWindows())
			return System.getenv("APPDATA") + "\\.maribel\\";
		else
			return System.getProperty("user.home") + "/maribel/";
	}

	public static String getConfigLocation(String subdirConfigName) {
		return Paths.get(basePath()).resolve("config/"+subdirConfigName).toString();
	}
	
	public static String getConfigLocation() {
		return Paths.get(basePath()).resolve("config").toString();
	}
	
	public static String getLogLocation(String subdirLogName) {
		return Paths.get(basePath()).resolve("logs/"+subdirLogName).toString();
	}
	
	public static String getLogLocation() {
		return Paths.get(basePath()).resolve("logs").toString();
	}
	
	public static String getResourceLocation(String path) {
		return assetsPath.resolve(path).toString();
	}
}
