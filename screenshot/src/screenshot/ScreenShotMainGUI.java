package screenshot;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class ScreenShotMainGUI extends JFrame implements ActionListener {
	// 定义热键标识，用于在设置多个热键时，在事件处理中区分用户按下的热键
	// 截图
	public static final int SSHOT_KEY_MARK = 0;
	// 退出程序
	public static final int EXIT_KEY_MARK = 3;
	// 退出当前截图
	public static final int EXIT_CURR_KEY_MARK = 4;
	// 保存
	public static final int SAVE_KEY_MARK = 1;
	// 复制到剪切板
	// public static final int COPY_KEY_MARK = 4;
	// 复制到剪切板并关闭截图
	public static final int CUT_KEY_MARK = 2;
	public JPanel jp1;
	public JButton button1, button2;
	public JFrame jf;
	public Screenshot accessibleScreenshot;
	public SshotUtils su = new SshotUtils();
	public static boolean fileExit = false;
	// 是否是刚启动设置(保证第一次启动设置的不同)
	public static boolean isJustSatrt = true;
	// 保证只能启动一个设置
	// public static boolean isOnlyOneSetup = true;
	// 是否打开了设置窗口
	public static boolean isOpenSetupJF = false;
	public SetupMsg sm;
	public SetupParams sp;
	public static String hotKey;
	// 所有的按键存入map
	public static Map<String, String> keyMap = new HashMap<String, String>();
	public static Map<String, String> keyMap2 = new HashMap<String, String>();

	public ScreenShotMainGUI() {
		// 初始化
		this.jf = this;
		initPro();
		initFirst();
		init();
	}

	// 初始化，检查是否已启动，是否最小化托盘
	private void initFirst() {
		// 是否已启动，启动则退出
		if (!su.checkSocket(sm.getPort() != 0 ? sm.getPort() : 12345)) {
			// JOptionPane.showMessageDialog(null, "程序已启动！请勿重复启动！", "提示消息",
			// JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}

	}

	// 设置最小化
	public void setJFMinSize() {
		// 是否最小化托盘
		try {
			// 若不支持托盘或者初始化失败,则提示
			if (!minimizeTray()) {
				JOptionPane.showMessageDialog(null, "系统不支持托盘！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			}
			dispose();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}

	// 初始化配置
	public void initPro() {
		// 初始化按键对照表
		su.initKeyMap();
		// 原理：首先判断exe所在目录是否存在隐藏的配置文件screentshotsetup.properties，
		// 如果存在，则读取此配置文件，如果不存在则生成一个默认的隐藏的配置文件
		// 实例化一个参数对象
		// 这个是用于设置具体参数
		sm = new SetupMsg();
		// 这个是用于显示具体参数
		sp = new SetupParams();
		File f = new File("screentshotsetup.properties");
		// 判断是否存在文件
		fileExit = su.isFileExit(f);
		if (fileExit) {
			// 获取默认配置信息
			try {
				// 获取显示参数
				sp = su.getDefaultMsg(f);
				// 转化为使用参数
				sm = su.trunSm(sp);
			} catch (Exception e1) {
				sm = new SetupMsg();
				e1.printStackTrace();
			}
		}
	}

	// 主方法
	public static void main(String[] args) {
		// 线程启动截图主程序
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 国人牛逼主题，值得学习
				// 初始化字体
				InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 13));
				// 设置本属性将改变窗口边框样式定义
				// 系统默认样式 osLookAndFeelDecorated
				// 强立体半透明 translucencyAppleLike
				// 弱立体感半透明 translucencySmallShadow
				// 普通不透明 generalNoTranslucencyShadow
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				// 设置主题为BeautyEye
				try {
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 隐藏“设置”按钮
				UIManager.put("RootPane.setupButtonVisible", false);
				// 开启/关闭窗口在不活动时的半透明效果
				// 设置此开关量为false即表示关闭之，BeautyEye LNF中默认是true
				BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
				// 设置BeantuEye外观下JTabbedPane的左缩进
				// 改变InsetsUIResource参数的值即可实现
				UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(3, 20, 2, 20));
				// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				new ScreenShotMainGUI();
			}
		});
	}

	// font
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	// 初始化
	private void init() {
		jp1 = new JPanel();
		jp1.setLayout(null);

		button1 = new JButton("截图");
		button1.addActionListener(this);
		button1.setBounds(1, 0, 100, 48);
		jp1.add(button1);
		button1.setEnabled(true);

		ImageIcon sshotSet = new ImageIcon(getClass().getResource("setup.png"));
		button2 = new JButton(sshotSet);
		button2.addActionListener(this);
		button2.setBounds(102, 0, 49, 47);
		jp1.add(button2);
		button2.setEnabled(true);

		this.add(jp1);
		this.setSize(162, 86);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon i = new ImageIcon(getClass().getResource("screenshot.png"));
		this.setIconImage(i.getImage());

		// 取出sm中list，进行处理并返回第二个参数和第三个参数
		int[] sskm = su.proParamList(sm.getSshkList(), SSHOT_KEY_MARK);
		int[] skm = su.proParamList(sm.getShkList(), SAVE_KEY_MARK);
		int[] ckm = su.proParamList(sm.getChkList(), CUT_KEY_MARK);
		int[] ekm = su.proParamList(sm.getEhkList(), EXIT_KEY_MARK);
		int[] eckm = su.proParamList(sm.getCchkList(), EXIT_CURR_KEY_MARK);

		// 第一步：注册热键，第一个参数表示该热键的标识，第二个参数表示组合键，如果没有则为0，第三个参数为定义的主要热键
		// 截图
		JIntellitype.getInstance().registerHotKey(SSHOT_KEY_MARK, sskm[0], sskm[1]);
		// 保存
		JIntellitype.getInstance().registerHotKey(SAVE_KEY_MARK, skm[0], skm[1]);
		// 复制到剪切板并关闭截图
		JIntellitype.getInstance().registerHotKey(CUT_KEY_MARK, ckm[0], ckm[1]);
		// 退出程序
		JIntellitype.getInstance().registerHotKey(EXIT_KEY_MARK, ekm[0], ekm[1]);
		// 取消当前截图
		JIntellitype.getInstance().registerHotKey(EXIT_CURR_KEY_MARK, eckm[0], eckm[1]);

		// JIntellitype.getInstance()
		// 第二步：添加热键监听器
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int markCode) {
				switch (markCode) {
				case SSHOT_KEY_MARK:
					if (ScreenShotMainGUI.isOpenSetupJF == true) {
						// 设置界面打开时，禁止截图程序
						su.proKeyBeforeSshot(SSHOT_KEY_MARK, sm);
						return;
					}
					startScreenShot();
					break;
				case EXIT_KEY_MARK:
					if (ScreenShotMainGUI.isOpenSetupJF == true) {
						// 设置界面打开时，禁止截图程序
						su.proKeyBeforeSshot(EXIT_KEY_MARK, sm);
						return;
					}
					System.exit(0);
					break;
				case EXIT_CURR_KEY_MARK:
					if (ScreenShotMainGUI.isOpenSetupJF == true) {
						// 设置界面打开时，禁止截图程序
						su.proKeyBeforeSshot(EXIT_CURR_KEY_MARK, sm);
						return;
					}
					exitCurrScreenShot();
					break;
				case SAVE_KEY_MARK:
					if (ScreenShotMainGUI.isOpenSetupJF == true) {
						// 设置界面打开时，禁止截图程序
						su.proKeyBeforeSshot(SAVE_KEY_MARK, sm);
						return;
					}
					saveCurrSshot();
					break;
				case CUT_KEY_MARK:
					if (ScreenShotMainGUI.isOpenSetupJF == true) {
						// 设置界面打开时，禁止截图程序
						su.proKeyBeforeSshot(CUT_KEY_MARK, sm);
						return;
					}
					cutCurrSshot();
					break;
				}
			}
		});

		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当最小化时
			public void windowIconified(WindowEvent e) {
				setJFMinSize();
			}
		});

		// 是否最小化托盘
		if (sm.isStartMinSize()) {
			setJFMinSize();
		}

	}

	// 复制
	protected void cutCurrSshot() {
		if (accessibleScreenshot != null) {
			try {
				accessibleScreenshot.copyClipImage();
				exitCurrScreenShot();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "复制失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	// 保存当前截图
	protected void saveCurrSshot() {
		if (accessibleScreenshot != null) {
			try {
				accessibleScreenshot.saveImage();
				exitCurrScreenShot();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "保存失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	// 退出当前截图
	public void exitCurrScreenShot() {
		if (accessibleScreenshot != null) {
			accessibleScreenshot.cancel();
			if (accessibleScreenshot.tools != null) {
				accessibleScreenshot.tools.dispose();
			}
		}
	}

	// 最小化系统托盘
	protected boolean minimizeTray() throws AWTException {
		MinimizeTrayJPanel mtj = new MinimizeTrayJPanel(this);
		return mtj.init();
	}

	public void startScreenShot() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 先判断是否存在当前截图
					// 有则取消
					exitCurrScreenShot();
					accessibleScreenshot = new Screenshot(jf, false, sm);
					accessibleScreenshot.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听截图按钮
		if (e.getSource().equals(button1)) {
			startScreenShot();
		}
		// 监听设置按钮
		if (e.getSource().equals(button2)) {
			// false表示已打开，true才可以打开
			if (!ScreenShotMainGUI.isOpenSetupJF) {
				ScreenShotMainGUI.isOpenSetupJF = true;
				SetupJF sjp = new SetupJF();
			}
		}
	}

}
