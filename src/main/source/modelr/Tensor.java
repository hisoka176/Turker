package main.source.modelr;

import java.util.ArrayList;
import Jama.Matrix;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import main.source.modelr.Content;
import java.util.TreeMap;

public class Tensor {
	
	private int[] xarray ;
	private int[] yarray ;
	private int[] zarray ;
	private double[] value;
	
 
	
	private  double[][][] matrix = null;
	
 
	private int xdemision = 0;
	private int ydemision = 0;
	private int zdemision = 0;
	
 
	
	// content 
	public double[][][] getMatrix() {
		return matrix;
	}


	// 初始化
	public Tensor(ArrayList<String> data,int xdemision,int ydemision,int zdemision){
		
		this.xdemision = xdemision;
		this.ydemision = ydemision;
		this.zdemision = zdemision;
		
		this.matrix = new double[this.xdemision][this.ydemision][this.zdemision];
		
		int length = data.size();
		xarray = new int[length];
		yarray = new int[length];
		zarray = new int[length];
		value = new double[length];
		
		int len = 0;
		for (String temp:data){
			
			String[] array = temp.split(",");
			int x = (int)Double.parseDouble(array[0]);
			int y = (int)Double.parseDouble(array[1]);
			int z = (int)Double.parseDouble(array[2]);
			double v = Double.parseDouble(array[3]);
			this.xarray[len] = x;
			this.yarray[len] = y;
			this.zarray[len] = z;
			this.value[len] = v;
			
			this.matrix[x][y][z] = v;

			len += 1;
		}
				
	}
	
	
	// tube1维度的情况
	public double[][] modelR1(){
		 
		Map<Integer,Content> result = new TreeMap<Integer,Content>();
		
		for(int i=0;i<this.zarray.length;i++){
			if(!result.containsKey(this.zarray[i])){
			 
				result.put(this.zarray[i],new Content(this.xdemision ,this.ydemision ));
			}
			Content temp = result.get(this.zarray[i]);
			temp.addElement(this.xarray[i], this.yarray[i], this.value[i]);
		 
		}
		
		double[][][] mergeMatrix = this.generateModel(result);
		double[][] matrix = this.mergeMatrix(mergeMatrix);
		
//		System.out.println();
//		for(int i = 0;i<matrix.length;i++){
//			for(int j = 0;j<matrix[0].length;j++){
//				System.out.print("	"+matrix[i][j]);
//			}
//			System.out.println();
//		}
		return matrix;
	}
	
	// tube2维度的情况
	public double[][] modelR2(){
		 
		Map<Integer,Content> result = new TreeMap<Integer,Content>();
		
		for(int i=0;i<this.zarray.length;i++){
			if(!result.containsKey(this.zarray[i])){
				result.put(this.zarray[i],new Content(this.ydemision,this.xdemision));
			}
			Content temp = result.get(this.zarray[i]);
			temp.addElement(this.yarray[i],this.xarray[i],this.value[i]);
	 
		}
		
		double[][][] mergeMatrix = this.generateModel(result);
		double[][] matrix = this.mergeMatrix(mergeMatrix);
		
		System.out.println();
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0;j<matrix[0].length;j++){
				System.out.print("	"+matrix[i][j]);
			}
			System.out.println();
		}
		
		return matrix;
	}
	
	// tuble3维度的情况
	public double[][] modelR3(){
		 
		Map<Integer,Content> result = new TreeMap<Integer,Content>();
		
		for(int i=0;i<this.yarray.length;i++){
			if(!result.containsKey(this.yarray[i])){
				result.put(this.yarray[i],new Content(this.zdemision,this.xdemision));
			}
			Content temp = result.get(this.yarray[i]);
			temp.addElement(this.zarray[i],this.xarray[i], this.value[i]);
		 
			
		}
			
		double[][][] mergeMatrix = this.generateModel(result);
		double[][] matrix = this.mergeMatrix(mergeMatrix);
		
		
		System.out.println();
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0;j<matrix[0].length;j++){
				System.out.print("	"+matrix[i][j]);
			}
			System.out.println();
		}
		return matrix;
	}
	
	// 产生demision matrix
	private double[][][] generateModel(Map<Integer,Content> result){
		Iterator iterator = result.entrySet().iterator();
		
		int x = result.keySet().size();
		double[][][] cube = null;
		
		while(iterator.hasNext()){
			Map.Entry<Integer,Content> temp =(Map.Entry) iterator.next();
			int key = temp.getKey();
			Content content = temp.getValue();
		 
			if(cube == null){
				cube = new double[x][content.getX()][content.getY()];
			}
			
			cube[key] = content.generateMatrix();
		 
		}
	 
//		for(int i=0;i<cube.length;i++){
//			for(int j = 0;j<cube[0].length;j++){
//				for(int k=0;k<cube[0][0].length;k++){
//					System.out.print(":" +cube[i][j][k]);
//				}
//			}
//		}
		return cube;
		
	}

 
	
	private double[][] mergeMatrix(double[][][] total){
		int tlength = total.length;
		int x = total[0].length;
		int y = total[0][0].length;
		
		
		double[][] matrix = new double[x][y*tlength];
		for(int i = 0;i<total.length;i++){
			double[][] temp = total[i];
			for(int j = 0;j<temp.length;j++){
				for(int k = 0;k<temp[0].length;k++){
					matrix[j][k+i*y] = temp[j][k];
				}
			}
		}
		return matrix;	
	}
	
	//矩阵的转置
/*	private double[][] transform(double[][] t){
		int x = t.length;
		int y = t[0].length;
		double[][] matrix = new double[y][x];
		for(int i = 0;i<x;i++){
			for(int j = 0;j<y;j++){
				matrix[j][i] = t[i][j];
			}
		}
		return matrix;
	}*/
	
	public static void main(String[] args){
		  
		ArrayList<String> bb = new ArrayList<String>();
		
		bb.add("0,0,0,1");
		bb.add("0,1,0,3");
		bb.add("1,0,0,2");
		bb.add("1,1,0,4");
		bb.add("0,0,1,5");
		bb.add("0,1,1,7");
		bb.add("1,0,1,6");
		bb.add("1,1,1,8");
	 
	}

}



