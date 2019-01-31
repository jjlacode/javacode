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
import factunomo.modelo.ModeloTablaProveedor;
import factunomo.modelo.obj.ClienteVO;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.ProveedorVO;
import factunomo.utils.DateUtils;
import factunomo.vista.componentes.FormularioConsultaGastos.MouseTablaGastos;
import factunomo.vista.componentes.NuevoGasto.ButtonEditor;
import factunomo.vista.componentes.NuevoGasto.ButtonRenderer;
import factunomo.vista.componentes.NuevoGasto.CambioTablaListener;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;


import com.sun.pdfview.*;





public class SeleccionarProveedor extends FormularioBase{
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel parriba;
	private JPanel panelDetalle;
	private JTable detalle;
	private Modelo modelo;
	
	private JDialog principal;
	private ArrayList<ProveedorVO> listaProveedores;
	public ProveedorVO proveedor;

	private JButton bnuevo;
	private JButton beditar;
	private JButton beliminar;
	private JButton bseleccionar;

	protected ModeloTablaProveedor modeloTabla;


	PagePanel panelpdf;
	PDFFile pdffile;
	int indice = 0 ;

	/**
	 * Constructor por defecto.
	 */
	public SeleccionarProveedor(JDialog principal){
		this.modelo=new Modelo();
		this.principal = principal;
		this.proveedor=null;
		this.listaProveedores = new ArrayList<ProveedorVO>();
		this.listaProveedores = modelo.getProveedores();
		modeloTabla = new ModeloTablaProveedor(ventana, detalle);
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);
		this.detalle.addMouseListener(new MouseTablaGastos());
		creaVentana();

	}


	private void creaVentana(){
		// Nueva ventana
		ventana = new JDialog(principal,"Seleccionar Proveedor",true);
		ventana.addWindowListener(new cerrarWin());
		ventana.setSize(550, 500);
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
		ventana.setVisible(true);
	}

	
	/**
	 * Llena la tabla de proveedores
	 */	
	private void llenaLista(){
		modeloTabla.borraTabla();
		for(int i = 0;i<listaProveedores.size();i++){
			modeloTabla.addProveedor(listaProveedores.get(i));
		}		
	}
	
	/**
	 * Llena la tabla de proveedores
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
		detalle.getColumnModel().getColumn(0).setMaxWidth(50);
		detalle.getColumnModel().getColumn(1).setMinWidth(200);

		JScrollPane lista = new JScrollPane(this.detalle);
		this.listaProveedores = modelo.getProveedores();
		llenaLista();
		panelDetalle.add(lista,cdet);
	}

	/** Funcion que devuelve el proveedor seleccionado
	 * 
	 */
	public ProveedorVO getProveedor()
	{
		return proveedor;
	}
	
	/**
	 * Dialogo de aviso de modificación
	 */
	public boolean dialogoConfirm()
	{
		int selected = JOptionPane.showConfirmDialog(principal,
			    "Si modifica los datos de un proveedor y edita posteriormente un gasto ya creado\nlos datos de proveedor cambiarán",
			    "¡Advertencia!",
			    JOptionPane.YES_NO_OPTION);
		if (selected==JOptionPane.YES_OPTION) return true;
		return false;
	}

	/*
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
			proveedor=null;
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
			NuevoProveedor proveedor=new NuevoProveedor(ventana);
			if (proveedor.getProveedor()!=null)
			{
				actualizarProductos();
				beditar.setEnabled(true);
				beliminar.setEnabled(true);
				bseleccionar.setEnabled(true);
				int fila=-1;
				for (int i=0;i<listaProveedores.size();i++)
				{
					if (listaProveedores.get(i).getIdProveedor().equals(proveedor.getProveedor().getIdProveedor()))
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
			if (!dialogoConfirm()) return;
			EditarProveedor proveedor=new EditarProveedor(ventana, listaProveedores.get(detalle.getSelectedRow()));
			if (proveedor.getProveedor()!=null)
			{
				actualizarProductos();
				int fila=-1;
				for (int i=0;i<listaProveedores.size();i++)
				{
					if (listaProveedores.get(i).getIdProveedor().equals(proveedor.getProveedor().getIdProveedor()))
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
			if (!dialogoConfirm()) return;
			modelo.eliminarProveedor(listaProveedores.get(detalle.getSelectedRow()).getIdProveedor());
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


	/**
	 * Guardar y salir
	 * @author sergio
	 *
	 */
	private void salir()
	{
		proveedor=listaProveedores.get(detalle.getSelectedRow());
		ventana.dispose();
	}

	class cerrarWin extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			proveedor=null;
			ventana.dispose();
		}
	}

	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}

}