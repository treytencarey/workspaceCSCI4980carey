package pkg;

public class ClassA {
	void foo(String str1) {
		str1 = pkg.UtilStr.trim(str1);
		System.out.println(str1);
	}
	
	void bar(String str1, String str2) {
		str1 = pkg.UtilStr.trim(str1);
		System.out.println(str1 + str2);
	}
	
	void baz(String data1) {
		data1 = pkg.UtilStr.trim(data1);
		System.out.println("hello" + data1);
	}
}
