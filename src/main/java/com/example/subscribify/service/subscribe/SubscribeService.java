package com.example.subscribify.service.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    /**
     * 구독 서비스 구매, 월 년단위 구독을 DB에 저장후
     */
    @Transactional
    public void purchaseSubscribe() {
        // 구매
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 구독 서비스 취소, 결제는 별도의 서비스로 분리
     * 취소의 경우 환불에 대한 처리가 필요함.
     * 취소는 flag 처리로 하고, 결제일이 도래한 경우 flag에 따라서 결제 처리를 하지 않음.
     */
    public void cancelSubscribe() {
        // 취소
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
