/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.workforceai.service;
import jakarta.validation.constraints.*; import org.springframework.stereotype.Service; import java.util.*;
/** 基于需求、技能、工时和连续班次生成可解释排班建议。 */
@Service public class WorkforceAnalysisService {
 public Result plan(Request r){int required=Math.max(1,(int)Math.ceil(r.forecastWorkloadHours()/r.shiftHours())),gap=required-r.availableEmployees(),risk=0;List<String>a=new ArrayList<>();if(gap>0){risk+=Math.min(45,gap*12);a.add("预测需求存在人员缺口");}if(r.skillCoveragePercent()<80){risk+=25;a.add("关键技能覆盖不足");}if(r.averageWeeklyHours()>40){risk+=Math.min(20,r.averageWeeklyHours()-40);a.add("平均周工时偏高");}if(r.maxConsecutiveShifts()>5){risk+=18;a.add("连续班次触及疲劳门槛");}risk=Math.min(100,risk);String d=risk>=70?"REPLAN":risk>=35?"SUPERVISOR_REVIEW":"PUBLISH";if(a.isEmpty())a.add("需求、技能和工时约束均满足");return new Result(r.scheduleNo(),required,gap,risk,d,a,risk>=35,gap>0?"启用弹性班组或跨岗支援":"按建议班表发布");}
 public record Request(@NotBlank String scheduleNo,@DecimalMin("0.1")double forecastWorkloadHours,@DecimalMin("1")double shiftHours,@Min(0)int availableEmployees,@Min(0)@Max(100)int skillCoveragePercent,@Min(0)@Max(100)int averageWeeklyHours,@Min(0)@Max(14)int maxConsecutiveShifts){}
 public record Result(String scheduleNo,int requiredEmployees,int staffingGap,int riskScore,String decision,List<String>alerts,boolean supervisorApprovalRequired,String recommendation){}
}
