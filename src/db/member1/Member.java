package db.member1;

import java.util.List;
import java.util.Scanner;

public class Member {
	private Scanner sc = new Scanner(System.in);
	private MemberDAO dao = new MemberDAOImpl(); 
	
	public void insertMember() {
		System.out.println("\n회원 추가...");
		
		try {
			MemberDTO dto = new MemberDTO();
			
			System.out.print("아이디?");
			dto.setId(sc.next());
			
			System.out.print("비밀번호?");
			dto.setPwd(sc.next());
			
			System.out.print("이름 ?");
			dto.setName(sc.next());

			System.out.print("생일?");
			dto.setBirth(sc.next());
			
			System.out.print("이메일?");
			dto.setEmail(sc.next());

			System.out.print("전화번호?");
			dto.setTel(sc.next());
			
			int result = dao.insertMember(dto);
			//System.out.println(result+"행이 추가 되었습니다. \n");
			
			if(result>=1)
				System.out.println("회원 등록 성공...");
			else
				System.out.println("회원 등록 실패...");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//checking point 
	public void updateMember() {
		System.out.println("\n 회원 정보 수정....");
		MemberDTO dto = null;
		
		System.out.print("수정할 아이디?");
		String id = sc.next();
		dto = dao.readMember(id);
		
		if(dto==null) {
			System.out.println("아이디가 존재하지 않습니다.\n");
			return;
		}
		
		System.out.print(dto.getId()+"\t");
		System.out.print(dto.getName()+"\t");
		System.out.print(dto.getPwd()+"\t");
		System.out.print(dto.getBirth()+"\t");
		System.out.print(dto.getEmail()+"\t");
		System.out.print(dto.getTel()+"\n");
		
		try {
			
			System.out.print("수정할 비밀번호?");
			dto.setPwd(sc.next());
			
			System.out.print("수정할 생년월일?");
			dto.setBirth(sc.next());
			
			System.out.print("수정할 이메일?");
			dto.setEmail(sc.next());
			
			System.out.print("수정할 전화번호?");
			dto.setTel(sc.next());
			
			int result = dao.updateMember(dto);
			System.out.println(result+"행이 수정 되었습니다. \n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//checking point 
	public void deleteMember() {
		System.out.println("\n회원 정보 삭제...");
		String val;
		
		try {
			System.out.print("삭제할 아이디?");
			val = sc.next();
			
			int result = dao.deleteMember(val);
			System.out.println(result+"행이 삭제 되었습니다. \n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void listMember() {
		System.out.println("\n전체 리스트...");
		
		List<MemberDTO> list = dao.listMember();
		//Iterator을 사용하여 출력할 수 있다.. 
		for(MemberDTO dto : list) {
			System.out.print(dto.getId()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPwd()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getEmail()+"\t");
			System.out.print(dto.getTel()+"\n");
			
			// member1 에 있는 한명의 정보는 출력이 안되는 문제점이 있다. 
		}
		
	}
	
	//checking point 
	public void findById() {
		System.out.println("\n 아이디 검색...");
		String id;
		
		try {
			System.out.print("검색할 아이디?");
			id = sc.next();
			
			MemberDTO dto = dao.readMember(id);
			
			if(dto==null) {
				System.out.println("아이디가 존재하지 않습니다.\n");
				return;
			}
			
			System.out.print(dto.getId()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPwd()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getEmail()+"\t");
			System.out.print(dto.getTel()+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//checking point 
	public void findByName() {
		System.out.println("\n 이름 검색...");
		String name;
		
		try {
			System.out.print("검색할 이름?");
			name = sc.next();
			List<MemberDTO> list = dao.listMember(name);
			
			for(MemberDTO dto : list) {
				System.out.print(dto.getId()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getPwd()+"\t");
				System.out.print(dto.getBirth()+"\t");
				System.out.print(dto.getEmail()+"\t");
				System.out.print(dto.getTel()+"\n");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
