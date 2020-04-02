package db.member3;

import java.util.Scanner;

public class MemberApp {
	public static void main(String[] args) {
		Member mm=new Member();
		Scanner sc=new Scanner(System.in);
		int ch;

		System.out.println("CallableStatement를 이용한 회원관리 프로그램");
		
		try {
			while(true) {
				do {
					System.out.print("1.등록 2.수정 3.삭제 4.아이디검색 5.이름검색 6.리스트 7.종료 =>");
					ch=sc.nextInt();
				} while(ch<1 || ch>7);
				
				if(ch==7) {
					break;
				}
				
				switch(ch) {
				case 1:mm.insertMember(); break;
				case 2:mm.updateMember(); break;
				case 3:mm.deleteMember(); break;
				case 4:mm.findById(); break;
				case 5:mm.findByName(); break;
				case 6:mm.listMember(); break;
				}
			}
		}finally {
			sc.close();
		}
	}
}
