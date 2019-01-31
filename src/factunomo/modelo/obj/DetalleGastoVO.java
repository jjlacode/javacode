package factunomo.modelo.obj;

import java.io.Serializable;

import factunomo.utils.StringUtil;

import java.text.DecimalFormat;


public class DetalleGastoVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer ID_detalleGasto;
	private String ID_gasto;
	private String ID_producto;
	private String descripcion;
	private Float precio;
	private Float cantidad;
	private Float IVA;
	private Float descuento;
	private Float subTotal;



	
	
	/**
	 * Constructor por defecto
	 */
	public DetalleGastoVO(){
		this.ID_detalleGasto = 0;
		this.ID_gasto = "";
		this.ID_producto = null;
		this.descripcion = "";
		this.precio = null;
		this.cantidad = null;
		this.IVA = null;
		this.descuento = null;
		this.subTotal = null;

	}

	
	
	public Integer getIdDetalleGasto() {
		return ID_detalleGasto;
	}
	public String getIdGasto() {
		return ID_gasto;
	}
	public String getIdProducto() {
		return ID_producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public Float getPrecio() {
		return precio;
	}
	public Float getCantidad() {
		return cantidad;
	}
	public Float getIVA() {
		return IVA;
	}
	public Float getDescuento() {
		return descuento;
	}
	public Float getSubTotal() {
		return subTotal;
	}
	



	public void setIdDetalleGasto(Integer ID_detalleGasto) {
		this.ID_detalleGasto = ID_detalleGasto;
	}
	public void setIdGasto(String ID_gasto) {
		this.ID_gasto = ID_gasto;
	}
	public void setIdProducto(String ID_producto) {
		this.ID_producto = ID_producto;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	public void setIVA(Float IVA) {
		this.IVA =IVA;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}
	public void setSubTotal(Float subTotal) {
		this.subTotal = subTotal;
	}

	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}

	public String toString(){
		String res = StringUtil.nullToString(this.ID_producto) + ", "+StringUtil.nullToString(this.descripcion);
		res += ", "+ this.cantidad+", "+this.precio;
		res += ", "+this.IVA;
		res += ", "+this.subTotal;
		//res += ", "+this.subTotal;
		return res;
	}
}
