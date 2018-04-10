package com.proj.gui.framework.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface BpController {
	public static ExecutorService executor = Executors.newFixedThreadPool(20);
	public void init();
}
