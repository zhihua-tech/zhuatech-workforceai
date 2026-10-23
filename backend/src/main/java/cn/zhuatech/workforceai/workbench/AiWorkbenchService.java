/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai.workbench;

import static cn.zhuatech.workforceai.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "minimumRestMinutes", "shifts");
    int rest = integer(r, "minimumRestMinutes", 0, 4320);
    Set<String> ids = new HashSet<>();
    Map<String, List<Map<String, Object>>> groups = new TreeMap<>();
    for (var s : rows(r, "shifts", 1, 300)) {
      keys(s, "id", "employee", "start", "end");
      unique(ids, id(s, "id"));
      String employee = id(s, "employee");
      var a = local(text(s, "start", 30));
      var b = local(text(s, "end", 30));
      long duration = java.time.Duration.between(a, b).toMinutes();
      if (duration <= 0 || duration > 1440) throw new IllegalArgumentException("单班时长须大于0且不超过24小时");
      groups.computeIfAbsent(employee, k -> new ArrayList<>()).add(s);
    }
    List<Map<String, Object>> conflicts = new ArrayList<>();
    for (var e : groups.entrySet()) {
      var shifts = e.getValue();
      shifts.sort(
          Comparator.<Map<String, Object>, java.time.LocalDateTime>comparing(
                  s -> local(s.get("start").toString()))
              .thenComparing(s -> s.get("id").toString()));
      for (int i = 0; i < shifts.size(); i++)
        for (int j = i + 1; j < shifts.size(); j++) {
          var a = shifts.get(i);
          var b = shifts.get(j);
          var ae = local(a.get("end").toString());
          var bs = local(b.get("start").toString());
          if (bs.isBefore(ae)) {
            var end =
                ae.isBefore(local(b.get("end").toString())) ? ae : local(b.get("end").toString());
            conflicts.add(
                Map.of(
                    "employee",
                    e.getKey(),
                    "first",
                    a.get("id"),
                    "second",
                    b.get("id"),
                    "type",
                    "OVERLAP",
                    "minutes",
                    java.time.Duration.between(bs, end).toMinutes()));
          }
        }
      var blockEnd = local(shifts.getFirst().get("end").toString());
      String lastId = shifts.getFirst().get("id").toString();
      for (int j = 1; j < shifts.size(); j++) {
        var next = shifts.get(j);
        var start = local(next.get("start").toString());
        var end = local(next.get("end").toString());
        if (start.isBefore(blockEnd)) {
          if (end.isAfter(blockEnd)) {
            blockEnd = end;
            lastId = next.get("id").toString();
          }
          continue;
        }
        long gap = java.time.Duration.between(blockEnd, start).toMinutes();
        if (gap < rest)
          conflicts.add(
              Map.of(
                  "employee",
                  e.getKey(),
                  "first",
                  lastId,
                  "second",
                  next.get("id"),
                  "type",
                  "REST_SHORTAGE",
                  "minutes",
                  rest - gap));
        blockEnd = end;
        lastId = next.get("id").toString();
      }
    }
    return Map.of(
        "conflicts",
        conflicts,
        "conflictCount",
        conflicts.size(),
        "timeBasis",
        "ORGANIZATION_LOCAL",
        "automaticReschedule",
        false);
  }
}
