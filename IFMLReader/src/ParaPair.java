
public class ParaPair {
	private UmlElement fromParaType = null;
	private String fromParaName = null;
	private UmlElement toParaType = null;
	private String toParaName = null;
	public ParaPair(String fromParaName, UmlElement fromParaType, String toParaName, UmlElement toParaType) {
		this.fromParaName = fromParaName;
		this.fromParaType = fromParaType;
		this.toParaName = toParaName;
		this.toParaType = toParaType;
	}
	public UmlElement getFromParaType() {
		return fromParaType;
	}
	public void setFromParaType(UmlElement fromParaType) {
		this.fromParaType = fromParaType;
	}
	public String getFromParaName() {
		return fromParaName;
	}
	public void setFromParaName(String fromParaName) {
		this.fromParaName = fromParaName;
	}
	public UmlElement getToParaType() {
		return toParaType;
	}
	public void setToParaType(UmlElement toParaType) {
		this.toParaType = toParaType;
	}
	public String getToParaName() {
		return toParaName;
	}
	public void setToParaName(String toParaName) {
		this.toParaName = toParaName;
	}

}
