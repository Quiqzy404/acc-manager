package dev.quiqzy.accsmanager;

import main.walksy.lib.core.gui.WLScreen;
import main.walksy.lib.core.gui.widget.WLButton;
import main.walksy.lib.core.render.WLGraphics2D;
import net.minecraft.client.gui.screen.Screen;

public final class AccountManagerScreen extends WLScreen {
    private final Screen previous;
    private String status = "No accounts connected";

    public AccountManagerScreen(Screen previous) {
        super(previous, "Accs Manager");
        this.previous = previous;
    }

    @Override
    protected void init() {
        clearChildren();
        int panelWidth = Math.min(620, width - 48);
        int left = (width - panelWidth) / 2;
        int top = (height - 340) / 2;

        addDrawableChild(new WLButton(left + panelWidth - 116, top + 286, 96, 30, "CLOSE",
                button -> close()).setCornerRadius(10));
        addDrawableChild(new WLButton(left + 20, top + 286, 174, 30, "ADD MICROSOFT ACCOUNT",
                button -> status = "Microsoft OAuth sign-in is not implemented yet.").setCornerRadius(10));
    }

    @Override
    public void extract(WLGraphics2D g, int mouseX, int mouseY, float delta) {
        int w = Math.min(620, g.guiWidth() - 48);
        int h = 340;
        int x = (g.guiWidth() - w) / 2;
        int y = (g.guiHeight() - h) / 2;

        // WalksyLib's own rounded glass renderer and material styling.
        g.fillRoundedGradient(0, 0, g.guiWidth(), g.guiHeight(),
                0xB90A0D14, 0xB90A0D14, 0xD90A0D14, 0xD90A0D14, 0);
        g.glass().dimensions(x, y, w, h).cornerRadius(22).minecraftAmount(0.04f).extract();
        g.fillRoundedGradient(x + 1, y + 1, w - 2, h - 2,
                0xE9151B27, 0xE9151B27, 0xE90D121C, 0xE90D121C, 21);
        g.fillRoundedGradient(x + 22, y + 66, w - 44, 1,
                0x554B607C, 0x554B607C, 0x554B607C, 0x554B607C, 1);

        g.text(textRenderer, "ACCS MANAGER", x + 24, y + 22, 0xFFF3F6FC);
        g.text(textRenderer, "MICROSOFT SESSIONS  /  1.21.11", x + 24, y + 43, 0xFF8E9DB2);

        int cardX = x + 22;
        int cardY = y + 88;
        int cardW = w - 44;
        g.fillRoundedGradient(cardX, cardY, cardW, 150,
                0xAA222B3A, 0xAA1B2432, 0xAA151D2A, 0xAA151D2A, 14);
        g.text(textRenderer, "ACCOUNTS", cardX + 18, cardY + 18, 0xFFE9EEF7);
        g.text(textRenderer, status, cardX + 18, cardY + 52, 0xFFB4C0D0);
        g.text(textRenderer, "Accounts will appear here after a supported sign-in.",
                cardX + 18, cardY + 72, 0xFF8493A8);
        g.text(textRenderer, "SESSION STATUS", cardX + 18, cardY + 111, 0xFF8295AD);
        g.fillRoundedGradient(cardX + cardW - 118, cardY + 108, 94, 22,
                0x553D9C75, 0x553D9C75, 0x553D9C75, 0x553D9C75, 11);
        g.text(textRenderer, "NOT CONNECTED", cardX + cardW - 108, cardY + 115, 0xFF9ED6B9);

        g.text(textRenderer, "SECURE LOCAL STORAGE", x + 24, y + 254, 0xFFDDE5F0);
        g.text(textRenderer, "No credentials are bundled with this project.",
                x + 24, y + 272, 0xFF8E9DB2);
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(previous);
    }

    @Override
    public boolean shouldPause() { return false; }
}
