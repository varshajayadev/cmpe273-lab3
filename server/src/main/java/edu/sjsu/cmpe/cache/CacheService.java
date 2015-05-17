package edu.sjsu.cmpe.cache;

import java.util.concurrent.ConcurrentHashMap;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfig;
import edu.sjsu.cmpe.cache.domain.Entry;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;

public class CacheService extends Service<CacheServiceConfig> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws Exception {
        new CacheService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfig> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfig configuration,
            Environment environment) throws Exception {
        /** Cache APIs */
        CacheInterface cache = new ChronicleMapCache(configuration.getHttpConfiguration().getPort());
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");

    }
}
