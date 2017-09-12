package ${controllerPackage}.${moduleName};

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.bean.BeanAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bmind.core.entry.JSONResult;
import com.bmind.core.entry.Result;
import com.bmind.api.entity.annotation.APIReturn;

import ${modelPackage}.${moduleName}.${className}Model;

import ${beanPackage}.${moduleName}.${className}Bean;

import ${servicePackage}.${moduleName}.${className}Service;

import com.bmind.core.base.BaseController;
@Controller
@RequestMapping("/${lowerName}")
public class ${className}Controller extends BaseController{

	//private final static Logger log= Logger.getLogger(${className}Controller.class);
	
	// service start
	@Autowired(required=false) // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
	private ${className}Service<${className}Bean> ${lowerName}Service;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	//@RequestMapping("${apiVersionPath}/list.do")
	@ResponseBody
	public Map<?, ?> list(HttpServletRequest request, HttpServletResponse response)throws Exception {	
		Map<String, String> context = getRootMap();
		return context;
	}

	/**
	 * ilook 首页
	 * @param model
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@APIReturn(data={List.class, ${className}Bean.class})
	@RequestMapping("${apiVersionPath}/dataList.do") 
	@ResponseBody
	public Result<?> dataList(${className}Model model, HttpServletRequest request, HttpServletResponse response)throws Exception {

		JSONResult<List<${className}Bean>> jResult = new JSONResult<List<${className}Bean>>();
		
		List<${className}Bean> dataList = ${lowerName}Service.queryByList(model);
	
		jResult.setData(dataList);
		return jResult;
	}

	/**
	 * 添加或修改数据
	 * @param bean
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@APIReturn(data={${className}Bean.class})
	@RequestMapping("${apiVersionPath}/save.do")
	@ResponseBody
	public Result<?> save(${className}Model model,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		JSONResult<${className}Bean> jResult = new JSONResult<${className}Bean>();
		
		${lowerName}Service.updateBySelective(model);
		
		${className}Bean bean = ${lowerName}Service.queryById(model.getId());
		
		jResult.setData(bean);
		return jResult;
	}

	/**
	 * 获取数据
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@APIReturn(data={${className}Bean.class})
	@RequestMapping("${apiVersionPath}/getInfoById.do")
	@ResponseBody
	public Result<?> getInfoById(@RequestParam(value="id")Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		JSONResult<${className}Bean> jResult = new JSONResult<${className}Bean>();
		
		${className}Bean bean  = ${lowerName}Service.queryById(id);
		
		jResult.setData(bean);
		
		return jResult;
	}

	/**
	 * 删除数据
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@APIReturn(back=Result.class)
	@RequestMapping("${apiVersionPath}/delete.do")
	@ResponseBody
	public Result<?> delete(Integer[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Result<?> result = new Result<Object>();
		Object []objArr = new Object[]{ids};
		${lowerName}Service.delete(objArr);
		return result;
	}

}
