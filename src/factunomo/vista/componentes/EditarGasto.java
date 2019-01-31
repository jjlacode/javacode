package factunomo.vista.componentes;


import java.awt.BorderLayout;

import org.jdesktop.swingx.autocomplete.*;

import java.lang.Object;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.Container;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import factunomo.modelo.Modelo;
import factunomo.modelo.ModeloTablaDetalleGasto;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.DetalleGastoVO;
import factunomo.modelo.obj.ProveedorVO;
import factunomo.modelo.obj.FormaPagoVO;

import com.toedter.calendar.JDateChooser;

import factunomo.vista.componentes.NuevoGasto.cambioFormaPago;
import factunomo.vista.componentes.decorators.CellEditor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.*;


public class EditarGasto extends FormularioBase{
	private static final long serialVersionUID = 1L;
	
	private boolean advertencia;

	// Paneles
	private JFrame principal;
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel panelEditar;
	private JPanel panelDetalle;
	private JPanel panelSuperior;
	private JPanel panelGasto;
	private JPanel panelProveedor;
	private JPanel panelTotal;
	private JTable detalle;
	private JPanel panelCentral;
	private JPanel panelFormaPago;

	private JButton beliminar;
	private JButton bnuevo;

	private Modelo modelo;
	// registros
	private GastoVO gastoEditar;
	private ArrayList<DetalleGastoVO> listaDetallesGasto;
	private ProveedorVO datosProveedor;
	private FormaPagoVO datosFormaPago;
	private ConfiguracionVO datosUsuario;
	
	private boolean cancelado;
	// fechas
	private Date fechaInicio;
	private Date fechaFinal;
	// campos
	private JTextField idgasto;
	private JDateChooser fecha;
	private JComboBox proveedor;
	private JComboBox formaPago;
	private JTextField descuento;
	private JTextField IRPF;
	private JTextField numeroCuenta;
	
	private Boolean aFoco;
	
	protected ModeloTablaDetalleGasto modeloTabla;

	private Float baseImponible;

