import axios from 'axios';

window.onload = function(e) {
<<<<<<< HEAD

	console.log("id: ", e.target.id);

=======
	
	console.log("id: ", e.target.id)
	
>>>>>>> refs/heads/YSJANG26
	axios.get(`/healthyFoodProject/foodMenu/view`)
		 .then(function(response) {

			console.log("response.data : ", response.data);

			// 오류 있을 때만 팝업으로 출력하도록 조치
			if (response.data === true) {
				msg = "새로운 회원의 초기 식단 데이터를 무사히 불러왔습니다.";

			}

			// 화면 갱신 : refresh
			location.reload();

		 })
		 .catch(function(err) {
			console.error("초기 식단 데이터를 불러오는 중 서버 에러가 발견되었습니다.");
		 });
		// axios

} // onload