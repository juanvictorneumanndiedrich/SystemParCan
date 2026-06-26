package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.CatequistaModelo;

public class ModeloTablaCatequista extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"ID","Nombres", "Apellidos", "Documento", "Estado"};
	private List<CatequistaModelo> catequista = new ArrayList<CatequistaModelo>();
	
	public void setLista(List<CatequistaModelo> lista) {
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
		case 0: return catequista.get(rowIndex).getCat_id();
		case 1: return catequista.get(rowIndex).getCat_nombre();
		case 2: return catequista.get(rowIndex).getCat_apellido();
		case 3: return catequista.get(rowIndex).getCat_documento();
		case 4: return catequista.get(rowIndex).isCat_estado() ? "Activo" : "Inactivo";
			
		}
		return null;
	}

}
