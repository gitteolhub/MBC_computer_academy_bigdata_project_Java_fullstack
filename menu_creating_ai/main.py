import datetime
import json
import random as ran
import threading as th
from os.path import exists

import pandas as pd

import ai_ctrl

#업데이트가 필요한 사용자의 선택 정보 파일 경로
chosenFoodMenu_path = 'swgic/src/main/resources/JsonDataFiles/chosenFoodMenu_Json.json'

#모든 사용자에 대한 정보 파일 경로
allMembers_path = 'swgic/src/main/resources/JsonDataFiles/AllMembersDump.json'

#과일류 식품 데이터와 그 외에 식품 데이터를 병합한 json 데이터 파일의 경로
data_path = 'Resources/food_data.json'

#과일류 식품 데이터만 다룬 json 데이터 파일 경로
fruit_data_path = 'Resources/preprocessed_food_data.json'

#데이터들을 문자열 형태로 저장하는 변수
data_str = ''

#모든 사용자들의 아이디 리스트
users_id = []

#사용자에 의한 식단 학습 데이터들을 저장하는 텍스트 파일 경로
save_file_path = 'Resources/Saved_files/user_data.txt'
#가중치 저장 파일 경로
file_path = 'Resources/Saved_files/weights.txt'
#사용자에 의한 식단 학습 데이터들을 저장하는 변수
user_reviews = [[],[]]

#
users_chosen_data = [{'tonicjh':0}, {'swgic':0}]

#쓰레드를 멈추는 이벤트 변수
stop_event = th.Event()

#배열 변수의 내용이 없는지 체크하는 함수(없으면 False, 있다면 True 반환)
def check_list_null(arr):
    if len(arr) > 0:
        return True
    else:
        return False

def count_and_print(c, t):
    delta_t = datetime.datetime.now() - t
    s = str(c + 1) + '번째 : ' + str(delta_t)
    print(s)

    return [c + 1, datetime.datetime.now()]

#json 데이터 파일 경로를 입력하여 데이터를 읽는 함수
def load_data_from_json_file(path):
    with open(path, 'r', encoding='utf-8') as json_file:
        data = json.load(json_file)
        return data

#문자열 형태의 데이터를 json 형식으로 읽는 함수
def load_data_from_json_string(s):
    data = json.loads(s)

    return data

json_data = load_data_from_json_file(data_path)
in_data = str(json_data).replace('\'', '\"')
input_data = json.loads(in_data)

#json 데이터 파일로부터 데이터를 읽는 함수
def get_data():
    js_data = load_data_from_json_file(data_path)
    _data = str(js_data).replace('\'', '\"')
    result = json.loads(_data)
    return result

def get_users_data():
    all_users_data_json_path = allMembers_path
    user_datas = load_data_from_json_file(all_users_data_json_path)
    _data = str(user_datas).replace('\'', '\"')
    result = json.loads(_data)
    return result

def get_users_food_choice():
    all_users_data_json_path = chosenFoodMenu_path
    chosen_food_datas = load_data_from_json_file(all_users_data_json_path)
    _data = str(chosen_food_datas).replace('\'', '\"')
    result = json.loads(_data)

    #result.

    return result

#정리가 안된 식품명을 정리하는 함수
def food_naming(n):
    under_keyword_list = ['오이', '감자', '미역줄기', '표고버섯', '건표고버섯', '느타리버섯', '새송이버섯', '가지', '씨', '김치', '깻잎', '초밥', '아이스티', '국밥', '숙주', '파래', '부추', '식초', '채소', '취나물', '콩나물']
    space_keyword_list = ['제거', '포함', '식혜', '스무디']
    order_keyword_list = ['얼린것', '삶은것', '자몽차', '레몬차', '/', '씨 ']
    delete_keyword_list = ['쌀', '양념장', '생것', '숙주_채소', '부추_채소']

    name = n
    for k in delete_keyword_list:
        x = name.split(' ')[0].split('(')[0]
        if '_' + str(k) == x[len(x) - len(k) - 1:len(x)]:
            name = name.replace('_' + str(k), '')

    _sp = name.split(' ')[0].split('(')[0].split('_')
    sp = []

    for k in under_keyword_list:
        key_count = 0
        for p in range(len(_sp)):
            if k in _sp[p]:
                if p > 0:
                    if key_count > 0:
                        key_count += 1
                    else:
                        key_count += 1
                        if not _sp[p] in sp:
                            sp.append(_sp[p])
                else:
                    key_count += 1
                    if not _sp[p] in sp:
                        sp.append(_sp[p])
            else:
                if p > 0:
                    if not _sp[p] in sp:
                        sp.append(_sp[p])
                else:
                    break

    if len(sp) == 0:
        sp = _sp

    contains_keys = ''
    for keys in order_keyword_list:
        if keys in name:
            for x in sp:
                if keys.replace(' ', '') in x:
                    contains_keys = keys.replace(' ', '')

    if len(contains_keys) > 0:
        if len(sp) > 2:
            if contains_keys == '/':
                for x in sp:
                    if '/' in x:
                        sp.remove(x)
                        break

            re_name = str(sp[0]) + '_' + str(sp[1])
            if contains_keys == '/':
                re_name = re_name.replace('_', '/')
            for x in range(2, len(sp)):
                if not str(sp[x]) in re_name:
                    re_name += '_' + str(sp[x])

            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])

            if len(name.split(' ')) > 1:
                for q in range(1, len(name.split(' '))):
                    if len(name.split(' ')[q]) > 0:
                        for k in space_keyword_list:
                            if not k in str(name.split(' ')[q]):
                                if not str(name.split(' ')[q]) in re_name:
                                    re_name += ' ' + str(name.split(' ')[q])
            return re_name
        elif len(sp) > 1:
            re_name = str(sp[0]) + '_' + str(sp[1])

            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])

            if len(name.split(' ')) > 1:
                for q in range(1, len(name.split(' '))):
                    if len(name.split(' ')[q]) > 0:
                        for k in space_keyword_list:
                            if not k in str(name.split(' ')[q]):
                                if not str(name.split(' ')[q]) in re_name:
                                    re_name += ' ' + str(name.split(' ')[q])
            return re_name
        else:
            re_name = str(sp[0])
            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])
            return re_name
    else:
        if len(sp) > 2:
            re_name = str(sp[1]) + '_' + str(sp[0])
            for x in range(2, len(sp)):
                if not str(sp[x]) in re_name:
                    re_name += '_' + str(sp[x])

            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])

            if len(name.split(' ')) > 1:
                for q in range(1, len(name.split(' '))):
                    if len(name.split(' ')[q]) > 0:
                        for k in space_keyword_list:
                            if not k in str(name.split(' ')[q]):
                                if not str(name.split(' ')[q]) in re_name:
                                    re_name += ' ' + str(name.split(' ')[q])
            return re_name
        elif len(sp) > 1:
            re_name = str(sp[1]) + '_' + str(sp[0])

            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])
            
            
            if len(name.split(' ')) > 1:
                for q in range(1, len(name.split(' '))):
                    if len(name.split(' ')[q]) > 0:
                        for k in space_keyword_list:
                            if not k in str(name.split(' ')[q]):
                                if not str(name.split(' ')[q]) in re_name:
                                    re_name += ' ' + str(name.split(' ')[q])
            return re_name
        else:
            re_name = str(sp[0])
            if len(name.split(' ')[0].split('(')) > 1:
                re_name += '(' + str(name.split(' ')[0].split('(')[1])
            return re_name

