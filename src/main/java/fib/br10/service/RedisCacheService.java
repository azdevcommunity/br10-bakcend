package fib.br10.service;

import fib.br10.service.abstracts.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService<K, V> implements CacheService<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }
    @Override
    public V getAndDelete(K key) {
        return redisTemplate.opsForValue().getAndDelete(key);
    }

    @Override
    public void put(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void putToSet(K key, V value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<V> getSet(K key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void put(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long remove(K key, V value) {
        return redisTemplate.opsForSet().remove(key, value);
    }
}
