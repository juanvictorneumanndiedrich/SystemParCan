package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.InscripcionModelo;
import utilidades.FechaUtil;

/** Tabla de solo lectura para la pantalla de Informe de Inscripcion (menu "Informes"). */
public class ModeloTablaInformeInscripcion extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "Fecha", "Catequizando", "Documento", "Grupo", "Estado" };
	/**
	 * Inscripciones que muestra la tabla.
	 */
	private List<InscripcionModelo> inscripciones = new ArrayList<InscripcionModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<InscripcionModelo> lista) {
		inscripciones = lista;
		fireTableDataChanged();
	}

	/**
	 * @return la lista de registros que muestra actualmente la tabla
	 */
	public List<InscripcionModelo> getLista() {
		return inscripciones;
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return inscripciones.size();
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
		InscripcionModelo inscripcion = inscripciones.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return inscripcion.getInscrip_fecha() != null ? FechaUtil.fechaAString(inscripcion.getInscrip_fecha())
					: "";
		case 1:
			return inscripcion.getCatequizando() != null
					? inscripcion.getCatequizando().getCatz_apellido() + ", "
							+ inscripcion.getCatequizando().getCatz_nombre()
					: "";
		case 2:
			return inscripcion.getCatequizando() != null ? inscripcion.getCatequizando().getCatz_documento() : "";
		case 3:
			return inscripcion.getGrupoCatequesis() != null ? inscripcion.getGrupoCatequesis().getGrup_nombre() : "";
		case 4:
			return Boolean.TRUE.equals(inscripcion.isInscrip_estado()) ? "Activo" : "Inactivo";
		}
		return null;
	}

}
