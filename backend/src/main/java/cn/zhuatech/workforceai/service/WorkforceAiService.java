/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai.service;
import cn.zhuatech.workforceai.common.BusinessException; import cn.zhuatech.workforceai.dto.WorkforceAiDto.*; import cn.zhuatech.workforceai.model.*; import cn.zhuatech.workforceai.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service @Transactional(readOnly=true) public class WorkforceAiService {
 private final WorkRecordRepository orders; private final ActivityRecordRepository reports; private final ResourceRegisterRepository resources; private final ReviewRecordRepository reviews; private final CurrentUserService current; /**
                                                                                                                                                                                                                                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                         */
public WorkforceAiService(WorkRecordRepository orders,ActivityRecordRepository reports,ResourceRegisterRepository resources,ReviewRecordRepository reviews,CurrentUserService current){this.orders=orders;this.reports=reports;this.resources=resources;this.reviews=reviews;this.current=current;}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public Dashboard shopfloorDashboard(){String unit=current.get().getOperatingUnitCode();return dashboard(unit==null?orders.findAllByOrderByDueDateAsc():orders.findByOperatingUnitCodeOrderByDueDateAsc(unit));} /**
                                                                                                                                                                                                                  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                  */
public Dashboard adminDashboard(){return dashboard(orders.findAllByOrderByDueDateAsc());} /**
                                                                                                                                                                                                                                                                                                            * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                            */
public List<WorkRecordView> workRecords(){return orders.findAllByOrderByDueDateAsc().stream().map(this::view).toList();}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Transactional public ReportResult report(Long id,ReportRequest request){WorkRecord item=orders.findById(id).orElseThrow(()->new BusinessException("排班规划任务不存在"));if(item.getStatus()==WorkRecord.Status.COMPLETED)throw new BusinessException("已归档排班不能继续反馈");if(item.getCompletedQty()+request.goodQty()>item.getPlannedQty())throw new BusinessException("完成岗位不能超过计划岗位");item.report(request.goodQty(),request.defectQty());reports.save(new ActivityRecord(item,request.operationName(),request.goodQty(),request.defectQty(),current.get().getFullName(),request.remark()));return new ReportResult(item.getRecordNo(),item.getCompletedQty(),item.getDefectQty(),progress(item),item.getStatus().name());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private Dashboard dashboard(List<WorkRecord> list){int planned=list.stream().mapToInt(WorkRecord::getPlannedQty).sum(),done=list.stream().mapToInt(WorkRecord::getCompletedQty).sum(),exceptions=list.stream().mapToInt(WorkRecord::getDefectQty).sum(),rate=planned==0?0:Math.round(done*100f/planned);List<Metric> metrics=List.of(new Metric("人员岗位",planned+"",list.size()+" 条排班","blue"),new Metric("规划完成率",rate+"%",done+" / "+planned,"green"),new Metric("时效风险",exceptions+"","负荷、请假与关键岗位","warn"),new Metric("调度审批",resources.countByStatus(ResourceRegister.Status.ALARM)+"",reviews.countByResult(ReviewRecord.Result.PENDING)+" 条待确认","red"));return new Dashboard(metrics,list.stream().map(this::view).toList(),resources.findAllByOrderByCodeAsc().stream().map(e->new ControlView(e.getCode(),e.getName(),e.getOperatingUnit().getName(),e.getStatus().name(),e.getOee(),e.getLastHeartbeat())).toList(),reviews.findTop10ByOrderByIdDesc().stream().map(i->new ReviewRecordView(i.getReviewRecordNo(),i.getWorkRecord().getRecordNo(),i.getWorkRecord().getSubjectName(),i.getReviewRecordType(),i.getReviewRecordQty(),i.getDefectQty(),i.getResult().name(),i.getInspector())).toList());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private WorkRecordView view(WorkRecord o){return new WorkRecordView(o.getId(),o.getRecordNo(),o.getSubjectCode(),o.getSubjectName(),o.getOperatingUnit().getName(),o.getOperatingUnit().getWorkshop(),o.getPlannedQty(),o.getCompletedQty(),o.getDefectQty(),o.getDueDate(),o.getStatus().name(),o.getVersionNo(),progress(o));} /**
                                                                                                                                                                                                                                                                                                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                                                   */
private int progress(WorkRecord o){return o.getPlannedQty()==0?0:Math.min(100,Math.round(o.getCompletedQty()*100f/o.getPlannedQty()));}
}
