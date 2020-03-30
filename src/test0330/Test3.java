package test0330;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.util.DBConn;

public class Test3 {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		
		try{
			stmt = conn.createStatement();
			
			sql = "SELECT name, birth, kor, mat, eng, kor+mat+eng tot FROM score WHERE hak='1111'"; 
			//데이터가 있으면 true이고, 없으면 false이다. 
			//false 면 , if나 while문장에서 빠져나온다. 
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) { // if는 하나만 가져오는 것, while은 전체다 가져오는것.
				//String name = rs.getString("name");
				String name = rs.getString(1); //오라클 인덱스는 1부터 시작
				String birth = rs.getString("birth"); //String birth = rs.getString(2);
				int kor = rs.getInt("kor");
				int mat = rs.getInt("mat");
				int eng = rs.getInt("eng");
				int tot = rs.getInt("tot");
				
				System.out.println(name+","+birth+","+kor+","+mat+","+eng+","+tot);
				//홍길동,2000-10-10 00:00:00,80,90,70,240 -- 자바는 생일을 가져올때 "년월일 시분초"로 통째로 가져온다. 
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
					
			} catch (Exception e2) {
				
			}
			
			try {
				if(stmt!=null) {
					stmt.close();
				}
					
			} catch (Exception e2) {
				
			}
		}
	}

}
