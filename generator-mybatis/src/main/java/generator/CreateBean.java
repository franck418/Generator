package generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 
 * <br>
 * <b>功能：</b>详细的功能描述<br>
 * <b>作者：</b>肖财高<br>
 * <b>日期：</b> 2011-12-16 <br>
 * <b>版权所有：<b>版权所有(C) 2011，QQ 405645010<br>
 */
public class CreateBean {

	private Connection connection;
	static String url;
	static String username;
	static String password;
	static String rt = "\r\t";
	String SQLTables = "SELECT table_name, table_comment FROM information_schema.tables WHERE table_schema = ";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void setMysqlInfo(String url, String username, String password, String shcema) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.SQLTables += " '" + shcema + "' ";
		
		System.out.println(SQLTables);
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() throws SQLException {
		this.connection = DriverManager.getConnection(url, username, password);
		return this.connection;
	}

	public List<TableData> getTables() throws SQLException {
		Connection con = this.getConnection();
		PreparedStatement ps = con.prepareStatement(SQLTables);
		ResultSet rs = ps.executeQuery();
		List<TableData> list = new ArrayList<TableData>();
		while (rs.next()) {
			TableData table = new TableData();
			String tableName = rs.getString(1);
			String comment = rs.getString(2);
			table.setTableName(tableName);
			table.setComment(comment);
			list.add(table);
		}
		rs.close();
		ps.close();
		con.close();
		return list;
	}

