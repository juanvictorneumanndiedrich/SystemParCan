package tabla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EstadoCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setHorizontalAlignment(JLabel.CENTER);

        if ("Activo".equals(value)) {
        	label.setForeground(new Color(0, 139, 0)); // Verde oscuro
        } else {
            label.setForeground(Color.RED);
        }

        // Mantener color de fondo al seleccionar
        if (isSelected) {
            label.setForeground(Color.WHITE);
        }

        return label;
    }
}