package db.employee2;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Employee {
	private Scanner sc=new Scanner(System.in);
	private EmployeeDAO dao=new EmployeeDAO();
	
	public void employeeManage() {
		int ch;
		while(true) {
			System.out.println("\n[사원관리]");
			do {
				System.out.print("1.사원등록 2.정보수정 3.사번검색 4.이름검색 5.리스트 6.메인 => ");
				ch = sc.nextInt();
			}while(ch<1||ch>6);
			
			if(ch==6) return;
			
			switch(ch) {
			case 1: insert(); break;
			case 2: update(); break;
			case 3: searchSabeon(); break;
			case 4: searchName(); break;
			case 5: list(); break;
			}
		}
	}
	
	public void insert() {
		System.out.println("\n사원 등록...");
		
		EmployeeDTO dto=new EmployeeDTO();
		
		System.out.print("사원번호 ? ");
		dto.setSabeon(sc.next());
		
		System.out.print("이름 ? ");
		dto.setName(sc.next());
		
		System.out.print("생년월일[yyyymmdd] ? ");
		dto.setBirth(sc.next());
		
		System.out.print("전화번호 ? ");
		dto.setTel(sc.next());
		
		int result=dao.insertEmployee(dto);
		if(result>=1) {
			System.out.println("사원등록 완료 !!!");
		} else {
			System.out.println("사원등록 실패 !!!");
		}
	}
	
	public void update() {
		System.out.println("\n사원 정보 수정...");
		
		String sabeon;
		System.out.print("수정할사번 ? ");
		sabeon=sc.next();
		
		EmployeeDTO dto=dao.readEmployee(sabeon);
		if(dto==null) {
			System.out.println("등록된 사원이 아닙니다. !!!");
			return;
		}
		
		System.out.println("등록정보 -> ");
		System.out.print(dto.getName()+"\t");
		System.out.print(dto.getBirth()+"\t");
		System.out.print(dto.getTel()+"\n");
		
		System.out.print("수정할 이름 ? ");
		dto.setName(sc.next());
		
		System.out.print("생년월일[yyyymmdd] ? ");
		dto.setBirth(sc.next());
		
		System.out.print("전화번호 ? ");
		dto.setTel(sc.next());
		
		dao.updateEmployee(dto);
		System.out.println("수정 완료 !!!");
	}
	
	public void searchSabeon() {
		System.out.println("\n사번 검색...");
		
		String sabeon;
		System.out.print("검색할사번 ? ");
		sabeon=sc.next();
		
		EmployeeDTO dto=dao.readEmployee(sabeon);
		if(dto==null) {
			System.out.println("등록된 사원이 아닙니다. !!!");
			return;
		}
		
		System.out.print(dto.getSabeon()+"\t");
		System.out.print(dto.getName()+"\t");
		System.out.print(dto.getBirth()+"\t");
		System.out.print(dto.getTel()+"\n");
	}

	public void searchName() {
		System.out.println("\n이름 검색...");
		
		String name;
		System.out.print("검색할이름 ? ");
		name=sc.next();
		
		List<EmployeeDTO> list=dao.listEmployee(name);
		
		Iterator<EmployeeDTO> it=list.iterator();
		while(it.hasNext()) {
			EmployeeDTO dto=it.next();
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getTel()+"\n");
		}
		
	}
	
	public void list() {
		System.out.println("\n사원 리스트...");
		List<EmployeeDTO> list=dao.listEmployee();
		
		Iterator<EmployeeDTO> it=list.iterator();
		while(it.hasNext()) {
			EmployeeDTO dto=it.next();
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getTel()+"\n");
		}
	}
	
}
