package generator;

import org.apache.velocity.VelocityContext;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <br>
 * <b>功能：</b>详细的功能描述<br>
 * <b>作者：</b>franck<br>
 */
public class CreateJava {
	private static ResourceBundle res = ResourceBundle.getBundle("DataSourceConfig");
	private static String url = res.getString("gpt.url");
	private static String username = res.getString("gpt.username");
	private static String passWord = res.getString("gpt.password");
	private static String schema = res.getString("gpt.schema");

	// 项目跟路径路径，此处修改为你的项目路径
//	 private static String rootPath =
//	 "E:\\Source\\IDEAWORK\\Bmind\\auto-generator";


	private static String rootPath = GeneratorConfig.rootPath;

	// private static String actionPath =


	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		//System.out.println("dffD".indexOf("[A-Z]"));
		TableData t = new TableData();
		t.setComment("排名表");
		t.setTableName("zy_exam_subject_topic");
		doExcute(t);
		
		//excuteList();
	}

	/**
	 * 获取项目的路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		String rootPath = "";
		try {
			File file = new File(CommonPageParser.class.getResource("/").getFile());
			rootPath = file.getParentFile().getParentFile().getParent() + "\\";
			rootPath = java.net.URLDecoder.decode(rootPath, "utf-8");
			System.out.println(rootPath);
			return rootPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootPath;
	}

	/**
	 * 生成单张表的数据模板
	 * @param table
     */
	public static void doExcute(TableData table) {
		if(table == null || table.getTableName() == null) {
			return;
		}
		CreateBean createBean = new CreateBean();
		createBean.setMysqlInfo(url, username, passWord, schema);

		String tableName = table.getTableName(); //
		String codeName = table.getComment(); //
		
		String classComment = "";
		if(codeName != null || !"".equals(codeName)) {
			
			int tail = codeName.indexOf(";") >= 0 ? codeName.indexOf(";") + 1 : codeName.length();
			classComment = codeName.substring(0,  tail).replaceAll("\r\n", "<br/>").replaceAll(" ", "");
		}

		String className = createBean.getTablesNameToClassName(tableName);
		String lowerName = createBean.getTablesNameTolowerClassName(tableName);
//		String moduleName = createBean.getTablesNameToModuleName(tableName);
		String moduleName ="";



		// 根路径
		// String srcPath = rootPath + "src\\";
		// 包路径
		// String pckPath = rootPath + "src\\com\\bmind\\e2c\\";
		// String pckPath = rootPath;

		// 页面路径，放到WEB-INF下面是为了不让手动输入路径访问jsp页面，起到安全作用
		// String webPath = rootPath + "WebRoot\\jsp\\product\\";

		// java,xml文件名称
		String atomicityPath = GeneratorConfig.atomicityPath + "/" + moduleName + "/" + className + ".java";
		String modelPath = GeneratorConfig.modelPath + "/" + moduleName + "/" + className + "Model.java";
		String beanPath = GeneratorConfig.beanPath +  "/" + moduleName + "/" + className+"Bean.java";
		String mapperPath = GeneratorConfig.mapperPath + "/" + moduleName + "/" + className + "Mapper.java";
		String servicePath = GeneratorConfig.servicePath + "/" + moduleName + "/I" + className + "Service.java";
		String serviceImplPath = GeneratorConfig.servicePath + "/impl/" + moduleName + "/" + className + "ServiceImpl.java";
		String controllerPath = GeneratorConfig.controllerPath + "/" + moduleName + "/" + className + "Controller.java";
		String sqlMapperPath = GeneratorConfig.xmlPath + "/" + moduleName + "/" + className + "Mapper.xml";
		// jsp页面路径
		// String pageEditPath = lowerName+"\\"+lowerName+"Edit.jsp";
		// String pageListPath = lowerName+"\\"+lowerName+"List.jsp";
		String atomicityPackage = (GeneratorConfig.atomicityPath /*+ "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String modelPackage = (GeneratorConfig.modelPath /*+ "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String beanPackage = (GeneratorConfig.beanPath /*+ "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String mapperPackage = (GeneratorConfig.mapperPath/* + "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String servicePackage = (GeneratorConfig.servicePath/* + "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String controllerPackage = (GeneratorConfig.controllerPath/* + "/" + moduleName*/).replaceAll("/|\\\\", ".");
		String sqlMapperPackage = (GeneratorConfig.xmlPath/* + "/" + moduleName*/).replaceAll("/|\\\\", ".");

		VelocityContext context = new VelocityContext();
		context.put("className", className); //
		context.put("lowerName", lowerName);
		context.put("codeName", codeName);
		context.put("classComment", classComment);
		context.put("tableName", tableName);
		context.put("moduleName", moduleName);

		context.put("atomicityPackage", atomicityPackage);
		context.put("beanPackage", beanPackage);
		context.put("modelPackage", modelPackage);
		context.put("mapperPackage", mapperPackage);
		context.put("servicePackage", servicePackage);
		context.put("controllerPackage", controllerPackage);
		context.put("sqlMapperPackage", sqlMapperPackage);
		
		if(GeneratorConfig.apiVersionPath != null && !"".equals(GeneratorConfig.apiVersionPath)) {
			context.put("apiVersionPath",GeneratorConfig.apiVersionPath);
		}

		/****************************** 生成bean字段 *********************************/
		try {
			// context.put("feilds",createBean.getBeanFeilds(tableName));
			// //生成bean
		} catch (Exception e) {
			e.printStackTrace();
		}

		/******************************* 生成sql语句 **********************************/
		try {
			Map<String, Object> sqlMap = createBean.getAutoCreateSql(tableName, schema);
			List<ColumnData> cDatas = createBean.getColumnDatas(tableName, schema);
			context.put("columnDatas", cDatas); // 生成bean
			ColumnData keyColumn = null;
			
			for(int i=0; cDatas != null&&i < cDatas.size(); i++) {
				if("PRI".equals(cDatas.get(i).getColumnKey())) {
					keyColumn = cDatas.get(i);
				}
			}
			context.put("keyColumn", keyColumn);
			context.put("SQL", sqlMap);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		//
		// -------------------生成文件代码---------------------/
		String srcRoot = rootPath + "src/main/java/";
		
		
		CommonPageParser.WriterPage(context, "TempAtomicity.java",srcRoot, atomicityPath); //生成实体Bean

//		CommonPageParser.WriterPage(context, "TempModel.java", srcRoot, modelPath); // 生成Model
//
		CommonPageParser.WriterPage(context, "TempBeanBuilder.java",srcRoot, beanPath); //生成实体Bean
//
		CommonPageParser.WriterPage(context, "TempMapper.java", srcRoot, mapperPath); // 生成MybatisMapper接口 // 相当于Dao
//
		CommonPageParser.WriterPage(context, "TempService.java", srcRoot, servicePath);// 生成Service

		CommonPageParser.WriterPage(context, "TempServiceImpl.java", srcRoot, serviceImplPath);// 生成ServiceImpl
//
		CommonPageParser.WriterPage(context, "TempMapper.xml", srcRoot, sqlMapperPath);// 生成Mybatis // xml配置文件
//
//		CommonPageParser.WriterPage(context, "TempController.java", srcRoot, controllerPath);// 生成Controller // 相当于接口
																								
		// 生JSP页面，如果不需要可以注释掉
//		context.put("basePath", "${e:basePath()}");
		// CommonPageParser.WriterPage(context, "TempList.jsp",webPath,
		// pageListPath);//生成Controller 相当于接口
		// CommonPageParser.WriterPage(context, "TempEdit.jsp",webPath,
		// pageEditPath);//生成Controller 相当于接口

		/************************* 修改xml文件 *****************************/
//		WolfXmlUtil xml = new WolfXmlUtil();
//		Map<String, String> attrMap = new HashMap<String, String>();
//		try {
//			/** 引入到mybatis-config.xml 配置文件 */
//			attrMap.clear();
//			attrMap.put("resource", sqlMapperPath);
//			xml.getAddNode(rootPath + "conf/mybatis-config.xml", "/configuration/mappers", "mapper", attrMap, "");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	/***
	 * 生成整个库的表的数据
	 * @throws SQLException
     */
	public static void excuteList() throws SQLException {
		CreateBean bean = new CreateBean();
		bean.setMysqlInfo(url, username, passWord, schema);
		List<TableData> tables = bean.getTables();
		
		for(TableData td : tables) {
			doExcute(td);
		}
	}
}
