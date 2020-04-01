package db.employee;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Salary {
	private Scanner sc = new Scanner(System.in);
	private SalaryDAO dao = new SalaryDAO();
	private Employee emp = null;

	public Salary(Employee emp) {
		this.emp = emp;
	}

	public void salaryManage() {
		int ch;
		while (true) {
			System.out.println("\n[급여관리]");
			do {
				System.out.print("1.지급 2.수정 3.삭제 4.월별리스트 5.사번검색 6.리스트 7.사원리스트 8.전체현황출력 9.메인 => ");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 9);

			if (ch == 9)
				return;

			switch (ch) {
			case 1:
				payment();
				break;
			case 2:
				update();
				break;
			case 3:
				delete();
				break;
			case 4:
				monthList();
				break;
			case 5:
				searchSabeon();
				break;
			case 6:
				list();
				break;
			case 7:
				emp.list();
				break; // 어떻게 연결되었는지 -- checking point
			case 8:
				analysis();
				break;
			}
		}
	}

	public void payment() {
		System.out.println("\n급여 지급...");

		try {
			SalaryDTO dto = new SalaryDTO();

			System.out.print("사원번호?");
			dto.setSabeon(sc.next());

			System.out.print("급여년월(YYYYMM)?");
			dto.setPayDate(sc.next());

			System.out.print("급여일자(YYYYMMDD)?");
			dto.setPaymentDate(sc.next());

			System.out.print("기본급?");
			dto.setPay(sc.nextInt());

			System.out.print("수당?");
			dto.setSudang(sc.nextInt());

			int tax = 0;
			int total = dto.getPay() + dto.getSudang();
			// System.out.println(total);

			if (total >= 3000000) {
				tax = (int) (total * 0.03);
			} else if (total >= 2000000) {
				tax = (int) (total * 0.02);
			} else {
				tax = 0;
			}
			// System.out.println(tax);
			dto.setTax(tax);

			System.out.print("메모?");
			dto.setMemo(sc.next());

			int result = dao.insertSalary(dto);
			if (result >= 1) {
				System.out.println("지급 완료!");
			} else {
				System.out.println("지급 실패...");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println();

	}

	public void update() {
		System.out.println("\n급여 수정...");

		try {
			int salaryNum;
			System.out.println("수정할 급여번호?");
			salaryNum = sc.nextInt();

			SalaryDTO dto = dao.readSalary(salaryNum);

			if (dto == null) {
				System.out.println("등록된 급여번호가 아닙니다.");
				return;
			}

			System.out.println("\n등록정보...");
			System.out.println("급여번호: " + dto.getSalaryNum());
			System.out.println("사번: " + dto.getSabeon());
			System.out.println("이름 : " + dto.getName()); // checking
			System.out.println("급여년월 : " + dto.getPayDate());
			System.out.println("급여지급년월일 : " + dto.getPaymentDate());
			System.out.println("기본급 : " + dto.getPay());
			System.out.println("수당 : " + dto.getSudang());
			System.out.println("세금 : " + dto.getTax());
			System.out.println("메모: " + dto.getMemo());

			System.out.print("급여년월(YYYYMM)?");
			dto.setPayDate(sc.next());

			System.out.print("급여지급일자(YYYYMMDD)?");
			dto.setPaymentDate(sc.next());

			System.out.print("기본급?");
			dto.setPay(sc.nextInt());

			System.out.print("수당?");
			dto.setSudang(sc.nextInt());

			int tax = 0;
			int total = dto.getTot();
			// System.out.println(total);

			if (total >= 3000000) {
				tax = (int) (total * 0.03);
			} else if (total >= 2000000) {
				tax = (int) (total * 0.02);
			} else {
				tax = 0;
			}
			// System.out.println(tax);
			dto.setTax(tax);

			System.out.print("메모?");
			dto.setMemo(sc.next());
			
			
			
			int result = dao.updateSalary(dto);

			if (result >= 1)
				System.out.println("급여정보 수정 성공...");
			else
				System.out.println("급여정보 수정 실패...");

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println();

	}

	public void delete() {
		System.out.println("\n급여 삭제...");
		
		int salaryNum;
		System.out.print("삭제할 급여번호?");
		salaryNum = sc.nextInt();
		
		int result = dao.deleteSalary(salaryNum);
		
		if(result>=1) {
			System.out.println("급여 정보 삭제 성공");
		}else {
			System.out.println("급여 정보 삭제 실패..");
		}
		System.out.println();
	
	}
	
	//*****너무 어려워 *****
	public void searchSabeon() {
		System.out.println("\n사번 검색..."); //MAP
		//급여년월, 사원번호 입력
		String payDate;
		System.out.println("검색할 급여년월(YYYYMM)?");
		payDate = sc.next();
		
		String sabeon;
		System.out.println("검색할 사원번호?");
		sabeon = sc.next();
		
		Map<String, Object> map = new HashMap<String, Object>(); //List(인터페이스)-ArrayList(클래스)
		
		map.put("payDate", payDate);
		map.put("sabeon", sabeon);
		
		List<SalaryDTO> list = dao.listSalary(map);
		
		//출력

		for(SalaryDTO dto : list) {
			System.out.print(dto.getSalaryNum()+"\t");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\n");
			
		}
		System.out.println();
	}

	public void monthList() {
		System.out.println("\n월별 리스트...");
		
		String payDate;
		System.out.println("검색할 급여년월(YYYYMM)?");
		payDate = sc.next();
		
		List<SalaryDTO> list = dao.listSalary(payDate);
		
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
		System.out.println();
	}

	public void list() {
		System.out.println("\n급여 리스트...");

		List<SalaryDTO> list = dao.listSalary();
		Iterator<SalaryDTO> it = list.iterator();

		while (it.hasNext()) {
			SalaryDTO dto = it.next();
			System.out.print(dto.getSalaryNum() + "\t");
			System.out.print(dto.getSabeon() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getPayDate() + "\t");
			System.out.print(dto.getPaymentDate() + "\t");
			System.out.print(dto.getPay() + "\t");
			System.out.print(dto.getSudang() + "\t");
			System.out.println(dto.getTot() + "\n"); // 총급여(기본급+수당)
		}
		System.out.println();
	}
	
	
	public void analysis() {
		System.out.println("\n전체 현황 파악 리스트...");
		
		String payYear;
		System.out.println("검색할 년도(YYYY)?");
		payYear = sc.next();
		
		List<GroupDTO> list = dao.AnalysisSalary(payYear);
		
		for(GroupDTO dto : list) {
			System.out.print(dto.getYear()+"\t");
			System.out.print(dto.getMonth()+"\t");
			System.out.print(dto.getSum_pay()+"\t");
			System.out.print(dto.getAvg_pay()+"\t");
			System.out.print(dto.getMax_pay()+"\n");
		}
		System.out.println();
		
	
	}
}
