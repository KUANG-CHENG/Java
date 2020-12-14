
package com.mr.service;

import java.io.*;
import java.util.Arrays;

/**
 * 分数记录器
 */
public class ScoreRecorder {
    private static final String SCOREFILE = "data/soure";// 得分记录文件
    private static int scores[] = new int[3];// 当前得分最高前三名

    /**
     * 分数初始化
     */
    public static void init() {
        File f = new File(SCOREFILE);// 创建记录文件
        if (!f.exists()) {// 如果文件不存在
            try {
                f.createNewFile();// 创建新文件
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;// 停止方法
        }
        FileInputStream fis = null;    // 文件输入流（字节流）
        InputStreamReader isr = null;  // 文件输入流（字符流）InputStreamReader----FileReader
        BufferedReader br = null;   // 缓冲字符流
        try {
            fis = new FileInputStream(f);// 文件字节输入流
            isr = new InputStreamReader(fis);// 字节流转字符流
            br = new BufferedReader(isr);// 缓冲字符流
            String value = br.readLine();// 读取一行，并将其返回为字符串
            if (!(value == null || "".equals(value))) {// 如果不为空值
                String vs[] = value.split(",");// 分割字符串，，例如660，550，330
                if (vs.length < 3) {// 如果分割结果小于3
                    Arrays.fill(scores, 0);// 数组填充0--scores数组保存三个int元素
                } else {
                    for (int i = 0; i < 3; i++) {
                        // 将记录文件中的值赋给当前分数数组
                        scores[i] = Integer.parseInt(vs[i]);// 返回有String指定的字符串中的数字的等价整数值
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 依次关闭流
            try {
                br.close();     // 关闭缓冲字符流
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close(); // 关闭文件输入流（字符流）
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close(); // 关闭文件输入流（字节流）
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 保存分数，将分数写入文档
     */
    public static void saveScore() {
        // 拼接得分数组
        String value = scores[0] + "," + scores[1] + "," + scores[2];
        FileOutputStream fos = null;  // 文件输出流（字节流）
        OutputStreamWriter osw = null; // 文件输出流（字符流）
        BufferedWriter bw = null; // 缓冲字符流
        try {
            fos = new FileOutputStream(SCOREFILE);// 文件字节输出流
            osw = new OutputStreamWriter(fos);// 字节流转字符流
            bw = new BufferedWriter(osw);// 缓冲字符流
            bw.write(value);// 写入拼接后的字符串
            bw.flush();// 字符流刷新
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 依次关闭流
            try {
                bw.close();// 关闭缓冲字符流
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                osw.close();// 关闭文件输出流（字符流）
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();// 关闭文件输出流（字节流）
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加分数。如果新添加的分数比排行榜分数高，则会将新分数记入排行榜。
     * 
     * @param score
     *            新分数
     */
    static public void addNewScore(int score) {
        // 在得分组数基础上创建一个长度为4的临时数组
        int tmp[] = Arrays.copyOf(scores, 4); // 返回的数组是新的数组对象，该拷贝不会影响原来的数组
        tmp[3] = score; // 将新分数赋值给第四个元素
        Arrays.sort(tmp); // 临时数组升序排列
        scores = Arrays.copyOfRange(tmp, 1, 4); // 将后三个元素赋值给得分数组--包括from，不包括to
    }  

    /**
     * 获取分数
     * 
     * @return
     */
    static public int[] getScores() {
        return scores;
    }

}
