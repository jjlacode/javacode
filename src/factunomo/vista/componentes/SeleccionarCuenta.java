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
import java.util.ArrayList;

import factunomo.modelo.Modelo;
import factunomo.modelo.ModeloTablaCuenta;

import javax.swing.*;


import com.sun.pdfview.*;





public class SeleccionarCuenta extends FormularioBase{
	private static final long serialVersionUID = 1L;
	private JDialog ventana;
	private Container contenedor;
	private JPanel pabajo;
	private JPanel panelDetalle;
	private JTable detalle;
	private Modelo modelo;
	
	private JDialog principal;
	private ArrayList<String> listaCuentas;
	public String cuenta;

	private JButton bseleccionar;
	

	protected ModeloTablaCuenta modeloTabla;


	PagePanel panelpdf;
	PDFFile pdffile;
	int indice = 0 ;

	/**
	 * Constructor por defecto.
	 */
	public SeleccionarCuenta(JDialog principal){
		this.modelo=new Modelo();
		this.principal = principal;
		this.cuenta=null;
		this.listaCuentas = new ArrayList<String>();
		modeloTabla = new ModeloTablaCuenta(ventana, detalle);
		this.detalle  = new JTable();
		this.detalle.setModel(modeloTabla);
		this.detalle.setSurrendersFocusOnKeystroke(true);
		this.detalle.addMouseListener(new MouseTablaClientes());
		creaVentana();
	}


	private void creaVentana(){
		// Nueva ventana
		ventana = new JDialog(principal,"Seleccionar Cuenta",true);
		ventana.addWindowListener(new cerrarWin());
		ventana.setSize(300, 150);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.setResizable(false);
		contenedor = ventana.getContentPane();
		contenedor.setBackground(Color.WHITE);
		contenedor.setLayout(new BorderLayout());

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

		contenedor.add(panelDetalle,BorderLayout.CENTER);
		contenedor.add(pabajo,BorderLayout.SOUTH);
		ventana.setVisible(true);

	}

	
	/**
	 * Llena la tabla de clientes
	 */	
	private void llenaLista(){
		modeloTabla.borraTabla();
		for(int i = 0;i<listaCuentas.size();i++){
			modeloTabla.addCuenta(listaCuentas.get(i));
		}		
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
		detalle.getColumnModel().getColumn(0).setMaxWidth(300);
		JScrollPane lista = new JScrollPane(this.detalle);
		this.listaCuentas = modelo.getCuentas();
		llenaLista();
		panelDetalle.add(lista,cdet);
	}

	/** Funcion que devuelve el cliente seleccionado
	 * 
	 */
	public String getCuenta()
	{
		return cuenta;
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
		cuenta=listaCuentas.get(detalle.getSelectedRow());
		ventana.dispose();
	}

	class cerrarWin extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			ventana.dispose();
		}
	}

}