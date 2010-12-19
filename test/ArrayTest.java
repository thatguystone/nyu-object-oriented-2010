class ArrayTest {
	public static void main(String[] args) {
		int y = 4;
		int[][][][] x = new int[5][y][7][2];
		x[0][1][2][3]= 1;
		int z = x[0][1][2][3];
		int[][] w = x[4][4];
	}

}
