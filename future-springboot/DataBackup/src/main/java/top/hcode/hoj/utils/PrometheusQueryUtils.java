package top.hcode.hoj.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.hcode.hoj.config.PrometheusConfig;
import top.hcode.hoj.pojo.entity.monitor.DiskInfo;

import java.net.URI;

/**
 * @Author: Minipax
 * @Date: 2025/8/12 14:30
 * @Description:
 */
@Slf4j
@Component
public class PrometheusQueryUtils {

    // Prometheus服务器地址（替换为你的实际地址）
    private static final String PROMETHEUS_URL = ":9090/api/v1/query";

    private final RestTemplate restTemplate = new RestTemplate();

    public DiskInfo queryDiskInfo(String serverIp) {
        Double freeDisk = queryMetric(serverIp, PrometheusConfig.FREE_DISK_QUERY);
        Double totalDisk = queryMetric(serverIp, PrometheusConfig.TOTAL_DISK_QUERY);

        if (freeDisk != null && totalDisk != null && freeDisk > 0 && totalDisk > 0) {
            return new DiskInfo(freeDisk, totalDisk);
        } else {
            return null;
        }

    }

    /**
     * 通用查询方法（抽取重复逻辑）
     */
    private Double queryMetric(String serverIp, String promql) {

        // 构建带参数的URL
        URI targetUrl = UriComponentsBuilder.fromUriString(serverIp + PROMETHEUS_URL)
                .queryParam("query", promql)
                .build()
                .toUri();

        // 调用Prometheus API
        try {
            String response = restTemplate.getForObject(targetUrl, String.class);

            // 解析响应结果
            JSONObject json = JSONObject.parseObject(response);
            if ("success".equals(json.getString("status"))) {
                JSONObject data = json.getJSONObject("data");
                if (!data.getJSONArray("result").isEmpty()) {
                    String value = data.getJSONArray("result").getJSONObject(0)
                            .getJSONArray("value").getString(1);
                    return Double.parseDouble(value);
                }
            }
        } catch (Exception e) {
            log.error("[Disk Info Query] get server disk info error, uri={}, error={}", serverIp, e);
        }

        return null; // 查询失败返回null
    }

}
