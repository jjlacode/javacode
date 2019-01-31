package factunomo.vista.componentes;

import java.awt.BorderLayout;



import java.lang.Object;
import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;

import factunomo.modelo.ComboModel;
import factunomo.modelo.Modelo;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.DetalleIngresoVO;
import factunomo.modelo.obj.ProductoVO;
import factunomo.modelo.obj.ClienteVO;
import factunomo.utils.DateUtils;
import factunomo.vista.componentes.FormularioConsultaGastos.todasFechasCL;
import factunomo.vista.componentes.NuevoGasto.FormaPagoAL;
import factunomo.vista.componentes.NuevoGasto.cambioFormaPago;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


import com.sun.pdfview.*;





public class CopyOfEditarCliente extends FormularioBase{
	 private JDialog ventana;
	 private Container contenedor;
	 private JPanel pabajo;
	 private JPanel panelDetalle;
	 private JPanel panelFormaPago;
	 private String idIngreso;
	 private Modelo modelo;
	 
	 private JTextField idCliente;
	 private JTextField nombre;
	 private JTextField direccion;
	 private JTextField CP;
	 private JTextField ciudad;
	 private JTextField provincia;
	 private JTextField NIF;
	 private JTextField telefono;
	 private JTextField movil;
	 private JTextField fax;
	 private JTextField email;
	 private JTextField web;
	 private JTextField numeroCuenta;
	 
	 private JCheckBox IRPF;
	 private JComboBox formaPago;
	 
	 private boolean cambio;
	 
	 private JDialog principal;
	 private FormaPagoVO datosFormaPago;
	 
	 private ClienteVO cliente;
	 
	 
	 PagePanel panelpdf;
	 PDFFile pdffile;
	 int indice = 0 ;
	 
