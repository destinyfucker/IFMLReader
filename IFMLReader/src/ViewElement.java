import java.util.ArrayList;
import java.util.List;

public class ViewElement {
	protected String id = null;
	protected String name = null;
	protected AndroidType androidType = null;
	protected ViewElement parentElement = null;
	protected List<ViewElement> childElements = null;
	
	public ViewElement(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		this.id = id;
		this.name = name;
		this.androidType = androidType;
		this.parentElement = parentElement;
		if (null == childElements) {
			childElements = new ArrayList<ViewElement>();
		}
		this.childElements = childElements;
	}
	
	public ViewElement(){};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AndroidType getAndroidType() {
		return androidType;
	}

	public void setAndroidType(AndroidType androidType) {
		this.androidType = androidType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ViewElement getParentElement() {
		return parentElement;
	}

	public void setParentElement(ViewElement parentElement) {
		this.parentElement = parentElement;
	}
	
	public List<ViewElement> getChildElements() {
		return childElements;
	}
	
	public void setChildElelements(List<ViewElement> childElements) {
		this.childElements = childElements;
	}
	
	public ViewContainer getDirectParentContainer() {
		ViewElement tempParent = parentElement;
		while (null != tempParent) {
			if (ViewContainer.class.isAssignableFrom(tempParent.getClass())) {
				
				return (ViewContainer) tempParent;
			}
			tempParent = tempParent.getParentElement();
		}
		return null;
	}
	//需添加对id的正则表达式的解析
}
