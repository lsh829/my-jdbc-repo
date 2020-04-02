package com.score3;

import java.util.List;
import java.util.Map;

// 6가지 기능을 담고 있는 interface
// 인터페이스 ? 뭘 만들건지 명시해주는 목적
public interface ScoreDAO {
	public int insertScore(ScoreDTO dto);
	public int updateScore(ScoreDTO dto);
	public int deleteScore(String hak);
	
	public ScoreDTO readScore(String hak); //학번검색 , 한 사람의 정보만 가져오기 때문에 ScoreDTO가 반환값
	public List<ScoreDTO> listScore(String name); // 여러 사람의 정보를 가져오기 때문에 List<> 가 반환값
	public List<ScoreDTO> listScore();
	
	//과목별 평균 리스트 
	public Map<String, Integer> averageScore(); //왜 맵에다가 리턴?
}
