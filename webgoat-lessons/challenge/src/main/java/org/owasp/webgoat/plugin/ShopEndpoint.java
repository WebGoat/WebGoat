package org.owasp.webgoat.plugin;

import com.beust.jcommander.internal.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.owasp.webgoat.plugin.SolutionConstants.SUPER_COUPON_CODE;

/**
 * @author nbaars
 * @since 4/6/17.
 */
@RestController
public class ShopEndpoint {

    @AllArgsConstructor
    private class CouponCodes {

        @Getter
        private List<CouponCode> codes = Lists.newArrayList();

        public boolean contains(String code) {
            return codes.stream().anyMatch(c -> c.getCode().equals(code));
        }
    }

    @AllArgsConstructor
    @Getter
    private class CouponCode {
        private String code;
        private int discount;
    }

    private CouponCodes couponCodes;

    public ShopEndpoint() {
        List<CouponCode> codes = Lists.newArrayList();
        for (int i = 0; i < 9; i++) {
            codes.add(new CouponCode(RandomStringUtils.random(10), i * 100));
        }
        this.couponCodes = new CouponCodes(codes);
    }

    @GetMapping(value = "/coupons/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CouponCodes getDiscountCodes(@PathVariable String user) {
        if ("Tom".equals(user)) {
            return couponCodes;
        }
        return null;
    }

    @GetMapping(value = "/coupons/valid/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isValidCouponCode(@PathVariable String code) {
        return couponCodes.contains(code);
    }

    @GetMapping(value = "/coupons", produces = MediaType.APPLICATION_JSON_VALUE)
    public CouponCodes coupons() {
        List<CouponCode> all = Lists.newArrayList();
        all.addAll(this.couponCodes.getCodes());
        all.add(new CouponCode(SUPER_COUPON_CODE, 100));
        return new CouponCodes(all);
    }
}
