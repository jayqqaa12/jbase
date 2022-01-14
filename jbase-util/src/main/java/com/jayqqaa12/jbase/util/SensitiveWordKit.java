package com.jayqqaa12.jbase.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/***
 * 网上抄的 修改一下
 *
 * 敏感词过滤工具  先 init 敏感词库 然后就可以用了
 *
 * DFA 算法 效率还不错 
 *
 * @author 12
 *
 */
public class SensitiveWordKit {

  private static SensitiveWord sensitiveWord;
  public static final int MIN_MATCH_TYPE = 1; // 最小匹配规则
  public static final int MAX_MATCH_TYPE = 2; // 最大匹配规则

  public static final int DEFUAT_MATCH = MIN_MATCH_TYPE;

  /**
   * 构造函数，初始化敏感词库
   */
  public static void init(String filename) {
    sensitiveWord = new SensitiveWord(FileUtil.getAbsolutePath(filename));
  }


  /**
   * 判断文字是否包含敏感字符
   */
  public static boolean isContaintSensitiveWord(String txt) {
    boolean flag = false;
    for (int i = 0; i < txt.length(); i++) {
      int matchFlag = CheckSensitiveWord(txt, i); // 判断是否包含敏感字符
      if (matchFlag > 0) { // 大于0存在，返回true
        flag = true;
      }
    }
    return flag;
  }

  /**
   * 获取文字中的敏感词
   */
  public static Set<String> getSensitiveWord(String txt) {
    Set<String> sensitiveWordList = new HashSet<String>();

    for (int i = 0; i < txt.length(); i++) {
      int length = CheckSensitiveWord(txt, i); // 判断是否包含敏感字符
      if (length > 0) { // 存在,加入list中
        sensitiveWordList.add(txt.substring(i, i + length));
        i = i + length - 1; // 减1的原因，是因为for会自增
      }
    }

    return sensitiveWordList;
  }

  /**
   * 替换敏感字字符
   */
  public static String replaceSensitiveWord(String txt, String replaceChar) {
    String resultTxt = txt;
    Set<String> set = getSensitiveWord(txt); // 获取所有的敏感词
    Iterator<String> iterator = set.iterator();
    String word = null;
    String replaceString = null;
    while (iterator.hasNext()) {
      word = iterator.next();
      replaceString = getReplaceChars(replaceChar, word.length());
      resultTxt = resultTxt.replaceAll(word, replaceString);
    }

    return resultTxt;
  }

  /**
   * 获取替换字符串
   */
  private static String getReplaceChars(String replaceChar, int length) {
    String resultReplace = replaceChar;
    for (int i = 1; i < length; i++) {
      resultReplace += replaceChar;
    }

    return resultReplace;
  }

  /**
   * 检查文字中是否包含敏感字符，检查规则如下：<br>
   */
  @SuppressWarnings({"rawtypes"})
  public static int CheckSensitiveWord(String txt, int beginIndex) {

    if (sensitiveWord == null) {
      throw new NullPointerException("must init() SensitiveWordKit before use");
    }

    boolean flag = false; // 敏感词结束标识位：用于敏感词只有1位的情况
    int matchFlag = 0; // 匹配标识数默认为0
    char word = 0;
    Map nowMap = sensitiveWord.getSensitiveWordMap();
    for (int i = beginIndex; i < txt.length(); i++) {
      word = txt.charAt(i);
      nowMap = (Map) nowMap.get(word); // 获取指定key
      if (nowMap != null) { // 存在，则判断是否为最后一个
        matchFlag++; // 找到相应key，匹配标识+1
        if ("1".equals(nowMap.get("isEnd"))) { // 如果为最后一个匹配规则,结束循环，返回匹配标识数
          flag = true; // 结束标志位为true
          if (DEFUAT_MATCH == MIN_MATCH_TYPE) { // 最小规则，直接返回,最大规则还需继续查找
            break;
          }
        }
      } else { // 不存在，直接返回
        break;
      }
    }
    if (matchFlag < 2 || !flag) { // 长度必须大于等于1，为词
      matchFlag = 0;
    }
    return matchFlag;
  }

}