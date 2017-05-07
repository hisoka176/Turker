package main.source.decomposition;

import java.util.ArrayList;

import main.source.common.MyUtil;
import main.source.modelr.MyTensor;
/*
 * 在HOSVD的基础上得到HOOI
 */
public class HOOI {
 
	HOSVD hosvd = null;
	int iterator = 0;//迭代的次数
	
	public HOOI(ArrayList<String> a,int xdemision,int ydemision,int zdemision,int iterator){
		
		hosvd = new HOSVD(a,xdemision,ydemision,zdemision,0);
		this.iterator = iterator;
	}
	// 得到估计的张量X，产生推荐列表
	public double[][][] getEstimator(){
		double[][] u1 = hosvd.getU1();
		double[][] u2 = hosvd.getU2();
		double[][] u3 = hosvd.getU3();
		double[][][] original = hosvd.getCoreMatrix();
		
		
		double[][][] core = null;
		double[][][] estimator = null;
		for(int i = 0;i<this.iterator;i++){
			core = MyUtil.caculateXdemision(original,MyUtil.transform(u1));
			core  = MyUtil.caculateYdemision(core, MyUtil.transform(u2));
			core = MyUtil.caculateZdemision(core, MyUtil.transform(u3));
			
			double[][] a = MyUtil.modelR1(core);
			double[][] b = MyUtil.modelR2(core);
			double[][] c = MyUtil.modelR3(core);
			
			u1 = MyUtil.getSVDU(a);
			u2 = MyUtil.getSVDU(b);
			u3 = MyUtil.getSVDU(c);
		}
		
		 estimator = MyUtil.caculateXdemision(core,u1);
		 estimator = MyUtil.caculateXdemision(estimator,u1);
		 estimator = MyUtil.caculateXdemision(estimator,u1);
		 
		 return estimator;
 
	}
}
