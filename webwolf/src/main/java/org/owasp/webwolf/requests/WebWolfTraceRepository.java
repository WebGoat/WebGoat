package org.owasp.webwolf.requests;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webwolf.user.UserRepository;
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

import static java.util.Optional.empty;
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
    private final UserRepository userRepository;

    public WebWolfTraceRepository(WebGoatUserToCookieRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
        String path = (String) map.getOrDefault("path", "");
        if (host.isPresent() && path.contains("/landing/")) {
            Optional<String> cookie = getFromHeaders("cookie", map);
            Optional<String> user = cookie.isPresent() ? findUserBasedOnCookie(cookie.get()) : getLoggedInUser();
            user.ifPresent(u -> {
                ConcurrentLinkedDeque<Trace> traces = this.cookieTraces.getUnchecked(u);
                traces.addFirst(new Trace(new Date(), map));
                cookieTraces.put(u, traces);
            });
            //No user found based on cookie and logged in user, so add the trace to all users
            //In case of XXE no cookie will be send we cannot retrieve who is logged in.
            //Standalone this is ok, in a challenge you need to make sure the solution or secret the users need to
            //fetch is unique
            if (!user.isPresent()) {
                List<WebGoatUser> users = this.userRepository.findAll();
                users.forEach(u -> {
                    ConcurrentLinkedDeque<Trace> traces = this.cookieTraces.getUnchecked(u.getUsername());
                    traces.addFirst(new Trace(new Date(), map));
                    cookieTraces.put(u.getUsername(), traces);
                });
            }
        }
    }

    private Optional<String> findUserBasedOnCookie(String cookiesIncomingRequest) {
        //Request from WebGoat to WebWolf will contain the session cookie of WebGoat try to map it to a user
        //this mapping is added to userSession by the CookieFilter in WebGoat code
        HttpCookie cookie = HttpCookie.parse(cookiesIncomingRequest).get(0);
        Optional<WebGoatUserCookie> userToCookie = repository.findByCookie(cookie.getValue());
        Optional<String> user = userToCookie.map(u -> u.getUsername());

        return user;
    }

    private Optional<String> getLoggedInUser() {
        Optional<String> user = empty();
        //User is maybe logged in to WebWolf use this user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof WebGoatUser) {
            WebGoatUser wg = (WebGoatUser) authentication.getPrincipal();
            user = of(wg.getUsername());
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
