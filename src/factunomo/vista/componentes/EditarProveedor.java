package factunomo.vista.componentes;

import java.awt.BorderLayout;



import java.lang.Object;
import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.Container;
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


import factunomo.modelo.Modelo;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.obj.ProveedorVO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


import com.sun.pdfview.*;





public class EditarProveedor extends FormularioBase{
	private static final long serialVersionUID = 1L;
	 private JDialog ventana;
	 private Container contenedor;
	 private JPanel pabajo;
	 private JPanel panelDetalle;
	 private JPanel panelFormaPago;
	 private Modelo modelo;
	 
	 private JTextField idProveedor;
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
	 private JCheckBox conIRPF;

	 private JComboBox<String> formaPago;
	 
	 private JDialog principal;
	 private FormaPagoVO datosFormaPago;
	 
	 private ProveedorVO proveedor;
	 
	 
	 PagePanel panelpdf;
	 PDFFile pdffile;
	 int indice = 0 ;
	 
	 /**
	  * Constructor por defecto.
	  */
	 public EditarProveedor(JDialog principal,ProveedorVO proveedor){
		 this.proveedor=proveedor;
		 this.modelo = new Modelo();
		 
		 if (this.proveedor.getIdFormaPago()==null||this.proveedor.getIdFormaPago().equals(""))
		 {
			 this.datosFormaPago=modelo.getFormaPago("01");
		 } else {
			 this.datosFormaPago=modelo.getFormaPago(this.proveedor.getIdFormaPago());
		 }
		 
		 this.principal = principal;
		 
		 this.idProveedor=new JTextField(4);
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
		 this.conIRPF=new JCheckBox("",false);
		 this.datosFormaPago=modelo.getFormaPago("01");
		 
		// Colocar forma pago
				 if (this.proveedor.getIdFormaPago()==null||this.proveedor.getIdFormaPago().equals(""))
				 {
					 this.datosFormaPago=modelo.getFormaPago("01");
				 } else {
					 this.datosFormaPago=modelo.getFormaPago(this.proveedor.getIdFormaPago());
				 }
		 
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
		 ventana = new JDialog(principal,"Editar Proveedor",true);
		 ventana.addWindowListener(new cerrarWin());
		 ventana.setSize(800, 500);
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
		 idProveedor.setText(proveedor.getIdProveedor());
		 panelDetalle.add(new JLabel("ID Proveedor:"),ct);
		 ct.gridx++;
		 panelDetalle.add(idProveedor,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 nombre.setText(proveedor.getNombre());
		 panelDetalle.add(new JLabel("Nombre:"),ct);
		 ct.gridx++;
		 panelDetalle.add(nombre,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 direccion.setText(proveedor.getDireccion());
		 panelDetalle.add(new JLabel("Direcci�n:"),ct);
		 ct.gridx++;
		 panelDetalle.add(direccion,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 CP.setText(proveedor.getCP());
		 panelDetalle.add(new JLabel("CP:"),ct);
		 ct.gridx++;
		 panelDetalle.add(CP,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 ciudad.setText(proveedor.getCiudad());
		 panelDetalle.add(new JLabel("Localidad:"),ct);
		 ct.gridx++;
		 panelDetalle.add(ciudad,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 provincia.setText(proveedor.getProvincia());
		 panelDetalle.add(new JLabel("Provincia:"),ct);
		 ct.gridx++;
		 panelDetalle.add(provincia,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 NIF.setText(proveedor.getNIF());
		 panelDetalle.add(new JLabel("CIF:"),ct);
		 ct.gridx++;
		 panelDetalle.add(NIF,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 telefono.setText(proveedor.getTelefono());
		 panelDetalle.add(new JLabel("Tel�fono:"),ct);
		 ct.gridx++;
		 panelDetalle.add(telefono,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 movil.setText(proveedor.getMovil());
		 panelDetalle.add(new JLabel("Tel�fono m�vil:"),ct);
		 ct.gridx++;
		 panelDetalle.add(movil,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 fax.setText(proveedor.getFax());
		 panelDetalle.add(new JLabel("Fax:"),ct);
		 ct.gridx++;
		 panelDetalle.add(fax,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 email.setText(proveedor.getEmail());
		 panelDetalle.add(new JLabel("Email:"),ct);
		 ct.gridx++;
		 panelDetalle.add(email,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 web.setText(proveedor.getWeb());
		 panelDetalle.add(new JLabel("Web:"),ct);
		 ct.gridx++;
		 panelDetalle.add(web,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 panelDetalle.add(new JLabel("Con IRPF:"),ct);
		 ct.gridx++;
		 conIRPF.setSelected(proveedor.getConIRPF());
		 panelDetalle.add(conIRPF,ct);
		 
		 // Forma de pago
		 panelFormaPago = new JPanel();
		 actualizarComboPago();
		 visualizarFormaPago();


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
		 
		 formaPago.setSelectedItem(modelo.getFormaPago(proveedor.getIdFormaPago()).getNombre());
		 
		 ventana.setVisible(true);


		 
	 }
	 
	 public ProveedorVO getProveedor()
	 {
		 return proveedor;
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
		 panelFormaPago.add(new JLabel("Forma de pago:"),cfor);
		 cfor.gridy++;
		 panelFormaPago.add(this.formaPago,cfor);
		 cfor.gridx++;
		 JButton bformaPago=new JButton("+");
		 bformaPago.addActionListener(new FormaPagoAL());
		 panelFormaPago.add(bformaPago,cfor);
		 cfor.gridx = 0;
		 cfor.gridy++;
		 cfor.gridwidth = 2;
		 numeroCuenta.setText(proveedor.getNumeroCuenta());
		 panelFormaPago.add(new JLabel("N�mero de cuenta:"),cfor);		
		 cfor.gridy++;
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

	 

	 /*
	  * ESCUCHAS
	  * 
	  */
	 
	 
	 /**
	  * Escucha de bot�n guardar
	  * @author sergio
	  *
	  */
	 class GuardarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
				if (nombre.getText().equals("")){
					int result = JOptionPane.showConfirmDialog(ventana,
							"El nombre del proveedor es obligatorio", "�Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				// Comprobar que el ID es un entero
				try {
					Integer.parseInt(idProveedor.getText());
				} catch (Exception e) {
					int result = JOptionPane.showConfirmDialog(ventana,
							"El ID proveedor ha de ser un n�mero entero", "�Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					idProveedor.requestFocus();
					return;
				}
				if (modelo.idProveedorDuplicado(idProveedor.getText())&&!modelo.idProveedorDuplicado(idProveedor.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otro proveedor con ese ID", "�Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (modelo.nombreProveedorDuplicado(nombre.getText())&&!modelo.nombreProveedorDuplicado(nombre.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otro proveedor con ese nombre", "�Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				proveedor.setIdProveedor(idProveedor.getText());
				proveedor.setNombre(nombre.getText());
				proveedor.setDireccion(direccion.getText());
				proveedor.setCP(CP.getText());
				proveedor.setCiudad(ciudad.getText());
				proveedor.setProvincia(provincia.getText());
				proveedor.setNIF(NIF.getText());
				proveedor.setTelefono(telefono.getText());
				proveedor.setMovil(movil.getText());
				proveedor.setFax(fax.getText());
				proveedor.setEmail(email.getText());
				proveedor.setWeb(web.getText());
				proveedor.setConIRPF(conIRPF.isSelected());
				proveedor.setIdFormaPago(datosFormaPago.getIdFormaPago());
				proveedor.setNumeroCuenta(numeroCuenta.getText());
				modelo.actualizarProveedor(proveedor);
				ventana.dispose();
			}
	 }
	 
	 /**
	  * Escucha de bot�n cerrar
	  * @author sergio
	  *
	  */
	 class CerrarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 proveedor=null;
			 ventana.dispose();
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
				 actualizarFormaPago();
			 }
		 }       
	 }

	 /**
	  * A�adir Escucha combo FormaPago raton
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
	  * Escucha del combo de formaPago Rat�n
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
	 
	 

	 class cerrarWin extends WindowAdapter
	 {
		 public void windowClosing(WindowEvent e)
	   {
			proveedor=null;
		    ventana.dispose();
	   }
	 }
	 
}