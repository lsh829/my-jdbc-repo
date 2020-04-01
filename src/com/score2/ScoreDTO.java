package com.score2;

/*- DTO (Data Transfer Object) : 데이터 전송 객체 
	- VO (Value Object) 와 유사 
	
	- DTO 한 사람의 정보를 담고 있는 곳이라고 말할 수 있다. 
	- DTO는 Getter, Setter 만 만들면 된다. 
	- 실무 나가면 VO로 만드는 사람도 있다. 

- DAO (Data Access Object) : 데이터 처리를 주 목적으로하는 클래스 
- CRUD : Create(Insert) , Retrieve (Select) , Update, Delete
	- DAO 진짜 DB를 작업하는 클래스 
	- CRUD 작업을 하는 곳이 된다고 말할 수 있다 . 
	
	* 클래스는 오직 하나의 기능이 들어가도록 만드는 것 권장. 
*/

public class ScoreDTO {
	private String hak;
	private String name;
	private String birth;
	private int kor;
	private int eng;
	private int mat;
	private int tot; 
	private int ave;
	private int rank;
	
	public String getHak() {
		return hak;
	}
	public void setHak(String hak) {
		this.hak = hak;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public int getKor() {
		return kor;
	}
	public void setKor(int kor) {
		this.kor = kor;
	}
	public int getEng() {
		return eng;
	}
	public void setEng(int eng) {
		this.eng = eng;
	}
	public int getMat() {
		return mat;
	}
	public void setMat(int mat) {
		this.mat = mat;
	}
	public int getTot() {
		return tot;
	}
	public void setTot(int tot) {
		this.tot = tot;
	}
	public int getAve() {
		return ave;
	}
	public void setAve(int ave) {
		this.ave = ave;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

}
