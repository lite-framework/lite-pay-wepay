package io.lite.project.pay.wepay.controller;

import io.lite.framework.common.Response;
import io.lite.project.pay.wepay.vo.NotifyRequest;

public interface WePayNotifyListener {
    Response<Void> onWePayNotify(NotifyRequest request);
}
