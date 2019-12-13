package com.one.hope.util;

/**
 * @author LinDingQiang
 * @time 2019/12/5 16:57
 * @email dingqiang.l@verifone.cn
 */
public class StringUtils {
    public static String bcd2string(byte[] bcd) {
        StringBuilder sb = new StringBuilder();
        char[] chs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : bcd) {
            sb.append(chs[(b >> 4) & 0x0f]).append(chs[b & 0x0f]);
        }
        return sb.toString();
    }
}
