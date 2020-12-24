package net.runelite.client.plugins.willower;

import net.runelite.api.*;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class WillowerOverlay extends Overlay {
    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
    private static final Color RED = new Color(221, 44, 0);
    private static final Color GREEN = new Color(0, 200, 83);
    private static final Color ORANGE = new Color(255, 109, 0);
    private static final Color YELLOW = new Color(255, 214, 0);
    private static final Color CYAN = new Color(0, 184, 212);
    private static final Color BLUE = new Color(41, 98, 255);
    private static final Color DEEP_PURPLE = new Color(98, 0, 234);
    private static final Color PURPLE = new Color(170, 0, 255);
    private static final Color GRAY = new Color(158, 158, 158);

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

    private void renderWillow(Graphics2D graphics, Tile tile, Player player) {
        GameObject[] gameObjects = tile.getGameObjects();
        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject != null) {
                    if (gameObject.getId() == ObjectID.WILLOW_10833) {
                        if (player.getLocalLocation().distanceTo(gameObject.getLocalLocation()) <= MAX_DISTANCE) {
                            OverlayUtil.renderTileOverlay(graphics, gameObject, "ID: " + gameObject.getId(), BLUE);
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
