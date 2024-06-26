package com.skyhorsemanpower.payment.iamport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyhorsemanpower.payment.iamport.dto.PaymentInfoDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamportServiceImpl implements IamportService {

    private static final String IAMPORT_PAYMENT_URL = "https://api.iamport.kr/payments/";

    private final IamportTokenProvider iamportTokenProvider;

    private String getIamportToken() {
        return iamportTokenProvider.getIamportToken();
    }

    @Override
    public PaymentInfoDto getPaymentInfo(String impUid) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(IAMPORT_PAYMENT_URL)
                .pathSegment(impUid)
                .toUriString();
            String authorization = String.format("Bearer %s", getIamportToken());

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", authorization)
                .GET()
                .build();

            ObjectMapper mapper = new ObjectMapper();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String responseBody = response.body();
            JsonNode rootNode = mapper.readTree(responseBody);
            JsonNode resNode = rootNode.get("response");
            return parseIamportPaymentResponse(resNode);
        } catch (InterruptedException | IOException | RuntimeException e) {
            log.info("getPaymentInfo error: {}", e.getMessage());
            return null;
        }
    }

    private PaymentInfoDto parseIamportPaymentResponse(JsonNode resNode) {
        JsonNode bankCode = resNode.get("bank_code");
        JsonNode bankName = resNode.get("bank_name");
        JsonNode cardCode = resNode.get("card_number");
        JsonNode cardName = resNode.get("card_name");
        JsonNode vbankCode = resNode.get("vcard_code");
        JsonNode vbankName = resNode.get("vbank_name");

        String payMethod = "etc";
        String payNumber = "etc";
        if (!bankCode.isNull() && !bankName.isNull()) {
            payMethod = bankName.asText();
            payNumber = bankCode.asText();
        } else if (!cardCode.isNull() && !cardName.isNull()) {
            payMethod = cardName.asText();
            payNumber = cardCode.asText();
        } else if (!vbankCode.isNull() && !vbankName.isNull()) {
            payMethod = vbankName.asText();
            payNumber = vbankCode.asText();
        }
        return PaymentInfoDto.builder()
            .payMethod(payMethod)
            .payNumber(payNumber)
            .amount(resNode.get("amount").decimalValue())
            .build();
    }
}
