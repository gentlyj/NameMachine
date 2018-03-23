package com.ifading.namemachine.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;

/**
 * Created  on 20180323//.
 *
 * @author by yangjingsheng
 */

public class PinyinUtils {

    /**
     * pinyin4j格式类
     */
    private HanyuPinyinOutputFormat format = null;
    /**
     * 拼音字符串数组
     */
    private String[] pinyin;

    /**
     * 通过构造方法进行初始化
     */
    public PinyinUtils() {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        pinyin = null;
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr 需转换的汉字字符串
     * @return 拼音字符串数组
     */
    public String getCharPinYin(char pinYinStr) {

        try {
            //执行转换
            pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr, format);

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println(e.toString());
            return null;
        }

        //pinyin4j规则，当转换的符串不是汉字，就返回null
        if (pinyin == null) {
            return null;
        }
        System.out.println("word:" + pinYinStr + "  all pinyin:" + Arrays.toString(pinyin));
        //多音字会返回一个多音字拼音的数组，pinyiin4j并不能有效判断该字的读音
        return pinyin[0];
    }

    /**
     * 从单个字里获取音调
     * @param word 字
     * @return 音调 1->1声,2->2声,3->3声,4->4声
     */
    public int getCharTone(char word) {
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        String charPinYin = getCharPinYin(word);
        if (charPinYin == null) {
            return -1;
        }
        char tone = charPinYin.charAt(charPinYin.length() - 1);
        return tone - 48;
    }

    public String getCharPinYinWithTone(char word) {
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        return getCharPinYin(word);
    }

}
