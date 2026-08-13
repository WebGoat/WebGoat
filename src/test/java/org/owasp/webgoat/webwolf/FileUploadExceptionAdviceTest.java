/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.view.RedirectView;

class FileUploadExceptionAdviceTest {

  @Test
  @DisplayName("An upload which exceeds the maximum upload size redirects back to the files page")
  void shouldRedirectToFilesWithMessage() {
    var modelAndView =
        new FileUploadExceptionAdvice()
            .handleMaxUploadSizeExceeded(new MaxUploadSizeExceededException(1024));

    Assertions.assertThat(modelAndView.getView()).isInstanceOf(RedirectView.class);
    Assertions.assertThat(((RedirectView) modelAndView.getView()).getUrl()).isEqualTo("files");
    Assertions.assertThat(modelAndView.getModel())
        .containsEntry("uploadSuccess", "File is too large to upload");
  }
}
