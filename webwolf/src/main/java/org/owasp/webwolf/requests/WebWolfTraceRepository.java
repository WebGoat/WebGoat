package org.owasp.webwolf.requests;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import java.util.List;

/**
 * Keep track of all the incoming requests, we are only keeping track of request originating from
 * WebGoat.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Slf4j
public class WebWolfTraceRepository implements HttpTraceRepository {

    private final EvictingQueue<HttpTrace> traces = EvictingQueue.create(10000);
    private List<String> exclusionList = Lists.newArrayList("/WebWolf/home", "/WebWolf/mail", "/WebWolf/files", "/images/", "/login", "/favicon.ico", "/js/", "/webjars/", "/WebWolf/requests", "/css/", "/mail");

    @Override
    public List<HttpTrace> findAll() {
        return List.of();
    }

    public List<HttpTrace> findAllTraces() {
        return Lists.newArrayList(traces);
    }

    private boolean isInExclusionList(String path) {
        return exclusionList.stream().anyMatch(e -> path.contains(e));
    }

    @Override
    public void add(HttpTrace httpTrace) {
        var path = httpTrace.getRequest().getUri().getPath();
        if (!isInExclusionList(path)) {
            traces.add(httpTrace);
        }
    }
}
