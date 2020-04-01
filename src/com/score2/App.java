package com.score2;

import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int ch;
		Score ss = new Score();
		
		System.out.println("PreparedStatement 예제....");
		
		
		try {
			while(true) {
				do {
					System.out.println(" ** 1.추가 2.수정 3.삭제  4.학번검색 5.이름검색 6.리스트 7.종료  ** ");
					ch = sc.nextInt();
				}while(ch<1||ch>7);
				
				if(ch==7) {
					break;
				}
				
				switch (ch) {
				case 1: ss.insert(); break;
				case 2: ss.update(); break;
				case 3: ss.delete(); break;
				case 4: ss.findByHak(); break;
				case 5: ss.findByName(); break;
				case 6: ss.listAll(); break;
				}
			}
			
		} finally {
			sc.close();
		}

	}

}
