package screenshot;

import java.awt.Color;

public class SetupParams {
	// 是否自定义保存
	public boolean isCustomizeSave;
	// 默认保存路径
	public String customSavePath;
	// 画笔颜色
	public int gColorP;
	// 画笔粗细
	public int gSizeP;
	// 图片格式
	public int imgFormatP;
	// 图片清晰度,0是一般，1是高清，2是超清
	public int imgSharpness;

	// 是否开机自启动
	public boolean isSelfStart;

	// 是否启动最小化
	public boolean isStartMinSize;

	// 截图快捷键
	public String sShotHotKey;
	// 保存快捷键
	public String saveHotKey;
	// 复制快捷键
	public String copyHotKey;
	// 退出快捷键
	public String exitHotKey;
	// 取消快捷键
	public String cancelHotKey;

	// 占用端口备用设置
	public String port;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
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

	public int getgColorP() {
		return gColorP;
	}

	public void setgColorP(int gColorP) {
		this.gColorP = gColorP;
	}

	public int getgSizeP() {
		return gSizeP;
	}

	public void setgSizeP(int gSizeP) {
		this.gSizeP = gSizeP;
	}

	public int getImgFormatP() {
		return imgFormatP;
	}

	public void setImgFormatP(int imgFormatP) {
		this.imgFormatP = imgFormatP;
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

	public String getSaveHotKey() {
		return saveHotKey;
	}

	public void setSaveHotKey(String saveHotKey) {
		this.saveHotKey = saveHotKey;
	}

	public String getCopyHotKey() {
		return copyHotKey;
	}

	public void setCopyHotKey(String copyHotKey) {
		this.copyHotKey = copyHotKey;
	}

	public String getExitHotKey() {
		return exitHotKey;
	}

	public void setExitHotKey(String exitHotKey) {
		this.exitHotKey = exitHotKey;
	}

	public String getCancelHotKey() {
		return cancelHotKey;
	}

	public void setCancelHotKey(String cancelHotKey) {
		this.cancelHotKey = cancelHotKey;
	}

	@Override
	public String toString() {
		return "SetupParams [isCustomizeSave=" + isCustomizeSave + ", customSavePath=" + customSavePath + ", gColorP="
				+ gColorP + ", gSizeP=" + gSizeP + ", imgFormatP=" + imgFormatP + ", imgSharpness=" + imgSharpness
				+ ", isSelfStart=" + isSelfStart + ", isStartMinSize=" + isStartMinSize + ", sShotHotKey=" + sShotHotKey
				+ ", saveHotKey=" + saveHotKey + ", copyHotKey=" + copyHotKey + ", exitHotKey=" + exitHotKey
				+ ", cancelHotKey=" + cancelHotKey + ", port=" + port + "]";
	}

}
