import java.util.ArrayList;
import java.util.List;

public class ViewElementEvent extends ViewElement{
	private ViewElement triggerElement = null;
	private ViewElement targetElement = null;
	private ViewElementEventType eventType= null;
	private ArrayList<ParaPair> paramBindList = null;
	private String targetStr = null;
	private static final String[] eventTypes= {"CLICK", "LONGPRESS", "SWIPE", "TOUCH", "SCROLL"};

	public ViewElementEvent(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements, ViewElement triggerElement, ViewElement targetElement, ViewElementEventType eventType) {
		super(id, name, androidType,parentElement, childElements);
		this.triggerElement = triggerElement;
		this.targetElement = targetElement;
		this.setEventType(eventType);
	}
	
//	public ViewElementEvent(String id, String name, AndroidType androidType, ViewElement parentElement, ViewElement triggerElement, ViewElement targetElement, String eventType) {
//		super(id, name, androidType,parentElement);
//		this.triggerElement = triggerElement;
//		this.targetElement = targetElement;
//		this.setEventTypeByStr(eventType);
//	}
	
	public ViewElementEvent(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements, ViewElement triggerElement, String targetStr, String eventType) {
		super(id, name, androidType,parentElement, childElements);
		this.triggerElement = triggerElement;
		this.targetStr = targetStr;
		this.setEventTypeByStr(eventType);
	}
	
	
	public ViewElementEvent(String id, String name, AndroidType androidType, ViewElement parentElement, List<ViewElement> childElements) {
		super(id, name, androidType, parentElement, childElements);
	}
	public ViewElement getTriggerElement() {
		return triggerElement;
	}
	public void setTriggerElement(ViewElement triggerElement) {
		this.triggerElement = triggerElement;
	}
	public ViewElementEventType getEventType() {
		return eventType;
	}
	public void setEventType(ViewElementEventType eventType) {
		this.eventType = eventType;
	}
	public ViewElement getTargetElement() {
		return targetElement;
	}
	public void setTargetElement(ViewElement targetElement) {
		this.targetElement = targetElement;
	}
	
	public void setEventTypeByStr(String eventName) {
		for (int i = 0; i < eventTypes.length; i++) {
			if (eventName.equalsIgnoreCase(eventTypes[i]) || eventName.contains(eventTypes[i])) {
				this.eventType = ViewElementEventType.values()[i];
			}
		}
	}

	public String getTargetStr() {
		return targetStr;
	}

	public void setTargetStr(String targetStr) {
		this.targetStr = targetStr;
	}
	
	public ViewContainer getTriggerViewContainer() {
		ViewContainer triggerContainer = null;
		ViewElement temp = triggerElement;
		while(temp != null) {
			if ((temp.getClass() == ViewContainer.class) || (temp.getClass() == Menu.class)) {
				triggerContainer = (ViewContainer)temp;
			}
			temp = temp.getParentElement();
		}
		return triggerContainer;
	}
	
	public ViewContainer getTargetViewContainer() {
		ViewContainer taretContainer = null;
		ViewElement temp = targetElement;
		while(temp != null) {
			if ((temp.getClass() == ViewContainer.class) || (temp.getClass() == Menu.class)) {
				taretContainer = (ViewContainer)temp;
			}
			temp = temp.getParentElement();
		}
		return taretContainer;
	}

	public ArrayList<ParaPair> getParamBindList() {
		return paramBindList;
	}

	public void setParamBindList(ArrayList<ParaPair> paramBindList) {
		this.paramBindList = paramBindList;
	}
}
