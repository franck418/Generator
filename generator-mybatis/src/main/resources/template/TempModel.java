package ${modelPackage}.${moduleName};

import java.io.Serializable;

import com.bmind.core.base.BaseModel;
import com.bmind.core.entry.Pager;
import com.bmind.api.entity.annotation.Comment;
import ${atomicityPackage}.${moduleName}.${className};

@Comment(description = "${classComment}", fieldName = "${lowerName}Model", dateType = "${className}Model", toDetail="${className}Model", example="{...}")
public class ${className}Model extends ${className} implements BaseModel, Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private Pager pager ;//= new Pager();
	
	public ${className}Model() {
		
	}
	
	public Pager getPager() {
		return this.pager ;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
}
