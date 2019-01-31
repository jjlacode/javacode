package factunomo.modelo;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.*;

import java.awt.KeyboardFocusManager;

import javax.swing.event.*;

import factunomo.modelo.obj.ProductoVO;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ModeloTablaProducto implements TableModel {

	/**
	 * Lista con los datos. Cada elemento de la lista es una instancia de
	 * DetalleGasto
	 */
	private ArrayList<ProductoVO> datos = new ArrayList<ProductoVO>();
	/**
	 * Lista de suscriptores. El JTable será un suscriptor de este modelo de
	 * datos
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	JDialog ventana;
	JTable tabla;
	
	/**
	 * Constructor
	 * @param ventana
	 * @param tabla
	 */
	public ModeloTablaProducto(JDialog ventana, JTable tabla){
		this.ventana = ventana;
		this.tabla = tabla;
	}

	
	
	
	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return datos.size();
	}
	
	
	public ProductoVO getProducto(int rowIndex) {
		return (ProductoVO) (datos.get(rowIndex));
	}
	
	public void addTableModelListener(TableModelListener l) {
		// Añade el suscriptor a la lista de suscriptores
		listeners.add(l);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ProductoVO aux = (ProductoVO) (datos.get(rowIndex));
		switch (columnIndex) {
		case 0:
			return aux.getIdProducto();
		case 1:
			return aux.getNombre();
		case 2:
			return Decimales(aux.getPrecio());
		case 3:
			return aux.getIVA();
		default:
			return null;
		}
	}


	public void addProducto(ProductoVO nuevoGasto) {
		this.datos.add(nuevoGasto);

		// Avisa a los suscriptores creando un TableModelEvent...
		//TableModelEvent evento;
		//evento = new TableModelEvent(this, this.getRowCount() - 1, this.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT);

		// ... y avisando a los suscriptores
		//avisaSuscriptores(evento);
	}
	
	public void borraTabla(){
		ArrayList<ProductoVO> vacio = new ArrayList<ProductoVO>();
		datos = vacio; 
	}
	
	public Class<String> getColumnClass(int columnIndex) {
		return String.class;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Código";
		case 1:
			return "Descripcion";
		case 2:
			return "Precio";
		case 3:
			return "IVA";
		default:
			return null;
		}
	}


	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		/*
		case 1:
			return false;
		case 6:
			return false;*/
		default:
			return false;
		}
	}

	
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Obtiene el detalle de la fila indicada
		ProductoVO aux = (ProductoVO) (datos.get(rowIndex));

		// Cambia el campo que indica columnIndex, poniendole el
		// aValue que se nos pasa.
		switch (columnIndex) {
		case 0:
			aux.setIdProducto((String) aValue);
		case 1:
			aux.setNombre((String) aValue);
			break;
		case 2:
			aux.setPrecio(Float.parseFloat((String) aValue));
			break;
		case 3:
			aux.setIVA(Float.parseFloat((String) aValue));
			break;
		default:
			break;
		}
		// Avisa a los suscriptores del cambio, creando un TableModelEvent ...
		TableModelEvent evento = new TableModelEvent(this);

		// ... y pasándoselo a los suscriptores.
		avisaSuscriptores(evento);
	}

	/**
	 * Pasa a los suscriptores el evento.
	 */
	private void avisaSuscriptores(TableModelEvent evento) {
		int i;
		//CambioTablaDetalle.tableChanged();
		// Bucle para todos los suscriptores en la lista, se llama al metodo
		// tableChanged() de los mismos, pasándole el evento.
		for (i = 0; i < listeners.size(); i++){
			//System.out.println("Hasta aqui sí");
			((TableModelListener) listeners.get(i)).tableChanged(evento);
		}
	}


	public void mostrarError(){
		int result = JOptionPane.showConfirmDialog(ventana,
				"El campo requiere un número decimal", "Entrada incorrecta",
				JOptionPane.DEFAULT_OPTION);
	}
	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;
	}
	

}
