package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.SacramentoModelo;

public class ModeloTablaSacramento extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "ID", "Nombre" };
	private List<SacramentoModelo> sacramentos = new ArrayList<SacramentoModelo>();

	public void setLista(List<SacramentoModelo> lista) {
		sacramentos = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return sacramentos.size();
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