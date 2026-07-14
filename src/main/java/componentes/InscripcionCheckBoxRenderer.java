package componentes;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import tabla.ModeloTablaInscripcion;

// Pinta el checkbox de la columna "Inscribir" deshabilitado (gris) para los
// catequizandos que ya estan inscriptos en el grupo seleccionado, ya que esas
// filas no se pueden tildar ni destildar desde esta pantalla (ModeloTablaInscripcion
// ya bloquea la edicion; esto solo lo hace visualmente evidente).
public class InscripcionCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private final ModeloTablaInscripcion modelo;

	public InscripcionCheckBoxRenderer(ModeloTablaInscripcion modelo) {
		this.modelo = modelo;
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		setSelected(Boolean.TRUE.equals(value));
		setEnabled(!modelo.estaBloqueadoEnFila(row));
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return this;
	}

}