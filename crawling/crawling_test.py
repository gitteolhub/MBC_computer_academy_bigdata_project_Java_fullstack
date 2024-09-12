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
    # 아래는 식품 정보에 대한 다양한 검색 조건들
    'foodCd': '',  # 식품코드
    'foodNm': '',  # 식품명
    'dataCd': '',  # 데이터구분코드
    'typeNm': '',  # 데이터구분명
    'foodOriginCd': '',  # 식품기원코드
    'foodOriginNm': '',  # 식품기원명
    'foodLv3Cd': '',  # 식품대분류코드
    'foodLv3Nm': '',  # 식품대분류명
    'foodLv4Cd': '',  # 대표식품코드
    'foodLv4Nm': '',  # 대표식품명
    'foodLv5Cd': '',  # 식품중분류코드
    'foodLv5Nm': '',  # 식품중분류명
    'foodLv6Cd': '',  # 식품소분류코드
    'foodLv6Nm': '',  # 식품소분류명
    'foodLv7Cd': '',  # 식품세분류코드
    'foodLv7Nm': '',  # 식품세분류명
    'nutConSrtrQua': '',  # 영양성분함량기준량
    'enerc': '',  # 에너지(kcal)
    'water': '',  # 수분(g)
    'prot': '',  # 단백질(g)
    'fatce': '',  # 지방(g)
    'ash': '',  # 회분(g)
    'chocdf': '',  # 탄수화물(g)
    'sugar': '',  # 당류(g)
    'fibtg': '',  # 식이섬유(g)
    'ca': '',  # 칼슘(mg)
    'fe': '',  # 철(mg)
    'p': '',  # 인(mg)
    'k': '',  # 칼륨(mg)
    'nat': '',  # 나트륨(mg)
    'vitaRae': '',  # 비타민 A(μg RAE)
    'retol': '',  # 레티놀(μg)
    'cartb': '',  # 베타카로틴(μg)
    'thia': '',  # 티아민(mg)
    'ribf': '',  # 리보플라빈(mg)
    'nia': '',  # 나이아신(mg)
    'vitc': '',  # 비타민 C(mg)
    'vitd': '',  # 비타민 D(μg)
    'chole': '',  # 콜레스테롤(mg)
    'fasat': '',  # 포화지방산(g)
    'fatrn': '',  # 트랜스지방산(g)
    'srcCd': '',  # 출처코드
    'srcNm': '',  # 출처명
    'foodSize': '',  # 식품중량
    'restNm': '',  # 업체명
    'dataProdCd': '',  # 데이터생성방법코드
    'dataProdNm': '',  # 데이터생성방법명
    'crtYmd': '',  # 데이터생성일자
    'crtrYmd': '',  # 데이터기준일자
    'instt_code': '',  # 제공기관코드
    'instt_nm': ''  # 제공기관기관명
}

# API에 요청을 보내고 응답 받음
response = requests.get(url, params=params)

# 응답 결과를 출력 (디버깅을 위해)
print(response.content)

# 응답 상태 코드가 200이면 성공, JSON 파일로 저장
if response.status_code == 200:
    with open('D:/student/LHT/works/crawling/food_data.json', 'w', encoding='utf-8') as f:
        json.dump(response.json(), f, ensure_ascii=False, indent=4)
    print("데이터가 food_data.json 파일로 저장되었습니다.")
else:
    # 200이 아니면 에러 발생, 에러 메시지 출력
    print(f"API 요청 실패: 상태 코드 {response.status_code}")
    print(f"에러 메시지: {response.text}")