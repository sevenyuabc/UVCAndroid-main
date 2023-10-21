package com.cicada.kidscard.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 * @Description: 线程管理工具
 */
public class ThreadPoolUtil {

	private static volatile ThreadPoolUtil instance;
	private static ExecutorService mThreadPool = null;

	public static ThreadPoolUtil getInstance() {
		if (null == instance) {
			synchronized (ThreadPoolUtil.class) {
				if (null == instance) {
					instance = new ThreadPoolUtil();
					mThreadPool = Executors.newFixedThreadPool(1 + 2 * Runtime.getRuntime().availableProcessors());
				}
			}
		}
		return instance;
	}

	public void submit(Runnable runnable) {
		mThreadPool.submit(runnable);
	}
}
