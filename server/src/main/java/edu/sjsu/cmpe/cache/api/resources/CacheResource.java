package edu.sjsu.cmpe.cache.api.resources;

import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import edu.sjsu.cmpe.cache.domain.Entry;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import javax.ws.rs.PathParam;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CacheResource {
    private final CacheInterface c;
    public CacheResource(CacheInterface cache) {
        this.c = cache;
    }

    @GET
    @Path("{key}")
    @Timed(name = "get-entry")
    public Entry get(@PathParam("key") LongParam key) {
        return c.get(key.get());
    }

    @GET
    @Timed(name = "view-all-entries")
    public List<Entry> getAll() {
        return c.getAll();
    }

    @PUT
    @Path("{key}/{value}")
    @Timed(name = "add-entry")
    public Response put(@PathParam("key") LongParam key,
            @PathParam("value") String value) {
        Entry entry = new Entry();
        entry.setKey(key.get());
        entry.setValue(value);

        c.save(entry);

        return Response.status(200).build();
    }
}
