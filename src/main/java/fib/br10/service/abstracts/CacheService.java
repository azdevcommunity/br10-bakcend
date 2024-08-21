package fib.br10.service.abstracts;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheService<K, V> {
    V get(K key);
    Set<V> getSet(K key);
    V getAndDelete(K key);
    void put(K key, V value);
    void putToSet(K key, V value);
    void put(K key, V value, long timeout, TimeUnit unit);
    void delete(K key);
    Long remove(K key, V value);
}