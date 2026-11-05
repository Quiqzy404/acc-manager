package dev.quiqzy.accsmanager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public final class MicrosoftAuth {

    private static final String CLIENT_ID = "maven.modrinth:7P86n6Vg:exxN7KBX";
    
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private MicrosoftAuth() {}

    public record Tokens(String accessToken, String refreshToken) {}

    public static CompletableFuture<Tokens> refresh(String refreshToken) {

        String body =
                "client_id=" + encode(CLIENT_ID) +
                "&grant_type=refresh_token" +
                "&refresh_token=" + encode(refreshToken) +
                "&scope=" + encode("XboxLive.signin offline_access");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://login.microsoftonline.com/consumers/oauth2/v2.0/token"
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new IllegalStateException(
                                "Microsoft authentication failed: HTTP "
                                        + response.statusCode()
                        );
                    }

                    JsonObject json = JsonParser.parseString(response.body())
                            .getAsJsonObject();

                    return new Tokens(
                            json.get("access_token").getAsString(),
                            json.has("refresh_token")
                                    ? json.get("refresh_token").getAsString()
                                    : refreshToken
                    );
                });
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
