import numpy as np
import random as ran
import pandas as pd

data_path = 'Resources/food_data.json'

#가중치 저장 파일 경로
file_path = 'Resources/Saved_files/weights.txt'

#입력 노드 갯수
input_count = 1

#은닉 레이어 갯수
hidden_layer_count = 3

#출력 노드 갯수
output_count = 1

#은닉 레이어당 가지는 노드 갯수
hidden_count = 3

#오차에 대한 가중치 계산 반영률
learning_rate = 0.5

weight = []
bios = []
hidden_net_layer = []
hidden_layer = []


def name_to_id(n, df):
    is_name = True
    name = str(n).replace(' ', '_')
    if is_name:
        if len(df['foodCd'].where(df['foodNm'] == str(name)).dropna()) > 0:
            id_num = df['foodCd'].where(df['foodNm'] == str(name)).dropna().values[0]
            result = str(id_num).replace('D', '').replace('-', '')
            result = int(result)
            for i in range(16):
                result = result / 10.0
            return float(result)
        else:
            return 0.0

    return n

#시그모이드 함수
def sigmoid_function(x):
    return 1 / (1 + np.exp(-x))

#변수들의 초기화 함수
def sign_weight_value(in_count, hid_count, out_count):
    #입력 레이어와 은닉 레이어 사이에 가중치 변수들의 초기화
    input_weight = []
    for i in range(in_count * hidden_count):
        input_weight.append(ran.random())

    #은닉 레이어와 은닉 레이어 사이에 가중치 변수들의 초기화
    hidden_weight = []
    for i in range(hid_count):
        for j in range(hidden_count * hidden_count):
            hidden_weight.append(ran.random())

    #은닉 레이어와 출력 레이어 사이에 가중치 변수들의 초기화
    output_weight = []
    for i in range(hidden_count * out_count):
        output_weight.append(ran.random())

    weight.append(input_weight)
    weight.append(hidden_weight)
    weight.append(output_weight)

    return weight

def sign_bios_value(hid_count):
    for i in range(hid_count + 1):
        bios.append(ran.random())
    return bios

#순전파 함수
def calculate_straight(weights, biases, input_data, _hidden_layer_count):
    #variable to return
    result = []

    # create variable : [input data, hidden layers[0...length of hidden layers], output data]
    _datas = [input_data]
    #print("len(hidden_layer) : " + str(len(hidden_layer)))
    for k in range(_hidden_layer_count):
        _datas.append(hidden_layer[k])


    # First hidden layer = [x0 * w0 + x1 * w1 + x2 * w2 + x3 * w3, x0 * w4 + x1 * w5 + x2 * w6...]
    for k in range(len(_datas)):
        # create variable : matching with _data's index between weights index
        weights_index = k
        if weights_index > 0 and weights_index != len(weights) - 1:
            weights_index = 1
        elif weights_index == len(weights) - 1:
            weights_index = 2

        # calculate and put result into node
        for w in weights[weights_index]:
            val = 0
            for x in _datas[k]:
                #print('x : ' + str(x) + ', w : ' + str(w))
                if '[' in str(x):
                    val += x[0] * w
                else:
                    val += x * w
            if k != len(_datas) - 1:
                hidden_net_layer[k].append(val + biases[k])
                hidden_layer[k].append(sigmoid_function(val + biases[k]))
            else:
                result.append(sigmoid_function(val + biases[k]))

        _datas = [input_data]
        for j in range(_hidden_layer_count):
            _datas.append(hidden_layer[j])

    return result

#순전파 계산 후 수행할 오차 계산 함수
def calculate_error(output_result, output_answer):
    result = 0.0
    for i in range(len(output_answer)):
        result += pow(output_answer[i] - output_result[i], 2.0) / 2.0

    return result

#가중치 업데이트를 위해 에러에 대한 가중치 증감률 계산 함수
def calculate_back_term1(target, output):
    term1 = -target - output

    return term1

def calculate_back_term2(output):
    term2 = output * (1.0 - output)

    return term2

def calculate_back_term3(weights, out_h):
    out_h_index = 0
    for wi in range(len(weights) - 1, 0, -1):
        weight_area = [input_count * hidden_count]
        for i in range(hidden_layer_count - 1):
            weight_area.append(weight_area[len(weight_area) - 1] + hidden_count * hidden_count)
        weight_area.append(weight_area[len(weight_area) - 1] + output_count * hidden_count)

        for i in range(len(weight_area) - 1, 1, -1):
            if weight_area[i - 1] <= wi < weight_area[i]:
                out_h_index = len(out_h) - (len(weight_area) - i)
    #print("len(out_h) : " + str(len(out_h)) + ", out_h_index : " + str(out_h_index))
    return out_h[out_h_index]

