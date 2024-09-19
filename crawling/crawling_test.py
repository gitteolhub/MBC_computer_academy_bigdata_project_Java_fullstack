import requests
import json

# 공공데이터포털에서 제공하는 식품영양성분 API URL
url = 'http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api'

# API 요청에 필요한 파라미터
params = {
    'serviceKey': '서비스 키',  # 보안 인증키, 실제 사용 시에만 입력
    'numOfRows': '100',  # 한 페이지에 가져올 데이터 개수
    'type': 'json',  # 응답 데이터 형식 (json, xml 중 선택)
    'pageNo': '1'  # 초기 페이지 번호 설정
    # 이 외 명시하지 않은 파라미터에선 모든 데이터를 가져온다.
}

# 모든 페이지의 데이터를 저장할 리스트 초기화
all_items = []

# 첫 번째 페이지 요청을 보내 데이터를 가져옴. 데이터에는 전체 데이터 개수인 totalCount가 포함되어 있음
response = requests.get(url, params=params)

# 응답 상태 코드가 200이면 성공
if response.status_code == 200:
    response_json = response.json()
    try:
        # totalCount를 이용하여 전체 페이지 수 계산
        total_count = int(response_json['response']['body']['totalCount'])
        num_of_rows = int(params['numOfRows'])
        total_pages = (total_count // num_of_rows) + (1 if total_count % num_of_rows > 0 else 0)

        print(f"전체 데이터 수: {total_count}개")
        print(f"전체 페이지 수: {total_pages}페이지")

        # 첫 번째 페이지의 items를 all_items에 추가
        items = response_json['response']['body']['items']
        all_items.extend(items)
        print("1페이지 데이터 수집 완료.")

        # 2페이지부터 마지막 페이지까지 반복하여 데이터 수집
        for page in range(2, total_pages + 1):
            params['pageNo'] = str(page)
            response = requests.get(url, params=params)

            if response.status_code == 200:
                response_json = response.json()
                try:
                    items = response_json['response']['body']['items']
                    all_items.extend(items)
                    print(f"{page}페이지 데이터 수집 완료.")
                except KeyError:
                    print(f"{page}페이지에 'items' 키가 없습니다.")
            else:
                print(f"{page}페이지 API 요청 실패: 상태 코드 {response.status_code}")
                print(f"에러 메시지: {response.text}")
    except KeyError:
        print("응답에 'totalCount' 또는 'items' 키가 없습니다.")
else:
    print(f"API 요청 실패: 상태 코드 {response.status_code}")
    print(f"에러 메시지: {response.text}")

# 모든 페이지의 데이터를 JSON 파일로 저장
with open('D:/student/LHT/works/crawling/food_data.json', 'w', encoding='utf-8') as f:  # 실행하는 PC에 따라 저장경로 지정
    json.dump(all_items, f, ensure_ascii=False, indent=4)

print("모든 페이지의 데이터가 food_data.json 파일로 저장되었습니다.")