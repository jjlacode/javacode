package factunomo.vista.componentes;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;

import factunomo.modelo.Modelo;
import factunomo.modelo.ModeloTablaCliente;
import factunomo.modelo.obj.ClienteVO;

import javax.swing.*;


import com.sun.pdfview.*;





public class SeleccionarCliente extends FormularioBase{
	private static final long serialVersionUID = 1L;
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel parriba;
	private JPanel panelDetalle;
	private JTable detalle;
	private Modelo modelo;
	
	private JDialog principal;
	private ArrayList<ClienteVO> listaClientes;
	public ClienteVO cliente;

	private JButton bnuevo;
	private JButton beditar;
	private JButton beliminar;
	private JButton bseleccionar;
	

	protected ModeloTablaCliente modeloTabla;


	PagePanel panelpdf;
	PDFFile pdffile;
	int indice = 0 ;

	/**
	 * Constructor por defecto.
	 */
	public SeleccionarCliente(JDialog principal){
		this.modelo=new Modelo();
		this.principal = principal;
		this.cliente=null;
		this.listaClientes = new ArrayList<ClienteVO>();
		this.listaClientes = modelo.getClientes();
		modeloTabla = new ModeloTablaCliente(ventana, detalle);
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);
		this.detalle.addMouseListener(new MouseTablaClientes());
		creaVentana();
	}


	private void creaVentana(){
		// Nueva ventana
		ventana = new JDialog(principal,"Seleccionar Cliente",true);
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
	 * Llena la tabla de clientes
	 */	
	private void llenaLista(){
		modeloTabla.borraTabla();
		for(int i = 0;i<listaClientes.size();i++){
			modeloTabla.addCliente(listaClientes.get(i));
		}		
	}
	
	/**
	 * Llena la tabla de clientes
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
		this.listaClientes = modelo.getClientes();
		llenaLista();
		panelDetalle.add(lista,cdet);
	}

	/** Funcion que devuelve el cliente seleccionado
	 * 
	 */
	public ClienteVO getCliente()
	{
		return cliente;
	}
	
	/**
	 * Dialogo de aviso de modificación
	 */
	public boolean dialogoConfirm()
	{
		int selected = JOptionPane.showConfirmDialog(principal,
			    "Si modifica los datos de un cliente y edita posteriormente una factura ya emitida\nlos datos de cliente cambiarán",
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
			NuevoCliente cliente=new NuevoCliente(ventana);
			if (cliente.getCliente()!=null)
			{
				actualizarProductos();
				beditar.setEnabled(true);
				beliminar.setEnabled(true);
				bseleccionar.setEnabled(true);
				int fila=-1;
				for (int i=0;i<listaClientes.size();i++)
				{
					if (listaClientes.get(i).getIdCliente().equals(cliente.getCliente().getIdCliente()))
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
			EditarCliente cliente=new EditarCliente(ventana, listaClientes.get(detalle.getSelectedRow()));
			if (cliente.getCliente()!=null)
			{
				actualizarProductos();
				int fila=-1;
				for (int i=0;i<listaClientes.size();i++)
				{
					if (listaClientes.get(i).getIdCliente().equals(cliente.getCliente().getIdCliente()))
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
			modelo.eliminarCliente(listaClientes.get(detalle.getSelectedRow()).getIdCliente());
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
	public class MouseTablaClientes extends MouseAdapter {
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
		cliente=listaClientes.get(detalle.getSelectedRow());
		ventana.dispose();
	}

	class cerrarWin extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			cliente=null;
			ventana.dispose();
		}
	}

	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}

}