package screenshot;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JTextField;

import net.jimmc.jshortcut.JShellLink;

public class SetupUtils {
	// 键盘按压事件监听
	public void keyPressedUtils(int keyPressedCode, JTextField tt) {
		tt.setText("");
		// 首先判断集合中是否有了这个按键,并且按键只能属于哪些范围
		if (!includeKey(keyPressedCode) && checkKey(keyPressedCode, 0)) {
			// 不包含并且属于规定范围的话加入list
			SetupJF.keyList.add(String.valueOf(keyPressedCode));
		}
		tt.setText("");
	}

	// 判断是否含有
	protected boolean includeKey(int keyPressedCode) {
		for (String keyP : SetupJF.keyList) {
			if (String.valueOf(keyPressedCode).equals(keyP)) {
				return true;
			}
		}
		return false;
	}

	// list是否包含这些按键
	private boolean includeKey(Set<String> keyList2, int k) {
		for (String key : keyList2) {
			if (String.valueOf(k).equals(key)) {
				return true;
			}
		}
		return false;
	}

	// 按键范围
	protected boolean checkKey(int keyPressedCode, int flag) {
		// 字母
		if (keyPressedCode >= 65 && keyPressedCode <= 90) {
			return true;
		}
		// 数字
		if (keyPressedCode >= 48 && keyPressedCode <= 57) {
			return true;
		}
		// 小键盘数字
		if (keyPressedCode >= 96 && keyPressedCode <= 105) {
			return true;
		}
		if (flag == 0) {
			// 控制键：shift，ctrl，alt
			if (keyPressedCode == 16 || keyPressedCode == 17 || keyPressedCode == 18) {
				return true;
			}
		}
		return false;
	}

	// 判断是否包含数字或者字母
	protected boolean onlyInSCA(Set<String> keyList2) {
		for (String key : keyList2) {
			if (checkKey(Integer.valueOf(key), 1)) {
				// 只要包含就返回
				return false;
			}
		}
		return true;
	}

	// 必须包含且只包含一个字母键或者数字键
	public boolean onlyOneNumOrAlp(Set<String> keyList2) {
		int num = 0;
		for (String key : keyList2) {
			if (checkKey(Integer.valueOf(key), 1)) {
				// 只要包含就加1
				num++;
			}
		}

		if (num > 1) {
			return false;
		}
		return true;
	}

	// 根据list识别key显示
	protected String getShowKey(Set<String> keyList2) {
		// 根据集合大小创建数组
		int size = keyList2.size();
		String[] showArr = new String[size];
		StringBuilder showKey = new StringBuilder();
		// 判断是否包含Shift
		String firstName = "";
		if (includeKey(keyList2, 16)) {
			firstName = "Shift";
			showArr[0] = firstName;
		}
		// 是否包含Ctrl
		String secondName = "";
		if (includeKey(keyList2, 17)) {
			secondName = "Ctrl";
			// 判断数组从第几个下标出现null值
			int index = getArrNullIndex(showArr);
			if (index != -1) {
				showArr[index] = secondName;
			}

		}

		// 是否包含Alt
		String thirdName = "";
		if (includeKey(keyList2, 18)) {
			thirdName = "Alt";
			// 判断数组从第几个下标出现null值
			int index = getArrNullIndex(showArr);
			if (index != -1) {
				showArr[index] = thirdName;
			}
		}

		// 获取其他键
		for (String key : keyList2) {
			// 判断每个key是什么并返回String
			String s = getKey(key);
			int index = getArrNullIndex(showArr);
			if (index != -1) {
				showArr[index] = s;
			}
		}
		// 拼接显示字符串
		for (int i = 0; i < showArr.length; i++) {
			showKey = showKey.append(showArr[i]);
			if (i < showArr.length - 1) {
				showKey.append(" + ");
			}
		}
		return showKey.toString();
	}

	// 根据数字返回真正的按键显示
	private String getKey(String key) {
		String s = ScreenShotMainGUI.keyMap.get(key);
		return s;
	}

	// 判断什么时候出现null
	private int getArrNullIndex(String[] showArr) {
		for (int i = 0; i < showArr.length; i++) {
			if (showArr[i] == null) {
				// 返回第一个null的下标
				return i;
			}
		}
		// 没有null返回-1
		return -1;
	}

	// 删除按键从list中
	protected void deleteKey(int keyReleased) {
		Iterator<String> it = SetupJF.keyList.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (String.valueOf(keyReleased).equals(key)) {
				it.remove();
				return;
			}
		}
	}

	// 写入快捷方式
	public boolean setAutoStart(boolean yesAutoStart, String lnk) {
		File f = new File(lnk);
		String p = f.getAbsolutePath();
		String startFolder = "";
		String osName = System.getProperty("os.name");
		String str = System.getProperty("user.home");
		if (osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 10")
				|| osName.equals("Windows Server 2012 R2") || osName.equals("Windows Server 2014 R2")
				|| osName.equals("Windows Server 2016")) {
			startFolder = System.getProperty("user.home")
					+ "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
		}
		if (osName.endsWith("Windows XP")) {
			startFolder = System.getProperty("user.home") + "\\「开始」菜单\\程序\\启动";
		}
		if (setRunBySys(yesAutoStart, p, startFolder, lnk)) {
			return true;
		}
		return false;
	}

	// 设置是否随系统启动
	public boolean setRunBySys(boolean b, String path, String path2, String lnk) {
		File file = new File(path2 + "\\" + lnk);
		Runtime run = Runtime.getRuntime();
		File f = new File(lnk);

		// 复制
		try {
			if (b) {
				// 写入
				// 判断是否隐藏，注意用系统copy布置为何隐藏文件不生效
				if (f.isHidden()) {
					// 取消隐藏
					try {
						Runtime.getRuntime().exec("attrib -H \"" + path + "\"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!file.exists()) {
					run.exec("cmd /c copy " + formatPath(path) + " " + formatPath(path2));
				}
				// 延迟0.5秒防止复制需要时间
				Thread.sleep(500);
			} else {
				// 删除
				if (file.exists()) {
					if (file.isHidden()) {
						// 取消隐藏
						try {
							Runtime.getRuntime().exec("attrib -H \"" + file.getAbsolutePath() + "\"");
						} catch (IOException e) {
							e.printStackTrace();
						}
						Thread.sleep(500);
					}
					run.exec("cmd /c del " + formatPath(file.getAbsolutePath()));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 解决路径中空格问题
	private String formatPath(String path) {
		if (path == null) {
			return "";
		}
		return path.replaceAll(" ", "\" \"");
	}

	// 向同级目录创建快捷方式（因为次方法直接向开机启动项创建有可能失败）
	public boolean createLnk(String appName) {
		try {
			File f = new File("");
			// 存放在exe目录
			String savePath = f.getAbsolutePath();
			JShellLink link = new JShellLink();
			// 存放路径
			link.setFolder(savePath);
			// 快捷方式名称
			link.setName(appName);
			// 指向的exe
			link.setPath(f + f.separator + appName);
			link.save();
			return true;
		} catch (Throwable e) {
			// 是更改后的jar应用，直接全部抛出
			e.printStackTrace();
		}
		return false;
	}

}
