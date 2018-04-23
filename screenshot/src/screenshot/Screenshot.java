package screenshot;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

//继承jwindow，无边框，与jframe同等级
public class Screenshot extends JWindow {
	public JFrame jf;
	private int startx, starty, endx, endy;
	// 标记范围
	private int startx2, starty2, endx2, endy2;
	// 最大截图标记，保存的最大范围
	public int xx, yy, ww, hh;
	// 第一次取到的屏幕
	private BufferedImage image = null;
	// 缓存加深颜色的主屏幕
	private BufferedImage tempImage = null;
	// 需要保存的图片
	private BufferedImage saveImage = null;
	// 是否开始画笔
	public boolean isdraw = false;
	// 缓存而已
	public BufferedImage tempImage2 = null;
	// 用于使用画笔的缓存
	public BufferedImage tempImage3 = null;

	// 剪切板
	Clipboard clipboard;
	// 工具类
	public ToolsWindow tools = null;
	public SetupMsg sm;

	// 普通初始化
	public Screenshot(boolean bl) throws AWTException {
		init(bl);
	}

	public Screenshot() throws AWTException {
		// init(bl);
	}

	// 无障碍初始化
	public Screenshot(JFrame jf, Boolean bl, SetupMsg sm) throws AWTException {
		this.jf = jf;
		this.sm = sm;
		init(bl);
	}

	private void init(boolean bl) throws AWTException {
		// 若无障碍则隐藏主体jframe（最小化）
		if (bl == true) {
			jf.setExtendedState(JFrame.ICONIFIED);
		}
		this.setVisible(true);
		// 获取屏幕尺寸
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, d.width, d.height);
		// 截取最大屏幕
		Robot robot = new Robot();
		image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		// 初始化就给全屏
		saveImage = image;

		// 将图片全屏显示浮于上方

