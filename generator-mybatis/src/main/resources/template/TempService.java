package ${servicePackage}.${moduleName};

import org.springframework.stereotype.Service;

/**
 * 
 * <br>
 * <b>功能：</b>${className}Service<br>
 * <b>作者：</b>franck<br>
 */
@Service("$!{lowerName}Service")
public interface I${className}Service<T> extends BasicService<T> {
}
