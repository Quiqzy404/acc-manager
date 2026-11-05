package dev.quiqzy.accsmanager;

import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MicrosoftAuth {

    private static final ExecutorService EXECUTOR =
            Executors.newSingleThreadExecutor();

    private MicrosoftAuth() {}

    public record Result(
            String name,
            String uuid,
            String refreshToken,
            String authJson
    ) {}

    public static CompletableFuture<Result> importRefreshToken(
            String refreshToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var httpClient =
                        MinecraftAuth.createHttpClient("AccsManager/0.3.0");

                JavaAuthManager authManager =
                        JavaAuthManager.create(httpClient)
                                .login(refreshToken.trim());

                var profile = authManager
                        .getMinecraftProfile()
                        .getUpToDate();

                String json = JavaAuthManager.toJson(authManager);

                return new Result(
                        profile.getName(),
                        profile.getId(),
                        refreshToken.trim(),
                        json
                );

            } catch (Exception e) {
                throw new RuntimeException("Microsoft authentication failed", e);
            }
        }, EXECUTOR);
    }
}
