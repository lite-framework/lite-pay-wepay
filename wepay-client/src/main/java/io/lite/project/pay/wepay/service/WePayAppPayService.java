package io.lite.project.pay.wepay.service;

import io.lite.framework.common.Response;
import io.lite.project.pay.wepay.vo.WePayOrderQueryRequest;
import io.lite.project.pay.wepay.vo.WePayOrderQueryResponse;
import io.lite.project.pay.wepay.vo.WePayOrderCreateRequest;
import io.lite.project.pay.wepay.vo.WePayOrderCreateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static io.lite.project.pay.wepay.common.UriPrefixes.WEPAY_SERVICE_API_ROOT;

public interface WePayAppPayService {

    @RequestMapping(value = WEPAY_SERVICE_API_ROOT + "/pay/wepay/appPay/createOrder", method = RequestMethod.POST)
    Response<WePayOrderCreateResponse> createPayOrder(@RequestBody WePayOrderCreateRequest request);

    @RequestMapping(value = WEPAY_SERVICE_API_ROOT + "/pay/wepay/appPay/queryOrder", method = RequestMethod.POST)
    Response<WePayOrderQueryResponse> queryOrder(@RequestBody WePayOrderQueryRequest request);


}
