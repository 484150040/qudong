### 实例配置
    接口地址: 大华ICC平台IP或者域名地址
    消息通道: 使用网关本地或者其他无限制登录的MQTT地址，保证与驱动模块DaHuaICCServer配置一致，如 "tcp://127.0.0.1:1883"
    协议版本: 请配置为 "1.0.0"
    客户端ID: 大华ICC平台提供的appKey
    客户端密钥: 大华ICC平台提供的appSecret

### 模块依赖
    本驱动模块依赖大华ICC平台服务（DaHuaICCServer）提供统一事件订阅功能；
    订阅配置: 
        订阅报警类: 61
        订阅业务类: cardRecord.offline

### 设备标签配置
```json
{
    "resource": "1000066$7$0$0"
}
```
    resource: 门通道编码

### 设备功能配置
    选择对应驱动实例即可，无需其他配置