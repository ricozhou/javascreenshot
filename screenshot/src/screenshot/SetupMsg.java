package screenshot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SetupMsg {
	// 是否自定义保存
	public boolean isCustomizeSave;
	// 默认保存路径
	public String customSavePath;
	// 画笔颜色
	public Color gColor;
	// 画笔粗细
	public float gSize;
	// 图片格式
	public String imgFormat;
	// 图片清晰度,0是一般，1是高清，2是超清
	public int imgSharpness;

	// 是否开机自启动
	public boolean isSelfStart;

	// 是否启动最小化
	public boolean isStartMinSize;

	// 截图快捷键
	public String sShotHotKey;
	public List<String> sshkList = new ArrayList<String>();
	// 保存快捷键
	public String saveHotKey;
	public List<String> shkList = new ArrayList<String>();

	// 复制快捷键
	public String copyHotKey;
	public List<String> chkList = new ArrayList<String>();

	// 退出快捷键
	public String exitHotKey;
	public List<String> ehkList = new ArrayList<String>();

	// 取消快捷键
	public String cancelHotKey;
	public List<String> cchkList = new ArrayList<String>();

	// 端口
	public int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSelfStart() {
		return isSelfStart;
	}

	public void setSelfStart(boolean isSelfStart) {
		this.isSelfStart = isSelfStart;
	}

	public boolean isStartMinSize() {
		return isStartMinSize;
	}

	public void setStartMinSize(boolean isStartMinSize) {
		this.isStartMinSize = isStartMinSize;
	}

	public boolean isCustomizeSave() {
		return isCustomizeSave;
	}

	public void setCustomizeSave(boolean isCustomizeSave) {
		this.isCustomizeSave = isCustomizeSave;
	}

	public String getCustomSavePath() {
		return customSavePath;
	}

	public void setCustomSavePath(String customSavePath) {
		this.customSavePath = customSavePath;
	}

	public Color getgColor() {
		return gColor;
	}

	public void setgColor(Color gColor) {
		this.gColor = gColor;
	}

	public float getgSize() {
		return gSize;
	}

	public void setgSize(float gSize) {
		this.gSize = gSize;
	}

	public String getImgFormat() {
		return imgFormat;
	}

	public void setImgFormat(String imgFormat) {
		this.imgFormat = imgFormat;
	}

	public int getImgSharpness() {
		return imgSharpness;
	}

	public void setImgSharpness(int imgSharpness) {
		this.imgSharpness = imgSharpness;
	}

	public String getsShotHotKey() {
		return sShotHotKey;
	}

	public void setsShotHotKey(String sShotHotKey) {
		this.sShotHotKey = sShotHotKey;
	}

	public List<String> getSshkList() {
		return sshkList;
	}

	public void setSshkList(List<String> sshkList) {
		this.sshkList = sshkList;
	}

	public String getSaveHotKey() {
		return saveHotKey;
	}

	public void setSaveHotKey(String saveHotKey) {
		this.saveHotKey = saveHotKey;
	}

	public List<String> getShkList() {
		return shkList;
	}

	public void setShkList(List<String> shkList) {
		this.shkList = shkList;
	}

	public String getCopyHotKey() {
		return copyHotKey;
	}

	public void setCopyHotKey(String copyHotKey) {
		this.copyHotKey = copyHotKey;
	}

	public List<String> getChkList() {
		return chkList;
	}

	public void setChkList(List<String> chkList) {
		this.chkList = chkList;
	}

	public String getExitHotKey() {
		return exitHotKey;
	}

	public void setExitHotKey(String exitHotKey) {
		this.exitHotKey = exitHotKey;
	}

	public List<String> getEhkList() {
		return ehkList;
	}

	public void setEhkList(List<String> ehkList) {
		this.ehkList = ehkList;
	}

	public String getCancelHotKey() {
		return cancelHotKey;
	}

	public void setCancelHotKey(String cancelHotKey) {
		this.cancelHotKey = cancelHotKey;
	}

	public List<String> getCchkList() {
		return cchkList;
	}

	public void setCchkList(List<String> cchkList) {
		this.cchkList = cchkList;
	}

	@Override
	public String toString() {
		return "SetupMsg [isCustomizeSave=" + isCustomizeSave + ", customSavePath=" + customSavePath + ", gColor="
				+ gColor + ", gSize=" + gSize + ", imgFormat=" + imgFormat + ", imgSharpness=" + imgSharpness
				+ ", isSelfStart=" + isSelfStart + ", isStartMinSize=" + isStartMinSize + ", sShotHotKey=" + sShotHotKey
				+ ", sshkList=" + sshkList + ", saveHotKey=" + saveHotKey + ", shkList=" + shkList + ", copyHotKey="
				+ copyHotKey + ", chkList=" + chkList + ", exitHotKey=" + exitHotKey + ", ehkList=" + ehkList
				+ ", cancelHotKey=" + cancelHotKey + ", cchkList=" + cchkList + ", port=" + port + "]";
	}

}
