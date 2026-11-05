package dev.quiqzy.accsmanager;

import main.walksy.lib.core.gui.WLScreen;
import main.walksy.lib.core.gui.widget.WLButton;
import main.walksy.lib.core.gui.widget.SearchBar;
import main.walksy.lib.core.render.WLGraphics2D;
import main.walksy.lib.core.render.glass.api.GlassStyle;
import main.walksy.lib.core.render.glass.api.LiquidGlassConstants;

import net.minecraft.client.gui.screen.Screen;

public final class AccountManagerScreen extends WLScreen {

    private final Screen previous;

    private String status = "No accounts connected";

    private SearchBar tokenField;

    private static final int WHITE = 0xFFF4F7FC;
    private static final int SECONDARY = 0xFFB3C0D2;
    private static final int MUTED = 0xFF8292A8;
    private static final int ACCENT = 0xFF8CB8FF;

    public AccountManagerScreen(Screen previous) {
        super(previous, "Accs Manager");
        this.previous = previous;
    }

    private int panelWidth() {
        return Math.min(900, Math.max(520, width - 48));
    }

    private int panelHeight() {
        return Math.min(560, Math.max(360, height - 48));
    }

    @Override
    protected void init() {
        clearChildren();

        int w = panelWidth();
        int h = panelHeight();

        int x = (width - w) / 2;
        int y = (height - h) / 2;

        int sidebarW = Math.min(208, Math.max(176, w / 4));
        int contentX = x + sidebarW + 24;
        int contentW = w - sidebarW - 48;

        tokenField = new SearchBar(
                contentX + 18,
                y + 120,
                contentW - 36,
                38,
                value -> {}
        );

        tokenField.setPlaceholder("Paste Microsoft refresh token...");
        tokenField.setMaxLength(8192);
        tokenField.setOnSubmit(this::importToken);

        addDrawableChild(tokenField);

        addDrawableChild(
                new WLButton(
                        contentX + 18,
                        y + 168,
                        contentW - 36,
                        34,
                        "IMPORT ACCOUNT",
                        button -> importToken()
                ).setCornerRadius(17)
        );

        addDrawableChild(
                new WLButton(
                        x + w - 118,
                        y + h - 54,
                        92,
                        30,
                        "CLOSE",
                        button -> close()
                ).setCornerRadius(15)
        );
    }

    private void importToken() {
        String refreshToken = tokenField.getValue().trim();

        if (refreshToken.isEmpty()) {
            status = "Enter a refresh token first";
            return;
        }

        status = "Authenticating Microsoft account...";

        MicrosoftAuth.importRefreshToken(refreshToken)
                .whenComplete((result, error) -> {

                    if (client == null) {
                        return;
                    }

                    client.execute(() -> {

                        if (error != null) {
                            status = "Authentication failed";
                            return;
                        }

                        AccountProfile account = new AccountProfile(
                                result.name(),
                                result.uuid(),
                                result.refreshToken(),
                                result.authJson()
                        );

                        AccountManager.add(account);

                        status = "Imported: " + result.name();

                        tokenField.setValue("");
                    });
                });
    }

    private GlassStyle mainGlass() {
        return new GlassStyle()
                .layer(0)
                .blurRadius(LiquidGlassConstants.BLUR_PANEL)
                .tint(0xFF111827, 0.24f)
                .refractionReach(0.34f)
                .refractionThickness(7f)
                .refractionFactor(1.6f)
                .refractionDispersion(1.2f)
                .lensFold(1f)
                .fresnelFactor(1.5f)
                .glareFactor(5.5f)
                .glareRange(34f)
                .glareHardness(9f)
                .brightEdge(0.22f)
                .edgeDarken(0xFF080D18, 0.16f)
                .rimLight(0xFFDCEBFF, 0.22f)
                .outline(0.8f, 0xFFE6F0FF, 0.22f)
                .shadow(10f, 0.34f, 0f, 8f)
                .shadowColor(0xFF000000, 0.34f);
    }

    private GlassStyle sidebarGlass() {
        return new GlassStyle()
                .layer(5)
                .blurRadius(LiquidGlassConstants.BLUR_CLEAR)
                .tint(0xFF26364D, 0.16f)
                .refractionStrength(0.55f)
                .refractionThickness(5f)
                .refractionFactor(1.35f)
                .refractionDispersion(1.1f)
                .fresnelFactor(1.5f)
                .glareFactor(4.5f)
                .glareRange(28f)
                .brightEdge(0.28f)
                .rimLight(0xFFDDEBFF, 0.2f)
                .edgeDarken(0xFF0A1220, 0.12f)
                .outline(0.7f, 0xFFDCE8F8, 0.16f)
                .shadow(5f, 0.2f, 0f, 3f);
    }

    private GlassStyle surfaceGlass() {
        return new GlassStyle()
                .layer(2)
                .blurRadius(8)
                .tint(0xFF26364B, 0.12f)
                .refractionReach(0.22f)
                .refractionThickness(4f)
                .refractionFactor(1.35f)
                .refractionDispersion(0.8f)
                .fresnelFactor(1.3f)
                .glareFactor(3.5f)
                .glareRange(24f)
                .brightEdge(0.18f)
                .rimLight(0xFFDDEBFF, 0.13f)
                .outline(0.65f, 0xFFE4EEFC, 0.13f);
    }

