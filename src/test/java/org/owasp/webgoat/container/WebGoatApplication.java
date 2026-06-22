/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "org.owasp.webgoat.container")
@PropertySource("classpath:application-webgoat.properties")
public class WebGoatApplication {}
