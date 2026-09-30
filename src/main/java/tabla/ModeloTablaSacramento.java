package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.SacramentoModelo;

/**
 * Modelo de tabla (TableModel) de solo lectura para listar los sacramentos en SacramentoVista. Columnas: ID y Nombre.
 * Las filas salen de la lista que se le entrega con {@link #setLista(List)}.
 */
public class ModeloTablaSacramento extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "ID", "Nombre" };
	/**
	 * Sacramentos que muestra la tabla.
	 */
	private List<SacramentoModelo> sacramentos = new ArrayList<SacramentoModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<SacramentoModelo> lista) {
		sacramentos = lista;
		fireTableDataChanged();
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return sacramentos.size();
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
		SacramentoModelo sacramento = sacramentos.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return sacramento.getSacr_id();
		case 1:
			return sacramento.getSacr_nombre();
		}
		return null;
	}

}