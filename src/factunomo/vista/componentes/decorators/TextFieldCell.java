package factunomo.vista.componentes.decorators;

import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.TableCellEditor;

public class TextFieldCell extends JTextField {
    public TextFieldCell(JTable cellTable) {
        super();                            // calling parent constructor
        final JTable table = cellTable;     // this one is required to get cell editor and stop editing

        this.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            // this function successfully provides cell editing stop
            // on cell losts focus (but another cell doesn't gain focus)
            public void focusLost(FocusEvent e) {
                CellEditor cellEditor = (CellEditor) table.getCellEditor();
                if (cellEditor != null)
                    if (cellEditor.getCellEditorValue() != null)
                        cellEditor.stopCellEditing();
                    else
                        cellEditor.cancelCellEditing();
            }
        });
    }
}
