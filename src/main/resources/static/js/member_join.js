/**
 * 비밀번호 확인 기능
 */

// 비밀번호가 일치하지 않을 시 폼 제출이 되지 않음
 document.getElementById('frm').addEventListener('submit', function(event) {
    const pw = document.getElementById('pw').value;
    const pwConfirm = document.getElementById('pw_confirm').value;

    if (pw !== pwConfirm) {
        alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
        event.preventDefault(); // 폼 제출을 막습니다
    }
});

//TODO 비밀번호 확인이 일치하지 않을 경우 에러 메시지 출력(다른 에러메시지와 같은 양식 사용)