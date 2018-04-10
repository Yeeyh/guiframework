package com.proj.gui.framework.component;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.panel.exception.AlertsException;
import com.tools.bp.jdbc.DbUtil;

public class BpFile {
	private JFileChooser jfc = new JFileChooser();
	private DbUtil db = AppContext.getDb();
	private String path;
	private Component frame;
	private ExecutorService executor;
	private File file;
	public BpFile() {
		initDb();
		path = db.fetchOne("select path from path");
		executor = Executors.newFixedThreadPool(5);
	}
	public BpFile(Component frame) {
		this();
		this.frame = frame;
	}
	public BpFile(Component frame,String title,String approveButtonText) {
		this(title,approveButtonText);
		this.frame = frame;
	}
	public BpFile(String title,String approveButtonText) {
		this();
		jfc.setDialogTitle(title);
		jfc.setApproveButtonText(approveButtonText);
	}
	public BpFile(File path,String title,String approveButtonText) {
		this(path,title);
		jfc.setApproveButtonText(approveButtonText);
	}
	public BpFile(File path,String title) {
		this(path);
		jfc.setDialogTitle(title);
	}
	public BpFile(String path) {
		this(new File(path));
	}
	public BpFile(File path) {
		initDb();
		this.path = path.getPath();
	}
	public File getFile() {
		return file;
	}
	public interface ByteReadHandler{
		public void preHandler(File file) throws Exception;
		public void handler(byte[] datas) throws Exception;
	}
	public interface LineReadHandler{
		public void handler(String line) throws Exception;
	}
	public interface LineWriteHandler{
		public void handler(BufferedWriter write) throws Exception;
	}
	public interface ByteWriteHandler{
		public void handler(BufferedOutputStream write) throws Exception;
	}
	public void uploadFile(ByteReadHandler handler) {
		executor.execute(new Runnable() {
			public void run() {
				if(!isApprove()) return;
				if (handler == null) return;
				file = getSelectedFile();
				try(BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file))) {
					handler.preHandler(file);
					for (int size=10*10*1024,i = fis.available()/size; i >= 0 ; i--) {
						size= fis.available()<size?fis.available():size;
						byte[] buf = new byte[size];
						fis.read(buf,0,size);
						fis.mark(size);
						handler.handler(buf);
					}
				} catch (Exception e) {
					AlertsException.error("file_upload", e);
				}
			}
		});
	}
	public void uploadFile(LineReadHandler handler) {
		executor.execute(new Runnable() {
			public void run() {
				if(!isApprove()) return;
				if(handler == null) return;
				file = getSelectedFile();
				try(LineNumberReader fr = new LineNumberReader(new FileReader(file))) {
					for(String line = fr.readLine(); line!=null; line = fr.readLine()) {
						handler.handler(line);
					}
				} catch (Exception e) {
					AlertsException.error("file_upload", e);
				}
			}
		});
	}
	public void downFile(byte[] data){
		downFile(data, 0, data.length);
	}
	public void downFile(byte[] data, int offset, int len){
		executor.execute(new Runnable() {
			public void run() {
				if(!isSave()) return;
				if (data == null) return;
				file = getSelectedFile();
				try( BufferedOutputStream bufos = new BufferedOutputStream(new FileOutputStream(file)) ) {
					bufos.write(data,offset,len);
				} catch (Exception e) {
					AlertsException.error("file_down", e);
				}
			}
		});
	}
	public void downFile(ByteWriteHandler handler){
		executor.execute(new Runnable() {
			public void run() {
				if(!isSave()) return;
				if (handler == null) return;
				file = getSelectedFile();
				try (BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(file))) {
					handler.handler(write);
				} catch (Exception e) {
					AlertsException.error("file_down", e);
				}
			}
		});
	}
	public void downFile(LineWriteHandler handler){
		executor.execute(new Runnable() {
			public void run() {
				if(!isSave()) return;
				if (handler == null) return;
				file = getSelectedFile();
				try (BufferedWriter write = new BufferedWriter(new FileWriter(file))) {
					handler.handler(write);
				} catch (Exception e) {
					AlertsException.error("file_down", e);
				}
			}
		});
	}
	public File getSelectedFile() {
		File f = jfc.getSelectedFile();
		if(db!=null && f!=null && f.getParentFile()!=null) {
			db.update("update path set path=?",f.getParentFile().getAbsolutePath());
		}
		return f;
	}
	private boolean isApprove() {
		jfc.setCurrentDirectory(new File(path));
		//指定当前窗体，若指定main窗体上传时会假死
		int act = jfc.showOpenDialog(frame==null?jfc:frame);
		if (act != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		return true;
	}
	private boolean isSave() {
		jfc.setCurrentDirectory(new File(path));
		int act = jfc.showSaveDialog(frame==null?jfc:frame);//指定当前窗体，若指定main窗体上传时会假死
		if (act != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		return true;
	}
	private void initDb() {
//		db.update("drop table path;");
		//跟踪用户选择的路径
		db.update("CREATE TABLE IF NOT EXISTS \"path\" (\"path\"  TEXT default \".\");");
		int count = Integer.parseInt(db.fetchOne("select count(path) from path;"));
		if(count!=1) {
			db.update("delete from path;");
			db.update("insert into path (path) values ('.');");
		}
	}
}
