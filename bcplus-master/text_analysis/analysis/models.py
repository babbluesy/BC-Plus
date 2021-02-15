from django.db import models
import os

# from selenium import webdriver  # 가상 웹 사용
import bs4
import urllib  # URL 파싱을 위해 사용
from urllib.request import urlopen, Request

from google.cloud import language_v1
# from google.cloud import translate_v2 as translate

from konlpy.tag import Hannanum  # KAIST 말뭉치를 이용해 생성된 사전
import re
import pandas as pd
import numpy as np

from rest_framework.fields import CharField

# Create your models here.

class main_analysis(models.Model):

    text = CharField(max_length=200)
    coord = CharField(max_length=200)
    # 환경변수 불러오기(서비스 계정 키가 저장된 경로로 설정)
    os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'files/toycherry3-f3c605d8bce5.json'
    
    # 형태소 분석
    def morp_analysis(self):
        noun_text = re.sub('[-_=+,#/\?:^$.@*\"※~&%ㆍ·!』\\‘’|\(\)\[\]\<\>`\'…》]', '', str(self))

        noun_text = re.sub('\n', '', noun_text)

        hannanum = Hannanum()
        text_list = hannanum.nouns(noun_text)  # 명사 분석

        word_list = pd.Series(text_list)
        result = word_list.value_counts().head(10)
        result_values = list(result.values)

        for i in range(len(result_values)):
            result_values[i] = np.int16(result_values[i]).item()

        freq_lst = []
        for i in range(len(result)):
            freq_lst.append({'word': result.keys()[i], 'freq': result_values[i]})

        return freq_lst

    # 개체명 인식(장소, 이벤트 인식)
    def NER(self, coord):
        # 클라이언트 인스턴스화
        NER_client = language_v1.LanguageServiceClient()
        # translate_client = translate.Client()

        # 요청 보낼 document 설정
        # Document.Type: TYPE_UNSPECIFIED, PLAIN_TEXT, HTML
        # translated_text = translate_client.translate(text, target_language='en')['translatedText']
        document = language_v1.Document(
            content=self,
            type_=language_v1.Document.Type.PLAIN_TEXT
        )

        # translated_document = language_v1.Document(
        #     content=translated_text,
        #     type_=language_v1.Document.Type.PLAIN_TEXT
        # )

        # 감정(Sentiment)
        # sentiment = client.analyze_sentiment(request={'document': document}).document_sentiment
        # 개체(Entity)
        entities = NER_client.analyze_entities(request={'document': document}).entities
        # translated_entities = NER_client.analyze_entities(request={'document': translated_document}).entities
        # sentiment = NER_client.analyze_sentiment(request={'document': document}).sentences

        entities_lst = []
        # translated_entities_lst = []
        entities_name_lst = []
        entities_type_lst = []
        # translated_entities_name_lst = []
        # translated_entities_type_lst = []

        for i in range(len(entities)):
            entity_name = entities[i].name  # 개체 이름
            entity_type = str(entities[i].type_)[5:]  # 개체 종류
            entities_lst.append((entity_name, entity_type))
            entities_name_lst.append(entity_name)
            entities_type_lst.append(entity_type)

        entities_lst = list(set(entities_lst))

        # for i in range(len(translated_entities)):
        #     entity_name = translated_entities[i].name  # 개체 이름
        #     entity_type = str(translated_entities[i].type_)[5:]  # 개체 종류
        #     translated_entities_lst.append((entity_name, entity_type))
        #     translated_entities_name_lst.append(entity_name)
        #     translated_entities_type_lst.append(entity_type)
        #
        # translated_entities_lst = list(set(translated_entities_lst))

        # 장소만 추출하기
        loc_lst = []

        for i in range(len(entities_lst)):
            if entities_lst[i][1] == 'LOCATION' or entities_lst[i][1] == 'ADDRESS':
                loc_lst.append(entities_lst[i])

        # # 이벤트만 추출하기
        # event_lst = []
        #
        # for i in range(len(translated_entities_lst)):
        #     if translated_entities_lst[i][1] == 'EVENT':
        #         event_lst.append(translated_entities_lst[i])
        #
        # event_cnt = len(event_lst)  # 이벤트 개수

        # 텍스트 기반 저장
        if len(loc_lst) > 0:
            korea_geo = pd.read_csv('files/korea_geo.csv',encoding='CP949')
            ctp_lst = list(korea_geo['ctp_kor'])
            geo_lst = ['시', '군', '구', '읍', '면', '동', '리', '통', '반']

            for i in range(len(ctp_lst)):
                if ctp_lst[i][-1] in geo_lst:
                    new_ctp = ctp_lst[i][0:-1]
                    ctp_lst.append(new_ctp)

            r_loc = []
            for i in range(len(loc_lst)):
                if loc_lst[i][0] in ctp_lst:
                    r_loc.append({'loc': loc_lst[i][0], 'tag': loc_lst[i][1]})

        # 유저 좌표 기반 저장
        else:
            if len(coord) == 0:
                r_loc = []
                r_loc.append({'loc': None, 'tag': None})
            else:
                coord_lst = coord.split(' ')
                city_lst = ['시', '군', '구']
                town_lst = ['읍', '면', '동', '리', '통', '반']
                city_name = ''
                town_name = ''

                for i in range(len(coord_lst)):
                    # 시, 군, 구
                    if coord_lst[i][-1] in city_lst:
                        city_name = coord_lst[i]

                    # 읍, 면, 동, 리
                    elif coord_lst[i][-1] in town_lst:
                        town_name = coord_lst[i]

                if len(city_name) != 0 or len(town_name) != 0:
                    r_loc = []
                    r_loc.append({'loc': city_name + ' ' + town_name, 'tag': 'LOCATION'})
                else:
                    r_loc = []
                    r_loc.append({'loc': None, 'tag': None})

        return r_loc

    # 해당 지역의 날씨
    def crawling_temp(self):  # 튜플
        loc_querry = urllib.parse.quote(self[0]['loc'] + ' 날씨')

        url = 'https://search.naver.com/search.naver?ie=utf8&query=' + loc_querry
        req = Request(url)
        page = urlopen(req)
        html = page.read()
        soup = bs4.BeautifulSoup(html, 'html5lib')

        # print(loc, '|', soup.find('p', class_='info_temperature').find('span', class_='todaytemp').text + '도')
        temp = soup.find('p', class_='info_temperature').find('span', class_='todaytemp').text
        cast_txt = soup.find('p', class_='cast_txt').text
        min_temp = soup.find('span', class_='min').text
        max_temp = soup.find('span', class_='max').text
        sensible_temp = soup.find('span', class_='sensible').text

        tomorrow_info = soup.findAll('div', class_='main_info morning_box')

        temp_D1_AM = tomorrow_info[0].find('p', class_='info_temperature').find('span', class_='todaytemp').text
        temp_D1_PM = tomorrow_info[1].find('p', class_='info_temperature').find('span', class_='todaytemp').text
        cast_txt_D1_AM = tomorrow_info[0].find('p', class_='cast_txt').text
        cast_txt_D1_PM = tomorrow_info[1].find('p', class_='cast_txt').text
        temp_D2_AM = tomorrow_info[2].find('p', class_='info_temperature').find('span', class_='todaytemp').text
        temp_D2_PM = tomorrow_info[3].find('p', class_='info_temperature').find('span', class_='todaytemp').text
        cast_txt_D2_AM = tomorrow_info[2].find('p', class_='cast_txt').text
        cast_txt_D2_PM = tomorrow_info[3].find('p', class_='cast_txt').text

        temp_info = {
            'loc': self[0]['loc'],
            'today':
                {
                    'temp': temp,
                    'cast_txt': cast_txt,
                    'min_temp': min_temp,
                    'max_temp': max_temp,
                    'sensible_temp': sensible_temp,
                },
            'D1':
                {
                    'temp_D1_AM': temp_D1_AM,
                    'temp_D1_PM': temp_D1_PM,
                    'cast_txt_D1_AM': cast_txt_D1_AM,
                    'cast_txt_D1_PM': cast_txt_D1_PM,
                },
            'D2':
                {
                    'temp_D2_AM': temp_D2_AM,
                    'temp_D2_PM': temp_D2_PM,
                    'cast_txt_D2_AM': cast_txt_D2_AM,
                    'cast_txt_D2_PM': cast_txt_D2_PM,
                }
        }

        return temp_info

    def crawling_res(self):
        loc_querry = urllib.parse.quote(self[0]['loc'] + ' 맛집')
        url = 'https://m.search.naver.com/search.naver?ie=utf8&query=' + loc_querry

        whole_lst = []

        # 맛집리스트 형식이 맞게 나올 때까지 반복
        req = Request(url)
        page = urlopen(req)
        html = page.read()
        soup = bs4.BeautifulSoup(html, 'html5lib')
        whole_lst = soup.findAll('li', class_='_3t81n')

        new_dict = []
        for i in range(len(whole_lst)):
            rest_name = whole_lst[i].find('span', class_='_3Yilt').text[2:]
            # try:
            #     rest_category = whole_lst[i].find('span', class_='category').text
            # except:
            #     rest_category = ''
            try:
                rest_rate = whole_lst[i].find('span', class_='_3Yzhl _1ahw0').text[2:]
            except:
                rest_rate = '0.00'
            try:
                sub_lst = whole_lst[i].find('div', class_='cb7hz undefined')['style'].split(';')
                img_path = sub_lst[2].split('(')[1].split(')')[0]
            except:
                img_path = '#'
            try:
                rest_url = whole_lst[i].find('a', class_='Ow5Yt')['href']
            except:
                rest_url = '#'

            sub_dict = {
                'rest_name': rest_name,
                # 'rest_category': rest_category,
                'rest_rate': rest_rate,
                'rest_url': rest_url,
                'img_path': img_path,
            }

            new_dict.append(sub_dict)

        rest_info = {
            'loc': self[0]['loc'],
            'rest_lst': new_dict
        }

        return rest_info

    # def crawling_res_2(location):
    #     # 웹 드라이버 설정
    #     options = webdriver.ChromeOptions()
    #     options.add_argument('headless')  # 웹 브라우저를 띄우지 않는 headless chrome 옵션 적용
    #     options.add_argument('window-size=1920x1080')
    #     options.add_argument('disable-infobars')
    #     options.add_argument('--disable-extensions')
    #     options.add_argument('disable-gpu')  # GPU 사용 안함
    #     options.add_argument('lang=ko_KR')  # 언어 설정
    #     driver = webdriver.Chrome('files/chromedriver.exe', options=options)
    #
    #     # 검색
    #     driver.get('https://www.google.co.kr/maps')  # 구글 열기
    #
    #     search_box = driver.find_element_by_name("q")  # 검색창 찾기
    #     search_box.clear()  # 검색창 비우기(채워져 있는 경우가 있음)
    #     search_box.send_keys(location[0] + '의 맛집')  # 검색어 입력
    #
    #     search_btn = driver.find_element_by_id('searchbox-searchbutton')
    #     search_btn.click()
    #     time.sleep(5)
    #
    #     # 가게 정보 찾기
    #     whole_lst = driver.find_element_by_xpath("//*[@class='section-layout section-scrollbox scrollable-y scrollable-show section-layout-flex-vertical']")
    #     item = whole_lst.find_elements_by_xpath("//*[@class='section-result']")
    #     rest_lst = []
    #
    #     for i in range(len(item)):
    #         splited_item = item[i].text.split('\n')
    #
    #         if '광고·' in splited_item:
    #             pass
    #         else:
    #             try:
    #                 rest_img_path = item[i].find_element_by_xpath("div//*[@class='section-result-image']")
    #                 style_attribute = rest_img_path.get_attribute('style')
    #                 rest_img_url = re.search(r"\((.+)\)(.+)", style_attribute).groups()[0][1:-1]
    #
    #                 splited_item[1] = float(splited_item[1][0:3])
    #                 new_row = (splited_item[0], splited_item[1], rest_img_url)
    #                 rest_lst.append(new_row)
    #
    #                 # rest_img = item[i].find_element_by_xpath("div//*[@class='tLipRb']")
    #                 # img_path = 'rest_img/' + splited_item[0] + '_img.png'
    #                 # rest_img.screenshot(img_path)
    #             except:
    #                 pass
    #     rest_dict = {'loc': location[0], 'rest_lst': rest_lst}
    #
    #     # 드라이버 종료
    #     driver.quit()
    #
    #     return rest_dict