package main.source.decomposition;
import java.util.ArrayList;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import main.source.modelr.MyTensor;
import main.source.common.MyUtil;


public class HOSVD {
	MyTensor tensor = null;
	int k = 0;
	/*
	 * xdemision :张量X维度
	 * ydemision :张量Y维度
	 * zdemision :张量Z维度
	 * K决定core张量的大小，在HOOI需要k=0，需要不断迭代
	 */
	public HOSVD(ArrayList<String> a,int xdemision,int ydemision,int zdemision,int k){
		System.out.println("start hosvd process...");
		tensor = new MyTensor(a,xdemision,ydemision,zdemision);
 
		this.k = k;
	}
	

	
	// 得到X(1)的SVD分解后的左奇异向量
	public double[][] getU1(){
		Matrix u1Matrix = new Matrix(this.tensor.modelR1());
		SingularValueDecomposition sd = u1Matrix.svd();
		double[][] source = sd.getU().getArray();
		
		int x = source.length;
		int y = source.length;
		
		double[][] result = MyUtil.removeNoise(source,this.k);
	
//		System.out.println("-----------------u1------------------");
//		System.out.println(u1Matrix.getRowDimension());
//		System.out.println(u1Matrix.getColumnDimension());
//		
//		System.out.println(source.length);
//		System.out.println(source[0].length);
//		
//		System.out.println(result.length);
//		System.out.println(result[0].length);
		
		return result;
		
	}
	
	// 得到X(2)的SVD分解后的左奇异向量
	public double[][] getU2(){
		Matrix u2Matrix = new Matrix(this.tensor.modelR2());
		SingularValueDecomposition sd = u2Matrix.svd();
		double[][] source = sd.getU().getArray();
		
		int x = source.length;
		int y = source.length;
		
		double[][] result = MyUtil.removeNoise(source,this.k);
		
//		System.out.println("-----------------u2------------------");
//		System.out.println(u2Matrix.getRowDimension());
//		System.out.println(u2Matrix.getColumnDimension());
//		
//		System.out.println(source.length);
//		System.out.println(source[0].length);
		
		return result;
	}
	// 得到X(3)的SVD分解后的左奇异向量
	public double[][] getU3(){
		Matrix u3Matrix = new Matrix(this.tensor.modelR3());
		SingularValueDecomposition sd = u3Matrix.svd();
		double[][] source = sd.getU().getArray();
		
		int x = source.length;
		int y = source.length;
		
		double[][] result = MyUtil.removeNoise(source,this.k);
//		System.out.println("-----------------u3------------------");
//		System.out.println(u3Matrix.getRowDimension());
//		System.out.println(u3Matrix.getColumnDimension());
//		
//		System.out.println(source.length);
//		System.out.println(source[0].length);
		
		return result;
	}
	
	
	//得到核心core张量
	public double[][][] getCoreMatrix(){
		double[][] u1 = this.getU1();
		double[][] u2 = this.getU2();
		double[][] u3 = this.getU3();
		double[][][] original = tensor.getMatrix();
		
		double[][][] result = this.caculateCoreMatrix(original, u1, u2, u3);
		return result;
	}
	// 得到张量X的估计，这个估计张量来得到推荐列表
	public double[][][] getEstimator(){
		double[][] u1 = this.getU1();
		double[][] u2 = this.getU2();
		double[][] u3 = this.getU3();
		
		double[][][] core = this.getCoreMatrix();
//		System.out.println("-----------");
//		System.out.println(core.length);
//		System.out.println(core[0].length);
//		System.out.println(core[0][0].length);
// 
//		System.out.println("-----------");
//		System.out.println(u1.length);
//		System.out.println(u1[0].length);
//		
//		System.out.println("-----------");
//		System.out.println(u2.length);
//		System.out.println(u2[0].length);
//		
//		
//		System.out.println("-----------");
//		System.out.println(u3.length);
//		System.out.println(u3[0].length);
		double[][][] estimatorMatrix = MyUtil.caculateXdemision(core,u1);
//		System.out.println("-----------");
//		System.out.println(estimatorMatrix.length);
//		System.out.println(estimatorMatrix[0].length);
//		System.out.println(estimatorMatrix[0][0].length);
		estimatorMatrix = MyUtil.caculateYdemision(estimatorMatrix,u2);
		estimatorMatrix = MyUtil.caculateZdemision(estimatorMatrix,u3);
		
//		System.out.println(estimatorMatrix.length);
//		System.out.println(estimatorMatrix[0].length);
//		System.out.println(estimatorMatrix[0][0].length);
		
		return estimatorMatrix;
	}
	// 做张量X和矩阵X(1)、矩阵X(2)、矩阵X(3)的运算
	public double[][][] caculateCoreMatrix(double[][][] core,double[][] a,double[][] b,double[][] c){
		
//		System.out.println(core.length);
//		System.out.println(core[0].length);
//		System.out.println(core[0][0].length);
//		
//		
//		System.out.println(c.length);
//		System.out.println(c[0].length);
 
		double[][][] result = MyUtil.caculateXdemision(core, MyUtil.transform(a));
		result = MyUtil.caculateYdemision(result, MyUtil.transform(b));
		result = MyUtil.caculateZdemision(result, MyUtil.transform(c));
//		
//		System.out.println(result.length);
//		System.out.println(result[0].length);
//		System.out.println(result[0][0].length);
		
		return result;
	}
	
	
	public static void main(String[] args){
		
		ArrayList<String> a = new ArrayList<String> ();
		
		a.add("0,0,0,1");
		a.add("0,1,0,1");
		a.add("1,0,0,0");
		a.add("1,1,0,0");
		a.add("2,0,0,0");
		a.add("2,1,0,0");
		
		a.add("0,0,1,0");
		a.add("0,1,1,0");
		a.add("1,0,1,1");
		a.add("1,1,1,0");
		a.add("2,0,1,0");
		a.add("2,1,1,0");
		
		a.add("0,0,2,0");
		a.add("0,1,2,0");
		a.add("1,0,2,0");
		a.add("1,1,2,0");
		a.add("2,0,2,0");
		a.add("2,1,2,1");
		
		a.add("0,0,3,0");
		a.add("0,1,3,0");
		a.add("1,0,3,1");
		a.add("1,1,3,0");
		a.add("2,0,3,0");
		a.add("2,1,3,1");
		
		HOSVD hosvd = new HOSVD(a,3,2,4,1);
		double[][][] core = hosvd.getCoreMatrix();
 
		System.out.println("++++++++++");
		hosvd.getEstimator();
		System.out.println("++++++++++");
		
		
 
		
	}
	
	

}
