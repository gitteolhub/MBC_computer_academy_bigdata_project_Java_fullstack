import numpy as np
import random as ran
import log_ctrl

data_path = 'Resources/food_data.json'

#가중치 저장 파일 경로
file_path = 'Resources/Saved_files/weights.txt'

#사용자가 좋아하는 식단 가중치 저장 파일 경로
like_file_path = 'Resources/Saved_files/like_weights.txt'

#입력 노드 갯수
input_count = 1

#출력 노드 갯수
output_count = 1

#오차에 대한 가중치 계산 반영률
learning_rate = 0.3

#weight = []
bios = []
hidden_net_layer = []
hidden_layer = []

#식품명을 학습 데이터로 사용 가능하도록 실수로 변환하는 함수
def name_to_id(n, _df):
    is_name = True
    name = str(n)
    if is_name:
        if len(_df['foodCd'].where(_df['foodNm'] == str(name)).dropna()) > 0:
            id_num = _df['foodCd'].where(_df['foodNm'] == str(name)).dropna().values[0]
            result = str(id_num).replace('D', '').replace('R', '').replace('-', '')
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
def sign_weight_value(in_count, hid_count, out_count, hidden_count):
    _weight = []

    #입력 레이어와 은닉 레이어 사이에 가중치 변수들의 초기화
    input_weight = []
    for i in range(in_count * hidden_count):
        input_weight.append(ran.random())

    #은닉 레이어와 은닉 레이어 사이에 가중치 변수들의 초기화
    hidden_weight = []
    for i in range(hid_count - 1):
        for j in range(hidden_count * hidden_count):
            hidden_weight.append(ran.random())

    #은닉 레이어와 출력 레이어 사이에 가중치 변수들의 초기화
    output_weight = []
    for i in range(hidden_count * out_count):
        output_weight.append(ran.random())

    _weight.append(input_weight)
    _weight.append(hidden_weight)
    _weight.append(output_weight)

    return _weight

def sign_bios_value(hid_count):
    _bios = []
    for i in range(hid_count + 1):
        _bios.append(ran.random())

    return _bios

#순전파 함수
def calculate_straight(weights, biases, input_data, _hidden_layer_count, _hidden_count, is_debug=False):

    log_ctrl.debug_log('weights : ' + str(weights), is_debug)

    #variable to return
    result = [0.0]

    # create variable : [input data, hidden layers[0...length of hidden layers]]
    _datas = [input_data]
    for k in range(_hidden_layer_count):
        _datas.append(hidden_layer[k])

    log_ctrl.debug_log('_datas : ' + str(_datas), is_debug)

    # First hidden layer = [x0 * w0 + x1 * w1 + x2 * w2 + x3 * w3, x0 * w4 + x1 * w5 + x2 * w6...]
    for k in range(len(_datas)):
        # create variable : matching with _data's index between weights index
        weights_index = k
        if weights_index > 0 and weights_index != len(weights) - 1:
            weights_index = 1
        elif weights_index == len(weights) - 1:
            weights_index = 2

        # calculate and put result into node
        for w in range(len(weights[weights_index])):
            val = 0

            for x in _datas[k]:
                if '[' in str(x):
                    val += float(x[0]) * float(str(weights[weights_index][w]).replace("'", ""))
                else:
                    val += float(x) * float(str(weights[weights_index][w]).replace("'", ""))

            if k != len(_datas) - 1:
                weights_count_by_k = (len(weights[weights_index]) / (_hidden_layer_count - 1))
                h_index = abs(k - 1) * int(w / len(input_data)) + max(0, int(w / weights_count_by_k)) * _hidden_count + max(1, k) * int(w % weights_count_by_k / _hidden_count)
                log_ctrl.debug_log('int(w % (len(weights[weights_index]) / (_hidden_layer_count - 1)) / _hidden_count) = h_index : int(' + str(w) + ' % (' + str(len(weights[weights_index])) + ' / (' + str(_hidden_layer_count) + ' - 1)) / ' + str(_hidden_count) + ' ) = ' + str(h_index), is_debug)

                if len(hidden_layer[k]) < (h_index + 1):
                    hidden_net_layer[k].append(val + float(str(biases[k]).replace("'", "")))
                    hidden_layer[k].append(sigmoid_function(val + float(str(biases[k]).replace("'", ""))))
                else:
                    hidden_net_layer[k][h_index] = (val + float(str(biases[k]).replace("'", "")))
                    hidden_layer[k][h_index] = (sigmoid_function(val + float(str(biases[k]).replace("'", ""))))

                log_ctrl.debug_log('hidden_layer[' + str(k) + '] : ' + str(hidden_layer[k]), is_debug)
            else:
                result[0] = (sigmoid_function(val + float(str(biases[k]).replace("'", "").replace(' ', '').replace(']', ''))))

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

