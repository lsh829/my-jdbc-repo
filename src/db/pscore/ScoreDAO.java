package db.pscore;

import java.util.List;

public interface ScoreDAO {
	public int insertScore(ScoreDTO dto);
	public int updateScore(ScoreDTO dto);
	public int deleteScore(String hak);
	
	public ScoreDTO readScore(String hak);
	public List<ScoreDTO> listScore(String name);
	public List<ScoreDTO> listScore();
}
