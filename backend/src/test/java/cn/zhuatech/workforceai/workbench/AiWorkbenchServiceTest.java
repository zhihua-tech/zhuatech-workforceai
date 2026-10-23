/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.workforceai.workbench;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class AiWorkbenchServiceTest {
  private final AiWorkbenchService service = new AiWorkbenchService();

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  private Map<String, Object> sample() {
    return Map.<String, Object>ofEntries(
        Map.entry("minimumRestMinutes", 660),
        Map.entry(
            "shifts",
            List.of(
                Map.<String, Object>ofEntries(
                    Map.entry("id", "S1"),
                    Map.entry("employee", "E1"),
                    Map.entry("start", "2026-10-23T08:00"),
                    Map.entry("end", "2026-10-23T16:00")),
                Map.<String, Object>ofEntries(
                    Map.entry("id", "S2"),
                    Map.entry("employee", "E1"),
                    Map.entry("start", "2026-10-23T15:00"),
                    Map.entry("end", "2026-10-23T23:00")),
                Map.<String, Object>ofEntries(
                    Map.entry("id", "S3"),
                    Map.entry("employee", "E2"),
                    Map.entry("start", "2026-10-23T08:00"),
                    Map.entry("end", "2026-10-23T16:00")))));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void computesBusinessResult() {
    var result = service.execute(sample());
    assertEquals(1, result.get("conflictCount"));
    assertEquals(60L, ((Map<?, ?>) ((List<?>) result.get("conflicts")).get(0)).get("minutes"));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversFeatureBoundary() {
    Map<String, Object> invalid = new HashMap<>(sample());
    invalid.put("minimumRestMinutes", -1);
    assertThrows(IllegalArgumentException.class, () -> service.execute(invalid));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversAdditionalBoundaryOne() {
    var r =
        Map.<String, Object>of(
            "minimumRestMinutes",
            0,
            "shifts",
            List.of(
                Map.of(
                    "id",
                    "A",
                    "employee",
                    "E",
                    "start",
                    "2026-10-23T08:00",
                    "end",
                    "2026-10-23T16:00"),
                Map.of(
                    "id",
                    "B",
                    "employee",
                    "E",
                    "start",
                    "2026-10-23T16:00",
                    "end",
                    "2026-10-24T00:00")));
    assertEquals(0, service.execute(r).get("conflictCount"));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversAdditionalBoundaryTwo() {
    var r = new HashMap<>(sample());
    r.put(
        "shifts",
        List.of(
            Map.of(
                "id",
                "A",
                "employee",
                "E",
                "start",
                "2026-10-23T16:00",
                "end",
                "2026-10-23T08:00")));
    assertThrows(IllegalArgumentException.class, () -> service.execute(r));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void deterministicAndDoesNotMutateInput() {
    var input = sample();
    String before = input.toString();
    assertEquals(service.execute(input), service.execute(input));
    assertEquals(before, input.toString());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsNull() {
    assertThrows(IllegalArgumentException.class, () -> service.execute(null));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsMissingFields() {
    assertThrows(IllegalArgumentException.class, () -> service.execute(Map.of()));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsUnknownFields() {
    var input = new HashMap<>(sample());
    input.put("__unknown", true);
    assertThrows(IllegalArgumentException.class, () -> service.execute(input));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsOversizedText() {
    assertThrows(
        IllegalArgumentException.class,
        () -> service.execute(Map.of("payload", "x".repeat(64001))));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsDeepInput() {
    Map<String, Object> v = Map.of("x", 1);
    for (int i = 0; i < 14; i++) v = Map.of("x", v);
    final var input = v;
    assertThrows(IllegalArgumentException.class, () -> service.execute(input));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpRequiresAuthentication() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/shift-conflict-ledger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"minimumRestMinutes\":660,\"shifts\":[{\"id\":\"S1\",\"employee\":\"E1\",\"start\":\"2026-10-23T08:00\",\"end\":\"2026-10-23T16:00\"},{\"id\":\"S2\",\"employee\":\"E1\",\"start\":\"2026-10-23T15:00\",\"end\":\"2026-10-23T23:00\"},{\"id\":\"S3\",\"employee\":\"E2\",\"start\":\"2026-10-23T08:00\",\"end\":\"2026-10-23T16:00\"}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpAllowsAuthenticatedComputation() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/shift-conflict-ledger")
                .principal(new TestingAuthenticationToken("test-user", "unused", "ROLE_OPERATOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"minimumRestMinutes\":660,\"shifts\":[{\"id\":\"S1\",\"employee\":\"E1\",\"start\":\"2026-10-23T08:00\",\"end\":\"2026-10-23T16:00\"},{\"id\":\"S2\",\"employee\":\"E1\",\"start\":\"2026-10-23T15:00\",\"end\":\"2026-10-23T23:00\"},{\"id\":\"S3\",\"employee\":\"E2\",\"start\":\"2026-10-23T08:00\",\"end\":\"2026-10-23T16:00\"}]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpRejectsInvalidInput() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/shift-conflict-ledger")
                .principal(new TestingAuthenticationToken("test-user", "unused", "ROLE_OPERATOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsAnonymousAuthenticationObject() {
    var actor =
        new AnonymousAuthenticationToken(
            "test-key",
            "anonymous",
            List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                    "ROLE_ANONYMOUS")));
    assertEquals(
        401, new AiWorkbenchController(service).run(sample(), actor).getStatusCode().value());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void nestedShiftDoesNotHideRestShortage() {
    var r =
        service.execute(
            Map.of(
                "minimumRestMinutes",
                660,
                "shifts",
                List.of(
                    Map.of(
                        "id",
                        "A",
                        "employee",
                        "E",
                        "start",
                        "2026-10-23T08:00",
                        "end",
                        "2026-10-23T20:00"),
                    Map.of(
                        "id",
                        "B",
                        "employee",
                        "E",
                        "start",
                        "2026-10-23T09:00",
                        "end",
                        "2026-10-23T10:00"),
                    Map.of(
                        "id",
                        "C",
                        "employee",
                        "E",
                        "start",
                        "2026-10-23T21:00",
                        "end",
                        "2026-10-23T22:00"))));
    var rest =
        ((List<Map<String, Object>>) r.get("conflicts"))
            .stream().filter(x -> x.get("type").equals("REST_SHORTAGE")).findFirst().orElseThrow();
    assertEquals("A", rest.get("first"));
    assertEquals(600L, rest.get("minutes"));
  }
}
