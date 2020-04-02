package test0402;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.util.DBConn;

public class Test1Transaction {

	public static void main(String[] args) {
		// 트랜잭션 예제 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		
		String sql; 
		String id, name, birth, tel;
		
		try {
			System.out.print("아이디?");
			id = br.readLine();
			
			System.out.print("이름?");
			name = br.readLine();
			
			// 생년월일이나 전화번호에다가 입력안하고 ENTER 치면 
			// null 값이 아니라 " " 들어간다.
			System.out.print("생년월일?");
			birth = br.readLine();
		
			System.out.print("전화번호?");
			tel = br.readLine();
			
			// 어느 테이블에는 데이터가 들어가고, 안들어가는 상황을 예외 처리 해준다. -- 트랜잭션 처리 
			// 자동 COMMIT 되지 않도록 설정(기본 : 자동 COMMIT)
			conn.setAutoCommit(false);
			
			
			sql = "INSERT INTO test1(id, name) VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			pstmt.close();
			
			
			sql = "INSERT INTO test2(id, birth) VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, birth);
			pstmt.executeUpdate();
			pstmt.close();
			
			
			sql = "INSERT INTO test3(id, tel) VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, tel);
			pstmt.executeUpdate();
			
			//COMMIT
			conn.commit();

			System.out.println("추가 성공...");
			
			
		} catch (SQLException e) {
			//ROLLBACK 
			try {
				// 실패를 했으면 ROLLBACK; 해줘야 한다. 
				conn.rollback();
			} catch (Exception e2) {
				
			}
			
			//e.printStackTrace();
			System.out.println(e.getMessage()); //메세지 제일 간단
			//System.out.println("추가 실패...");
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				conn.setAutoCommit(true); //다시 정상을 만들어줘야 한다. 
			} catch (Exception e2) {
				
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			DBConn.close();
		}

	}

}

//트랜잭션 처리를 해주면 test1에도 팟콩이의 데이터가 안들어감을 볼 수 있다. 
// INSERT, UPDATE, DELETE 하고만 연관이 있다. 