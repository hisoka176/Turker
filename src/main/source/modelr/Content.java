package main.source.modelr;

import java.util.ArrayList;
import java.util.Map;

import main.source.modelr.Tuple;


public class Content{
	
	ArrayList<Tuple> content = null;
	int x = 0;
	int y = 0;
	
	public Content(int x,int y){
		this.x = x;
		this.y = y;
		content = new ArrayList<Tuple>();
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void addElement(int x,int y,double v){
		Tuple temp = new Tuple(x,y,v);
		this.content.add(temp);
	}
	//返回矩阵
	public double[][] generateMatrix(){
		
		int size = content.size();
		if(size == 0)return new double[this.x][this.y];
		int[] x = new int[size];
		int[] y = new int[size];
		double[] v = new double[size];
		
		for(int i=0;i<size;i++){
			Tuple temp = content.get(i);
			x[i] = temp._1();
			y[i] = temp._2();
			v[i] = temp._3();
		}
		
		double[][] matrix = this.makeMatrix(x,y,v);
		return matrix;
		
	}
	
	// 得到纵坐标的一个矩阵
	private double[][] makeMatrix(int[] px,int[] py,double[] pv){
		
		
		double[][] matrix = new double[this.x][this.y];
		for(int i=0;i<px.length;i++){
			matrix[px[i]][py[i]] = pv[i];
		}
		return matrix;
	}
	public int size(){
		return this.content.size();
	}
	public String toString(){
		String  result = "";
		for(Tuple temp:this.content){
			result = result+ temp.toString() + " ||　";
		}
		return result;
	}
}
