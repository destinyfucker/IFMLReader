import java.util.List;

public class ViewComponent extends ViewElement{
	public ViewComponent(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
}
