# WorkforceAI API
Copyright 2026 上海如静知华信息科技有限公司。业务接口使用 JWT 鉴权。

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录 |
| POST | `/api/ai/workforce/plan` | 劳动力供需与排班风险评估 |
| GET | `/api/admin/dashboard` | 人力计划看板 |
| GET | `/api/admin/work-orders` | 排班任务 |
| GET | `/api/workspace/dashboard` | 调度员工作台 |

输入包含预测工时、班次时长、可用人数、技能覆盖、周工时和连续班次；输出所需人数、缺口、风险和发布建议。
