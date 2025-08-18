package ovh.paulem.arcana.regions;

public interface Tracked<K> {
    K getKey();

    void onUnload();
}
