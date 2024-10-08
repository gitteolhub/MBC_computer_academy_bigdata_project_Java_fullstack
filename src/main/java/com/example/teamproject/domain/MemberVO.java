package com.example.teamproject.domain;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원정보 테이블 값객체(VO, Entity Object)
 *
 * @author java
 *
 */
@Data
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 오버로딩된 생성자 => @Builder
@Slf4j // 추가
public class MemberVO {

	/** 아이디 */
	private String id;
	/** 패쓰워드 */
	private String pw;
	/** 이름 */
	private String name;
	/** 성별 */
	private String gender;
	/** 이메일 */
	private String email;
	/** 연락처1(휴대폰) */
	private String mobile;
	/** 연락처2(일반전화) */
	private String phone;
	/** 우편번호 */
	private String zip;
	/** 도로명 주소 */
	private String roadAddress;
	/** 지번 주소 */
	private String jibunAddress;
	/** 상세 주소 */
	private String detailAddress;
	/** 생년월일 */
	private Date birthday;
	/** 가입일 */
	private Date joindate;

	/** 회원 활성화 여부 : 추가 */
	private int enabled;

	// Map → MemberVO 생성자 추가
	public MemberVO(Map<String, Object> requestMap) {

		Set<String> set = requestMap.keySet();
		Iterator<String> it = set.iterator();
		Field field; // reflection 정보 활용

		while (it.hasNext()) {

			 String fldName = it.next();

			 try {
		    		// DTO와 1:1 대응되는 필드들 처리
			    	try {
							field = this.getClass().getDeclaredField(fldName);
							field.setAccessible(true);

							if (!fldName.equals("birthday") || !fldName.equals("joindate")) {
								field.set(this, requestMap.get(fldName));
							}

					} catch (NoSuchFieldException e) {

						// 만약 VO와 1:1 대응되지 않는 인자일 경우는 이 부분에서 입력처리합니다.
						log.info("인자와 필드가 일치하지 않습니다.");

					} // try

			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} // try

		} // while

	} //

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberVO other = (MemberVO) obj;
		return Objects.equals(birthday, other.birthday) && Objects.equals(detailAddress, other.detailAddress)
				&& Objects.equals(email, other.email) && Objects.equals(gender, other.gender)
				&& Objects.equals(id, other.id) && Objects.equals(jibunAddress, other.jibunAddress)
				&& Objects.equals(mobile, other.mobile) && Objects.equals(name, other.name)
				&& Objects.equals(phone, other.phone) && Objects.equals(pw, other.pw)
				&& Objects.equals(roadAddress, other.roadAddress) && Objects.equals(zip, other.zip);
	}
	@Override
	public int hashCode() {
		return Objects.hash(birthday, detailAddress, email, gender, id, jibunAddress, mobile, name, phone, pw,
				roadAddress, zip);
	}

}