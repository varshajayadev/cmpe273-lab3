package edu.sjsu.cmpe.cache.client;


public interface CacheSerInterface {
    public String get(long key);

    public void put(long key, String value);
}
