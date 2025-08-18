package ovh.paulem.arcana.regions.container;

import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

import java.util.Collection;

public interface SubContainerHolderAccessor<S extends Tracked<K>, K extends WorldKey> {
    S getHolder(int x, int y, int z);

    void setHolder(int x, int y, int z, S holder);

    void removeHolder(int x, int y, int z);

    Collection<Holder<S, K>> getAllHolders();
}
