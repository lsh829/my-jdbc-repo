package com.score3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

//현재는 콘솔용이지만, 
//추후에는 웹용, 모바일용 으로 작성 가능하다.

public class Score {
	private BufferedReader br = new BufferedReader (new InputStreamReader(System.in)); 
	//Scanner 말고  BufferedReader 사용
	private ScoreDAO dao = new ScoreDAOImpl(); //checking point 
	
	//1)
	public void insert() {
		System.out.println("\n데이터 추가...");
		ScoreDTO dto = new ScoreDTO();
		try {
			System.out.print("학번?");
			dto.setHak(br.readLine());
			
			System.out.print("이름?");
			dto.setName(br.readLine());
			
			System.out.print("생년월일?");
			dto.setBirth(br.readLine());
			
			System.out.print("국어?");
			dto.setKor(Integer.parseInt(br.readLine()));
			
			System.out.print("영어?");
			dto.setEng(Integer.parseInt(br.readLine()));
			
			System.out.print("수학?");
			dto.setMat(Integer.parseInt(br.readLine()));
			
			int result = dao.insertScore(dto);
			
			if(result!=0) {
				System.out.println("자료를 추가했습니다.");
			}else {
				System.out.println("자료추가가 실패했습니다.");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//4)
	public void update() {
		System.out.println("\n데이터 수정...");
		ScoreDTO dto = new ScoreDTO();
		try {
			System.out.print("수정할 학번?");
			dto.setHak(br.readLine());
			
			System.out.print("수정할 이름?");
			dto.setName(br.readLine());
			
			System.out.print("수정할 생년월일?");
			dto.setBirth(br.readLine());
			
			System.out.print("수정할 국어?");
			dto.setKor(Integer.parseInt(br.readLine()));
			
			System.out.print("수정할 영어?");
			dto.setEng(Integer.parseInt(br.readLine()));
			
			System.out.print("수정할 수학?");
			dto.setMat(Integer.parseInt(br.readLine()));
			
			int result = dao.updateScore(dto);
			
			if(result!=0) {
				System.out.println("자료를 수정했습니다.");
			}else {
				System.out.println("자료수정이 실패했습니다.");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//3)
	public void delete() {
		System.out.println("\n데이터 삭제...");
		String hak;
		
		
		try {
			System.out.print("삭제할 학번?");
			hak = br.readLine();
			
			int result = dao.deleteScore(hak); //실수 포인트 
			
			if(result!=0) {
				System.out.println("자료를 삭제했습니다.");
			}else {
				System.out.println("자료삭제가 실패했습니다.");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//6)
	public void findByHak() {
		System.out.println("\n학번 검색...");
		String hak;
		
		
		try {
			System.out.print("검색할 학번?");
			hak = br.readLine();
			
			ScoreDTO dto = dao.readScore(hak);
			
			if(dto==null) {
				System.out.println("등록된 자료가 없습니다.\n");
				return;
			}
			
			System.out.print(dto.getHak()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getKor()+"\t");
			System.out.print(dto.getEng()+"\t");
			System.out.print(dto.getMat()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getAve()+"\n");
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//5) 
	public void findByName() {
		System.out.println("\n이름 검색...");
		String name;
		
		
		try {
			System.out.print("검색할 이름?");
			name = br.readLine();
			List<ScoreDTO> list = dao.listScore(name);
			
			for(ScoreDTO dto : list) {
				System.out.print(dto.getHak()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getBirth()+"\t");
				System.out.print(dto.getKor()+"\t");
				System.out.print(dto.getEng()+"\t");
				System.out.print(dto.getMat()+"\t");
				System.out.print(dto.getTot()+"\t");
				System.out.print(dto.getAve()+"\t");
				System.out.print(dto.getRank()+"\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//2)
	public void listAll() {
		System.out.println("\n전체 리스트...");
		
		List<ScoreDTO> list = dao.listScore();
		// 데이터 뽑아내는 것
		// 1) literator 사용 
		// 2) toString() 재정의 
		
		for(ScoreDTO dto : list) {
			System.out.print(dto.getHak()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.print(dto.getKor()+"\t");
			System.out.print(dto.getEng()+"\t");
			System.out.print(dto.getMat()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getAve()+"\t");
			System.out.print(dto.getRank()+"\n");
		}
	}
	
	//(추가) 과목별 평균 리스트 출력 
	public void average() {
		System.out.println("\n과목별 평균  리스트...");
		
		
		//checking point 
		Map<String, Integer> map = dao.averageScore();
		int kor = map.get("kor");
		int eng = map.get("eng");
		int mat = map.get("mat");
		
		System.out.println("국어:"+kor);
		System.out.println("영어:"+eng);
		System.out.println("수학:"+mat);
		
		
		System.out.println();
	}
	
	
}
