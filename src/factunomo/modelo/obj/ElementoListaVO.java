package factunomo.modelo.obj;

import factunomo.utils.StringUtil;

public class ElementoListaVO {

	
	private String codigo;
	private String descripcion;
	
	
	public ElementoListaVO(){
		this.codigo = "";
		this.descripcion = "";
	}
	
	
	public ElementoListaVO(String codigo, String descripcion){
		this.codigo = codigo;
		this.descripcion = descripcion;
	}


	
	
	public String getCodigo() {
		return codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}

	
	

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	

	@Override
	public String toString() {
		return StringUtil.nullToString(this.descripcion);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoListaVO other = (ElementoListaVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
