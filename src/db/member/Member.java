package db.member;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Member {
	private Scanner sc=new Scanner(System.in);
	private MemberDAO dao=new MemberDAOImpl(); 
	
	public void insertMember() {
		System.out.println("\n회원 등록...");
		
		try {
			MemberDTO dto=new MemberDTO();
			
			System.out.print("아이디 ?");
			dto.setId(sc.next());
			
			System.out.print("패스워드 ?");
			dto.setPwd(sc.next());

			System.out.print("이름 ?");
			dto.setName(sc.next());

			System.out.print("생년월일 ?");
			dto.setBirth(sc.next());
			
			System.out.print("이메일 ?");
			dto.setEmail(sc.next());
			
			System.out.print("전화번호 ?");
			dto.setTel(sc.next());
			
			int result = dao.insertMember(dto);
			if(result>=1)
				System.out.println("회원 등록 성공...");
			else
				System.out.println("회원 등록 실패...");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println();
	}

	public void updateMember() {
		System.out.println("\n회원 정보 수정...");
		
		try {
			String id;
			System.out.print("수정할 아이디 ? ");
			id=sc.next();
			
			MemberDTO dto=dao.readMember(id);
			if(dto==null) {
				System.out.println("등록된 아이디가 아닙니다.");
				return;
			}
			
			System.out.println("\n등록정보...");
			System.out.println("이름 : "+dto.getName());
			System.out.println("패스워드 : "+dto.getPwd());
			System.out.println("생년월일 : "+dto.getBirth());
			System.out.println("이메일 : "+dto.getEmail());
			System.out.println("전화번호 : "+dto.getTel());
			
			System.out.print("패스워드 ?");
			dto.setPwd(sc.next());

			System.out.print("생년월일 ?");
			dto.setBirth(sc.next());
			
			System.out.print("이메일 ?");
			dto.setEmail(sc.next());
			
			System.out.print("전화번호 ?");
			dto.setTel(sc.next());
			
			int result = dao.updateMember(dto);
			
			if(result>=1)
				System.out.println("회원정보 수정 성공...");
			else
				System.out.println("회원정보 수정 실패...");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println();
	}

	public void deleteMember() {
		System.out.println("\n회원 정보 삭제...");
		
		String id;
		System.out.print("삭제할 아이디 ? ");
		id=sc.next();
		
		int result = dao.deleteMember(id);
		
		if(result>=1)
			System.out.println("회원정보 삭제 성공...");
		else
			System.out.println("회원정보 삭제 실패...");
		
		System.out.println();
	}

	public void findById() {
		System.out.println("\n아이디 검색...");
		
		String id;
		System.out.print("검색할 아이디 ? ");
		id=sc.next();
		
		MemberDTO dto=dao.readMember(id);
		if(dto==null) {
			System.out.println("등록된 정보가 아닙니다.");
			return;
		}
		
		System.out.print(dto.getId()+"\t");
		System.out.print(dto.getName()+"\t");
		System.out.print(dto.getPwd()+"\t");
		System.out.print(dto.getBirth()+"\t");
		System.out.print(dto.getEmail()+"\t");
		System.out.print(dto.getTel()+"\n");
		
		System.out.println();
	}

	public void findByName() {
		System.out.println("\n이름 검색...");
		
		String name;
		System.out.print("검색할 이름 ? ");
		name=sc.next();
		
		List<MemberDTO> list=dao.listMember(name);
		for(MemberDTO dto : list) {
			System.out.print(dto.getId()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPwd()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getEmail()+"\t");
			System.out.print(dto.getTel()+"\n");
		}
		
		System.out.println();
	}

	public void listMember() {
		System.out.println("\n전체 리스트...");
	
		List<MemberDTO> list=dao.listMember();
		Iterator<MemberDTO> it=list.iterator();
		while(it.hasNext()) {
			MemberDTO dto=it.next();
			System.out.print(dto.getId()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPwd()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getEmail()+"\t");
			System.out.print(dto.getTel()+"\n");
		}
		
		System.out.println();		
	}
}
