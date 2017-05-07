package main.source.data;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.math.*;
import java.util.*;

/* 
 * 从mysql当中读取数据，用户名、账号写死，注意
 */
public class MysqlData {
	private String driver;
	private String url;
	private String username;
	private String password;
	private Connection conn;
	private static final double EARTH_RADIUS = 6378137;
	//用户映射索引表
	private Map userHashMap = new HashMap<String,Integer>();
	private Integer usercount = 0;
	//新闻映射索引表
	private Map newsHashMap = new HashMap<String,Integer>();
	private Integer newscount = 0;
	//地点索引映射表
	private Map placeHashMap = new HashMap<String,Integer>();
	private Integer placecount = 0;
	
 
	/*
	 * 用户和张量的坐标的映射
	 */
	public Map getUserHashMap() {
		return userHashMap;
	}
	/* 
	 * 新闻和张量的坐标映射
	 */
	public Map getNewsHashMap() {
		return newsHashMap;
	}
	/*
	 * 地点和张量的坐标映射
	 */
	public Map getPlaceHashMap() {
		return placeHashMap;
	}

	public MysqlData(){
		/* 初始化连接池 */
		this.driver = "com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://localhost:3306/turker";
		this.username = "root";
		this.password = "root";
		this.conn = null;
		 
	}
	
	/*
	 * 从MYSQL读入数据，最后结果以user,news,place,distance为字符串，所有记录放在ArrayList<String>当中
	 */
	private ArrayList<ArrayList<String>> processSQLData(){
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		//初始化连接池
		try{
			Class.forName(this.driver);
			this.conn = (Connection) DriverManager.getConnection(this.url,this.username,this.password);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		//读取数据
		String sql ="select * from tweets_sample";
		PreparedStatement pstmt;
		try{
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("load data from mysql...");
			int col = rs.getMetaData().getColumnCount();
			while(rs.next()){
				//得到userid,newsid,palceid
				String newsid = rs.getString(1);
				String userid =rs.getString(2);
				String placeid = rs.getString(21);
				//计算距离
				double userlatitude =rs.getDouble(12);
				double userlongitude =rs.getDouble(13);
				double newslatitude = rs.getDouble(26);
				double newslongitude = rs.getDouble(27);
				
				double distance = this.caculateDistance(userlatitude,userlongitude,newslatitude,newslongitude);
				//用户映射添加
				if(!this.userHashMap.containsKey(userid)){
					this.userHashMap.put(userid,this.usercount);
					this.usercount += 1;
				}
				
				if (!this.newsHashMap.containsKey(newsid)){
					this.newsHashMap.put(newsid, this.newscount);
					this.newscount += 1;
				}
				
				if (!this.placeHashMap.containsKey(placeid)){
					this.placeHashMap.put(placeid, this.placecount);
					this.placecount += 1;
				}
				
				
				//临时数据集
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(userid);
				temp.add(newsid);
				temp.add(placeid);
				temp.add(""+distance);
				data.add(temp);	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return data;
		
	}
	
	public ArrayList<String> tensor(){
		ArrayList<ArrayList<String>> data = this.processSQLData();
		ArrayList<String> result = this.getTensor(data);
	 
		return result;
		
	}
	//该方法的输出作为构建张量过程的输入
	private  ArrayList<String> getTensor(ArrayList<ArrayList<String>> data){
		ArrayList<String> result = new ArrayList<String>();
		for(ArrayList<String> temp:data){
			String user = temp.get(0);
			String news = temp.get(1);
			String place = temp.get(2);
			String value = temp.get(3);
			
			int xindex = (int)this.userHashMap.get(user);
			int yindex = (int)this.newsHashMap.get(news);
			int zindex = (int)this.placeHashMap.get(place);
			result.add(""+xindex+","+yindex+","+zindex+","+Double.parseDouble(value));
		}
		return result;
	}
	
	
	
	private static double rad(double d)  
	{  
		return (double)(d * Math.PI / 180.0);  
	}

	//根据经纬度计算两个地点的距离
    private static double caculateDistance(double lon1,double lat1,double lon2, double lat2)  
    {  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lon1) - rad(lon2);  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
       return (double)s;  
    }

	public static void main(String[] args){
		MysqlData a = new MysqlData();
		ArrayList<String> b = a.tensor();
	 
	 
		
			
		
		 
	}

}
