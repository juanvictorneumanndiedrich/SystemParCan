package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.TransferenciaModelo;
import utilidades.FechaUtil;

/** Tabla de solo lectura para la pantalla de Informe de Transferencia (menu "Informes"). */
public class ModeloTablaInformeTransferencia extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "Fecha", "Catequizando", "Grupo Origen", "Grupo Destino", "Observacion" };
	/**
	 * Transferencias que muestra la tabla.
	 */
	private List<TransferenciaModelo> transferencias = new ArrayList<TransferenciaModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<TransferenciaModelo> lista) {
		transferencias = lista;
		fireTableDataChanged();
	}

	/**
	 * @return la lista de registros que muestra actualmente la tabla
	 */
	public List<TransferenciaModelo> getLista() {
		return transferencias;
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return transferencias.size();
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
		TransferenciaModelo transferencia = transferencias.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return transferencia.getTransf_fecha() != null ? FechaUtil.fechaAString(transferencia.getTransf_fecha())
					: "";
		case 1:
			return transferencia.getCatequizando() != null
					? transferencia.getCatequizando().getCatz_apellido() + ", "
							+ transferencia.getCatequizando().getCatz_nombre()
					: "";
		case 2:
			return transferencia.getGrupoOrigen() != null ? transferencia.getGrupoOrigen().getGrup_nombre() : "";
		case 3:
			return transferencia.getGrupoDestino() != null ? transferencia.getGrupoDestino().getGrup_nombre() : "";
		case 4:
			return transferencia.getTransf_observacion();
		}
		return null;
	}

}
