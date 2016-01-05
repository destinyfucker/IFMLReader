import java.util.List;

public class Parameter extends ViewElement{
	private UmlElement type = null;
	public Parameter(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
	public Parameter(String id, String name, ViewElement parentElement, UmlElement type) {
		super(id, name, null, parentElement, null);
		this.type = type;
	}
	public UmlElement getType() {
		return type;
	}
	public void setType(UmlElement type) {
		this.type = type;
	}
}
