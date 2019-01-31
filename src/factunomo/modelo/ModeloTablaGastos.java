package factunomo.modelo;

import javax.swing.table.*;
import javax.swing.event.*;
import factunomo.modelo.obj.GastoVO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ModeloTablaGastos implements TableModel {

	/**
	 * Lista con los datos. Cada elemento de la lista es una instancia de
	 * Gastos
	 */
	private ArrayList<GastoVO> datos = new ArrayList<GastoVO>();
	/**
	 * Lista de suscriptores. El JTable será un suscriptor de este modelo de
	 * datos
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();

	private SimpleDateFormat formato=new SimpleDateFormat("dd-MM-yyyy");
	
	
	
	public int getColumnCount() {
		return 6;
	}

	public int getRowCount() {
		return datos.size();
	}
	
	
	public GastoVO getGasto(int rowIndex) {
		return (GastoVO) (datos.get(rowIndex));
	}
	
	

	public Object getValueAt(int rowIndex, int columnIndex) {
		GastoVO aux = (GastoVO) (datos.get(rowIndex));
		switch (columnIndex) {
		case 0:
			return aux.getIdGasto();
		case 1:
			String fecha=formato.format(aux.getFecha());
			return fecha;
		case 2:
			return aux.getNombre();
		case 3:
			return aux.getNIF();
		case 4:
			float base=aux.getBaseImponible1()+aux.getBaseImponible2()+aux.getBaseImponible3();
			return Decimales(base);
		case 5:
			return Decimales(aux.getTOTAL());
		default:
			return null;
		}
	}

	public void borraGasto(int fila) {
		this.datos.remove(fila);
		TableModelEvent evento = new TableModelEvent(this, fila, fila,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
		avisaSuscriptores(evento);
	}

	public void addGasto(GastoVO nuevaGasto) {
		this.datos.add(nuevaGasto);

		// Avisa a los suscriptores creando un TableModelEvent...
		//TableModelEvent evento;
		//evento = new TableModelEvent(this, this.getRowCount() - 1, this.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT);

		// ... y avisando a los suscriptores
		//avisaSuscriptores(evento);
	}

	
	public void addTableModelListener(TableModelListener l) {
		// Añade el suscriptor a la lista de suscriptores
		listeners.add(l);
	}

	public Class<String> getColumnClass(int columnIndex) {
		return String.class;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "ID_gasto";
		case 1:
			return "Fecha";
		case 2:
			return "Nombre";
		case 3:
			return "CIF";
		case 4:
			return "Base";
		case 5:
			return "TOTAL";
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
		// Obtiene la gasto de la fila indicada
		GastoVO aux = (GastoVO) (datos.get(rowIndex));

		// Cambia el campo de Gasto que indica columnIndex, poniendole el
		// aValue que se nos pasa.
		switch (columnIndex) {
		case 0:
			aux.setIdGasto((String) aValue);
			break;
		case 1:
			aux.setFecha((Date) aValue);
			break;
		case 2:
			aux.setNombre((String) aValue);
			break;
		case 3:
			aux.setNIF((String) aValue);
			break;
		case 4:
			aux.setBaseImponible1((Float) aValue);
			break;
		case 5:
			aux.setTOTAL((Float) aValue);
			break;
		default:
			break;
		}

		// Avisa a los suscriptores del cambio, creando un TableModelEvent ...
		TableModelEvent evento = new TableModelEvent(this, rowIndex, rowIndex,columnIndex);

		// ... y pasándoselo a los suscriptores.
		avisaSuscriptores(evento);
	}

	/**
	 * Pasa a los suscriptores el evento.
	 */
	private void avisaSuscriptores(TableModelEvent evento) {
		int i;

		// Bucle para todos los suscriptores en la lista, se llama al metodo
		// tableChanged() de los mismos, pasándole el evento.
		for (i = 0; i < listeners.size(); i++)
			((TableModelListener) listeners.get(i)).tableChanged(evento);
	}
	
	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;
	}
	

}
