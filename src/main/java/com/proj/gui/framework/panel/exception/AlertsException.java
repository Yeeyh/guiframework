package com.proj.gui.framework.panel.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class AlertsException extends Throwable {
	private static final long serialVersionUID = 1L;
	public static AlertsPanel alerts;
	@Autowired
	public void setAlerts(AlertsPanel alerts) {
		AlertsException.alerts = alerts;
	}
	public static void error(String tool, Exception e) {
		try (
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw, true)
				) {
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			alerts.error(tool, e.toString(), sw.toString());
		} catch (Exception e2) {
		}
	}
}