# nm = '참외_씨 포함_생것'
# print('test : ' + str(food_naming(nm)))

#실수를 다시 식품명으로 변환하는 함수
def id_to_food(num, _df):
    for _i in range(16):
        num = num * 10.0
    num = str(int(num))
    num = 'D' + num[:3] + '-' + num[3:len(num) - 4] + '-' + num[len(num) - 4:]
    if len(_df['foodNm'].where(_df['foodCd'] == str(num)).dropna()) > 0:
        return _df['foodNm'].where(_df['foodCd'] == str(num)).dropna().values[0]
    return ''

#식품명을 학습 데이터로 사용 가능하도록 실수로 변환하는 함수
def name_to_id(n, _df):
    is_name = True
    name = str(n)
    if is_name:
        if len(_df['foodCd'].where(_df['foodNm'] == str(name)).dropna()) > 0:
            id_num = _df['foodCd'].where(_df['foodNm'] == str(name)).dropna().values[0]
            result = str(id_num).replace('D', '').replace('R', '').replace('-', '')
            result = int(result)
            for _i in range(16):
                result = result / 10.0
            return float(result)
        else:
            return 0.0

    return n

#저장된 가중치와 바이오스 데이터들을 파일로부터 읽는 함수
def read_weights_file():
    f = open(file_path, 'r')
    read_str = ''
    while True:
        line = f.readline()
        if not line: break
        read_str += line.replace(' ', '').replace('array(', '').replace(')', '').replace('\n', '').replace('\r', '')

    sp = read_str.split('|')
    result = sp

    f.close()

    return result

#기준 칼로리 및 영양소를 입력하여, 적합한 식품 리스트를 반환하는 함수
def get_food_by_kcal(spec, cal, choc, protein, province):
    food_result = []
    for x in input_data:
        if dict(x)['foodLv3Nm'] == spec:
            if float(dict(x)['enerc']) <= cal:
                if len(dict(x)['chocdf']) > 0 and len(dict(x)['prot']) > 0 and len(dict(x)['fatce']) > 0:
                    is_choc = float(dict(x)['chocdf'].replace('g', ''))
                    is_protein = float(dict(x)['prot'].replace('g', ''))
                    is_province = float(dict(x)['fatce'].replace('g', ''))

                    if is_choc <= choc:
                        if is_protein <= protein:
                            if is_province <= province:
                                food_result.append(dict(x)['foodNm'])
                    else:
                        if choc > 0.0:
                            how_much = int(is_choc / choc + 0.9)
                            if 6 >= how_much > 1:
                                if is_protein <= protein:
                                    if is_province <= province:
                                        food_result.append('{0} (1/{1})'.format(dict(x)['foodNm'], str(how_much)))
    return food_result

#하루 권장 섭취 열량을 계산하는 함수
def calc_kcal_per_day(age, weight, height, exercise_per_day):
    if age >= 19:
        if height >= 160:
            main_weight = (height - 100) * 0.9
            b_man = weight / (height / 100.0) * (height / 100.0)
            if b_man >= 30.0:
                if exercise_per_day == 0:
                    return main_weight * 30 - 700
                elif exercise_per_day == 1:
                    return main_weight * 35 - 650
                else:
                    return main_weight * 40 - 600
            elif b_man <= 25.0:
                if exercise_per_day == 0:
                    return main_weight * 30
                elif exercise_per_day == 1:
                    return main_weight * 35
                else:
                    return main_weight * 40
            else:
                if exercise_per_day == 0:
                    return main_weight * 30 - 600
                elif exercise_per_day == 1:
                    return main_weight * 35 - 550
                else:
                    return main_weight * 40 - 500
        elif height > 150:
            main_weight = (height - 150) * 0.5 + 50.0
            b_man = weight / (height / 100.0) * (height / 100.0)
            if b_man >= 30.0:
                if exercise_per_day == 0:
                    return main_weight * 30 - 700
                elif exercise_per_day == 1:
                    return main_weight * 35 - 650
                else:
                    return main_weight * 40 - 600
            elif b_man <= 25.0:
                if exercise_per_day == 0:
                    return main_weight * 30
                elif exercise_per_day == 1:
                    return main_weight * 35
                else:
                    return main_weight * 40
            else:
                if exercise_per_day == 0:
                    return main_weight * 30 - 600
                elif exercise_per_day == 1:
                    return main_weight * 35 - 550
                else:
                    return main_weight * 40 - 500
        else:
            main_weight = height - 100.0
            b_man = weight / (height / 100.0) * (height / 100.0)
            if b_man >= 30.0:
                if exercise_per_day == 0:
                    return main_weight * 30 - 700
                elif exercise_per_day == 1:
                    return main_weight * 35 - 650
                else:
                    return main_weight * 40 - 600
            elif b_man <= 25.0:
                if exercise_per_day == 0:
                    return main_weight * 30
                elif exercise_per_day == 1:
                    return main_weight * 35
                else:
                    return main_weight * 40
            else:
                if exercise_per_day == 0:
                    return main_weight * 30 - 600
                elif exercise_per_day == 1:
                    return main_weight * 35 - 550
                else:
                    return main_weight * 40 - 500
    else:
        return 1000 + age * 100

