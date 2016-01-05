import java.util.ArrayList;
import java.util.List;

public class NavigationFlow extends ViewElement{
	private ViewElement triggerElement = null;
	private ViewElement targetElement = null;
	private String targetStr = null;
	private ArrayList<ParaPair> paramBindList = null;
	private ArrayList<String[]> paramStrList = null;
//	public NavigationFlow(String id, String name, AndroidType androidType, ViewElement parentElement, ViewElement triggerElement, ViewElement targetElement) {
//		super(id, name, androidType, parentElement);
//		this.triggerElement = triggerElement;
//		this.targetElement = targetElement;
//	}
	public NavigationFlow(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements, ViewElement triggerElement, String targetStr) {
		super(id, name, androidType, parentElement, childElements);
		this.triggerElement = triggerElement;
		this.targetStr = targetStr;
	}
	public NavigationFlow(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
	public ViewElement getTriggerElement() {
		return triggerElement;
	}
	public void setTriggerElement(ViewElement triggerElement) {
		this.triggerElement = triggerElement;
	}
	public ViewElement getTargetElement() {
		return targetElement;
	}
	public void setTargetElement(ViewElement targetElement) {
		this.targetElement = targetElement;
	}
	public String getTargetStr() {
		return targetStr;
	}
	public void setTargetStr(String targetStr) {
		this.targetStr = targetStr;
	}
	public ArrayList<ParaPair> getParamBindList() {
		return paramBindList;
	}
	public void setParamBindList(ArrayList<ParaPair> paramBindList) {
		this.paramBindList = paramBindList;
	}
	public ArrayList<String[]> getParamStrList() {
		return paramStrList;
	}
	public void setParamStrList(ArrayList<String[]> paramStrList) {
		this.paramStrList = paramStrList;
	}
	
}
