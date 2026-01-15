package core.repository;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepository<V> {

    protected final Map<String, V> store;

    protected AbstractRepository() {
        this.store = new HashMap<>();
    }

    public V get(String id) {
        return store.get(id);
    }

    public Map<String, V> getAll() {
        return Collections.unmodifiableMap(store);
    }

    public void register(String id, V value) {
        store.put(id, value);
    }

    public boolean isExist(String id) {
        return store.containsKey(id);
    }

    public int contain() {
        return store.size();
    }
}
