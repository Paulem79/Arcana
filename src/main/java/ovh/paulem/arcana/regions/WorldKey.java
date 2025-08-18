package ovh.paulem.arcana.regions;

import org.bukkit.World;

public interface WorldKey {
    World getWorld();
    int getX();
    int getY();
    int getZ();
}
