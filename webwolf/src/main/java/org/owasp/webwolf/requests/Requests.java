package org.owasp.webwolf.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.Trace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Controller for fetching all the HTTP requests from WebGoat to WebWolf for a specific
 * user.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/WebWolf/requests")
public class Requests {

    private final WebWolfTraceRepository traceRepository;
    private final ObjectMapper objectMapper;

    @AllArgsConstructor
    @Getter
    private class Tracert {
        private final Date date;
        private final String path;
        private final String json;
    }

    @GetMapping
    public ModelAndView get(HttpServletRequest request) {
        ModelAndView m = new ModelAndView("requests");
        List<Tracert> traces = traceRepository.findAllTraces().stream()
                .map(t -> new Tracert(t.getTimestamp(), path(t), toJsonString(t))).collect(toList());
        m.addObject("traces", traces);

        return m;
    }

    private String path(Trace t) {
        return (String) t.getInfo().getOrDefault("path", "");
    }

    private String toJsonString(Trace t) {
        try {
            return objectMapper.writeValueAsString(t.getInfo());
        } catch (JsonProcessingException e) {
            log.error("Unable to create json", e);
        }
        return "No request(s) found";
    }
}
