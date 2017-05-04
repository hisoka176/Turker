package main.source.modelr;

public class Tuple{
	int x = 0;
	int y = 0;
	double v = 0;
	public Tuple(int x,int y,double v){
		this.x = x;
		this.y = y;
		this.v = v;
	}
	
	public int _1(){
		return this.x;
	}
	
	public int _2(){
		return this.y;
	}
	
	public double _3(){
		return this.v;
	}
	
	public String toString(){
		return "x:"+ this.x+" y:" + this.y + " z:" + this.v;
	}
}
