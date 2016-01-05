import java.util.ArrayList;

public class CaseGenerator {

	private ArrayList<ViewElement> elementList = null;
	private ArrayList<ViewElementEvent> eventList = null;
	//生成的测试用例中的循环数
	private static final int CIRCLETIME = 0;
	//private ArrayList<ViewContainer> containerList = null;
	public CaseGenerator(ArrayList<ViewElement> elementList, ArrayList<ViewElementEvent> eventList) {
		this.elementList = elementList;
		this.eventList = eventList;
	}
	
	public ArrayList<ViewElement> getElementList() {
		return elementList;
	}
	public void setElementList(ArrayList<ViewElement> elementList) {
		this.elementList = elementList;
	}
	public ArrayList<ViewElementEvent> getEventList() {
		return eventList;
	}
	public void setEventList(ArrayList<ViewElementEvent> eventList) {
		this.eventList = eventList;
	}
	
	public ArrayList<ArrayList<ViewElementEvent>> getCases() {
		//containerList = new ArrayList<ViewContainer>();
		 ArrayList<ArrayList<ViewElementEvent>> caseList = new  ArrayList<ArrayList<ViewElementEvent>>();
//		for (ViewElement element: elementList) {
//			if ((element.getClass() == ViewContainer.class) || (element.getClass() == Menu.class)) {
//				containerList.add((ViewContainer)element);
//			}
//		}
		for (ViewElementEvent event: eventList) {
			if (event.getTriggerViewContainer().isDefault || event.getTriggerViewContainer().isXOR) {
				ArrayList<ViewElementEvent> cases = new ArrayList<ViewElementEvent>();
				addEvent(caseList, cases, event);
			}
		}
		
		return caseList;
		
	}
	
	//递归用，用于获得下一个event
	private void addEvent(ArrayList<ArrayList<ViewElementEvent>> caseList, ArrayList<ViewElementEvent> cases, ViewElementEvent event) {
		cases.add(event);
		ViewContainer targetContainer= event.getTargetViewContainer();
		boolean hasNext = false;
		for (ViewElementEvent e :eventList) {
			if ((e.getTriggerViewContainer() == targetContainer) && (!hasReachedTheCricleTime(cases, e))) {
				ArrayList<ViewElementEvent> newCase = (ArrayList<ViewElementEvent>) cases.clone();
				addEvent(caseList, newCase, e);
				hasNext = true;
			}
		}
		if (!hasNext) {
			caseList.add(cases);
		}
	}
	
	//
	private boolean hasReachedTheCricleTime(ArrayList<ViewElementEvent> cases, ViewElementEvent event) {
		int time = CIRCLETIME;
		for (ViewElementEvent tempEvent: cases) {
			if (tempEvent == event) {
				time--;
			}
		}
		if (time < 0) {
			return true;
		} else {
			return false;
		}
	}
}
