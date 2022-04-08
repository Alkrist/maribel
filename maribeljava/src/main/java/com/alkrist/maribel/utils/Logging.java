package com.alkrist.maribel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * Logger util, used to handle logging. Logger is from built-in java logger.
 * 
 * Logger creates a sub folder in a specified folder and stores current log as "latest.log",
 * previous log is zipped and saved with a name of last modify time.
 * 
 * @author Mikhail
 *
 */
public class Logging {

	private static Logger logger;
	private static FileHandler fh;
	private static MaribelFormatter formatter;
	
	/**
	 * Init logger, create  log file or renew it, zip previous log file.
	 */
	public static void initLogger() {
		logger = Logger.getLogger("Maribel");

			try {
				
				File file = new File(FileUtil.getLogPath()+"latest.log");
				
				if(file.exists() && !file.isDirectory()) {
					BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
					LocalDateTime ldt = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
					String zipName = FileUtil.getLogPath();
					zipName += ldt.getYear() + "-";
					zipName += ldt.getMonthValue() + "-";
					zipName += ldt.getDayOfMonth() + "-";
					zipName += ldt.getHour() + "-";
					zipName += ldt.getMinute() + "-";
					zipName += ldt.getSecond() + ".gz";
					zipLatestToArchive(file.getPath(), zipName);
					file.delete();
				}
					
				
				file.getParentFile().mkdirs();
				file.createNewFile();
				
				formatter = new MaribelFormatter();
				fh = new FileHandler(FileUtil.getLogPath()+"latest.log");
				logger.addHandler(fh);
				fh.setFormatter(formatter);
				logger.info("log started today");

			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
			
	}
	
	//TODO: make it sync!!!
	public static Logger getLogger() {
		return logger;
	}
	
	private static boolean zipLatestToArchive(String srcPath, String zipPath) {
		
		try {
			
			FileInputStream fis = new FileInputStream(srcPath);
			FileOutputStream fos = new FileOutputStream(zipPath);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			
			byte[] buffer = new byte[1024];
			int length;

			while((length=fis.read(buffer)) != -1){
                gzos.write(buffer, 0, length);
            }

			gzos.close();
			fos.close();
			fis.close();

			return true;
			
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static class MaribelFormatter extends Formatter{

		private final DateFormat df = new SimpleDateFormat("hh:mm:ss.SSS");
		
		@Override
		public String format(LogRecord record) {
			
			StringBuilder builder = new StringBuilder(1000);
		
			builder.append('[').append(df.format(new Date(record.getMillis()))).append("]");
			builder.append('[').append(record.getLevel().toString().toUpperCase()).append("] - ");
			builder.append(formatMessage(record));
	        builder.append("\n");
			
			return builder.toString();
		}		
	}
	
}
