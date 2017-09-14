package com.jayqqaa12.jbase.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


public class SensitiveWord {

    private static final Logger LOG = LoggerFactory.getLogger(SensitiveWord.class);

    private String ENCODING = "utf8";    //字符编码
    @SuppressWarnings("rawtypes")
    public Map sensitiveWordMap;
    private String filename;

    public SensitiveWord(String filename) {
        this.filename = filename;
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }


    public Map getSensitiveWordMap() {

        return sensitiveWordMap;
    }


    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     * isEnd = 0
     * 国 = {<br>
     * isEnd = 1
     * 人 = {isEnd = 0
     * 民 = {isEnd = 1}
     * }
     * 男  = {
     * isEnd = 0
     * 人 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     * 五 = {
     * isEnd = 0
     * 星 = {
     * isEnd = 0
     * 红 = {
     * isEnd = 0
     * 旗 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     *
     * @param keyWordSet 敏感词库
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }


    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        File file = new File(filename);    //读取文件

        try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);) {
            if (file.isFile() && file.exists()) {      //文件流是否存在
                set = new HashSet<>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            } else
                throw new FileNotFoundException("敏感词库文件不存在");

        } catch (Exception e) {
            throw e;
        }
        return set;
    }
}