package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.GrupoCatequesisModelo;

public class ModeloTablaGrupoCatequesis extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "ID", "Nombre", "Año", "Etapa" };
	private List<GrupoCatequesisModelo> grupos = new ArrayList<GrupoCatequesisModelo>();

	public void setLista(List<GrupoCatequesisModelo> lista) {
		grupos = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return grupos.size();
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		GrupoCatequesisModelo grupo = grupos.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return grupo.getGrup_id();
		case 1:
			return grupo.getGrup_nombre();
		case 2:
			return grupo.getGrup_anho() != null ? grupo.getGrup_anho().getYear() : null;
		case 3:
			return grupo.getEtapa() != null ? grupo.getEtapa().getEtap_descripcion() : "";
		}
		return null;
	}

}