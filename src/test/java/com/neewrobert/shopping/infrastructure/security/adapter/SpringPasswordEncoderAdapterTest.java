package com.neewrobert.shopping.infrastructure.security.adapter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


class SpringPasswordEncoderAdapterTest {

    private SpringPasswordEncoderAdapter springPasswordEncoderAdapter;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        springPasswordEncoderAdapter = new SpringPasswordEncoderAdapter(bCryptPasswordEncoder);
    }

    @Test
    void shouldEncodePassword() {
        var rawPassword = "123456";
        var encodedPassword = springPasswordEncoderAdapter.encode(rawPassword);

        Assertions.assertThat(encodedPassword).isNotEqualTo(rawPassword);
        Assertions.assertThat(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
