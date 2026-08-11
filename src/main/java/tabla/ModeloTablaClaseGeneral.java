package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ClaseModelo;
import utilidades.FechaUtil;

/**
 * Variante de ModeloTablaClase para la pantalla de Clases de acceso
 * directo (ClasesVista): a diferencia de ModeloTablaClase (que solo se usa
 * dentro de un Grupo de Catequesis ya elegido, ver ClaseController), aca
 * la tabla mezcla clases de distintos grupos, asi que hace falta una
 * columna extra con el nombre del grupo de cada una.
 */
public class ModeloTablaClaseGeneral extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "ID", "Fecha", "Descripcion", "Grupo" };
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
		case 3:
			return clase.getGrupoCatequesis() != null ? clase.getGrupoCatequesis().getGrup_nombre() : "";
		}
		return null;
	}

}