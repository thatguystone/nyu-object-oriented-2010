import java.util.ArrayList;

public class arrayList {
	public static void main(String[] args) {
		ArrayList l = new ArrayList();
		l.add(new Object());
		l.add(new Object());
		l.add(new Object());
		System.out.println(l.size());
		
		System.out.println(l.get(1).getClass().getName());
	}
}
