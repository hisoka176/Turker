package main.source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import main.source.data.*;
import main.source.decomposition.*;
import main.source.modelr.*;
import main.source.validation.*;


public class Test {
	public static void main(String[] args){
		
			// 获取数据
			MysqlData mysql = new MysqlData();
			ArrayList<String> data = mysql.tensor();
			
			// 因为用户ID很大，3214453这种，所以要映射
			Map<Integer,Integer> userMap = mysql.getUserHashMap();
			Map<Integer,Integer> newsMap = mysql.getNewsHashMap();
			Map<Integer,Integer> placeMap = mysql.getPlaceHashMap();
			
			// 分割数据
			SplitData splitData = new SplitData(data);
			HashMap<String,ArrayList<String>> dataMap = splitData.process();
			ArrayList<String> train = dataMap.get("train");
			ArrayList<String> test = dataMap.get("test");
			
			// hosvd
//			HOSVD hosvd = new HOSVD(train,userMap.size(),newsMap.size(),placeMap.size(),1);
//			double[][][] original = hooi.getEstimator();
			//hooi
			HOOI hooi = new HOOI(train,userMap.size(),newsMap.size(),placeMap.size(),10);
			double[][][] original = hooi.getEstimator();
			
			//precision and recall 
			Validation validation = new Validation(original,test);
			String result = validation.printPrecisionAndRecall();
			System.out.println(result);
			
			
			
 
			
		
		
	}
}
