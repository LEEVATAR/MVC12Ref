package com.model2.mvc.service.chatbot;

import java.io.FileInputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
	
	
    private String secretKey = "VGZDTVBiZXBBRXhBbWFsdnRManVkV3RVRlJQdmtLbFg=";

	
    private final ChatbotMessageBuilder chatbotMessageBuilder;
    private final SignatureGenerator signatureGenerator;
    private final ChatbotApiCaller chatbotApiCaller;

    @Autowired
    public ChatbotService(ChatbotMessageBuilder chatbotMessageBuilder, SignatureGenerator signatureGenerator, ChatbotApiCaller chatbotApiCaller) {
        this.chatbotMessageBuilder = chatbotMessageBuilder;
        this.signatureGenerator = signatureGenerator;
        this.chatbotApiCaller = chatbotApiCaller;
    }

    public String processChatbotMessage(String userMessage) {
        // 1. 챗봇 메시지 생성
        String chatbotMessage = chatbotMessageBuilder.build(userMessage);

        // 2. 시그니처 생성
        String signature = signatureGenerator.generateSignature(chatbotMessage, secretKey);

        // 3. 챗봇 API 호출 및 응답 처리
        String chatbotResponse = chatbotApiCaller.callChatbotApi(chatbotMessage, signature);

        return chatbotResponse;
    }
}
