import requests
import json
import os
from datetime import datetime

# 공공데이터포털에서 제공하는 식품(음식)영양성분 API URL
url = 'http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api'

# API 요청에 필요한 파라미터
params = {
    'serviceKey': '서비스 키',  # 보안 인증키, 실제 사용 시에만 입력
    'numOfRows': '100',  # 한 페이지에 가져올 데이터 개수
    'type': 'json',  # 응답 데이터 형식 (json, xml 중 선택)
    'pageNo': '1'  # 초기 페이지 번호 설정
    # 이 외 명시하지 않은 파라미터에선 모든 데이터를 가져온다.
}

# 데이터를 저장할 파일 경로
file_path = 'D:/student/LHT/works/crawling/dish_data.json'
log_file_path = 'D:/student/LHT/works/crawling/dish_update_log.txt'

# 모든 페이지의 데이터를 저장할 리스트 초기화
all_items = []

# 첫 번째 페이지 요청을 보내 데이터를 가져옴. 데이터에는 전체 데이터 개수인 totalCount가 포함되어 있음
response = requests.get(url, params = params)

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

# 새로운 데이터를 JSON 형식으로 변환
new_data_json = json.dumps(all_items, ensure_ascii = False, indent = 4)

# 기존 파일이 있는지 확인하고, 있으면 이전 데이터를 읽어옴
if os.path.exists(file_path):
    with open(file_path, 'r', encoding = 'utf-8') as f:
        old_data_json = f.read()

    # 이전 데이터와 새로운 데이터를 비교
    if new_data_json == old_data_json:
        print("데이터가 갱신되지 않았습니다.")
        update_status = "No Update"
    else:
        print("데이터가 갱신되었습니다.")
        update_status = "Updated"
        # 새로운 데이터를 파일로 저장
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_data_json)
else:
    # 파일이 없으면 새로운 데이터를 바로 저장
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_data_json)
    update_status = "First Time Update"
    print("처음으로 데이터를 저장했습니다.")

# 갱신 여부를 기록하는 로그 파일에 작성
if update_status != "No Update":
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    with open(log_file_path, 'a', encoding='utf-8') as log_file:
        log_file.write(f"{current_time} - {update_status} - {response_json['response']['header']['resultMsg']} - {total_count} items\n")
    print("저장 혹은 갱신된 날짜와 시간이 로그 파일에 기록되었습니다.")