#나이별로 계산된 하루 권장 열량을 입력하여 식품교환표를 기반으로 하루에 식품군당 교환 가능 단위를 구하는 함수
def calc_amount_per_day(age, cal):
    if age <= 18:
        table = [[5, 1, 2, 6, 2, 2, 1],
                 [5, 1, 2, 6, 3, 2, 2],
                 [6, 1, 2, 6, 3, 2, 2],
                 [6, 1, 3, 6, 3, 2, 2],
                 [7, 1, 3, 7, 4, 2, 2],
                 [7, 1, 3, 7, 4, 2, 2],
                 [8, 1, 3, 7, 4, 2, 2],
                 [9, 1, 3, 7, 4, 2, 2],
                 [9, 2, 3, 7, 5, 2, 2],
                 [10, 2, 3, 7, 5, 2, 2],
                 [10, 2, 3, 7, 5, 3, 2],
                 [10, 2, 4, 7, 5, 3, 2],
                 [11, 2, 4, 7, 5, 3, 2],
                 [12, 2, 4, 7, 5, 3, 2],
                 [13, 2, 4, 7, 5, 3, 2],
                 [14, 2, 4, 7, 5, 3, 2],
                 [14, 2, 5, 7, 6, 3, 2]]

        return table[age - 1]
    else:
        table = [[4, 1, 2, 7, 2, 1, 1],
                 [5, 1, 2, 7, 2, 1, 1],
                 [5, 1, 3, 6, 3, 1, 1],
                 [6, 1, 3, 6, 3, 1, 1],
                 [7, 1, 3, 6, 3, 1, 1],
                 [7, 2, 3, 7, 4, 1, 1],
                 [8, 2, 3, 7, 4, 1, 1],
                 [8, 2, 3, 7, 4, 1, 2],
                 [8, 2, 3, 7, 4, 2, 2],
                 [9, 2, 3, 7, 4, 2, 2],
                 [10, 2, 3, 7, 4, 2, 2],
                 [10, 2, 4, 7, 4, 2, 2],
                 [11, 2, 4, 7, 4, 2, 2],
                 [11, 3, 4, 8, 5, 2, 2],
                 [12, 3, 4, 8, 5, 2, 2],
                 [13, 3, 4, 8, 5, 2, 2],
                 [13, 3, 5, 8, 5, 2, 2],
                 [13, 3, 5, 9, 6, 2, 3],
                 [14, 3, 5, 9, 6, 2, 3]]

        return table[int((cal / 100) - 10)]


