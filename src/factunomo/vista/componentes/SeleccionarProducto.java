package factunomo.vista.componentes;

import java.awt.BorderLayout;



import java.lang.Object;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import factunomo.modelo.ModeloTablaProducto;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.ProductoVO;
import factunomo.utils.DateUtils;
import factunomo.vista.componentes.FormularioConsultaGastos.MouseTablaGastos;
import factunomo.vista.componentes.NuevoGasto.ButtonEditor;
import factunomo.vista.componentes.NuevoGasto.ButtonRenderer;
import factunomo.vista.componentes.NuevoGasto.CambioTablaListener;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;


import com.sun.pdfview.*;





public class SeleccionarProducto extends FormularioBase{
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel parriba;
	private JPanel panelDetalle;
	private JTable detalle;
	private String idGasto;
	private Modelo modelo;

	private JDialog principal;
	private GastoVO gastoEditar;
	private ArrayList<ProductoVO> listaProductos;
	private ConfiguracionVO datosFactunomo;
	public ProductoVO producto;

	private JTextField idgasto;
	private JDateChooser fecha;

	private JButton bnuevo;
	private JButton beditar;
	private JButton beliminar;
	private JButton bseleccionar;

	protected ModeloTablaProducto modeloTabla;


	PagePanel panelpdf;
	PDFFile pdffile;
	int indice = 0 ;

	/**
	 * Constructor por defecto.
	 */
	public SeleccionarProducto(JDialog principal){
		this.modelo=new Modelo();
		this.principal = principal;
		this.producto=null;
		this.listaProductos = new ArrayList<ProductoVO>();
		this.listaProductos = modelo.getProductos();

		this.fecha = new JDateChooser();

		modeloTabla = new ModeloTablaProducto(ventana, detalle);
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);
		this.detalle.addMouseListener(new MouseTablaGastos());
		creaVentana();

	}


	private void creaVentana(){
		// Nueva ventana
		ventana = new JDialog(principal,"Seleccionar Producto",true);
		ventana.addWindowListener(new cerrarWin());
		ventana.setSize(450, 500);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.setResizable(false);

		contenedor = ventana.getContentPane();
		contenedor.setBackground(Color.WHITE);
		contenedor.setLayout(new BorderLayout());

		parriba = new JPanel();
		parriba.setLayout(new FlowLayout());
		bnuevo=new JButton("Nuevo");
		bnuevo.addActionListener(new NuevoAL());
		beditar=new JButton("Editar");
		beditar.addActionListener(new EditarAL());
		beliminar=new JButton("Eliminar");
		beliminar.addActionListener(new EliminarAL());
		beditar.setEnabled(false);
		beliminar.setEnabled(false);
		parriba.add(bnuevo);
		parriba.add(beditar);
		parriba.add(beliminar);

		// DATOS
		panelDetalle = new JPanel();
		visualizarDetalle();



		// BOTONES
		pabajo = new JPanel();
		pabajo.setLayout(new FlowLayout());

		JButton bcerrar=new JButton("Cerrar");
		bcerrar.addActionListener(new CerrarAL());
		bseleccionar=new JButton("Seleccionar");
		bseleccionar.addActionListener(new SeleccionarAL());
		bseleccionar.setEnabled(false);

		pabajo.add(bseleccionar);
		pabajo.add(bcerrar);

		contenedor.add(parriba,BorderLayout.NORTH);
		contenedor.add(panelDetalle,BorderLayout.CENTER);
		contenedor.add(pabajo,BorderLayout.SOUTH);
		//ventana.pack();
		ventana.setVisible(true);



	}

	
	/**
	 * Llena la tabla de productos
	 */	
	private void llenaLista(){
		modeloTabla.borraTabla();
		for(int i = 0;i<listaProductos.size();i++){
			modeloTabla.addProducto(listaProductos.get(i));
		}		
	}
	
	/**
	 * Llena la tabla de productos
	 */	
	private void actualizarProductos(){
		panelDetalle.removeAll();
		visualizarDetalle();
		panelDetalle.updateUI();
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
		detalle.getColumnModel().getColumn(0).setMaxWidth(80);
		detalle.getColumnModel().getColumn(1).setMinWidth(250);

		JScrollPane lista = new JScrollPane(this.detalle);
		this.listaProductos = modelo.getProductos();
		llenaLista();
		panelDetalle.add(lista,cdet);
	}



	/***
	 * ESCUCHAS
	 * @author sergio
	 *
	 */

	/**
	 * Escucha botón seleccionar
	 * @author sergio
	 *
	 */
	class SeleccionarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			salir();
			return;
		}
	}

	/**
	 * Escucha botón cerrar
	 * @author sergio
	 *
	 */
	class CerrarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			ventana.dispose();
			return;
		}
	}

	/**
	 * Escucha botón nuevo
	 * @author sergio
	 *
	 */
	class NuevoAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			NuevoProducto producto=new NuevoProducto(ventana);
			if (producto.getProducto()!=null)
			{
				actualizarProductos();
				beditar.setEnabled(true);
				beliminar.setEnabled(true);
				bseleccionar.setEnabled(true);
				int fila=-1;
				for (int i=0;i<listaProductos.size();i++)
				{
					if (listaProductos.get(i).getIdProducto().equals(producto.getProducto().getIdProducto()))
					{
						fila=i;
					}
				}
				detalle.changeSelection(fila, 0, false, false);
			}
			return;
		}
	}

	/**
	 * Escucha botón editar
	 * @author sergio
	 *
	 */
	class EditarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			EditarProducto producto=new EditarProducto(ventana, listaProductos.get(detalle.getSelectedRow()));
			if (producto.isCambio())
			{
				actualizarProductos();
				int fila=-1;
				for (int i=0;i<listaProductos.size();i++)
				{
					if (listaProductos.get(i).getIdProducto().equals(producto.getIdProducto()))
					{
						fila=i;
					}
				}
				detalle.changeSelection(fila, 0, false, false);
			}			
			return;
		}
	}

	/**
	 * Escucha botón eliminar
	 * @author sergio
	 *
	 */
	class EliminarAL implements ActionListener {
		public void actionPerformed(ActionEvent evento){
			modelo.eliminarProducto(listaProductos.get(detalle.getSelectedRow()).getIdProducto());
			actualizarProductos();
			beditar.setEnabled(false);
			beliminar.setEnabled(false);
			bseleccionar.setEnabled(false);
			detalle.getSelectionModel().clearSelection();
			return;
		}
	}


	/**
	 * Escucha ratón tabla
	 * @author sergio
	 *
	 */
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
				bseleccionar.setEnabled(true);
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					salir();
				}
			}
		}
	}

	/*
	 class editarCombo extends ActionListener
	 {
	    public void actionPerformed(ActionEvent evento){
	    	String sFichero = "elemento.pdf";
	   }
	 }
	 */

	/**
	 * Guardar y salir
	 * @author sergio
	 *
	 */
	private void salir()
	{
		producto=listaProductos.get(detalle.getSelectedRow());
		ventana.dispose();
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