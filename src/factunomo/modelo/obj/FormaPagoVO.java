package factunomo.modelo.obj;

import java.io.Serializable;

import factunomo.utils.StringUtil;

public class FormaPagoVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String ID_formapago;
	private String nombre;
	private boolean numeroCuenta;

	
	
	public FormaPagoVO(){
		this.ID_formapago = "";
		this.nombre = "";
		this.numeroCuenta=false;
	}

	
	public String getIdFormaPago() {
		return ID_formapago;
	}
	public String getNombre() {
		return nombre;
	}
	public boolean getNumeroCuenta() {
		return numeroCuenta;
	}
	

	public void setIdFormaPago(String ID_formapago) {
		this.ID_formapago = ID_formapago;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setNumeroCuenta(boolean cuenta) {
		this.numeroCuenta = cuenta;
	}	


	public String toString(){
		String res = StringUtil.nullToString(this.nombre);
		return res;
	}
}
