package factunomo.vista.componentes;


import java.awt.BorderLayout;

import org.jdesktop.swingx.autocomplete.*;

import java.lang.Object;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import factunomo.modelo.ModeloTablaDetalleIngreso;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.DetalleIngresoVO;
import factunomo.modelo.obj.ClienteVO;
import factunomo.modelo.obj.FormaPagoVO;

import com.toedter.calendar.JDateChooser;

import factunomo.vista.componentes.NuevoCliente.CuentaAL;
import factunomo.vista.componentes.NuevoCliente.cambioFormaPago;
import factunomo.vista.componentes.decorators.CellEditor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.*;

public class NuevoIngreso extends FormularioBase{
	private static final long serialVersionUID = 1L;

	// Paneles
	private JFrame principal;
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel panelEditar;
	private JPanel panelDetalle;
	private JPanel panelSuperior;
	private JPanel panelIngreso;
	private JPanel panelCliente;
	private JPanel panelTotal;
	private JTable detalle;
	private JPanel panelCentral;
	private JPanel panelFormaPago;

	private JButton beliminar;
	private JButton bnuevo;

	private Modelo modelo;
	// registros
	private IngresoVO ingresoEditar;
	private ArrayList<DetalleIngresoVO> listaDetallesIngreso;
	private ClienteVO datosCliente;
	private FormaPagoVO datosFormaPago;
	private ConfiguracionVO datosUsuario;
	
	private boolean cancelado;
	// fechas
	private Date fechaInicio;
	private Date fechaFinal;
	// campos
	private JTextField idingreso;
	private JDateChooser fecha;
	private JComboBox<String> cliente;
	private JComboBox<String> formaPago;
	private JTextField descuento;
	private JTextField IRPF;
	private JTextField numeroCuenta;
	private JButton bcuenta;
	
	private Boolean aFoco;
	
	protected ModeloTablaDetalleIngreso modeloTabla;

	private Float baseImponible;

	/**
	 * Constructor por defecto.
	 */
	public NuevoIngreso(ConfiguracionVO datos, JFrame principal){
		this.cancelado=false;
				
		// INICIALIZAR VARIABLES
		this.modelo = new Modelo();
		this.principal = principal;
		this.datosUsuario = datos;
		this.ingresoEditar = new IngresoVO();
		this.ingresoEditar.setIdIngreso(modelo.nuevoIdIngreso(datosUsuario.getEjercicio()));
		this.listaDetallesIngreso = new ArrayList<DetalleIngresoVO>();
		this.fechaInicio = modelo.fechaInicio(datos.getEjercicio(),datos.getPeriodo());
		this.fechaFinal = modelo.fechaFinal(datos.getEjercicio(),datos.getPeriodo());
		modeloTabla = new ModeloTablaDetalleIngreso(ventana, detalle);
		modeloTabla.addTableModelListener(new CambioTablaListener());
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);

