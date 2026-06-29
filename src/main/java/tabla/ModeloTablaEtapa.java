package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


import modelo.EtapaModelo;

public class ModeloTablaEtapa  extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"ID", "Descripcion", "Estado"};
	private List<EtapaModelo> catequista = new ArrayList<EtapaModelo>();
	
	public void setLista(List<EtapaModelo> lista) {
		catequista = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		
		return catequista.size();
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
		switch (columnIndex) {
		case 0: return catequista.get(rowIndex).getEtap_id();
		case 1: return catequista.get(rowIndex).getEtap_descripcion();
		case 2: return catequista.get(rowIndex).isEtap_estado() ? "Activo" : "Inactivo";
		}
		return null;
	}

}
