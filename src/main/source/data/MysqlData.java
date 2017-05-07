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
 * ��mysql���ж�ȡ���ݣ��û������˺�д����ע��
 */
public class MysqlData {
	private String driver;
	private String url;
	private String username;
	private String password;
	private Connection conn;
	private static final double EARTH_RADIUS = 6378137;
	//�û�ӳ��������
	private Map userHashMap = new HashMap<String,Integer>();
	private Integer usercount = 0;
	//����ӳ��������
	private Map newsHashMap = new HashMap<String,Integer>();
	private Integer newscount = 0;
	//�ص�����ӳ���
	private Map placeHashMap = new HashMap<String,Integer>();
	private Integer placecount = 0;
	
 
	/*
	 * �û��������������ӳ��
	 */
	public Map getUserHashMap() {
		return userHashMap;
	}
	/* 
	 * ���ź�����������ӳ��
	 */
	public Map getNewsHashMap() {
		return newsHashMap;
	}
	/*
	 * �ص������������ӳ��
	 */
	public Map getPlaceHashMap() {
		return placeHashMap;
	}

	public MysqlData(){
		/* ��ʼ�����ӳ� */
		this.driver = "com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://localhost:3306/turker";
		this.username = "root";
		this.password = "root";
		this.conn = null;
		 
	}
	
	/*
	 * ��MYSQL�������ݣ��������user,news,place,distanceΪ�ַ��������м�¼����ArrayList<String>����
	 */
	private ArrayList<ArrayList<String>> processSQLData(){
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		//��ʼ�����ӳ�
		try{
			Class.forName(this.driver);
			this.conn = (Connection) DriverManager.getConnection(this.url,this.username,this.password);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		//��ȡ����
		String sql ="select * from tweets_sample";
		PreparedStatement pstmt;
		try{
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("load data from mysql...");
			int col = rs.getMetaData().getColumnCount();
			while(rs.next()){
				//�õ�userid,newsid,palceid
				String newsid = rs.getString(1);
				String userid =rs.getString(2);
				String placeid = rs.getString(21);
				//�������
				double userlatitude =rs.getDouble(12);
				double userlongitude =rs.getDouble(13);
				double newslatitude = rs.getDouble(26);
				double newslongitude = rs.getDouble(27);
				
				double distance = this.caculateDistance(userlatitude,userlongitude,newslatitude,newslongitude);
				//�û�ӳ�����
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
				
				
				//��ʱ���ݼ�
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
	//�÷����������Ϊ�����������̵�����
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

	//���ݾ�γ�ȼ��������ص�ľ���
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
