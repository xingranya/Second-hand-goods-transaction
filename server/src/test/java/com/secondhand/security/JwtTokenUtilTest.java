package com.secondhand.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "test-secret-key-for-jwt");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 86400L);
    }

    @Test
    public void validateTokenShouldReturnFalseWhenTokenExpired() {
        UserDetails userDetails = new User("tester", "password", AuthorityUtils.NO_AUTHORITIES);
        String expiredToken = Jwts.builder()
                .setSubject("tester")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10_000))
                .setExpiration(new Date(System.currentTimeMillis() - 1_000))
                .signWith(SignatureAlgorithm.HS512, "test-secret-key-for-jwt")
                .compact();

        boolean valid = jwtTokenUtil.validateToken(expiredToken, userDetails);

        assertFalse(valid);
    }
}
