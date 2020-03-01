package io.lite.project.pay.wepay.vo;

import io.lite.framework.common.VO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NotifyRequest extends VO {
    private static final long serialVersionUID = 1L;

    // 我方的订单号
    private String result_code;
    private String openid;
    private String trade_type;
    private String bank_type;
    private String total_fee;
    private String cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;
    private Boolean success;
}
