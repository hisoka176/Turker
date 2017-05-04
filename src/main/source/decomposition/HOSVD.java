package main.source.decomposition;

import java.util.ArrayList;
import java.util.Date;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import main.source.modelr.*;


public class HOSVD {
	private int k = 0;
	private double[][][] original = null;
	
	private double[][] userSVDU = null;
	private double[][] newsSVDU = null;
	private double[][] placeSVDU = null;
	
	public HOSVD(double[][][] core,double[][] user,double[][] news,double[][] place,int k){
		this.k = k;
		this.original = core;
		this.userSVDU = this.getSingleVector(user);
		this.newsSVDU = this.getSingleVector(news);
		this.placeSVDU = this.getSingleVector(place);
		System.out.println(userSVDU.length);
		System.out.println(userSVDU[0].length);
		
		System.out.println(newsSVDU.length);
		System.out.println(newsSVDU[0].length);
		
		System.out.println(placeSVDU.length);
		System.out.println(placeSVDU[0].length);
	}
	
	public double[][] getSingleVector(double[][] ma){
 
		SingularValueDecomposition u1 = new Matrix(ma).svd();
		Matrix u1Vector = u1.getU();
		return u1Vector.getArray();
	}
	
	// dot
	private double[][][] getCoreCube(double[][][] core,double[][] user,double[][] news,double[][] place){
		int corex = core.length;
		int corey = core[0].length;
		int corez = core[0][0].length;
		
		int userx = user.length;
		int usery = user[0].length;
		
		int newsx = news.length;
		int newsy = news[0].length;
		
		int placex = place.length;
		int placey = place[0].length;
		
		//user dot
		double[][][] userResult = new double[userx][corey][corez];
		for(int i=0;i<userx;i++){
			for(int j = 0;j<corey;j++){
				for(int k = 0;k<corez;k++){
					
					double total = 0;
					for(int l=0;l<usery;l++){
						total += core[l][j][k]*user[i][l];
					}
					userResult[i][j][k] =  total;
				}
			}
		}
		
		//news dot
		double[][][] newsResult = new double[userx][newsx][corez];
		for(int i = 0;i<userx;i++){
			for(int j= 0;j<newsx;j++){
				for(int k = 0;k<corez;k++){
					double total = 0;
					for(int l =0;l<newsy;j++){
						total += userResult[i][l][k]*news[j][l];
					}
					newsResult[i][j][k] =  total;
				}
			}
		}
		
		//place dot
		double[][][] placeResult = new double[userx][newsx][placex];
		for(int i = 0;i<userx;i++){
			for(int j= 0;j<newsx;j++){
				for(int k = 0;k<placex;k++){
					double total = 0;
					for(int l =0;l<placey;j++){
						total += userResult[i][j][l]*place[k][l];
					}
					placeResult[i][j][k] =  total;
				}
			}
		}
		
		return placeResult;
		
		
	}
	
	public double[][][] getEstimatorMatrix(){
		double[][][] core = this.getCoreCube(this.original, 
				this.userSVDU, 
				this.newsSVDU,
				this.placeSVDU);
		return this.getCoreCube(core, this.userSVDU, this.newsSVDU, this.placeSVDU);
	}
	
	//get core matrix
	public double[][][] getCoreMatrix(){
		double[][] userT = this.transform(this.userSVDU);
		double[][] newT = this.transform( this.newsSVDU);
		double[][] placeT = this.transform(this.placeSVDU);
		
		return this.getCoreCube(this.original,userT,newT,placeT);
	}
	
	
	private double[][] transform(double[][] t){
		int x = t.length;
		int y = t[0].length;
		double[][] matrix = new double[y][x];
		for(int i = 0;i<x;i++){
			for(int j = 0;j<y;j++){
				matrix[j][i] = t[i][j];
			}
		}
		return matrix;
	}
	
}
