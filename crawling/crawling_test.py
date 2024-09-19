import requests
import json

# 공공데이터포털에서 제공하는 식품영양성분 API URL
url = 'http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api'

# API 요청에 필요한 파라미터
params = {
    'serviceKey': '서비스 키',  # 보안 인증키, 실제 사용 시에만 입력
    'pageNo': '1',  # 페이지 번호
    'numOfRows': '100',  # 한 페이지에 가져올 데이터 개수
    'type': 'json',  # 응답 데이터 형식 (json, xml 중 선택)
    # 명시하지 않은 파라미터에선 모든 데이터를 가져온다.
}

# API에 요청을 보내고 응답 받음
response = requests.get(url, params=params)

# 응답 결과를 출력 (디버깅을 위해)
print(response.content)

# 응답 상태 코드가 200이면 성공, JSON 파일로 저장
if response.status_code == 200:
    with open('D:/student/LHT/works/crawling/food_data.json', 'w', encoding='utf-8') as f:  # 실행하는 PC에 따라 저장경로 지정
        json.dump(response.json(), f, ensure_ascii=False, indent=4)
    print("데이터가 food_data.json 파일로 저장되었습니다.")
else:
    # 200이 아니면 에러 발생, 에러 메시지 출력
    print(f"API 요청 실패: 상태 코드 {response.status_code}")
    print(f"에러 메시지: {response.text}")