#역전파 함수
def calculate_backward(_datas, weights):
    #_datas = [input_node, hidden_nodes_outputs[0...length of hidden layers], output_result, output_answer]
    output_answer = _datas[len(_datas) - 1]
    output_result = _datas[len(_datas) - 2]
    out_h = _datas[0] + _datas[len(_datas) - 3]

    #print("out_h : " + str(out_h))

    saved_w = []
    saved_w_plus_t1 = []
    saved_w_plus_t2 = []
    saved_w_plus_t3 = []

    pre_saved_w = []
    pre_saved_w_plus_t1 = []
    pre_saved_w_plus_t2 = []
    pre_saved_w_plus_t3 = []
    for wi in range(len(weights) - 1, 0, -1):
        which_out_h = 0

        weight_area = [input_count * hidden_count]
        for i in range(hidden_layer_count - 1):
            weight_area.append(weight_area[len(weight_area) - 1] + hidden_count * hidden_count)
        weight_area.append(weight_area[len(weight_area) - 1] + output_count * hidden_count)

        for i in range(len(weight_area) - 1, 1, -1):
            if weight_area[i - 1] <= wi < weight_area[i]:
                which_out_h = len(out_h) - (len(weight_area) - i)
        weight_plus_t1 = calculate_back_term1(output_answer, output_result[wi % len(output_result)])
        weight_plus_t2 = calculate_back_term2(output_answer)
        weight_plus_t3 = calculate_back_term3(weights, out_h[which_out_h])
        weight_plus = weight_plus_t1 * weight_plus_t2 * weight_plus_t3

        pre_saved_w_plus_t1.append(weight_plus_t1)
        pre_saved_w_plus_t2.append(weight_plus_t2)
        pre_saved_w_plus_t3.append(weight_plus_t3)

        pre_saved_w.append(weights[wi])

        weights[wi] -= learning_rate * weight_plus

        saved_w.append(pre_saved_w)
        saved_w_plus_t1.append(pre_saved_w_plus_t1)
        saved_w_plus_t2.append(pre_saved_w_plus_t2)
        saved_w_plus_t3.append(pre_saved_w_plus_t3)

    return weights

#학습된 가중치 데이터들을 파일 형태로 저장하는 함수
def save_weights_file(weights):
    open(file_path, 'w').close()

    f = open(file_path, 'a')

    #write weight data into save file
    f.write('[')
    for i in range(len(weights)):
        f.write('[')
        for j in range(len(weights[i])):
            data = str(weights[i][j])
            f.write(data)
            if j < len(weights[i]) - 1:
                f.write(',')
        f.write(']')
        if i < len(weights) - 1:
            f.write(',')
    f.write(']')

    #write bios data into save file
    f.write('|[')
    for i in range(len(bios)):
        data = str(bios[i])
        f.write(data)
        if i < len(bios) - 1:
            f.write(',')
    f.write(']')

    f.close()

#저장된 가중치와 바이오스 데이터들을 파일로부터 읽는 함수
def read_weights_file():
    f = open(file_path, 'r')
    read_str = ''
    result = []
    while True:
        line = f.readline()
        if not line: break
        read_str += line.replace(' ', '').replace('array(', '').replace(')', '').replace('\n', '').replace('\r', '')

    sp = read_str.split('|')
    result = sp

    f.close()
    return result

#문자열 형태의 리스트를 리스트 형태로 변환하는 함수
def str_to_list(s):
    df = pd.read_json(data_path)
    result0 = []
    if '[[' in s:
        sp = s[1:len(s) - 1].split(',')
        for x in sp:
            res = []
            sp1 = str(x).replace('[', '').replace(']', '').split(',')
            for y in sp1:
                res.append(float(name_to_id(y, df)))
            result0.append(res)
        return result0
    else:
        sp = s[1:len(s) - 1].split(',')
        for x in sp:
            result0.append(float(name_to_id(x, df)))
        return result0

#사용자의 취향 데이터를 학습하는 함수
def train(train_count, input_data, _hidden_layer_count, output_data):
    input_count = len(input_data)
    output_count = len(output_data)
    if len(hidden_layer) <= 0:
        for n in range(_hidden_layer_count):
            hidden_layer.append([])
            hidden_net_layer.append([])

    bios = sign_bios_value(_hidden_layer_count)
    weights = sign_weight_value(input_count, _hidden_layer_count, output_count)
    saved_data = read_weights_file()
    if len(saved_data) > 1:
        weights = str_to_list(str(saved_data[0]))
        weight = weights
        bios = str_to_list(str(saved_data[1]))

    for t in range(train_count):

        #순전파 함수 호출
        input_index = ran.randint(0, input_count - 1)
        straight_result = calculate_straight(weights, bios, input_data[input_index], _hidden_layer_count)

        #오차 계산 함수 호출
        #error = calculate_error(straight_result, output_data)

        #역전파 함수 호출
        output_index = ran.randint(0, output_count - 1)
        _datas = [input_data[input_index], hidden_layer, straight_result, output_data[output_index]]
        weight = calculate_backward(_datas, weights)

        #print('Trained Data : ' + str(input_data[input_index]) + ' = ' + str(output_data[output_index]))

        weights = weight
        save_weights_file(weights)

def detect_favorite_menu(input_data, like_percent):
    menu_result = False
    bios = sign_bios_value(hidden_layer_count)
    weights = sign_weight_value(1, hidden_layer_count, 1)
    saved_data = read_weights_file()

    if len(saved_data) > 1:
        weights = str_to_list(str(saved_data[0]))
        weight = weights
        bios = str_to_list(str(saved_data[1]))

    result = calculate_straight(weights, bios, input_data, hidden_layer_count)
    if result[0] > like_percent:
        menu_result = True


    return menu_result

