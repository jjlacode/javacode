package factunomo.modelo;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.*;

import javax.swing.event.*;

import java.util.ArrayList;

public class ModeloTablaCuenta implements TableModel {

	/**
	 * Lista con los datos. Cada elemento de la lista es una instancia de
	 * Cuentas
	 */
	private ArrayList<String> datos = new ArrayList<String>();
	/**
	 * Lista de suscriptores. El JTable será un suscriptor de este modelo de
	 * datos
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	JDialog ventana;
	JTable tabla;
	Modelo modelo;
	
	/**
	 * Constructor
	 * @param ventana
	 * @param tabla
	 */
	public ModeloTablaCuenta(JDialog ventana, JTable tabla){
		this.ventana = ventana;
		this.tabla = tabla;
		this.modelo=new Modelo();
	}

	
	
	
	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		return datos.size();
	}
	
	
	public String getCuenta(int rowIndex) {
		return (String) (datos.get(rowIndex));
	}
	
	public void addTableModelListener(TableModelListener l) {
		// Añade el suscriptor a la lista de suscriptores
		listeners.add(l);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String aux = (String) (datos.get(rowIndex));
		switch (columnIndex) {
		case 0:
			return aux;
		default:
			return null;
		}
	}


	public void addCuenta(String cuenta) {
		this.datos.add(cuenta);
	}
	
	public void borraTabla(){
		ArrayList<String> vacio = new ArrayList<String>();
		datos = vacio; 
	}
	
	public Class<String> getColumnClass(int columnIndex) {
		return String.class;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Cuenta";
		default:
			return null;
		}
	}


	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Obtiene el detalle de la fila indicada
		String aux = (String) (datos.get(rowIndex));

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
			((TableModelListener) listeners.get(i)).tableChanged(evento);
		}
	}


	public void mostrarError(){
		int result = JOptionPane.showConfirmDialog(ventana,
				"El campo requiere un número decimal", "Entrada incorrecta",
				JOptionPane.DEFAULT_OPTION);
	}

}
