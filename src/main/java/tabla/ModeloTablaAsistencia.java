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
	private String[] columnas = { "Catequizando", "Estado", "Observaciones" };
	private List<AsistenciaModelo> asistencias = new ArrayList<AsistenciaModelo>();

	public void setLista(List<AsistenciaModelo> lista) {
		asistencias = lista;
		fireTableDataChanged();
	}

	public List<AsistenciaModelo> getLista() {
		return asistencias;
	}

	@Override
	public int getRowCount() {
		return asistencias.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 1) return EstadoAsistencia.class;
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// El nombre del catequizando (columna 0) es de solo lectura.
		return columnIndex == 1 || columnIndex == 2;
	}

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