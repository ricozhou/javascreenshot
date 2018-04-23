package screenshot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

//系统托盘
public class MinimizeTrayJPanel extends JFrame {
	public JFrame jf;
	// 托盘图标
	public TrayIcon ti;
	// 托盘
	public SystemTray st;
	// 右击菜单
	public PopupMenu pm;
	public MenuItem mi1, mi2;

	public MinimizeTrayJPanel(JFrame jf) throws AWTException {
		this.jf = jf;
	}

	public boolean init() throws AWTException {
		pm = new PopupMenu();
		mi1 = new MenuItem("Open");
		mi2 = new MenuItem("Exit");
		// 添加右击菜单
		pm.add(mi1);
		pm.add(mi2);
		// 添加监听事件
		mi1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.setExtendedState(JFrame.NORMAL);
				jf.setVisible(true);
			}
		});
		mi2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// 是否支持托盘
		if (!SystemTray.isSupported()) {
			jf.setExtendedState(JFrame.NORMAL);
			return false;
		}
		// 获取系统托盘
		st = SystemTray.getSystemTray();
		// 托盘图标
		ImageIcon i = new ImageIcon(getClass().getResource("ss.png"));
		ti = new TrayIcon(i.getImage(), "Shift+Alt+A截图，Ctrl+Alt+Q退出程序", pm);
		st.add(ti);
		ti.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 双击托盘
				if (e.getClickCount() == 2) {
					jf.setExtendedState(JFrame.NORMAL);
					jf.setVisible(true);
				}
			}
		});
		return true;
	}
}
