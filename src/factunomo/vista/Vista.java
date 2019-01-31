package factunomo.vista;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import factunomo.modelo.Modelo;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.utils.StringUtil;
import factunomo.vista.componentes.FormularioConsultaGastos;
import factunomo.vista.componentes.FormularioConsultaIngresos;
import factunomo.vista.componentes.FormularioDatosUsuario;
import factunomo.vista.componentes.FormularioInformes;
import factunomo.vista.componentes.FormularioInicio;


public class Vista  extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final String TEXTO_MENU_SALIR = "Salir";
	public static final String TEXTO_MENU_INFORMES = "Informes";
	public static final String TEXTO_MENU_DATOS = "Datos Usuario";
	public static final String TEXTO_MENU_CONSULTAR_INGRESOS = "Ingresos";
	public static final String TEXTO_MENU_CONSULTAR_GASTOS = "Gastos";

	private Container contenedor;
	private boolean config;
	
	
	/**
	 * Constructor por defecto.
	 */
	public Vista(){
		super("FACTUNOMO 1.0");
		this.config=getConfig();
		this.setIconImage (new ImageIcon("icono_factunomo.png").getImage());
		configurarContenedor();
		configurarComponentes();
		configurarLookAndFeel();
		configurarVista();
		cargarInicio();
		if (!config) cargarDatosUsuario();
	}
	
	
	/**
	 * Configura el contenedor del Frame.
	 */
	public void configurarContenedor(){
		contenedor = this.getContentPane();
		contenedor.setBackground(Color.WHITE);
		contenedor.setLayout(new BorderLayout(5,5));
	}
	
	
	/**
	 * Metodo que configura el Look&Feel de la aplicacion
	 */
	public void configurarLookAndFeel(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e){
			System.err.println("ERROR cargando L&F: " + e);
		}
	}
	
	/**
	 * Configura las dimensiones y propiedades del Frame.
	 */
	public void configurarVista(){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(d.width-10, d.height-50));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	/**
	 * Configura los componentes de la aplicación: menus, paneles, etc.
	 */
	public void configurarComponentes(){
		configurarMenu();
	}


	private void configurarMenu() {
		JToolBar barra = new JToolBar("Menu");
		JButton botonInformes = new JButton(TEXTO_MENU_INFORMES);
		botonInformes.addActionListener(new ListenerMenu());
		JButton botonConsEnvios = new JButton(TEXTO_MENU_CONSULTAR_INGRESOS);
		botonConsEnvios.addActionListener(new ListenerMenu());
		JButton botonConsPersonas = new JButton(TEXTO_MENU_CONSULTAR_GASTOS);
		botonConsPersonas.addActionListener(new ListenerMenu());
		JButton botonDatos = new JButton(TEXTO_MENU_DATOS);
		botonDatos.addActionListener(new ListenerMenu());
		JButton botonSalir = new JButton(TEXTO_MENU_SALIR);
		botonSalir.addActionListener(new ListenerMenu());
		JSeparator j = new JSeparator(SwingConstants.VERTICAL);
		j.setSize(50, 20);
		barra.add(botonInformes);
		barra.add(new JSeparator(SwingConstants.VERTICAL));
		barra.add(botonConsEnvios);
		barra.add(new JSeparator(SwingConstants.VERTICAL));
		barra.add(botonConsPersonas);
		barra.add(new JSeparator(SwingConstants.VERTICAL));
		barra.add(botonDatos);
		barra.add(new JSeparator(SwingConstants.VERTICAL));
		barra.add(botonSalir);
		this.add(barra,BorderLayout.PAGE_START);
		this.add(new JPanel(),BorderLayout.NORTH);
	}
	
	
	
	private void cargarDatosUsuario() {
		if (!config){
			dialogoConfig();
		}
		FormularioDatosUsuario form = new FormularioDatosUsuario();
		this.contenedor.remove(1);
		this.contenedor.add(form,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void cargarInicio() {
		FormularioInicio form = new FormularioInicio();
		this.contenedor.remove(1);
		this.contenedor.add(form,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	private void cargarConsultaIngresos() {
		this.config=getConfig();
		if (!config){
			dialogoConfig();
			return;
		}
		FormularioConsultaIngresos form = new FormularioConsultaIngresos(this);
		this.contenedor.remove(1);
		this.contenedor.add(form,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	private void cargarConsultaGastos() {
		this.config=getConfig();
		if (!config){
			dialogoConfig();
			return;
		}
		FormularioConsultaGastos form = new FormularioConsultaGastos(this);		
		this.contenedor.remove(1);
		this.contenedor.add(form,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void cargarFormularioInformes() {
		this.config=getConfig();
		if (!config){
			dialogoConfig();
			return;
		}
		FormularioInformes form = new FormularioInformes(this);
		this.contenedor.remove(1);
		this.contenedor.add(form,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	/**
	 * Verificar los datos de configuración
	 */
	public boolean getConfig()
	{
		Modelo modelo=new Modelo();
		ConfiguracionVO datos=modelo.getDatosFactunomo();
		if (StringUtil.isNullOrBlank(datos.getNombre()) || StringUtil.isNullOrBlank(datos.getNIF()) || StringUtil.isNullOrBlank(datos.getDireccion()) || StringUtil.isNullOrBlank(datos.getCiudad()) || StringUtil.isNullOrBlank(datos.getProvincia()))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Dialogo de solicitud de configuración
	 */
	public void dialogoConfig()
	{
		JOptionPane.showMessageDialog(
				   this,
				   "Debes configurar los datos del usuario para comenzar");
	}
	
	public void desactivarPanel(){
		this.setEnabled(false);
	}
	
	
	/**********************************************************************************/
	/**********************************************************************************/
	/**********************************************************************************/
	/**
	 * Clase para las acciones del menu
	 * @author Danius
	 *
	 */
	private class ListenerMenu implements  ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String evento = arg0.getActionCommand();
			if(evento.equals(TEXTO_MENU_CONSULTAR_INGRESOS)){
				cargarConsultaIngresos();
			} else if(evento.equals(TEXTO_MENU_CONSULTAR_GASTOS)){
				cargarConsultaGastos();
			} else if(evento.equals(TEXTO_MENU_DATOS)){
				cargarDatosUsuario();
			} else if(evento.equals(TEXTO_MENU_INFORMES)){
				cargarFormularioInformes();
			} else if(evento.equals(TEXTO_MENU_SALIR)){
				System.exit(0);
			}
		}
		
	}
}
