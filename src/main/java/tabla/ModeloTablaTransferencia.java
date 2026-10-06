package tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import modelo.CatequizandoModelo;

/**
 * Tabla de catequizandos inscriptos en el grupo de origen elegido en la pantalla de Transferencia. La columna 0 es un
 * checkbox: tildado significa que ese catequizando se va a transferir al grupo destino al confirmar.
 */
public class ModeloTablaTransferencia extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	/** Titulos de las columnas, en el orden en que se muestran. */
	private String[] columnas = { "Transferir", "Nombre", "Apellido", "Documento" };

	/** Catequizandos que muestra la tabla (ya filtrados por el buscador). */
	private List<CatequizandoModelo> catequizandos = new ArrayList<CatequizandoModelo>();

	/** Estado tildado/destildado por catz_id, para no perderlo al filtrar con el buscador. */
	private Map<Integer, Boolean> seleccionados = new HashMap<Integer, Boolean>();

	/**
	 * Carga los catequizandos del grupo de origen, todos destildados.
	 *
	 * @param lista catequizandos a mostrar
	 */
	public void cargar(List<CatequizandoModelo> lista) {
		this.catequizandos = lista;
		this.seleccionados.clear();
		fireTableDataChanged();
	}

	/**
	 * Cambia que filas se muestran (resultado del buscador) sin perder los checkbox ya tildados.
	 *
	 * @param listaFiltrada catequizandos que deben verse ahora
	 */
	public void filtrar(List<CatequizandoModelo> listaFiltrada) {
		this.catequizandos = listaFiltrada;
		fireTableDataChanged();
	}

	/**
	 * Indica si el catequizando esta tildado para transferir.
	 *
	 * @param catzId id del catequizando
	 * @return {@code true} si esta tildado
	 */
	public boolean estaSeleccionado(Integer catzId) {
		return Boolean.TRUE.equals(seleccionados.get(catzId));
	}

	/** @return cantidad de filas (una por catequizando visible) */
	@Override
	public int getRowCount() {
		return catequizandos.size();
	}

	/** @return cantidad de columnas de la tabla */
	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	/**
	 * @param column indice de la columna (desde 0)
	 * @return el titulo de la columna indicada
	 */
	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	/**
	 * La columna 0 es un checkbox ({@code Boolean}); el resto es texto.
	 *
	 * @param columnIndex indice de la columna (desde 0)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex == 0 ? Boolean.class : String.class;
	}

	/**
	 * Solo se puede editar el checkbox de la columna 0.
	 *
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	/**
	 * Guarda el estado del checkbox de la fila (ignora columnas distintas de 0).
	 *
	 * @param aValue      nuevo valor (se toma como tildado solo si es {@code true})
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex != 0) return;
		Integer catzId = catequizandos.get(rowIndex).getCatz_id();
		seleccionados.put(catzId, Boolean.TRUE.equals(aValue));
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * Devuelve el valor de la celda: checkbox de transferencia, nombre, apellido o documento.
	 *
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CatequizandoModelo catequizando = catequizandos.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return estaSeleccionado(catequizando.getCatz_id());
		case 1:
			return catequizando.getCatz_nombre();
		case 2:
			return catequizando.getCatz_apellido();
		case 3:
			return catequizando.getCatz_documento();
		}
		return null;
	}

}
