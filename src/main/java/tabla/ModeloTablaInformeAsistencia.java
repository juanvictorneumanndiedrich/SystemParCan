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
	private String[] columnas = { "Fecha", "Catequizando", "Grupo", "Estado", "Observaciones" };
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

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
