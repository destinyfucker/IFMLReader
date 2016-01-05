
public abstract class UmlElement {
	protected String id = null;
	protected String name = null;
	protected String packageName = null;
	public UmlElement(String id, String name, String packageName) {
		this.id = id;
		this.name = name;
		this.packageName = packageName;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
