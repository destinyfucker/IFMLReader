import java.util.List;

public class Menu extends ViewContainer {
	public Menu(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
	public Menu(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements, boolean isDefault, boolean isLandMark, boolean isXOR) {
		super(id, name, androidType, parentElement, childElements);
		this.isDefault = isDefault;
		this.isLandMark = isLandMark;
		this.isXOR = isXOR;
	}
	
}