	/**
	 * Constructor por defecto.
	 */
	public EditarGasto(GastoVO gastoEditar, ArrayList<DetalleGastoVO> listaDetallesGasto, ConfiguracionVO datos, JFrame principal){
				
		// INICIALIZAR VARIABLES
		this.advertencia=false;
		this.cancelado=false;
		this.modelo = new Modelo();
		this.principal = principal;
		this.gastoEditar = gastoEditar;
		this.listaDetallesGasto = listaDetallesGasto;
		this.fechaInicio = modelo.fechaInicio(datos.getEjercicio(),datos.getPeriodo());
		this.fechaFinal = modelo.fechaFinal(datos.getEjercicio(),datos.getPeriodo());
		modeloTabla = new ModeloTablaDetalleGasto(ventana, detalle);
		this.llenaLista();
		modeloTabla.addTableModelListener(new CambioTablaListener());
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);
		this.detalle.setCellSelectionEnabled(true);
		CellEditor EditorCeldas= new CellEditor(ventana);
		TableColumn col2 = detalle.getColumnModel().getColumn(2);
	    col2.setCellEditor(EditorCeldas);
	    TableColumn col3 = detalle.getColumnModel().getColumn(3);
	    col3.setCellEditor(EditorCeldas);
	    TableColumn col4 = detalle.getColumnModel().getColumn(4);
	    col4.setCellEditor(EditorCeldas);
	    TableColumn col5 = detalle.getColumnModel().getColumn(5);
	    col5.setCellEditor(EditorCeldas);
	    detalle.addFocusListener(new EscuchaFoco());
	    detalle.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	    detalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "selectNextColumnCell");
	    KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
	    detalle.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(escape, "ESCAPE");
	    detalle.getActionMap().put("ESCAPE", escapeAction2);

		this.datosProveedor = modelo.getProveedor(this.gastoEditar.getNombre());
		this.datosFormaPago = modelo.getFormaPago(this.gastoEditar.getIdFormaPago());
		this.datosUsuario = datos;
		// Campos formulario
		this.fecha = new JDateChooser();
		this.fecha.setDate(gastoEditar.getFecha());
		this.fecha.setFocusable(false);
		this.idgasto = new JTextField (12);
		this.idgasto.setText(gastoEditar.getIdGasto());
		this.proveedor = this.getComboProveedores(0);
		this.proveedor.setSelectedItem(gastoEditar.getNombre());
		this.formaPago = this.getComboFormasPago();
		this.descuento = new JTextField (5);
		this.descuento.setText("0,00");
		this.descuento.addActionListener(new DescuentoAL());
		this.descuento.addFocusListener(new DescuentoFL());
		this.IRPF = new JTextField (5);
		this.IRPF.setText("0,00");
		this.IRPF.addActionListener(new IRPFAL());
		this.IRPF.addFocusListener(new IRPFFL());
		this.numeroCuenta = new JTextField (23);

		creaVentana();

		
	}
	
	/**
	 * Función de consulta de salida por cancelación
	 * @return
	 */
	public boolean isCancelado()
	{
		return cancelado;
	}

	/**
	 * Método para capturar la tecla ESC y terminar la edición de celda
	 * o no permitir la deselección si no está editando
	 */
	Action escapeAction2 = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (detalle.isEditing())
			{
				int row = detalle.getEditingRow();
				int col = detalle.getEditingColumn();
				
				detalle.getCellEditor(row, col).cancelCellEditing();
				detalle.changeSelection(row, col, false, false);
				System.out.println("Entra");

			} else {
				int row = detalle.getEditingRow();
				int col = detalle.getEditingColumn();
				
				detalle.getSelectionModel().clearSelection();
				actualizarDetalle();
			}
		}	
	};
	
	private void creaVentana(){
		Float DescuentoInicial=gastoEditar.getDescuento();
		Float IRPFinicial=gastoEditar.getTipoIRPF();
		String CuentaInicial=gastoEditar.getNumeroCuenta();
		String PagoInicial=modelo.getFormaPago(gastoEditar.getIdFormaPago()).getNombre();
		// Nueva ventana
		
		ventana = new JDialog(principal,"Editar Gasto",true);
		
		ventana.addWindowListener(new cerrarWin());
		ventana.setSize(800, 500);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.setResizable(false);

		contenedor = ventana.getContentPane();
		contenedor.setBackground(Color.WHITE);
		contenedor.setLayout(new BorderLayout());

		// PANELES
		panelSuperior = new JPanel();	
		panelSuperior.setLayout(new GridBagLayout());
		GridBagConstraints csup = new GridBagConstraints();
		csup.insets = new Insets(3, 3, 3, 3);        
		csup.gridx = 0;
		csup.gridy = 0;
		csup.weightx = 0;
		csup.weighty = 0;
		csup.fill = GridBagConstraints.NONE;
		csup.anchor = GridBagConstraints.LINE_START;

		panelGasto = new JPanel();
		panelGasto.setLayout(new GridBagLayout());
		GridBagConstraints cgas = new GridBagConstraints();
		cgas.insets = new Insets(3, 3, 3, 3);        
		cgas.gridx = 0;
		cgas.gridy = 0;
		cgas.weightx = 0;
		cgas.weighty = 0;
		cgas.fill = GridBagConstraints.NONE;
		cgas.anchor = GridBagConstraints.LINE_START;

		panelProveedor = new JPanel();

		panelDetalle = new JPanel();
		
		panelEditar = new JPanel();
		
		panelTotal = new JPanel();

		panelFormaPago = new JPanel();

		panelCentral = new JPanel();
		panelCentral.setLayout(new GridBagLayout());
		GridBagConstraints ccen = new GridBagConstraints();
		ccen.gridx = 0;
		ccen.gridy = 0;
		ccen.weightx = 1;
		ccen.weighty = 1;

		// DISPOSICION

		// gasto
		panelGasto.add(new JLabel("<html><b>Fecha: </b></html>"),cgas);
		cgas.gridx++;
		panelGasto.add(this.fecha,cgas);
		cgas.gridy++;
		cgas.gridx = 0;
		panelGasto.add(new JLabel("Número:"),cgas);
		cgas.gridx++;
		panelGasto.add(this.idgasto,cgas);
		panelSuperior.add(panelGasto,csup);

		// Proveedor
		csup.gridx++;
		actualizarComboProveedor();
		visualizarProveedor();
		panelSuperior.add(panelProveedor,csup);

		// Detalle
		aFoco=false;
		bnuevo=new JButton("Nuevo");
		bnuevo.addActionListener(new NuevoAL());
		beliminar=new JButton("Eliminar");
		beliminar.addActionListener(new EliminarAL());
		visualizarEditar();
		beliminar.setFocusable(false);
		
		beliminar.setEnabled(false);
		ccen.gridwidth = 2;
		ccen.weighty = 0;
		ccen.weightx = 0;
		panelCentral.add(panelEditar,ccen);
		
		visualizarDetalle();
		ccen.gridy ++;
		ccen.gridwidth = 2;
		ccen.weighty = 1;
		ccen.weightx = 1;
		ccen.fill = GridBagConstraints.BOTH;
		panelCentral.add(panelDetalle,ccen);
		// FormaPago
		actualizarComboPago();
		visualizarFormaPago();
		ccen.gridy ++;
		ccen.weighty = 0;
		ccen.weightx = 0;
		ccen.gridwidth = 1;
		ccen.insets = new Insets(5, 20, 0, 20); 
		panelCentral.add(panelFormaPago,ccen);

		// Total
		visualizarTotal();

		ccen.insets = new Insets(2, 20, 0, 50); 
		ccen.gridx++;
		ccen.weightx = 1;
		panelCentral.add(panelTotal,ccen);

		// BOTONES
		pabajo = new JPanel();
		pabajo.setLayout(new FlowLayout());

		JButton bcerrar=new JButton("Cancelar");
		bcerrar.addActionListener(new CerrarAL());
		JButton bguardar=new JButton("Guardar");
		bguardar.addActionListener(new GuardarAL());

		pabajo.add(bguardar);
		pabajo.add(bcerrar);
		

		contenedor.add(panelSuperior,BorderLayout.NORTH);
		contenedor.add(panelCentral,BorderLayout.CENTER);
		contenedor.add(pabajo,BorderLayout.SOUTH);
		// Inicializar
		this.proveedor.setSelectedItem(this.gastoEditar.getNombre());
		this.formaPago.setSelectedItem((Object)PagoInicial);
		this.numeroCuenta.setText(CuentaInicial);
		System.out.println(CuentaInicial);
		this.IRPF.setText(Decimales(IRPFinicial));
		this.descuento.setText(Decimales(DescuentoInicial));
		this.gastoEditar.setTipoIRPF(IRPFinicial);	
		actualizarTotal();
		ventana.setVisible(true);
		
	}

	
	/**
	 * Visualizar editar
	 */
	private void visualizarEditar(){
		panelEditar.setLayout(new FlowLayout());
		panelEditar.add(bnuevo);
		panelEditar.add(beliminar);
		beliminar.setEnabled(aFoco);
	}

	
	/**
	 * Visualizar Detalle
	 */
	private void visualizarDetalle(){
		panelDetalle.setLayout(new GridBagLayout());
		GridBagConstraints cdet = new GridBagConstraints();
		cdet.gridx = 0;
		cdet.gridy = 0;
		cdet.weighty = 1;
		cdet.weightx = 1;
		cdet.fill = GridBagConstraints.BOTH;
		detalle.getColumn(" ").setCellRenderer(new ButtonRenderer());
		detalle.getColumn(" ").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		detalle.getColumnModel().getColumn(0).setMaxWidth(18);
		detalle.getColumnModel().getColumn(1).setPreferredWidth(40);
		detalle.getColumnModel().getColumn(2).setMinWidth(250);
		
		JScrollPane lista = new JScrollPane(this.detalle);
		panelDetalle.add(lista,cdet);
	}
	
	/**
	 * Visualizar panel proveedor
	 */
	private void visualizarProveedor () {
		String cadena = "";
		panelProveedor.setLayout(new GridBagLayout());
		GridBagConstraints cpro = new GridBagConstraints();
		cpro.insets = new Insets(3, 3, 3, 3);        
		cpro.gridx = 0;
		cpro.gridy = 0;
		cpro.weightx = 0;
		cpro.weighty = 0;
		cpro.fill = GridBagConstraints.NONE;
		cpro.anchor = GridBagConstraints.LINE_START;
		panelProveedor.add(new JLabel("Nombre:"),cpro);
		cpro.gridx++;
		panelProveedor.add(this.proveedor,cpro);
		cpro.gridx++;
		JButton bproveedor=new JButton("+");
		bproveedor.addActionListener(new ProveedorAL());
		panelProveedor.add(bproveedor,cpro);
		cpro.gridx = 0;
		cpro.gridy++;
		panelProveedor.add(new JLabel("Dirección:"),cpro);		
		cpro.gridx++;
		if (datosProveedor.getDireccion() != null) {
			cadena = this.datosProveedor.getDireccion();
		}
		if (datosProveedor.getCiudad() != null )
		{
			if (!datosProveedor.getCiudad().equals(""))
			{
				cadena += " | "+this.datosProveedor.getCiudad();
			}
		}
		cpro.gridwidth=2;
		panelProveedor.add(new JLabel(cadena),cpro);
		cpro.gridwidth=1;
		cpro.gridx = 0;
		cpro.gridy++;		
		panelProveedor.add(new JLabel("CIF:"),cpro);		
		cpro.gridx++;
		if (datosProveedor.getNIF() != null) {
			panelProveedor.add(new JLabel("<html><b>"+this.datosProveedor.getNIF()+"</b></html>"),cpro);
		}
	}

	/**
	 * Visualizar panel forma de pago
	 */
	private void visualizarFormaPago () {
		String cadena = "";
		panelFormaPago.setLayout(new GridBagLayout());
		GridBagConstraints cfor = new GridBagConstraints();
		cfor.gridx = 0;
		cfor.gridy = 0;
		cfor.weightx = 0;
		cfor.weighty = 0;
		cfor.fill = GridBagConstraints.NONE;
		cfor.anchor = GridBagConstraints.LINE_START;
		panelFormaPago.add(new JLabel("Forma de pago:"),cfor);
		cfor.gridy++;
		this.formaPago.setSelectedItem(this.datosFormaPago.getNombre());
		panelFormaPago.add(this.formaPago,cfor);
		cfor.gridx++;
		JButton bformaPago=new JButton("+");
		bformaPago.addActionListener(new FormaPagoAL());
		panelFormaPago.add(bformaPago,cfor);
		cfor.gridx = 0;
		cfor.gridy++;
		panelFormaPago.add(new JLabel("Número de cuenta:"),cfor);		
		cfor.gridy++;
		if (gastoEditar.getNumeroCuenta() != " ") {
			cadena = gastoEditar.getNumeroCuenta();
		}
		gastoEditar.setNumeroCuenta(numeroCuenta.getText());
		if (this.datosFormaPago.getNumeroCuenta()){
			 this.numeroCuenta.setEnabled(true);
			 if (modelo.getCuentas().size()>0){
			 }
		 } else {
			 this.numeroCuenta.setEnabled(false);
		 }
		cfor.fill = GridBagConstraints.HORIZONTAL;
		panelFormaPago.add(this.numeroCuenta,cfor);

	}
	
	
	/**
	 * Visualizar panel total
	 */
	private void visualizarTotal(){
		panelTotal.setLayout(new GridBagLayout());
		GridBagConstraints ctot = new GridBagConstraints();
		ctot.gridx = 3;
		ctot.gridy = 0;
		ctot.weightx = 0;
		ctot.anchor = GridBagConstraints.LINE_END;
		panelTotal.add(new JLabel("Base Imponible:"),ctot);
		ctot.gridx = 5;
		baseImponible = 0f;
		Float subTotal = 0f;
		Float base = 0f;
		Float[] IVA = new Float[3];
		Float[] tipoIVA = new Float[3];
		// obtener sumas de IVA
		for (int i=0; i<listaDetallesGasto.size();i++){
			baseImponible = baseImponible + listaDetallesGasto.get(i).getSubTotal();
			for (int j=0;j<3;j++) {
				if (tipoIVA[j]==null){
					IVA[j] = listaDetallesGasto.get(i).getSubTotal();
					tipoIVA[j] = listaDetallesGasto.get(i).getIVA();
					break;
				}
				if (listaDetallesGasto.get(i).getIVA().equals(tipoIVA[j]))
				{
					IVA[j] += listaDetallesGasto.get(i).getSubTotal();
					break;
				}
			}
		}
		String cadena = "" + Decimales(baseImponible);
		ctot.anchor = GridBagConstraints.LINE_END;
		ctot.weightx = 1;
		panelTotal.add(new JLabel(cadena),ctot);
		
		// DESCUENTO
		ctot.gridx = 0;
		ctot.gridy = 1;
		ctot.anchor = GridBagConstraints.LINE_START;
		ctot.weightx = 0;
		panelTotal.add(new JLabel("Descuento:  "),ctot);
		ctot.gridx++;
		cadena="" + Decimales(gastoEditar.getDescuento());
		ctot.anchor = GridBagConstraints.LINE_END;
		cadena = descuento.getText();
		cadena=cadena.replace(",",".");	
		gastoEditar.setDescuento(Float.parseFloat(cadena));
		this.descuento.setHorizontalAlignment(JTextField.RIGHT);
		panelTotal.add(this.descuento,ctot);
		ctot.gridx++;
		ctot.anchor = GridBagConstraints.LINE_START;
		panelTotal.add(new JLabel("%"),ctot);
		ctot.gridx++;
		cadena = "" + Decimales(baseImponible);
		ctot.anchor = GridBagConstraints.LINE_END;
		panelTotal.add(new JLabel(cadena),ctot);
		ctot.gridx++;
		cadena= " ";
		if (gastoEditar.getDescuento()!=0f){
			subTotal = baseImponible*gastoEditar.getDescuento()/100;
			cadena ="-";
		}
		cadena += Decimales(subTotal);
		ctot.weightx = 1;
		panelTotal.add(new JLabel(cadena),ctot);
		ctot.gridx++;
		baseImponible-= subTotal;
		base = baseImponible;
		Float baseIVA=0f;
		cadena = "" + Decimales(base);
		panelTotal.add(new JLabel(cadena),ctot);
		for (int i=0;i<tipoIVA.length;i++){
			if (tipoIVA[i]!=null && tipoIVA[i]!=0f){
				ctot.gridx = 0;
				ctot.gridy++;
				ctot.anchor = GridBagConstraints.LINE_START;
				ctot.weightx = 0;
				panelTotal.add(new JLabel("IVA:"),ctot);
				ctot.gridx++;
				cadena = "" + tipoIVA[i];
				ctot.anchor = GridBagConstraints.LINE_END;
				panelTotal.add(new JLabel(cadena),ctot);
				ctot.gridx++;
				ctot.anchor = GridBagConstraints.LINE_START;
				panelTotal.add(new JLabel("%"),ctot);
				ctot.gridx++;
				baseIVA = IVA[i]*(100-gastoEditar.getDescuento())/100;
				cadena = "" + Decimales(base);
				ctot.anchor = GridBagConstraints.LINE_END;
				panelTotal.add(new JLabel(cadena),ctot);
				ctot.gridx++;
				subTotal=tipoIVA[i]*baseIVA/100;
				cadena = "" + Decimales(subTotal);
				ctot.weightx = 1;
				panelTotal.add(new JLabel(cadena),ctot);
				ctot.gridx++;
				base+=subTotal;
				cadena = "" + Decimales(base);
				panelTotal.add(new JLabel(cadena),ctot);
			}
		}
		// actualizar variable
		gastoEditar.setIVA1(0f);
		gastoEditar.setIVA2(0f);
		gastoEditar.setIVA3(0f);
		gastoEditar.setBaseImponible1(0f);
		gastoEditar.setBaseImponible2(0f);
		gastoEditar.setBaseImponible3(0f);
		if (tipoIVA[0]!=null) gastoEditar.setIVA1(tipoIVA[0]);
		if (tipoIVA[1]!=null) gastoEditar.setIVA2(tipoIVA[1]);
		if (tipoIVA[2]!=null) gastoEditar.setIVA3(tipoIVA[2]);
		if (IVA[0]!=null) gastoEditar.setBaseImponible1(IVA[0]*(100-gastoEditar.getDescuento())/100);
		if (IVA[1]!=null) gastoEditar.setBaseImponible2(IVA[1]*(100-gastoEditar.getDescuento())/100);
		if (IVA[2]!=null) gastoEditar.setBaseImponible3(IVA[2]*(100-gastoEditar.getDescuento())/100);
		
		// IRPF
		ctot.gridx = 0;
		ctot.gridy++;
		ctot.anchor = GridBagConstraints.LINE_START;
		ctot.weightx = 0;
		panelTotal.add(new JLabel("IRPF:  "),ctot);
		ctot.gridx++;
		ctot.anchor = GridBagConstraints.LINE_END;
		this.IRPF.setHorizontalAlignment(JTextField.RIGHT);
		panelTotal.add(this.IRPF,ctot);
		cadena = IRPF.getText();
		cadena=cadena.replace(",",".");	
		gastoEditar.setTipoIRPF(Float.parseFloat(cadena));
		ctot.gridx++;
		ctot.anchor = GridBagConstraints.LINE_START;
		panelTotal.add(new JLabel("%"),ctot);
		ctot.gridx++;
		cadena = "" + Decimales(baseImponible);
		ctot.anchor = GridBagConstraints.LINE_END;
		panelTotal.add(new JLabel(cadena),ctot);
		ctot.gridx++;
		subTotal = baseImponible*gastoEditar.getTipoIRPF()/100;
		cadena =""+ Decimales(subTotal);
		ctot.weightx = 1;
		if (gastoEditar.getTipoIRPF()!=0f)	cadena="-"+cadena;
		panelTotal.add(new JLabel(cadena),ctot);
		ctot.gridx++;
		base = base - subTotal;
		cadena = "" + Decimales(base);
		panelTotal.add(new JLabel(cadena),ctot);
		
		// TOTAL
		gastoEditar.setTOTAL(base);		
		ctot.gridx = 4;
		ctot.gridy++;
		JLabel textoTotal = new JLabel("TOTAL");
		textoTotal.setFont(new Font("Arial", Font.PLAIN, 14));
		panelTotal.add(textoTotal,ctot);
		ctot.gridx++;
		cadena = "" + Decimales(base);
		JLabel cifraTotal = new JLabel(cadena);
		cifraTotal.setFont(new Font("Arial", Font.BOLD, 14));
		panelTotal.add(cifraTotal,ctot);
	}


	/**
	 * Llena la tabla de detalles del gasto
	 */	
	private void llenaLista(){
		for(int i = 0;i<listaDetallesGasto.size();i++){
			modeloTabla.addDetalleGasto(listaDetallesGasto.get(i));
		}		
	}

	/**
	 * Actualizar Editar
	 */
	private void actualizarEditar(){
		panelEditar.removeAll();
		visualizarEditar();
		panelEditar.updateUI();
	}
	
	/**
	 * Actualizar Detalle
	 */
	private void actualizarDetalle(){
		modeloTabla.borraTabla();
		llenaLista();
		panelDetalle.removeAll();
		visualizarDetalle();
		panelDetalle.updateUI();
	}

	/**
	 * Actualizar Total
	 */
	private void actualizarTotal(){
		panelTotal.removeAll();
		visualizarTotal();
		panelTotal.updateUI();
	}

	/**
	  * Actualizar formaPago
	  */
	 private void actualizarFormaPago(){
		 datosFormaPago = modelo.getNombreFormaPago((String) formaPago.getSelectedItem());
		 panelFormaPago.removeAll();
		 visualizarFormaPago();
		 panelFormaPago.updateUI();
	 }
	 
	 /**
	  * Actualizar combo formas pago
	  */
	 private void actualizarComboPago()
	 {
		 this.formaPago = getComboFormasPago();
		 this.datosFormaPago = modelo.getNombreFormaPago((String) formaPago.getSelectedItem());
		 AutoCompleteDecorator.decorate(this.formaPago); 
		 this.formaPago.addActionListener(new cambioFormaPago());
		 addFormaPagoRatonListener(this.formaPago);
	 }

	/**
	 * Actualizar proveedor
	 */
	private void actualizarProveedor(){
		datosProveedor = modelo.getProveedor((String) proveedor.getSelectedItem());
		panelProveedor.removeAll();
		visualizarProveedor();
		panelProveedor.updateUI();
	}
	
	/**
	 * Actualizar combo proveedor
	 */
	private void actualizarComboProveedor()
	{
		this.proveedor = this.getComboProveedores(0);
		this.datosProveedor=modelo.getProveedor((String) proveedor.getSelectedItem());
		AutoCompleteDecorator.decorate(this.proveedor);
		this.proveedor.addActionListener(new cambioProveedor());
		addProveedorRatonListener(proveedor);
	}
	

	public void avanzaCelda()
    {
		int col = detalle.getSelectedColumn();
		int fil = detalle.getSelectedRow();
        detalle.editCellAt(fil, col+1);
        detalle.transferFocus();
    }
	
	/**
	 * Acciones cambio proveedor
	 */
	private void accionesCambioProveedor()
	{
		if (!((String) proveedor.getSelectedItem()).equals("Ninguno"))
		{
			actualizarProveedor();
			int forma = 0;
			if (!datosProveedor.getIdFormaPago().equals("")){
				forma=Integer.parseInt(datosProveedor.getIdFormaPago())-1;
				gastoEditar.setNumeroCuenta(datosProveedor.getNumeroCuenta());
				numeroCuenta.setText(datosProveedor.getNumeroCuenta());
			} else {
				forma=1;
				gastoEditar.setNumeroCuenta("");	
				numeroCuenta.setText("");
			}
			if (datosProveedor.getConIRPF()){
				gastoEditar.setTipoIRPF(datosUsuario.getIRPF());
				String cadena=""+datosUsuario.getIRPF();
				IRPF.setText(cadena);
			} else {
				IRPF.setText("0,00");
			}
			
			formaPago.setSelectedIndex(forma);
			actualizarTotal();
		}
		proveedor.requestFocus();
	}
	
	/*
	 * ESCUCHAS
	 */

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
				accionesCambioProveedor();
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
			//	                ((JViewport) ((JScrollPane) ((BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI())).getComponents()[0]).getComponents()[0]).getComponents()[0].addMouseListener(this);
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Añadir Escucha combo FormaPago raton
	 * @param box
	 */
	private void addFormaPagoRatonListener(JComboBox box) {
		try {
			Field popupInBasicComboBoxUI = BasicComboBoxUI.class.getDeclaredField("popup");
			popupInBasicComboBoxUI.setAccessible(true);
			BasicComboPopup popup = (BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI());

			Field scrollerInBasicComboPopup = BasicComboPopup.class.getDeclaredField("scroller");
			scrollerInBasicComboPopup.setAccessible(true);
			JScrollPane scroller = (JScrollPane) scrollerInBasicComboPopup.get(popup);

			scroller.getViewport().getView().addMouseListener(EscuchaFormaPagoRaton());
			//	                ((JViewport) ((JScrollPane) ((BasicComboPopup) popupInBasicComboBoxUI.get(box.getUI())).getComponents()[0]).getComponents()[0]).getComponents()[0].addMouseListener(this);
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Escucha del combo de formaPago Ratón
	 * @return
	 */
	private MouseAdapter EscuchaFormaPagoRaton() {
		return new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				//System.out.println("mouseClicked");
			}

			public void mousePressed(MouseEvent mouseEvent) {
				//System.out.println("mousePressed");
			}

			public void mouseReleased(MouseEvent mouseEvent) {
				advertencia=true;
				actualizarFormaPago();
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
				accionesCambioProveedor();
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
	 * Escucha Botones de la tabla
	 * @author sergio
	 *
	 */
	class ButtonEditor extends DefaultCellEditor {
		protected JButton button;

		private String label;

		private boolean isPushed;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = (value == null) ? "" : value.toString();
			button.setText(label);
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				int fila=detalle.getEditingRow();
				int columna=2;
				SeleccionarProducto editarProducto=new SeleccionarProducto(ventana);
				if (editarProducto.producto!=null)
				{
					columna=3;
					listaDetallesGasto.get(fila).setIdProducto(editarProducto.producto.getIdProducto());
					listaDetallesGasto.get(fila).setDescripcion(editarProducto.producto.getNombre());
					listaDetallesGasto.get(fila).setCantidad(1f);
					listaDetallesGasto.get(fila).setPrecio(editarProducto.producto.getPrecio());
					listaDetallesGasto.get(fila).setIVA(editarProducto.producto.getIVA());
					float subTotal=editarProducto.producto.getPrecio();
					listaDetallesGasto.get(fila).setSubTotal(subTotal);
					actualizarDetalle();
				}
			}
			isPushed = false;
			return new String(label);
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			
			super.fireEditingStopped();
		}
	}

	
	/**
	 * Crear botones de la tabla
	 * @author sergio
	 *
	 */
	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}
	
	
	/**
	 * Escucha boton nuevo
	 * @author sergio
	 *
	 */
	class NuevoAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evento){
			DetalleGastoVO p = new DetalleGastoVO();
			p.setIdProducto("");
			p.setDescripcion("");
			p.setCantidad(0f);
			p.setPrecio(0f);
			p.setIVA(0f);
			p.setSubTotal(0f);
			int fila=detalle.getRowCount();
			listaDetallesGasto.add(p);
			actualizarDetalle();
			detalle.requestFocus();
			detalle.changeSelection(fila, 2, false, false);
			System.out.println("nuevo");
			return;
			
		}
	}
	
	
	/**
	 * Escucha boton eliminar
	 * @author sergio
	 *
	 */
	
	class EliminarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			ArrayList<DetalleGastoVO> lista = new ArrayList<DetalleGastoVO>();
			int num=detalle.getSelectedRow();
			System.out.println(num);
			for (int i=0;i<listaDetallesGasto.size();i++){
				if (i!=num){
					lista.add(listaDetallesGasto.get(i));
				}
			}
			listaDetallesGasto=lista;
			actualizarDetalle();
			return;
		}
	}
	
	/**
	 * Escucha boton guardar
	 * @author sergio
	 *
	 */
	class GuardarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			if (fecha.getDate().before(fechaInicio)){
				int result = JOptionPane.showConfirmDialog(ventana,
						"Se van a actualizar los datos de un gasto anterior al periodo actual. ¿Está seguro de continuar? Si lo hace, los informes de ese periodo y ejercicio pueden dejar de ser válidos.", "¡Advertencia!",
						JOptionPane.YES_NO_OPTION);
				switch (result) {
                case JOptionPane.YES_OPTION:
                    break;
                default:
                    return;
                }
			}
			if (idgasto.getText().length()>8) {
				int result = JOptionPane.showConfirmDialog(ventana,
						"El número de gasto debe tener un máximo de 8 caracteres", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			if (!gastoEditar.getIdGasto().equals(idgasto.getText()) && modelo.idGastoDuplicado(idgasto.getText())){
				int result = JOptionPane.showConfirmDialog(ventana,
						"Ya existe otro gasto con ese número", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}			
			if (fecha.getDate().after(fechaFinal)){
				int result = JOptionPane.showConfirmDialog(ventana,
						"La fecha introducida es posterior al periodo actual. ¿Está seguro de continuar?", "Aviso",
						JOptionPane.YES_NO_OPTION);
				switch (result) {
                case JOptionPane.YES_OPTION:
                    break;
                default:
                    return;
                }
			}
			if (((String)proveedor.getSelectedItem()).equals("Ninguno"))
			{
				int result = JOptionPane.showConfirmDialog(ventana,
						"Es necesario crear al menos un proveedor", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			if (listaDetallesGasto.size()<1){
				int result = JOptionPane.showConfirmDialog(ventana,
						"El asiento no contiene datos", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			
			gastoEditar.setFecha(fecha.getDate());
			gastoEditar.setIdGasto(idgasto.getText());
			gastoEditar.setNombre((String)proveedor.getSelectedItem());
			gastoEditar.setIdProveedor(datosProveedor.getIdProveedor());
			gastoEditar.setDireccion("");
			if (datosProveedor.getDireccion()!=null)
			{
				gastoEditar.setDireccion(datosProveedor.getDireccion());
			}
			gastoEditar.setCP("");
			if (datosProveedor.getCP()!=null)
			{
				gastoEditar.setCP(datosProveedor.getCP());
			}
			gastoEditar.setCiudad("");
			if (datosProveedor.getCiudad()!=null)
			{
				gastoEditar.setCiudad(datosProveedor.getCiudad());
			}
			gastoEditar.setProvincia("");
			if (datosProveedor.getProvincia()!=null)
			{
				gastoEditar.setProvincia(datosProveedor.getProvincia());
			}
			gastoEditar.setNIF("");
			if (datosProveedor.getNIF()!=null)
			{
				gastoEditar.setNIF(datosProveedor.getNIF());
			}
			gastoEditar.setIdFormaPago(datosFormaPago.getIdFormaPago());
			String cadena=descuento.getText();
			cadena=cadena.replace(",",".");
			gastoEditar.setDescuento(Float.parseFloat(cadena));
			gastoEditar.setNumeroCuenta(numeroCuenta.getText());
			
			try {
				modelo.actualizarGasto(gastoEditar,listaDetallesGasto);
			} catch (Exception e) {
			}
			
			ventana.dispose();

		}
	}

	/**
	 * Escucha boton cancelar
	 * @author sergio
	 *
	 */
	class CerrarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			ventana.dispose();
			String sFichero = "elemento.pdf";
			File fichero = new File(sFichero);
			if (fichero.exists())
				fichero.delete();
			return;
		}
	}

	/**
	 * Escucha campo descuento
	 * @author sergio
	 *
	 */
	class DescuentoAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			advertencia=true;
			accionesDescuento();
		}
	}
	class DescuentoFL implements FocusListener {
		public void focusGained(FocusEvent evento){
		}
		public void focusLost(FocusEvent evento){
			if (advertencia)
			{
				advertencia=false;
				return;
			}
			accionesDescuento();
		}
	}
	
	/**
	 * Acciones cambio Descuento
	 */
	private void accionesDescuento()
	{
		Float cifra=0f;
		String cadena = descuento.getText();
		cadena=cadena.replace(",",".");
		boolean err = false;
		try {
			cifra=Float.parseFloat(cadena);
		} catch (Exception e) {
			err=true;
		}
		if (cifra<0 || cifra>100) err=true;
		if (err){
			int result = JOptionPane.showConfirmDialog(ventana,
					"El Descuento debe ser un número decimal entre 0 y 100", "¡Advertencia!",
					JOptionPane.DEFAULT_OPTION);
			return;
		} else {
			descuento.setText(Decimales(cifra));
			actualizarTotal();
		}
	}
	
	/**
	 * Escucha campo IRPF
	 * @author sergio
	 *
	 */
	class IRPFAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			advertencia=true;
			accionesIRPF();
		}
	}
	class IRPFFL implements FocusListener {
		public void focusGained(FocusEvent evento){
		}
		public void focusLost(FocusEvent evento){
			if (advertencia)
			{
				advertencia=false;
				return;
			}
			accionesIRPF();
		}
	}
	
	/**
	 * Acciones cambio IRPF
	 */
	private void accionesIRPF()
	{
		Float cifra=0f;
		String cadena = IRPF.getText();
		cadena=cadena.replace(",",".");
		boolean err = false;
		try {
			cifra=Float.parseFloat(cadena);
		} catch (Exception e) {
			err=true;
		}
		if (cifra<0 || cifra>100) err=true;
		if (err){
			int result = JOptionPane.showConfirmDialog(ventana,
					"El IRPF debe ser un número decimal entre 0 y 100", "¡Advertencia!",
					JOptionPane.DEFAULT_OPTION);
			return;
		} else {
			IRPF.setText(Decimales(cifra));
			actualizarTotal();
		}
	}
	
	/**
	 * Escucha boton proveedor
	 * @author sergio
	 *
	 */
	class ProveedorAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			Object seleccion=proveedor.getSelectedItem();
			SeleccionarProveedor proveedorSeleccionado=new SeleccionarProveedor(ventana);
			actualizarComboProveedor();
			actualizarProveedor();
			if (proveedorSeleccionado.getProveedor()!=null)
			{
				proveedor.setSelectedItem(proveedorSeleccionado.getProveedor().getNombre());
			} else {
				try {
					proveedor.setSelectedItem(seleccion);
				} catch (Exception e) {
					proveedor.setSelectedIndex(0);
				}
			}
			return;
		}
	}

	/**
	 * Escucha boton formaPago
	 * @author sergio
	 *
	 */
	class FormaPagoAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			Object seleccion=formaPago.getSelectedItem();
			SeleccionarFormaPago formaSeleccionada=new SeleccionarFormaPago(ventana);
			actualizarComboPago();
			if (formaSeleccionada.getFormaPago()!=null)
			{
				formaPago.setSelectedItem(formaSeleccionada.getFormaPago().getNombre());
			} else {
				if (modelo.nombrePagoDuplicado((String)seleccion))
				{
					formaPago.setSelectedItem(seleccion);
				} else {
					formaPago.setSelectedIndex(0);
				}
			}
			return;
		}
	}
	
	/**
	 * Escucha combo de formas de pago enter
	 * @author sergio
	 *
	 */
	class cambioFormaPago implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			JComboBox source = (JComboBox) event.getSource();
			if(!source.isPopupVisible()){
				if (advertencia){
					advertencia=false;
					return;
				}
				actualizarFormaPago();
			}
		}       
	}
	
	/**
	 * Escucha boton cerrar de la ventana
	 * @author sergio
	 *
	 */
	class cerrarWin extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			ventana.dispose();
		}
	}



	/**
	 * Escucha tabla acciones
	 * @author sergio
	 */
	 
	public class CambioTablaListener implements TableModelListener {
		public void tableChanged(TableModelEvent evt){
			 if (evt.getType() == TableModelEvent.UPDATE) {
				actualizarTotal();
             }		
		}	
		
	}
	
	private class EscuchaFoco implements FocusListener{
		public void focusGained(FocusEvent e) {
			aFoco=true;
			actualizarEditar();
			System.out.println("gana");
		}

		// this function successfully provides cell editing stop
		// on cell losts focus (but another cell doesn't gain focus)
		public void focusLost(FocusEvent e) {
			aFoco=false;
			actualizarEditar();
			System.out.println("pierde");
			detalle.getSelectionModel().clearSelection();

		}
	}



	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}

}