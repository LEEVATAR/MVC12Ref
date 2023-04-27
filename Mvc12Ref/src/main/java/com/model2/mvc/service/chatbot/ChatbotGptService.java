package com.model2.mvc.service.chatbot;

import okhttp3.*;

import java.io.IOException;

public class ChatbotGptService {
    public String fetchGptResponse(String prompt) throws IOException {
        String Gpt_API_KEY = "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";
        String Gpt_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonBody = "{"
                + "\"prompt\":\"" + prompt + "\","
                + "\"max_tokens\": 50,"
                + "\"temperature\": 0.7,"
                + "\"n\": 1,"
                + "\"stop\": null"
                + "}";

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(Gpt_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + Gpt_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