	/**
	 * 查询表的字段，封装成List
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<ColumnData> getColumnDatas(String tableName, String tableSchema) throws SQLException {
		String SQLColumns = "SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,COLUMN_KEY FROM information_schema.columns WHERE table_name =  '"
				+ tableName + "' " + " and table_schema = '" + tableSchema + "'";
		Connection con = this.getConnection();
		PreparedStatement ps = con.prepareStatement(SQLColumns);
		List<ColumnData> columnList = new ArrayList<ColumnData>();
		ResultSet rs = ps.executeQuery();
		StringBuffer str = new StringBuffer();
		StringBuffer getset = new StringBuffer();
		while (rs.next()) {
			String name = rs.getString(1);
			String type = rs.getString(2);
			String comment = rs.getString(3);
			String key = rs.getString(4);

			type = this.getType(type);

			ColumnData cd = new ColumnData();
			cd.setColumnName(name);
			cd.setDataType(type);
			cd.setColumnComment(comment);
			cd.setColField(getColomnField(name));
			cd.setColFiled4Method(getColomnField4Method(name));
			cd.setColumnKey(key);
			columnList.add(cd);
		}
		argv = str.toString();
		method = getset.toString();
		rs.close();
		ps.close();
		con.close();
		return columnList;
	}

	private String method;
	private String argv;

	/**
	 * 生成实体Bean 的属性和set,get方法
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public String getBeanFeilds(String tableName, String schema) throws SQLException {
		List<ColumnData> dataList = getColumnDatas(tableName, schema);
		StringBuffer str = new StringBuffer();
		StringBuffer getset = new StringBuffer();
		for (ColumnData d : dataList) {
			String name = d.getColumnName();
			name = getColomnField(name);
			String type = d.getDataType();
			String comment = d.getColumnComment();
			// type=this.getType(type);
			String maxChar = name.substring(0, 1).toUpperCase();
			str.append("\r\t").append("private ").append(type + " ").append(name).append(";//   ").append(comment);
			String method = maxChar + name.substring(1, name.length());
			getset.append("\r\t").append("public ").append(type + " ").append("get" + method + "() {\r\t");
			getset.append("    return this.").append(name).append(";\r\t}");
			getset.append("\r\t").append("public void ").append("set" + method + "(" + type + " " + name + ") {\r\t");
			getset.append("    this." + name + "=").append(name).append(";\r\t}");
		}
		argv = str.toString();
		method = getset.toString();
		return argv + method;
	}

	/**
	 * D.K.R
	 */
	public String getColomnField(String name) {
		name = name.toLowerCase();
		if (name.indexOf("_") > 0) {// 如果有多个单词_组成的字段
			String[] subNames = name.split("_");
			for (int i = 1; i < subNames.length; i++) {
				subNames[i] = subNames[i].substring(0, 1).toUpperCase() + subNames[i].substring(1);
				if (i == 1) {
					name = subNames[0] + subNames[i];
				} else {
					name += subNames[i];
				}
			}
		}
		return name;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getColomnField4Method(String name) {
		name = name.toLowerCase();
		String result = "";

		String[] subNames = name.split("_");
		for (int i = 0; i < subNames.length; i++) {
			subNames[i] = subNames[i].substring(0, 1).toUpperCase() + subNames[i].substring(1);
			result += subNames[i];
		}

		return result;
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>详细的功能描述<br>
	 * <b>作者：</b>肖财高<br>
	 * <b>日期：</b> 2011-12-20 <br>
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	// public List<Map<String,String>> getColumnsMap(String tableName) throws
	// SQLException{
	// String SQLColumns="SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT
	// FROM information_schema.columns WHERE table_schema = 'ssi' and table_name
	// = '"+tableName+"' ";
	//// String SQLColumns="desc "+tableName;
	// Connection con=this.getConnection();
	// PreparedStatement ps=con.prepareStatement(SQLColumns);
	// List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
	// ResultSet rs=ps.executeQuery();
	// while(rs.next()){
	// Map<String,String> columnsMap=new HashMap<String, String>();
	//// String name = rs.getString(1);
	//// String type = rs.getString(2);
	//// String comment = rs.getString(3);
	//
	//
	// String name = rs.getString(1);
	// String type = rs.getString(2);
	// String comment = rs.getString(3);
	// type=this.getType(type);
	// columnsMap.put("COLUMN_NAME", name);
	// columnsMap.put("DATA_TYPE", type);
	// columnsMap.put("COLUMN_COMMENT", comment);
	// listMap.add(columnsMap);
	// }
	// rs.close();
	// ps.close();
	// con.close();
	// return listMap;
	// }
	public String getType(String type) {
		type = type.toLowerCase();
		if ("char".equals(type) || "varchar".equals(type) || "varbinary".equals(type)) {
			return "String";
		} else if ("int".equals(type)) {
			return "Integer";
		} else if ("bigint".equals(type)) {
			return "Long";
		} else if ("timestamp".equals(type) || "date".equals(type) || "datetime".equals(type) || "time".equals(type)) {
			return "java.util.Date";
		} else if ("tinyint".equals(type)) {
			return "Integer";
		} else if ("decimal".equals(type)) {
			return "BigDecimal";
		} else if ("double".equals(type)) {
			return "Double";
		} else if ("text".equals(type)) {
			return "String";
		}
		return "Integer";
	}

	public void getPackage(int type, String createPath, String content, String packageName, String className,
			String extendsClassName, String... importName) throws Exception {
		if (null == packageName) {
			packageName = "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packageName).append(";\r");
		sb.append("\r");
		for (int i = 0; i < importName.length; i++) {
			sb.append("import ").append(importName[i]).append(";\r");
		}
		sb.append("\r");
		sb.append("/**\r *  entity. @author wolf Date:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ "\r */");
		sb.append("\r");
		sb.append("\rpublic class ").append(className);
		if (null != extendsClassName) {
			sb.append(" extends ").append(extendsClassName);
		}
		if (type == 1) { // bean
			sb.append(" ").append("implements java.io.Serializable {\r");
		} else {
			sb.append(" {\r");
		}
		sb.append("\r\t");
		sb.append("private static final long serialVersionUID = 1L;\r\t");
		String temp = className.substring(0, 1).toLowerCase();
		temp += className.substring(1, className.length());
		if (type == 1) {
			sb.append("private " + className + " " + temp + "; // entity ");
		}
		sb.append(content);
		sb.append("\r}");
		System.out.println(sb.toString());
		this.createFile(createPath, "", sb.toString());
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>表名转换成类名，去掉tb||t， 每_首字母大写<br>
	 * <b>作者：</b>肖财高<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTablesNameToClassName(String tableName) {
		String[] split = tableName.split("_");
		if (split.length > 1) {
			int index = 0;
			if ("tb".equals(split[0].toLowerCase()) || "t".equals(split[0].toLowerCase())) {
				index = 1;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = index; i < split.length; i++) {
				String sp = split[i].substring(0, 1).toUpperCase()
						+ split[i].substring(1, split[i].length());
				sb.append(sp);
			}
			System.out.println(sb.toString());
			return sb.toString();
		} else {
			String temp = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
			return temp;
		}
	}
	
	/**
	 * 
	 * <br>
	 * <b>功能：</b>表名转换成Field，去掉tb||t, 然后首个_首字母小写，剩下的每_首字母大写<br>
	 * <b>作者：</b>邓卡仁<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTablesNameTolowerClassName(String tableName) {
		String[] split = tableName.split("_");
		if (split.length > 1) {
			int index = 0;
			if ("tb".equals(split[0].toLowerCase()) || "t".equals(split[0].toLowerCase())) {
				index = 1;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = index; i < split.length; i++) {
				
				String sp = "";
				if(i == index) {
					sp = split[i].toLowerCase();
				} else {
					sp = split[i].substring(0, 1).toUpperCase()
							+ split[i].substring(1, split[i].length()).toLowerCase();;
				}
				
				sb.append(sp);
			}
			System.out.println(sb.toString());
			return sb.toString();
		} else {
			String temp = split[0].substring(0, 1).toLowerCase() + split[0].substring(1, split[0].length());
			return temp;
		}
	}
	
	/**
	 * 
	 * <br>
	 * <b>功能：</b>表名转换成Field，去掉tb||t, 然后首个_所有字母小写，返回<br>
	 * <b>作者：</b>邓卡仁<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTablesNameToModuleName(String tableName) {
		String[] split = tableName.split("_");
		if (split.length > 1) {
			int index = 0;
			if ("tb".equals(split[0].toLowerCase()) || "t".equals(split[0].toLowerCase())) {
				index = 1;
			}
			String sp = split[index].toLowerCase();
			System.out.println(sp);
			return sp;
		} else {
			String temp = split[0].toLowerCase();
			return temp;
		}
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>创建文件<br>
	 * <b>作者：</b>肖财高<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param path
	 * @param fileName
	 * @param str
	 * @throws IOException
	 */
	public void createFile(String path, String fileName, String str) throws IOException {
		FileWriter writer = new FileWriter(new File(path + fileName));
		writer.write(new String(str.getBytes("utf-8")));
		writer.flush();
		writer.close();
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>生成sql语句<br>
	 * <b>作者：</b>肖财高<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param tableName
	 * @throws Exception
	 */
	static String selectStr = "select ";
	static String from = " from ";

	public Map<String, Object> getAutoCreateSql(String tableName, String schema) throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		List<ColumnData> columnDatas = getColumnDatas(tableName, schema);
		String columns = this.getColumnSplit(columnDatas);
		String[] columnList = getColumnList(columns); // 表所有字段
		String columnFields = getColumnFields(columns); // 表所有字段 按","隔开
		// String insert="insert into "+tableName+"("+columns.replaceAll("\\|",
		// ",")+")\n values(#{"+columns.replaceAll("\\|", "},#{")+"})";
		String insert = getInsertSql(tableName, columnList, columns);
		String update = getUpdateSql(tableName, columnList);
		String updateSelective = getUpdateSelectiveSql(tableName, columnDatas);
		String selectById = getSelectByIdSql(tableName, columnList);
		String delete = getDeleteSql(tableName, columnList);
		sqlMap.put("columnList", columnList);
		sqlMap.put("columnFields", columnFields);
		sqlMap.put("insert", insert.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
		sqlMap.put("update", update.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
		sqlMap.put("delete", delete);
		sqlMap.put("updateSelective", updateSelective);
		sqlMap.put("selectById", selectById);
		return sqlMap;
	}

	/**
	 * delete
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public String getDeleteSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(tableName).append(" set is_del = 1 ").append(" where ");
		sb.append(columnsList[0]).append(" = #{").append(getColomnField(columnsList[0])).append("}");
		return sb.toString();
	}

	/**
	 * 根据id查询
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public String getSelectByIdSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("select <include refid=\"Base_Column_List\" /> \n");
		sb.append("\t from ").append(tableName).append(" where ");
		sb.append(columnsList[0]).append(" = #{").append(getColomnField(columnsList[0])).append("}");
		return sb.toString();
	}

	/**
	 * 获取所有的字段，并按","分割
	 * 
	 * @param columns
	 * @return
	 * @throws SQLException
	 */
	public String getColumnFields(String columns) throws SQLException {
		String fields = columns;
		if (fields != null && !"".equals(fields)) {
			fields = fields.replaceAll("[|]", ",");
		}
		return fields;
	}

	/**
	 * 
	 * @param columns
	 * @return
	 * @throws SQLException
	 */
	public String[] getColumnList(String columns) throws SQLException {
		String[] columnList = columns.split("[|]");
		return columnList;
	}

	/**
	 * 修改记录
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public String getInsertSql(String tableName, String[] columnsList, String columns) throws SQLException {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < columnsList.length; i++) {
			String column = columnsList[i];
			
			if ("CREATETIME".equals(column.toUpperCase()))
				continue;

			if ("UPDATETIME".equals(column.toUpperCase()))
				sb.append(column + "=now()");
			else
				sb.append("#{" + getColomnField(column) + "}");// D.K.R
			// 最后一个字段不需要","
			if ((i + 1) < columnsList.length) {
				sb.append(",");
			}
		}
		String insert = "insert into " + tableName + "(" + columns.replaceAll("\\|", ",") + ")\n values(" + sb + ")";
		return insert;
	}

	/**
	 * 修改记录
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public String getUpdateSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < columnsList.length; i++) {
			String column = columnsList[i];
			if ("CREATETIME".equals(column.toUpperCase()))
				continue;

			if ("UPDATETIME".equals(column.toUpperCase()))
				sb.append(column + "=now()");
			else
				sb.append(column + "=#{" + getColomnField(column) + "}");// D.K.R
			// 最后一个字段不需要","
			if ((i + 1) < columnsList.length) {
				sb.append(",");
			}
		}
		String update = "update " + tableName + " set " + sb.toString() + " where " + columnsList[0] + "=#{"
				+ getColomnField(columnsList[0]) + "}";
		return update;
	}

	public String getUpdateSelectiveSql(String tableName, List<ColumnData> columnList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		ColumnData cd = columnList.get(0); // 获取第一条记录，主键
		sb.append("\t<trim  suffixOverrides=\",\" >\n");
		for (int i = 1; i < columnList.size(); i++) {
			ColumnData data = columnList.get(i);
			String colField = data.getColField();
			sb.append("\t<if test=\"").append(colField).append(" != null ");
			// String 还要判断是否为空
			if ("String" == data.getDataType()) {
				sb.append(" and ").append(colField).append(" != ''");
			}
			sb.append(" \">\n\t\t");
			sb.append(colField + "=#{" + data.getColField() + "},\n");
			sb.append("\t</if>\n");
		}
		sb.append("\t</trim>");
		String update = "update " + tableName + " set \n" + sb.toString() + " where " + cd.getColumnName() + "=#{"
				+ cd.getColField() + "}";
		return update;
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>获取所有列名字<br>
	 * <b>作者：</b>肖财高<br>
	 * <b>日期：</b> 2011-12-21 <br>
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public String getColumnSplit(List<ColumnData> columnList) throws SQLException {
		StringBuffer commonColumns = new StringBuffer();
		for (ColumnData data : columnList) {
			commonColumns.append(data.getColumnName() + "|");
		}
		return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
	}

}
