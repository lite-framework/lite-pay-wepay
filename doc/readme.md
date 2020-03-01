# 设计

1. 此系统不落单，app 调用此系统此系统直接加工请求并透传给微信服务器，当微信服务器回调时，也直接将请求透传给各个业务系统
1. 支持多个app （appid） 同时使用，无需为多个app 部署多个实例，使用 bizKey 对app 进行标识。
1. 每一个 配置项 [wepay.config.merchantConfigs[0].xxx] 对应一个 WePayMerchantConfig 配置，这些参数可以从微信文档中找到
1. wepay.config.merchantConfigs[0].notify_url 应该为每个 app 接收本模块的回调的 url，应该是一个内网url
1. wepay.config.notifyUrl 为， 当前服务接收微信回调的url，需要是一个公网可访问的url

