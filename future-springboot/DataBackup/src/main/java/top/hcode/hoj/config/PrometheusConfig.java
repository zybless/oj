package top.hcode.hoj.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2025/8/12 14:52
 * @Description:
*/
@Configuration
public class PrometheusConfig {

    public static final String FREE_DISK_QUERY = "node_filesystem_free_bytes{mountpoint=\"/\"}";

    public static final String TOTAL_DISK_QUERY = "node_filesystem_size_bytes{mountpoint=\"/\"}";

}
