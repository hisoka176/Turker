package main.source.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import main.source.common.MyUtil;

/* 
 * 输出准确率和召回率
 */
public class Validation {
	
	double[][][] original = null ;
	ArrayList<String> test = null;
	// original:张量的估计，就是填补缺失值后的张量
	// test:在splitData   8:2划分当中2的那部分，构建张量用8的部分
	public Validation(double[][][] original,ArrayList<String> test){
		System.out.println("validation the predict...");
		this.original = original;
		this.test = test;
	}
	// 得到推荐鞋标
	public ArrayList<String> getTop5X(){
		
		ArrayList<String> result = new ArrayList<String>();
		
		int x = this.original.length;
		int y = this.original[0].length;
		int z = this.original[0][0].length;
		
		for(int i = 0;i<x;i++ ){
			for(int k = 0;k<z;k++){
				double[] temp = new double[y];
				for(int j = 0;j<y;j++){
					temp[j] = this.original[i][j][k];
				}
				String string = MyUtil.sort(temp);
				String total = ""+i+","+k+","+string;
				
				result.add(total);
			}
		}
		return result;
		
	}
	// 最后的字符串
	public String printPrecisionAndRecall(){
		HashMap<Integer,HashMap<Integer,String>> testMap = this.makeTestMapY(this.test);
		ArrayList<String> originalList = this.getTop5X();
		
		HashMap<Integer,HashMap<Integer,String>>  predictMap = this.makePredictMap(originalList);
		ArrayList<String> result = makePrecisionAndRecall(predictMap,testMap);
		String string = this.getPrecisionAndRecall(result);
		return string;
		
	}
	// 可以忽略
	public ArrayList<String> getTop5Y(){
		ArrayList<String> result = new ArrayList<String>();
		
		int x = this.original.length;
		int y = this.original[0].length;
		int z = this.original[0][0].length;
		
		for(int i = 0;i<x;i++ ){
			for(int j = 0;j<y;j++){
				double[] temp = new double[z];
				for(int k = 0;k<z;k++){
					temp[j] = this.original[i][j][k];
				}
				String string = MyUtil.sort(temp);
				String total = ""+i+","+"j"+string;
				
				result.add(total);
			}
		}
		return result;
	}
	// 产生测试的MAP形式
		// key:user
		// value:
		// 			key:place
		//          value:新闻1|新闻2
	public HashMap<Integer,HashMap<Integer,String>> makeTestMapX(ArrayList<String> test){
		HashMap<Integer,HashMap<Integer,String>> testMap = new HashMap<Integer,HashMap<Integer,String>>();
		for(String str:test){
			String[] array = str.split(",");
			int user = Integer.parseInt(array[0]);
			int news = Integer.parseInt(array[1]);
			int place = Integer.parseInt(array[2]);
			double value = Double.parseDouble(array[3]);
			if(!testMap.containsKey(user)){
				HashMap<Integer,String> temp = new HashMap<Integer,String>();
			}
			HashMap<Integer,String> temp = testMap.get(user);
			if(!temp.containsKey(news)){
				temp.put(news, "");
			}
			String placeString = temp.get(news);
			temp.put(news, ""+placeString+"|"+place);
		}
		return testMap;
		
	}
	
	// 可以忽略
	public HashMap<Integer,HashMap<Integer,String>> makeTestMapY(ArrayList<String> test){
		HashMap<Integer,HashMap<Integer,String>> testMap = new HashMap<Integer,HashMap<Integer,String>>();
		for(String str:test){
			String[] array = str.split(",");
			int user = (int)Double.parseDouble(array[0]);
			int news = (int)Double.parseDouble(array[1]);
			int place = (int)Double.parseDouble(array[2]);
			double value = Double.parseDouble(array[3]);
			if(!testMap.containsKey(user)){
				HashMap<Integer,String> temp = new HashMap<Integer,String>();
				testMap.put(user, temp);
			}
			HashMap<Integer,String> temp = testMap.get(user);
			if(!temp.containsKey(place)){
				temp.put(place, "");
			}
			String newsString = temp.get(place);
			temp.put(place, ""+newsString+"|"+news);
		}
		return testMap;
		
	}
	// 产生预测的MAP形式
	// key:user
	// value:
	// 			key:place
	//          value:新闻1|新闻2
	public HashMap<Integer,HashMap<Integer,String>> makePredictMap(ArrayList<String> content){
		HashMap<Integer,HashMap<Integer,String>> testMap = new HashMap<Integer,HashMap<Integer,String>>();
		for(String string:content){
			String[] array = string.split(",");
			int x = Integer.parseInt(array[0]);
			int y = Integer.parseInt(array[1]);
			
			String recommendList = array[2];
			if(!testMap.containsKey(x)){
				testMap.put(x,new HashMap<Integer,String>());
			}
			HashMap<Integer,String> yMap = testMap.get(x);
		
			yMap.put(y, recommendList);
		}
		return testMap;
	}
	// 调用的步骤
	public String getPrecisionAndRecall(ArrayList<String> test){
		double  precisionTotal = (double)test.size()*5;
		double commonTotal = 0.0;
		double recallTotal = 0.0;
		for (String string:test){
			String[] array = string.split(",");
			int common = Integer.parseInt(array[0]);
			int recall =  Integer.parseInt(array[1]);
			commonTotal += common;
			recallTotal += recall;
		}
		String result = "precision:" + commonTotal/precisionTotal+"\nrecall:" + commonTotal/recallTotal;
		return result;
	}
	
	
	// 根绝推荐和测试产生准确率和召回率
	public ArrayList<String> makePrecisionAndRecall(HashMap<Integer,HashMap<Integer,String>> predict,HashMap<Integer,HashMap<Integer,String>> test){
		ArrayList<String> precisionAndRecall = new ArrayList<String>();
		Set<Entry<Integer,HashMap<Integer,String>>> entryset = test.entrySet();
		Iterator<Entry<Integer,HashMap<Integer,String>>> iterator =  entryset.iterator();
		
		while(iterator.hasNext()){
			Entry<Integer,HashMap<Integer,String>> entry = iterator.next();
			Integer user = entry.getKey();
			HashMap<Integer,String> needToTest = entry.getValue();
			
			if(!predict.containsKey(user)){
				continue;
			}else{
				// 需要预测的部分内容
				HashMap<Integer,String> needToPredict = predict.get(user);
				// 测试集里面的数据
				Set<Integer> keySet = needToTest.keySet();
				Iterator<Integer> keyIterator = keySet.iterator();
				while(keyIterator.hasNext()){
					Integer key = keyIterator.next();
					
					
					if(needToPredict.containsKey(key)){
						
						
						// test part
						String testString = needToTest.get(key);
						testString = testString.substring(1);
						String[] testArray = testString.split("\\|");
				 
						
						ArrayList<String> testList = new ArrayList<String>();
						for(String tstr:testArray){
							testList.add(tstr);
						}
						
						Set<String> testSet = new HashSet<String>(testList);
						// predict part 
						String predictString = needToPredict.get(key);
						String[] predictArray = predictString.split("\\|");
						
						ArrayList<String> predictList = new ArrayList<String>();
						for(String tstr:predictArray){
							predictList.add(tstr);
						}
						
						Set<String> predictSet = new HashSet<String>(predictList);
						//result part
						Set<String> result = new HashSet<String>();
						result.addAll(testSet);
						result.retainAll(predictSet);
						String resultString = ""+result.size()+","+needToTest.size();
						precisionAndRecall.add(resultString);
						
					}else{
						continue;
					}
					
					
				}
			}
		}
		return precisionAndRecall;
	}
}
