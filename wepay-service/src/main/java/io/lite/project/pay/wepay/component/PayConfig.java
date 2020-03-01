package io.lite.project.pay.wepay.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Component
@ConfigurationProperties("wepay.config")
public class PayConfig {
    private List<WePayMerchantConfig> merchantConfigs = new ArrayList<WePayMerchantConfig>();

    private Map<String, WePayMerchantConfig> merchantMap = null;

    private String notifyUrl;
    private String billIp;

    public WePayMerchantConfig getMerchantConfig(String appid) {
        if (merchantMap == null) {
            buildMerchantMap();
        }

        return merchantMap.get(appid);
    }


    private synchronized void buildMerchantMap() {

        if (merchantMap != null)
            return;

        merchantMap = new HashMap<>();

        merchantConfigs.forEach(config -> {
            merchantMap.put(config.getAppid(), config);
        });
    }

}
