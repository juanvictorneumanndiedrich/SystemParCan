package componentes;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import tabla.ModeloTablaInscripcion;

/**
 * Pinta el checkbox de la columna "Inscribir" deshabilitado (gris) para los
 * catequizandos que ya estan inscriptos en el grupo seleccionado, ya que esas
 * filas no se pueden tildar ni destildar desde esta pantalla (ModeloTablaInscripcion
 * ya bloquea la edicion; esto solo lo hace visualmente evidente).
 */
public class InscripcionCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	/** Modelo de la tabla, consultado para saber si la fila esta bloqueada. */
	private final ModeloTablaInscripcion modelo;

	/**
	 * Crea el renderer para la tabla de catequizandos de la pantalla de Inscripcion.
	 * 
	 * @param modelo modelo de la tabla, usado para saber que filas estan bloqueadas
	 */
	public InscripcionCheckBoxRenderer(ModeloTablaInscripcion modelo) {
		this.modelo = modelo;
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
	}

	/**
	 * Configura el checkbox con el valor de la celda: tildado si el valor es {@code true} y habilitado solo si
	 * la fila no esta bloqueada. Respeta el color de fondo de seleccion de la tabla.
	 *
	 * @return este mismo checkbox, ya configurado para dibujar la celda
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		setSelected(Boolean.TRUE.equals(value));
		setEnabled(!modelo.estaBloqueadoEnFila(row));
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return this;
	}

}