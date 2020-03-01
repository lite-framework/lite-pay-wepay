package io.lite.project.pay.wepay.service;

import io.lite.project.pay.wepay.AppTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WePayAppPayServiceTest extends AppTest {
    @Autowired
    WePayAppPayService wePayAppPayService;


    @Test
    public void test() {
        wePayAppPayService.test();
    }

}
