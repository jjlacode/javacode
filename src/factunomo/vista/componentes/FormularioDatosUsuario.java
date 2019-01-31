package factunomo.vista.componentes;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.utils.StringUtil;
import factunomo.vista.componentes.FormularioInformes.RadioListener;
import factunomo.vista.componentes.FormularioInformes.cambioAnhos;

public class FormularioDatosUsuario extends FormularioBase {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Componentes
	 */
	private JTextField nombre;
	private JTextField NIF;
	private JTextField direccion;
	private JTextField CP;
	private JTextField localidad;
	private JTextField provincia;
	private JTextField web;
	private JTextField email;
	private JTextField telefono;
	private JTextField IVAGeneral;
	private JTextField IVAReducido;
	private JTextField IVASuperReducido;
	private JTextField numeroCuenta1;
	private JTextField numeroCuenta2;
	private JTextField numeroCuenta3;
	private JTextField IRPF;
	private JRadioButton opcion1;
	private JRadioButton opcion2;
	private JRadioButton opcion3;
	private JRadioButton opcion4;
	private ButtonGroup grupoBotones;
	private JTextField periodo;
	private JComboBox anhos;
	
	
	/**
	 * Constructor por defecto.
	 */
	public FormularioDatosUsuario(){
		super();
		configurarContenedor();
		configurarComponentes();
	}	
	
	
	
	/**
	 * Configura los componentes de la aplicación: menus, paneles, etc.
	 */
	public void configurarComponentes(){
		ConfiguracionVO datos = this.modelo.getDatosFactunomo();
		//Inicializacion de los campos
		this.nombre = new JTextField(30);
		this.nombre.setText(datos != null ? datos.getNombre() : "");
		this.NIF = new JTextField(12);
		this.NIF.setText(datos != null ? datos.getNIF() : "");
		this.direccion = new JTextField(50);
		this.direccion.setText(datos != null ? datos.getDireccion() : "");
		this.CP = new JTextField(6);
		this.CP.setText(datos != null ? datos.getCP() : "");
		this.localidad = new JTextField(15);
		this.localidad.setText(datos != null ? datos.getCiudad() : "");
		this.provincia = new JTextField(15);
		this.provincia.setText(datos != null ? datos.getProvincia() : "");
		this.web = new JTextField(50);
		this.web.setText(datos != null ? datos.getWeb() : "");
		this.email = new JTextField(50);
		this.email.setText(datos != null ? datos.getEmail() : "");
		this.telefono = new JTextField(16);
		this.telefono.setText(datos != null ? datos.getTelefono() : "");
		this.IVAGeneral = new JTextField(6);
		this.IVAGeneral.setText(datos != null ? Decimales(datos.getIVAGeneral()) : "");
		this.IVAReducido = new JTextField(6);
		this.IVAReducido.setText(datos != null ? Decimales(datos.getIVAReducido()) : "");
		this.IVASuperReducido = new JTextField(6);
		this.IVASuperReducido.setText(datos != null ? Decimales(datos.getIVASuperReducido()) : "");
		this.numeroCuenta1 = new JTextField(25);
		this.numeroCuenta1.setText(datos != null ? datos.getNumeroCuenta1() : "");
		this.numeroCuenta2 = new JTextField(25);
		this.numeroCuenta2.setText(datos != null ? datos.getNumeroCuenta2() : "");
		this.numeroCuenta3 = new JTextField(25);
		this.numeroCuenta3.setText(datos != null ? datos.getNumeroCuenta3() : "");
		this.IRPF = new JTextField(6);
		this.IRPF.setText(datos != null ? Decimales(datos.getIRPF()) : "");
		this.periodo = new JTextField(50);
		this.periodo.setText(datos != null ? String.valueOf(datos.getPeriodo()) : "");
		this.anhos = this.getComboAnhos(0);
		AutoCompleteDecorator.decorate(this.anhos);
		//this.pass = new JPasswordField(15);
		//this.pass.setText(datos != null ? datos.getPass() : "");
		//Insercion de los campos en el panel
		this.addTitulo("<html><b>Datos del Usuario</b></html>");
		//this.addField("", new JLabel("   "));
		this.addField("Nombre", this.nombre);
		this.addField("CIF", this.NIF);
		this.addField("Dirección", this.direccion);
		this.addField("Código postal", this.CP);
		this.addField("Localidad", this.localidad);
		this.addField("Provincia", this.provincia);
		this.addField("Página web", this.web);
		this.addField("Email", this.email);
		this.addField("Teléfono", this.telefono);
		this.addField("IVA General", this.IVAGeneral);
		this.addField("IVA Reducido", this.IVAReducido);
		this.addField("IVA Superreducido", this.IVASuperReducido);
		this.addField("Número de cuenta 1", this.numeroCuenta1);
		this.addField("Número de cuenta 2", this.numeroCuenta2);
		this.addField("Número de cuenta 3", this.numeroCuenta3);
		this.addField("IRPF", this.IRPF);
		this.addField("Ejercicio", this.anhos);
		// Selección periodo
		JPanel panelSeleccion = new JPanel();
		panelSeleccion.setLayout(new FlowLayout());
		opcion1 = new JRadioButton( "T1");
		opcion1.setActionCommand("1");
		panelSeleccion.add( opcion1 );
		opcion2 = new JRadioButton( "T2");
		opcion2.setActionCommand("2");
		panelSeleccion.add( opcion2 );		
		opcion3 = new JRadioButton( "T3");
		opcion3.setActionCommand("3");
		panelSeleccion.add( opcion3 );
		opcion4 = new JRadioButton( "T4");
		opcion4.setActionCommand("4");
		panelSeleccion.add( opcion4 );
		grupoBotones = new ButtonGroup();
		grupoBotones.add(opcion1);
		grupoBotones.add(opcion2);
		grupoBotones.add(opcion3);
		grupoBotones.add(opcion4);
		this.addField("Periodo", panelSeleccion);
		JButton boton = new JButton("Guardar");
		boton.addActionListener(new ActionBoton());
		this.addField("",boton);
		
		//Colocar ejercicio y periodo actuales
		if (!datos.getEjercicio().equals(0)) this.anhos.setSelectedItem((Object) datos.getEjercicio());
		int periodo=datos.getPeriodo();
		if (periodo==0) periodo=modelo.getPeriodo(new Date());
		switch (periodo){
		case 1:
			opcion1.setSelected(true);
			break;
		case 2:
			opcion2.setSelected(true);
			break;
		case 3:
			opcion3.setSelected(true);
			break;
		case 4:
			opcion4.setSelected(true);
			break;
		}
}

	
	private String validar(){
		String resultado = "";
		if(StringUtil.isNullOrBlank(this.nombre.getText())){
			resultado = "- El campo Nombre es obligatorio.\n";
		}
		if(StringUtil.isNullOrBlank(this.NIF.getText())){
			resultado += "- El campo NIF es obligatorio.\n";
		}
		if(StringUtil.isNullOrBlank(this.direccion.getText())){
			resultado += "- El campo Dirección es obligatorio.\n";
		}
		if(StringUtil.isNullOrBlank(this.CP.getText())){
			resultado += "- El campo Código postal es obligatorio.\n";
		}
		if(StringUtil.isNullOrBlank(this.localidad.getText())){
			resultado += "- El campo Localidad es obligatorio.\n";
		}
		if(StringUtil.isNullOrBlank(this.provincia.getText())){
			resultado += "- El campo Provincia es obligatorio.\n";
		}
		/*
		if(this.pass.getPassword().length == 0){
			resultado += "- El campo Contraseña es obligatorio.";
		}
		*/
		return resultado;
	}
	
	
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	
		
