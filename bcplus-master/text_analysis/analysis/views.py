from django.shortcuts import render
from .models import main_analysis
from rest_framework.decorators import api_view
from django.http import JsonResponse       # import from django.http
from drf_yasg.utils import swagger_auto_schema
from .serializers import FreqSerializer, NERSerializer, TempSerializer, RestSerializer, MemoAnalysisSerializer

# Create your views here.
# @api_view(['GET', 'POST'])
# def index(request):
#     original_text = '대전에 놀러가는 날'
#
#     return render(request, 'analysis/index.html',
#                   {
#                       'original_text': original_text,
#                   })

@swagger_auto_schema(method='POST', request_body=FreqSerializer)
@api_view(['POST'])
def show_freq(request):
    if request.method == 'POST':
        try:
            data = request.data
            text = data['text']
            original_text = text.replace('\t', '').replace('\n', '').replace("\'", '')
            freq_lst = main_analysis.morp_analysis(original_text)

        except Exception as e:
            print(e)

    return JsonResponse({
        'freq_lst': freq_lst,
    })

@swagger_auto_schema(method='POST', request_body=NERSerializer)
@api_view(['POST'])
def show_entities(request):
    if request.method == 'POST':
        try:
            data = request.data
            text = data['text']
            coord = data['coord']
            original_text = text.replace('\t', '').replace('\n', '').replace("\'", '')
            location_lst = main_analysis.NER(original_text, coord)  # 장소 정보
            # event_cnt = main_analysis.NER(original_text)[1]  # 이벤트 개수

        except Exception as e:
            print(e)

    return JsonResponse({
        'location_lst': location_lst,
    })

@swagger_auto_schema(method='POST', request_body=TempSerializer)
@api_view(['POST'])
def show_temp(request):
    if request.method == 'POST':
        try:
            data = request.data
            text = data['text']
            coord = data['coord']
            original_text = text.replace('\t', '').replace('\n', '').replace("\'", '')
            location_lst = main_analysis.NER(original_text, coord)
            weather = main_analysis.crawling_temp(location_lst)

        except Exception as e:
            print(e)

    return JsonResponse({
        'weather': weather,
    })

@swagger_auto_schema(method='POST', request_body=RestSerializer)
@api_view(['POST'])
def show_rest(request):
    if request.method == 'POST':
        try:
            data = request.data
            text = data['text']
            coord = data['coord']
            original_text = text.replace('\t', '').replace('\n', '').replace("\'", '')
            location_lst = main_analysis.NER(original_text, coord)
            restaurant = main_analysis.crawling_res(location_lst)

        except Exception as e:
            print(e)

    return JsonResponse({
        'restaurant': restaurant,
    })

@swagger_auto_schema(method='POST', request_body=MemoAnalysisSerializer)
@api_view(['POST'])
def MemoAnalysis(request):
    if request.method == 'POST':
        try:
            data = request.data
            text = data['text']
            coord = data['coord']
            text = text.replace('\t', '').replace('\n', '').replace("\'", '')
            try:
                location_lst = main_analysis.NER(text, coord)
            except:
                location_lst = []
                location_lst.append({'loc': None, 'tag': None})
            try:
                weather = main_analysis.crawling_temp(location_lst)
            except:
                weather = {
                    'loc': None,
                    'today':
                        {
                            'temp': None,
                            'cast_txt': None,
                            'min_temp': None,
                            'max_temp': None,
                            'sensible_temp': None,
                        },
                    'D1':
                        {
                            'temp_D1_AM': None,
                            'temp_D1_PM': None,
                            'cast_txt_D1_AM': None,
                            'cast_txt_D1_PM': None,
                        },
                    'D2':
                        {
                            'temp_D2_AM': None,
                            'temp_D2_PM': None,
                            'cast_txt_D2_AM': None,
                            'cast_txt_D2_PM': None,
                        }
                }
            try:
                restaurant = main_analysis.crawling_res(location_lst)
            except:
                restaurant = {
                    'loc': None,
                    'rest_lst': [{
                        'rest_name': None,
                        'rest_category': None,
                        'rest_rate': None,
                        'rest_url': None,
                        'img_path': None,
                    }]
                }
        except Exception as e:
            print(e)

    return JsonResponse({
        'location_lst': location_lst,
        'weather': weather,
        'restaurant': restaurant,
    })