#교환단위에 의한 식사 가능한 곡류군 식품 리스트를 반환하는 함수
def get_rice_per_day(debug, to_name, _df):
    sp = ['밥류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[0][0] * 100, amount_per_day[0][0] * 23, amount_per_day[0][0] * 2, amount_per_day[0][0] * 1000)
        _lunch = get_food_by_kcal(x, amount_per_day[0][1] * 100, amount_per_day[0][1] * 23, amount_per_day[0][1] * 2, amount_per_day[0][1] * 1000)
        _dinner = get_food_by_kcal(x, amount_per_day[0][2] * 100, amount_per_day[0][2] * 23, amount_per_day[0][2] * 2, amount_per_day[0][2] * 1000)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
    if debug:
        print('Lunch Option : ' + str(food[0]))
    return food


#교환단위에 의한 식사 가능한 육류군 식품 리스트를 반환하는 함수
def get_meat_per_day(debug, to_name, _df):
    sp = ['수·조·어·육류', '구이류', '조림류', '장아찌·절임류', '두류, 견과 및 종실류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[2][0] * 50, amount_per_day[2][0] * 1000, amount_per_day[2][0] * 8, amount_per_day[2][0] * 5)
        _lunch = get_food_by_kcal(x, amount_per_day[2][1] * 50, amount_per_day[2][1] * 1000, amount_per_day[2][1] * 8, amount_per_day[2][1] * 5)
        _dinner = get_food_by_kcal(x, amount_per_day[2][2] * 50, amount_per_day[2][2] * 1000, amount_per_day[2][2] * 8, amount_per_day[2][2] * 5)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
    if debug:
        print('Lunch Option : ' + str(food[1]))
    return food

#교환단위에 의한 식사 가능한 채소군 식품 리스트를 반환하는 함수
def get_vegetable_per_day(debug, to_name, _df):
    sp = ['생채·무침류', '나물·숙채류', '김치류', '볶음류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[3][0] * 20, amount_per_day[3][0] * 3, amount_per_day[3][0] * 2, amount_per_day[3][0] * 1000)
        _lunch = get_food_by_kcal(x, amount_per_day[3][1] * 20, amount_per_day[3][1] * 3, amount_per_day[3][1] * 2, amount_per_day[3][1] * 1000)
        _dinner = get_food_by_kcal(x, amount_per_day[3][2] * 20, amount_per_day[3][2] * 3, amount_per_day[3][2] * 2, amount_per_day[3][2] * 1000)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
    if debug:
        print('Lunch Option : ' + str(food[1]))
    return food

#교환단위에 의한 식사 가능한 지방군 식품 리스트를 반환하는 함수
def get_province_per_day(debug, to_name, _df):
    sp = ['장류, 양념류', '튀김류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[4][0] * 45, amount_per_day[4][0] * 1000, amount_per_day[4][0] * 1000, amount_per_day[4][0] * 5)
        _lunch = get_food_by_kcal(x, amount_per_day[4][1] * 45, amount_per_day[4][1] * 1000, amount_per_day[4][1] * 1000, amount_per_day[4][1] * 5)
        _dinner = get_food_by_kcal(x, amount_per_day[4][2] * 45, amount_per_day[4][2] * 1000, amount_per_day[4][2] * 1000, amount_per_day[4][2] * 5)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
    if debug:
        print('Lunch Option : ' + str(food[1]))
    return food

#교환단위에 의한 식사 가능한 우유군 식품 리스트를 반환하는 함수
def get_milk_per_day(debug, to_name, _df):
    sp = ['유제품류 및 빙과류', '음료 및 차류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[5][0] * 90, amount_per_day[5][0] * 11, amount_per_day[5][0] * 1100, amount_per_day[5][0] * 1100)
        _lunch = get_food_by_kcal(x, amount_per_day[5][1] * 90, amount_per_day[5][1] * 11, amount_per_day[5][1] * 1100, amount_per_day[5][1] * 1100)
        _dinner = get_food_by_kcal(x, amount_per_day[5][2] * 90, amount_per_day[5][2] * 11, amount_per_day[5][2] * 1100, amount_per_day[5][2] * 1100)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
    if debug:
        print('Lunch Option : ' + str(food[1]))
    return food

#교환단위에 의한 식사 가능한 과일군 식품 리스트를 반환하는 함수
def get_fruit_per_day(debug, to_name, _df):
    sp = ['과일류']
    food = [[], [], []]
    for x in sp:
        _breakfast = get_food_by_kcal(x, amount_per_day[6][0] * 50, amount_per_day[6][0] * 12, amount_per_day[6][0] * 1000, amount_per_day[6][0] * 1000)
        _lunch = get_food_by_kcal(x, amount_per_day[6][1] * 50, amount_per_day[6][1] * 12, amount_per_day[6][1] * 1000, amount_per_day[6][1] * 1000)
        _dinner = get_food_by_kcal(x, amount_per_day[6][2] * 50, amount_per_day[6][2] * 12, amount_per_day[6][2] * 1000, amount_per_day[6][2] * 1000)
        if to_name:
            for p in range(len(_breakfast)):
                _breakfast[p] = name_to_id(_breakfast[p], _df)
            for p in range(len(_lunch)):
                _lunch[p] = name_to_id(_lunch[p], _df)
            for p in range(len(_dinner)):
                _dinner[p] = name_to_id(_dinner[p], _df)
            
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
        else:
            food = [food[0] + _breakfast, food[1] + _lunch, food[2] + _dinner]
            
    if debug:
        print('Lunch Option : ' + str(food[1]))
    return food

#리스트중 랜덤한 원소를 반환하는 함수
def get_random_from_array(arr):
    if len(arr) > 0:
        index = ran.randint(0, len(arr) - 1)
        return arr[index]
    else:
        return ''


#식품의 열량을 해당 식품의 중량으로 변환하는 함수
def convert_kcal_to_gram(name, cal):
    if '(' in name:
        name = name[:len(name) - 6]

    for x in input_data:
        if dict(x)['foodNm'] == name:
            if len(dict(x)['enerc']) > 0 and len(dict(x)['foodSize']) > 0:
                _gram = 0.0
                if dict(x)['foodSize'] != 'null':
                    _gram = float(dict(x)['foodSize'].replace('g', '').replace('m', '').replace('l', ''))
                _kcal = int(int(dict(x)['enerc']) / 100 * _gram)
                return int(cal / _kcal * _gram)
            else:
                return 0
    return 0

#반환된 식단 메뉴를 출력하는 함수
def print_meal(meal):
    for _i in range(2, len(meal)):
        if len(str(meal[_i])) > 0:
            _menu_str = ''
            for _j in range(len(str(meal[_i]))):
                _menu_str += str(meal[_i][_j])
                if _j < len(str(meal[_i])) - 1:
                    if len(str(meal[_i][_j])) > 0:
                        _menu_str += ', '
            print('추천 식단 : ' + str(_menu_str))

#지정된 갯수만큼 식단을 반환하는 함수(returns_id : 식품명 또는 아이디 형태로 반환할지 지정하는 매개변수)
def find_meal(c, returns_id, _df):
    debug_process = False
    meal = [[my_age], [my_gender]]
    for x in range(c):
        if returns_id:

            ind = 1
            rice = rices_id[ind][ran.randint(0, len(rices_id[ind]) - 1)]
            meat = meats_id[ind][ran.randint(0, len(meats_id[ind]) - 1)]
            vegetable = vegetables_id[ind][ran.randint(0, len(vegetables_id[ind]) - 1)]
            milk = milks_id[ind][ran.randint(0, len(milks_id[ind]) - 1)]
            fruit = fruits_id[ind][ran.randint(0, len(fruits_id[ind]) - 1)]

            meal.append([rice, meat, vegetable, fruit, milk])
        else:
            ind = 1
            rice = rices[ind][ran.randint(0, len(rices[ind]) - 1)]
            meat = meats[ind][ran.randint(0, len(meats[ind]) - 1)]
            vegetable = vegetables[ind][ran.randint(0, len(vegetables[ind]) - 1)]
            milk = milks[ind][ran.randint(0, len(milks[ind]) - 1)]
            fruit = fruits[ind][ran.randint(0, len(fruits[ind]) - 1)]

            meal.append([rice, meat, vegetable, fruit, milk])

        if debug_process:
            print_meal(meal)

    return meal

#문자열 형태의 리스트를 리스트 형태로 변환하는 함수
def str_to_list(s, to_id, _df):
    if '[[[' in s:
        result0 = []
        sp = s[1:len(s) - 1].split('],[')
        if len(sp) > 1:
            for x in sp:
                res = []
                sp1 = str(x).replace('[', '').replace(']', '').split(',')
                for y in sp1:
                    res1 = []
                    sp2 = str(y).replace('[', '').replace(']', '').split(',')
                    for z in sp2:
                        if to_id:
                            res1.append(float(name_to_id(z, _df)))
                        else:
                            print(z)
                            res1.append(z)
                    res.append(res1)
                result0.append(res)
            return result0
        else:
            for x in sp:
                res = []
                sp1 = str(x).replace('[', '').replace(']', '').split(',')
                for y in sp1:
                    if to_id:
                        res.append(float(name_to_id(y, _df)))
                    else:
                        res.append(y)
                result0.append(res)
            return result0
    elif '[[' in s:
        result0 = []
        sp = s[1:len(s) - 1].replace(', ', ',').split('],[')
        for x in sp:
            res = []
            sp1 = str(x).replace('[', '').replace(']', '').split(',')
            for y in sp1:
                if to_id:
                    res.append(float(name_to_id(y, _df)))
                else:
                    res.append(y)
            result0.append(res)
        return result0
    elif '[' in s:
        result0 = []
        if s[1:len(s) - 1] != '':
            sp = s[1:len(s) - 1].split(',')
            for x in sp:
                if to_id:
                    result0.append(float(name_to_id(x, _df)))
                else:
                    result0.append(x)
            return result0
        else:
            return result0
    else:
        result0 = []
        return result0

# _df0 = pd.read_json(data_path)
# print('str_to_list results : ' + str(str_to_list("[[콩밥_완두콩, 감자조림, 얼갈이배추김치, 살구_생것, 스무디_얼음수박], [리소토/리조또_간편조리세트_베이컨버섯크림리조또, 버섯구이_새송이버섯, 겉절이_치커리, 귤_임온주_생것, 커피_헥사메리카노 핫(HOT)]]", False, _df0)))

#식단을 추천하는 인공지능을 학습시키는 함수
def train_ai(train_count, _hidden_layer_count, _hidden_count):

    _save_time = datetime.datetime.now()
    _save_time2 = datetime.datetime.now()

    _df2 = pd.read_json(data_path)
    _save_time = _save_time + datetime.timedelta(days=1)

    _saved_data = read_weights_file()
    _save_time2 = _save_time + datetime.timedelta(seconds=10)

    while not stop_event.is_set():
        if _save_time == datetime.datetime.now():
            _df2 = pd.read_json(data_path)
            _save_time = _save_time + datetime.timedelta(days=1)

        if _save_time2 == datetime.datetime.now():
            _saved_data = read_weights_file()
            _save_time2 = _save_time + datetime.timedelta(seconds=10)

        _data_sp = load_user_choice(_df2, 'Resources/Saved_files/' + str(my_id) + '.txt')
        if len(_data_sp) > 0:
            _data = []
            if len(str_to_list(_data_sp[0], True, _df2)) > 0:
                _data = [[str_to_list(_data_sp[0], True, _df2)], str_to_list(_data_sp[1], True, _df2)]

                input_learning_data = []
                for p in _data[0]:
                    input_learning_data.append(p)

                output_learning_data = []
                for q in _data[1]:
                    answer = q
                    output_learning_data.append(float(answer))

                ai_ctrl.train(train_count, input_learning_data, _hidden_layer_count, _hidden_count, output_learning_data, _df2, _saved_data)

#인공지능에 입력할 식단 데이터를 생성 및 입력하여, 적합한지 판별하는 함수
def create_menu_from_ai(_df, _saved_data, _hidden_layer_count, _hidden_count):
    #인공지능에 입력할 식단 데이터 갯수
    menu_count = 10
    #뽑아낼 식단 갯수
    result_count = 2

    #과정을 출력할지 지정하는 변수
    debug_process = True
    if debug_process:
        print('\n====================================================================================================\n식단 짜는 중...')

    #인공지능에 입력할 아이디 형태의 식단 데이터
    _test_data = find_meal(menu_count, True, _df)

    #인공지능이 판단한 후 표시할 식품명 형태의 식단 데이터
    _test_data2 = find_meal(menu_count, False, _df)

    if debug_process:
        print('식품군이 골고루 들어간 당뇨병 식단만으로 필터링 중...')

    #인공지능의 판단에 의하여 반환된 식단 데이터들
    ai_result = []

    #사용자가 선택한 학습 데이터가 존재하는지 확인
    if len(user_reviews[1]) > 0:

        #식단들의 적합률 퍼센트를 저장하는 변수
        ai_foods_percent = []

        #뽑아낼 식단 갯수 만큼 식단마다 나온 적합률 퍼센트를 순위별로 저장하는 변수
        max_like_percent = []
        for x in range(result_count):
            max_like_percent.append(0.0)

        #식단들중에 사용자의 마음에 들만한 식단을 뽑아내기 위해 적합률을 인공지능에게 판별시키는 반복문
        for x in range(2, len(_test_data)):
            is_ok_percent = ai_ctrl.detect_favorite_menu(_hidden_layer_count, _hidden_count, _test_data[x], _df, _saved_data)
            ai_foods_percent.append(is_ok_percent)

            #print('ai_foods_percent : ' + str(ai_foods_percent) + ', max_like_percent : ' + str(max_like_percent))

            #식단들의 적합률 퍼센트들의 최고기록을 갱신시키는 반복문
            for q in range(len(max_like_percent)):
                if is_ok_percent > max_like_percent[q]:
                    for p in range(len(max_like_percent) - 1, q, -1):
                        max_like_percent[p] = max_like_percent[p - 1]
                    max_like_percent[q] = is_ok_percent
                    break


        for p in range(len(max_like_percent)):
            ai_result.append(_test_data2[2 + ai_foods_percent.index(max_like_percent[p])])

        food_menu = []

        for z in range(len(ai_result)):
            foods = []
            for x in range(len(ai_result[z])):
                foods.append(ai_result[z][x])

            food_menu.append(foods)

        #print('food_menu : ' + str(food_menu))

        return food_menu
    else:
        _test_index = []
        for x in range(result_count):
            _test_index.append(ran.randint(2, len(_test_data2) - 1))

        food_menu = []
        for z in range(len(_test_index)):
            foods = []
            for x in range(len(_test_data2[_test_index[z]])):
                foods.append(_test_data2[_test_index[z]][x])

            food_menu.append(foods)

        print(food_menu)

        ai_result = food_menu

        return ai_result

#사용자가 선택 또는 새로고침한 식단 데이터를 학습 데이터로써 파일 형태로 저장하는 함수
def save_user_choice(path):
    f = open(path, 'w', encoding='utf-8')

    add_str = ''


    add_str += str('[')
    for _i in range(len(user_reviews[0])):
        add_str += str('[')
        for _j in range(len(user_reviews[0][_i])):
            _data = str(user_reviews[0][_i][_j])
            add_str += str(_data)

            if _j < len(user_reviews[0][_i]) - 1:
                add_str += str(',')
            else:
                add_str += str(']')
        if _i < len(user_reviews[0]) - 1:
            add_str += str(',')
        else:
            add_str += str(']')

    add_str += str('|[')
    for _i in range(len(user_reviews[1])):
        _data = str(user_reviews[1][_i])
        add_str += str(_data)
        if _i < len(user_reviews[1]) - 1:
            add_str += str(',')
    add_str += str(']')


    f.write(add_str)

    f.close()

#사용자별 학습 데이터를 저장한 파일로부터 데이터를 읽는 함수
def load_user_choice(_df, path):
    if exists(path):
        f = open(path, 'r', encoding='utf-8')
        read_str = ''

        while True:
            line = f.readline()
            if not line: break
            read_str += line.replace(' ', '').replace('\n', '').replace('\r', '')

        sp = read_str.split('|')
        result = sp

        if len(result) > 1:
            user_reviews[0] = str_to_list(result[0], False, _df)
            user_reviews[1] = str_to_list(result[1], False, _df)


        f.close()
        return result
    else:
        return []

#콘솔에서 사용자의 성별을 묻는 함수
def ask_gender():
    _my_gender = input('\n성별을 입력해주세요 (남:M/여:F) : ')
    if _my_gender == '여' or _my_gender == 'F':
        return 0
    elif _my_gender == '남' or _my_gender == 'M':
        return 1
    else:
        print('\n성별을 다시 입력해주세요.\n')
        return ask_gender()

#콘솔에서 사용자가 하루에 얼마나 움직이는지 묻는 함수
def ask_exercise():
    _my_exercise = input('\n하루에 얼마나 움직입니까? (상:H/중:M/하:L) : ')
    if _my_exercise == '하' or _my_exercise == 'L':
        return 0
    elif _my_exercise == '중' or _my_exercise == 'M':
        return 1
    elif _my_exercise == '상' or _my_exercise == 'H':
        return 2
    else:
        print('\n다시 입력해주세요.\n')
        return ask_exercise()

# 사용자 정보 변수(기본 값으로 초기화)
my_id = 'user_data'
my_age = 20
my_weight = 64
my_height = 170

# daily exercise amount (not moving each day = 0, moving my body little bit per day = 1, exercise a lot per day = 2)
my_exercise = 1

# Female = 0, Male = 1
my_gender = 1

def login_user(_id, _age, _weight, _height, _gender, _exercise):
    global my_id, my_age, my_weight, my_height, my_exercise, my_gender
    
    my_id = _id
    my_age = _age
    my_weight = _weight
    my_height = _height

    my_exercise = _exercise

    my_gender = _gender


# 사용자의 하루 권장 섭취 열량 변수
kcal = int(calc_kcal_per_day(my_age, my_weight, my_height, my_exercise))
# 나이별 사용자의 하루 권장 식품 교환 단위 변수
amount = calc_amount_per_day(my_age, kcal)
# 사용자의 하루 권장 섭취 탄수환물량 변수(당류 포함)
_choc = int(kcal / 10)

# 나이별 사용자의 하루 한 끼당 권장 교환단위 변수
amount_per_day = [[int(amount[0] / 3), int(amount[0] / 3), int(amount[0] / 3)],
                  [int(amount[1] / 3), int(amount[1] / 3), int(amount[1] / 3)],
                  [int(amount[2] / 3), int(amount[2] / 3), int(amount[2] / 3)],
                  [int(amount[3] / 3), int(amount[3] / 3), int(amount[3] / 3)],
                  [int(amount[4] / 3), int(amount[4] / 3), int(amount[4] / 3)],
                  [int(amount[5] / 3), int(amount[5] / 3), int(amount[5] / 3)],
                  [int(amount[6] / 3), int(amount[6] / 3), int(amount[6] / 3)]]

if amount[0] % 3 == 0:
    amount_per_day = [[int(amount[0] / 3), int(amount[0] / 3), int(amount[0] / 3)],
                      [int(amount[1] / 3), int(amount[1] / 3), int(amount[1] / 3)],
                      [int(amount[2] / 3), int(amount[2] / 3), int(amount[2] / 3)],
                      [int(amount[3] / 3), int(amount[3] / 3), int(amount[3] / 3)],
                      [int(amount[4] / 3), int(amount[4] / 3), int(amount[4] / 3)],
                      [int(amount[5] / 3), int(amount[5] / 3), int(amount[5] / 3)],
                      [int(amount[6] / 3), int(amount[6] / 3), int(amount[6] / 3)]]
elif amount[0] % 3 == 1:
    amount_per_day = [[int(amount[0] / 3), int(amount[0] / 3) + 1, int(amount[0] / 3)],
                      [int(amount[1] / 3), int(amount[1] / 3) + 1, int(amount[1] / 3)],
                      [int(amount[2] / 3), int(amount[2] / 3) + 1, int(amount[2] / 3)],
                      [int(amount[3] / 3), int(amount[3] / 3) + 1, int(amount[3] / 3)],
                      [int(amount[4] / 3), int(amount[4] / 3) + 1, int(amount[4] / 3)],
                      [int(amount[5] / 3), int(amount[5] / 3) + 1, int(amount[5] / 3)],
                      [int(amount[6] / 3), int(amount[6] / 3) + 1, int(amount[6] / 3)]]
else:
    amount_per_day = [[int(amount[0] / 3) + 1, int(amount[0] / 3) + 1, int(amount[0] / 3)],
                      [int(amount[1] / 3) + 1, int(amount[1] / 3) + 1, int(amount[1] / 3)],
                      [int(amount[2] / 3) + 1, int(amount[2] / 3) + 1, int(amount[2] / 3)],
                      [int(amount[3] / 3) + 1, int(amount[3] / 3) + 1, int(amount[3] / 3)],
                      [int(amount[4] / 3) + 1, int(amount[4] / 3) + 1, int(amount[4] / 3)],
                      [int(amount[5] / 3) + 1, int(amount[5] / 3) + 1, int(amount[5] / 3)],
                      [int(amount[6] / 3) + 1, int(amount[6] / 3) + 1, int(amount[6] / 3)]]



#인공지능 학습 횟수
training_count = 50
#인공지능에게 주어질 학습 데이터 갯수
test_data_count = 30

#인공지능에게 주어질 학습 데이터 생성
df = pd.read_json(data_path)

#식품 데이터에서 식품군별로 식품 리스트를 저장하는 변수들
rices = get_rice_per_day(False, False, df)
meats = get_meat_per_day(False, False, df)
vegetables = get_vegetable_per_day(False, False, df)
provinces = get_province_per_day(False, False, df)
milks = get_milk_per_day(False, False, df)
fruits = get_fruit_per_day(False, False, df)

rices_id = get_rice_per_day(False, True, df)
meats_id = get_meat_per_day(False, True, df)
vegetables_id = get_vegetable_per_day(False, True, df)
provinces_id = get_province_per_day(False, True, df)
milks_id = get_milk_per_day(False, True, df)
fruits_id = get_fruit_per_day(False, True, df)


test_data = find_meal(test_data_count, True, df)

hidden_layer_count = 3
hidden_count = 3

#쓰레드로 백그라운드에서 인공지능 학습 진행
thread1 = th.Thread(target=train_ai, args=[training_count, hidden_layer_count, hidden_count])


#테스트 모드인지 지정하는 변수
is_test = True

count_time = 0
delta_time = datetime.datetime.now()
c_dt = [count_time, delta_time]


if is_test:
    is_mode = input('1.식품군이 골고루 들어간 당뇨병 식단으로 추천 되었는지 테스트\n2.임시 사용자로써 자신의 취향이 반영된 식단이 추천 되는지 테스트\n\n위 1,2번 중 테스트 모드를 골라주세요 : ')

    if '1' in str(is_mode):
        # 백그라운드에서 인공지능 학습 시작
        thread1.start()

        # 판단 기록 변수
        answer_count = []
        #연속으로 y를 선택한 횟수
        y_count = 0
        #몇 번 연속으로 y를 선택해야 성공인지
        max_y_count = 30
        #df를 갱신하는 시간 변수
        save_time = datetime.datetime.now()
        #가중치 변수를 갱신하는 시간 변수
        save_time2 = datetime.datetime.now()

        _df = pd.read_json(data_path)

        load_user_choice(_df, 'Resources/Saved_files/' + str(my_id) + '.txt')

        #저장된 가중치 변수
        saved_data = read_weights_file()
        if len(saved_data) > 1:
            saved_data = [str_to_list(saved_data[0], False, _df), str_to_list(saved_data[1], False, _df)]

        save_time = save_time + datetime.timedelta(days=1)
        save_time2 = save_time2 + datetime.timedelta(seconds=10)
        while not stop_event.is_set():
            if save_time == datetime.datetime.now():
                _df = pd.read_json(data_path)
                save_time = save_time + datetime.timedelta(days=1)

            if save_time2 == datetime.datetime.now():
                saved_data = read_weights_file()
                if len(saved_data) > 1:
                    saved_data = [str_to_list(saved_data[0], False, _df), str_to_list(saved_data[1], False, _df)]
                save_time2 = save_time2 + datetime.timedelta(seconds=10)

            debug_delay = datetime.datetime.now()
            menu = create_menu_from_ai(_df, saved_data, hidden_layer_count, hidden_count)
            print('AI full delay : ' + str(datetime.datetime.now() - debug_delay))
            breakfast = ''
            lunch = ''
            dinner = ''
            _menu = []
            for i in range(len(menu)):
                _foods = ''
                for j in range(len(menu[i])):
                    if len(menu[i][j]) > 0:
                        _foods += food_naming(str(menu[i][j]))
                        if j < len(menu[i]) - 1:
                            if len(menu[i][j]) > 0:
                                _foods += ', '
                _menu.append(_foods)
            print('\n추천 식단 : ' + str(_menu[0]))

            reviews = input('식품군이 골고루 들어간 당뇨병 식단으로 추천되었나요? (y/n) : ')
            if 'y' in reviews.lower():
                user_reviews[0].append(menu[0])
                user_reviews[1].append('1')

                answer_count.append('y')
                y_count += 1

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')

                if y_count >= max_y_count:
                    print('\n' + str(max_y_count) + '번 연속 성공하여 학습을 종료합니다')
                    print('\n총 ' + str(len(answer_count)) + '번 식단을 산출하여 ' + str(answer_count.count('y')) + '번 적합한 식단이 나왔고, ' + str(answer_count.count('n')) + '번 적합하지 않은 식단이 나왔습니다.')
                    stop_event.set()

            elif 'n' in reviews.lower():
                user_reviews[0].append(menu[0])
                user_reviews[1].append('0')

                answer_count.append('n')
                y_count = 0

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')
            else:
                print('\n총 ' + str(len(answer_count)) + '번 식단을 산출하여 ' + str(answer_count.count('y')) + '번 적합한 식단이 나왔고, ' + str(answer_count.count('n')) + '번 적합하지 않은 식단이 나왔습니다.')
                print('\n\n프로그램 종료 중...')

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')
                stop_event.set()

    elif '2' in str(is_mode):
        my_id = input('\n아이디를 입력해주세요 : ')
        my_gender = ask_gender()
        my_age = int(input('\n나이를 입력해주세요(세) : ').replace(' ', '').replace('세', '').replace('살', ''))
        my_height = float(input('\n신장(키)을 입력해주세요(cm) : ').lower().replace(' ', '').replace('c', '').replace('m', ''))
        my_weight = float(input('\n체중(몸무게)을 입력해주세요(kg) : ').lower().replace(' ', '').replace('k', '').replace('g', ''))
        my_exercise = ask_exercise()

        #백그라운드에서 인공지능 학습 시작
        thread1.start()

        #판단 기록 변수
        answer_count = []
        # 연속으로 y를 선택한 횟수
        y_count = 0
        # 몇 번 연속으로 y를 선택해야 성공인지
        max_y_count = 30
        # df를 갱신하는 시간 변수
        save_time = datetime.datetime.now()
        # 가중치 변수를 갱신하는 시간 변수
        save_time2 = datetime.datetime.now()

        _df = pd.read_json(data_path)

        load_user_choice(_df, 'Resources/Saved_files/' + str(my_id) + '.txt')

        # 저장된 가중치 변수
        saved_data = read_weights_file()
        if len(saved_data) > 1:
            saved_data = [str_to_list(saved_data[0], False, _df), str_to_list(saved_data[1], False, _df)]

        save_time = save_time + datetime.timedelta(days=1)
        save_time2 = save_time2 + datetime.timedelta(seconds=10)
        while not stop_event.is_set():
            if save_time == datetime.datetime.now():
                _df = pd.read_json(data_path)
                save_time = save_time + datetime.timedelta(days=1)

            if save_time2 == datetime.datetime.now():
                saved_data = read_weights_file()
                if len(saved_data) > 1:
                    saved_data = [str_to_list(saved_data[0], False, _df), str_to_list(saved_data[1], False, _df)]
                save_time2 = save_time2 + datetime.timedelta(seconds=10)

            debug_delay = datetime.datetime.now()
            menu = create_menu_from_ai(_df, saved_data, hidden_layer_count, hidden_count)
            print('AI full delay : ' + str(datetime.datetime.now() - debug_delay))

            breakfast = ''
            lunch = ''
            dinner = ''
            _menu = []
            for i in range(len(menu)):
                _foods = ''
                for j in range(len(menu[i])):
                    if len(menu[i][j]) > 0:
                        _foods += food_naming(str(menu[i][j]))
                        if j < len(menu[i]) - 1:
                            if len(menu[i][j]) > 0:
                                _foods += ', '
                _menu.append(_foods)
            print('\n추천 식단 : ' + str(_menu[0]))

            reviews = input('추천된 식단이 마음에 들었나요? (y/n) : ')
            if 'y' in reviews.lower():
                user_reviews[0].append(menu[0])
                user_reviews[1].append('1')

                answer_count.append('y')
                y_count += 1

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')

                if y_count >= max_y_count:
                    print('\n' + str(max_y_count) + '번 연속 성공하여 학습을 종료합니다')
                    print('\n총 ' + str(len(answer_count)) + '번 식단을 산출하여 ' + str(answer_count.count('y')) + '번 마음에 드는 식단이 나왔고, ' + str(answer_count.count('n')) + '번 마음에 들지 않는 식단이 나왔습니다.')
                    stop_event.set()

            elif 'n' in reviews.lower():
                user_reviews[0].append(menu[0])
                user_reviews[1].append('0')

                answer_count.append('n')
                y_count = 0

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')
            else:
                print('\n총 ' + str(len(answer_count)) + '번 식단을 산출하여 ' + str(answer_count.count('y')) + '번 마음에 드는 식단이 나왔고, ' + str(answer_count.count('n')) + '번 마음에 들지 않는 식단이 나왔습니다.')
                print('\n\n프로그램 종료 중...')

                save_user_choice('Resources/Saved_files/' + str(my_id) + '.txt')
                stop_event.set()
else:
    # 백그라운드에서 인공지능 학습 시작
    user_ai = th.Thread(target=train_ai, args=[training_count, hidden_layer_count, hidden_count])
    user_ai.start()

    print("\nPlease enter 'stop' command to turn off AI Training\n")
    comm = input('\nAI Training >> ')

    while not stop_event.is_set():
        if comm.lower() == 'stop':
            print('\nShut down AI Training...')
            stop_event.set()

thread1.join()
