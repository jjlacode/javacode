package factunomo.vista.componentes;

import java.awt.BorderLayout;






import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.toedter.calendar.JDateChooser;

import factunomo.modelo.ModeloTablaIngresos;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FiltroVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.DetalleIngresoVO;
import factunomo.utils.DateUtils;
import factunomo.utils.StringUtil;


import factunomo.vista.componentes.FormularioConsultaIngresos.ActionBoton;
import factunomo.vista.componentes.decorators.TableRenderer;
import factunomo.vista.componentes.InformePDF;


public class FormularioInformes extends FormularioBase {


	private static final long serialVersionUID = 1L;

	/**
	 * Componentes
	 */
	private IngresoVO ingresoEditar;
	private ConfiguracionVO datosFactunomo;
	private ArrayList<DetalleIngresoVO> listaDetalleIngreso;
	private ArrayList<IngresoVO> listaIngresos;
	private FiltroVO filtroInforme;
	
	private JPanel panelBuscar;
	private JPanel panelDetalle;
	private JPanel panelSeleccion;
	private JPanel panelBusqueda;
	private JPanel panelLista;
	private JRadioButton opcion1;
	private JRadioButton opcion2;
	private JRadioButton opcion3;
	private JRadioButton opcion4;
	private JRadioButton opcion5;
	private JCheckBox informe1;
	private JCheckBox informe2;
	private JCheckBox informe3;
	private JCheckBox informe4;
	private JCheckBox informe5;
	private JCheckBox informe6;
	private ButtonGroup grupoInformes;

	private ButtonGroup grupoBotones;
	private JCheckBox todasFechas;
	private Integer seleccion;
	private JPanel panelDER;
	private JDateChooser desde_find;
	private JDateChooser hasta_find;
	private JTable ingresos;
	private JRadioButton sel1,sel2;
	//Edicion del gasto
	private JDateChooser fecha;
	private JTextField nombre;
	private JFrame principal;
	
	//combos
	private JComboBox cliente;
	private JComboBox anhos;
	
	// botones
	private JButton beditar;
	private JButton beliminar;
	private JButton bpdf;

	

	
	
