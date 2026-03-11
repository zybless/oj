package top.hcode.hoj.utils;

/**
 * LexoRankUtils
 *
 * @author zybless
 * @date 2026/3/11 17:51
 */
public class LexoRankUtils {

    /**
     * 在 prev 和 next 之间生成新 rank
     * prev = null 表示插到最前面
     * next = null 表示插到最后面
     */
    public static String between(String prev, String next) {
        if (prev == null && next == null) return "naaaa"; // 唯一一条
        if (prev == null) return before(next);
        if (next == null) return after(prev);

        if (prev.compareTo(next) >= 0) {
            throw new IllegalArgumentException("prev >= next: " + prev + " / " + next);
        }

        // 补齐长度
        int maxLen = Math.max(prev.length(), next.length()) + 1;
        int[] a = toArray(prev, maxLen);
        int[] b = toArray(next, maxLen);

        // 逐位取中间值
        int[] mid = new int[maxLen];
        for (int i = 0; i < maxLen; i++) {
            mid[i] = (a[i] + b[i]);
        }
        // 处理进位（从右往左除2）
        for (int i = maxLen - 1; i > 0; i--) {
            if (mid[i] % 2 != 0) {
                mid[i - 1]++;
            }
            mid[i] /= 2;
        }
        mid[0] /= 2;

        String result = toString(mid).replaceAll("a+$", "");
        if (result.isEmpty()) result = "n";

        // 极端情况：结果等于 prev，追加一个中间字符
        if (result.compareTo(prev) <= 0) {
            result = prev + "n";
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
        return "a" + "n" + first;
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

    // ---------- 私有辅助 ----------

    private static int[] toArray(String s, int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = i < s.length() ? s.charAt(i) : 'a';
        }
        return arr;
    }

    private static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int c : arr) sb.append((char) c);
        return sb.toString();
    }
}
