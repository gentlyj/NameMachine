package com.ifading.namemachine.utils;

import org.junit.Test;

/**
 * Created  on 20180323//.
 *
 * @author by yangjingsheng
 */

public class PinyinUtilsTest {
    String[] toneChar = {"一", "二", "三", "四"};
    PinyinUtils pinyinUtils = new PinyinUtils();
    @Test
    public void testgetCharPinYin() {

        char word = '昂';
        String charPinYin = pinyinUtils.getCharPinYin(word);
        char tone = charPinYin.charAt(charPinYin.length() - 1);
        System.out.println("word:" + word + " 拼音:" + charPinYin + " 音调: " + toneChar[tone - 49] + "声");
    }

    @Test
    public void testgetCharTone(){
        char word = '昂';
        int charPinYin = pinyinUtils.getCharTone(word);
        System.out.println("word:" + word + " 音调:" + charPinYin);
        testgetCharPinYinWithTone();
    }

    @Test
    public void testgetCharPinYinWithTone(){

        char word = '女';
        String charPinYin = pinyinUtils.getCharPinYinWithTone(word);
        System.out.println("word:" + word + " 拼音:" + charPinYin);
    }
}
