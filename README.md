# 知华智能劳动力排班平台（WorkforceAI）


## 本次升级：排班净工时与临时增援缺口

扣休息及已预留工时后，计算计划任务分钟与新增人力需要，临时人员按整人向上取整。不自动排班或解释劳动法上限。

新增已认证的结构化录入工作台（`/operations.html`），支持批量核算、逐条结果依据、分级复核和CSV/完整JSON导出。结果由真实后端算法计算，不是预置演示数字。所有数值精确传递，不调用外部模型。

这是**只读核算**：不写库、不审批、不自动执行财务、签署、销毁或设备控制。输入条件满足不是执行授权。[算法、输入边界与接口说明](docs/OPERATIONS-REVIEW.md)。
## 本次功能更新：排班重叠与休息间隔核验

逐员工核对排班时间重叠及相邻班次休息间隔，输出冲突工单和分钟数，不自动调班或作劳动合规结论。 登录后从「业务计算工具」使用，支持校验与完整结果导出。详见[算法口径、接口和测试说明](docs/AI_OPERATIONS_UPGRADE_20261023.md)。

[简体中文](README.md) | [English](README.en.md)

[知华科技官网](https://www.zhuatech.cn/) · 上海如静知华信息科技有限公司 · `cn.zhuatech.workforceai`

WorkforceAI 面向门店、客服、仓储、交付和制造现场，把需求预测、人员可用性、技能矩阵、周工时与连续班次转化为可解释班表建议。

![劳动力计划与排班中心](docs/images/workforceai-admin.png)

## 从需求到班表

1. 汇总订单、客流、工单或产量，形成未来需求工时。
2. 读取在岗、休假、技能资质和岗位适配信息。
3. 计算需求人数、人员缺口、技能覆盖和疲劳风险。
4. 产生 `PUBLISH / SUPERVISOR_REVIEW / REPLAN` 建议。
5. 由调度员调整、主管审批后发布，员工端确认或申请换班。

![当班调度移动端](docs/images/workforceai-h5.png)

### 已包含

- 管理端供需看板、排班任务、技能矩阵、规则、主管复核和分析；
- 移动端班表确认、调班意见、人员资源和风险升级；
- 工时与连续班次门禁、关键岗位资格校验和完整审计记录；
- Java/Vue 前后端分离、MySQL、Docker Compose 与自动化测试。

## 运行

```bash
docker compose up --build
```

访问 `http://localhost:5173`；账号 `admin / Demo@2026`、`operator / Demo@2026`。排班评估接口：`POST /api/ai/workforce/plan`。架构与数据模型见 [docs](docs/architecture.md)。

## 使用限制与合作

本项目仅能用于个人非商业学习交流，**不得商用**。企业内部部署、生产使用、SaaS、客户交付、收费服务、品牌替换或商业发行，必须事先取得上海如静知华信息科技有限公司书面授权，以 [LICENSE](LICENSE) 为准。

需要智能排班、人力数字化、ERP/OA 集成、AI 转型、FDE 与软件项目外包，请联系[知华科技](https://www.zhuatech.cn/)：

| 咨询微信一 | 咨询微信二 |
| --- | --- |
| ![微信咨询一](docs/images/zhuatech-wechat-consulting.png) | ![微信咨询二](docs/images/zhuatech-wechat-consulting-2.png) |

Copyright © 2026 上海如静知华信息科技有限公司
