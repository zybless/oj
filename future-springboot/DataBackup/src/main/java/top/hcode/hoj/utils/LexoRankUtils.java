package top.hcode.hoj.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * LexoRankUtils
 *
 * @author zybless
 * @date 2026/3/11 17:51
 */
public class LexoRankUtils {

    private static final int BASE = 26; // 'a'=0, 'z'=25

    /**
     * 在 prev 和 next 之间生成新 rank（正确的 base-26 算术中值）
     * prev = null 表示插到最前面
     * next = null 表示插到最后面
     */
    public static String between(String prev, String next) {
        if (prev == null && next == null) return "naaaa";
        if (prev == null) return before(next);
        if (next == null) return after(prev);

        if (prev.compareTo(next) >= 0) {
            throw new IllegalArgumentException("prev >= next: " + prev + " / " + next);
        }

        int len = Math.max(prev.length(), next.length()) + 1;

        // 转成 base-26 数组（'a'=0, 'z'=25）
        int[] a = new int[len];
        int[] b = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = i < prev.length() ? prev.charAt(i) - 'a' : 0;
            b[i] = i < next.length() ? next.charAt(i) - 'a' : 0;
        }

        // ✅ 正确的 base-26 加法：从右往左累加并处理进位
        int[] sum = new int[len];
        int carry = 0;
        for (int i = len - 1; i >= 0; i--) {
            int s = a[i] + b[i] + carry;
            sum[i] = s % BASE;
            carry = s / BASE;
        }
        // carry 是最高位溢出（0 或 1）

        // ✅ 正确的 base-26 除以 2：从高位往低位，余数传递到下一位
        int[] mid = new int[len];
        int rem = carry;
        for (int i = 0; i < len; i++) {
            int cur = rem * BASE + sum[i];
            mid[i] = cur / 2;
            rem = cur % 2;
        }

        // 转回字符串，去掉尾部 'a'
        StringBuilder sb = new StringBuilder();
        for (int digit : mid) {
            sb.append((char) ('a' + digit));
        }
        String result = sb.toString().replaceAll("a+$", "");
        if (result.isEmpty()) result = "n";

        // 最终兜底校验
        if (result.compareTo(prev) <= 0 || result.compareTo(next) >= 0) {
            throw new IllegalArgumentException("no room between: " + prev + " / " + next);
        }
        return result;
    }

    /** 在 first 之前插入 */
    public static String before(String first) {
        char[] chars = first.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] > 'a') {
                chars[i]--;
                return new String(chars, 0, i + 1);
            }
        }
        return "a" + first;
    }

    /** 在 last 之后插入 */
    public static String after(String last) {
        char[] chars = last.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] < 'z') {
                chars[i]++;
                return new String(chars, 0, i + 1);
            }
        }
        return last + "n";
    }

    /**
     * 均匀生成 count 个 rank，用于 rebalance
     * 分布在 nb~ ~ ny~ 区间，留足上下扩展空间
     */
    public static List<String> generateBalancedRanks(int count) {
        List<String> ranks = new ArrayList<>();
        int totalSlots = 24 * 26 * 26;
        for (int i = 0; i < count; i++) {
            int slot = (int) (((double) (i + 1) / (count + 1)) * totalSlots);
            char c1 = (char) ('b' + slot / (26 * 26));
            char c2 = (char) ('a' + (slot / 26) % 26);
            char c3 = (char) ('a' + slot % 26);
            ranks.add("n" + c1 + c2 + c3);
        }
        return ranks;
    }

    private static int[] toArray(String s, int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = i < s.length() ? s.charAt(i) - 'a' : 0;
        }
        return arr;
    }
}