		// 监听鼠标点击松开
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 记录鼠标点击行为 坐标
				if (isdraw) {
					// 记录画框的坐标
					startx2 = e.getX();
					starty2 = e.getY();
				} else {
					// 截图的坐标
					startx = e.getX();
					starty = e.getY();
					if (tools != null) {
						tools.setVisible(false);
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (isdraw) {
					// 如果是画框则每次都保存最新的图片
					tempImage2 = tempImage3;
					saveImage = tempImage2.getSubimage(xx, yy, ww, hh);
				} else {
					// 鼠标松开时，显示工具栏
					if (tools == null) {
						tools = new ToolsWindow(Screenshot.this, e.getX(), e.getY());
					} else {
						tools.setLocation(e.getX(), e.getY());
					}
					tools.setVisible(true);
					tools.toFront();
				}

			}
		});

		// 监听鼠标拖动
		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				// 是否标记重点(画框)
				if (isdraw) {
					// 鼠标拖动时，记录画框坐标
					endx2 = e.getX();
					endy2 = e.getY();
					// 先创建个缓存图片
					tempImage3 = (BufferedImage) createImage(Screenshot.this.getWidth(), Screenshot.this.getHeight());
					// 获取缓存的画笔
					Graphics2D g2 = (Graphics2D) tempImage3.getGraphics();
					// 将之前截好并显示的图加入进来
					g2.drawImage(tempImage2, 0, 0, null);
					int x2 = Math.min(startx2, endx2);
					int y2 = Math.min(starty2, endy2);
					int width2 = Math.abs(endx2 - startx2) + 1;
					int height2 = Math.abs(endy2 - starty2) + 1;
					g2.setColor(sm.getgColor() != null ? sm.getgColor() : Color.RED);
					// 加粗
					g2.setStroke(new BasicStroke(sm.getgSize() != 0 ? sm.getgSize() : 1.0f));
					// 在此基础上画框
					g2.drawRect(x2 - 1, y2 - 1, width2 + 1, height2 + 1);
					// 画完框将此缓存图片显示在整个屏幕
					Screenshot.this.getGraphics().drawImage(tempImage3, 0, 0, Screenshot.this);

				} else {
					// 鼠标拖动时，记录坐标
					endx = e.getX();
					endy = e.getY();
					int x = Math.min(startx, endx);
					int y = Math.min(starty, endy);
					int width = Math.abs(endx - startx) + 1;
					int height = Math.abs(endy - starty) + 1;
					// 创建缓存图片
					tempImage2 = (BufferedImage) createImage(Screenshot.this.getWidth(), Screenshot.this.getHeight());

					// 获取画布画笔
					Graphics g = tempImage2.getGraphics();
					// 将加深颜色的整个屏幕加入
					g.drawImage(tempImage, 0, 0, null);

					g.setColor(Color.BLUE);
					// 画截图的范围
					g.drawRect(x - 1, y - 1, width + 1, height + 1);
					// 使用刚开始没有任何变化的屏幕根据坐标截取范围
					saveImage = image.getSubimage(x, y, width, height);
					// 加入（即截取的部分颜色又显示正常）
					g.drawImage(saveImage, x, y, null);
					// 显示在屏幕上
					Screenshot.this.getGraphics().drawImage(tempImage2, 0, 0, Screenshot.this);
					// 记录最后的截图范围坐标
					xx = x;
					yy = y;
					ww = width;
					hh = height;
				}

			}
		});

		// 置顶
		this.setAlwaysOnTop(true);
	}

	// 绘制图片
	@Override
	public void paint(Graphics g) {
		// 绘制一个全屏幕加深的图片
		RescaleOp ro = new RescaleOp(0.7f, 0, null);
		tempImage = ro.filter(image, null);
		g.drawImage(tempImage, 0, 0, this);
	}

	// 保存图片
	public void saveImage() throws IOException {
		boolean isCus;
		// 如果文件不存在，其默认值是false，需要手动改成自定义保存
		if (!ScreenShotMainGUI.fileExit) {
			isCus = true;
		} else {
			isCus = sm.isCustomizeSave();
		}

		String path = "C:\\";
		// 时间戳
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String fileName = sdf.format(new Date());
		String imgFormat = sm.getImgFormat() != null ? sm.getImgFormat() : "png";
		if (isCus) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("保存");

			// 文件过滤器，用户过滤可选择文件
			// FileNameExtensionFilter filter = new
			// FileNameExtensionFilter("JPG", "jpg");
			// jfc.setFileFilter(filter);

			// 初始化一个默认文件
			File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
			File defaultFile = new File(filePath + File.separator + fileName + "." + imgFormat);
			jfc.setSelectedFile(defaultFile);

			int flag = jfc.showSaveDialog(jf);
			if (flag == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				path = file.getPath();
				// 检查文件后缀，防止用户忘记输入后缀或者输入不正确的后缀
				if (!(path.endsWith(".png") || path.endsWith(".PNG"))
						&& !(path.endsWith(".jpg") || path.endsWith(".JPG"))
						&& !(path.endsWith(".bmp") || path.endsWith(".BMP"))
						&& !(path.endsWith(".jpeg") || path.endsWith(".JPEG"))
						&& !(path.endsWith(".gif") || path.endsWith(".GIF"))) {
					path += ".png";
				}
			} else {
				dispose();
				return;
			}
		} else {
			// 直接默认保存
			File f = new File(sm.getCustomSavePath());
			if (!f.exists()) {
				path = "C:\\" + fileName + "." + imgFormat;
			} else {
				path = sm.getCustomSavePath() + "\\" + fileName + "." + imgFormat;
			}
		}
		// 写入文件
		// 点击一下表示截全屏
		if (saveImage == null) {
			saveImage = image;
		}
		ImageIO.write(saveImage, imgFormat, new File(path));
		dispose();
	}

	// 复制到剪切板
	public void copyClipImage() throws IOException {
		// 获得系统剪贴板
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 这步不太懂
		Transferable trans = new Transferable() {
			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return saveImage;
				}
				throw new UnsupportedFlavorException(flavor);
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}
		};
		// 添加到剪切板
		clipboard.setContents(trans, null);
		cancel();
	}

	// 窗口隐藏
	public void cancel() {
		// 隐藏操作窗口
		dispose();
	}

	public void drawImage() {
		isdraw = true;
	}
}

/*
 * 操作窗口
 */
class ToolsWindow extends JWindow {
	private Screenshot parent;
	ImageIcon copyImage = new ImageIcon(getClass().getResource("copy.png"));
	ImageIcon saveImage = new ImageIcon(getClass().getResource("save.png"));
	ImageIcon cancelImage = new ImageIcon(getClass().getResource("cancel.png"));
	ImageIcon drawImage = new ImageIcon(getClass().getResource("draw.png"));

	public ToolsWindow(Screenshot parent, int x, int y) {
		this.parent = parent;
		this.init();
		this.setLocation(x, y);
		this.pack();
		this.setVisible(true);

	}

	private void init() {
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar("a");

		//
		JButton copyClipButton = new JButton(copyImage);
		copyClipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parent.copyClipImage();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(copyClipButton);

		// 保存按钮
		JButton saveButton = new JButton(saveImage);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parent.saveImage();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(saveButton);

		// 标记
		JButton drawButton = new JButton(drawImage);
		drawButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.drawImage();
				// dispose();
			}
		});
		toolBar.add(drawButton);

		// 关闭按钮
		JButton closeButton = new JButton(cancelImage);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.cancel();
				dispose();
			}
		});
		toolBar.add(closeButton);
		this.add(toolBar, BorderLayout.NORTH);

		// 置顶
		this.setAlwaysOnTop(true);
	}

}
