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

import factunomo.modelo.ModeloTablaGastos;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FiltroVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.DetalleGastoVO;
import factunomo.utils.DateUtils;
import factunomo.utils.StringUtil;


import factunomo.vista.componentes.decorators.TableRenderer;
import factunomo.vista.componentes.ImprimePDF;


public class FormularioConsultaGastos extends FormularioBase {


	private static final long serialVersionUID = 1L;

	/**
	 * Componentes
	 */
	private GastoVO gastoEditar;
	private ConfiguracionVO datosFactunomo;
	private ArrayList<DetalleGastoVO> listaDetalleGasto;
	private ArrayList<GastoVO> listaGastos;
	private FiltroVO filtroGasto;
	
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
	private ButtonGroup grupoBotones;
	private JCheckBox todasFechas;
	private Integer seleccion;
	private JPanel panelDER;
	private JDateChooser desde_find;
	private JDateChooser hasta_find;
	private JTable gastos;
	private JRadioButton sel1,sel2;
	//Edicion del gasto
	private JDateChooser fecha;
	private JTextField nombre;
	private JFrame principal;
	
	//combos
	private JComboBox proveedor;
	private JComboBox anhos;
	
	// botones
	private JButton beditar;
	private JButton beliminar;
	private JButton bpdf;

	

	
	
	/**
	 * Constructor por defecto.
	 */
	public FormularioConsultaGastos(JFrame ventana) {
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
		this.filtroGasto = new FiltroVO();
		this.filtroGasto.setNombre("TODOS");
		this.filtroGasto.setFechaFinal(fechaHoy);
		this.filtroGasto.setFechaInicio(fechaHoy);
		escuchaCambioFecha escuchaFecha= new escuchaCambioFecha();
		this.desde_find = new JDateChooser();
		this.desde_find.setDate(fechaHoy);
		this.desde_find.addPropertyChangeListener(escuchaFecha);
		this.hasta_find = new JDateChooser();
		this.hasta_find.setDate(fechaHoy);
		this.hasta_find.addPropertyChangeListener(escuchaFecha);
		ModeloTablaGastos modeloTabla = new ModeloTablaGastos();
		this.gastos = new JTable(modeloTabla);
		this.gastos.setDefaultRenderer(Object.class, new TableRenderer());
		this.gastos.addMouseListener(new MouseTablaGastos());
		gastos.getSelectionModel().addListSelectionListener(new EscuchaTabla());
		this.datosFactunomo = modelo.getDatosFactunomo();
		this.proveedor = this.getComboProveedores(1);
		AutoCompleteDecorator.decorate(this.proveedor);
		this.proveedor.addActionListener(new cambioProveedor());
		addProveedorRatonListener(proveedor);
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
		JLabel titulo = new JLabel("<html><b>Consulta de Gastos</b></html>");
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
		
		
		
		//Botones superiores
		JPanel panelForm3 = new JPanel();
		panelForm3.setLayout(new FlowLayout());
		JButton boton3 = new JButton("Nuevo");
		boton3.addActionListener(new ActionBoton());
		panelForm3.add(boton3);
		beliminar = new JButton("Eliminar");
		beliminar.addActionListener(new ActionBoton());
		panelForm3.add(beliminar);
		this.panelBuscar.add(panelForm3,cst);
		cst.gridy++;
		
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

		//Buscar
		panelBusqueda = new JPanel();
		panelBusqueda.setLayout(new GridBagLayout());
		GridBagConstraints cbus = new GridBagConstraints();
		cbus.fill = GridBagConstraints.BOTH;
		cbus.gridx = 0;
		cbus.gridy = 0;
		
		JPanel panelProveedor = new JPanel();
		panelProveedor.setLayout(new FlowLayout());
		panelProveedor.add(new JLabel("<html><b>Proveedor</b></html>"));
		panelProveedor.add(this.proveedor);		
		panelBusqueda.add(panelProveedor,cbus);
		cbus.gridy++;
		JPanel panelFechas = new JPanel();
		panelFechas.setLayout(new FlowLayout());
		panelFechas.add(new JLabel("<html><b>Desde</b></html>"));
		panelFechas.add(this.desde_find);
		panelFechas.add(new JLabel("<html><b>Hasta</b></html>"));
		panelFechas.add(this.hasta_find);
		panelFechas.add(new JLabel("<html><b>Todo</b></html>"));
		panelFechas.add(this.todasFechas);
		
		panelBusqueda.add(panelFechas,cbus);
		this.panelBuscar.add(panelBusqueda,cst);
		cst.gridy++;

				
		//Listado
		this.panelLista = new JPanel();
		visualizarLista();
		cst.fill = GridBagConstraints.BOTH;
		cst.weightx = 1;
		cst.weighty = 1;
		
		this.panelBuscar.add(panelLista,cst);

		//Panel detalle
		
		this.panelDER = new JPanel();
		this.panelDetalle = new JPanel();
		panelDER.setLayout(new BorderLayout());
		
		
		// Botones
		JPanel panelForm5 = new JPanel();
		panelForm5.setLayout(new FlowLayout());
		beditar = new JButton("Editar");
		beditar.addActionListener(new ActionBoton());
		panelForm5.add(beditar);
		bpdf = new JButton("PDF");
		bpdf.addActionListener(new ActionBoton());
		panelForm5.add(bpdf);
		beditar.setEnabled(false);
		bpdf.setEnabled(false);
		this.panelDER.add(panelForm5,BorderLayout.NORTH);
		this.panelDER.add(panelDetalle,BorderLayout.CENTER);
		
		//Panel contenido
		JPanel panelContenido = new JPanel();
		panelContenido.setLayout(new GridLayout(1,2));
		GridBagLayout ccon = new GridBagLayout();
		ccon.columnWidths = new int[]{800,50};
		panelContenido.add(this.panelBuscar,ccon);
		panelContenido.add(this.panelDER,ccon);
		this.add(panelContenido,BorderLayout.CENTER);
		panelContenido.setBorder(new EmptyBorder(0,50,0,0));
		panelDER.setBorder(new EmptyBorder(0,50,0,0));
		
		
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
		filtroGasto.setFechaInicio(desde_find.getDate());
		filtroGasto.setFechaFinal(hasta_find.getDate());
		filtroGasto.setNombre("TODOS");
		this.proveedor.setEnabled(false);
		this.desde_find.setEnabled(false);
		this.hasta_find.setEnabled(false);
		this.todasFechas.setEnabled(false);
		this.beliminar.setEnabled(false);
		llenaLista(filtroGasto);
	}

	
	
	
	
	
	public void actualizarDetalleGasto(){
		panelDetalle.removeAll();
		visualizarGasto();
		panelDetalle.updateUI();
	}
	
