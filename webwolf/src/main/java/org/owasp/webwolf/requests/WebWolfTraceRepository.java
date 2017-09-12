package org.owasp.webwolf.requests;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webwolf.user.WebGoatUser;
import org.owasp.webwolf.user.WebGoatUserCookie;
import org.owasp.webwolf.user.WebGoatUserToCookieRepository;
import org.springframework.boot.actuate.trace.Trace;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.HttpCookie;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.Optional.of;

/**
 * Keep track of all the incoming requests, we are only keeping track of request originating from
 * WebGoat and only if there is a cookie (otherwise we can never relate it back to a user).
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Slf4j
public class WebWolfTraceRepository implements TraceRepository {

    private final LoadingCache<String, ConcurrentLinkedDeque<Trace>> cookieTraces = CacheBuilder.newBuilder()
            .maximumSize(4000).build(new CacheLoader<String, ConcurrentLinkedDeque<Trace>>() {
                @Override
                public ConcurrentLinkedDeque<Trace> load(String s) throws Exception {
                    return new ConcurrentLinkedDeque<>();
                }
            });
    private final WebGoatUserToCookieRepository repository;

    public WebWolfTraceRepository(WebGoatUserToCookieRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Trace> findAll() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("nice", "Great you found the standard Spring Boot tracing endpoint!");
        Trace trace = new Trace(new Date(), map);
        return Lists.newArrayList(trace);
    }

    public List<Trace> findTraceForUser(String username) {
        return Lists.newArrayList(cookieTraces.getUnchecked(username));
    }

    @Override
    public void add(Map<String, Object> map) {
        Optional<String> host = getFromHeaders("host", map);
        Optional<String> referer = getFromHeaders("referer", map);
        if (host.isPresent() && referer.orElse("").contains("WebGoat")) {
            Optional<String> cookie = getFromHeaders("cookie", map);
            cookie.ifPresent(c -> {
                Optional<String> user = findUserBasedOnCookie(c);
                user.ifPresent(u -> {
                    ConcurrentLinkedDeque<Trace> traces = this.cookieTraces.getUnchecked(u);
                    traces.addFirst(new Trace(new Date(), map));
                    cookieTraces.put(u, traces);
                });

            });
        }
    }

    private Optional<String> findUserBasedOnCookie(String cookiesIncomingRequest) {
        //Request from WebGoat to WebWolf will contain the session cookie of WebGoat try to map it to a user
        //this mapping is added to userSession by the CookieFilter in WebGoat code
        HttpCookie cookie = HttpCookie.parse(cookiesIncomingRequest).get(0);
        Optional<WebGoatUserCookie> userToCookie = repository.findByCookie(cookie.getValue());
        Optional<String> user = userToCookie.map(u -> u.getUsername());

        if (!user.isPresent()) {
            //User is maybe logged in to WebWolf use this user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof WebGoatUser) {
                WebGoatUser wg = (WebGoatUser) authentication.getPrincipal();
                user = of(wg.getUsername());
            }
        }
        return user;
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