    @Override
    public void extract(
            WLGraphics2D g,
            int mouseX,
            int mouseY,
            float delta
    ) {

        int w = Math.min(900, g.guiWidth() - 48);
        int h = Math.min(560, g.guiHeight() - 48);

        w = Math.max(520, w);
        h = Math.max(360, h);

        int x = (g.guiWidth() - w) / 2;
        int y = (g.guiHeight() - h) / 2;

        int sidebarW = Math.min(208, Math.max(176, w / 4));

        int contentX = x + sidebarW + 24;
        int contentW = w - sidebarW - 48;
        int contentRight = x + w - 24;

        g.glass()
                .dimensions(x, y, w, h)
                .cornerRadius(28)
                .style(mainGlass())
                .extract();

        g.glass()
                .dimensions(
                        x + 12,
                        y + 12,
                        sidebarW,
                        h - 24
                )
                .cornerRadius(22)
                .style(sidebarGlass())
                .extract();

        g.glass()
                .dimensions(
                        contentX,
                        y + 104,
                        contentW,
                        h - 142
                )
                .cornerRadius(22)
                .style(surfaceGlass())
                .extract();

        g.fillRoundedGradient(
                x + 22, y + 1,
                x + w - 22, y + 2,
                0x33FFFFFF,
                0x11FFFFFF,
                0x05FFFFFF,
                0x22FFFFFF,
                1
        );

        g.text(textRenderer, "A C C S",
                x + 30, y + 30, WHITE);

        g.text(textRenderer, "MANAGER",
                x + 30, y + 48, MUTED);

        g.fillRoundedGradient(
                x + 24, y + 76,
                x + sidebarW - 24, y + 77,
                0x44DCEBFF,
                0x18DCEBFF,
                0x08DCEBFF,
                0x22DCEBFF,
                1
        );

        g.text(textRenderer, "LIBRARY",
                x + 28, y + 100, MUTED);

        g.glass()
                .dimensions(
                        x + 20,
                        y + 116,
                        sidebarW - 40,
                        34
                )
                .cornerRadius(12)
                .style(sidebarGlass().layer(6))
                .focus(0.35f)
                .extract();

        g.text(textRenderer, "Accounts",
                x + 38, y + 128, WHITE);

        g.text(textRenderer, "Session profiles",
                x + 38, y + 145, MUTED);

        g.text(textRenderer, "PREFERENCES",
                x + 28, y + 184, MUTED);

        g.text(textRenderer, "Local storage",
                x + 38, y + 210, SECONDARY);

        g.text(textRenderer, "This device only",
                x + 38, y + 228, MUTED);

        g.text(textRenderer, "MINECRAFT",
                x + 28, y + h - 64, MUTED);

        g.text(textRenderer, "1.21.11 / FABRIC",
                x + 28, y + h - 45, SECONDARY);

        g.text(textRenderer, "Your accounts",
                contentX + 4, y + 34, WHITE);

        g.text(
                textRenderer,
                "Import an existing Microsoft refresh token.",
                contentX + 4,
                y + 55,
                SECONDARY
        );

        g.text(
                textRenderer,
                "MICROSOFT AUTH",
                contentRight - 112,
                y + 82,
                ACCENT
        );

        int cardX = contentX + 18;
        int cardY = y + 220;
        int cardW = contentW - 36;
        int cardH = Math.max(130, Math.min(190, h - 230));

        g.glass()
                .dimensions(cardX, cardY, cardW, cardH)
                .cornerRadius(20)
                .style(surfaceGlass().layer(3))
                .extract();

        g.radialGlow(
                cardX + cardW / 2f,
                cardY + 55,
                60f,
                0x1E78AFFF
        );

        g.glass()
                .dimensions(
                        cardX + cardW / 2f - 26,
                        cardY + 25,
                        52,
                        52
                )
                .cornerRadius(26)
                .style(
                        new GlassStyle()
                                .layer(4)
                                .blurRadius(10)
                                .tint(0xFF8CB8FF, 0.16f)
                                .refractionStrength(0.8f)
                                .refractionThickness(8f)
                                .refractionFactor(1.7f)
                                .refractionDispersion(1.4f)
                                .brightEdge(0.35f)
                                .rimLight(0xFFE7F2FF, 0.35f)
                                .outline(0.8f, 0xFFE7F2FF, 0.24f)
                )
                .extract();

        String title = AccountManager.getAccounts().isEmpty()
                ? "No accounts connected"
                : AccountManager.getAccounts().size() + " accounts connected";

        g.text(
                textRenderer,
                title,
                cardX + (cardW - textRenderer.getWidth(title)) / 2,
                cardY + 92,
                WHITE
        );

        String subtitle = "Verified profiles are stored locally.";

        g.text(
                textRenderer,
                subtitle,
                cardX + (cardW - textRenderer.getWidth(subtitle)) / 2,
                cardY + 114,
                SECONDARY
        );

        g.text(
                textRenderer,
                status,
                contentX + 20,
                y + h - 82,
                MUTED
        );

        g.text(
                textRenderer,
                "TOKENS ARE NEVER DISPLAYED IN LOGS",
                contentX + 20,
                y + h - 48,
                MUTED
        );
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(previous);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}