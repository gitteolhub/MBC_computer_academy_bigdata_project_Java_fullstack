import requests
import json

url = 'http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api'
params = {'serviceKey' : '보안 인증키 이므로 사용할 때만 입력', 'pageNo' : '1', 'numOfRows' : '100', 'type' : 'json', 'foodCd' : '', 'foodNm' : '', 'dataCd' : '', 'typeNm' : '', 'foodOriginCd' : '', 'foodOriginNm' : '', 'foodLv3Cd' : '', 'foodLv3Nm' : '', 'foodLv4Cd' : '', 'foodLv4Nm' : '', 'foodLv5Cd' : '', 'foodLv5Nm' : '', 'foodLv6Cd' : '', 'foodLv6Nm' : '', 'foodLv7Cd' : '', 'foodLv7Nm' : '', 'nutConSrtrQua' : '', 'enerc' : '', 'water' : '', 'prot' : '', 'fatce' : '', 'ash' : '', 'chocdf' : '', 'sugar' : '', 'fibtg' : '', 'ca' : '', 'fe' : '', 'p' : '', 'k' : '', 'nat' : '', 'vitaRae' : '', 'retol' : '', 'cartb' : '', 'thia' : '', 'ribf' : '', 'nia' : '', 'vitc' : '', 'vitd' : '', 'chole' : '', 'fasat' : '', 'fatrn' : '', 'srcCd' : '', 'srcNm' : '', 'foodSize' : '', 'restNm' : '', 'dataProdCd' : '', 'dataProdNm' : '', 'crtYmd' : '', 'crtrYmd' : '', 'instt_code' : '', 'instt_nm' : '' }

response = requests.get(url, params=params)
print(response.content)

# JSON 파일로 저장
if response.status_code == 200:  # 성공적인 응답인 경우
    with open('D:/student/LHT/works/crawling/food_data.json', 'w', encoding='utf-8') as f:
        json.dump(response.json(), f, ensure_ascii=False, indent=4)
    print("데이터가 nutrition_data.json 파일로 저장되었습니다.")
else:
    print(f"API 요청 실패: 상태 코드 {response.status_code}")
    print(f"에러 메시지: {response.text}")