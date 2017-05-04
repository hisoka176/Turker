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
import main.source.validation.*;

public class Test {
	public static void main(String[] args){
		
			MysqlData mysql = new MysqlData();
			ArrayList<String> data = mysql.tensor();
			
			HashMap<String,Integer> userMap = (HashMap<String,Integer>)mysql.getUserHashMap();
			HashMap<String,Integer> newsMap = (HashMap<String,Integer>)mysql.getNewsHashMap();
			HashMap<String,Integer> placeMap = (HashMap<String,Integer>)mysql.getPlaceHashMap();
			
 
			SplitData splitdata = new SplitData(data);
			HashMap<String,ArrayList<String>> content = splitdata.process();
 
			
			ArrayList<String> train = content.get("train");
			ArrayList<String> test = content.get("test");
			
			Tensor tensor = new Tensor(train,userMap.size(),newsMap.size(),placeMap.size());
			double[][] userMatrix = tensor.modelR1();
//			double[][] newsMatrix = tensor.modelR2();
//			double[][] placeMatrix = tensor.modelR3();
			double[][][] originalMatrix = tensor.getMatrix();
			
			System.out.println(userMatrix.length);
			System.out.println(userMatrix[0].length);
			
			System.out.println(newsMatrix.length);
			System.out.println(newsMatrix[0].length);
			
			System.out.println(placeMatrix.length);
			System.out.println(placeMatrix[0].length);
			
//			HOSVD hosvd = new HOSVD(originalMatrix,userMatrix,newsMatrix,placeMatrix,2);
			
		
		
	}
}
