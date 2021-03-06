package main.source.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 *  
 * 这个类负责切分数据，按照2:8的比例划分数据，训练集占8份
 */
public class SplitData {
	ArrayList<String> data = null;
	public SplitData(ArrayList<String> data){
		this.data = data;
		System.out.println("start split data...");
	}
	
	public HashMap<String,ArrayList<String>> process(){
		// 最后的结果放在一个MAP当中
		// key:train -> value:训练数据（ArrayList<String>）
		// key:test  -> value:测试数据（ArrayList<String>）
		HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
		Random random = new Random();
		double max = -100000000000000d;
		double min = 1000000000000000000000000d;
 
		for(String str:this.data){
			String[] array = str.split(",");
			double v = Double.parseDouble(array[3]);
			max = max > v?max:v;
			min = min < v?min:v;
			 
		}
		
		
		ArrayList<String> trainData = new ArrayList<String>();
		ArrayList<String> testData = new ArrayList<String>();
		
		for(String str:data){
			String[] array = str.split(",");
			double value = Double.parseDouble(array[3]);
			double xindex = Integer.parseInt(array[0]);
			double yindex = Integer.parseInt(array[1]);
			double zindex = Integer.parseInt(array[2]);
				 
			value = (value - min)/(max - min);
			if(random.nextFloat()<0.8){//调解划分比例
				trainData.add(""+xindex+","+yindex+","+zindex+","+value);
			}else{
				testData.add(""+xindex+","+yindex+","+zindex+","+value);
			}
			
			
		}
		
		result.put("train", trainData);
		result.put("test", testData);
		
		return result;	
	}
	
}
