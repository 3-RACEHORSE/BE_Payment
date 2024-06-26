package com.skyhorsemanpower.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.skyhorsemanpower.payment.iamport.IamportService;
import com.skyhorsemanpower.payment.iamport.IamportServiceImpl;
import com.skyhorsemanpower.payment.iamport.IamportTokenProvider;
import com.skyhorsemanpower.payment.iamport.dto.PaymentInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(IamportTokenProvider.class)
public class IamportTest {

    @Autowired
    private IamportTokenProvider iamportTokenProvider;

    @Test
    void getIamportTokenTest() {
        String token = iamportTokenProvider.getIamportToken();

        assertThat(token).isNotNull();
    }

    @Test
    void getIamportPaymentInfoTest() {
        IamportService iamportService = new IamportServiceImpl(iamportTokenProvider);
        PaymentInfoDto paymentInfoDto = iamportService.getPaymentInfo("imp_889504258470");

        assertThat(paymentInfoDto.getAmount()).isNotNull();
        assertThat(paymentInfoDto.getPayMethod()).isNotNull();
        assertThat(paymentInfoDto.getPayNumber()).isNotNull();
    }

}
