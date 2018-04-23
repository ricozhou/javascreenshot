package screenshot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.melloware.jintellitype.JIntellitype;

public class SshotUtils {
	// 通过实例化占用一个serverSocket端口来判断是否已经启动
	private static ServerSocket serverSocket = null;
	// 占用一个端口号
	// private static final int serverPort = 12345;

	// 检查是否被占用,被占用说明已经启动则返回
	public boolean checkSocket(int port) {
		try {
			serverSocket = new ServerSocket(port);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isFileExit(File file) {
		if (file.exists()) {
			return true;
		}
		return false;
	}

	// 从配置文件中获取参数
	public SetupParams getDefaultMsg(File f) throws Exception {
		Properties pro = new Properties();
		FileInputStream fis = new FileInputStream(f);
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		SetupParams sp = new SetupParams();
		// 基本设置
		sp.setCustomizeSave("0".equals(pro.getProperty("iscustomizesave")) ? true : false);
		sp.setCustomSavePath(pro.getProperty("customsavepath"));
		sp.setgColorP(Integer.valueOf(pro.getProperty("gcolorp")));
		sp.setgSizeP(Integer.valueOf(pro.getProperty("gsizep")));
		sp.setImgFormatP(Integer.valueOf(pro.getProperty("imgformatp")));
		sp.setImgSharpness(Integer.valueOf(pro.getProperty("imgsharpness")));
		sp.setSelfStart("0".equals(pro.getProperty("isselfstart")) ? true : false);
		sp.setStartMinSize("0".equals(pro.getProperty("isstartminsize")) ? true : false);

		// 快捷键设置
		sp.setsShotHotKey(pro.getProperty("sshothotkey"));
		sp.setSaveHotKey(pro.getProperty("savehotkey"));
		sp.setCopyHotKey(pro.getProperty("copyhotkey"));
		sp.setExitHotKey(pro.getProperty("exithotkey"));
		sp.setCancelHotKey(pro.getProperty("cancelhotkey"));

		// 获取占用端口
		sp.setPort(pro.getProperty("port"));

		fis.close();
		bf.close();
		return sp;
	}

	// 将参数转换
	public SetupMsg trunSm(SetupParams sp) {
		SetupMsg sm = new SetupMsg();
		// 是否自定义保存
		sm.setCustomizeSave(sp.isCustomizeSave());
		// 保存路径
		sm.setCustomSavePath(sp.getCustomSavePath());
		// 画笔颜色
		Color c = Color.RED;
		int cnum = sp.getgColorP();
		if (cnum == 0) {
			// 红
			c = Color.RED;
		} else if (cnum == 1) {
			// 蓝
			c = Color.BLUE;
		} else if (cnum == 2) {
			// 黑
			c = Color.BLACK;
		} else if (cnum == 3) {
			// 黄
			c = Color.YELLOW;
		} else if (cnum == 4) {
			// 橙
			c = Color.ORANGE;
		} else if (cnum == 5) {
			// 绿
			c = Color.GREEN;
		}
		sm.setgColor(c);

		// 画笔粗细
		float gp = 1;
		int gpp = sp.getgSizeP();
		if (gpp == 0) {
			gp = 1;
		} else if (gpp == 1) {
			gp = 2;
		} else if (gpp == 2) {
			gp = 3;
		} else if (gpp == 3) {
			gp = 4;
		} else if (gpp == 4) {
			gp = 5;
		}
		sm.setgSize(gp);

		// 图片格式
		String gf = "png";
		int gff = sp.getImgFormatP();
		if (gff == 0) {
			gf = "png";
		} else if (gff == 1) {
			gf = "jpg";
		} else if (gff == 2) {
			gf = "bmp";
		} else if (gff == 3) {
			gf = "jpeg";
		} else if (gff == 4) {
			gf = "gif";
		}
		sm.setImgFormat(gf);

		// 图片清晰度
		sm.setImgSharpness(sp.getImgSharpness());
		// 是否自启动
		sm.setSelfStart(sp.isSelfStart());
		// 是否最小化
		sm.setStartMinSize(sp.isStartMinSize());
		// 快捷键参数转换
		sm.setsShotHotKey(sp.getsShotHotKey());
		sm.setSaveHotKey(sp.getSaveHotKey());
		sm.setCopyHotKey(sp.getCopyHotKey());
		sm.setExitHotKey(sp.getExitHotKey());
		sm.setCancelHotKey(sp.getCancelHotKey());

		// 并且以list格式存入
		sm.setSshkList(returnKeyList(sp.getsShotHotKey()));
		sm.setShkList(returnKeyList(sp.getSaveHotKey()));
		sm.setChkList(returnKeyList(sp.getCopyHotKey()));
		sm.setEhkList(returnKeyList(sp.getExitHotKey()));
		sm.setCchkList(returnKeyList(sp.getCancelHotKey()));

		// 端口
		int port = 0;
		if (sp.getPort() != null && !"".equals(sp.getPort())) {
			port = Integer.valueOf(sp.getPort());
		}
		sm.setPort(port);

		// 返回
		return sm;
	}

	// string to list
	private List<String> returnKeyList(String hk) {
		List<String> list = new ArrayList<String>();
		// 拆分成数组
		String[] sa = hk.replaceAll(" ", "").split("\\+");
		// 遍历数组根据value从map中取出key,加入集合
		// 先找控制键
		for (String s : sa) {
			// 从map中取出key
			for (Map.Entry<String, String> entry : ScreenShotMainGUI.keyMap2.entrySet()) {
				// 如果找到则加入
				if (s.equals(entry.getValue())) {
					list.add(entry.getKey());
					// 都是唯一对应，找到直接退出
					break;
				}
			}
		}

		// 再找其他键
		for (String s : sa) {
			// 从map中取出key
			for (Map.Entry<String, String> entry : ScreenShotMainGUI.keyMap.entrySet()) {
				// 如果找到则加入
				if (s.equals(entry.getValue())) {
					list.add(entry.getKey());
					// 都是唯一对应，找到直接退出
					break;
				}
			}
		}

		return list;
	}

	// 保存设置
	public boolean setupMsgToPro(SetupParams spp) {
		File f;
		String content = "";
		f = new File("screentshotsetup.properties");
		content = "iscustomizesave=" + (spp.isCustomizeSave() ? "0" : "1") + "\r\n" + "customsavepath="
				+ spp.getCustomSavePath() + "\r\n" + "gcolorp=" + spp.getgColorP() + "\r\n" + "gsizep="
				+ spp.getgSizeP() + "\r\n" + "imgformatp=" + spp.getImgFormatP() + "\r\n" + "imgsharpness="
				+ spp.getImgSharpness() + "\r\n" + "isselfstart=" + (spp.isSelfStart() ? "0" : "1") + "\r\n"
				+ "isstartminsize=" + (spp.isStartMinSize() ? "0" : "1") + "\r\n" + "sshothotkey="
				+ spp.getsShotHotKey() + "\r\n" + "savehotkey=" + spp.getSaveHotKey() + "\r\n" + "copyhotkey="
				+ spp.getCopyHotKey() + "\r\n" + "exithotkey=" + spp.getExitHotKey() + "\r\n" + "cancelhotkey="
				+ spp.getCancelHotKey() + "\r\n" + "port=" + spp.getPort();
		// 先删除隐藏文件再重新创建，隐藏文件不支持修改
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);// 创建文件输出流对象
			// 设置文件的隐藏属性
			String set = "attrib +H " + f.getAbsolutePath();
			Runtime.getRuntime().exec(set);
			// 将字符串写入到文件中
			fos.write(content.getBytes());
			return true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	// 为了防止设置热键时已经设好的热键不能设置了，特此处理
	public void proKeyBeforeSshot(int keyMark, SetupMsg sm) {
		List<String> list = new ArrayList<String>();
		if (keyMark == 0) {
			list = sm.getSshkList();
			if (list == null || list.size() <= 0) {
				list.add("16");
				list.add("17");
				list.add("65");
			}
		} else if (keyMark == 1) {
			list = sm.getShkList();
			if (list == null || list.size() <= 0) {
				list.add("16");
				list.add("17");
				list.add("83");
			}
		} else if (keyMark == 2) {
			list = sm.getChkList();
			if (list == null || list.size() <= 0) {
				list.add("16");
				list.add("17");
				list.add("90");
			}
		} else if (keyMark == 3) {
			list = sm.getEhkList();
			if (list == null || list.size() <= 0) {
				list.add("17");
				list.add("18");
				list.add("81");
			}
		} else if (keyMark == 4) {
			list = sm.getCchkList();
			if (list == null || list.size() <= 0) {
				list.add("17");
				list.add("81");
			}
		}
		// 先记录按键
		// hotKey = "aa";
		SetupJF.keyList.clear();
		for (String s : list) {
			SetupJF.keyList.add(s);
		}
	}

	// 将list转为参数
	public int[] proParamList(List<String> hkList, int f) {
		int[] params = new int[2];
		if (hkList != null && hkList.size() > 0) {
			for (String s : hkList) {
				if (Integer.valueOf(s) == 16) {
					// shift
					params[0] = params[0] + JIntellitype.MOD_SHIFT;
				} else if (Integer.valueOf(s) == 17) {
					params[0] = params[0] + JIntellitype.MOD_CONTROL;
				} else if (Integer.valueOf(s) == 18) {
					params[0] = params[0] + JIntellitype.MOD_ALT;
				} else {
					params[1] = params[1] + Integer.valueOf(s);
				}
			}
		} else {
			if (f == 0) {
				params[0] = JIntellitype.MOD_SHIFT + JIntellitype.MOD_CONTROL;
				params[1] = 65;
			} else if (f == 1) {
				params[0] = JIntellitype.MOD_SHIFT + JIntellitype.MOD_CONTROL;
				params[1] = 83;
			} else if (f == 2) {
				params[0] = JIntellitype.MOD_SHIFT + JIntellitype.MOD_CONTROL;
				params[1] = 90;
			} else if (f == 3) {
				params[0] = JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT;
				params[1] = 81;
			} else if (f == 4) {
				params[0] = JIntellitype.MOD_CONTROL;
				params[1] = 81;
			}
		}

		return params;
	}

	// 初始化KeyMap
	public void initKeyMap() {
		// 字母
		ScreenShotMainGUI.keyMap.put("65", "A");
		ScreenShotMainGUI.keyMap.put("66", "B");
		ScreenShotMainGUI.keyMap.put("67", "C");
		ScreenShotMainGUI.keyMap.put("68", "D");
		ScreenShotMainGUI.keyMap.put("69", "E");
		ScreenShotMainGUI.keyMap.put("70", "F");
		ScreenShotMainGUI.keyMap.put("71", "G");
		ScreenShotMainGUI.keyMap.put("72", "H");
		ScreenShotMainGUI.keyMap.put("73", "I");
		ScreenShotMainGUI.keyMap.put("74", "J");
		ScreenShotMainGUI.keyMap.put("75", "K");
		ScreenShotMainGUI.keyMap.put("76", "L");
		ScreenShotMainGUI.keyMap.put("77", "M");
		ScreenShotMainGUI.keyMap.put("78", "N");
		ScreenShotMainGUI.keyMap.put("79", "O");
		ScreenShotMainGUI.keyMap.put("80", "P");
		ScreenShotMainGUI.keyMap.put("81", "Q");
		ScreenShotMainGUI.keyMap.put("82", "R");
		ScreenShotMainGUI.keyMap.put("83", "S");
		ScreenShotMainGUI.keyMap.put("84", "T");
		ScreenShotMainGUI.keyMap.put("85", "U");
		ScreenShotMainGUI.keyMap.put("86", "V");
		ScreenShotMainGUI.keyMap.put("87", "W");
		ScreenShotMainGUI.keyMap.put("88", "X");
		ScreenShotMainGUI.keyMap.put("89", "Y");
		ScreenShotMainGUI.keyMap.put("90", "Z");

		// 数字
		ScreenShotMainGUI.keyMap.put("48", "0");
		ScreenShotMainGUI.keyMap.put("49", "1");
		ScreenShotMainGUI.keyMap.put("50", "2");
		ScreenShotMainGUI.keyMap.put("51", "3");
		ScreenShotMainGUI.keyMap.put("52", "4");
		ScreenShotMainGUI.keyMap.put("53", "5");
		ScreenShotMainGUI.keyMap.put("54", "6");
		ScreenShotMainGUI.keyMap.put("55", "7");
		ScreenShotMainGUI.keyMap.put("56", "8");
		ScreenShotMainGUI.keyMap.put("57", "9");

		// 小键盘数字
		ScreenShotMainGUI.keyMap.put("96", "0");
		ScreenShotMainGUI.keyMap.put("97", "1");
		ScreenShotMainGUI.keyMap.put("98", "2");
		ScreenShotMainGUI.keyMap.put("99", "3");
		ScreenShotMainGUI.keyMap.put("100", "4");
		ScreenShotMainGUI.keyMap.put("101", "5");
		ScreenShotMainGUI.keyMap.put("102", "6");
		ScreenShotMainGUI.keyMap.put("103", "7");
		ScreenShotMainGUI.keyMap.put("104", "8");
		ScreenShotMainGUI.keyMap.put("105", "9");

		// 控制键
		ScreenShotMainGUI.keyMap2.put("16", "Shift");
		ScreenShotMainGUI.keyMap2.put("17", "Ctrl");
		ScreenShotMainGUI.keyMap2.put("18", "Alt");
	}
}