	public void actualizarLista(){
		panelLista.removeAll();
		visualizarLista();
		panelLista.updateUI();
	}
	
	public void actualizarBusqueda(int e){
		seleccion = e;
		if (!anhos.getSelectedItem().equals("TODOS")){
			todasFechas.setSelected(false);
			proveedor.setSelectedItem((Object)"TODOS");
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

	public void visualizarLista(){
		panelLista.setLayout(new BorderLayout());
		gastos.getColumnModel().getColumn(1).setMinWidth(200);
		JScrollPane lista = new JScrollPane(this.gastos);	
		this.panelLista.add(lista);
		
	}

	public void visualizarGasto(){
		panelDetalle.setLayout(new GridBagLayout());
		GridBagConstraints ct = new GridBagConstraints();
		ct.insets = new Insets(3, 3, 3, 3);        
		ct.gridx = 0;
		ct.gridy = 0;
		ct.weightx = 1;
		ct.weighty = 0;
		ct.fill = GridBagConstraints.NONE;
		ct.anchor = GridBagConstraints.LINE_START;

		// Llenar lista de detalles del gasto
		if (gastoEditar!=null)
		{
			listaDetalleGasto = llenaListaDetalle(this.gastoEditar.getIdGasto());
			Float baseImponible = 0.00F;

			ct.gridx++;
			panelDetalle.add(new JLabel("GASTO"),ct);
			ct.gridx=3;
			panelDetalle.add(new JLabel("Nombre:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+this.gastoEditar.getNombre()+"</b></html>"),ct);
			ct.gridy++;
			ct.gridx = 0;
			panelDetalle.add(new JLabel("Fecha:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+DateUtils.dateToString(this.gastoEditar.getFecha())+"</b></html>"),ct);
			ct.gridx=3;
			panelDetalle.add(new JLabel("Dirección:"),ct);
			ct.gridx++;
			if (this.gastoEditar.getDireccion()!=null)
			{
				panelDetalle.add(new JLabel("<html><b>"+this.gastoEditar.getDireccion()+"</b></html>"),ct);
			}
			ct.gridy++;
			ct.gridx = 0;
			panelDetalle.add(new JLabel("Número:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+this.gastoEditar.getIdGasto()+"</b></html>"),ct);
			ct.gridx=3;
			panelDetalle.add(new JLabel("CIF:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+this.gastoEditar.getNIF()+"</b></html>"),ct);
			ct.gridy++;
			ct.gridx = 0;
			panelDetalle.add(new JLabel("   "),ct);
			ct.gridy++;
			// Cabecera detalle
			ct.gridx = 0;
			panelDetalle.add(new JLabel("Descripción:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("Precio:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("Cantidad:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("IVA:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("Subtotal:"),ct);
			ct.gridx++;
			ct.gridy++;
			Float subTotal = null;
			for(int i = 0;i<listaDetalleGasto.size();i++){
				ct.gridx = 0;
				panelDetalle.add(new JLabel("<html><b>"+listaDetalleGasto.get(i).getDescripcion()+"</b></html>"),ct);
				ct.gridx++;
				panelDetalle.add(new JLabel("<html><b>"+listaDetalleGasto.get(i).getPrecio()+"</b></html>"),ct);
				ct.gridx++;
				panelDetalle.add(new JLabel("<html><b>"+listaDetalleGasto.get(i).getCantidad()+"</b></html>"),ct);
				ct.gridx++;
				panelDetalle.add(new JLabel("<html><b>"+listaDetalleGasto.get(i).getIVA()+"</b></html>"),ct);
				ct.gridx++;
				subTotal = listaDetalleGasto.get(i).getPrecio()*listaDetalleGasto.get(i).getCantidad();
				panelDetalle.add(new JLabel("<html><b>"+modelo.Decimales(subTotal)+"</b></html>"),ct);
				ct.gridy++;
				baseImponible = baseImponible + subTotal;
			}
			ct.gridx = 0;
			panelDetalle.add(new JLabel("   "),ct);
			ct.gridy++;
			ct.gridx = 3;
			panelDetalle.add(new JLabel("Base Imponible:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+modelo.Decimales(baseImponible)+"</b></html>"),ct);
			ct.gridy++;
			String cadena = "";
			subTotal = baseImponible;
			Float cantidad = null;
			// Descuento
			if (this.gastoEditar.getDescuento()!=0.0)
			{
				ct.gridx = 0;
				cadena = "Descuento "+this.gastoEditar.getDescuento()+"%:";
				panelDetalle.add(new JLabel(cadena),ct);
				ct.gridx=3;
				cantidad = this.gastoEditar.getDescuento()*baseImponible/100;
				cadena = "-"+ modelo.Decimales(cantidad);
				panelDetalle.add(new JLabel("<html><b>"+cadena+"</b></html>"),ct);
				ct.gridx++;
				baseImponible-= cantidad;
				cadena = "" + modelo.Decimales(baseImponible);
				subTotal = baseImponible;
				panelDetalle.add(new JLabel("<html><b>"+cadena+"</b></html>"),ct);
				ct.gridy++;
			}
			// IVA
			Float[] IVA=new Float[3];
			Float[] base = new Float[3];
			IVA[0]=this.gastoEditar.getIVA1();
			IVA[1]=this.gastoEditar.getIVA2();
			IVA[2]=this.gastoEditar.getIVA3();
			base[0]=this.gastoEditar.getBaseImponible1();
			base[1]=this.gastoEditar.getBaseImponible2();
			base[2]=this.gastoEditar.getBaseImponible3();
			for (int i=0; i<IVA.length;i++){
				if (IVA[i]>0f)
				{
					ct.gridx = 0;
					cadena = "IVA "+IVA[i]+"% sobre "+modelo.Decimales(base[i]);
					panelDetalle.add(new JLabel(cadena),ct);
					ct.gridx=3;
					cantidad = IVA[i]*base[i]/100;
					cadena = ""+ modelo.Decimales(cantidad);
					panelDetalle.add(new JLabel("<html><b>"+cadena+"</b></html>"),ct);
					ct.gridx++;
					subTotal=subTotal+cantidad;
					cadena = "" + modelo.Decimales(subTotal);
					panelDetalle.add(new JLabel("<html><b>"+ cadena +"</b></html>"),ct);
					ct.gridy++;
				}
			}
			
			// IRPF
			if (gastoEditar.getTipoIRPF()>0f)
			{
				ct.gridx=0;
				cadena = "IRPF "+this.gastoEditar.getTipoIRPF()+"% ";
				cadena+=" sobre "+modelo.Decimales(baseImponible);
				panelDetalle.add(new JLabel(cadena),ct);
				ct.gridx=3;
				cantidad = this.gastoEditar.getTipoIRPF()*baseImponible/100;
				cadena = "-"+ modelo.Decimales(cantidad);
				panelDetalle.add(new JLabel("<html><b>"+cadena+"</b></html>"),ct);
				ct.gridx++;
				subTotal = subTotal - cantidad;
				cadena = "" + modelo.Decimales(subTotal);
				panelDetalle.add(new JLabel("<html><b>"+ cadena +"</b></html>"),ct);
				ct.gridy++;
			}
			// TOTAL
			ct.gridx = 3;
			panelDetalle.add(new JLabel("TOTAL:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+modelo.Decimales(this.gastoEditar.getTOTAL())+"</b></html>"),ct);
			
			// TOTAL
			ct.gridx = 3;
			panelDetalle.add(new JLabel("TOTAL:"),ct);
			ct.gridx++;
			panelDetalle.add(new JLabel("<html><b>"+modelo.Decimales(this.gastoEditar.getTOTAL())+"</b></html>"),ct);
		}
	}


	
	/*
	 * Llena la tabla de gastos con la lista filtrada
	 */
	public void llenaLista(FiltroVO filtro){
		//gastos.
		gastoEditar=null;
		gastos.getSelectionModel().clearSelection();
		beditar.setEnabled(false);
		beliminar.setEnabled(false);
		bpdf.setEnabled(false);
		actualizarDetalleGasto();
		listaGastos = modelo.getGastos(filtro);
		ModeloTablaGastos modeloTabla = new ModeloTablaGastos();
		for(int i = 0;i<listaGastos.size();i++){
			modeloTabla.addGasto(listaGastos.get(i));
		}
		
		gastos.setModel(modeloTabla);	
	}
	
	
	/*
	 * Llena una lista con los detalles de un ID gasto
	 */
	public ArrayList<DetalleGastoVO> llenaListaDetalle(String ID_gasto){
		ArrayList<DetalleGastoVO> listaDetalleGasto = modelo.getDetalleGasto(ID_gasto);
		return listaDetalleGasto;
		
	}
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/


	/**
	 * Escucha cambios de fecha
	 */
	public class escuchaCambioFecha implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent e){
			filtroGasto.setFechaInicio(desde_find.getDate());
			filtroGasto.setFechaFinal(hasta_find.getDate());
			filtroGasto.setNombre((String) proveedor.getSelectedItem());
			llenaLista(filtroGasto);		
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
	 * Escucha combo de proveedores enter
	 * @author sergio
	 *
	 */
	class cambioProveedor implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			JComboBox source = (JComboBox) event.getSource();
			if(!source.isPopupVisible()){
				filtroGasto.setNombre((String)proveedor.getSelectedItem()); 
				llenaLista(filtroGasto);
			}
		}       
	}

	/**
	 * Añadir Escucha combo proveedores raton
	 * @param box
	 */
	private void addProveedorRatonListener(JComboBox box) {
		try {
			Field popupInBasicComboBoxUI = BasicComboBoxUI.class.getDeclaredField("popup");
			popupInBasicComboBoxUI.setAccessible(true);
			BasicComboPopup popup = (BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI());

			Field scrollerInBasicComboPopup = BasicComboPopup.class.getDeclaredField("scroller");
			scrollerInBasicComboPopup.setAccessible(true);
			JScrollPane scroller = (JScrollPane) scrollerInBasicComboPopup.get(popup);

			scroller.getViewport().getView().addMouseListener(EscuchaProveedorRaton());
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escucha del combo de proveedores raton
	 * @return
	 */
	private MouseAdapter EscuchaProveedorRaton() {
		return new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				//System.out.println("mouseClicked");
			}

			public void mousePressed(MouseEvent mouseEvent) {
				//System.out.println("mousePressed");
			}

			public void mouseReleased(MouseEvent mouseEvent) {
				filtroGasto.setNombre((String)proveedor.getSelectedItem()); 
				llenaLista(filtroGasto);
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
				desde_find.setDate(modelo.primeraFecha("GASTOS"));
				hasta_find.setDate(modelo.ultimaFecha("GASTOS"));
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
			proveedor.setEnabled(false);
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
			proveedor.setEnabled(true);
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
	
	public class ActionBoton implements ActionListener {

		public void actionPerformed(ActionEvent ev) {
			String evento = ev.getActionCommand();
			if(evento.equals("Nuevo")){
				try {
					NuevoGasto gasto=new NuevoGasto (datosFactunomo, principal);
					if (gasto.isCancelado())
					{
						actualizarBusqueda(seleccion);
					}		
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"Se ha producido un error al crear nueva entrada: "+ e.getMessage(), 
							"ERROR DE ACTUALIZACIÓN",JOptionPane.ERROR_MESSAGE);
				}
			} else if(evento.equals("Eliminar")){		
				int result = JOptionPane.showConfirmDialog(principal,
						"Eliminar gasto.\n¿Está seguro?", "¡Advertencia!",
						JOptionPane.YES_NO_OPTION);
				switch (result) {
                case JOptionPane.YES_OPTION:
                	modelo.eliminarGasto(gastoEditar.getIdGasto());
                	actualizarBusqueda(seleccion);
                	
                    break;
                default:
                    return;
                }
			} else if(evento.equals("Editar")){
				try {
					accionesEditar();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"Se ha producido un error al actualizar el gasto: "+ e.getMessage(), 
							"ERROR DE ACTUALIZACIÓN",JOptionPane.ERROR_MESSAGE);
				}
			} else if(evento.equals("PDF")){
					try {
						new ImprimePDF(gastoEditar, listaDetalleGasto, datosFactunomo);
						String gasto = gastoEditar.getIdGasto();
						new verPDF (gasto, principal);
		
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,"Se ha producido un error al imprimir el documento: "+ e.getMessage(), 
								"ERROR DE BORRADO",JOptionPane.ERROR_MESSAGE);
					
				} 
			} else{

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
	
	
	public class MouseTablaGastos extends MouseAdapter {
		public void mousePressed(MouseEvent e){
			maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e){
			maybeShowPopup(e);
		}

		public void maybeShowPopup(MouseEvent e){
			if(!e.isPopupTrigger()){
				beditar.setEnabled(true);
				beliminar.setEnabled(true);
				bpdf.setEnabled(true);
				//detalleGasto();
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					accionesEditar();
				}
			}
		}
	}
	
	public void accionesEditar()
	{
		EditarGasto gasto=new EditarGasto (gastoEditar, listaDetalleGasto, datosFactunomo, principal);
		if (!gasto.isCancelado())
		{
			int fila=gastos.getSelectedRow();
			int tamanho=gastos.getRowCount();
			actualizarBusqueda(seleccion);
			if (gastos.getRowCount()==tamanho)
			{
				gastos.changeSelection(fila, 0, false, false);
				beditar.setEnabled(true);
				beliminar.setEnabled(true);
				bpdf.setEnabled(true);
			}
		}			
	}

	/**
	 * Escucha Acciones tabla
	 * @author sergio
	 *
	 */
	public class EscuchaTabla implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent evt){
			if (gastos.getSelectedRowCount()>0)
			{
				gastoEditar = (GastoVO)((ModeloTablaGastos)gastos.getModel()).getGasto(gastos.getSelectedRow());
				actualizarDetalleGasto();
			}
		}		
	}
}
