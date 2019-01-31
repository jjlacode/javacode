package factunomo.vista.componentes;

import javax.swing.JLabel;


public class FormularioInicio extends FormularioBase {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Constructor por defecto.
	 */
	public FormularioInicio(){
		super();
		configurarContenedor();
		configurarComponentes();
	}	
	
	
	
	/**
	 * Configura los componentes de la aplicación: menus, paneles, etc.
	 */
	public void configurarComponentes(){
		
		this.addTexto("Factunomo 1.0");
		this.addTexto("");
		this.addTexto("Autor: Sergio Lozano");
		this.addTexto("© 2013");
		this.addTexto("miromaticas@gmail.com");
	}

}
