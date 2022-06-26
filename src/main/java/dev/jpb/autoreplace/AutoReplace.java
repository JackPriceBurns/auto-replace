package dev.jpb.autoreplace;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class AutoReplace extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        this.getLogger().log(Level.INFO, "Enabled AutoReplace!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
