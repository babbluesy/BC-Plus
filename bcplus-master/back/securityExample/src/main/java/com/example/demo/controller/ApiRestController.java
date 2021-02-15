package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Lunar;
import com.example.demo.dto.holiday;
import com.example.demo.service.HolidayService;
import com.example.demo.service.LunarService;
import com.example.demo.utils.CalMonth;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@RestController
public class ApiRestController {

	@Resource(name = "lunarService")
	private LunarService lunarService;

	@Resource(name="holidayService")
	private HolidayService holidayService;
	private CalMonth calMonth=new CalMonth();
	@GetMapping("/holiday/{month}")
	public holiday getholiday(@PathVariable(value="month") int month) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String startDate=sdf.format(new Date());
    	String date=startDate.substring(0,6);
    	
    	
    	startDate=sdf.format(calMonth.getMonth(startDate, month));
    	date=startDate.substring(0,6);
    	int IntSDate=Integer.parseInt(date);
    	System.out.println(IntSDate);
    	holiday holiday=holidayService.findByDate(IntSDate);
		return holiday;
	}
	
	
	@ApiOperation(value = "Test Sample", tags = "selectlunar")
	@GetMapping("/lunar/{month}")
	public Lunar getLunar(@PathVariable(value = "month") int month) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String startDate=sdf.format(new Date());
    	String date=startDate.substring(0,6);
    	
    	
    	startDate=sdf.format(calMonth.getMonth(startDate, month));
    	date=startDate.substring(0,6);
    	int IntSDate=Integer.parseInt(date);    	
    	Lunar lunar=lunarService.findByDate(IntSDate);
		return lunar;
	}

	@GetMapping("/create")
	public void callapi() throws IOException, InterruptedException {
		for (int y = 2017; y <= 2022; y++) {
			for (int i = 1; i <= 12; i++) {
				int date = y * 100 + i;
				StringBuffer result = new StringBuffer();
				result.append("<date>" + date + "</date>");
					String urlstr = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?"
							+ "solYear=" + y + 
							"&solMonth=" + String.format("%02d", i)
							+ "&ServiceKey=y%2Fw3bdCke9rqzPT2wMZG%2BHGQqMApfjp%2BOV7mxiFxROpGWsM3%2Be9KFPRzIomS9GsJ8Zq9rs%2FeGJYp%2BE9vTgzzJQ%3D%3D";
					String line;
					try {
						URL url = new URL(urlstr);
						HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
						urlconnection.setRequestMethod("GET");

						BufferedReader br = new BufferedReader(
								new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
						while (br!=null&&(line = br.readLine())!=null) {	

							String[] lines=line.split("<item>");
							result.append("<days>");
							for(String li:lines) {
								String subline="";
								if(li.contains("<header>"))
									continue;
								subline += li.substring(li.indexOf("<dateName>"), li.indexOf("</dateName>")) + "</dateName>";
								String nextline=li.substring(li.indexOf("<locdate>"),li.indexOf("</locdate>"))+"</locdate>";
								subline+=nextline;
								result.append("<day>"+subline+"</day>");
							}
						}
							result.append("</days>");
						urlconnection.disconnect();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				JSONObject xmlJSONOjb = XML.toJSONObject(result.toString());
				Gson gson = new Gson();
				File file = new File("src/holiday/holiday" + date + ".json");
				FileWriter files = new FileWriter(file);
				files.write(xmlJSONOjb.toString());
				files.flush();
				files.close();
			}
		}
	}

}
