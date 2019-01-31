package factunomo.vista.componentes;

import java.awt.BorderLayout;




import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.text.DecimalFormat;

import factunomo.modelo.Modelo;
import factunomo.modelo.obj.ProductoVO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;




public class NuevoProducto extends FormularioBase{
	 private JDialog ventana;
	 private Container contenedor;
	 private JPanel pabajo;
	 private JPanel panelDetalle;
	 private Modelo modelo;
	 
	 private JTextField nombre;
	 private JTextField precio;
	 private JTextField IVA;
	 private JTextField codigo;
	 
	 private JDialog principal;
	 private ProductoVO producto;
	 
	 
	 /**
	  * Constructor por defecto.
	  */
	 public NuevoProducto(JDialog principal){
		 this.modelo = new Modelo();
		 this.principal = principal;
	 
		 this.nombre=new JTextField(40);
		 this.codigo=new JTextField(3);
		 this.precio=new JTextField(6);
		 this.IVA=new JTextField(4);
		 this.producto=new ProductoVO();
		 
		 
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
		 ventana = new JDialog(principal,"Nuevo Producto",true);
		 ventana.addWindowListener(new cerrarWin());
		 ventana.setSize(620, 250);
		 ventana.setLocationRelativeTo(null);
		 ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 ventana.setResizable(false);
		 
		 contenedor = ventana.getContentPane();
		 contenedor.setBackground(Color.WHITE);
		 contenedor.setLayout(new BorderLayout());
		 
		 
		 // DATOS
		 panelDetalle = new JPanel();
		 panelDetalle.setLayout(new GridBagLayout());
		 panelDetalle.setBorder(new EmptyBorder(5,0,0,0));
		 GridBagConstraints ct = new GridBagConstraints();
		 ct.insets = new Insets(3, 3, 3, 3);        
		 ct.gridx = 0;
		 ct.gridy = 0;
		 ct.weightx = 0;
		 ct.weighty = 0;
		 ct.fill = GridBagConstraints.NONE;
		 ct.anchor = GridBagConstraints.LINE_START;
		 codigo.setText(modelo.nuevoIdProducto());
		 panelDetalle.add(new JLabel("Código:"),ct);
		 ct.gridx++;
		 panelDetalle.add(codigo,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 panelDetalle.add(new JLabel("Descripción:"),ct);
		 ct.gridx++;
		 panelDetalle.add(nombre,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 nombre.requestFocus();
		 panelDetalle.add(new JLabel("Precio:"),ct);
		 ct.gridx++;
		 panelDetalle.add(precio,ct);
		 ct.gridx = 0;
		 ct.gridy++;
		 panelDetalle.add(new JLabel("IVA:"),ct);
		 ct.gridx++;
		 panelDetalle.add(IVA,ct);
		 ct.gridx = 0;
		 ct.gridy++;




		 // BOTONES
		 pabajo = new JPanel();
		 pabajo.setLayout(new FlowLayout());

		 JButton bcerrar=new JButton("Cerrar");
		 bcerrar.addActionListener(new CerrarAL());
		 JButton bguardar=new JButton("Guardar");
		 bguardar.addActionListener(new GuardarAL());
		 
		 pabajo.add(bguardar);
		 pabajo.add(bcerrar);

		 contenedor.add(panelDetalle,BorderLayout.CENTER);
		 contenedor.add(pabajo,BorderLayout.SOUTH);
		 ventana.setVisible(true);
	 }

	 
	 /**
	  * Devuelve el producto creado
	  * @return productoVO
	  */
	 public ProductoVO getProducto()
	 {
		 return producto;
	 }
	 


	 /*
	  * ESCUCHAS
	  * 
	  */
	 
	 
	 /**
	  * Escucha de botón guardar
	  * @author sergio
	  *
	  */
	 class GuardarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 	// Comprobar que el nombre no está vacío
				if (nombre.getText().equals("")){
					int result = JOptionPane.showConfirmDialog(ventana,
							"El nombre del producto es obligatorio", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					nombre.requestFocus();
					return;
				}
				// Comprobar que el código es un entero
				try {
					Integer.parseInt(codigo.getText());
				} catch (Exception e) {
					int result = JOptionPane.showConfirmDialog(ventana,
							"El código ha de ser un número entero de tres cifras", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					codigo.requestFocus();
					return;
				}
				// Comprobar que el precio es un decimal y poner a 0 si vacío
				if (!precio.getText().equals(""))
				{
					try {
						Float.parseFloat(precio.getText());
					} catch (Exception e) {
						int result = JOptionPane.showConfirmDialog(ventana,
								"El precio ha de ser un número decimal", "¡Advertencia!",
								JOptionPane.DEFAULT_OPTION);
						precio.requestFocus();
						return;
					}
					producto.setPrecio(Float.parseFloat(precio.getText()));
				} else producto.setPrecio(0f);
				// Comprobar que el IVA es un decimal y poner a 0 si vacío
				if (!IVA.getText().equals(""))
				{
					try {
						Float.parseFloat(IVA.getText());
					} catch (Exception e) {
						int result = JOptionPane.showConfirmDialog(ventana,
								"El IVA ha de ser un número decimal", "¡Advertencia!",
								JOptionPane.DEFAULT_OPTION);
						IVA.requestFocus();
						return;
					}
					producto.setIVA(Float.parseFloat(IVA.getText()));
				} else producto.setIVA(0f);
				// Comprobar si el código ya existe
				if (modelo.idProductoDuplicado(codigo.getText())){
					int result = JOptionPane.showConfirmDialog(ventana,
							"Ya existe otro producto con ese nombre", "¡Advertencia!",
							JOptionPane.DEFAULT_OPTION);
					codigo.requestFocus();
					return;
				}
				// Operaciones de guardado
				producto.setIdProducto(codigo.getText());
				producto.setNombre(nombre.getText());	
				modelo.guardarProducto(producto);
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
			 producto=null;
			 ventana.dispose();
			 return;
		 }
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