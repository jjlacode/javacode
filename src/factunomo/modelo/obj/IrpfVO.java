package factunomo.modelo.obj;

public class IrpfVO {
	
	private String IdCliente;
	private Float tipoIRPF;
	private Float base;
	private Float cantidad;
	
	
	
	public String getIdCliente(){
		return IdCliente;
	}
	public Float getTipoIRPF() {
		return tipoIRPF;
	}
	public Float getBase() {
		return base;
	}
	public Float getCantidad() {
		return cantidad;
	}

	
	
	public void setIdCliente(String IdCliente) {
		this.IdCliente = IdCliente;
	}
	public void setTipoIRPF(Float tipoIRPF) {
		this.tipoIRPF = tipoIRPF;
	}
	public void setBase(Float base) {
		this.base = base;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	
	public String toString(){
		String res = IdCliente+", "+base;
		res += ", "+cantidad;
		return res;
	}
}
