from rest_framework import serializers
from analysis.models import main_analysis

class FreqSerializer(serializers.ModelSerializer):
    class Meta:
        model = main_analysis
        fields = ('text', )

class NERSerializer(serializers.ModelSerializer):
    class Meta:
        model = main_analysis
        fields = ('text', )

class TempSerializer(serializers.ModelSerializer):
    class Meta:
        model = main_analysis
        fields = ('text', 'coord')

class RestSerializer(serializers.ModelSerializer):
    class Meta:
        model = main_analysis
        fields = ('text', 'coord')

class MemoAnalysisSerializer(serializers.ModelSerializer):
    class Meta:
        model = main_analysis
        fields = ('text', 'coord')