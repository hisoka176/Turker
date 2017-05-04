package main.source.modelr;


import java.util.ArrayList;
import Jama.Matrix;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import main.source.modelr.Content;
import java.util.TreeMap;


public class MyTensor {
	private ArrayList<String>  data = null;
	private int xdemision = 0;
	private int ydemision = 0;
	private int zdemision = 0;
	private double[][][] matrix = null;
	
	public MyTensor(ArrayList<String> data,int xdemision,int ydemision,int zdemision){
		this.data = data;
		this.xdemision = xdemision;
		this.ydemision = ydemision;
		this.zdemision = zdemision;
 
		this.matrix = new double[this.xdemision][this.ydemision][this.zdemision];
		
		for(String str:data){
			String[] array = str.split(",");
			int x = (int)Double.parseDouble(array[0]);
			int y = (int)Double.parseDouble(array[1]);
			int z = (int)Double.parseDouble(array[2]);
			double v = Double.parseDouble(array[3]);
			
			this.matrix[x][y][z] = v;
		}
			
	}
	
	public double[][] modelR1(){
		double[][] result = new double[this.xdemision][this.ydemision*this.zdemision];
		for(int i = 0;i<this.zdemision;i++){
			for(int j=0;j<this.xdemision;j++){
				for(int k=0;k<this.ydemision;k++){
					result[j][i*this.ydemision+k] = this.matrix[j][k][i];
				}
			}
		}
		return result;
	}
	
	public double[][] modelR2(){
		double[][] result = new double[this.ydemision][this.xdemision*this.zdemision];
		for(int i = 0;i<this.zdemision;i++){
			for(int j=0;j<this.ydemision;j++){
				for(int k=0;k<this.xdemision;k++){
					result[j][i*this.xdemision+k] = this.matrix[k][j][i];
				}
			}
		}
		return result;
	}
	
	public double[][] modelR3(){
		double[][] result = new double[this.zdemision][this.zdemision*this.ydemision];
		for(int i = 0;i<this.ydemision;i++){
			for(int j=0;j<this.zdemision;j++){
				for(int k=0;k<this.xdemision;k++){
					result[j][i*this.xdemision+k] = this.matrix[k][i][j];
				}
			}
		}
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
	 
		
		MyTensor tensor = new MyTensor(a,3,2,3);
		double[][] result = tensor.modelR1();
		for(int i=0;i<result.length;i++){
			System.out.println();
			for(int j=0;j<result[0].length;j++){
				System.out.print(" "+result[i][j]);
			}
		}
		
		
		
	}
}
