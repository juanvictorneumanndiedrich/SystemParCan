package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.CatequistaModelo;

/**
 * Modelo de tabla (TableModel) de solo lectura para listar los catequistas en CatequistaVista. Columnas: ID, Nombres, Apellidos, Documento y Estado (Activo/Inactivo).
 * Las filas salen de la lista que se le entrega con {@link #setLista(List)}.
 */
public class ModeloTablaCatequista extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = {"ID","Nombres", "Apellidos", "Documento", "Estado"};
	/**
	 * Catequistas que muestra la tabla.
	 */
	private List<CatequistaModelo> catequista = new ArrayList<CatequistaModelo>();
	
	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<CatequistaModelo> lista) {
		catequista = lista;
		fireTableDataChanged();
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		
		return catequista.size();
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
		switch (columnIndex) {
		case 0: return catequista.get(rowIndex).getCat_id();
		case 1: return catequista.get(rowIndex).getCat_nombre();
		case 2: return catequista.get(rowIndex).getCat_apellido();
		case 3: return catequista.get(rowIndex).getCat_documento();
		case 4: return catequista.get(rowIndex).isCat_estado() ? "Activo" : "Inactivo";
			
		}
		return null;
	}

}