		this.detalle.setCellSelectionEnabled(true);
		CellEditor EditorCeldas= new CellEditor(ventana);
		TableColumn col1 = detalle.getColumnModel().getColumn(1);
	    col1.setCellEditor(EditorCeldas);
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
		this.datosCliente = new ClienteVO();
		this.datosCliente.setIdFormaPago("01");
		this.datosFormaPago = new FormaPagoVO();
		// Campos formulario
		this.fecha = new JDateChooser();
		Date paraFecha= new Date();
		if (fechaFinal.before(paraFecha)){
			paraFecha=fechaFinal;
		}
		this.fecha.setDate(paraFecha);
		this.fecha.setFocusable(false);
		this.idingreso = new JTextField (12);
		this.idingreso.setText(ingresoEditar.getIdIngreso());
		this.formaPago = getComboFormasPago();
		this.descuento = new JTextField (5);
		this.descuento.addActionListener(new DescuentoAL());
		this.descuento.addFocusListener(new DescuentoFL());
		this.numeroCuenta = new JTextField (23);
		this.IRPF = new JTextField (5);
		this.IRPF.setText("18");
		this.IRPF.addActionListener(new IRPFAL());
		this.IRPF.addFocusListener(new IRPFFL());

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
			} else {
				detalle.getSelectionModel().clearSelection();
				actualizarDetalle();
			}
		}	
	};
	
	private void creaVentana(){
		// Nueva ventana
		
		ventana = new JDialog(principal,"Nuevo Ingreso",true);
		
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

		panelIngreso = new JPanel();
		panelIngreso.setLayout(new GridBagLayout());
		GridBagConstraints cgas = new GridBagConstraints();
		cgas.insets = new Insets(3, 3, 3, 3);        
		cgas.gridx = 0;
		cgas.gridy = 0;
		cgas.weightx = 0;
		cgas.weighty = 0;
		cgas.fill = GridBagConstraints.NONE;
		cgas.anchor = GridBagConstraints.LINE_START;

		panelCliente = new JPanel();

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

		// ingreso
		panelIngreso.add(new JLabel("<html><b>Fecha: </b></html>"),cgas);
		cgas.gridx++;
		panelIngreso.add(this.fecha,cgas);
		cgas.gridy++;
		cgas.gridx = 0;
		panelIngreso.add(new JLabel("Número:"),cgas);
		cgas.gridx++;
		panelIngreso.add(this.idingreso,cgas);
		panelSuperior.add(panelIngreso,csup);

		// Cliente
		csup.gridx++;
		actualizarComboCliente();
		visualizarCliente();
		panelSuperior.add(panelCliente,csup);
		String idFP=datosCliente.getIdFormaPago();
		if (idFP.equals("")) idFP="01";
		datosFormaPago=modelo.getFormaPago(idFP);
		ingresoEditar.setIdFormaPago(idFP);
		ingresoEditar.setNumeroCuenta(datosCliente.getNumeroCuenta());

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
		bcuenta=new JButton("+");
		bcuenta.addActionListener(new CuentaAL());
		actualizarComboPago();
		visualizarFormaPago();
		ccen.gridy ++;
		ccen.weighty = 0;
		ccen.weightx = 0;
		ccen.gridwidth = 1;
		ccen.insets = new Insets(5, 20, 0, 20); 
		panelCentral.add(panelFormaPago,ccen);

		// Total
		IRPF.setText(Decimales(ingresoEditar.getTipoIRPF()));
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
		
		// Valores iniciales
		accionesCambioCliente();
				
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
	 * Visualizar panel cliente
	 */
	private void visualizarCliente () {
		String cadena = "";
		panelCliente.setLayout(new GridBagLayout());
		GridBagConstraints cpro = new GridBagConstraints();
		cpro.insets = new Insets(3, 3, 3, 3);        
		cpro.gridx = 0;
		cpro.gridy = 0;
		cpro.weightx = 0;
		cpro.weighty = 0;
		cpro.fill = GridBagConstraints.NONE;
		cpro.anchor = GridBagConstraints.LINE_START;
		panelCliente.add(new JLabel("Nombre:"),cpro);
		cpro.gridx++;
		panelCliente.add(this.cliente,cpro);
		cpro.gridx++;
		JButton bproveedor=new JButton("+");
		bproveedor.addActionListener(new ClienteAL());
		panelCliente.add(bproveedor,cpro);
		cpro.gridx = 0;
		cpro.gridy++;
		panelCliente.add(new JLabel("Dirección:"),cpro);		
		cpro.gridx++;
		if (datosCliente.getDireccion() != null) {
			cadena = this.datosCliente.getDireccion();
		}
		if (datosCliente.getCiudad() != null )
		{
			if (!datosCliente.getCiudad().equals(""))
			{
				cadena += " | "+this.datosCliente.getCiudad();
			}
		}
		cpro.gridwidth=2;
		panelCliente.add(new JLabel(cadena),cpro);
		cpro.gridwidth=1;
		cpro.gridx = 0;
		cpro.gridy++;		
		panelCliente.add(new JLabel("NIF:"),cpro);		
		cpro.gridx++;
		if (datosCliente.getNIF() != null) {
			panelCliente.add(new JLabel("<html><b>"+this.datosCliente.getNIF()+"</b></html>"),cpro);
		}
	}

	/**
	 * Visualizar panel forma de pago
	 */
	private void visualizarFormaPago () {
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
		panelFormaPago.add(this.formaPago,cfor);
		cfor.gridx++;
		JButton bformaPago=new JButton("+");
		bformaPago.addActionListener(new FormaPagoAL());
		panelFormaPago.add(bformaPago,cfor);
		cfor.gridx = 0;
		cfor.gridy++;
		panelFormaPago.add(new JLabel("Número de cuenta:"),cfor);		
		cfor.gridy++;
		cfor.gridwidth = 2;
		cfor.fill = GridBagConstraints.HORIZONTAL;
		panelFormaPago.add(this.numeroCuenta,cfor);
		this.numeroCuenta.setText(this.ingresoEditar.getNumeroCuenta());
		cfor.gridx=3;
		cfor.gridwidth = 1;
		panelFormaPago.add(bcuenta,cfor);
		if (this.datosFormaPago.getNumeroCuenta()){
			 this.numeroCuenta.setEnabled(true);
			 if (modelo.getCuentas().size()>0){
				 bcuenta.setEnabled(true);
			 }
		 } else {
			 this.numeroCuenta.setEnabled(false);
			 bcuenta.setEnabled(false);
		 }		

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
		// Obtener suma por tipos de IVA
		for (int i=0; i<listaDetallesIngreso.size();i++){
			baseImponible = baseImponible + listaDetallesIngreso.get(i).getSubTotal();
			for (int j=0;j<3;j++) {
				if (tipoIVA[j]==null){
					IVA[j] = listaDetallesIngreso.get(i).getSubTotal();
					tipoIVA[j] = listaDetallesIngreso.get(i).getIVA();
					break;
				}
				if (listaDetallesIngreso.get(i).getIVA().equals(tipoIVA[j]))
				{
					IVA[j] += listaDetallesIngreso.get(i).getSubTotal();
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
		cadena="" + Decimales(ingresoEditar.getDescuento());
		ctot.anchor = GridBagConstraints.LINE_END;
		this.descuento.setText(cadena);
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
		if (ingresoEditar.getDescuento()!=null){
			subTotal = baseImponible*ingresoEditar.getDescuento()/100;
			cadena ="-";
		}
		cadena += Decimales(subTotal);
		ctot.weightx = 1;
		panelTotal.add(new JLabel(cadena),ctot);
		ctot.gridx++;
		baseImponible-= subTotal;
		base = baseImponible;
		
		// IVA
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
				baseIVA = IVA[i]*(100-ingresoEditar.getDescuento())/100;
				cadena = "" + Decimales(baseIVA);
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
		ingresoEditar.setIVA1(0f);
		ingresoEditar.setIVA2(0f);
		ingresoEditar.setIVA3(0f);
		ingresoEditar.setBaseImponible1(0f);
		ingresoEditar.setBaseImponible2(0f);
		ingresoEditar.setBaseImponible3(0f);
		if (tipoIVA[0]!=null) ingresoEditar.setIVA1(tipoIVA[0]);
		if (tipoIVA[1]!=null) ingresoEditar.setIVA2(tipoIVA[1]);
		if (tipoIVA[2]!=null) ingresoEditar.setIVA3(tipoIVA[2]);
		if (IVA[0]!=null) ingresoEditar.setBaseImponible1(IVA[0]*(100-ingresoEditar.getDescuento())/100);
		if (IVA[1]!=null) ingresoEditar.setBaseImponible2(IVA[1]*(100-ingresoEditar.getDescuento())/100);
		if (IVA[2]!=null) ingresoEditar.setBaseImponible3(IVA[2]*(100-ingresoEditar.getDescuento())/100);
		
		// IRPF
				ctot.gridx = 0;
				ctot.gridy++;
				ctot.anchor = GridBagConstraints.LINE_START;
				ctot.weightx = 0;
				panelTotal.add(new JLabel("IRPF:  "),ctot);
				ctot.gridx++;
				cadena="" + Decimales(ingresoEditar.getTipoIRPF());
				ctot.anchor = GridBagConstraints.LINE_END;
				this.IRPF.setText(cadena);
				this.IRPF.setHorizontalAlignment(JTextField.RIGHT);
				panelTotal.add(this.IRPF,ctot);
				ctot.gridx++;
				ctot.anchor = GridBagConstraints.LINE_START;
				panelTotal.add(new JLabel("%"),ctot);
				ctot.gridx++;
				cadena = "" + Decimales(baseImponible);
				ctot.anchor = GridBagConstraints.LINE_END;
				panelTotal.add(new JLabel(cadena),ctot);
				ctot.gridx++;
				cadena= " ";
				subTotal=0f;
				if (ingresoEditar.getTipoIRPF()!=0f){
					subTotal = baseImponible*ingresoEditar.getTipoIRPF()/100;
					cadena ="-";
				}
				cadena += Decimales(subTotal);
				ctot.weightx = 1;
				panelTotal.add(new JLabel(cadena),ctot);
				ctot.gridx++;
				base = base - subTotal;
				cadena = "" + Decimales(base);
				panelTotal.add(new JLabel(cadena),ctot);
		
		// TOTAL
		ingresoEditar.setTOTAL(base);
		
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
		for(int i = 0;i<listaDetallesIngreso.size();i++){
			modeloTabla.addDetalleIngreso(listaDetallesIngreso.get(i));
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
		String cadena = descuento.getText();
		cadena=cadena.replace(",",".");
		Boolean err = false;
		try {
			ingresoEditar.setDescuento(Float.parseFloat(cadena));
		} catch (Exception e) {
			err=true;
		}
		if (ingresoEditar.getDescuento()<0 || ingresoEditar.getDescuento()>100) err=true;
		if (err){
			int result = JOptionPane.showConfirmDialog(ventana,
					"El descuento debe ser un número decimal entre 0 y 100", "¡Advertencia!",
					JOptionPane.DEFAULT_OPTION);
			return;
		}
		cadena = IRPF.getText();
		cadena=cadena.replace(",",".");
		err = false;
		try {
			ingresoEditar.setTipoIRPF(Float.parseFloat(cadena));
		} catch (Exception e) {
			err=true;
		}
		if (ingresoEditar.getTipoIRPF()<0 || ingresoEditar.getTipoIRPF()>100) err=true;
		if (err){
			int result = JOptionPane.showConfirmDialog(ventana,
					"El IRPF debe ser un nœmero decimal entre 0 y 100", "¡Advertencia!",
					JOptionPane.DEFAULT_OPTION);
			return;
		}
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
	 * Actualizar cliente
	 */
	private void actualizarCliente(){
		datosCliente = modelo.getCliente((String) cliente.getSelectedItem());
		panelCliente.removeAll();
		visualizarCliente();
		panelCliente.updateUI();
	}
	
	/**
	 * Actualizar combo cliente
	 */
	private void actualizarComboCliente()
	{
		this.cliente = this.getComboClientes(0);
		this.datosCliente=modelo.getCliente((String) cliente.getSelectedItem());
		if (datosCliente.getConIRPF()) ingresoEditar.setTipoIRPF(datosUsuario.getIRPF());
		AutoCompleteDecorator.decorate(this.cliente);
		this.cliente.addActionListener(new cambioCliente());
		addClienteRatonListener(cliente);
	}

	public void avanzaCelda()
    {
		int col = detalle.getSelectedColumn();
		int fil = detalle.getSelectedRow();
        detalle.editCellAt(fil, col+1);
        detalle.transferFocus();
    }
	
	/**
	 * Acciones cambio Cliente
	 */
	private void accionesCambioCliente()
	{
		if (!((String) cliente.getSelectedItem()).equals("Ninguno"))
		{
			actualizarCliente();
			int forma = 0;
			if (!datosCliente.getIdFormaPago().equals("")){
				forma=Integer.parseInt(datosCliente.getIdFormaPago())-1;
				ingresoEditar.setNumeroCuenta(datosCliente.getNumeroCuenta());
				numeroCuenta.setText(datosCliente.getNumeroCuenta());
			} else {
				ingresoEditar.setNumeroCuenta("");	
				numeroCuenta.setText("");
			}
			if (datosCliente.getConIRPF()){
				ingresoEditar.setTipoIRPF(datosUsuario.getIRPF());
				String cadena=""+datosUsuario.getIRPF();
				IRPF.setText(cadena);
			} else {
				IRPF.setText("0,00");
			}
			
			formaPago.setSelectedIndex(forma);
			actualizarTotal();
		}
		cliente.requestFocus();
	}
	
	
	/*
	 * ESCUCHAS
	 */

	/**
	 * Escucha combo de Clientes enter
	 * @author sergio
	 *
	 */
	class cambioCliente implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			JComboBox<String> source = (JComboBox) event.getSource();
			if(!source.isPopupVisible()){
				accionesCambioCliente();
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
				accionesCambioCliente();
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
		private int fila, columna;

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
				fila=detalle.getEditingRow();
				columna=2;
				SeleccionarProducto editarProducto=new SeleccionarProducto(ventana);
				if (editarProducto.producto!=null)
				{
					columna=3;
					listaDetallesIngreso.get(fila).setIdProducto(editarProducto.producto.getIdProducto());
					listaDetallesIngreso.get(fila).setDescripcion(editarProducto.producto.getNombre());
					listaDetallesIngreso.get(fila).setCantidad(1f);
					listaDetallesIngreso.get(fila).setPrecio(editarProducto.producto.getPrecio());
					listaDetallesIngreso.get(fila).setIVA(editarProducto.producto.getIVA());
					float subTotal=editarProducto.producto.getPrecio();
					listaDetallesIngreso.get(fila).setSubTotal(subTotal);
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
			detalle.requestFocus();
			detalle.changeSelection(fila, columna, false, false);
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
			DetalleIngresoVO p = new DetalleIngresoVO();
			p.setIdProducto("");
			p.setDescripcion("");
			p.setCantidad(0f);
			p.setPrecio(0f);
			p.setIVA(0f);
			p.setSubTotal(0f);
			int fila=detalle.getRowCount();
			listaDetallesIngreso.add(p);
			actualizarDetalle();
			detalle.requestFocus();
			detalle.changeSelection(fila, 2, false, false);
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
			ArrayList<DetalleIngresoVO> lista = new ArrayList<DetalleIngresoVO>();
			int num=detalle.getSelectedRow();
			for (int i=0;i<listaDetallesIngreso.size();i++){
				if (i!=num){
					lista.add(listaDetallesIngreso.get(i));
				}
			}
			listaDetallesIngreso=lista;
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
			if (idingreso.getText().length()>8) {
				int result = JOptionPane.showConfirmDialog(ventana,
						"El número de ingreso debe tener un máximo de 8 caracteres", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			if (!ingresoEditar.getIdIngreso().equals(idingreso.getText()) && modelo.idIngresoDuplicado(idingreso.getText())){
				int result = JOptionPane.showConfirmDialog(ventana,
						"Ya existe otro ingreso con ese número", "¡Advertencia!",
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
			if (((String)cliente.getSelectedItem()).equals("Ninguno"))
			{
				int result = JOptionPane.showConfirmDialog(ventana,
						"Es necesario crear al menos un cliente", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			if (listaDetallesIngreso.size()<1){
				int result = JOptionPane.showConfirmDialog(ventana,
						"El asiento no contiene datos", "¡Advertencia!",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			ingresoEditar.setFecha(fecha.getDate());
			ingresoEditar.setIdIngreso(idingreso.getText());
			ingresoEditar.setNombre((String)cliente.getSelectedItem());
			ingresoEditar.setIdCliente(datosCliente.getIdCliente());
			ingresoEditar.setDireccion(datosCliente.getDireccion());
			ingresoEditar.setCP(datosCliente.getCP());
			ingresoEditar.setCiudad(datosCliente.getCiudad());
			ingresoEditar.setProvincia(datosCliente.getProvincia());
			ingresoEditar.setNIF(datosCliente.getNIF());
			String cadena=IRPF.getText();
			cadena=cadena.replace(",",".");
			ingresoEditar.setTipoIRPF(Float.parseFloat(cadena));
			ingresoEditar.setIdFormaPago(datosFormaPago.getIdFormaPago());
			ingresoEditar.setNumeroCuenta(numeroCuenta.getText());
			cadena=descuento.getText();
			cadena=cadena.replace(",",".");
			ingresoEditar.setDescuento(Float.parseFloat(cadena));
			try {
				modelo.guardarIngreso(ingresoEditar,listaDetallesIngreso);
			} catch (Exception e) {
			}			
			cancelado=true;
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
			actualizarTotal();
			return;
		}
	}
	class DescuentoFL implements FocusListener {
		public void focusGained(FocusEvent evento){
		}
		public void focusLost(FocusEvent evento){
			actualizarTotal();
			return;
		}
	}
	
	/**
	 * Escucha campo IRPF
	 * @author sergio
	 *
	 */
	class IRPFAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			actualizarTotal();
			return;
		}
	}
	class IRPFFL implements FocusListener {
		public void focusGained(FocusEvent evento){
		}
		public void focusLost(FocusEvent evento){
			actualizarTotal();
			return;
		}
	}
	
	/**
	 * Escucha boton clientes
	 * @author sergio
	 *
	 */
	class ClienteAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			Object seleccion=cliente.getSelectedItem();
			SeleccionarCliente clienteSeleccionado=new SeleccionarCliente(ventana);
			actualizarComboCliente();
			actualizarCliente();
			if (clienteSeleccionado.getCliente()!=null)
			{
				cliente.setSelectedItem(clienteSeleccionado.getCliente().getNombre());
			} else {
				try {
					cliente.setSelectedItem(seleccion);
				} catch (Exception e) {
					cliente.setSelectedIndex(0);
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
	  * Escucha boton Numero de Cuenta
	  * @author sergio
	  *
	  */
	 class CuentaAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 SeleccionarCuenta cuenta=new SeleccionarCuenta(ventana);
			 if (cuenta.getCuenta()!=null){
				 numeroCuenta.setText(cuenta.getCuenta());
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
		}

		// this function successfully provides cell editing stop
		// on cell losts focus (but another cell doesn't gain focus)
		public void focusLost(FocusEvent e) {
			aFoco=false;
			actualizarEditar();
			detalle.getSelectionModel().clearSelection();

		}
	}


	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}
	
}