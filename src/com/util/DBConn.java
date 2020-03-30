package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
	//내일 시험 테스트
	private static Connection conn;
	
	//생성자가 private이니까 외부접근 불가능 
	private DBConn() {
		
		
	}
	
	public static Connection getConnection() {
		//수업에서는 오라클이 쌍용학원 컴퓨터에 깔려있기 때문에, 127.0.0.1로 설정.
		//String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe"; //11g 
		
		String url = "jdbc:oracle:thin:@211.238.142.54:1521:xe"; //11g
		//String url = "jdbc:oracle:thin:@//211.238.142.54:1521/xe"; //12c
		
		String user = "sky";
		String pwd = "java$!";
		
		if(conn==null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
					//JDK 6 부터는 생략 가능. 자동 로딩 
				conn = DriverManager.getConnection(url,user,pwd);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static void close() {
		if(conn!=null) {
			try {
				if(! conn.isClosed()) {
					conn.close();
				}
			}catch(SQLException e) {
				
			}
			
		}
		conn = null;
	}
}
