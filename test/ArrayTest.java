class ArrayTest {
	public static void main(String[] args) {
		int y = 4;
		int x = 8;
		int [] singleton=new int[1];
		int [][] matrix=new int[3][3];
		matrix[0][2]=12;
		int [][][][] c=new int[y][23][x][27];

//i don't know what i am doing...

		c[y-1][22][x-1][1]=0;
		c[0][x][1][1]=0;
		c[1][y][3][1]=0;
		int z[][][][][] = new int[10][1][2][1][1];
		z[9][0][1][0][0]=1;
//not sure why this line causes index out of bound...
//		z[  c[0][x][1][1]  ][0][0][0][0]=1;
	}

}
