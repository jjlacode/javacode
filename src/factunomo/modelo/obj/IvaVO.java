package factunomo.modelo.obj;


public class IvaVO {
	
	private Float tipoIVA;
	private Float base;
	private Float cantidad;
	
	
	

	public Float getTipoIVA() {
		return tipoIVA;
	}
	public Float getBase() {
		return base;
	}
	public Float getCantidad() {
		return cantidad;
	}

	
	
	public void setTipoIVA(Float tipoIVA) {
		this.tipoIVA = tipoIVA;
	}
	public void setBase(Float base) {
		this.base = base;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	
	public String toString(){
		String res = tipoIVA+", "+base;
		res += ", "+cantidad;
		return res;
	}
}
