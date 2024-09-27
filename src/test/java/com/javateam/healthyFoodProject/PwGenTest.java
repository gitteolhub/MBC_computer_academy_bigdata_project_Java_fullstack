package com.javateam.healthyFoodProject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwGenTest {
	public static void main(String[] args) {
		
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		
		// 암호화할 비밀번호 문자열
		String strPw = "#Abcd2345";
		
		// 비밀번호를 10번 암호화하여 출력
		for (int i=0; i < 10; i++) {
			// encode 메서드를 사용하여 비밀번호를 암호화하고 출력
			System.out.println(pwEncoder.encode(strPw));
		}
	}

}

// #Abcd1234
// $2a$10$nwZedgB05kJqU9TB8dDadOVsgBPdKYnV7EgtHgY9DuFUhZYeApAre

// #Abcd3333
//$2a$10$xtG2AMrHT9yz8px3bWKWnuHTDYzjLpTo26N0frKFurwJQCnBzwP1q

// #Abcd2222
//$2a$10$Fv1P9qA4T2lLg6po3a9g8umHZbBOAur6t3KbXazAmnZyLVXTB1t6S

// #Abcd2345
// $2a$10$RtIntRneiR7bzkynq.tUC.1Gnd2M.WHW96XTD.UEYqePdO.QCNyES


