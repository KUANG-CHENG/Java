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
 * ���ֲ�����
 */
public class MusicPlayer implements Runnable {
	File soundFile; // �����ļ�
	Thread thread;// ���߳� -- ִ��run����
	boolean circulate;// �Ƿ�ѭ������

	/**
	 * ���췽����Ĭ�ϲ�ѭ������
	 * 
	 * @param filepath �����ļ���������
	 * @throws FileNotFoundException
	 */

	// ���췽��--1
	public MusicPlayer(String filepath) throws FileNotFoundException {
		this(filepath, false);
	}

	/**
	 * ���췽��
	 * 
	 * @param filepath  �����ļ���������
	 * @param circulate �Ƿ�ѭ������
	 * @throws FileNotFoundException
	 */

	// ���췽��--2
	public MusicPlayer(String filepath, boolean circulate) throws FileNotFoundException {
		this.circulate = circulate;
		soundFile = new File(filepath);   // File soundFile
		if (!soundFile.exists()) {// ����ļ�������
			throw new FileNotFoundException(filepath + "δ�ҵ�");  // �׳������ļ�δ�ҵ�
		}
	}

	/**
	 * ����
	 */
	public void play() {
		thread = new Thread(this);// �����̶߳���
		thread.start();// �����߳�
	}

	/**
	 * ֹͣ����
	 */
	public void stop() {
		thread.stop();// ǿ�ƹر��߳�
	}

	/**
	 * ��д�߳�ִ�з���
	 */
	
	/*
	 * ��run()����������һ��128K�Ļ������ֽ�����
	 * �����Բ���ѭ���ķ�ʽ�������ļ�����Ƶ��������ʽ���뻺����
	 * �ѻ�����������д�������Դ��������
	 * */
	public void run() {
		byte[] auBuffer = new byte[1024 * 128];// ����128k������
		do {
			AudioInputStream audioInputStream = null; // ������Ƶ����������
			SourceDataLine auline = null; // ��Ƶ��Դ������
			try {
				// �������ļ��л�ȡ��Ƶ������
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);// soundFile--�����ļ�
				AudioFormat format = audioInputStream.getFormat(); // ��ȡ��Ƶ��ʽ
//				System.out.println(audioInputStream.getFormat());
				// ����Դ���������ͺ�ָ����Ƶ��ʽ���������ж���
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); // ��ȡ���ָ�ʽ
				// ������Ƶϵͳ������ָ�� Line.Info �����е�����ƥ����У���ת��ΪԴ�����ж���
				auline = (SourceDataLine) AudioSystem.getLine(info);
				auline.open(format);// ����ָ����ʽ��Դ������
				auline.start();// Դ�����п�����д�
				int byteCount = 0;// ��¼��Ƶ�������������ֽ���
				while (byteCount != -1) {// �����Ƶ�������ж�ȡ���ֽ�����Ϊ-1
					// ����Ƶ�������ж���128K������--auBuffer=128k������
					byteCount = audioInputStream.read(auBuffer, 0, auBuffer.length);
					if (byteCount >= 0) {// ���������Ч����--auline=��Ƶ��Դ������
						auline.write(auBuffer, 0, byteCount);// ����Ч����д����������
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} finally {
				auline.drain();// ���������
				auline.close();// �ر�������
			}
		} while (circulate);// ����ѭ����־�ж��Ƿ�ѭ������
	}
}