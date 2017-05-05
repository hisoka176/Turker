package main.source.decomposition;
import java.util.ArrayList;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import main.source.modelr.MyTensor;
public class HOSVD {
	MyTensor tensor = null;
	public HOSVD(ArrayList<String> a,int xdemision,int ydemision,int zdemision,int k){
		tensor = new MyTensor(a,xdemision,ydemision,zdemision);
	}
	
	public void getU1(){
		Matrix u1Matrix = new Matrix(this.tensor.modelR1());
		SingularValueDecomposition sd = u1Matrix.svd();
		System.out.println(u1Matrix.getRowDimension());
		System.out.println(u1Matrix.getColumnDimension());
		
		System.out.println(sd.getU().getRowDimension());
		System.out.println(sd.getU().getColumnDimension());
		
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
		
		HOSVD hosvd = new HOSVD(a,3,2,3,1);
		hosvd.getU1();
		
	}
	

}
