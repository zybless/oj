package top.hcode.hoj.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * @Author: Minipax
 * @Date: 2024/7/31 14:42
 * @Description:
 */
public class CommonUtils {

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static <T> void addElementToPQ(PriorityQueue<T> priorityQueue, T object, int maxSize) {
        priorityQueue.add(object);
        if (priorityQueue.size() > maxSize) {
            priorityQueue.poll();
        }
    }

}
