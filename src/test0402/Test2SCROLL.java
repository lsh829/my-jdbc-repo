package test0402;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.util.DBConn;

public class Test2SCROLL {
	//스크롤 예제 
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql;

		Scanner sc = new Scanner(System.in);
		int ch;

		try {
			sql = "SELECT hak, name, birth, kor, eng, mat FROM score";

			// 순방향만
			// stmt = conn.prepareStatement(sql);
			
			// 순/역방향 가능 . 변경결과 바로 반영. 데이터 수정 불가 
			stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY
					);
			
			
			/* 결과 집합 유형 
			
			*	TYPE_FORWARD_ONLY : 기본, 순방향만 
			*	TYPE_SCROLL_SENSITIVE : 순/역방향 가능. 변경내용 바로 반영 
			*	TYPE_SCROLL_INSENSITIVE : 순/역방향 가능. 변경내용 바로 반영 x
			*/
			
			
			/* 동시성 유형 
			
			*	CONCUR_READ_ONLY : 읽기만 가능  
			*	CONCUR_UPDATABLE : 수정 가능
			*/
			
			rs = stmt.executeQuery(sql);
			

			while (true) {
				do {
					System.out.print("1.처음 2.이전 3.다음 4.끝(마지막) 5.종료 =>");
					ch = sc.nextInt();
				} while (ch < 1 || ch > 5);
				
				if(ch==5) {
					break;
				}
				
				switch (ch) {
				
				case 1:
					if (rs.first()) {
						System.out.println("처음 :" + rs.getString(1)+", "+rs.getString(2));
					}
					break;
					
				case 2:
					if (rs.previous()) {
						System.out.println("이전 :" + rs.getString(1)+", "+rs.getString(2));
					}
					break;
				
				case 3:
					if (rs.next()) {
						System.out.println("다음 :" + rs.getString(1)+", "+rs.getString(2));
					}
					break;
					
				case 4:
					if (rs.last()) {
						System.out.println("끝 :" + rs.getString(1)+", "+rs.getString(2));
					}
					break;
				
				}//switch_end				
				
			}//while_end


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			sc.close();
			
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					
				}
			}
		}//finally_end
	}

}
