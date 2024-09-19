import requests
import json

# 공공데이터포털에서 제공하는 식품영양성분 API URL
url = 'http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api'

# API 요청에 필요한 파라미터
params = {
    'serviceKey': '서비스 키',  # 보안 인증키, 실제 사용 시에만 입력
    'numOfRows': '100',  # 한 페이지에 가져올 데이터 개수
    'type': 'json',      # 응답 데이터 형식 (json, xml 중 선택)
    # 명시하지 않은 파라미터에선 모든 데이터를 가져온다.
}

# 모든 페이지의 데이터를 저장할 리스트 초기화
all_items = []

# 페이지 번호를 1부터 122까지 반복
for page in range(1, 123):
    params['pageNo'] = str(page)  # 현재 페이지 번호 설정

    # API에 요청을 보내고 응답 받음
    response = requests.get(url, params=params)

    # 응답 상태 코드가 200이면 성공
    if response.status_code == 200:
        response_json = response.json()

        # 응답 데이터에서 'items' 추출
        try:
            items = response_json['response']['body']['items']
            all_items.extend(items)  # 전체 리스트에 추가
            print(f"{page}페이지 데이터 수집 완료.")
        except KeyError:
            print(f"{page}페이지에 'items' 키가 없습니다.")
    else:
        # 200이 아니면 에러 발생, 에러 메시지 출력
        print(f"API 요청 실패: 상태 코드 {response.status_code}")
        print(f"에러 메시지: {response.text}")

# 모든 페이지의 데이터를 JSON 파일로 저장
with open('D:/student/LHT/works/crawling/food_data.json', 'w', encoding='utf-8') as f:
    json.dump(all_items, f, ensure_ascii=False, indent=4)

print("모든 페이지의 데이터가 food_data.json 파일로 저장되었습니다.")