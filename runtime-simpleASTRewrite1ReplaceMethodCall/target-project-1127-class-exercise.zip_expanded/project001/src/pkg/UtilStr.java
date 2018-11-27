package pkg;

public class UtilStr {
	public static String trim(String str) {
		str = str.replace("\\s+", "");
		return str;
	}
}
