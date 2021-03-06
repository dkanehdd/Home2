package com.kosmo.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mms.MemberDTO;
import mms.certificationService;

@Controller
public class Example {

	@RequestMapping("/check/sendSMS")
	@ResponseBody  
	public String sendSMS(HttpServletRequest req) {
		
		String phoneNumber = req.getParameter("phoneNumber");
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        certificationService.certifiedPhoneNumber(phoneNumber,numStr);
        return numStr;
    }
	@RequestMapping("/naver")
	public String Naver() {
		
		return "naver";
	}
	@RequestMapping("/callback")
	public String callback() {
		return "callback";
	}
	
	@RequestMapping("/callbackAction")
	public String Navercallback(HttpServletRequest req, Model model) {
		String token = req.getParameter("token");
		
		String header = "Bearer " + token; // Bearer 다음에 공백 추가

		String apiURL = "https://openapi.naver.com/v1/nid/me";

		Map<String, String> requestHeaders = new HashMap();
		requestHeaders.put("Authorization", header);
		String responseBody = get(apiURL, requestHeaders);
		MemberDTO dto = new MemberDTO();
		System.out.println(responseBody);
		String image = "";
		try {
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParse.parse(responseBody);
			JSONObject personObject = (JSONObject) jsonObj.get("response");
			System.out.println(personObject.get("id")); 
			System.out.println(personObject.get("email")); 
			System.out.println(personObject.get("mobile").toString().replace("-", "")); 
			System.out.println(personObject.get("gender").toString().equals("M")?"남":"여"); 
			System.out.println(personObject.get("name")); 
			System.out.println(personObject.get("birthday")); 
			dto.setId(personObject.get("id").toString());
			dto.setEmail(personObject.get("email").toString());
			dto.setPhone(personObject.get("mobile").toString().replace("-", ""));
			dto.setGender(personObject.get("gender").toString().equals("M")?"남":"여");
			dto.setName(personObject.get("name").toString());
			dto.setBirthday(personObject.get("birthday").toString());
			image = personObject.get("profile_image").toString();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		model.addAttribute("dto", dto);
		model.addAttribute("image", image);
		return "result";
	}
	
	
	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
 
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
}