def calculate_back_term3(weights, out_h, hidden_layer_count, hidden_count):

    out_h_index = 0
    for wi in range(len(weights) - 1, 0, -1):
        weight_area = [input_count * hidden_count]
        for i in range(hidden_layer_count - 1):
            weight_area.append(weight_area[len(weight_area) - 1] + hidden_count * hidden_count)
        weight_area.append(weight_area[len(weight_area) - 1] + output_count * hidden_count)

        for i in range(len(weight_area) - 1, 1, -1):
            if weight_area[i - 1] <= wi < weight_area[i]:
                out_h_index = len(out_h) - (len(weight_area) - i)

    if type(out_h) == float:
        return out_h
    else:
        return out_h[out_h_index]

#역전파 함수
def calculate_backward(_datas, weights, hidden_layer_count, hidden_count):
    #_datas = [input_node, hidden_nodes_outputs[0...length of hidden layers], output_result, output_answer]
    output_answer = _datas[len(_datas) - 1]
    output_result = _datas[len(_datas) - 2]
    out_h = [_datas[0], _datas[len(_datas) - 3]]


    saved_w = []
    saved_w_plus_t1 = []
    saved_w_plus_t2 = []
    saved_w_plus_t3 = []

    pre_saved_w = []
    pre_saved_w_plus_t1 = []
    pre_saved_w_plus_t2 = []
    pre_saved_w_plus_t3 = []

    _weights = two_to_one_list(weights)


    for wi in range(len(_weights) - 1, 0, -1):
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
        weight_plus_t3 = calculate_back_term3(_weights, out_h[which_out_h], hidden_layer_count, hidden_count)


        weight_plus = weight_plus_t1 * weight_plus_t2 * weight_plus_t3

        pre_saved_w_plus_t1.append(weight_plus_t1)
        pre_saved_w_plus_t2.append(weight_plus_t2)
        pre_saved_w_plus_t3.append(weight_plus_t3)

        pre_saved_w.append(float(_weights[wi]))

        _weights[wi] = float(_weights[wi]) - float(learning_rate) * float(weight_plus)

        saved_w.append(pre_saved_w)
        saved_w_plus_t1.append(pre_saved_w_plus_t1)
        saved_w_plus_t2.append(pre_saved_w_plus_t2)
        saved_w_plus_t3.append(pre_saved_w_plus_t3)

    return weights

def check_str_ok_and_fix(in_str):
    result = ''
    big_count_op = 0
    big_count_ed = 0

    mid_count_op = 0
    mid_count_ed = 0

    sma_count_op = 0
    sma_count_ed = 0

    last_s = ''

    for s in in_str:
        if s == '[':
            big_count_op += 1
            result += s
            last_s = s
        elif s == ']':
            if big_count_op > big_count_ed and last_s != '{' and last_s != '(':
                result += s
                last_s = s
                big_count_ed += 1
        elif s == '{':
            mid_count_op += 1
            result += s
            last_s = s
        elif s == '}':
            if mid_count_op > mid_count_ed and last_s != '(' and last_s != '[':
                result += s
                last_s = s
                mid_count_ed += 1
        elif s == '(':
            sma_count_op += 1
            result += s
            last_s = s
        elif s == ')':
            if sma_count_op > sma_count_ed and last_s != '{' and last_s != '[':
                result += s
                last_s = s
                sma_count_ed += 1
        else:
            result += s

    return result

