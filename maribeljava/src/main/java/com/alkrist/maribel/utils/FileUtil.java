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

	/**
	 * 
	 * @return base path for the game files
	 */
	public static String getBasePath() {
		if (isWindows())
			return System.getenv("APPDATA") + "\\.maribel\\";
		else
			return getUserPath() + "/.maribel/";
	}

	/**
	 * 
	 * @return path to logs
	 */
	public static String getLogPath() {
		if (isWindows())
			return getBasePath() + "logs\\";
		else
			return getBasePath() + "logs/";
	}

	/**
	 * 
	 * @return path to config files
	 */
	public static String getConfigPath() {
		if (isWindows())
			return getBasePath() + "config\\";
		else
			return getBasePath() + "config/";
	}

	public static String getAssetsPath() {
		if (isWindows())
			return "src\\main\\resources\\assets\\";
		else
			return "src/main/resources/assets/";
	}

	public static String getShadersPath() {
		if (isWindows())
			return getAssetsPath() + "shaders\\";
		else
			return getAssetsPath() + "shaders/";
	}
	
	public static String getTexturesPath() {
		if (isWindows())
			return getAssetsPath() + "textures\\";
		else
			return getAssetsPath() + "textures/";
	}
	
	public static String getMeshesPath() {
		if (isWindows())
			return getAssetsPath() + "meshes\\";
		else
			return getAssetsPath() + "meshes/";
	}

	private static String getUserPath() {
		return System.getProperty("user.home");
	}

}
