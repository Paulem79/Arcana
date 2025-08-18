package ovh.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

public class ArraySectionContainer<S extends Tracked<K>, K extends WorldKey> implements SectionContainer<S, K> {

    private final Holder<S, K>[] contents;
    private int size;

    private ArraySectionContainer(int size) {
        contents = new Holder[size];
    }

    public static<S extends Tracked<K>, K extends WorldKey> ArraySectionContainer<S, K> create(int size) {
        return new ArraySectionContainer<>(size);
    }

    @Override
    public Holder<S, K> get(int position) {
        return contents[position];
    }

    @Override
    public void set(int position, Holder<S, K> holder) {
        Holder<S, K> old = get(position);
        contents[position] = holder;

        if (old == null && holder != null) {
            size++;
        } else if (old != null && holder == null) {
            size--;
        }
    }

    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        Int2ObjectOpenHashMap<Holder<S, K>> result = new Int2ObjectOpenHashMap<>();

        if (size == 0) {
            return result;
        }

        for (int index = 0; index < contents.length; index++) {
            Holder<S, K> holder = contents[index];

            if (holder == null) {
                continue;
            }

            result.put(index, holder);
        }

        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}