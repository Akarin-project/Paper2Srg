package org.bukkit.craftbukkit.map;

import java.util.UUID;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CraftMapRenderer extends MapRenderer {

    private final MapData worldMap;

    public CraftMapRenderer(CraftMapView mapView, MapData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        // Map
        for (int x = 0; x < 128; ++x) {
            for (int y = 0; y < 128; ++y) {
                canvas.setPixel(x, y, worldMap.field_76198_e[y * 128 + x]);
            }
        }

        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }

        for (UUID key : worldMap.field_76203_h.keySet()) { // Spigot string -> uuid.
            // If this cursor is for a player check visibility with vanish system
            Player other = Bukkit.getPlayer(key); // Spigot
            if (other != null && !player.canSee(other)) {
                continue;
            }

            MapDecoration decoration = (MapDecoration) worldMap.field_76203_h.get(key);
            cursors.addCursor(decoration.func_176112_b(), decoration.func_176113_c(), (byte) (decoration.func_176111_d() & 15), decoration.func_176110_a());
        }
    }

}
