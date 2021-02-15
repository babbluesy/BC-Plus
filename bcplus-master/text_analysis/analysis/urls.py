from django.urls import path
from . import views


urlpatterns = [
    # path('', views.index, name='index'),
    path('analysis/show_freq', views.show_freq, name='show_freq'),
    path('analysis/show_entities', views.show_entities, name='show_entities'),
    path('analysis/show_temp', views.show_temp, name='show_temp'),
    path('analysis/show_rest', views.show_rest, name='show_rest'),
    path('analysis/MemoAnalysis', views.MemoAnalysis, name='MemoAnalysis'),

]