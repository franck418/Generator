package generator;

/**
 * 表字段类
 * @author Administrator
 *
 */
public class ColumnData {

	private String columnName;
	private String dataType;
	private String columnComment;
	private String colField;
	private String colFiled4Method;
	private String columnKey;

	public String getColFiled4Method() {
		return colFiled4Method;
	}
	public void setColFiled4Method(String colFiled4Method) {
		this.colFiled4Method = colFiled4Method;
	}
	public String getColField() {
		return colField;
	}
	public void setColField(String colField) {
		this.colField = colField;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	
	public String getDataTypeX() {
		if(this.dataType != null) {
			return dataType.substring(dataType.lastIndexOf(".") + 1);
		}
		
		return null;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getColumnComment4V() {
		if(this.columnComment != null) {
			return columnComment.replaceAll("\r\n", "<br/>").replaceAll(" ", "");
		}
		return null;
	}
	
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	} 
	
	public String getColumnKey() {
		return columnKey;
	}
	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
}
