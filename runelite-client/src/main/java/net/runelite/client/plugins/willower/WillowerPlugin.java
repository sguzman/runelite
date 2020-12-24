package net.runelite.client.plugins.willower;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Willower",
        description = "Highlights clickable bounding box for nearby willowers and clicks them",
        tags = {"woodcutting", "willow"},
        loadWhenOutdated = true,
        enabledByDefault = false
)
@Slf4j
public class WillowerPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private WillowerConfig config;

    @Provides
    public WillowerConfig provideConfig(final ConfigManager configManager)
    {
        return configManager.getConfig(WillowerConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.debug("Willower started up");
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.debug("Willower shut down");
    }
}
