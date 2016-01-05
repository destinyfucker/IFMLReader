import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class IFMLReader {
	private static ArrayList<UmlElement> umlElementList = new ArrayList<UmlElement>();
	private static ArrayList<ViewElement> elementList = new ArrayList<ViewElement>();
	private static ArrayList<ViewElementEvent> eventList = new ArrayList<ViewElementEvent>();
	private final static String[] nameList = {"interactionFlowModelElements", "viewElements", "viewElementEvents", "outInteractionFlows", "interactionFlowModelElements", "parameters"};
	private final static String[] typeList = {"core:ViewContainer", "core:ViewComponent", "", "core:NavigationFlow", "ext:IFMLMenu", ""};
	private final static Class[] elementType = {ViewContainer.class, ViewComponent.class, ViewElementEvent.class, NavigationFlow.class, Menu.class, Parameter.class};
	public static Element readIFMLFile(String fileName) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(fileName));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		return root;
	}
	//从uml文件中获取Umlelement类型（暂时只有class与primitivetype）
	private static void getAllUmlElement(Element rootElement) {
		getUmlElement(null, rootElement);
	}
	
	//递归用
	private static void getUmlElement(String packageName, Element umlElement) {
		List<Element> list = umlElement.elements();
		if (list.isEmpty()) {
			return;
		}
		for (Element e: list) {
			String type = e.attributeValue("type");
			String name = e.attributeValue("name");
			String id = e.attributeValue("id");
			String newPackageName = (null == packageName)?"":packageName;
			UmlElement newElement = null;
			if (type != null && e.getName().equals("packagedElement")) {
				if (type.equals("uml:PrimitiveType")) {
					newElement = new PrimitiveType(id, name, packageName);
					umlElementList.add(newElement);
				} else if (type.equals("uml:Class")) {
					newElement = new UmlClass(id, name, packageName);
					umlElementList.add(newElement);
				} else if (type.equals("uml:Package")) {
					newPackageName += (newPackageName.equals("")) ? name: "." + name;
				}
			} else if (e.getName().equals("ownedAttribute")) {
				if (umlElement.attributeValue("type").equals("uml:Class")) {
					UmlElement parentClass = getUmlElementById(umlElement.attributeValue("id"));
					if (parentClass.getClass() == UmlClass.class) {
						((UmlClass)parentClass).addAttribute(name, getUmlElementById(type));
					}
				}
			}
			getUmlElement(newPackageName, e);
		}
	}
	
	//遍历所有节点,获取ViewElement
	private static void getAllViewElement(Element firstOne){
		getViewElement(null, firstOne);
		
		//下面主要对list进行检查：删除其中无用的navigationflow，为event绑定数据源，并生成最终的eventList
		
		//绑定所有element的child
		for (ViewElement element : elementList) {
			for (ViewElement otherElement : elementList) {
				if (otherElement.getParentElement() == element) {
					if (!element.getChildElements().contains(otherElement)) {
						element.getChildElements().add(otherElement);
					}
				}
			}
		}
		
		//这里将绑定参数
		for (ViewElement element : elementList) {
			if (element.getClass() == NavigationFlow.class) {
				NavigationFlow flow = (NavigationFlow)element;
				ArrayList<ParaPair> paraList = (null == flow.getParamBindList()) ? new ArrayList<ParaPair>() : flow.getParamBindList();
				for (String[] paramStr : flow.getParamStrList()) {
					Parameter sourceParam = (Parameter)getTargetElementByStr(paramStr[0]);
					Parameter targetParam = (Parameter)getTargetElementByStr(paramStr[1]);
					if ((null != sourceParam) && (null != targetParam)) {
						ParaPair newPair = new ParaPair(sourceParam.getName(), sourceParam.getType(), targetParam.getName(), targetParam.getType());
						paraList.add(newPair);
					}
				}
				flow.setParamBindList(paraList);
			}
		}
		
		//这里将navigationFlow的指向对象确定好
		for (int i = 0;  i < elementList.size(); i++) {
			if (elementList.get(i).getClass() == NavigationFlow.class) {
				NavigationFlow element = (NavigationFlow)elementList.get(i);
				if (element.getTargetStr() != null && element.getTargetElement() == null) {
					String targetStr = element.getTargetStr();
					ViewElement targetElement = getTargetElementByStr(targetStr);
					element.setTargetElement(targetElement);
					if (element.getParentElement().getClass() == ViewElementEvent.class) {
						ViewElementEvent parentElement = (ViewElementEvent)element.getParentElement();
						parentElement.setTargetElement(targetElement);
						parentElement.setParamBindList(element.getParamBindList());
					}
				}
			}
		}
		
		//这里若event组件中的viewElement有主动填写，则targetElement以该填写内容为主
		for (ViewElement element : elementList) {
			if (element.getClass() == ViewElementEvent.class) {
				ViewElementEvent event = (ViewElementEvent) element;
				if (event.getTargetStr() != null) {
					event.setTargetElement(getTargetElementByStr(event.getTargetStr()));
				}
				eventList.add(event);
			}
		}

	}
	
	//递归遍历算法
	private static void getViewElement(ViewElement parent, Element originOne) {
		List<Element> list = originOne.elements();
		if(null == list || list.size() == 0) {
			return;
		} else {
			for(int i = 0; i < list.size(); i++) {
				Element e = list.get(i);
				String type = e.attributeValue("type");
				if (null == type) {
					type = "";
				}
				String name = e.getName();
				String specialName = e.attributeValue("name");
				String id = e.attributeValue("id");
				//这里耦合比较高，可以考虑换一种方法
				//Start
				ViewElement viewElement = null;
				if (nameList[0].equals(name) && typeList[0].equals(type)) {
					boolean isDefault = ("true").equalsIgnoreCase(e.attributeValue("isDefault")) ? true:false;
					boolean isLandMark = ("true").equalsIgnoreCase(e.attributeValue("isLandMark")) ? true:false;
					boolean isXOR = ("true").equalsIgnoreCase(e.attributeValue("isXOR")) ? true:false;
					viewElement = new ViewContainer(id, specialName, null, parent, null, isDefault, isLandMark, isXOR);
				} else if (nameList[1].equals(name) && typeList[1].equals(type)) {
					viewElement = new ViewComponent(id, specialName, null, parent, null);
				} else if (nameList[2].equals(name) && typeList[2].equals(type)) {
					viewElement = new ViewElementEvent(id, specialName, null, parent, null, parent, null, specialName);
				} else if (nameList[3].equals(name) && typeList[3].equals(type)) {
					//TODO 将数据存入其中
					String targetStr = e.attributeValue("targetInteractionFlowElement");
					if (null == targetStr || targetStr.equals("")) {
						continue;
					}

					ArrayList<String[]> paramStrList = new ArrayList<String[]>();
					Element params  = e.element("parameterBindingGroup");
					if (null != params) {
						List<Element> paraList = params.elements("parameterBindings");
						for (Element para: paraList) {
							String[] tempList = new String[2];
							tempList[0] = para.attributeValue("sourceParameter");
							tempList[1] = para.attributeValue("targetParameter");
							paramStrList.add(tempList);
						}
					}
					//由于其下除了绑定参数无其他子节点，故在此不进行递归
					viewElement = new NavigationFlow(id, specialName, null, parent, null, parent, targetStr);
					((NavigationFlow)viewElement).setParamStrList(paramStrList);
					elementList.add(viewElement);
					continue;
				} else if (nameList[4].equals(name) && typeList[4].equals(type)) {
					boolean isDefault = ("true").equalsIgnoreCase(e.attributeValue("isDefault")) ? true:false;
					boolean isLandMark = ("true").equalsIgnoreCase(e.attributeValue("isLandMark")) ? true:false;
					boolean isXOR = ("true").equalsIgnoreCase(e.attributeValue("isXOR")) ? true:false;
					viewElement = new Menu(id, specialName, null, parent, null, isDefault, isLandMark, isXOR);
				} else if (nameList[5].equals(name) && typeList[5].equals(type)) {

					//这里直接跳过递归，直接将type导入进去，并不递归其下的元素
					Element temp = e.element("type");
					String umlId = temp.attributeValue("href");
					UmlElement umlType = null;
					if (umlId.contains(".uml#")) {
						umlId = umlId.split(".uml#")[1];
						umlType = getUmlElementById(umlId);
					}

					viewElement = new Parameter(id, specialName, parent, umlType);
					elementList.add(viewElement);
					continue;
				}
				//End
				elementList.add(viewElement);
				getViewElement(viewElement, e);
			}
		}
	}
	
	private static UmlElement getUmlElementById(String id) {
		for (UmlElement e : umlElementList) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}
	
	private static ViewElement getTargetElementByStr(String targetStr) {
		if (!targetStr.startsWith("//@interactionFlowModel/@")) {
			return null;
		}
		targetStr = targetStr.substring(25);
		String[] parents = targetStr.split("/@");
		ViewElement parent = null;
		ViewElement nowOne = null;
		for(String elementStr: parents) {
			String[] temp = elementStr.split("\\.");
			String typeName = temp[0];
			Class classType = null;
			for (int j = 0; j < nameList.length; j++) {
				if (nameList[j].equals(typeName)) {
					classType = elementType[j];
				}
			}
 
			int index = Integer.parseInt(temp[1]);
			for (ViewElement element :elementList) {
				if ((element.getClass().isAssignableFrom(classType)) && (element.getParentElement() == parent)) {
					index--;
					if (index < 0) {
						parent = element;
						nowOne = element;
						break;
					}
				}
			}
		}
		return nowOne;
	}
	
	private static UmlElement getUmlTypeByStr(String typeStr) {
		if (!typeStr.contains(".uml#")) {
			return null;
		}
		String umlId = typeStr.split(".uml#")[1];
		UmlElement type = getUmlElementById(umlId);
		return type;
	}
	
	public static void main(String[] arg) {
		Element umlRoot = IFMLReader.readIFMLFile("Sudoku.uml");
		getAllUmlElement(umlRoot);
		ArrayList<UmlElement> list = umlElementList;
		Element coreRoot = IFMLReader.readIFMLFile("Sudoku.core");
		Element modelElement = coreRoot.element("interactionFlowModel");
		getAllViewElement(modelElement);
		CaseGenerator generator = new CaseGenerator(elementList, eventList);
		ArrayList<ArrayList<ViewElementEvent>> caseList = generator.getCases();
		ArrayList<ViewElement> temp = elementList;
		ArrayList<ViewElementEvent> temp2 = eventList;
		System.out.println("ok");
		AndroidIdSetter test = new AndroidIdSetter(elementList);
		test.initSetter();
	}
}
