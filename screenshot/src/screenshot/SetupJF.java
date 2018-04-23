package screenshot;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SetupJF extends JFrame implements ActionListener {
	public JPanel jp1;
	public JButton button1, button2, button3;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10, jlb11, jlb12, jlb13, jlb14, jlb15, jlb16,
			jlb17;
	public JRadioButton jrb1, jrb2;
	public ButtonGroup bg1;
	public JFrame jf;
	public JTextField tt1, tt2, tt3, tt4, tt5, tt6;
	private JComboBox jcb1, jcb2, jcb3, jcb4;
	public JCheckBox jcbox1, jcbox2, jcbox3;
	public SetupParams sp;
	// 光标在的时候标志
	public boolean isOnFocus = false;
	public static boolean fileExit2 = false;
	// public char keyPressed;
	// public char keyReleased;
	// 将按键存入set集合
	public static Set<String> keyList = new HashSet<String>();
	public SshotUtils su = new SshotUtils();
	public SetupMsg sm;
	public SetupUtils sus = new SetupUtils();

	public SetupJF() {
		this.jf = this;
		initPro();
		// 初始化
		init();
	}

	// 初始化配置
	public void initPro() {
		// 初始化按键对照表
		// 原理：首先判断exe所在目录是否存在隐藏的配置文件screentshotsetup.properties，
		// 如果存在，则读取此配置文件，如果不存在则生成一个默认的隐藏的配置文件
		// 实例化一个参数对象
		// 这个是用于设置具体参数
		sm = new SetupMsg();
		// 这个是用于显示具体参数
		sp = new SetupParams();
		File f = new File("screentshotsetup.properties");
		// 判断是否存在文件
		fileExit2 = su.isFileExit(f);
		if (fileExit2) {
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

	private void init() {
		// 初始化面板
		jp1 = new JPanel();
		jp1.setLayout(null);

		// 基本设置
		jlb1 = new JLabel("基本设置：");
		jlb1.setBounds(10, 10, 80, 25);
		jp1.add(jlb1);

		jlb3 = new JLabel("保存设置：");
		jlb3.setBounds(37, 40, 80, 25);
		jp1.add(jlb3);

		tt1 = new JTextField();
		tt1.setBounds(107, 70, 252, 25);
		jp1.add(tt1);
		tt1.setEditable(false);

		jrb1 = new JRadioButton("自定义保存");
		jrb2 = new JRadioButton("默认路径保存");
		// 如果文件不存在，其默认值是false，需要手动改成自定义保存
		if (!ScreenShotMainGUI.fileExit) {
			jrb1.setSelected(true);
			jrb2.setSelected(false);
			tt1.setVisible(false);
			tt1.setText("");
		} else {
			jrb1.setSelected(sp.isCustomizeSave());
			jrb2.setSelected(!sp.isCustomizeSave());
		}
		if (jrb2.isSelected()) {
			tt1.setVisible(true);
			tt1.setText(sp.getCustomSavePath() != null ? sp.getCustomSavePath() : "C://");
		} else {
			tt1.setVisible(false);
			tt1.setText("");
		}

		jrb1.setBounds(100, 40, 100, 25);
		jrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb1.isSelected()) {
					tt1.setVisible(false);
					tt1.setText("");
				} else {

				}
			}
		});
		jp1.add(jrb1);

		jrb2.setBounds(250, 40, 130, 25);
		jrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb2.isSelected()) {
					if (!ScreenShotMainGUI.fileExit) {
						// 打开文件夹
						// 接收文件
						JFileChooser chooser = new JFileChooser();
						// 设定只能选择到文件夹
						chooser.setFileSelectionMode(1);
						chooser.setDialogTitle("选择默认保存路径");
						// 默认文件名称还有放在当前目录下
						int s = chooser.showDialog(jf, "保存");
						if (s == 1) {
							if (!tt1.isVisible()) {
								jrb1.setSelected(true);
								jrb2.setSelected(false);
								tt1.setVisible(false);
								tt1.setText("");
							} else {

							}
							return;
						} else {
							// 保存路径
							String saveFilePath = chooser.getSelectedFile().toString();
							tt1.setText(saveFilePath);
							tt1.setVisible(true);
						}
					} else {
						if (sp.isCustomizeSave) {
							// 打开文件夹
							// 接收文件
							JFileChooser chooser = new JFileChooser();
							// 设定只能选择到文件夹
							chooser.setFileSelectionMode(1);
							chooser.setDialogTitle("选择默认保存路径");
							// 默认文件名称还有放在当前目录下
							int s = chooser.showDialog(jf, "保存");
							if (s == 1) {
								if (!tt1.isVisible()) {
									jrb1.setSelected(true);
									jrb2.setSelected(false);
									tt1.setVisible(false);
									tt1.setText("");
								} else {

								}
								return;
							} else {
								// 保存路径
								String saveFilePath = chooser.getSelectedFile().toString();
								tt1.setText(saveFilePath);
								tt1.setVisible(true);
							}
						} else {
							if (ScreenShotMainGUI.isJustSatrt == true) {
								// 如果是首次启动
								tt1.setVisible(true);
								tt1.setText(sp.getCustomSavePath() != null ? sp.getCustomSavePath() : "C://");
								// 改为不是首次启动
								ScreenShotMainGUI.isJustSatrt = false;
							} else {
								// 打开文件夹
								// 接收文件
								JFileChooser chooser = new JFileChooser();
								// 设定只能选择到文件夹
								chooser.setFileSelectionMode(1);
								chooser.setDialogTitle("选择默认保存路径");
								// 默认文件名称还有放在当前目录下
								int s = chooser.showDialog(jf, "保存");
								if (s == 1) {
									if (!tt1.isVisible()) {
										jrb1.setSelected(true);
										jrb2.setSelected(false);
										tt1.setVisible(false);
										tt1.setText("");
									} else {

									}
									return;
								} else {
									// 保存路径
									String saveFilePath = chooser.getSelectedFile().toString();
									tt1.setText(saveFilePath);
									tt1.setVisible(true);
								}
							}
						}

					}

				} else {
					tt1.setVisible(false);
					tt1.setText("");
				}
			}
		});
		jp1.add(jrb2);
		bg1 = new ButtonGroup();
		bg1.add(jrb1);
		bg1.add(jrb2);

		jlb4 = new JLabel("画笔设置：");
		jlb4.setBounds(37, 100, 80, 25);
		jp1.add(jlb4);

		jlb5 = new JLabel("颜色：");
		jlb5.setBounds(108, 100, 50, 25);
		jp1.add(jlb5);

		String[] color = { "红色", "蓝色", "黑色", "黄色", "橙色", "绿色" };
		jcb1 = new JComboBox(color);
		// 设置默认显示值
		jcb1.setSelectedIndex(sp.getgColorP());
		jcb1.setBounds(153, 100, 70, 25);
		jp1.add(jcb1);

		jlb6 = new JLabel("粗   细：");
		jlb6.setBounds(230, 100, 55, 25);
		jp1.add(jlb6);

		String[] gSize = { "1", "2", "3", "4", "5" };
		jcb2 = new JComboBox(gSize);
		// 设置默认显示值
		jcb2.setSelectedIndex(sp.getgSizeP());
		jcb2.setBounds(288, 100, 70, 25);
		jp1.add(jcb2);

		jlb7 = new JLabel("图片设置：");
		jlb7.setBounds(37, 130, 80, 25);
		jp1.add(jlb7);

		jlb8 = new JLabel("格式：");
		jlb8.setBounds(108, 130, 50, 25);
		jp1.add(jlb8);

		String[] format = { ".png", ".jpg", ".bmp", ".jpeg", ".gif" };
		jcb3 = new JComboBox(format);
		// 设置默认显示值
		jcb3.setSelectedIndex(sp.getImgFormatP());
		jcb3.setBounds(153, 130, 70, 25);
		jp1.add(jcb3);

		jlb9 = new JLabel("清晰度：");
		jlb9.setBounds(230, 130, 60, 25);
		jp1.add(jlb9);

		String[] definition = { "一般", "高清", "超清" };
		jcb4 = new JComboBox(definition);
		// 设置默认显示值
		jcb4.setSelectedIndex(sp.getImgSharpness());
		jcb4.setBounds(288, 130, 70, 25);
		jp1.add(jcb4);

		jlb15 = new JLabel("其他设置：");
		jlb15.setBounds(37, 160, 80, 25);
		jp1.add(jlb15);

		jcbox1 = new JCheckBox("开机自启动", false);
		jcbox1.setBounds(102, 160, 100, 25);
		jp1.add(jcbox1);
		jcbox1.setSelected(sp.isSelfStart());

		jcbox2 = new JCheckBox(" 启动最小化", false);
		jcbox2.setBounds(256, 160, 110, 25);
		jp1.add(jcbox2);
		jcbox2.setSelected(sp.isStartMinSize());

		// jcbox3 = new JCheckBox(" 注册成服务", false);
		// jcbox3.setBounds(108, 160, 50, 25);
		// jp1.add(jcbox3);

		// 快捷设置
		jlb2 = new JLabel("快捷设置：");
		jlb2.setBounds(10, 200, 80, 25);
		jp1.add(jlb2);

		jlb10 = new JLabel("截图设置：");
		jlb10.setBounds(37, 240, 80, 25);
		jp1.add(jlb10);

		tt2 = new JTextField();
		tt2.setBounds(107, 240, 252, 25);
		tt2.setText(sp.getsShotHotKey() != null ? sp.getsShotHotKey() : "Shift + Ctrl + A");
		jp1.add(tt2);
		// 文本框加入键盘监听
		tt2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyPressedCode = e.getKeyCode();
				sus.keyPressedUtils(keyPressedCode, tt2);
			}
		});

		tt2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyReleased = e.getKeyCode();
				keyReleasedUtils(keyReleased, tt2);
			}
		});

		// tt2.setEditable(false);

		jlb11 = new JLabel("保存设置：");
		jlb11.setBounds(37, 270, 80, 25);
		jp1.add(jlb11);

		tt3 = new JTextField();
		tt3.setText(sp.getSaveHotKey() != null ? sp.getSaveHotKey() : "Shift + Ctrl + S");
		tt3.setBounds(107, 270, 252, 25);
		jp1.add(tt3);
		// 文本框加入键盘监听
		tt3.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyPressedCode = e.getKeyCode();
				sus.keyPressedUtils(keyPressedCode, tt3);
			}
		});

		tt3.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyReleased = e.getKeyCode();
				keyReleasedUtils(keyReleased, tt3);
			}
		});

		jlb12 = new JLabel("复制设置：");
		jlb12.setBounds(37, 300, 80, 25);
		jp1.add(jlb12);

		tt4 = new JTextField();
		tt4.setText(sp.getCopyHotKey() != null ? sp.getCopyHotKey() : "Shift + Ctrl + Z");
		tt4.setBounds(107, 300, 252, 25);
		jp1.add(tt4);
		// 文本框加入键盘监听
		tt4.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyPressedCode = e.getKeyCode();
				sus.keyPressedUtils(keyPressedCode, tt4);
			}
		});

		tt4.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyReleased = e.getKeyCode();
				keyReleasedUtils(keyReleased, tt4);
			}
		});

		jlb13 = new JLabel("退出设置：");
		jlb13.setBounds(37, 330, 80, 25);
		jp1.add(jlb13);

		tt5 = new JTextField();
		tt5.setText(sp.getExitHotKey() != null ? sp.getExitHotKey() : "Ctrl + Alt + Q");
		tt5.setBounds(107, 330, 252, 25);
		jp1.add(tt5);
		// 文本框加入键盘监听
		tt5.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyPressedCode = e.getKeyCode();
				sus.keyPressedUtils(keyPressedCode, tt5);
			}
		});

		tt5.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyReleased = e.getKeyCode();
				keyReleasedUtils(keyReleased, tt5);
			}
		});

		jlb14 = new JLabel("取消设置：");
		jlb14.setBounds(37, 360, 80, 25);
		jp1.add(jlb14);

		tt6 = new JTextField();
		tt6.setText(sp.getCancelHotKey() != null ? sp.getCancelHotKey() : "Ctrl + Q");
		tt6.setBounds(107, 360, 252, 25);
		jp1.add(tt6);
		// 文本框加入键盘监听
		tt6.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyPressedCode = e.getKeyCode();
				sus.keyPressedUtils(keyPressedCode, tt6);
			}
		});

		tt6.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyReleased = e.getKeyCode();
				keyReleasedUtils(keyReleased, tt6);
			}
		});

		button1 = new JButton("确定");
		button1.setBounds(50, 410, 60, 25);
		jp1.add(button1);
		button1.addActionListener(this);

		button2 = new JButton("关于");
		button2.setBounds(170, 410, 60, 25);
		jp1.add(button2);
		button2.addActionListener(this);

		button3 = new JButton("取消");
		button3.setBounds(290, 410, 60, 25);
		jp1.add(button3);
		button3.addActionListener(this);

		this.add(jp1);
		this.setTitle("截图设置");
		this.setSize(400, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("setup.png"));
		this.setIconImage(imageIcon.getImage());

		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				// 更改状态
				ScreenShotMainGUI.isOpenSetupJF = false;
			}
		});

	}

	// 键盘松开事件监听
	protected void keyReleasedUtils(int keyReleased, JTextField tt) {

		// 每次判断list个数，大于1则是组合键，小于等于1是单个按键
		if (keyList != null && keyList.size() > 1) {
			// 组合键
			// 将按键挨个取出判断是什么键
			// for (String key : keyList) {
			// System.out.println(key);
			// }
			// 判断是否只是包含控制键
			if (sus.onlyInSCA(keyList) || !sus.onlyOneNumOrAlp(keyList)) {
				tt.setText("");
				JOptionPane.showMessageDialog(null, "必须包含且只包含一个字母键或者数字键！如：Shift+Ctrl+A！", "提示消息",
						JOptionPane.WARNING_MESSAGE);
			} else {
				// 识别好之后显示在界面
				tt.setText(sus.getShowKey(keyList));
			}
			keyList.clear();
		} else if (keyList.size() == 1) {
			tt.setText("");
			// 只按住了一个按键
			// 不允许单个按键作为快捷键，防止热键冲突
			JOptionPane.showMessageDialog(null, "请设置组合键！如：Shift+Ctrl+A！", "提示消息", JOptionPane.WARNING_MESSAGE);
			// 松开按键后将松开的按键从list删除
			sus.deleteKey(keyReleased);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听确定保存按钮
		if (e.getSource().equals(button1)) {
			// 获取所有的参数
			SetupParams spp = getParams();
			// 检查参数
			int flag = checkParams(spp);
			if (flag == 0) {
				// 判断是否开机自启动，如果是则将快捷文件写入启动文件夹，不是的话则删除启动文件夹
				// 程序自动先创建一个快捷方式
				String lnk = "ScreenShot.exe.lnk";
				String appName = "ScreenShot.exe";
				// 如果开机自启动
				if (spp.isSelfStart()) {
					// 如果同级目录没有快捷方式
					File f = new File(lnk);
					if (!f.exists()) {
						// 不存在则创建
						if (!sus.createLnk(appName)) {
							// 创建失败则继续
							spp.setSelfStart(false);
							jcbox1.setSelected(false);
							JOptionPane.showMessageDialog(null, "创建快捷方式失败！请手动创建并重试！", "提示消息",
									JOptionPane.WARNING_MESSAGE);
						} else {
							// 创建成功则设置
							if (!sus.setAutoStart(spp.isSelfStart(), lnk)) {
								spp.setSelfStart(false);
								jcbox1.setSelected(false);
								JOptionPane.showMessageDialog(null, "设置开机自启动失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						// 存在则设置
						if (!sus.setAutoStart(spp.isSelfStart(), lnk)) {
							spp.setSelfStart(false);
							jcbox1.setSelected(false);
							JOptionPane.showMessageDialog(null, "设置开机自启动失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					}

				} else {
					// 不启动则取消
					if (!sus.setAutoStart(spp.isSelfStart(), lnk)) {
						spp.setSelfStart(true);
						jcbox1.setSelected(true);
						JOptionPane.showMessageDialog(null, "取消开机自启动失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				}

				// 直接写入文件
				// 存入配置文件
				try {
					if (su.setupMsgToPro(spp)) {
						JOptionPane.showMessageDialog(null, "保存设置成功！重启生效！", "提示消息", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "保存设置失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "保存设置失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}

				// 关闭设置界面
				ScreenShotMainGUI.isOpenSetupJF = false;
				dispose();
			} else if (flag == 1) {
				JOptionPane.showMessageDialog(null, "请设置快捷键！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

		// 监听关于按钮
		if (e.getSource().equals(button2)) {
			String about = " 1. 默认快捷键：截图：Shift+Ctrl+A，保存：Shift+Ctrl+S，复制：Shift+Ctrl+Z，退出：Ctrl+Alt+Q，取消：Ctrl+Q。\r\n 2. 设置快捷键使用组合键，最少两个键且不息包含字母键或数字键，如：Shift+Ctrl+A，避免热键冲突。\r\n 3. 清晰度设置尚未实现。\r\n 4. 若默认保存路径不存在，则自动保存在C盘根目录。\r\n 5. 本程序依赖Java，若没有Java环境则复制jre文件夹并改名为“jre”，复制到此程序同级目录即可。";
			JOptionPane.showMessageDialog(null, about, "提示消息", JOptionPane.WARNING_MESSAGE);
		}

		// 监听取消按钮
		if (e.getSource().equals(button3)) {
			ScreenShotMainGUI.isOpenSetupJF = false;
			dispose();
		}
	}

	// 获取参数
	private SetupParams getParams() {
		SetupParams spp = new SetupParams();
		// 获取基本设置
		// 保存设置
		spp.setCustomizeSave(jrb1.isSelected());
		spp.setCustomSavePath(tt1.getText().replaceAll("\\\\", "\\\\\\\\"));

		// 画笔设置
		// 颜色
		spp.setgColorP(jcb1.getSelectedIndex());
		// 粗细
		spp.setgSizeP(jcb2.getSelectedIndex());

		// 图片设置
		// 格式
		spp.setImgFormatP(jcb3.getSelectedIndex());
		// 清晰度
		spp.setImgSharpness(jcb4.getSelectedIndex());
		// 是否自启动
		spp.setSelfStart(jcbox1.isSelected());
		// 是否启动最小化
		spp.setStartMinSize(jcbox2.isSelected());

		// 快捷设置
		// 截图
		spp.setsShotHotKey(tt2.getText().trim());
		// 保存
		spp.setSaveHotKey(tt3.getText().trim());
		// 复制
		spp.setCopyHotKey(tt4.getText().trim());
		// 退出
		spp.setExitHotKey(tt5.getText().trim());
		// 取消
		spp.setCancelHotKey(tt6.getText().trim());
		// 端口
		spp.setPort(String.valueOf(sm.getPort()));

		return spp;
	}

	// 校验参数
	private int checkParams(SetupParams spp) {
		if ("".equals(spp.getsShotHotKey()) || "".equals(spp.getSaveHotKey()) || "".equals(spp.getCopyHotKey())
				|| "".equals(spp.getExitHotKey()) || "".equals(spp.getCancelHotKey())) {
			return 1;

		}
		return 0;
	}

}
