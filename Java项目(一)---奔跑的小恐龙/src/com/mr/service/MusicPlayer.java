package com.mr.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 音乐播放器
 */
public class MusicPlayer implements Runnable {
	File soundFile; // 音乐文件
	Thread thread;// 父线程 -- 执行run方法
	boolean circulate;// 是否循环播放

	/**
	 * 构造方法，默认不循环播放
	 * 
	 * @param filepath 音乐文件完整名称
	 * @throws FileNotFoundException
	 */

	// 构造方法--1
	public MusicPlayer(String filepath) throws FileNotFoundException {
		this(filepath, false);
	}

	/**
	 * 构造方法
	 * 
	 * @param filepath  音乐文件完整名称
	 * @param circulate 是否循环播放
	 * @throws FileNotFoundException
	 */

	// 构造方法--2
	public MusicPlayer(String filepath, boolean circulate) throws FileNotFoundException {
		this.circulate = circulate;
		soundFile = new File(filepath);   // File soundFile
		if (!soundFile.exists()) {// 如果文件不存在
			throw new FileNotFoundException(filepath + "未找到");  // 抛出错误，文件未找到
		}
	}

	/**
	 * 播放
	 */
	public void play() {
		thread = new Thread(this);// 创建线程对象
		thread.start();// 开启线程
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		thread.stop();// 强制关闭线程
	}

	/**
	 * 重写线程执行方法
	 */
	
	/*
	 * 在run()方法中声明一个128K的缓冲区字节数组
	 * 程序以不断循环的方式将音乐文件以音频输入流格式读入缓冲区
	 * 把缓冲区的数据写入混音器源数据行中
	 * */
	public void run() {
		byte[] auBuffer = new byte[1024 * 128];// 创建128k缓冲区
		do {
			AudioInputStream audioInputStream = null; // 创建音频输入流对象
			SourceDataLine auline = null; // 混频器源数据行
			try {
				// 从音乐文件中获取音频输入流
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);// soundFile--音乐文件
				AudioFormat format = audioInputStream.getFormat(); // 获取音频格式
//				System.out.println(audioInputStream.getFormat());
				// 按照源数据行类型和指定音频格式创建数据行对象
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); // 获取音乐格式
				// 利用音频系统类获得与指定 Line.Info 对象中的描述匹配的行，并转换为源数据行对象
				auline = (SourceDataLine) AudioSystem.getLine(info);
				auline.open(format);// 按照指定格式打开源数据行
				auline.start();// 源数据行开启读写活动
				int byteCount = 0;// 记录音频输入流读出的字节数
				while (byteCount != -1) {// 如果音频输入流中读取的字节数不为-1
					// 从音频数据流中读出128K的数据--auBuffer=128k缓冲区
					byteCount = audioInputStream.read(auBuffer, 0, auBuffer.length);
					if (byteCount >= 0) {// 如果读出有效数据--auline=混频器源数据行
						auline.write(auBuffer, 0, byteCount);// 将有效数据写入数据行中
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} finally {
				auline.drain();// 清空数据行
				auline.close();// 关闭数据行
			}
		} while (circulate);// 根据循环标志判断是否循环播放
	}
}