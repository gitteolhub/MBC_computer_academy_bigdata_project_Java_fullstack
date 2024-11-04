/////////////////////////////////////////////////////////////////////////////////////////////

// 에러 메시징 함수
// 기능 : 필드별로 폼 점검 시행 후 에러 메시징(패널)
//        개별 필드 체크 플래그에 리턴
// 1) 함수명 : isCheckFldValid
// 2) 인자 :
// 필드 (아이디) 변수(fld), 필드 기정값(initVal),
// 필드별 정규표현식(유효성 점검 기준) (regex)
// 필드별 에러 패널(errPnl), 필드별 에러 메시지(errMsg)
// 3) 리턴 : fldCheckFlag : boolean (true/false) : 유효/무효
// 4) 용례(usage) :  
// idCheckFlag = isCheckFldValid(idFld, 
//                        /^[a-zA-Z]{1}\w{7,19}$/,
//                        idFldErrPnl,
//                        "",     
//                        "회원 아이디는 8~20사이의 영문으로 
//                        시작하여 영문 대소문자/숫자로 입력하십시오.")
function isCheckFldValid(fld, regex, initVal, errPnl, errMsg) {

    // 리턴값 : 에러 점검 플래그
    let fldCheckFlag = false;

    // 체크 대상 필드 값 확인
    console.log(`체크 대상 필드 값 : ${fld.value}`);

    // 폼 유효성 점검(test)
    console.log(`점검 여부 : ${regex.test(fld.value)}`);

    if (regex.test(fld.value) == false) {

        errPnl.style.height = "50px"; 
        errPnl.innerHTML = errMsg; 

        // 기존 필드 데이터 초기화
        // fld.value = "";
        fld.value = initVal;
        fld.focus(); // 재입력 준비     
        
        fldCheckFlag = false;

    } else { // 정상

        // 에러 패널 초기화
        errPnl.style.height = "0"; 
        errPnl.innerHTML = "";

        fldCheckFlag = true;
    } // if

    return fldCheckFlag;
} //

////////////////////////////////////////////////////


