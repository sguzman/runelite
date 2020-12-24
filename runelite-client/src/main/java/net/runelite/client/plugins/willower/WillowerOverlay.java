package net.runelite.client.plugins.willower;

import net.runelite.api.*;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class WillowerOverlay extends Overlay {
    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);

    private static final int MAX_DISTANCE = 2400;

    private final Client client;
    private final WillowerPlugin plugin;

    @Inject
    private WillowerOverlay(Client client, WillowerPlugin plugin)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        graphics.setFont(FONT);
        renderTileObjects(graphics);

        return null;
    }

    private void renderTileObjects(Graphics2D graphics2D) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = client.getPlane();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null)
                {
                    continue;
                }

                Player player = client.getLocalPlayer();
                if (player == null)
                {
                    continue;
                }

                renderWillow(graphics2D, tile, player);
            }
        }
    }

    private static boolean isWillow(int id) {
        Set<Integer> willows = new HashSet<>();

        willows.add(10819);
        willows.add(10829);
        willows.add(10831);
        willows.add(10833);

        return willows.contains(id);
    }

    private void renderWillow(Graphics2D graphics, Tile tile, Player player) {
        GameObject[] gameObjects = tile.getGameObjects();
        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject != null) {
                    if (isWillow(gameObject.getId())) {
                        if (player.getLocalLocation().distanceTo(gameObject.getLocalLocation()) <= MAX_DISTANCE) {
                            OverlayUtil.renderTileOverlay(graphics, gameObject, "ID: " + gameObject.getId(), Color.BLUE);
                        }

                        // Draw a polygon around the convex hull
                        // of the model vertices
                        Shape p = gameObject.getClickbox();
                        if (p != null) {
                            graphics.draw(p);
                        }
                    }
                }
            }
        }
    }
}
