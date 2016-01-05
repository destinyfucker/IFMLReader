import java.util.ArrayList;
import java.util.HashMap;

public class UmlClass extends UmlElement{
	private HashMap<String, UmlElement> attributesList = null;

	public UmlClass(String id, String name, String packageName) {
		super(id, name, packageName);
		setAttributesList(new HashMap<String, UmlElement>());
		// TODO Auto-generated constructor stub
	}

	public UmlClass(String id, String name, String packageName, HashMap<String, UmlElement> list) {
		super(id, name, packageName);
		if (null == list) {
			list = new HashMap<String, UmlElement>();
		}
		setAttributesList(list);
	}

	public HashMap<String, UmlElement> getAttributesList() {
		return attributesList;
	}

	public void setAttributesList(HashMap<String, UmlElement> attributesList) {
		if (null == attributesList) {
			attributesList = new HashMap<String, UmlElement>();
		}
		this.attributesList = attributesList;
	}
	
	public void addAttribute(String key, UmlElement newElement) {
		this.attributesList.put(key, newElement);
	}
}