window.onload = function(e) {

    // 각 필드들의 에러 점검 여부 (플래그(flag) 변수)

    // 회원 정보 수정용 패쓰워드 필드 점검 플래그들
    // 주의) 초기 상태는 true로 설정 => 사유) 기존 패쓰워드를 보유하고 있으므로 공백이어도 문제 안됨.
    let pw1CheckFlag = true;

	let pw2CheckFlag = true;
	
	// password1과 password2의 값의 동일성 여부(공백일지라도 값은 동일) : 기본값은 true
	let pwEqualCheckFld = true;

    // 이메일 점검 플래그(회원 수정시 기존 값이 필수값이므로 true 설정)
    let emailCheckFlag = true;

	// 이메일 중복 점검 플래그
	let emailDuplicatedCheckFlag = false;

    // 주소 점검 플래그
    // 주소에 대한 사항이 필수가 아니라 선택 사항인 경우는 
    // 입력하지 않아도 무관하기 때문에 초기 상태(무입력 상태)도 true로 간주하여
    // 초기값 true 설정

    // 패쓰워드 필드 인식
	// 회원 정보 수정 패쓰워드 필드
    let pw1Fld = document.getElementById("passwordUpdate");

	// 회원 정보 수정 패쓰워드(확인) 필드
	let pw2Fld = document.getElementById("passwordVerify");
	
    // 패쓰워드 에러 패널 인식
    let pwFldErrPnl = document.getElementById("password_fld_err_pnl");

    // 이메일 필드 인식
    let emailFld = document.getElementById("email");

    // 이메일 필드 에러 패널 인식
    let emailFldErrPnl = document.getElementById("email_fld_err_pnl");

	 // 연락처 점검 플래그(회원 수정시 기존 값이 필수값이므로 true 설정)
    let mobileCheckFlag = true;

	// 연락처(휴대폰) 중복 점검 플래그
	let mobileDuplicatedCheckFlag = false;
	
 	// 연락처(휴대폰) 필드 인식
    let mobileFld = document.getElementById("phone");
    
    // 연락처(휴대폰) 필드 에러 패널 인식
    let mobileFldErrPnl = document.getElementById("phone_fld_err_pnl");
    ////////////////////////////////////////////////////////////////////////

	// 회원 정보 수정 패쓰워드 필드들 이벤트 처리
	
    // 패쓰워드 필드 유효성 점검(validation)
    // 기준)
    /*
        1) 길이(length) 8~20 : {8,20}
        2) 최소 1개의 숫자 포함 : (?=.*\d)
        3) 최소 1개의 특수문자 포함 : (?=.*\W)
        4) 대문자 1개 이상 포함 : (?=.*[A-Z])
        5) 소문자 1개 이상 포함 : (?=.*[a-z])
        6) regex(정규표현식) : (?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}
        7) 메시징 : 회원 패쓰워드는 영문 대소/숫자/특수문자를 1개이상 포함하여 8~20자로 작성하십시오..
    */

    // 패쓰워드1 필드 입력 후 이벤트 처리 : blur
	// 유의) password1 과 password2의 값의 동일성 여부도 점검  
	// 패쓰워드 확인 필드에서 벗어났을 때(두 개의 패쓰워드 필드가 입력되었을 때 해당될 수 있는 이벤트 상황) 이벤트 처리
    pw1Fld.onblur = (e) => {
	
		console.log("패쓰워드 필드1 & 패쓰워드 필드값 일치성 점검 : blur")		
		
		let pw1FldVal = pw1Fld.value;
		let pw2FldVal = pw2Fld.value;
		
		pwEqualCheckFld = pw1FldVal.trim() == pw2FldVal.trim() ? true : false;
		
		console.log("패쓰워드 필드들의 값 일치성 점검 blur-1 : ", pwEqualCheckFld);

		// 주의) 패쓰워드 필드가 공백이면 검사 제외(회원정보 수정 모드에서는 공백은 기존 패쓰워드 유지 상태이므로 문제 없음)
		  
		if (pw1FldVal.trim() != '' || pw2FldVal.trim() != '') {

	        pw1CheckFlag = isCheckFldValid(pw1Fld,                                 
	                        /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
	                        "",
	                        pwFldErrPnl,
	                        "회원 패쓰워드는 영문 대소/숫자/특수문자 "+ 
	                        "1개이상 포함하여 8~20자로 작성하십시오.");
		} else {
			
		    // 에러 패널 초기화
	        pwFldErrPnl.style.height = "0"; 
	        pwFldErrPnl.innerHTML = "";
	
	        pw1CheckFlag = true;			
		} //
		
    } //     

	// 패쓰워드2(확인) 필드 입력 후 이벤트 처리 : blur
	// 유의) password1 과 password2의 값의 동일성 여부도 점검  
	// 패쓰워드 확인 필드에서 벗어났을 때(두 개의 패쓰워드 필드가 입력되었을 때 해당될 수 있는 이벤트 상황) 이벤트 처리
    pw2Fld.onblur = (e) => {
	
		console.log("패쓰워드 필드2 & 패쓰워드 필드값 일치성 점검 : blur")		
		
		let pw1FldVal = pw1Fld.value;
		let pw2FldVal = pw2Fld.value;
		
		pwEqualCheckFld = pw1FldVal.trim() == pw2FldVal.trim() ? true : false;
		
		console.log("패쓰워드 필드들의 값 일치성 점검 blur-2 : ", pwEqualCheckFld);

		// 주의) 패쓰워드 필드가 공백이면 검사 제외(회원정보 수정 모드에서는 공백은 기존 패쓰워드 유지 상태이므로 문제 없음)
		  
		if (pw1FldVal.trim() != '' || pw2FldVal.trim() != '') {
			
	        pw2CheckFlag = isCheckFldValid(pw2Fld,                                 
	                        /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
	                        "",
	                        pwFldErrPnl,
	                        "회원 패쓰워드는 영문 대소/숫자/특수문자 "+ 
                       		"1개이상 포함하여 8~20자로 작성하십시오.");
		} else {
			
		    // 에러 패널 초기화
	        pwFldErrPnl.style.height = "0"; 
	        pwFldErrPnl.innerHTML = "";
	
	        pw2CheckFlag = true;			
		} //
		
    } //     

    // 패쓰워드 필드 입력 후 이벤트 처리 : keyup
    pw1Fld.onkeyup = (e) => {

        console.log("패쓰워드 필드1 & 패쓰워드 필드값 일치성 점검 : keyup")		
		
		let pw1FldVal = pw1Fld.value;
		let pw2FldVal = pw2Fld.value;
		
		pwEqualCheckFld = pw1FldVal.trim() == pw2FldVal.trim() ? true : false;
		
		console.log("패쓰워드 필드들의 값 일치성 점검 keyup-1 : ", pwEqualCheckFld);
		  
		if (pw1FldVal.trim() != '' || pw2FldVal.trim() != '') {
			
	        pw1CheckFlag = isCheckFldValid(pw1Fld,                                 
	                        /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
	                        pw1Fld.value,
	                        pwFldErrPnl,
	                        "회원 패쓰워드는 영문 대소/숫자/특수문자 1개이상 포함하여 8~20자로 작성하십시오.");
		} else {
			
		    // 에러 패널 초기화
	        pwFldErrPnl.style.height = "0"; 
	        pwFldErrPnl.innerHTML = "";
	
	        pw1CheckFlag = true;			
		} //	
		
    } //     

	// 패쓰워드 필드2 입력 후 이벤트 처리 : keyup
    pw2Fld.onkeyup = (e) => {

        console.log("패쓰워드 필드2 & 패쓰워드 필드값 일치성 점검 : keyup")		
		
		let pw1FldVal = pw1Fld.value;
		let pw2FldVal = pw2Fld.value;
		
		pwEqualCheckFld = pw1FldVal.trim() == pw2FldVal.trim() ? true : false;
		
		console.log("패쓰워드 필드들의 값 일치성 점검 keyup-2 : ", pwEqualCheckFld);
		  
		if (pw1FldVal.trim() != '' || pw2FldVal.trim() != '') {
			
		    pw1CheckFlag = isCheckFldValid(pw2Fld,                                 
		                    /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}/,
		                    pw2Fld.value,
		                    pwFldErrPnl,
	                        "회원 패쓰워드는 영문 대소/숫자/특수문자 1개이상 포함하여 8~20자로 작성하십시오.");
		}
		
    } //     


    ///////////////////////////////////////////////////////////////////////////////////////////
   
    // 이메일 필드 입력 후 이벤트 처리 : onkeyup
    emailFld.onkeyup = (e) => {

        console.log("이메일 필드 onkeyup")
        // 이메일 필드 유효성 점검(validation)
        // 기준)
        /*
            1) "@", "." 포함여부 점검
            2) regex(정규표현식) : /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/
            3) 메시징 : 회원 이메일을 제시된 예와 같이 작성해주세요.
        */
        emailCheckFlag = isCheckFldValid(emailFld,
                        /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        emailFld.value,
                        emailFldErrPnl,
                        "회원 이메일을 제시된 예와 같이 작성해주세요.");
    } //     

	// 연락처(휴대폰) 필드 입력 후 이벤트 처리 : onkeyup
    mobileFld.onkeyup = (e) => {

        console.log("연락처(휴대폰) 필드 onkeyup")
        // 연락처 필드 유효성 점검(validation)
        // 기준)
        /*
            1) 휴대폰 입력 예시 : ex) 010-1234-5678
            2) regex(정규표현식) : /^010-\d{4}-\d{4}$/
            3) 메시징 : 회원 연락처(휴대폰)를 제시된 예와 같이 작성해주세요.
        */
        mobileCheckFlag = isCheckFldValid(mobileFld,
                        /^010-\d{4}-\d{4}$/,
                        mobileFld.value,
                        mobileFldErrPnl,
                        "회원 연락처(휴대폰)를 제시된 예와 같이 작성해주세요.");
    } //     
	
	// 이메일 중복 점검 : AJAX axios
	emailFld.onblur = (e) => {
		
		console.log("이메일 중복 점검");
		
		// 중복 점검 REST 주소(회원 정보 수정 전용)/인자(id 추가) 변경
		var idFld = document.getElementById("id"); // 아이디 필드
		
		axios.get(`/memberProject/member/hasFldForUpdate/${idFld.value}/EMAIL/${emailFld.value}`)
			 .then(function(response) {
				
				emailDuplicatedCheckFlag = response.data;
				console.log("response.data : ", response.data);

				let emailDupErrMsg = emailDuplicatedCheckFlag == true ? "중복되는 이메일이 존재합니다" : "사용가능한 이메일입니다"				   

				if (emailDuplicatedCheckFlag == true) {
					emailFldErrPnl.innerHTML = emailDupErrMsg;
					// CSS 상하 여백 조정
					emailFldErrPnl.style.paddingBottom = '20px';
				} else {
					emailFldErrPnl.innerHTML = ""; // 메시지 초기화
					emailFldErrPnl.style.paddingBottom = 0; // 메시지 패널 초기화
				} //	
					
			 })
			 .catch(function(err) {
				console.error("이메일 중복 점검 중 서버 에러가 발견되었습니다.");
				// emailDuplicatedCheckFlag = false;
			 });
	} //
	
	// 연락처(휴대폰) 중복 점검 : AJAX axios
	mobileFld.onblur = (e) => {
		
		console.log("연락처(휴대폰) 중복 점검");
		
		// 중복 점검 REST 주소(회원 정보 수정 전용)/인자(id 추가) 변경
		var idFld = document.getElementById("id"); // 아이디 필드
	
		axios.get(`/memberProject/member/hasFldForUpdate/${idFld.value}/MOBILE/${mobileFld.value}`)
			 .then(function(response) {
				
				mobileDuplicatedCheckFlag = response.data;
				console.log("response.data : ", response.data);

				let mobileDupErrMsg = mobileDuplicatedCheckFlag == true ? "중복되는 연락처(휴대폰)가 존재합니다" : "사용가능한 연락처(휴대폰)입니다"				   

				if (mobileDuplicatedCheckFlag == true) {
					mobileFldErrPnl.innerHTML = mobileDupErrMsg;
					// CSS 상하 여백 조정
					mobileFldErrPnl.style.paddingBottom = '20px';
				} else {
					mobileFldErrPnl.innerHTML = ""; // 메시지 초기화
					mobileFldErrPnl.style.paddingBottom = 0; // 메시지 패널 크기 초기화
				}	
					
			 })
			 .catch(function(err) {
				console.error("연락처(휴대폰) 중복 점검 중 서버 에러가 발견되었습니다.");
				// mobileDuplicatedCheckFlag = false;				
			 });
			
	} //

    /////////////////////////////////////////////////////////////////
 
    // 전송 버튼 이벤트 처리
    let frm = document.getElementById("frm");

    frm.onsubmit = function(e) {
	
		alert("회원수정 폼점검");
		
        console.log("\n######## 회원수정폼 전체점검 ###############################\n\n");
		
		// 회원 정보 수정용 패쓰워드 필드들의 점검 플래그
        console.log(`패쓰워드 점검 플래그(pw1CheckFlag) : ${pw1CheckFlag}`);
		console.log(`패쓰워드 점검 플래그(pw2CheckFlag) : ${pw2CheckFlag}`);
		console.log(`패쓰워드 동일 여부 점검 플래그(pwEqualCheckFld) : ${pwEqualCheckFld}`);		

        // 이메일 및 연락처 점검 플래그
        console.log(`이메일 점검 플래그(emailCheckFlag) : ${emailCheckFlag}`);
		console.log(`연락처(휴대폰) 점검 플래그(mobileCheckFlag) : ${mobileCheckFlag}`);
		
		// 이메일/연락처(휴대폰) 중복 점검 플래그
		// 주의) 이 플래그들은 false 이어야 중복되지 않는 값을 의미합니다.  
		console.log(`이메일 중복 점검 플래그(emailDuplicatedCheckFlag) : ${emailDuplicatedCheckFlag}`);
		console.log(`연락처(휴대폰) 중복 점검 플래그(mobileDuplicatedCheckFlag) : ${mobileDuplicatedCheckFlag}`);
		
		//////////////////////////////////////////////////////////////////////
		//		
		// 패쓰워드 필드 동일성 점검 : 주의) 전송 직전에 점검 !
		pwEqualCheckFld = pw1Fld.value == pw2Fld.value ? true : false;
		console.log("패쓰워드 동일 여부 점검 플래그(전송 직전)(pwEqualCheckFld) : ", pwEqualCheckFld);
		
		console.log("\n######################################################\n");
		
		alert("잠시 전체 폼점검 플래그 확인");
		
        // 모든 플래그 참(true) : 논리곱(&&)
		// 패쓰워드 필드 값 일치성 점검 필드 추가 
        if (pw1CheckFlag == true &&
			pw2CheckFlag == true &&
			pwEqualCheckFld == true &&
            emailCheckFlag == true &&
            mobileCheckFlag == true &&
			emailDuplicatedCheckFlag == false &&
			mobileDuplicatedCheckFlag == false)
        {
			
            alert("전송");

			// e.preventDefault();

        } else {

			alert("폼 점검 오류");
            console.log("폼 점검 오류");

            // 필드들을 종합적으로 일일이 점검할 필요가 있기 때문에 
            // if ~ else if문은 사용하지 않고 개별 if문을 사용하도록 하겠습니다.
			
            // 이메일 필드 재점검
            if (emailCheckFlag == false) {
                
                emailCheckFlag = isCheckFldValid(emailFld,
                                /^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/,
                                emailFld.value,
                                emailFldErrPnl,
                                "회원 이메일을 제시된 예와 같이 작성해주세요.");

            } //    
            
            // 연락처 필드 재점검
            if (mobileCheckFlag == false) {

                mobileCheckFlag = isCheckFldValid(mobileFld,
                        /^010-\d{4}-\d{4}$/,
                        mobileFld.value, 
                        mobileFldErrPnl,
                        "회원 연락처(휴대폰)를 제시된 예와 같이 작성해주세요.");
            } // 

			// 이메일/연락처(휴대폰) 중복 재점검에 따른 최종 메시징			
			if (emailDuplicatedCheckFlag == true) {
				alert("중복되는 이메일이 존재합니다");
			}
			
			if (mobileDuplicatedCheckFlag == true) {
				alert("중복되는 연락처(휴대폰)가 존재합니다");
			}
			
			// password1 과 password2의 값의 동일성 여부도 점검  
			if (pwEqualCheckFld == false) {				
				alert("패쓰워드가 일치하지 않습니다. 다시 입력하십시오.")
			} //
			
            // 전송 방지
            e.preventDefault();
        } //

    } // frm.onsubmit ...

} // window.onload .....