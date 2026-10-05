/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Sends the user back to the files page when an upload exceeds the configured multipart limits,
 * instead of ending up on the generic error page.
 *
 * <p>Spring parses the multipart request before it resolves the handler, so {@link
 * MaxUploadSizeExceededException} is thrown before {@link FileServer} is invoked. A local exception
 * handler inside the controller would therefore never be called, which is why this lives in a
 * {@link ControllerAdvice}.
 */
@ControllerAdvice
@Slf4j
public class FileUploadExceptionAdvice {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ModelAndView handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
    log.debug("Upload rejected, maximum upload size exceeded", e);

    return new ModelAndView(
        new RedirectView("files", true),
        new ModelMap().addAttribute("uploadSuccess", FileServer.UPLOAD_TOO_LARGE));
  }
}
