package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ClaseModelo;
import utilidades.FechaUtil;

/**
 * Modelo de tabla (TableModel) de solo lectura para listar las clases de un grupo en ClaseVista. Columnas: ID, Fecha y Descripcion.
 * Las filas salen de la lista que se le entrega con {@link #setLista(List)}.
 */
public class ModeloTablaClase extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "ID", "Fecha", "Descripcion" };
	/**
	 * Clases que muestra la tabla.
	 */
	private List<ClaseModelo> clases = new ArrayList<ClaseModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<ClaseModelo> lista) {
		clases = lista;
		fireTableDataChanged();
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return clases.size();
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
	 * Devuelve el valor que se muestra en una celda, tomado del registro de esa fila.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ClaseModelo clase = clases.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return clase.getClase_id();
		case 1:
			return clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase()) : "";
		case 2:
			return clase.getClase_descripcion();
		}
		return null;
	}

}