
package dev.quiqzy.accsmanager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class SessionTokenAuth {

    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public record Profile(String name, String uuid) {}

    public static CompletableFuture<Profile> fetchProfile(String token) {

        if (!isValidFormat(token)) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid token format")
            );
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.minecraftservices.com/minecraft/profile"
                ))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        return HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {

                    if (response.statusCode() == 401) {
                        throw new IllegalStateException("Token expired or invalid");
                    }

                    if (response.statusCode() != 200) {
                        throw new IllegalStateException(
                                "Minecraft API error: " + response.statusCode()
                        );
                    }

                    JsonObject json = JsonParser.parseString(
                            response.body()
                    ).getAsJsonObject();

                    return new Profile(
                            json.get("name").getAsString(),
                            json.get("id").getAsString()
                    );
                });
    }

    public static boolean isValidFormat(String token) {
        return token != null
                && token.startsWith("eyJ")
                && token.length() > 100;
    }
}
