import os
import pandas as pd

# 파일 경로 설정(PC에 따라 알맞은 경로 설정)
preprocessed_dish_data_file_path = 'D:/student/LHT/works/crawling/preprocessed_dish_data.json'
preprocessed_material_data_file_path = 'D:/student/LHT/works/crawling/preprocessed_material_data.json'
food_data_file_path = 'D:/student/LHT/works/crawling/food_data.json'

# 파일의 수정 시간을 변수에 저장. 병합된 파일이 없을 경우 0을 저장하여 병합 진행.
preprocessed_dish_data_mtime = os.path.getmtime(preprocessed_dish_data_file_path)
preprocessed_material_data_mtime = os.path.getmtime(preprocessed_material_data_file_path)
food_data_mtime = os.path.getmtime(food_data_file_path) if os.path.exists(food_data_file_path) else 0

# 전처리 파일 중 하나라도 변경되었을 시 코드 실행
if (preprocessed_dish_data_mtime or preprocessed_material_data_mtime) > food_data_mtime:
    # JSON 파일 읽기
    preprocessed_dish_data_df = pd.read_json(preprocessed_dish_data_file_path)
    preprocessed_material_data_df = pd.read_json(preprocessed_material_data_file_path)

    # 식품(음식)과 식품(원재료) 병합
    df_food = pd.concat([preprocessed_dish_data_df, preprocessed_material_data_df], ignore_index=True)

    # 병합한 데이터프레임을 JSON 파일로 저장
    df_food.to_json(food_data_file_path, orient='records', force_ascii=False, indent=4)
    print("갱신된 전처리 데이터가 병합되어 저장되었습니다.")
else:
    print("전처리된 데이터가 변경되지 않았습니다.")