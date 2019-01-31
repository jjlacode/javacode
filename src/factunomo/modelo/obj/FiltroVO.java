package factunomo.modelo.obj;

import java.util.Date;



public class FiltroVO {
	
	private Date fechaInicio;
	private Date fechaFinal;
	private String nombre;
	
	
	

	public Date getFechaInicio() {
		return fechaInicio;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public String getNombre() {
		return nombre;
	}

	
	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String toString(){
		String res = fechaInicio+", "+fechaFinal;
		res += ", "+nombre;
		return res;
	}
}
