package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.CatequizandoModelo;

public class ModeloTablaCatequizando  extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"ID","Nombres", "Apellidos", "Documento", "Estado"};
	private List<CatequizandoModelo> catequizando = new ArrayList<CatequizandoModelo>();
	
	public void setLista(List<CatequizandoModelo> lista) {
		catequizando = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return catequizando.size();
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
		case 0: return catequizando.get(rowIndex).getCatz_id();
		case 1: return catequizando.get(rowIndex).getCatz_nombre();
		case 2: return catequizando.get(rowIndex).getCatz_apellido();
		case 3: return catequizando.get(rowIndex).getCatz_documento();
		case 4: return catequizando.get(rowIndex).getCatz_estado();
		
		}
		
		return null;
	}

	
	
	
}
