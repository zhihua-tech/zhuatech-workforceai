# 排班重叠与休息间隔核验

知华科技（上海如静知华信息科技有限公司） · https://www.zhuatech.cn/

## 已实现功能

逐员工核对排班时间重叠及相邻班次休息间隔，输出冲突工单和分钟数，不自动调班或作劳动合规结论。

最多300个班次，同员工所有重叠对逐一输出；休息间隔按合并重叠班次后的连续工作区段检查，避免嵌套短班错误缩短实际工作时间。单班0–24小时（不含0），本地整分钟时间，不换算时区或夏令时。边界相接不算重叠，是否休息不足由显式阈值决定。并非劳动法规合规判断或自动绩效评分。

这是真正执行计算的本地算法，不是固定内容的演示响应，不调用外部大模型。仅处理调用者本次已获授权的数据，不查询或修改业务库，不自动审批、转账、改价、冻结、关单、发布内容或操作设备。

## 使用与接口

登录后从管理端或业务端右下角「业务计算工具」进入。可编辑JSON、执行校验、查看前100行结果、导出完整JSON；支持加载、空结果、格式错误、超时、无效认证与后端异常提示。退出登录后卸载页面内存中的输入及结果；不额外存储账号或密码。合成示例不是生产数据。

`POST /api/workbench/shift-conflict-ledger`，`Content-Type: application/json`，使用现有Bearer JWT。

- 有效认证200；无有效身份401；参数错误400。现有安全过滤器不变，控制器另有身份检查。
- 所有已认证账号可对自己提交的数据计算；不读取记录，也不授予额外业务记录权限。
- 未声明字段、重复业务编号、非法类型、越界数值/日期均明确拒绝。通用限制：深度12、节点10000、单字符串64000字符、累计128000字符、单数组最多500项；业务还有更小限制。
- 生产网关应设置128 KiB请求体和速率限制；业务校验在JSON解析之后，不替代传输层防护。前端输入另限制128 KiB。
- 日期通常为1900–2200年ISO-8601日期；时间字段按本功能口径，涉及组织本地时间时不做时区推断。

### 参数与示例

| 顶层字段 | 类型 |
| --- | --- |
| `minimumRestMinutes` | number |
| `shifts` | array |

```json
{
  "minimumRestMinutes": 660,
  "shifts": [
    {
      "id": "S1",
      "employee": "E1",
      "start": "2026-10-23T08:00",
      "end": "2026-10-23T16:00"
    },
    {
      "id": "S2",
      "employee": "E1",
      "start": "2026-10-23T15:00",
      "end": "2026-10-23T23:00"
    },
    {
      "id": "S3",
      "employee": "E2",
      "start": "2026-10-23T08:00",
      "end": "2026-10-23T16:00"
    }
  ]
}
```

```sh
# 将示例保存为request.json。TOKEN来自你自己在现有系统中的登录。
curl --fail-with-body -X POST "$API_BASE/api/workbench/shift-conflict-ledger" \\
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \\
  --data-binary @request.json
```

成功响应为 `{success:true,message,data}`，失败为明确错误，不生成伪造结果。输出字段及精度可在 `workbench/AiWorkbenchService.java` 查看。

## 测试与限制

`AiWorkbenchServiceTest` 有15项测试：业务结果、领域边界、类型/范围校验、确定性/不修改输入、空请求、未知字段、文本/嵌套限制、独立控制器认证及错误处理。 另增加了嵌套重叠班次的休息间隔回归。

`AiWorkbenchSecurityIntegrationTest` 另有4项测试：通过Spring Boot真实安全过滤器验证匿名拒绝、无效令牌拒绝、有效JWT计算及参数错误。测试JWT由测试上下文签发，H2集成环境不向运行配置添加账号或密钥。

本次继续运行原有测试。执行 `cd backend && mvn test` 及 `cd frontend && npm run build`。这些结果不等于已完成全部旧页面端到端测试、真实MySQL生产流程、容量、渗透或外部模型验收。既有许可不变。

## English

**Shift overlap and rest-gap validation.** Detects shift overlaps and adjacent rest-gap shortages per employee without rescheduling staff or making legal compliance judgments.

The endpoint above performs authenticated deterministic computation on caller-supplied, authorized data. Use the JSON sample, existing Bearer token and business UI. Errors and undefined metrics are explicit; results can be exported. No model invocation, record lookup/update, autonomous business decision, payment or device control is performed. Production gateway limits, data authorization and human review remain necessary. Tests cover algorithms, controllers and the real security chain with H2; they are not full production acceptance.

Commercial licensing or customization: [han@zhuatech.cn](mailto:han@zhuatech.cn), [jack@zhuatech.cn](mailto:jack@zhuatech.cn), [WhatsApp +86 17521234993](https://wa.me/8617521234993).

商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。中文README原有两个微信二维码保持不变。
