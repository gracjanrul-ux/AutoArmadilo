package com.example.autoarmadillo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutoArmadilloMod implements ClientModInitializer {

    private static KeyBinding toggleKey;
    private boolean isEnabled = false;
    private int repairTimer = 0;

    @Override
    public void onInitializeClient() {
        // Rejestracja klawisza włączającego/wyłączającego (Domyślnie 'O')
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoarmadillo.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.autoarmadillo"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null || client.interactionManager == null) return;

            // Przełączanie włącz/wyłącz
            while (toggleKey.wasPressed()) {
                isEnabled = !isEnabled;
                client.player.sendMessage(
                        Text.literal(" AutoArmadillo: " + (isEnabled ? "§aWŁĄCZONY" : "§cWYŁĄCZONY")),
                        true
                );
            }

            if (isEnabled) {
                // 1. Wykrywanie czy gracz patrzy bezpośrednio na Armadillo
                HitResult hit = client.crosshairTarget;
                if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHit = (EntityHitResult) hit;
                    
                    // Jeśli mob, na którego patrzymy, to Armadillo
                    if (entityHit.getEntity() instanceof ArmadilloEntity armadillo) {
                        // Użyj przedmiotu z głównej dłoni (np. szczotki) na pancerniku
                        client.interactionManager.interactEntity(client.player, armadillo, Hand.MAIN_HAND);
                        client.player.swingHand(Hand.MAIN_HAND);
                    }
                }

                // 2. Wpisywanie /repair co 2 sekundy (40 ticków)
                repairTimer++;
                if (repairTimer >= 40) {
                    client.player.networkHandler.sendChatCommand("repair");
                    repairTimer = 0;
                }
            } else {
                repairTimer = 0;
            }
        });
    }
}