	/**
	 * Escucha botón Guardar
	 * @author sergio
	 *
	 */
	public class ActionBoton implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			try{
				String validacion = validar();
				if(StringUtil.isNullOrBlank(validacion)){
					ConfiguracionVO datos = new ConfiguracionVO();
					datos.setNombre(nombre.getText());
					datos.setNIF(NIF.getText());
					datos.setDireccion(direccion.getText());
					datos.setCP(CP.getText());
					datos.setCiudad(localidad.getText());
					datos.setProvincia(provincia.getText());
					datos.setWeb(web.getText());
					datos.setEmail(email.getText());
					datos.setTelefono(telefono.getText());
					String cadena=IVAGeneral.getText();
					cadena=cadena.replace(",",".");
					datos.setIVAGeneral(Float.parseFloat(cadena));
					cadena=IVAReducido.getText();
					cadena=cadena.replace(",",".");
					datos.setIVAReducido(Float.parseFloat(cadena));
					cadena=IVASuperReducido.getText();
					cadena=cadena.replace(",",".");
					datos.setIVASuperReducido(Float.parseFloat(cadena));
					datos.setNumeroCuenta1(numeroCuenta1.getText());
					datos.setNumeroCuenta1(numeroCuenta1.getText());
					datos.setNumeroCuenta1(numeroCuenta1.getText());
					cadena=IRPF.getText();
					cadena=cadena.replace(",",".");
					datos.setIRPF(Float.parseFloat(cadena));
					cadena=String.valueOf(anhos.getSelectedItem());
					datos.setEjercicio(Integer.parseInt(cadena));
					int ejer=0;
					if (opcion1.isSelected()) ejer=1;
					if (opcion2.isSelected()) ejer=2;
					if (opcion3.isSelected()) ejer=3;
					if (opcion4.isSelected()) ejer=4;
					datos.setPeriodo(ejer);
					//datos.setPass(new String(pass.getPassword()));
					modelo.actualizarDatosUsuario(datos);
					JOptionPane.showMessageDialog(null, "Los Datos del Usuario han sido actualizados correctamente.",
							"OPERACIÓN EXITOSA",JOptionPane.INFORMATION_MESSAGE);
				} else{
					JOptionPane.showMessageDialog(null, validacion,
							"ERROR DE VALIDACIÓN",JOptionPane.ERROR_MESSAGE);
				}
			} catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos del establecimiento: "+e.getMessage(),
						"ERROR DE ACTUALIZACIÓN",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}
}
