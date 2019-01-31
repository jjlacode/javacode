package factunomo.modelo.obj;

import java.io.Serializable;
import java.util.Date;

import factunomo.utils.StringUtil;

public class IngresoVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String ID_ingreso;
	private Date fecha;
	private String ID_cliente;
	private String nombre;
	private String NIF;
	private String direccion;
	private String CP;
	private String ciudad;
	private String provincia;
	private Float tipoIRPF;
	private Float descuento;
	private Float baseImponible1;
	private Float IVA1;
	private Float baseImponible2;
	private Float IVA2;
	private Float baseImponible3;
	private Float IVA3;
	private String ID_formaPago;
	private String numeroCuenta;
	private Float TOTAL;

	
	
	/**
	 * Constructor por defecto
	 */
	public IngresoVO(){
		this.ID_ingreso = null;
		this.fecha = null;
		this.ID_cliente = "";
		this.nombre = "";
		this.NIF = "";
		this.direccion = "";
		this.CP = "";
		this.ciudad = "";
		this.provincia = "";
		this.tipoIRPF = 0f;
		this.descuento = 0f;
		this.baseImponible1 = null;
		this.IVA1 = null;
		this.baseImponible2 = null;
		this.IVA2 = null;
		this.baseImponible3 = null;
		this.IVA3 = null;		
		this.ID_formaPago = "";
		this.numeroCuenta = "";
		this.TOTAL = null;

	}

	
	
	
	public String getIdIngreso() {
		return ID_ingreso;
	}
	public Date getFecha() {
		return fecha;
	}
	public String getIdCliente() {
		return ID_cliente;
	}
	public String getNombre() {
		return nombre;
	}
	public String getNIF() {
		return NIF;
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
	public Float getTipoIRPF() {
		return tipoIRPF;
	}
	public Float getDescuento() {
		return descuento;
	}
	public Float getBaseImponible1() {
		return baseImponible1;
	}
	public Float getIVA1() {
		return IVA1;
	}
	public Float getBaseImponible2() {
		return baseImponible2;
	}
	public Float getIVA2() {
		return IVA2;
	}
	public Float getBaseImponible3() {
		return baseImponible3;
	}
	public Float getIVA3() {
		return IVA3;
	}
	public String getIdFormaPago() {
		return ID_formaPago;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public Float getTOTAL() {
		return TOTAL;
	}
	





	public void setIdIngreso(String ID_ingreso) {
		this.ID_ingreso = ID_ingreso;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setIdCliente(String ID_cliente) {
		this.ID_cliente = ID_cliente;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setNIF(String NIF) {
		this.NIF = NIF;
	}
	public void setDireccion(String direccion) {
		this.direccion =direccion;
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
	public void setTipoIRPF(Float tipoIRPF) {
		this.tipoIRPF = tipoIRPF;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}
	public void setBaseImponible1(Float baseImponible1) {
		this.baseImponible1 = baseImponible1;
	}
	public void setIVA1(Float IVA1) {
		this.IVA1 = IVA1;
	}
	public void setBaseImponible2(Float baseImponible2) {
		this.baseImponible2 = baseImponible2;
	}
	public void setIVA2(Float IVA2) {
		this.IVA2 = IVA2;
	}
	public void setBaseImponible3(Float baseImponible3) {
		this.baseImponible3 = baseImponible3;
	}
	public void setIVA3(Float IVA3) {
		this.IVA3 = IVA3;
	}
	public void setIdFormaPago(String ID_formaPago) {
		this.ID_formaPago = ID_formaPago;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public void setTOTAL(Float TOTAL) {
		this.TOTAL = TOTAL;
	}


	public String toString(){
		String res = StringUtil.nullToString(this.ID_cliente)+" "+StringUtil.nullToString(this.nombre);
		res += ", "+this.baseImponible1+", "+this.IVA1;
		res += ", "+this.TOTAL;
		return res;
	}
}
