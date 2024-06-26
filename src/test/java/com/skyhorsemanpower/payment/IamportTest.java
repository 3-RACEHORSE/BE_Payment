package com.skyhorsemanpower.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.skyhorsemanpower.payment.iamport.IamportTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IamportTest {

    @Autowired
    private IamportTokenProvider iamportTokenProvider;

    @Test
    void getIamportTokenTest() {
        String token = iamportTokenProvider.getIamportToken();

        assertThat(token).isNotNull();
    }

}
