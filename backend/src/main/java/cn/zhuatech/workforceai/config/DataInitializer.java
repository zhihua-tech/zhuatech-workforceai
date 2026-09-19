/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai.config;
import cn.zhuatech.workforceai.model.*; import cn.zhuatech.workforceai.repository.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder; import java.time.LocalDate; import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Configuration public class DataInitializer {/**
                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                              */
@Bean CommandLineRunner seed(OperatingUnitRepository units,WorkRecordRepository tasks,ResourceRegisterRepository resources,ReviewRecordRepository reviews,UserRepository users,PasswordEncoder encoder){return args->{if(units.count()>0)return;
 var u1=units.save(new OperatingUnit("WORKFORCE-CENTER","劳动力计划中心","运营总部",320));var u2=units.save(new OperatingUnit("SUZHOU-OPS","苏州运营调度组","苏州运营中心",220));var u3=units.save(new OperatingUnit("SKILL-POOL","关键技能支援组","运营支持中心",100));
 var t1=tasks.save(new WorkRecord("SC-260819-118","OPS-SZ-08","苏州履约中心晚班",u2,18,12,6,LocalDate.now(),WorkRecord.Status.RUNNING,"晚班+关键岗"));var t2=tasks.save(new WorkRecord("SC-260819-112","CS-EAST-03","华东客服高峰班",u1,22,20,2,LocalDate.now(),WorkRecord.Status.RUNNING,"弹性班"));var t3=tasks.save(new WorkRecord("SC-260819-106","MRO-02","设备维保周末班",u3,8,5,3,LocalDate.now(),WorkRecord.Status.RELEASED,"专业技能"));var t4=tasks.save(new WorkRecord("SC-260819-099","WH-05","仓配早高峰班",u2,16,16,0,LocalDate.now(),WorkRecord.Status.COMPLETED,"早班"));
 resources.saveAll(List.of(new ResourceRegister("ATTEND-01","考勤与可用性数据",u1,ResourceRegister.Status.RUNNING,99),new ResourceRegister("SKILL-02","岗位技能矩阵",u1,ResourceRegister.Status.RUNNING,98),new ResourceRegister("FORECAST-03","需求预测服务",u3,ResourceRegister.Status.ALARM,94)));reviews.saveAll(List.of(new ReviewRecord("RV-260819-032",t1,"缺员班次复核",18,6,ReviewRecord.Result.PENDING,"程望舒"),new ReviewRecord("RV-260819-027",t4,"班表发布复核",16,0,ReviewRecord.Result.PASSED,"陆青"),new ReviewRecord("RV-260819-018",t3,"技能覆盖复核",8,3,ReviewRecord.Result.FAILED,"程望舒")));
 String demo=encoder.encode("Demo@2026");users.saveAll(List.of(new UserAccount("operator",demo,"叶宁",UserAccount.Role.DOMAIN_USER,"EAST-DISPATCH"),new UserAccount("planner",demo,"顾清",UserAccount.Role.DOMAIN_OPERATOR,null),new UserAccount("quality",demo,"苏岚",UserAccount.Role.QUALITY,null),new UserAccount("admin",encoder.encode("ZhuaTech@2026"),"系统管理员",UserAccount.Role.ADMIN,null)));};}}
