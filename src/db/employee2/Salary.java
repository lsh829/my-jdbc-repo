package db.employee2;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Salary {
	private Scanner sc=new Scanner(System.in);
	private SalaryDAO dao=new SalaryDAO();
	private Employee emp=null;
	
	public Salary(Employee emp) {
		this.emp = emp;
	}
	
	public void salaryManage() {
		int ch;
		while(true) {
			System.out.println("\n[급여관리]");
			do {
				System.out.print("1.지급 2.수정 3.삭제 4.월별리스트 5.사번검색 6.리스트 7.사원리스트 8.메인 => ");
				ch = sc.nextInt();
			}while(ch<1||ch>8);
			
			if(ch==8) return;
			
			switch(ch) {
			case 1:payment(); break;
			case 2:update(); break;
			case 3:delete(); break;
			case 4:monthList(); break;
			case 5:searchSabeon(); break;
			case 6:list(); break;
			case 7:emp.list(); break;
			}
		}
	}
	
	public void payment() {
		System.out.println("\n급여 지급...");
		
		try {
			SalaryDTO dto=new SalaryDTO();
			
			System.out.print("사원번호 ?");
			dto.setSabeon(sc.next());
			
			System.out.print("급여년월[yyyymm]?");
			dto.setPayDate(sc.next());
			
			System.out.print("급여지급일자[yyyymmdd]?");
			dto.setPaymentDate(sc.next());
			
			System.out.print("기본급?");
			dto.setPay(sc.nextInt());
			
			System.out.print("수당?");
			dto.setSudang(sc.nextInt());

			System.out.print("메모?");
			dto.setMemo(sc.next());

			int tot=dto.getPay()+dto.getSudang();
			int tax=0;
			
			if(tot>=3000000)
				tax=(int)(tot*0.03);
			else if(tot>=2000000)
				tax=(int)(tot*0.02);
			else
				tax=0;
			
			dto.setTax(tax);
			
			int result=dao.insertSalary(dto);
			
			if(result>=1) {
				System.out.println("급여 지급 완료 !!!");
			} else {
				System.out.println("급여 지급 실패 !!!");
			}
			
		}catch (InputMismatchException e) {
			System.out.println("입력 형식이 일치하지 않습니다. !!!");
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void update() {
		System.out.println("\n급여 수정...");
		
		try {
			int salaryNum;
			System.out.print("수정할 급여 번호 ?");
			salaryNum=sc.nextInt();
			
			SalaryDTO dto=dao.readSalary(salaryNum);
			if(dto==null) {
				System.out.println("등록된 정보가 아닙니다. !!!");
				return;
			}
			
			System.out.println("등록정보 -> ");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.print(dto.getAfterPay()+"\n");
			
			System.out.print("수정할 사원번호 ?");
			dto.setSabeon(sc.next());
			
			System.out.print("급여년월[yyyymm]?");
			dto.setPayDate(sc.next());
			
			System.out.print("급여지급일자[yyyymmdd]?");
			dto.setPaymentDate(sc.next());
			
			System.out.print("기본급?");
			dto.setPay(sc.nextInt());
			
			System.out.print("수당?");
			dto.setSudang(sc.nextInt());
			
			System.out.print("메모?");
			dto.setMemo(sc.next());
			
			int tot=dto.getPay()+dto.getSudang();
			int tax=0;
			
			if(tot>=3000000)
				tax=(int)(tot*0.03);
			else if(tot>=2000000)
				tax=(int)(tot*0.02);
			else
				tax=0;
			
			dto.setTax(tax);
			
			int result=dao.updateSalary(dto);
			
			if(result>=1) {
				System.out.println("급여 수정 완료 !!!");
			} else {
				System.out.println("급여 수정 실패 !!!");
			}
			
		}catch (InputMismatchException e) {
			System.out.println("입력 형식이 일치하지 않습니다. !!!");
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void delete() {
		System.out.println("\n급여 삭제...");
		
		int salaryNum;
		System.out.print("삭제할 급여 번호 ?");
		salaryNum=sc.nextInt();
		
		SalaryDTO dto=dao.readSalary(salaryNum);
		if(dto==null) {
			System.out.println("등록된 정보가 아닙니다. !!!");
			return;
		}
		
		dao.deleteSalary(salaryNum);
	}

	public void searchSabeon() {
		System.out.println("\n사번 검색...");
		
		String payDate, sabeon;
		System.out.print("검색할 급여년월[yyyymm] ?");
		payDate=sc.next();
		System.out.print("검색할 사번 ?");
		sabeon=sc.next();
		Map<String, Object> map=new HashMap<>();
		map.put("payDate", payDate);
		map.put("sabeon", sabeon);
		
		List<SalaryDTO> list=dao.listSalary(map);
		
		for(SalaryDTO dto : list) {
			System.out.print(dto.getSalaryNum()+"\t");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.print(dto.getAfterPay()+"\n");
		}
		
	}

	public void monthList() {
		System.out.println("\n월별 리스트...");
		
		String payDate;
		System.out.print("검색할 급여년월[yyyymm] ?");
		payDate=sc.next();
		List<SalaryDTO> list=dao.listSalary(payDate);
		
		for(SalaryDTO dto : list) {
			System.out.print(dto.getSalaryNum()+"\t");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.print(dto.getAfterPay()+"\n");
		}
	}
	
	public void list() {
		System.out.println("\n급여 리스트...");
	
		List<SalaryDTO> list=dao.listSalary();
		
		for(SalaryDTO dto : list) {
			System.out.print(dto.getSalaryNum()+"\t");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.print(dto.getAfterPay()+"\n");
		}		
	}
	
}
