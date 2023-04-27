package com.model2.mvc.service.chatbot;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ChatbotMessageBuilder {
	
	public String build(String userMessage) {
        // ���⿡ ê�� �޽����� �����ϴ� ������ �ۼ��մϴ�.
        String requestBody = buildRequestMessage(userMessage);
        return requestBody;
    }

	
    public static String buildRequestMessage(String voiceMessage) {
        String requestBody = "";

        try {
            JSONObject obj = new JSONObject();

            long timestamp = new Date().getTime();
            obj.put("version", "v2");
            obj.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2"); // ���� ������ UUID�� ����Ͽ� ������ ����� ID�� �����ϴ� ���� �����ϴ�.
            obj.put("timestamp", timestamp);

            JSONObject bubblesObj = new JSONObject();
            bubblesObj.put("type", "text");

            JSONObject dataObj = new JSONObject();
            dataObj.put("description", voiceMessage);

            bubblesObj.put("data", dataObj);

            JSONArray bubblesArray = new JSONArray();
            bubblesArray.put(bubblesObj);

            obj.put("bubbles", bubblesArray);
            obj.put("event", "send");

            requestBody = obj.toString();

        } catch (Exception e) {
            System.out.println("## Exception : " + e);
        }

        return requestBody;
    }
}
