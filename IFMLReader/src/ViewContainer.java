import java.util.List;

public class ViewContainer extends ViewElement{
	protected boolean isDefault = false;
	protected boolean isLandMark = false;
	protected boolean isXOR = false;
	public ViewContainer(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
	public ViewContainer(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements, boolean isDefault, boolean isLandMark, boolean isXOR) {
		super(id, name, androidType, parentElement, childElements);
		this.isDefault = isDefault;
		this.isLandMark = isLandMark;
		this.isXOR = isXOR;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isLandMark() {
		return isLandMark;
	}
	public void setLankMark(boolean isLandMark) {
		this.isLandMark = isLandMark;
	}
	public boolean isXOR() {
		return isXOR;
	}
	public void setXOR(boolean isXOR) {
		this.isXOR = isXOR;
	}
}
