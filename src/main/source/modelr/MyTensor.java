package main.source.modelr;


import java.util.ArrayList;
import Jama.Matrix;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
 
import java.util.TreeMap;

/*
 * 构建张量使用
 */
public class MyTensor {
	private ArrayList<String>  data = null;
	private int xdemision = 0;
	private int ydemision = 0;
	private int zdemision = 0;
	private double[][][] matrix = null;//存放的数据
	
	//读取ArrayList<String>构建张量
	public MyTensor(ArrayList<String> data,int xdemision,int ydemision,int zdemision){
		System.out.println("construct tensor...");
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
	// 得到X(1),X是构建的张量
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
	
	// 得到X(2),X是构建的张量
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
	// 得到X(3),X是构建的张量
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
	
 
	// 简单测试
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
	// 构建的张量
	public double[][][] getMatrix() {
		return matrix;
	}
}
