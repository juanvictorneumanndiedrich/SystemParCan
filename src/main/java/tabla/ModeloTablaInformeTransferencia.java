package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.TransferenciaModelo;
import utilidades.FechaUtil;

/** Tabla de solo lectura para la pantalla de Informe de Transferencia (menu "Informes"). */
public class ModeloTablaInformeTransferencia extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "Fecha", "Catequizando", "Grupo Origen", "Grupo Destino", "Observacion" };
	private List<TransferenciaModelo> transferencias = new ArrayList<TransferenciaModelo>();

	public void setLista(List<TransferenciaModelo> lista) {
		transferencias = lista;
		fireTableDataChanged();
	}

	public List<TransferenciaModelo> getLista() {
		return transferencias;
	}

	@Override
	public int getRowCount() {
		return transferencias.size();
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
