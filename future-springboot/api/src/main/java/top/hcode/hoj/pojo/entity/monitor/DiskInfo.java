package top.hcode.hoj.pojo.entity.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Minipax
 * @Date: 2025/8/12 14:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiskInfo {

    private Double usagePercentage; // 使用率（%）

    private Double freeDisk; // 已用容量（GB）

    private Double totalDisk; // 总容量（GB）

    public DiskInfo(Double freeDisk, Double totalDisk) {
        this.freeDisk = freeDisk / 1024.0 / 1024.0 / 1024.0;
        this.totalDisk = totalDisk / 1024.0 / 1024.0 / 1024.0;
        this.usagePercentage = (totalDisk - freeDisk) / totalDisk * 100.0;
    }
}