#학습된 가중치 데이터들을 파일 형태로 저장하는 함수
def save_weights_file(path, weights, _bios, if_id):
    origin_str = open(path, 'r', encoding='utf-8')

    read_str = ''

    add_str = ''

    while True:
        line = origin_str.readline()
        if not line: break
        read_str += line.replace(' ', '').replace('\n', '').replace('\r', '')

    if '{' in read_str and if_id != '':
        if '},{' in read_str:
            sp = read_str.split('},{')
            for s in sp:
                if str(if_id) == str(s.replace(' ', '').replace('{', '').replace('}', '').replace("\'", '').replace('\"', '').split(':')[0]):
                    if add_str == '':
                        add_str = '{' + str(if_id) + ':'
                    else:
                        add_str += str(if_id) + ':'

                    break
                else:
                    add_str += str(s) + '},{'
            if not str(if_id) in add_str:
                add_str += str(if_id) + ':'
        else:
            if if_id in read_str:
                add_str = '{' + str(if_id) + ':'
            else:
                add_str = str(read_str) + ',{' + str(if_id) + ':'
    else:
        if if_id == '':
            add_str = ''
        else:
            add_str = '{' + str(if_id) + ':'

    open(path, 'w', encoding='utf-8').close()
    f = open(path, 'w', encoding='utf-8')

    #write weight data into save file
    add_str += str('[')
    for i in range(len(weights)):
        add_str += str('[')
        for j in range(len(weights[i])):
            data = str(weights[i][j])
            add_str += str(data)
            if j < len(weights[i]) - 1:
                add_str += str(',')
        add_str += str(']')
        if i < len(weights) - 1:
            add_str += str(',')
    add_str += str(']')

    #write bios data into save file
    add_str += str('|[')
    for i in range(len(_bios)):
        data = str(_bios[i])
        add_str += str(data)
        if i < len(_bios) - 1:
            add_str += str(',')
    add_str += str(']')

    if '{' in add_str:
        add_str += '}'

        if '},{' in read_str:
            regex = ''
            sp = read_str.split('},{')
            for s in sp:
                if str(if_id) == str(s.replace(' ', '').replace('{', '').replace('}', '').replace("\'", '').replace('\"', '').split(':')[0]):
                    regex = str(s) + '}'
                    break
            if regex != '':
                sp2 = read_str.split(regex)
                if len(sp2) > 1:
                    add_str += sp2[1].replace(' ', '')

    add_str = check_str_ok_and_fix(add_str)

    f.write(add_str)

    f.close()

def two_to_one_list(arr):
    result = []

    if len(arr) > 0:
        for x in arr:
            if type(x) == float:
                result.append(x)
            elif type(x) == str:
                result.append(float(x))
            else:
                for y in x:
                    if type(y) == float:
                        result.append(y)
                    elif type(y) == str:
                        result.append(float(y))
                    else:
                        for z in y:
                            result.append(z)

        return result
    else:
        return result

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

#사용자의 취향 데이터를 학습하는 함수
def train(train_count, input_data, _hidden_layer_count, hidden_count, output_data, _df, saved_data, save_file_path, _if_id):
    _input_count = len(input_data[0])
    _output_count = len(output_data)
    if len(hidden_layer) <= 0:
        for n in range(_hidden_layer_count):
            hidden_layer.append([])
            hidden_net_layer.append([])

    if len(saved_data) > 1:
        _weight = str_to_list(str(saved_data[0]), False, _df)
        _bios = str_to_list(str(saved_data[1]), False, _df)
    else:
        _weight = sign_weight_value(_input_count, _hidden_layer_count, _output_count, hidden_count)
        _bios = sign_bios_value(_hidden_layer_count)

    for t in range(train_count):

        #순전파 함수 호출
        input_index = ran.randint(0, _input_count - 1)
        straight_result = calculate_straight(_weight, _bios, input_data[0][input_index], _hidden_layer_count, hidden_count)

        #역전파 함수 호출
        output_index = ran.randint(0, _output_count - 1)
        _datas = [input_data[0][input_index]]
        for x in hidden_layer:
            _datas.append(x)
        _datas.append(straight_result)
        _datas.append(output_data[output_index])
        _weight = calculate_backward(_datas, _weight, _hidden_layer_count, hidden_count)

        save_weights_file(save_file_path, _weight, _bios, _if_id)

#일정 갯수의 식단을 받아와 학습된 인공지능으로 판단하는 함수
def detect_favorite_menu(_hidden_layer_count, hidden_count, input_data, _df, saved_data):
    _input_count = len(input_data)

    if len(hidden_layer) <= 0:
        for n in range(_hidden_layer_count):
            hidden_layer.append([])
            hidden_net_layer.append([])

    if len(saved_data) > 1:
        _weight = str_to_list(str(saved_data[0]), False, _df)
        _bios = str_to_list(str(saved_data[1]), False, _df)
    else:
        _weight = sign_weight_value(_input_count, _hidden_layer_count, 1, hidden_count)
        _bios = sign_bios_value(_hidden_layer_count)

    result = calculate_straight(_weight, _bios, input_data, _hidden_layer_count, hidden_count)

    menu_result = result[0]

    log_ctrl.debug_log('menu_result : ' + str(menu_result))

    return menu_result

