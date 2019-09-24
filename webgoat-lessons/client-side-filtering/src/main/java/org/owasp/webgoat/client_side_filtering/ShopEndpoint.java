/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.client_side_filtering;

import com.beust.jcommander.internal.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.owasp.webgoat.client_side_filtering.ClientSideFilteringFreeAssignment.SUPER_COUPON_CODE;

/**
 * @author nbaars
 * @since 4/6/17.
 */
@RestController
@RequestMapping("/clientSideFiltering/challenge-store")
public class ShopEndpoint {

    @AllArgsConstructor
    private class CheckoutCodes {

        @Getter
        private List<CheckoutCode> codes;

        public Optional<CheckoutCode> get(String code) {
            return codes.stream().filter(c -> c.getCode().equals(code)).findFirst();
        }
    }

    @AllArgsConstructor
    @Getter
    private class CheckoutCode {
        private String code;
        private int discount;
    }

    private CheckoutCodes checkoutCodes;

    public ShopEndpoint() {
        List<CheckoutCode> codes = Lists.newArrayList();
        codes.add(new CheckoutCode("webgoat", 25));
        codes.add(new CheckoutCode("owasp", 25));
        codes.add(new CheckoutCode("owasp-webgoat", 50));
        this.checkoutCodes = new CheckoutCodes(codes);
    }

    @GetMapping(value = "/coupons/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckoutCode getDiscountCode(@PathVariable String code) {
        if (SUPER_COUPON_CODE.equals(code)) {
            return new CheckoutCode(SUPER_COUPON_CODE, 100);
        }
        return checkoutCodes.get(code).orElse(new CheckoutCode("no", 0));
    }

    @GetMapping(value = "/coupons", produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckoutCodes all() {
        List<CheckoutCode> all = Lists.newArrayList();
        all.addAll(this.checkoutCodes.getCodes());
        all.add(new CheckoutCode(SUPER_COUPON_CODE, 100));
        return new CheckoutCodes(all);
    }
}
