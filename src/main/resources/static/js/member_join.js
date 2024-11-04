/**
 * 비밀번호 확인 기능
 */

 document.getElementById('frm').addEventListener('submit', function(event) {
    const pw = document.getElementById('pw').value;
    const pwConfirm = document.getElementById('pw_confirm').value;

    if (pw !== pwConfirm) {
        alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
        event.preventDefault(); // 폼 제출을 막습니다
    }
});

document.getElementById('pw').addEventListener('input', checkPasswordMatch);
document.getElementById('pw_confirm').addEventListener('input', checkPasswordMatch);

function checkPasswordMatch() {
    const pw = document.getElementById('pw').value;
    const pwConfirm = document.getElementById('pw_confirm').value;
    const pwConfirmField = document.getElementById('pw_confirm_fld_pnl');
    const errorField = document.getElementById('pw_confirm_fld_err_pnl');

    if (pw === pwConfirm) {
        pwConfirmField.classList.remove('has-error');
        errorField.textContent = '';
    } else {
        pwConfirmField.classList.add('has-error');
        errorField.textContent = '비밀번호가 일치하지 않습니다';
    }
}