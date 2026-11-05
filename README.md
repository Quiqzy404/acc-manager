# Accs Manager — Minecraft 1.21.11 / Fabric

This revision replaces the old vanilla-style account panel with a full-screen `WLScreen` built on WalksyLib 2.0.5's glass renderer and `WLButton` widgets.

## Current state

- WalksyLib-based glass account-manager UI.
- Entry point on the title screen and multiplayer screen.
- Project targets Minecraft 1.21.11, Fabric Loader 0.19.5, Fabric API 0.141.6+1.21.11 and Java 21.
- **Microsoft OAuth, refresh-token import/rotation, Xbox/XSTS/Minecraft authentication, account switching and secure credential storage are not implemented yet.** The Add Microsoft Account button is intentionally marked as unavailable rather than pretending to authenticate.
- No user tokens or cookie exports are included in this ZIP.

## Important about the supplied text file

The uploaded entries have a cookie-export style structure (`cookie:cookie`), not the format of standalone Microsoft OAuth refresh tokens. Do not feed browser-cookie exports into this mod. Use an explicit Microsoft sign-in flow instead. Since the file contains credential-like material, keep it private and revoke affected sessions if it was exposed.

## Build

Use JDK 21 and Gradle. From this directory run:

```powershell
gradle build
```

The resulting mod JAR is created in `build/libs`. Install it with Fabric API and WalksyLib 2.0.5 for Minecraft 1.21.11.
