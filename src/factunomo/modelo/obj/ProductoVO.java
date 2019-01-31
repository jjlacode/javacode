package factunomo.modelo.obj;

import java.io.Serializable;

import factunomo.utils.StringUtil;

public class ProductoVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String ID_producto;
	private String nombre;
	private Float precio;
	private Float IVA;

	
	
	
	public ProductoVO(){
		this.codigo = "";
		this.ID_producto = "";
		this.nombre = "";
		this.precio = null;
		this.IVA = null;

	}

	public String getCodigo() {
		return codigo;
	}
	public String getIdProducto() {
		return ID_producto;
	}
	public String getNombre() {
		return nombre;
	}
	public Float getPrecio() {
		return precio;
	}
	public Float getIVA() {
		return IVA;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setIdProducto(String ID_producto) {
		this.ID_producto = ID_producto;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public void setIVA(Float IVA) {
		this.IVA = IVA;
	}

	public String toString(){
		String res = StringUtil.nullToString(this.nombre);
		return res;
	}
}
