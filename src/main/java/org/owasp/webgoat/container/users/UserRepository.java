/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<WebGoatUser, String> {

  WebGoatUser findByUsername(String username);

  List<WebGoatUser> findAll();

  boolean existsByUsername(String username);
}
