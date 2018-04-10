package test.proj.gui.framework.upload;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpFile;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpFile.LineReadHandler;
@Component
@Table
public class UploadFile extends BpPanel {
	private static final long serialVersionUID = 1L;
	public void init() {
		JButton b = new JButton("upload");
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
//				BpFile up = new BpFile("/root/Downloads");
				BpFile up = new BpFile(UploadFile.this,"上传文件到服务器","上传");
				//指定当前窗体，若指定main窗体上传时会假死
				up.uploadFile(new LineReadHandler() {
					public void handler(String line) throws Exception {
						System.out.println(line);
					}
				});
			}
		});
		layout.grid().line(0).grid().column(0).fill().both().add(b, this);
	}
}

