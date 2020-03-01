package io.lite.project.pay.wepay.vo;

import io.lite.framework.common.VO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WePayOrderCreateResponse extends VO {
    private static final long serialVersionUID = 1L;

    //
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String trade_type;
    private String prepay_id;
}
