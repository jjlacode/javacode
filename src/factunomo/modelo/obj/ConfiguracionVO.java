package factunomo.modelo.obj;

import java.io.Serializable;

import factunomo.utils.StringUtil;

public class ConfiguracionVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String direccion;
	private String CP;
	private String ciudad;
	private String provincia;
	private String NIF;
	private String email;
	private String web;
	private String telefono;
	private Float IVAGeneral;
	private Float IVAReducido;
	private Float IVASuperReducido;
	private Float IRPF;
	private String numeroCuenta1;
	private String numeroCuenta2;
	private String numeroCuenta3;
	private Integer periodo;
	private Integer ejercicio;
	
	
	
	public ConfiguracionVO(){
		this.nombre = "";
		this.direccion = "";
		this.CP = "";
		this.ciudad = "";
		this.provincia = "";
		this.NIF = "";
		this.email = "";
		this.web = "";
		this.telefono = "";
		this.IVAGeneral = null;
		this.IVAReducido = null;
		this.IVASuperReducido = null;
		this.IRPF = 0f;
		this.numeroCuenta1 = "";
		this.numeroCuenta2 = "";
		this.numeroCuenta3 = "";
		this.periodo = null;
		this.ejercicio = null;
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
	public Float getIVAGeneral() {
		return IVAGeneral;
	}
	public Float getIVAReducido() {
		return IVAReducido;
	}
	public Float getIVASuperReducido() {
		return IVASuperReducido;
	}
	public Float getIRPF() {
		return IRPF;
	}
	public String getNumeroCuenta1() {
		return numeroCuenta1;
	}
	public String getNumeroCuenta2() {
		return numeroCuenta2;
	}
	public String getNumeroCuenta3() {
		return numeroCuenta3;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public Integer getEjercicio() {
		return ejercicio;
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
	public void setIVAGeneral(Float IVAGeneral) {
		this.IVAGeneral = IVAGeneral;
	}
	public void setIVAReducido(Float IVAReducido) {
		this.IVAReducido = IVAReducido;
	}
	public void setIVASuperReducido(Float IVASuperReducido) {
		this.IVASuperReducido = IVASuperReducido;
	}
	public void setIRPF(Float IRPF) {
		this.IRPF = IRPF;
	}
	public void setNumeroCuenta1(String numeroCuenta1) {
		this.numeroCuenta1 = numeroCuenta1;
	}
	public void setNumeroCuenta2(String numeroCuenta2) {
		this.numeroCuenta2 = numeroCuenta2;
	}
	public void setNumeroCuenta3(String numeroCuenta3) {
		this.numeroCuenta3 = numeroCuenta3;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}
	
	public String toString(){
		String res = StringUtil.nullToString(this.nombre);
		return res;
	}
}
