package edu.sjsu.cmpe.cache.client;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class Client {

    static String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

    static int max_index(long[] wt_array, int len) {
        long max = 0;
        int index = 0;
        for (int i = 0; i < len; i++) {
            if (wt_array[i] > max) {
                max = wt_array[i];
                index = i;
            }
        }
        return index;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client with algorithm:  " + args[0]);
        CacheSerInterface[] cacheArr = new CacheSerInterface[3];
        CacheSerInterface c0 = new DistributedCacheServ(
                "http://localhost:3000");
        CacheSerInterface c1 = new DistributedCacheServ(
                "http://localhost:3001");
        CacheSerInterface c2 = new DistributedCacheServ(
                "http://localhost:3002");
        cacheArr[0] = c0;
        cacheArr[1] = c1;
        cacheArr[2] = c2;

        if (args[0].equals("Consistent")) {
          
            for (int i = 0; i < 10; i++) {
                    HashCode hc = Hashing.md5().newHasher()
                            .putLong(i+1)
                            .putString(data[i], StandardCharsets.UTF_8)
                            .hash();
                    int hash = Hashing.consistentHash(hc, 3);
                    if (hash > 2) {
                            System.out.println("bad hash");
                    } else {
                            cacheArr[hash].put(i+1, data[i]);
                    }
            }
            
           
            for (int i = 0; i < 10; i++) {
                    HashCode hc = Hashing.md5().newHasher().putLong(i+1)
                            .putString(data[i], StandardCharsets.UTF_8)
                            .hash();
                    int hash = Hashing.consistentHash(hc, cacheArr.length);
                    if (hash > 2) {
                            System.out.println("bad hash");
                    } else {
                            String data = cacheArr[hash].get(i+1);
                            System.out.println("Reading from cache " + hash + 
                                            " : data = " + data + " for key " + (i + 1));
                    }
            }
        } else {
          
            long weight[] = new long[3];
            int idx = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 3; j++) {
                    weight[j] = Hashing.md5().newHasher()
                                    .putLong(i+1)
                                    .putLong(j)
                                    .putString(data[i], StandardCharsets.UTF_8)
                                    .hash().asLong();
                }
                idx = max_index(weight, 3);

                if (idx > 2) {
                        System.out.println("!!bad hash!!");
                } else {
                        cacheArr[idx].put(i+1, data[i]);
                }
            }
            
           
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 3; j++) {
                    weight[j] = Hashing.md5().newHasher()
                                    .putLong(i+1)
                                    .putLong(j)
                                    .putString(data[i], StandardCharsets.UTF_8)
                                    .hash().asLong();
                }
                idx = max_index(weight, 3);

                if (idx > 2) {
                    System.out.println("bad hash");
                } else {
                    String data = cacheArr[idx].get(i+1);
                    System.out.println("Reading from cache " + idx + 
                                  " : data = " + data + " for key " + (i + 1));
                }
            }
        }

    }

}
