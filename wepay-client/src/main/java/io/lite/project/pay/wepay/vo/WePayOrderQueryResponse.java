package io.lite.project.pay.wepay.vo;

import io.lite.framework.common.VO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WePayOrderQueryResponse extends VO {
    private static final long serialVersionUID = 1L;


    private String trade_state;
    private String bank_type;
    private String total_fee;
    private String cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;
    private String trade_state_desc;
}