	 /**
	  * Constructor por defecto.
	  */
	 public CopyOfEditarCliente(JDialog principal,ClienteVO cliente){
		 this.cambio=false;
		 this.cliente=cliente;
		 this.modelo = new Modelo();
		 
		 if (this.cliente.getIdFormaPago()==null||this.cliente.getIdFormaPago().equals(""))
		 {
			 this.datosFormaPago=modelo.getFormaPago("01");
		 } else {
			 this.datosFormaPago=modelo.getFormaPago(this.cliente.getIdFormaPago());
		 }
		 
		 this.principal = principal;
		 
		 this.idCliente=new JTextField(4);
		 this.nombre=new JTextField(50);
		 this.direccion=new JTextField(50);
		 this.CP=new JTextField(5);
		 this.ciudad=new JTextField(50);
		 this.provincia=new JTextField(40);
		 this.NIF=new JTextField(12);
		 this.telefono=new JTextField(25);
		 this.movil=new JTextField(25);
		 this.fax=new JTextField(25);
		 this.email=new JTextField(25);
		 this.web=new JTextField(25);
		 this.numeroCuenta=new JTextField(25);
		 this.IRPF=new JCheckBox("Con IRPF ",false);
		 this.formaPago = this.getComboFormasPago();
		 AutoCompleteDecorator.decorate(this.formaPago);
		 this.panelFormaPago = new JPanel();
		 
		 /**
		  * Colocar foco al abrir
		  * 
		  */
		 EventQueue.invokeLater(new Runnable() {

			 @Override
			 public void run() {
				 nombre.grabFocus();
				 nombre.requestFocus();//or inWindow
			 }
		 });

		 creaVentana();
		 
	 }
	 
	 
	 private void creaVentana(){
		 // Nueva ventana
		 ventana = new JDialog(principal,"Editar Cliente",true);
		 ventana.addWindowListener(new cerrarWin());
		 ventana.setSize(800, 600);
		 ventana.setLocationRelativeTo(null);
		 ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 ventana.setResizable(false);
		 
		 contenedor = ventana.getContentPane();
		 contenedor.setBackground(Color.WHITE);
		 contenedor.setLayout(new BorderLayout());
		 
		 
		 // DATOS
		 panelDetalle = new JPanel();
		 panelDetalle.setLayout(new GridBagLayout());
		 panelDetalle.setBorder(new EmptyBorder(10,0,0,0));
		 GridBagConstraints ct = new GridBagConstraints();
		 ct.insets = new Insets(3, 3, 3, 3);        
		 ct.gridx = 0;
		 ct.gridy = 0;
		 ct.weightx = 0;
		 ct.weighty = 0;
		 ct.fill = GridBagConstraints.NONE;
		 ct.anchor = GridBagConstraints.LINE_START;
		 idCliente.setText(cliente.getIdCliente());
		 panelDetalle.add(new JLabel("ID Cliente:"),ct);
		 ct.gridx++;
		 panelDetalle.add(idCliente,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 nombre.setText(cliente.getNombre());
		 panelDetalle.add(new JLabel("Nombre:"),ct);
		 ct.gridx++;
		 panelDetalle.add(nombre,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 direccion.setText(cliente.getDireccion());
		 panelDetalle.add(new JLabel("Dirección:"),ct);
		 ct.gridx++;
		 panelDetalle.add(direccion,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 CP.setText(cliente.getCP());
		 panelDetalle.add(new JLabel("CP:"),ct);
		 ct.gridx++;
		 panelDetalle.add(CP,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 ciudad.setText(cliente.getCiudad());
		 panelDetalle.add(new JLabel("Localidad:"),ct);
		 ct.gridx++;
		 panelDetalle.add(ciudad,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 provincia.setText(cliente.getProvincia());
		 panelDetalle.add(new JLabel("Provincia:"),ct);
		 ct.gridx++;
		 panelDetalle.add(provincia,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 NIF.setText(cliente.getNIF());
		 panelDetalle.add(new JLabel("CIF:"),ct);
		 ct.gridx++;
		 panelDetalle.add(NIF,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 IRPF.setSelected(cliente.getConIRPF());
		 panelDetalle.add(new JLabel("IRPF:"),ct);
		 ct.gridx++;
		 panelDetalle.add(IRPF,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 telefono.setText(cliente.getTelefono());
		 panelDetalle.add(new JLabel("Teléfono:"),ct);
		 ct.gridx++;
		 panelDetalle.add(telefono,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 movil.setText(cliente.getMovil());
		 panelDetalle.add(new JLabel("Teléfono móvil:"),ct);
		 ct.gridx++;
		 panelDetalle.add(movil,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 fax.setText(cliente.getFax());
		 panelDetalle.add(new JLabel("Fax:"),ct);
		 ct.gridx++;
		 panelDetalle.add(fax,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 email.setText(cliente.getEmail());
		 panelDetalle.add(new JLabel("Email:"),ct);
		 ct.gridx++;
		 panelDetalle.add(email,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 web.setText(cliente.getWeb());
		 panelDetalle.add(new JLabel("Web:"),ct);
		 ct.gridx++;
		 panelDetalle.add(web,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 visualizarFormaPago();
		 this.formaPago.addActionListener(new cambioFormaPago());
		 addFormaPagoRatonListener(formaPago);




		 // BOTONES
		 pabajo = new JPanel();
		 pabajo.setLayout(new FlowLayout());

		 JButton bcerrar=new JButton("Cerrar");
		 bcerrar.addActionListener(new CerrarAL());
		 JButton bguardar=new JButton("Guardar");
		 bguardar.addActionListener(new GuardarAL());
		 
		 pabajo.add(bguardar);
		 pabajo.add(bcerrar);

		 contenedor.add(panelDetalle,BorderLayout.NORTH);
		 contenedor.add(panelFormaPago,BorderLayout.CENTER);
		 contenedor.add(pabajo,BorderLayout.SOUTH);

		 ventana.setVisible(true);


		 
	 }
	 
	 public String getID()
	 {
		 return cliente.getIdCliente();
	 }

	 private void visualizarFormaPago()
	 {
		 panelFormaPago.setLayout(new GridBagLayout());
		 GridBagConstraints cfor = new GridBagConstraints();
		 cfor.gridx = 0;
		 cfor.gridy = 0;
		 cfor.weightx = 0;
		 cfor.weighty = 0;
		 cfor.fill = GridBagConstraints.NONE;
		 cfor.anchor = GridBagConstraints.LINE_START;
		 //formaPago.setSelectedItem(modelo.getFormaPago(cliente.getIdFormaPago()).getNombre());
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
		 cfor.gridwidth = 2;
		 numeroCuenta.setText(cliente.getNumeroCuenta());
		 panelFormaPago.add(new JLabel("Número de cuenta:"),cfor);		
		 cfor.gridy++;
		 switch (this.formaPago.getSelectedIndex()){
		 case 1:
		 case 6:
			 this.numeroCuenta.setEnabled(true);
			 break;
		 default:
			 this.numeroCuenta.setEnabled(false);
		 }
		 cfor.fill = GridBagConstraints.HORIZONTAL;
		 panelFormaPago.add(this.numeroCuenta,cfor);
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
	  * Funcion para consultar el cambio
	  * @return productoVO
	  */
	 public boolean isCambio()
	 {
		 return cambio;
	 }
	 

	 /*
	  * ESCUCHAS
	  * 
	  */
	 
	 /**
		 * Escucha opcion conIRPF
		 * @author sergio
		 *
		 */
	 /*
		class conIRPFCL implements ChangeListener {
			public void stateChanged(ChangeEvent evento){
				if (conIRPF.isSelected())
				{
					IRPF.setEnabled(true);
				} else {
					IRPF.setEnabled(false);
				}
			}
		}
		*/
	 
	 /**
	  * Escucha de botón guardar
	  * @author sergio
	  *
	  */
	 class GuardarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
				if (nombre.getText().equals("")){
					int result = JOptionPane.showConfirmDialog(ventana,
							"El nombre del cliente es obligatorio", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				// Comprobar que el ID es un entero
				try {
					Integer.parseInt(idCliente.getText());
				} catch (Exception e) {
					int result = JOptionPane.showConfirmDialog(ventana,
							"El ID cliente ha de ser un número entero", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					idCliente.requestFocus();
					return;
				}
				if (modelo.idClienteDuplicado(idCliente.getText())&&!modelo.idClienteDuplicado(idCliente.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otro cliente con ese ID", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (modelo.nombreProveedorDuplicado(nombre.getText())&&!modelo.nombreProveedorDuplicado(nombre.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otro cliente con ese nombre", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				/*
				if (conIRPF.isSelected()&&IRPF.getText().equals("")){
					int result = JOptionPane.showConfirmDialog(ventana,
							"No ha indicado porcentaje de IRPF", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				*/
				cliente.setIdCliente(idCliente.getText());
				cliente.setNombre(nombre.getText());
				cliente.setDireccion(direccion.getText());
				cliente.setCP(CP.getText());
				cliente.setCiudad(ciudad.getText());
				cliente.setProvincia(provincia.getText());
				cliente.setNIF(NIF.getText());
				cliente.setConIRPF(IRPF.isSelected());
				cliente.setTelefono(telefono.getText());
				cliente.setMovil(movil.getText());
				cliente.setFax(fax.getText());
				cliente.setEmail(email.getText());
				cliente.setWeb(web.getText());
				/*
				cliente.setConIRPF(conIRPF.isSelected());
				cliente.setIRPF(0f);
				if (conIRPF.isSelected())
				{
					cliente.setIRPF(Float.parseFloat(IRPF.getText()));
				}
				*/
				System.out.println(cliente.getConIRPF());
				cliente.setIdFormaPago(datosFormaPago.getIdFormaPago());
				cliente.setNumeroCuenta(numeroCuenta.getText());
				modelo.actualizarCliente(cliente);
				cambio=true;
				ventana.dispose();
			}
	 }
	 
	 /**
	  * Escucha de botón cerrar
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
			    principal.setEnabled(true);
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
			 new NuevoFormaPago(ventana);
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
				 System.out.println("mouseClicked");
			 }

			 public void mousePressed(MouseEvent mouseEvent) {
				 System.out.println("mousePressed");
			 }

			 public void mouseReleased(MouseEvent mouseEvent) {
				 actualizarFormaPago();

			 }

			 public void mouseEntered(MouseEvent mouseEvent) {
				 System.out.println("mouseEntered");
			 }

			 public void mouseExited(MouseEvent mouseEvent) {
				 System.out.println("mouseExited");
			 }
		 };
	 }
	 
	 

	 class cerrarWin extends WindowAdapter
	 {
		 public void windowClosing(WindowEvent e)
	   {
	    	String sFichero = "elemento.pdf";
			File fichero = new File(sFichero);
		    if (fichero.exists())
		        fichero.delete();
		    ventana.dispose();
	   }
	 }
	 
	 public String Decimales (Float numero){
			DecimalFormat df = new DecimalFormat("0.00");
			String respuesta = df.format(numero);
			return respuesta;

		}
	 
}