class caseNewArray {
	public static void main(String[] args) {
		int i[] = new int[3], m = 2;
		int[] t = new int[3];
		
		i[1] = 100;
		
		Object[] o = new Object[1];
		String[][] o2 = new String[1][2];
		
		i.getClass();
		o.getClass();
		o2.getClass().getSuperclass();
		
		System.out.println("It didn't segfault! That means it worked!");
	}
}
