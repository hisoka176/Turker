package main.source.common;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
/*
 * 工具类
 */
public class MyUtil {
	
	//HOSVD  中K觉得core张量大小的，k越大core张量越小
	public static double[][] removeNoise(double[][] source,int k){
		
		int sx = source.length;
		int sy = source[0].length;
		
		double[][] dest = new double[sx][sy - k];
		
		int dx = dest.length;
		int dy = dest[0].length;
		
		for(int i = 0;i<dx;i++){
			for(int j = 0;j<dy;j++){
				dest[i][j] = source[i][j];
			}
		}
		
		return dest;	
	}
	// 张量X_ijk  和 矩阵 M_mi 做运算得到张量X_mjk
	public static double[][][] caculateXdemision(double[][][] a,double[][] b){
		int xa = a.length;
		int ya = a[0].length;
		int za = a[0][0].length;
		
		int xb = b.length;
		int yb = b.length;
		
		double[][][] result = new double[xb][ya][za];
		for(int i = 0;i<xb;i++){
			for(int j = 0;j<ya;j++){
				for(int k = 0;k < za;k++){
					double total = 0.0;
					for(int l = 0;l<xa;l++){
						total += a[l][j][k]*b[i][l];
					}
					result[i][j][k] = total;
				}
			}
		}
		
		return result;
	}
	// 张量X_ijk  和 矩阵 M_mj 做运算得到张量X_imk
	public static double[][][] caculateYdemision(double[][][] a,double[][] b){
		
		int xa = a.length;
		int ya = a[0].length;
		int za = a[0][0].length;
		
		int xb = b.length;
		int yb = b[0].length;
		
		double[][][] result = new double[xa][xb][za];
		for(int i = 0;i<xa;i++){
			for(int j = 0;j<xb;j++){
				for(int k = 0;k < za;k++){
					double total = 0.0;
					for(int l = 0;l<yb;l++){
						total += a[i][l][k]*b[j][l];
					}
					result[i][j][k] = total;
				}
			}
		}
		
		return result;		
	}
	// 张量X_ijk  和 矩阵 M_mk 做运算得到张量X_ijm
	public static double[][][] caculateZdemision(double[][][] a,double[][] b){
		
		int xa = a.length;
		int ya = a[0].length;
		int za = a[0][0].length;
		
		int xb = b.length;
		int yb = b[0].length;
		
		double[][][] result = new double[xa][ya][xb];
		
		for(int i = 0;i<xa;i++){
			for(int j = 0;j<ya;j++){
				for(int k = 0;k < xb;k++){
					double total = 0.0;
					for(int l = 0;l<yb;l++){
						total += a[i][j][l]*b[k][l];
					}
					result[i][j][k] = total;
				}
			}
		}
		
		return result;	
	}
	// 矩阵的转置
	public static double[][] transform(double[][] a){
		int x = a.length;
		int y = a[0].length;
		
		double[][] result = new double[y][x];
		for(int i = 0;i<x;i++){
			for(int j = 0;j<y;j++){
				 result[j][i] = a[i][j];
			}
		}
		
		return result;	
	}
	
	//一个数组的排序，前5个最大元素的索引
	public static String sort(double[] data){
		int[] index = new int[data.length];
		for(int i=0;i<index.length;i++){
			index[i] = i;
		}
		for(int i=1;i<data.length;i++){
			int end = i;
			int start = 0;
			for(int j=i-1;j>=0;j--){
				if(data[i] > data[j])continue;
				else {
					start = j+1;
					break;
				}
			}
	 
			if(start==(end -1))continue;
			else{
				
				double temp = data[end];
				int t = index[end];
				for(int l=end;l>start;l--){
				 
			 
					data[l] = data[l-1];
					index[l] = index[l-1];
				}
				data[start] = temp;
				index[start] = t;
			}
			
		}
		
 
		String result = ""+index[0] +"|"+index[1]+"|"+index[2]+"|"+index[3]+"|"+index[4];
	 
		return result;
	}
	// 张量X的X(1)矩阵
	public static double[][] modelR1(double[][][] matrix){
		int xdemision = matrix.length;
		int ydemision = matrix[0].length;
		int zdemision = matrix[0][0].length;
		double[][] result = new double[ xdemision][ydemision*zdemision];
		for(int i = 0;i< zdemision;i++){
			for(int j=0;j< xdemision;j++){
				for(int k=0;k< ydemision;k++){
					result[j][i* ydemision+k] =  matrix[j][k][i];
				}
			}
		}
		return result;
	}
	
	// 张量X的X(2)矩阵
	public static double[][] modelR2(double[][][] matrix){
		int xdemision = matrix.length;
		int ydemision = matrix[0].length;
		int zdemision = matrix[0][0].length;
		
		
		double[][] result = new double[ ydemision][ xdemision*zdemision];
		for(int i = 0;i<zdemision;i++){
			for(int j=0;j<ydemision;j++){
				for(int k=0;k<xdemision;k++){
					result[j][i*xdemision+k] = matrix[k][j][i];
				}
			}
		}
		return result;
	}
	// 张量X的X(3)矩阵
	public static double[][] modelR3(double[][][] matrix){
		int xdemision = matrix.length;
		int ydemision = matrix[0].length;
		int zdemision = matrix[0][0].length;
		double[][] result = new double[zdemision][zdemision*ydemision];
		for(int i = 0;i<ydemision;i++){
			for(int j=0;j<zdemision;j++){
				for(int k=0;k<xdemision;k++){
					result[j][i*xdemision+k] = matrix[k][i][j];
				}
			}
		}
		return result;
	}
	//得到矩阵的SVD分解的左奇异向量
	public static double[][] getSVDU(double[][] matrix){
		Matrix mMatrix = new Matrix(matrix);
		SingularValueDecomposition svd = mMatrix.svd();
		double[][] u = svd.getU().getArray();
		return u;
	}
}
