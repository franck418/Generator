#if(${moduleName} != "")
package ${mapperPackage}.${moduleName};
#else
package ${mapperPackage};
#end

/**
 * ${className} Mapper
 * @author Administrator
 *
 */
public interface ${className}Mapper<T> extends BasicMapper<T> {
	
	
}
