package com.example.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CalMonth {

    public List<String> getMonthList(Date start,Date end){
    	List<String>result=new ArrayList<String>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String startDate=sdf.format(start);
    	String endDate=sdf.format(end);
    	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    	int sy=Integer.parseInt(startDate.substring(0,4));
    	int sm=Integer.parseInt(startDate.substring(4,6));
    	int ey=Integer.parseInt(endDate.substring(0,4));
    	int em=Integer.parseInt(endDate.substring(4,6));
    	
    	int period=(ey-sy)*12+(em-sm);
    	
    	if(period==0)result.add(startDate.substring(0,6));
    	else {
    		result.add(startDate.substring(0,6));
    		for(int i=0;i<period;i++) {
    			if(sm==12) {
    				sy++;
    				sm=1;
    			}
    			else {
    				sm++;
    			}
    			if(sm<10)result.add(String.valueOf(sy)+"0"+String.valueOf(sm));
    			else result.add(String.valueOf(sy)+String.valueOf(sm));
    		}
    	}
    	
    	return result;
    }
    public Date getDate(String day,int flag) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal=Calendar.getInstance();    	
    	if(flag==1) {    			
        	cal.set(Integer.parseInt(day.substring(0,4)),Integer.parseInt(day.substring(4,6))-1,1);
        	day=day+cal.getActualMaximum(Calendar.DAY_OF_MONTH);        	
    	}
		cal.setTime(sdf.parse(day));
    	cal.add(Calendar.HOUR, 9);
    	return cal.getTime();    	
    }
    public Date getDateUTC(String day,int flag) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal=Calendar.getInstance();    	
    	if(flag==1) {    			
        	cal.set(Integer.parseInt(day.substring(0,4)),Integer.parseInt(day.substring(4,6))-1,1);
        	day=day+cal.getActualMaximum(Calendar.DAY_OF_MONTH);        	
    	}	
    	cal.setTime(sdf.parse(day));
    	cal.add(Calendar.HOUR, 0);
    	if(flag!=1) {
    		cal.add(Calendar.HOUR, -9);
    	}
    	return cal.getTime();    	
    }  
    public Date getMonth(String month,int a) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal=Calendar.getInstance(); 
    	cal.setTime(sdf.parse(month));
    	cal.add(Calendar.MONTH, a);
    	return cal.getTime();
    }
    public Date addDay(String month,int a) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal=Calendar.getInstance(); 
    	cal.setTime(sdf.parse(month));
    	cal.add(Calendar.HOUR, a);
    	return cal.getTime();
    }
}
