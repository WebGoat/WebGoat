package org.owasp.webgoat.service;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.model.LessonInfoModel;
import org.owasp.webgoat.lessons.model.LessonMenuItem;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

@Controller
public class LessonInfoService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(LessonMenuService.class);

    @RequestMapping(value = "/lessoninfo.mvc", produces = "application/json")
    public @ResponseBody
    LessonInfoModel getLessonInfo(HttpSession session) {
        WebSession webSession = getWebSession(session);
        return new LessonInfoModel(webSession);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex) {
        return "An error occurred retrieving the LessonInfoModel:" + ex.getMessage();
    }

    protected LessonInfoModel getLessonInfoModel(WebSession webSession) {
      return new LessonInfoModel(webSession);
    }


}
