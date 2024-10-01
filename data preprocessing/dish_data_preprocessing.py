import os
import pandas as pd

# 파일 경로 설정(PC에 따라 알맞은 경로 설정)
original_file_path = 'D:/student/LHT/works/crawling/dish_data.json'
preprocessed_file_path = 'D:/student/LHT/works/crawling/preprocessed_dish_data.json'

# 파일의 수정 시간을 변수에 저장. 전처리 된 파일이 없을 경우 0을 저장하여 전처리 진행.
original_mtime = os.path.getmtime(original_file_path)
preprocessed_mtime = os.path.getmtime(preprocessed_file_path) if os.path.exists(preprocessed_file_path) else 0

# 원본 파일이 전처리 파일보다 최신일 경우에만 코드 실행
if original_mtime > preprocessed_mtime:
    # JSON 파일 읽기. 값이 float형으로 자동 변환되지 않도록 데이터 타입을 str로 명시.
    df = pd.read_json(original_file_path, dtype={
        'foodCd': str,
        'foodNm': str,
        'foodLv3Nm': str,
        'enerc': str,
        'prot': str,
        'fatce': str,
        'chocdf': str,
        'foodSize': str
    })

    # 차례대로 '식품코드', '식품명', '식품대분류명', '에너지(kcal)', '단백질(g)', '지방(g)', '탄수화물(g)', '식품중량' 열만 선택
    df_preprocessed = df[['foodCd', 'foodNm', 'foodLv3Nm', 'enerc', 'prot', 'fatce', 'chocdf', 'foodSize']]

    # 처리한 데이터프레임을 JSON 파일로 저장
    df_preprocessed.to_json(preprocessed_file_path, orient='records', force_ascii=False, indent=4)
    print("갱신된 원본 데이터가 전처리되어 저장되었습니다.")
else:
    print("원본 데이터가 변경되지 않았습니다.")