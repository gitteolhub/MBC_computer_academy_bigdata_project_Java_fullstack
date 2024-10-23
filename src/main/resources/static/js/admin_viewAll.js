/**
 *
 */

/////////////////////////////////////////////////////////////////////////////////////////////
//
//
// 에러 메시징 함수
// 기능 : 필드별로 폼 점검 시행 후 에러 메시징(패널)
//        개별 필드 체크 플래그에 리턴
// 1) 함수명 : isCheckFldValid
// 2) 인자 :
// 필드 (아이디) 변수(fld), 필드 기정값(initVal),
// 필드별 정규표현식(유효성 점검 기준) (regex)
// 필드별 에러 메시지(errMsg)
// 3) 리턴 : fldCheckFlag : boolean (true/false) : 유효/무효
// 4) 용례(usage) :
// idCheckFlag = isCheckFldValid(idFld,
//                        /^[a-zA-Z]{1}\w{7,19}$/,
//                        "",
//                        "회원 아이디는 8~20사이의 영문으로
//                        시작하여 영문 대소문자/숫자로 입력하십시오.")
function isCheckFldValid(fld, regex, initVal, errMsg) {

    // 리턴값 : 에러 점검 플래그
    let fldCheckFlag = false;

    // 체크 대상 필드 값 확인
    console.log(`체크 대상 필드 값 : ${fld.value}`);

    // 폼 유효성 점검(test)
    console.log(`점검 여부 : ${regex.test(fld.value)}`);

    if (regex.test(fld.value) == false) {

        alert(errMsg); // 팝업 출력
        fldCheckFlag = false;
    } else {
	   fldCheckFlag = true;
	}

    return fldCheckFlag;
} //

////////////////////////////////////////////////////

