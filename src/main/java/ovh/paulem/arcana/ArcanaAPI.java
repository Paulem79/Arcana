package ovh.paulem.arcana;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcanaAPI<P extends JavaPlugin> {
    @Getter
    private final P plugin;

    public ArcanaAPI(P plugin) {
        this.plugin = plugin;
    }

    public void init() {
        getPlugin().getLogger().info("ArcanaAPI initialized.");
    }
}
