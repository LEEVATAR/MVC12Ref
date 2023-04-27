package com.model2.mvc.service.chatbot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@RequestMapping("/api/*")
public class ChatbotController {

	public ChatbotController() {
		System.out.println("hello!!");
	}
	//gpt기능추가
	public String fetchGptResponse(String prompt) throws IOException {
		String Gpt_API_KEY = "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";
		String Gpt_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
		
		OkHttpClient client = new OkHttpClient();
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		String jsonBody = "{" + "\"prompt\":\"" + prompt + "\"," + "\"max_tokens\": 50," + "\"temperature\": 0.7,"
				+ "\"n\": 1," + "\"stop\": null" + "}";

		RequestBody body = RequestBody.create(jsonBody, JSON);
		Request request = new Request.Builder().url(Gpt_API_URL).post(body)
				.addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + Gpt_API_KEY)
				.build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	@CrossOrigin
	@RequestMapping("chatbot")
	public ResponseEntity<String> chat(@org.springframework.web.bind.annotation.RequestBody String requestBody) {
		JSONObject json = new JSONObject(requestBody);
		String eventType = json.getString("event");
		String inputText = "";
		
		boolean isGptMode = false;
		if (json.has("isGptMode")) {
		    isGptMode = json.getBoolean("isGptMode");
		}

		
		if (eventType.equals("send")) {
			inputText = json.getString("inputText");
		}
		
		String chatbotMessage = "";
		if (eventType.equals("open")) {
			inputText = "";
		}
		System.out.println("안티티티티프래질" + inputText);
		if (isGptMode) {
			try {
				chatbotMessage = fetchGptResponse(inputText);
			} catch (IOException e) {
				e.printStackTrace();
				chatbotMessage = "GPT API 호출 중 에러가 발생했습니다.";
			}
		} else {
			try {
				String apiURL = "https://a939u9nmlj.apigw.ntruss.com/custom/v1/9745/0d08472a858f3cb1994fd4f8eb5ead1fd4ccc4827bd38ed0ba1b2f37a997dee0";

				URL url = new URL(apiURL);

				String message = getReqMessage(inputText);
				System.out.println("##" + message);

				String encodeBase64String = makeSignature(message, "VGZDTVBiZXBBRXhBbWFsdnRManVkV3RVRlJQdmtLbFg=");

				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json;UTF-8");
				con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

				// post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(message.getBytes("UTF-8"));
				wr.flush();
				wr.close();
				int responseCode = con.getResponseCode();

				BufferedReader br;

				if (responseCode == 200) { // Normal call
					System.out.println(con.getResponseMessage());

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
					String decodedString;
					while ((decodedString = in.readLine()) != null) {
						chatbotMessage = decodedString;
					}
					// chatbotMessage = decodedString;
					in.close();

				} else { // Error occurred
					chatbotMessage = con.getResponseMessage();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
			System.out.println(chatbotMessage + "whut it is!!!!!!");
			JSONObject responseObject = new JSONObject(chatbotMessage);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

			return new ResponseEntity<>(responseObject.toString(), headers, HttpStatus.OK);
		}

	

	public static String makeSignature(String message, String secretKey) {

		String encodeBase64String = "";

		try {
			byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");

			SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
			encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

			return encodeBase64String;

		} catch (Exception e) {
			System.out.println(e);
		}

		return encodeBase64String;

	}

	public static String getReqMessage(String voiceMessage) {

		String requestBody = "";

		try {

			JSONObject obj = new JSONObject();

			long timestamp = new Date().getTime();

			System.out.println("##" + timestamp);

			obj.put("version", "v2");
			obj.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2");
			// => userId is a unique code for each chat user, not a fixed value, recommend
			// use UUID. use different id for each user could help you to split chat history
			// for users.

			obj.put("timestamp", timestamp);

			JSONObject bubbles_obj = new JSONObject();

			bubbles_obj.put("type", "text");

			JSONObject data_obj = new JSONObject();
			data_obj.put("description", voiceMessage);

			bubbles_obj.put("type", "text");
			bubbles_obj.put("data", data_obj);

			JSONArray bubbles_array = new JSONArray();
			bubbles_array.put(bubbles_obj);

			obj.put("bubbles", bubbles_array);
			obj.put("event", "send");

			requestBody = obj.toString();

		} catch (Exception e) {
			System.out.println("## Exception : " + e);
		}

		return requestBody;
	}

}