window.onload = function() {

	// 활동여부(enabled) 스위치 컴포넌트/라벨 제어
	let enabledFlds = document.querySelectorAll("[id^='enabled_']");

	for (enabledFld of enabledFlds) {

		enabledFld.onclick = function(e) {

			let id = e.target.id;

			console.log("체크된 아이디 :", id);
			let enabledFld      = document.getElementById(id);
			let enabledLabelFld = document.getElementById('label_' + id);

			console.log("enabledFld.checked : ", enabledFld.checked);

			enabledLabelFld.innerHTML = enabledFld.checked == true ? "활동" : "휴면";

			// 값 재설정
			enabledFld.value = enabledFld.checked == true ? 1 : 0;

		} // function

	} // for

	///////////////////////////////////////////////////////////////////////////////

	// 등급(role) 업데이트(수정)
	let role_update_btns = document.querySelectorAll("[id^='role_update_btn_']");

	for (role_update_btn of role_update_btns) {

		role_update_btn.onclick = function(e) {

			let id = e.target.id;
			id = id.substring("role_update_btn_".length);

			console.log("id :", id);

			// 등급(role) 체크 박스들 값 읽어오기
			let roleUser  = document.getElementById("role1_"+id);
			let roleAdmin = document.getElementById("role2_"+id);

			console.log("roleUser : ", roleUser.checked);
			console.log("roleAdmin : ", roleAdmin.checked);

			// AJAX 전송 : axios
			console.log("등급(롤) 변경");

			// 중복 점검 REST 주소(회원 정보 수정 전용)/인자(id 추가) 변경
			var idFld = document.getElementById("id"); // 아이디 필드

			// 메시지
			let msg = "";

			axios.get(`/healthyFoodProject/admin/updateRoles/${id}/roleUser/${roleUser.checked}/roleAdmin/${roleAdmin.checked}`)
				 .then(function(response) {

						let responseData = response.data;
						console.log("response.data : ", responseData);

						console.log("responseData == true : ", responseData == true);

						if (roleUser.checked == false) {

							// 해당 체크박스 필드에서 disabled 처리되면 메시지 처리되지 않음
							// 아래는 필드 활성화시에 작동됨 !
							msg = "회원 미만의 등급으로 처리할 수 없습니다.";
							// 회원 체크박스 필드 원상 복귀
							document.getElementById("role1_" + id).checked = "checked";


						} else {

							if (responseData == true) {
								msg = "회원 등급(role) 변경에 성공하였습니다.";
							} else {
								msg = "회원 등급(role) 변경에 실패하였습니다.";
							}
						} //

						alert(msg);

					 })
					 .catch(function(err) {
						console.error("회원 등급(role) 업데이트(수정)에 실패하였습니다.");
						alert("회원 등급(role) 업데이트(수정)에 실패하였습니다");
					 });
				// axios

		} // for

	} // 등급(role) 업데이트(수정) 끝

	//////////////////////////////////////////////////////////////////////////////
	//
	// 검색 버튼 전송시 폼점검 : 검색어 공백 점검
	let searchBtn  = document.getElementById("search_btn");
	let searchWord = document.getElementById("searchWord");

	searchBtn.onclick = (e) => {

		if (searchWord.value.trim() == '') {

			alert("검색어를 입력하십시오.");
			searchWord.focus();
		}

	} //

	///////////////////////////////////////////////////////////////////////////////
	//
	// 활동 여부 설정 : 회원 활동/휴면 계정 설정
	let enabledUpdateBtns = document.querySelectorAll("[id^=enabled_update_btn_]");

	for (enabledUpdateBtn of enabledUpdateBtns) {

		enabledUpdateBtn.onclick = (e) => {

			let msg = "";

			let id = e.target.id; // 'enabled_'~~~
			id = id.substring('enabled_update_btn_'.length);

			let enabled = document.getElementById('enabled_'+id).value;

			console.log("회원 상태 변경 : ",  id, ', ', enabled);

			axios.get(`/healthyFoodProject/admin/changeMemberState/${id}/${enabled}`)
				 .then(function(response) {

						console.log("response.data === true : ", response.data === true);

						if (response.data === true) {
							msg = "회원 계정 상태 변경 처리에 성공하였습니다.";
						}

						alert(msg);

					 })
					 .catch(function(err) {
						console.error("회원 계정 상태 변경 처리에 실패하였습니다. : ", err.error);
						alert("회원 계정 상태 변경 처리에 실패하였습니다.");
					 });
				// axios

		} // onclick

	} // for

	///////////////////////////////////////////////////////////////////////////////
	//
	// 연락처(휴대폰) 수정 관련
	let phoneUpdateFlds = document.querySelectorAll("[id^=phone_]");

	for (phoneUpdateFld of phoneUpdateFlds) {

		// 클릭시 => 편집가능 상태(readonly 해제)
		phoneUpdateFld.onclick = (e) => {

			let id = e.target.id; // 'mobile_' ~~~
			id = id.substring('phone_'.length);

			let phoneFld = document.getElementById('phone_'+id);

			// 휴대폰 표시 영역(td)를 편집 가능한 상태로 변경
			phoneFld.removeAttribute("readonly");
			phoneFld.focus();
		}

		// 벗어났을 때 => 편집 불가능 상태(readonly)
		phoneUpdateFld.onblur = (e) => {

			let id = e.target.id; // 'mobile_' ~~~
			id = id.substring('phone_'.length);

			let phoneFld = document.getElementById('phone_'+id);

			// 휴대폰 표시 영역(td)를 편집 불가능한 상태(초기 상태)로 변경
			phoneFld.setAttribute("readonly", "readonly");
		}

	} // for


	///////////////////////////////////////////////////////////////////////////////////
	//
	// 회원 정보 (일괄) 수정 버튼
	//
	let updateBtns = document.querySelectorAll("[id^=update_btn]");

	for (updateBtn of updateBtns) {

		updateBtn.onclick = (e) => {

			let id = e.target.id; // 'phone_' ~~~
			id = id.substring('update_btn'.length);

			let updateBtn = document.getElementById('update_btn'+id);

			// 변경할 필드들의 값 집계
			let phoneFld = document.getElementById("phone_"+id);

			console.log("휴대폰 : ", phoneFld.value);

			// 유효성 점검(regex validation)
			// 기존의 개별 회원정보 수정 페이지와는 달리 팝업(alert)로 에러 출력
			let phoneCheckFlag = isCheckFldValid(phoneFld, /^010-\d{4}-\d{4}$/, phoneFld.value, "회원 연락처(휴대폰)를 제시된 예와 같이 작성해주세요.\n\n ex) 010-1234-5678");

			console.log("phoneCheckFlag : " + phoneCheckFlag);

			// 유효성 점검을 통과해야만 전송
			if (phoneCheckFlag == true) { // 유효한 휴대폰 정보라면...

				alert("중복 점검 전송");

				// 휴대폰 중복 점검 플래그
				let phoneDuplicatedCheckFlag = false;

				axios.get(`/memberProject/member/hasFldForUpdate/${id}/phone/${phoneFld.value}`)
				 .then(function(response) {

					phoneDuplicatedCheckFlag = response.data;
					console.log("response.data : ", response.data);

					let phoneDupErrMsg = phoneDuplicatedCheckFlag == true ? "중복되는 연락처(휴대폰)가 존재합니다" : "사용가능한 연락처(휴대폰)입니다"

								// 오류 있을 때만 팝업으로 출력하도록 조치
					if (phoneDuplicatedCheckFlag == true) {
						alert(phoneDupErrMsg);
					}

				 })
				 .catch(function(err) {
					console.error("연락처(휴대폰) 중복 점검 중 서버 에러가 발견되었습니다.");
					phoneDuplicatedCheckFlag = false;
				 });
				// axios

				console.log("phoneDuplicatedCheckFlag : ", phoneDuplicatedCheckFlag);


				/////////////////////////////////////////////////////////////////////////////

				// 중복 점검 통과시 회원 정보(휴대폰) 수정 전송
				if (phoneDuplicatedCheckFlag == false) { // 중복된 휴대폰이 없으면...

					alert("휴대폰 정보 수정 전송 : " + phoneFld.value);

					// 변경할 회원 정보 전송
					// case-1
					// axios.get(`/healthyFoodProject/member/updateMemberByAdmin/${id}/phone/${phoneFld.value}`)

					// case-2
					// post 방식
					axios({
							method : "post",
							url    : '/healthyFoodProject/admin/updateMemberByAdmin',
							data   : {id : id, mobile : phoneFld.value},
							headers: {"Content-Type":'multipart/form-data'}
						 })
						 .then(function(response) {

							console.log("response.data === true : ", response.data === true);

							if (response.data === true) {
								msg = "회원 정보 수정 처리에 성공하였습니다.";
							}

							alert(msg);

						 })
						 .catch(function(err) {
							 
							console.error("회원 정보 수정 중 서버 에러가 발견되었습니다.");
							alert("회원 정보 수정 처리에 실패하였습니다.");
						 });
						// axios

				}

	  		} // if : 유효성 점검

		} //

	} // for

	///////////////////////////////////////////////////////////////////////////////////
	//
	// (개별) 회원 정보 (일괄) 삭제 버튼
	//
	let deleteBtns = document.querySelectorAll("[id^=delete_btn]");

	for (deleteBtn of deleteBtns) {

		deleteBtn.onclick = (e) => {

			id = e.target.id;
			id = id.substring('delete_btn'.length);

			// let deleteBtn = document.getElementById('delete_btn'+id);

			if (confirm(`${id} 님의 회원 정보를 정말 삭제하시겠습니까?`) == true) {

				alert(`${id} 님의 회원 정보를 삭제하겠습니다.`);

				axios.get(`/healthyFoodProject/admin/deleteMemberByAdmin/${id}`)
				 .then(function(response) {

					console.log("response.data : ", response.data);

					// 오류 있을 때만 팝업으로 출력하도록 조치
					if (response.data === true) {
						msg = "회원 정보 삭제 처리에 성공하였습니다.";
					}

					alert(msg);
					// 화면 갱신 : refresh
					location.reload();

				 })
				 .catch(function(err) {
					console.error("개별 회원 정보 삭제 중 서버 에러가 발견되었습니다.");
					alert(`${id} 님의 회원 정보 삭제 중 서버 에러가 발견되었습니다.`);
				 });
				// axios

			} else {
				alert(`${id} 님의 회원 정보 삭제를 취소하였습니다.`);
			} //

		} // onclick

	} // for

	///////////////////////////////////////////////////////////////////////////////////

// "검색 구분" 필드가 변경될 때마다 입력 예시 문구(안내문) 및 필드 변경
	let searchKey = document.getElementById("searchKey");
	// let searchWord = document.getElementById("searchWord");
	let guideText = ""

	searchKey.onchange = function() {

		switch (searchKey.value) {
 
  		case "id"       : guideText = "8~20자로 영문/숫자로 작성합니다"      ; break;
		case "name"     : guideText = "한글로 입력합니다"                  ; break;
		case "gender"   : guideText = "'남' 혹은 '여'로 입력합니다"         ; break;
		case "email"    : guideText = "abcd@abcd.com과 같이 입력합니다"    ; break;
		case "mobile"   : guideText = "010-1234-5678와 같이 입력합니다"    ; break;
		case "phone"    : guideText = "02-1234-5678과 같이 입력합니다"     ; break;
		case "address"  : guideText = "시/구/군/동이름 등을 입력합니다"       ; break;
		case "birthday" : guideText = "2000-01-01과 같이 입력합니다"       ; break;
		case "joindate" : guideText = "2000-01-01과 같이 입력합니다"       ; break;
		case "role"     : guideText = "'관리자' 혹은 '회원'으로 입력합니다"    ; break;
		}

		searchWord.placeholder = guideText;

		// 가입일, 생일일 경우는 년월일 입력하도록 date 필드로 변환 조치
		// 다른 필드일 경우는 text 필드로 재변환
		if (searchKey.value == "birthday" || searchKey.value == "joindate") {
			searchWord.type = "date";
		} else {
			searchWord.type = "text";
		} //

	}
	
} // onload
