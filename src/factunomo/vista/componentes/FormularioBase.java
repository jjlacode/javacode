package factunomo.vista.componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import factunomo.modelo.Modelo;
import factunomo.modelo.obj.ElementoListaVO;
import factunomo.utils.StringUtil;



public class FormularioBase extends JPanel {

	private static final long serialVersionUID = 1L;

	
	protected GridBagConstraints constraints;
	protected Modelo modelo;
	
	
	
	/**
	 * Constructor por defecto.
	 */
	public FormularioBase(){
		super();
		this.modelo = Modelo.getInstance();
	}
	
	
	/**
	 * Configura el contenedor del Frame.
	 */
	public void configurarContenedor(){
		this.setLayout(new GridBagLayout());
		this.constraints = new GridBagConstraints();
		this.constraints.insets = new Insets(3, 3, 3, 3);        
		this.constraints.gridx = 0;        
		this.constraints.gridy = 0;
	}

	
	
	/**
	 * Inserta un titulo al formulario
	 * @param texto
	 */
	public void addTitulo(String texto) {
		JLabel titulo = new JLabel(texto);
		Font auxFont=titulo.getFont();
		titulo.setFont(new Font(auxFont.getFontName(), auxFont.getStyle(), 20));
		titulo.setForeground(Color.BLUE);
		
		GridBagConstraints ct = new GridBagConstraints();
		ct.anchor=GridBagConstraints.NORTH;
		ct.insets=new Insets(0,0,10,0);
		ct.gridwidth=GridBagConstraints.REMAINDER;
        add(titulo, ct);
        this.constraints.gridy++;
        this.constraints.gridy++;
	}
	
	/**
	 * Inserta un ERROR
	 * @param texto
	 */
	public void addError(String texto) {
		JLabel titulo = new JLabel("<html><b><font color='red'>ERROR: </font> "+texto+"</b></html>");
		Font auxFont=titulo.getFont();
		titulo.setFont(new Font(auxFont.getFontName(), auxFont.getStyle(), 14));
		
		GridBagConstraints ct = new GridBagConstraints();
		ct.anchor=GridBagConstraints.NORTH;
		ct.insets=new Insets(0,0,10,0);
		ct.gridwidth=GridBagConstraints.REMAINDER;
        add(titulo, ct);
	}
	
	/**
	 * Inserta un texto fijo
	 * @param texto
	 */
	public void addTexto(String texto) {
		JLabel titulo = new JLabel(texto);
		
		GridBagConstraints ct = new GridBagConstraints();
		ct.anchor=GridBagConstraints.NORTH;
		ct.insets=new Insets(0,0,10,0);
		ct.gridwidth=GridBagConstraints.REMAINDER;
        add(titulo, ct);
        this.constraints.gridy++;
	}
	
	
	public void addField(String label, JComponent component) {
		addField(new JLabel("<html><b>"+label+"</b></html>"), component);
	}    
	
	
	/**
	 * Inserta un nuevo campo en el formulario
	 * @param label Etiqueta para el campo
	 * @param component Componente a insertar
	 */
	public void addField(JLabel label, JComponent component) {        
		this.constraints.gridx = 0;
		this.constraints.weightx = 0;
		this.constraints.weighty = 0;
		Class<?> clazz = component.getClass();
		this.constraints.fill = GridBagConstraints.NONE;
		if(JScrollPane.class == clazz)
			this.constraints.anchor = GridBagConstraints.FIRST_LINE_END;
		else
			this.constraints.anchor = GridBagConstraints.LINE_END;
		this.add(label, this.constraints);
		this.constraints.gridx = 1;
		if(JScrollPane.class == clazz) {
			this.constraints.weightx = 1;
			this.constraints.weighty = 1;
			this.constraints.fill = GridBagConstraints.BOTH;
			this.add(component, this.constraints);
		} else if(JTextField.class == clazz) {
			JTextField textField = (JTextField) component;
			if(textField.getColumns() == 0) {
				// si no se ha especificado un número de columnas 
				// el componente será tan ancho como la quepa en la ventana 
				this.constraints.fill = GridBagConstraints.BOTH;
			} else {
				// esto arregla un problema que ocurre
				// cuando el componente no cabe en anchura
				textField.setMinimumSize(textField.getPreferredSize());
				this.constraints.anchor = GridBagConstraints.LINE_START;
			}
			this.add(component, constraints);
		} else {
			this.constraints.anchor = GridBagConstraints.LINE_START;
			this.add(component, constraints);
		}
		label.setLabelFor(component);
		this.constraints.gridy++;
	}
	
	
	
	public static void addField(String label, JComponent component, JPanel panel, GridBagConstraints ct) {
		addField(new JLabel("<html><b>"+label+"</b></html>"), component, panel, ct);
	}
	
	
	private static void addField(JLabel label, JComponent component, JPanel panel, GridBagConstraints ct) {
		ct.fill = GridBagConstraints.NONE;
		ct.anchor = GridBagConstraints.LINE_END;
		Class<?> clazz = component.getClass();
		ct.gridx = 0;
		panel.add(label, ct);
		ct.gridx = 1;
		if(JTextField.class == clazz){
			ct.fill = GridBagConstraints.BOTH;
			panel.add(component, ct);
		} else{
			ct.anchor = GridBagConstraints.LINE_START;
			panel.add(component, ct);
		}
		
		label.setLabelFor(component);
	}
	
	
	
	
	
	
	/**
	 * Inicializa el campo fecha que se pasa por parametro
	 * @param campo
	 */
	/*
	protected void inicializarCampoFecha(JDateChooser campo){
		campo.getEditor().getTextField().setText("");
	}*/
	
	/**
	 * Inicializa un campo fecha con la fecha que se pasa por parametro.
	 * @param campo
	 * @param fecha
	 */
	/*
	protected void inicializarCampoFecha(JDateChooser campo, Date fecha){
		campo.getEditor().getTextField().setText(DateUtils.dateToString(fecha,"dd/MM/yyyy"));
		campo.setDate(fecha);
	}*/
	
	/**
	 * Devuelve el combo de años
	 * 
	 * @return
	 */
	protected JComboBox<String> getComboAnhos(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		combo = this.modelo.getAnhos(tipo);
		return combo;
	}

	/**
	 * Devuelve el combo de proveedores
	 * 
	 * @return
	 */
	protected JComboBox<String> getComboProveedores(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		combo = this.modelo.getProveedores(tipo);
		return combo;
	}
	
	/**
	 * Devuelve el combo de clientes
	 * 
	 * @return
	 */
	protected JComboBox<String> getComboClientes(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		combo = this.modelo.getClientes(tipo);
		return combo;
	}
	
	
	
	/**
	 * Devuelve el combo de formas de pago
	 * 
	 * @return
	 */
	protected JComboBox<String> getComboFormasPago() {
		JComboBox<String> combo = new JComboBox<String>();
		combo = this.modelo.getFormasPago();
		return combo;
	}

	
	
	/**
	 * Funcion que devuelve la descripcion de un elemento de una lista 
	 * a partir de su codigo
	 * @param lista
	 * @param codigo
	 * @return
	 */
	protected String getDescripcionElemento(ArrayList<ElementoListaVO> lista, String codigo){
		if(!StringUtil.isNullOrBlank(codigo)){
			if(lista != null && lista.size() > 0){
				for(int i=0;i<lista.size();i++){
					ElementoListaVO e = (ElementoListaVO)lista.get(i);
					if(codigo.equals(e.getCodigo()))
						return e.getDescripcion();
				}
			}
		}
		return "";
	}
}
