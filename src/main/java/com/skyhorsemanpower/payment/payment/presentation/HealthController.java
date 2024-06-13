package com.skyhorsemanpower.payment.payment.presentation;

import com.skyhorsemanpower.payment.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class HealthController {

    @GetMapping
    public SuccessResponse<Object> healthCheck() {
        return new SuccessResponse<>(true);
    }
}
