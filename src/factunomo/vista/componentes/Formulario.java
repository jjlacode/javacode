package factunomo.vista.componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import factunomo.modelo.Modelo;
import factunomo.modelo.obj.GastoVO;
import factunomo.utils.StringUtil;
import com.toedter.calendar.JDateChooser;

public class Formulario extends JPanel {

	private static final long serialVersionUID = 1L;

	
	private GridBagConstraints constraints;
	private Modelo modelo;
	/**
	 * Componentes
	 */
	private JDateChooser fecha;
	private JTextField nombre;

	
	
	/**
	 * Constructor por defecto.
	 */
	public Formulario(){
		super();
		this.modelo = new Modelo();
		configurarContenedor();
		configurarComponentes();
	}
	
	
	/**
	 * Configura el contenedor del Frame.
	 */
	public void configurarContenedor(){
		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		this.constraints = new GridBagConstraints();
		this.constraints.insets = new Insets(3, 3, 3, 3);        
		this.constraints.gridx = 0;        
		this.constraints.gridy = 0;
	}
	
	
	
	
	
	/**
	 * Configura los componentes de la aplicación: menus, paneles, etc.
	 */
	public void configurarComponentes(){
		//Inicializacion de los campos
		this.fecha = new JDateChooser();
		this.nombre = new JTextField(30);
		//Insercion de los campos en el panel
		this.addField("Fecha de entrada", this.fecha);
		this.addField("Nombre", this.nombre);
		JButton boton = new JButton("Insertar");
		boton.addActionListener(new ActionBoton());
		this.addField("",boton);
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
	
	

	
	
	/**
	 * Metodo que limpia los campos del formulario
	 */
	/*
	public void limpiar(){
		this.inicializarCampoFecha(this.fecha);
		this.nombre.setText("");
	}*/
	
	/**
	 * Inicializa el campo fecha que se pasa por parametro
	 * @param campo
	 */
	/*
	private void inicializarCampoFecha(JDateChooser campo){
		JSpinner.DateEditor editor = campo.getEditor();
		editor = new JSpinner.DateEditor(campo.getDateSpinner(), "");
        editor.setPreferredSize(new Dimension(80,13));
        campo.getDateSpinner().setEditor(editor);
	}*/
	
	
	
	
	
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	
	public class ActionBoton implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			try{
				GastoVO gasto = new GastoVO();
				gasto.setFecha(fecha.getDate());
				gasto.setNombre(StringUtil.nullToString(nombre.getText()));
				//modelo.insertGasto(gasto);
				JOptionPane.showMessageDialog(null, "La persona ha sido insertada correctamente",
						"OPERACIÓN EXITOSA",JOptionPane.INFORMATION_MESSAGE);
				//limpiar();
			} catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Se ha producido un error al insertar la persona: "+e.getMessage(),
						"ERROR DE ISNERCIÓN",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
}
