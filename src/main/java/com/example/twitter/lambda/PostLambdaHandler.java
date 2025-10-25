package com.example.twitter.lambda;

import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.twitter.model.Post;
import com.example.twitter.model.PostRequest;
import com.example.twitter.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {
            PostRequest postRequest = objectMapper.readValue(input.getBody(), PostRequest.class);
            Post post = new Post(
                UUID.randomUUID().toString(),
                postRequest.getText(),
                objectMapper.readValue(
                    callUserService(postRequest.getUsername()),
                    User.class
                )
            );
            
            return response
                .withStatusCode(200)
                .withBody(objectMapper.writeValueAsString(post));
        } catch (Exception e) {
            return response
                .withStatusCode(500)
                .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String callUserService(String username) {
        return "{\"id\":\"" + UUID.randomUUID().toString() + "\",\"username\":\"" + username + "\"}";
    }
}