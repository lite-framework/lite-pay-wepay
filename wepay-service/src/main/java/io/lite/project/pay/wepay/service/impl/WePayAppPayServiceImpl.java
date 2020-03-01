package io.lite.project.pay.wepay.service.impl;

import io.lite.framework.common.Base;
import io.lite.framework.common.Response;
import io.lite.project.pay.wepay.Definition;
import io.lite.project.pay.wepay.component.PayConfig;
import io.lite.project.pay.wepay.component.WePayMerchantConfig;
import io.lite.project.pay.wepay.service.WePayAppPayService;
import io.lite.project.pay.wepay.util.HttpUtil;
import io.lite.project.pay.wepay.util.PayCommonUtil;
import io.lite.project.pay.wepay.util.XMLUtil;
import io.lite.project.pay.wepay.vo.WePayOrderQueryRequest;
import io.lite.project.pay.wepay.vo.WePayOrderQueryResponse;
import io.lite.project.pay.wepay.vo.WePayOrderCreateRequest;
import io.lite.project.pay.wepay.vo.WePayOrderCreateResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@SuppressWarnings("all")
public class WePayAppPayServiceImpl extends Base implements WePayAppPayService {

    @Autowired
    private PayConfig payConfig;

    @Override
    protected Logger getLogger() {
        return log;
    }


    private void addCommonParamsAndSign(SortedMap params, WePayMerchantConfig merchantConfig) {

        // 生成随机字符串
        String currTime = PayCommonUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayCommonUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        params.put("appid", merchantConfig.getAppid());
        params.put("mch_id", merchantConfig.getMch_id());
        params.put("nonce_str", nonce_str);
        params.put("trade_type", merchantConfig.getTrade_type());
        params.put("spbill_create_ip", payConfig.getBillIp());
        params.put("notify_url", payConfig.getNotifyUrl());
        params.put("body", merchantConfig.getBody());// 商品描述

        String sign = PayCommonUtil.createSign("UTF-8", params, merchantConfig.getKey());
        params.put("sign", sign);// 签名
    }


    // rules 微信统一下单接口 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
    @Override
    public Response<WePayOrderCreateResponse> createPayOrder(WePayOrderCreateRequest request) {
        return exec(() -> {

            info("" + request);

            WePayMerchantConfig merchantConfig = payConfig.getMerchantConfig(request.getAppid());
            SortedMap<Object, Object> params = new TreeMap<Object, Object>();


            params.put("out_trade_no", request.getOrderNo());// 商户订单号
            params.put("total_fee", request.getAmount());// 总金额

            addCommonParamsAndSign(params, merchantConfig);

            info("params=" + params);
            String requestXML = PayCommonUtil.getRequestXml(params);

            info("requestXML=" + requestXML);

            String resXml = HttpUtil.postData(Definition.UNIFIED_ORDER_URL, requestXML);
            info("resXml=" + resXml);

            Map map = XMLUtil.doXMLParse(resXml);
            info("map=" + map);

            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {


                    String appid = (String) map.get("appid");
                    String mch_id = (String) map.get("mch_id");
                    String nonce_str = (String) map.get("nonce_str");
                    String sign = (String) map.get("sign");
                    String trade_type = (String) map.get("trade_type");
                    String prepay_id = (String) map.get("prepay_id");

                    var wePayOrderCreateResponse = new WePayOrderCreateResponse().setAppid(appid)
                            .setMch_id(mch_id).setNonce_str(nonce_str).setSign(sign)
                            .setTrade_type(trade_type).setPrepay_id(prepay_id);

                    return wePayOrderCreateResponse;
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    errIf(true, "pay_error");
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                errIf(true, "pay_error");
            }


            return null;
        });
    }

    @Override
    public Response<WePayOrderQueryResponse> queryOrder(WePayOrderQueryRequest request) {
        return exec(() -> {

            info("" + request);

            return null;
        });
    }


}
