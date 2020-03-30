package test0330;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.util.DBConn;

public class Test4 {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null; //커서 개념과 유사, 담아놓는 개념 
							 //커서의 FETCH 를 이용해서 한줄씩 가져왔다. 
		String sql;
		
		try{
			stmt = conn.createStatement();
			
			sql = "SELECT name, birth, kor, mat, eng, kor+mat+eng tot, hak FROM score";
			
			rs = stmt.executeQuery(sql); //select 실행 메소드는 executeQuery 이다. 
			
			// while은 전체 다 가져오는것.
			while(rs.next()) {  //데이터가 한 개만 존재하더라고 next() 는 필수 이다.
				String name = rs.getString("name");
				
				
				//String birth = rs.getString("birth"); //String birth = rs.getString(2);
				//1111,홍길동,2000-10-10 00:00:00,80,90,70,240-- 자바는 생일을 가져올때 "년월일 시분초"로 통째로 가져온다. 
				//2222,심심해,1999-01-10 00:00:00,80,90,70,240
				
				
				String birth = rs.getDate("birth").toString();
				//1111,홍길동,2000-10-10,80,90,70,240 - 시분초가 안나온다. 
				//2222,심심해,1999-01-10,80,90,70,240

				int kor = rs.getInt("kor");
				int mat = rs.getInt("mat");
				int eng = rs.getInt("eng");
				int tot = rs.getInt("tot");
				String hak = rs.getString("hak");
				
				System.out.println(hak+","+name+","+birth+","+kor+","+mat+","+eng+","+tot);
				
				
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
