package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ClaseModelo;
import utilidades.FechaUtil;

public class ModeloTablaClase extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "ID", "Fecha", "Descripcion" };
	private List<ClaseModelo> clases = new ArrayList<ClaseModelo>();

	public void setLista(List<ClaseModelo> lista) {
		clases = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return clases.size();
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