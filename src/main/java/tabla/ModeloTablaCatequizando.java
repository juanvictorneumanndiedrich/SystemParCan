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
	private List<CatequizandoModelo> lista = new ArrayList<CatequizandoModelo>();
	
	public void setLista(List<CatequizandoModelo> lista) {
		
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
