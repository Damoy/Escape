package toolbox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Logs {

	private static int logCount = 0;
	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date;
	private static StringBuilder sbBuilder = new StringBuilder();
	
	public static void println(String s){
//		getLogs();
//		sbBuilder.append(s);
//		sbBuilder.append("]");
//		System.out.println(sbBuilder.toString());
//		logCount++;
	}
	
	public static void println(float f){
		println(f + "");
	}
	
	public static void println(int nb){
		println(nb + "");
	}
	
	public static void println(Object[] array){
		for(int i = 0; i < array.length; i++){
			println(array[i].toString());
		}
	}
	
	
	public static void println(List<?> list){
		for(Object o : list){
			println(o.toString());
		}
	}
	
	
	private static void getLogs(){
		clearBuilder();
		sbBuilder.append("[Logs:");
		sbBuilder.append(logCount);
		sbBuilder.append(" | ");
		sbBuilder.append(getDate());
		sbBuilder.append(" | ");
	}
	
	private static String getDate(){
		date = new Date();
		return dateFormat.format(date);
	}
	
	private static void clearBuilder(){
		sbBuilder.setLength(0);
	}
}
