package factunomo.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import factunomo.modelo.obj.ElementoListaVO;

public class ComboModel implements ComboBoxModel  {

	
	private Vector<ElementoListaVO> data = new Vector<ElementoListaVO>();
    private Vector<ListDataListener> list = new Vector<ListDataListener>();
    private ElementoListaVO selectedItem;

    
    
    public ComboModel(ResultSet r, String codigo, String descri) throws SQLException {
        r.beforeFirst();
        while (r.next()) {
            this.data.add(new ElementoListaVO(r.getString(codigo), r.getString(descri)));
        }
    }
    
    
    public ComboModel(ResultSet r, String codigo, String descri, ElementoListaVO def) throws SQLException {
        r.beforeFirst();
        if(def != null){
        	this.data.add(def);
        }
        while (r.next()) {
            this.data.add(new ElementoListaVO(r.getString(codigo), r.getString(descri)));
        }
    }

    
    
    public ElementoListaVO searchSelectedItem(String i) {
        for (ElementoListaVO o : this.data) {
            if (i.equals(o.getCodigo())) {
                return o;
            }
        }
        return null;
    }

    
    
	@Override
	public Object getSelectedItem() {
		return this.selectedItem;

	}

	@Override
	public void setSelectedItem(Object arg0) {
		this.selectedItem = arg0 instanceof ElementoListaVO ? (ElementoListaVO) arg0 : null;
        for (ListDataListener l : this.list) {
            l.contentsChanged(new ListDataEvent(this, javax.swing.event.ListDataEvent.CONTENTS_CHANGED, 0, 0));
        }

	}

	@Override
	public void addListDataListener(ListDataListener l) {
		this.list.add(l);
	}

	@Override
	public Object getElementAt(int index) {
		return this.data.get(index);
	}

	@Override
	public int getSize() {
		return this.data.size();

	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		this.list.remove(l);
	}
	
	
	public String getSelectedCodigo() {
        return this.selectedItem == null ? null : this.selectedItem.getCodigo();
    }

    public String getSelectedDescripcion() {
        return this.selectedItem == null ? null : this.selectedItem.getDescripcion();
    }


}
