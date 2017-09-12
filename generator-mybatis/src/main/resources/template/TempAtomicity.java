#if(${moduleName} != "")
package ${atomicityPackage}.${moduleName};
#else
package ${atomicityPackage};
#end

import java.io.Serializable;



public class ${className} implements Serializable {
	
	private static final long serialVersionUID = 1L;
	#foreach($item in $!{columnDatas})
	
	/**
	 * 	${item.columnComment}
	 */
	private ${item.dataType} ${item.colField};	
	#end	
	#foreach($item in $!{columnDatas})
	
	public void set${item.colFiled4Method}(${item.dataType} ${item.colField}) {
		this.${item.colField} = ${item.colField};
	}
	public ${item.dataType} get${item.colFiled4Method}() {
		return this.${item.colField};
	}
	#end
	
}