	/**
	 * Constructor por defecto.
	 */
	public FormularioInformes(JFrame ventana) {
		principal = ventana;
		configurarContenedor();
		configurarComponentes();
		
	}
	
	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	          
	    }
	}
	
	public void configurarContenedor(){
		this.setLayout(new BorderLayout());
	}
	
	

	/**
	 * Configura los componentes de la aplicación: menus, paneles, etc.
	 */
	public void configurarComponentes() {

		
		// Inicializacion de los campos
		Date fechaHoy= new Date();
		this.filtroInforme = new FiltroVO();
		this.filtroInforme.setNombre("TODOS");
		this.filtroInforme.setFechaFinal(fechaHoy);
		this.filtroInforme.setFechaInicio(fechaHoy);
		escuchaCambioFecha escuchaFecha= new escuchaCambioFecha();
		this.desde_find = new JDateChooser();
		this.desde_find.setDate(fechaHoy);
		this.desde_find.addPropertyChangeListener(escuchaFecha);
		this.hasta_find = new JDateChooser();
		this.hasta_find.setDate(fechaHoy);
		this.hasta_find.addPropertyChangeListener(escuchaFecha);
		this.datosFactunomo = modelo.getDatosFactunomo();
		this.cliente = this.getComboClientes(1);
		AutoCompleteDecorator.decorate(this.cliente);
		this.cliente.addActionListener(new cambioCliente());
		addClienteRatonListener(cliente);
		this.anhos = this.getComboAnhos(1);
		AutoCompleteDecorator.decorate(this.anhos);
		this.anhos.addActionListener(new cambioAnhos());
		addAnhosRatonListener(anhos);
		
		this.seleccion = datosFactunomo.getPeriodo();
		this.todasFechas = new JCheckBox();
		this.todasFechas.addChangeListener(new todasFechasCL());
		

		//Titulo
		JPanel panelTitulo = new JPanel();
		panelTitulo.setLayout(new FlowLayout());
		JLabel titulo = new JLabel("<html><b>Creación de Informes</b></html>");
		Font auxFont=titulo.getFont();
		titulo.setFont(new Font(auxFont.getFontName(), auxFont.getStyle(), 20));
		titulo.setForeground(Color.BLUE);
		panelTitulo.add(titulo);
		this.add(panelTitulo,BorderLayout.NORTH);
		
		
		//Panel búsqueda
		this.panelBuscar = new JPanel();
		this.panelBuscar.setLayout(new GridBagLayout());
		GridBagConstraints cst = new GridBagConstraints();
		cst.fill = GridBagConstraints.CENTER;
		cst.gridx = 0;
		cst.gridy = 0;
		
		
		//Opcion de seleccion
		JPanel selector = new JPanel();
		selector.setLayout(new FlowLayout());
		sel1 = new JRadioButton("Selección", true);
		sel1.addActionListener(new Sel1AL());
		sel2 = new JRadioButton("Búsqueda", true);
		sel2.addActionListener(new Sel2AL());
		ButtonGroup grupoSeleccion = new ButtonGroup();
		grupoSeleccion.add(sel1);
		grupoSeleccion.add(sel2);
		selector.add(sel1,cst);
		selector.add(sel2,cst);
		this.panelBuscar.add(selector,cst);
		cst.gridy++;
		
		//selección
		panelSeleccion = new JPanel();
		panelSeleccion.setLayout(new FlowLayout());
		panelSeleccion.add(new JLabel("<html><b>Año</b></html>"));
		panelSeleccion.add(this.anhos);
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
		opcion5 = new JRadioButton( "Todo");
		opcion5.setActionCommand("0");
		panelSeleccion.add( opcion5 );
		grupoBotones = new ButtonGroup();
		grupoBotones.add(opcion1);
		grupoBotones.add(opcion2);
		grupoBotones.add(opcion3);
		grupoBotones.add(opcion4);
		grupoBotones.add(opcion5);
		// Register a listener for the radio buttons.
        RadioListener escuchaPeriodo = new RadioListener();
        opcion1.addActionListener(escuchaPeriodo);
        opcion2.addActionListener(escuchaPeriodo);
        opcion3.addActionListener(escuchaPeriodo);
        opcion4.addActionListener(escuchaPeriodo);
        opcion5.addActionListener(escuchaPeriodo);
		this.panelBuscar.add(panelSeleccion,cst);
		cst.gridy++;
		
		// PANEL CLIENTE
		JPanel panelCliente = new JPanel();
		panelCliente.setLayout(new FlowLayout());
		panelCliente.add(new JLabel("<html><b>Cliente</b></html>"));
		panelCliente.add(this.cliente);		
		this.panelBuscar.add(panelCliente,cst);
		cst.gridy++;
		
		// PANEL FECHAS
		JPanel panelFechas = new JPanel();
		panelFechas.setLayout(new FlowLayout());
		panelFechas.add(new JLabel("<html><b>Desde</b></html>"));
		panelFechas.add(this.desde_find);
		panelFechas.add(new JLabel("<html><b>Hasta</b></html>"));
		panelFechas.add(this.hasta_find);
		panelFechas.add(new JLabel("<html><b>Todo</b></html>"));
		panelFechas.add(this.todasFechas);
		
		this.panelBuscar.add(panelFechas,cst);
		cst.gridy++;
		
		// PANEL INFORME
		JPanel panelInforme = new JPanel();
		panelInforme.setLayout(new GridBagLayout());
		GridBagConstraints cinf = new GridBagConstraints();
		cinf.gridx=0;
		cinf.gridy=0;
		
		informe1 = new JCheckBox("Ingresos",true);
		panelInforme.add( informe1,cinf);
		cinf.gridx++;
		informe2 = new JCheckBox( "Gastos",true);
		panelInforme.add( informe2,cinf);
		cinf.gridx=0;
		cinf.gridy++;
		cinf.gridwidth=2;
		informe3 = new JCheckBox( "Listado",true);
		panelInforme.add( informe3,cinf);
		cinf.gridy++;
		informe4 = new JCheckBox( "Resumen IVA");
		panelInforme.add( informe4,cinf);
		cinf.gridy++;
		informe5 = new JCheckBox( "Resumen IRPF");
		panelInforme.add( informe5,cinf);
		cinf.gridy++;
		informe6 = new JCheckBox( "Balance");
		panelInforme.add( informe6,cinf);
		
		this.panelBuscar.add(panelInforme,cst);
		cst.gridy++;
		
		// Botón Obtener informe
		JButton obtener=new JButton("Obtener Informe");
		obtener.addActionListener(new ActionBoton());
		this.panelBuscar.add(obtener,cst);
		cst.gridy++;
		
		// PANEL CONTENIDO COMPLETO
		JPanel panelContenido = new JPanel();
		panelContenido.setLayout(new GridLayout(1,2));
		GridBagLayout ccon = new GridBagLayout();
		ccon.columnWidths = new int[]{800,50};
		panelContenido.add(this.panelBuscar,ccon);
		this.add(panelContenido,BorderLayout.CENTER);
		panelContenido.setBorder(new EmptyBorder(0,50,0,0));
		
		//Paneles para dejar huecos en las partes de abajo, izquierda y derecha
		JPanel aux = new JPanel();
		aux.setSize(new Dimension(100,30));
		aux.setPreferredSize(new Dimension(100,30));
		this.add(aux,BorderLayout.SOUTH);
		JPanel aux2 = new JPanel();
		aux2.setSize(new Dimension(30,30));
		aux2.setPreferredSize(new Dimension(30,30));
		this.add(aux2,BorderLayout.WEST);
		this.add(aux2,BorderLayout.EAST);
		
		//Colocar ejercicio y periodo actuales
		this.anhos.setSelectedItem((Object) String.valueOf(datosFactunomo.getEjercicio()));
		
		switch (seleccion){
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
		
		desde_find.setDate(modelo.fechaInicio(datosFactunomo.getEjercicio(), datosFactunomo.getPeriodo()));
    	hasta_find.setDate(modelo.fechaFinal(datosFactunomo.getEjercicio(), datosFactunomo.getPeriodo()));
    	filtroInforme.setFechaInicio(desde_find.getDate());
		filtroInforme.setFechaFinal(hasta_find.getDate());
		filtroInforme.setNombre("TODOS");
				
		this.cliente.setEnabled(false);
		this.desde_find.setEnabled(false);
		this.hasta_find.setEnabled(false);
		this.todasFechas.setEnabled(false);

	}

	
	
	public void actualizarBusqueda(int e){
		seleccion = e;
		if (!anhos.getSelectedItem().equals("TODOS")){
			todasFechas.setSelected(false);
			cliente.setSelectedItem((Object)"TODOS");
			if (e > 0){
				desde_find.setDate(modelo.fechaInicio(Integer.parseInt((String)anhos.getSelectedItem()), e));
				hasta_find.setDate(modelo.fechaFinal(Integer.parseInt((String)anhos.getSelectedItem()), e));		
			} else {
				desde_find.setDate(modelo.fechaInicio(Integer.parseInt((String)anhos.getSelectedItem()), 1));
				hasta_find.setDate(modelo.fechaFinal(Integer.parseInt((String)anhos.getSelectedItem()), 4));
			}
		} else {
			todasFechas.setSelected(true);
		}
	}


	

	
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/


	/**
	 * Escucha cambios de fecha
	 */
	public class escuchaCambioFecha implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent e){
			filtroInforme.setFechaInicio(desde_find.getDate());
			filtroInforme.setFechaFinal(hasta_find.getDate());
			filtroInforme.setNombre((String) cliente.getSelectedItem());	
		}
	}
	
	/**
	 * Escucha radiobotones periodos
	 * @author sergio
	 *
	 */
	class RadioListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			actualizarBusqueda(Integer.parseInt(e.getActionCommand()));
		}
	}
	
	/**
	 * Escucha combo de clientes enter
	 * @author sergio
	 *
	 */
	class cambioCliente implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			JComboBox source = (JComboBox) event.getSource();
			if(!source.isPopupVisible()){
				filtroInforme.setNombre((String)cliente.getSelectedItem()); 
			}
		}       
	}

	/**
	 * Añadir Escucha combo clientes raton
	 * @param box
	 */
	private void addClienteRatonListener(JComboBox box) {
		try {
			Field popupInBasicComboBoxUI = BasicComboBoxUI.class.getDeclaredField("popup");
			popupInBasicComboBoxUI.setAccessible(true);
			BasicComboPopup popup = (BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI());

			Field scrollerInBasicComboPopup = BasicComboPopup.class.getDeclaredField("scroller");
			scrollerInBasicComboPopup.setAccessible(true);
			JScrollPane scroller = (JScrollPane) scrollerInBasicComboPopup.get(popup);

			scroller.getViewport().getView().addMouseListener(EscuchaClienteRaton());
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escucha del combo de clientes raton
	 * @return
	 */
	private MouseAdapter EscuchaClienteRaton() {
		return new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				//System.out.println("mouseClicked");
			}

			public void mousePressed(MouseEvent mouseEvent) {
				//System.out.println("mousePressed");
			}

			public void mouseReleased(MouseEvent mouseEvent) {
				filtroInforme.setNombre((String)cliente.getSelectedItem());
			}

			public void mouseEntered(MouseEvent mouseEvent) {
				//System.out.println("mouseEntered");
			}

			public void mouseExited(MouseEvent mouseEvent) {
				//System.out.println("mouseExited");
			}
		};
	}
	
	/**
	 * Escucha opcion todasFechas
	 * @author sergio
	 *
	 */
	class todasFechasCL implements ChangeListener {
		public void stateChanged(ChangeEvent evento){
			if (todasFechas.isSelected())
			{
				desde_find.setDate(modelo.primeraFecha("INGRESOS"));
				hasta_find.setDate(modelo.ultimaFecha("INGRESOS"));
				desde_find.setEnabled(false);
				hasta_find.setEnabled(false);
			} else if (sel2.isSelected()){
				desde_find.setEnabled(true);
				hasta_find.setEnabled(true);
			}
		}
	}

	/**
	 * Escucha seleccion
	 * @author sergio
	 *
	 */
	class Sel1AL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			cliente.setEnabled(false);
			desde_find.setEnabled(false);
			hasta_find.setEnabled(false);
			todasFechas.setEnabled(false);
			anhos.setEnabled(true);
			if (!anhos.getSelectedItem().equals("TODOS"))
			{
				opcion1.setEnabled(true);
				opcion2.setEnabled(true);
				opcion3.setEnabled(true);
				opcion4.setEnabled(true);
			}
			opcion5.setEnabled(true);		
			actualizarBusqueda(seleccion);
		}
	}
	class Sel2AL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			cliente.setEnabled(true);
			desde_find.setEnabled(true);
			hasta_find.setEnabled(true);
			todasFechas.setEnabled(true);
			todasFechas.setSelected(false);
			anhos.setEnabled(false);
			opcion1.setEnabled(false);
			opcion2.setEnabled(false);
			opcion3.setEnabled(false);
			opcion4.setEnabled(false);
			opcion5.setEnabled(false);
		}
	}
	
	/**
	 * Escucha botón Obtener Informe
	 * @author sergio
	 *
	 */
	public class ActionBoton implements ActionListener {

		public void actionPerformed(ActionEvent ev) {
			String evento = ev.getActionCommand();
			if(evento.equals("Obtener Informe")){
				if (!informe1.isSelected()&&!informe2.isSelected()){
					int result = JOptionPane.showConfirmDialog(principal,
							"Al menos un tipo de datos debe estar seleccionado:\nIngresos o Gastos", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (!informe3.isSelected()&&!informe4.isSelected()&&!informe5.isSelected()&&!informe6.isSelected()){
					int result = JOptionPane.showConfirmDialog(principal,
							"Al menos un componente del informe debe estar seleccionado:\nListado, Resumen IVA, Resumen IRPF o Balance", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}	
				try {
					new InformePDF(filtroInforme,informe1.isSelected(),informe2.isSelected(),informe3.isSelected(),informe4.isSelected(),informe5.isSelected(),informe6.isSelected());
					new verPDF ("Informe", principal);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"Se ha producido un error al crear el informe: "+ e.getMessage(), 
							"ERROR DE CREACIÓN",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Accion cambio combo de años
	 */
	private void accionCambioAnhos(){
		if (anhos.getSelectedItem().equals("TODOS")){
			opcion1.setEnabled(false);
			opcion2.setEnabled(false);
			opcion3.setEnabled(false);
			opcion4.setEnabled(false);
			opcion5.setSelected(true);
		} else {
			opcion1.setEnabled(true);
			opcion2.setEnabled(true);
			opcion3.setEnabled(true);
			opcion4.setEnabled(true);
			if (opcion5.isSelected()){
				seleccion=5;
			}
		}		
		actualizarBusqueda(seleccion);
	}
	
	/**
	 * Escucha combo de años enter
	 * @author sergio
	 *
	 */
	class cambioAnhos implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			JComboBox source = (JComboBox) event.getSource();
			if(!source.isPopupVisible()){
				accionCambioAnhos();
			}
		}       
	}

	/**
	 * Añadir Escucha combo años raton
	 * @param box
	 */
	private void addAnhosRatonListener(JComboBox box) {
		try {
			Field popupInBasicComboBoxUI = BasicComboBoxUI.class.getDeclaredField("popup");
			popupInBasicComboBoxUI.setAccessible(true);
			BasicComboPopup popup = (BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI());

			Field scrollerInBasicComboPopup = BasicComboPopup.class.getDeclaredField("scroller");
			scrollerInBasicComboPopup.setAccessible(true);
			JScrollPane scroller = (JScrollPane) scrollerInBasicComboPopup.get(popup);

			scroller.getViewport().getView().addMouseListener(EscuchaAnhoRaton());
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escucha del combo de años raton
	 * @return
	 */
	private MouseAdapter EscuchaAnhoRaton() {
		return new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				//System.out.println("mouseClicked");
			}

			public void mousePressed(MouseEvent mouseEvent) {
				//System.out.println(anhos.getSelectedItem());
			}

			public void mouseReleased(MouseEvent mouseEvent) {
				//System.out.println("liberado");
				accionCambioAnhos();
			}

			public void mouseEntered(MouseEvent mouseEvent) {
				//System.out.println("mouseEntered");
			}

			public void mouseExited(MouseEvent mouseEvent) {
				//System.out.println("mouseExited");
			}
		};
	}
	
}
