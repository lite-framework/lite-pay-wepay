package io.lite.project.pay.wepay.component;


import lombok.Data;

@Data
public class WePayMerchantConfig {
    private String appid;
    private String mch_id;
    private String key;
    private String device_info;
    private String body;
    private String notify_url;
    private String trade_type;
}
