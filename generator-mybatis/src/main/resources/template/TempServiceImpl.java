
#if(${moduleName} != "")
package ${servicePackage}.${moduleName}.impl;
#else
package ${servicePackage}.impl;
#end
//import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${mapperPackage}.${className}Mapper;
import ${servicePackage}.I${className}Service;
import ${beanPackage}.${className}Bean;

/**
 * 
 * <br>
 * <b>功能：</b>${className}Service<br>
 * <b>作者：</b>franck<br>
 */
@Service("$!{lowerName}Service")
public class ${className}ServiceImpl extends BasicServiceImpl implements I${className}Service  {
	//private final static Logger log= Logger.getLogger(${className}Service.class);
	
	@Autowired
    private ${className}Mapper mapper;

		
	public ${className}Mapper<${className}Bean> getMapper() {
		return mapper;
	}

}
