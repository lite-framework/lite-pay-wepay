package io.lite.project.pay.wepay.controller;


import io.lite.framework.common.Base;
import io.lite.framework.common.ResponseCode;
import io.lite.project.pay.wepay.common.UriPrefixes;
import io.lite.project.pay.wepay.component.PayConfig;
import io.lite.project.pay.wepay.util.HttpUtil;
import io.lite.project.pay.wepay.util.PayCommonUtil;
import io.lite.project.pay.wepay.util.XMLUtil;
import io.lite.project.pay.wepay.vo.NotifyRequest;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@Slf4j
@RestController
public class WePayNotifyController extends Base {


    @Autowired
    private PayConfig payConfig;

    @RequestMapping(value = UriPrefixes.WEPAY_SERVICE_API_FOR_PORTAL + "/pay/onNotify", method = RequestMethod.POST)
    public void onNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        var notifyParams = parseParams(request);


        /**
         * on_wepay_server_notify[{appid=wxae102531d32061d0, bank_type=OTHERS, cash_fee=100, fee_type=CNY, is_subscribe=N, mch_id=1557795951, nonce_str=0002476842, openid=opMCfwT9Y58ueFF4W
         * deboH00p--k, out_trade_no=158299216741300001, result_code=SUCCESS, return_code=SUCCESS, sign=C438182F43BA5B27D626A5D17F39CAE0, time_end=20200301000254, total_fee=100, trade_type=APP, transaction_id=4200000508202003018900914869}]
         */
        info("on_wepay_server_notify[" + notifyParams + "]");

        var appId = notifyParams.get("appid");

        if (PayCommonUtil.isTenpaySign("UTF-8", notifyParams, appId)) {

            notifyBiz(notifyParams);
            responseWePayService(response);

        } else {
            info("on_wepay_server_notify[sign_fail]");
        }
    }

    private void notifyBiz(SortedMap<String, String> notifyParams) throws Exception {
        var appId = notifyParams.get("appid");
        var wePayMerchantConfig = payConfig.getMerchantConfig(appId);
        var notifyUrl = wePayMerchantConfig.getNotify_url();
        var paySuccess = "SUCCESS".equals(notifyParams.get("result_code"));

        var notifyRequest = new NotifyRequest()
                .setBank_type(notifyParams.get("bank_type"))
                .setResult_code(notifyParams.get("result_code"))
                .setOpenid(notifyParams.get("openid"))
                .setTrade_type(notifyParams.get("trade_type"))
                .setTotal_fee(notifyParams.get("total_fee"))
                .setCash_fee(notifyParams.get("cash_fee"))
                .setTransaction_id(notifyParams.get("transaction_id"))
                .setOut_trade_no(notifyParams.get("out_trade_no"))
                .setTime_end(notifyParams.get("time_end"))
                .setSuccess(paySuccess);

        // todo call biz system
        info("notify_biz[" + notifyUrl + "][" + notifyRequest + "]");
        String str = HttpUtil.postData(notifyUrl, JSON.toJSONString(notifyRequest));
        info("biz_response[" + str + "]");


        var jsonObject = JSON.parseObject(str);
        var code = (String) jsonObject.get("code");

        if (StringUtils.equals(code, ResponseCode.SUCCESS.getCode())) {
            info("notify_biz_success");
        } else {

            //  rules throw exception to make wepay server re notify us.
            throw new Exception("error");
        }


    }

    private SortedMap<String, String> parseParams(HttpServletRequest request) throws Exception {

        // rules step1 : 解析通知内容为字符串 notifyContent
        var inputStream = request.getInputStream();
        var sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        var notifyContent = sb.toString();

        // rules step2 ： 将字符串解析为params
        Map<String, String> m = XMLUtil.doXMLParse(notifyContent);

        SortedMap<String, String> notifyParams = new TreeMap<String, String>();

        Iterator it = m.keySet().iterator();

        while (it.hasNext()) {
            var parameter = (String) it.next();
            var parameterValue = m.get(parameter);

            var v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            notifyParams.put(parameter, v);
        }

        return notifyParams;
    }

    private void responseWePayService(HttpServletResponse response) throws Exception {
        var resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        var out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
