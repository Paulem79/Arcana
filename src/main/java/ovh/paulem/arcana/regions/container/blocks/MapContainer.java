package ovh.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

public class MapContainer<S extends Tracked<K>, K extends WorldKey> implements SectionContainer<S, K> {
    private final Int2ObjectOpenHashMap<Holder<S, K>> map = new Int2ObjectOpenHashMap<>();

    @Override
    public Holder<S, K> get(int position) {
        return map.get(position);
    }

    @Override
    public void set(int position, Holder<S, K> holder) {
        map.put(position, holder);
    }

    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        return map.clone();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
}