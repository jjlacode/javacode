package factunomo.modelo.obj;

import java.io.Serializable;

import factunomo.utils.StringUtil;

public class ClienteVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String ID_cliente;
	private String nombre;
	private String direccion;
	private String CP;
	private String ciudad;
	private String provincia;
	private String NIF;
	private String email;
	private String web;
	private String telefono;
	private String movil;
	private String fax;
	private Boolean conIRPF;
	private String numeroCuenta;
	private String ID_formaPago;

	
	
	
	public ClienteVO(){
		this.ID_cliente = "";
		this.nombre = "";
		this.direccion = "";
		this.CP = "";
		this.ciudad = "";
		this.provincia = "";
		this.NIF = "";
		this.email = "";
		this.web = "";
		this.telefono = "";
		this.movil = "";
		this.fax = "";
		this.conIRPF = true;
		this.numeroCuenta = "";
		this.ID_formaPago = "";
	}

	public String getIdCliente() {
		return ID_cliente;
	}
	public String getNombre() {
		return nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public String getCP() {
		return CP;
	}	
	public String getCiudad() {
		return ciudad;
	}
	public String getProvincia() {
		return provincia;
	}
	public String getNIF() {
		return NIF;
	}
	public String getEmail() {
		return email;
	}
	public String getWeb() {
		return web;
	}
	public String getTelefono() {
		return telefono;
	}
	public String getMovil() {
		return movil;
	}
	public String getFax() {
		return fax;
	}
	public Boolean getConIRPF() {
		return conIRPF;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public String getIdFormaPago() {
		return ID_formaPago;
	}




	public void setIdCliente(String ID_cliente) {
		this.ID_cliente = ID_cliente;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public void setCP(String CP) {
		this.CP = CP;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public void setNIF(String NIF) {
		this.NIF = NIF;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public void setConIRPF(Boolean conIRPF) {
		this.conIRPF = conIRPF;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public void setIdFormaPago(String ID_formaPago) {
		this.ID_formaPago = ID_formaPago;
	}

	public String toString(){
		String res = StringUtil.nullToString(this.nombre);
		return res;
	}
}
