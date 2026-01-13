package fish.crafting.nogroundflying;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SavedMovementStates;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerWorldData;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PlayerPacketWatcher;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class NoGroundFlyingPlugin extends JavaPlugin {

    public NoGroundFlyingPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        PacketAdapters.registerInbound((PlayerPacketWatcher) (playerRef, packet) -> {
            if (!(packet instanceof ClientMovement movement)) {
                return;
            }

            if (movement.movementStates == null || playerRef.getReference() == null) {
                return;
            }

            Store<EntityStore> store = playerRef.getReference().getStore();
            World world = store.getExternalData().getWorld();

            if (movement.movementStates.onGround && movement.movementStates.flying) {
                world.execute(() -> {
                    Ref<EntityStore> ref = playerRef.getReference();

                    Player player = playerRef.getReference().getStore().getComponent(playerRef.getReference(), Player.getComponentType());

                    if (player == null) {
                        return;
                    }

                    PlayerWorldData perWorldData = player.getPlayerConfigData().getPerWorldData(world.getName());
                    SavedMovementStates movementStates = perWorldData.getLastMovementStates();
                    SavedMovementStates savedMovementStates = movementStates != null ? movementStates : new SavedMovementStates();
                    savedMovementStates.flying = false;

                    player.applyMovementStates(
                            ref,
                            savedMovementStates,
                            movement.movementStates,
                            store
                    );
                });
            }
        });
    }
}
