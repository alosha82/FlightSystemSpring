package com.example.FlightSystemsSpring.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTestFunctions
{
    @SneakyThrows
    public static <T> T generateEntityOfType(Class<T> tClass,String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .build();

        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson;
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        return gson.fromJson(response.body(), tClass);
    }

    @SneakyThrows
    public static int getStatusCode(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .build();


        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }
}
