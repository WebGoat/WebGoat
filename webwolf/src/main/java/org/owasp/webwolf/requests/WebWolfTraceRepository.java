package org.owasp.webwolf.requests;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.Trace;
import org.springframework.boot.actuate.trace.TraceRepository;

import java.util.*;

/**
 * Keep track of all the incoming requests, we are only keeping track of request originating from
 * WebGoat.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Slf4j
public class WebWolfTraceRepository implements TraceRepository {

    private final EvictingQueue<Trace> traces = EvictingQueue.create(10000);
    private List<String> exclusionList = Lists.newArrayList("/WebWolf/home", "/WebWolf/mail","/WebWolf/files", "/images/", "/login", "/favicon.ico", "/js/", "/webjars/", "/WebWolf/requests", "/css/", "/mail");

    @Override
    public List<Trace> findAll() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("nice", "Great you found the standard Spring Boot tracing endpoint!");
        Trace trace = new Trace(new Date(), map);
        return Lists.newArrayList(trace);
    }

    public List<Trace> findAllTraces() {
        return Lists.newArrayList(traces);
    }

    private boolean isInExclusionList(String path) {
        return exclusionList.stream().anyMatch(e -> path.contains(e));
    }

    @Override
    public void add(Map<String, Object> map) {
        Optional<String> host = getFromHeaders("host", map);
        String path = (String) map.getOrDefault("path", "");
        if (host.isPresent() && !isInExclusionList(path)) {
            traces.add(new Trace(new Date(), map));
        }
    }

    private Optional<String> getFromHeaders(String header, Map<String, Object> map) {
        Map<String, Object> headers = (Map<String, Object>) map.get("headers");
        if (headers != null) {
            Map<String, Object> request = (Map<String, Object>) headers.get("request");
            if (request != null) {
                return Optional.ofNullable((String) request.get(header));
            }
        }
        return Optional.empty();
    }
}
