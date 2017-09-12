package ${beanPackage}.${moduleName};

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bmind.core.base.BaseBean;
import com.bmind.core.dic.EnumObject;

import com.bmind.api.entity.annotation.Comment;

import ${atomicityPackage}.${moduleName}.${className};

@Comment(description = "${classComment}", fieldName = "${lowerName}Bean", dataType = "${className}Bean", toDetail="${className}Bean", example="{...}")
public class ${className}Bean extends ${className} implements BaseBean, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void initViewBean() {
		//初始化bean 的BaseBean成员属性， for view
		BaseBean.ViewBean.init(this);
		
		//初始化${className}属性与${className}Bean属性对应,  for view
		
	}
	
	
	@Override
	public List<Class<? extends EnumObject>> initDictClass() {
		// TODO Auto-generated  method stub
		
		List<Class<? extends EnumObject>> list = new ArrayList<Class<? extends EnumObject>>();
		
		//初始化bean成员属性， for view
		list.addAll( BaseBean.ViewBean.initDictClass(this) );
		
		return list;
	}
}
