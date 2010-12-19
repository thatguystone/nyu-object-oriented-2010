class ArrayTest {
	public static void main(String[] args) {
		int y = 4;
		int[][][][] x = new int[5][y][7][2];
		x[0][1][2][1] = 10101010;
		
		System.out.println(x[0][1][2][1]);
		
		int z = x[0][1][2][1];
		System.out.println("waypoint");
		int[][] w = x[1][1];
		System.out.println("waypoint");
		w[0][0] = 1;
		System.out.println("waypoint");
		System.out.println(w[0][0]);
	}
}
