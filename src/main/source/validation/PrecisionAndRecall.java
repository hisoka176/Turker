package main.source.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrecisionAndRecall {
	
	double[][][] estimatorMatrix  = null;
	ArrayList<String> test = null;
	
	public PrecisionAndRecall(){
		
	}
	public PrecisionAndRecall(double[][][] estimatorMatrix,ArrayList<String> test){
		this.estimatorMatrix  = estimatorMatrix;
		this.test = test;
	}
	
	//ÅÅÐò²¢·µ»ØË÷Òý
	private String sort(double[] data){
		double[] index = new double[data.length];
		for(int i=0;i<index.length;i++){
			index[i] = i;
		}
		for(int i=1;i<data.length;i++){
			int end = i;
			int start = 0;
			for(int j=i-1;j>=0;j--){
				if(data[i] > data[j])continue;
				else {
					start = j+1;
					break;
				}
			}
	 
			if(start==(end -1))continue;
			else{
				
				double temp = data[end];
				double t = index[end];
				for(int l=end;l>start;l--){
				 
			 
					data[l] = data[l-1];
					index[l] = index[l-1];
				}
				data[start] = temp;
				index[start] = t;
			}
			
		}
		
//		HashMap<String,HashMap<Integer,Double>> result= new HashMap<String,HashMap<Integer,Double>>();
//		HashMap<Integer,Double> dataMap = new HashMap<Integer,Double>();
//		HashMap<Integer,Double> indexMap = new HashMap<Integer,Double>();
		
		
//		dataMap.put(1, data[0]);
//		dataMap.put(2, data[1]);
//		dataMap.put(3, data[2]);
//		dataMap.put(4, data[3]);
//		dataMap.put(5, data[4]);
// 
//		indexMap.put(1, index[0]);
//		indexMap.put(2, index[1]);
//		indexMap.put(3, index[2]);
//		indexMap.put(4, index[3]);
//		indexMap.put(5, index[4]);
// 
//		result.put("socre", dataMap);
//		result.put("index", indexMap);
		String result = ""+index[0] +","+index[1]+","+index[2]+","+index[3]+","+index[4];
	 
		return result;
	}
	public HashMap<Integer,HashMap<Integer,String>> getTop5(){
		
		HashMap<Integer,HashMap<Integer,String>> topMap = new HashMap<Integer,HashMap<Integer,String>>();
		int x = this.estimatorMatrix.length;
		for(int i = 0;i<x;i++){
			double[][] matrix = this.estimatorMatrix[i];
			
			int y = matrix.length;
			int z = matrix[0].length;
			HashMap<Integer,String> placeMap = new HashMap<Integer,String>();
			for(int j=0;j<y;j++){
				double[] temp = new double[z];
				for(int k = 0;k<z;k++){
					temp[k] = matrix[j][k];
				}
				String result = this.sort(temp);
				placeMap.put(j, result);
			}
			topMap.put(i, placeMap);	
		}
		
		return topMap;
	}
	
	public void estimator(
			HashMap<Integer,HashMap<Integer,String>> top5,ArrayList<String> test,
			HashMap<Double,Integer> userHashMap,
			HashMap<Double,Integer> placeHashMap,
			HashMap<Double,Integer> newsHashMap){
		Set<String> differentUser = new HashSet<String>();
		double precision = 0.0;
		
		for(String str:test){
			String[] array = str.split(",");
			String user =  array[0];
			differentUser.add(user);
			String news =  array[1];
			String place =  array[2];
			double  value =  Double.parseDouble(array[3]);
			
			int userIndex = userHashMap.get(user);
			int newsIndex = newsHashMap.get(news);
			int placeIndex = placeHashMap.get(place);
			
			HashMap<Integer,String> placeMap =  top5.get(userIndex);
			String content = placeMap.get(placeIndex);
			String[] indexes = content.split(",");
			
			Set<String> result = new HashSet<String>();
			for(String str1:indexes){
				result.add(str1);
			}
			
			String newsMark = ""+newsIndex;
			if(result.contains(newsIndex)){
				precision += 1;
			}
			
			System.out.println("+----------------------------+");
			System.out.println("|      precision:"+precision/(differentUser.size()*5) +"recall:"+precision/test.size());
			System.out.println("+----------------------------+");
		 
		 	
		}
		
		
		
	}
	

	
	
}
