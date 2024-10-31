import datetime
#로그 파일 경로
log_file_path = 'Resources/Saved_files/Logs/log.txt'

#로그 파일 삭제
def clear_log_file():
    open(log_file_path, 'w', encoding='utf-8').close()

#과정을 기록하고 저장하는 로그 파일 생성 함수
def add_log_file(s):
    origin_log = read_log_file()
    origin_log = '[' + str(datetime.datetime.now()) + '] ' + origin_log + '\n'
    
    
    f = open(log_file_path, 'w', encoding='utf-8')
    
    add_str = origin_log + '[' + str(datetime.datetime.now()) + '] ' + str(s)
    
    f.write(add_str)
    f.close()

#로그 파일 내용 읽어들여 반환하는 함수
def read_log_file():
    log_body = ''

    f = open(log_file_path, 'r', encoding='utf-8')

    while True:
        line = f.readline()
        if not line: break
        log_body += line.replace(' ', '').replace('array(', '').replace(')', '').replace('\n', '').replace('\r', '')

    f.close()

    return log_body

#디버깅 함수
def debug_log(s, is_debug=False):
    add_log_file(s)

    if is_debug:
        print(s)