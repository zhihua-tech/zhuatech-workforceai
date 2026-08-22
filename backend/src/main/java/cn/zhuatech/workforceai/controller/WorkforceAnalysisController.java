/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai.controller;

import cn.zhuatech.workforceai.common.ApiResponse;
import cn.zhuatech.workforceai.service.WorkforceAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/workforce")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class WorkforceAnalysisController {
    private final WorkforceAnalysisService service;
    public WorkforceAnalysisController(WorkforceAnalysisService service) { this.service = service; }
    @PostMapping("/plan")
    public ApiResponse<WorkforceAnalysisService.Result> plan(@Valid @RequestBody WorkforceAnalysisService.Request request) {
        return ApiResponse.ok("智能排班建议已生成", service.plan(request));
    }
}
