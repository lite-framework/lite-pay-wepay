package io.lite.project.pay.wepay.vo;

import io.lite.framework.common.PortalRequest;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WePayOrderCreateRequest extends PortalRequest {
    private static final long serialVersionUID = 1L;

    // 我方的订单号
    private String orderNo;

    // 支付金额，单位元
    private Integer amount;

    //
    private String appid;

}
