
package dev.quiqzy.accsmanager;

public record AccountProfile(
        String name,
        String uuid,
        String accessToken,
        String refreshToken
) {
}
