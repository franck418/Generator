package ${beanPackage};

import java.io.Serializable;

import com.zy.report.core.entity.${className};

public class ${className}Bean extends ${className}  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	#foreach($item in $!{columnDatas})
	#if($item.dataType =="String" )

	private ${item.dataType} ${item.colField}Exact;
	#end
	#end
	
	public static class Builder extends ${className}{
	#foreach($item in $!{columnDatas})
	#if($item.dataType =="String" )

		private ${item.dataType} ${item.colField}Exact;
	#end
	#end

	#foreach($item in $!{columnDatas})

		public Builder ${item.colField}(${item.dataType}  ${item.colField}){
			set${item.colFiled4Method}(${item.colField});
			return this;
		}
		#if($item.dataType =="String" )

		public Builder ${item.colField}Exact(${item.dataType}  ${item.colField}Exact){
			this.${item.colField}Exact = ${item.colField}Exact;
			return this;
		}
		#end
	#end
		
		public ${className}Bean build(){
			return new ${className}Bean(this);
		}
	}
	

	//======Constructor========
	public ${className}Bean(){}

	public ${className}Bean(Builder builder){

	#foreach($item in $!{columnDatas})
	set${item.colFiled4Method}(builder.get${item.colFiled4Method}());
	#if($item.dataType =="String" )
		this.${item.colField}Exact = builder.${item.colField}Exact;
	#end
	#end

	}

	//======Getter and Setter=====
	#foreach($item in $!{columnDatas})
	#if($item.dataType =="String" )

	public void set${item.colFiled4Method}Exact(${item.dataType} ${item.colField}Exact) {
		this.${item.colField}Exact = ${item.colField}Exact;
	}
	public ${item.dataType} get${item.colFiled4Method}Exact() {
		return this.${item.colField}Exact;
	}
	#end
	#end
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("${className}:");
		builder.append("{");
	#foreach($item in $!{columnDatas})
    builder.append(", ${item.colField} =").append(get${item.colFiled4Method}());
	#if($item.dataType =="String" )
	builder.append(", ${item.colField}Exact =").append(${item.colField}Exact);
	#end
	#end
	builder.append("}");
		return builder.toString();
	}
	
	
	
	
	
	
	
}
