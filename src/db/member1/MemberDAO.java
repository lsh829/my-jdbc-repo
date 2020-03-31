package db.member1;

import java.util.List;

public interface MemberDAO {
	public int insertMember(MemberDTO dto);
	public int updateMember(MemberDTO dto);
	public int deleteMember(String id);
	
	public MemberDTO readMember(String id);
	public List<MemberDTO> listMember();
	public List<MemberDTO> listMember(String val);
}
