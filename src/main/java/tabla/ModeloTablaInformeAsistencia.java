package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.AsistenciaModelo;
import modelo.EstadoAsistencia;
import utilidades.FechaUtil;

/**
 * Tabla de solo lectura para la pantalla de Informe de Asistencia (menu
 * "Informes"). A diferencia de ModeloTablaAsistencia (que es la grilla
 * editable de "tomar asistencia" de una clase puntual), esta muestra
 * registros ya guardados de distintas clases/grupos, para consulta.
 */
public class ModeloTablaInformeAsistencia extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "Fecha", "Catequizando", "Grupo", "Estado", "Observaciones" };
	/**
	 * Asistencias registradas que muestra la tabla.
	 */
	private List<AsistenciaModelo> asistencias = new ArrayList<AsistenciaModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<AsistenciaModelo> lista) {
		asistencias = lista;
		fireTableDataChanged();
	}

	/**
	 * @return la lista de registros que muestra actualmente la tabla
	 */
	public List<AsistenciaModelo> getLista() {
		return asistencias;
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return asistencias.size();
	}

	/**
	 * @return cantidad de columnas de la tabla
	 */
	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	/**
	 * @return el titulo de la columna indicada
	 *
	 * @param column indice de la columna (desde 0)
	 */
	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	/**
	 * Indica si una celda se puede editar.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Devuelve el valor que se muestra en una celda, tomado del registro de esa fila.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AsistenciaModelo asistencia = asistencias.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return asistencia.getClase() != null && asistencia.getClase().getClase_fechaClase() != null
					? FechaUtil.fechaAString(asistencia.getClase().getClase_fechaClase())
					: "";
		case 1:
			return asistencia.getInscripcion() != null && asistencia.getInscripcion().getCatequizando() != null
					? asistencia.getInscripcion().getCatequizando().getCatz_apellido() + ", "
							+ asistencia.getInscripcion().getCatequizando().getCatz_nombre()
					: "";
		case 2:
			return asistencia.getInscripcion() != null && asistencia.getInscripcion().getGrupoCatequesis() != null
					? asistencia.getInscripcion().getGrupoCatequesis().getGrup_nombre()
					: "";
		case 3:
			return textoEstado(asistencia.getEstado());
		case 4:
			return asistencia.getObservaciones();
		}
		return null;
	}

	/**
	 * Convierte un estado de asistencia en el texto que ve el usuario.
	 *
	 * @param estado estado a mostrar; {@code null} da texto vacio
	 */
	private String textoEstado(EstadoAsistencia estado) {
		if (estado == null) return "";
		switch (estado) {
		case PRESENTE:
			return "Presente";
		case AUSENTE:
			return "Ausente";
		case JUSTIFICADO:
			return "Justificado";
		}
		return "";
	}

}
