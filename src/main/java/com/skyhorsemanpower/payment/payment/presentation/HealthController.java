package com.skyhorsemanpower.payment.payment.presentation;

import com.skyhorsemanpower.payment.common.SuccessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class HealthController {

    @GetMapping("/health")
    public SuccessResponse<Object> healthCheck() {
        return new SuccessResponse<>(true);
    }
}
