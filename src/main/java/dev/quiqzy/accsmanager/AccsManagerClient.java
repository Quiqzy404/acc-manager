package dev.quiqzy.accsmanager;

import main.walksy.lib.core.gui.widget.WLButton;
import dev.quiqzy.accsmanager.mixin.ScreenAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public final class AccsManagerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen || screen instanceof MultiplayerScreen) {
                int x = scaledWidth - 124;
                int y = 12;
                ((ScreenAccessor) screen).accsManager$addDrawableChild(new WLButton(x, y, 112, 24, "ACCOUNTS",
                        button -> client.setScreen(new AccountManagerScreen(screen))));
            }
        });
    }
}
