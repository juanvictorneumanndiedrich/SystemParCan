package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.AsistenciaModelo;
import modelo.EstadoAsistencia;

/**
 * A diferencia de las demas ModeloTabla*, esta es editable: representa la
 * grilla de "tomar asistencia" de una Clase puntual. Cada fila es un
 * AsistenciaModelo (uno por catequizando inscripto en el grupo de esa
 * clase); si todavia no existe el registro en la base, el Controller arma
 * uno nuevo en memoria con estado=null hasta que el usuario lo marque.
 */
public class ModeloTablaAsistencia extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "Catequizando", "Estado", "Observaciones" };
	/**
	 * Asistencias que muestra la grilla (una por catequizando inscripto).
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
	 * Indica el tipo de dato de cada columna; JTable lo usa para elegir el renderer y el editor por defecto.
	 *
	 * @param columnIndex indice de la columna (desde 0)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 1) return EstadoAsistencia.class;
		return String.class;
	}

	/**
	 * Solo son editables Estado y Observaciones; el nombre del catequizando (columna 0) es de solo lectura.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// El nombre del catequizando (columna 0) es de solo lectura.
		return columnIndex == 1 || columnIndex == 2;
	}

	/**
	 * Devuelve el valor de la celda: nombre y apellido del catequizando, estado u observaciones.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AsistenciaModelo asistencia = asistencias.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return asistencia.getInscripcion().getCatequizando().getCatz_nombre() + " "
					+ asistencia.getInscripcion().getCatequizando().getCatz_apellido();
		case 1:
			return asistencia.getEstado();
		case 2:
			return asistencia.getObservaciones();
		}
		return null;
	}

	/**
	 * Guarda el estado o las observaciones editados en la {@link AsistenciaModelo} de la fila. Todavia no se persiste en
	 * la base de datos; eso lo hace el controller al guardar.
	 *
	 * @param value nuevo valor de la celda
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		AsistenciaModelo asistencia = asistencias.get(rowIndex);
		switch (columnIndex) {
		case 1:
			asistencia.setEstado((EstadoAsistencia) value);
			break;
		case 2:
			asistencia.setObservaciones((String) value);
			break;
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

}