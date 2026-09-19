/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai; import cn.zhuatech.workforceai.service.WorkforceAnalysisService; import org.junit.jupiter.api.Test; import static org.assertj.core.api.Assertions.assertThat;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class WorkforceAnalysisServiceTests {private final WorkforceAnalysisService s=new WorkforceAnalysisService();
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void replansUnderstaffedFatigueShift(){var r=s.plan(new WorkforceAnalysisService.Request("SC-88",120,8,8,60,55,7));assertThat(r.decision()).isEqualTo("REPLAN");assertThat(r.staffingGap()).isPositive();}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void publishesHealthySchedule(){var r=s.plan(new WorkforceAnalysisService.Request("SC-20",64,8,10,95,38,4));assertThat(r.decision()).isEqualTo("PUBLISH");}}
