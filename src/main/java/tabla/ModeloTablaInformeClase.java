package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.AsistenciaModelo;
import modelo.ClaseModelo;
import modelo.EstadoAsistencia;
import utilidades.FechaUtil;

/**
 * Tabla de solo lectura para la pantalla de Informe de Clase (menu
 * "Informes"). Ademas de fecha/grupo/descripcion, resume cuantas
 * asistencias se registraron y cuantos catequizandos quedaron marcados
 * Presente en cada clase.
 */
public class ModeloTablaInformeClase extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "Fecha", "Grupo", "Descripcion", "Asistencias Registradas", "Presentes" };
	private List<ClaseModelo> clases = new ArrayList<ClaseModelo>();

	public void setLista(List<ClaseModelo> lista) {
		clases = lista;
		fireTableDataChanged();
	}

	public List<ClaseModelo> getLista() {
		return clases;
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	// Las columnas de resumen son numericas: declarar Integer.class hace
	// que el renderer por defecto de JTable las alinee a la derecha, en
	// vez de a la izquierda como el texto.
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 3 || columnIndex == 4) return Integer.class;
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ClaseModelo clase = clases.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase()) : "";
		case 1:
			return clase.getGrupoCatequesis() != null ? clase.getGrupoCatequesis().getGrup_nombre() : "";
		case 2:
			return clase.getClase_descripcion();
		case 3:
			return clase.getAsistencias() != null ? clase.getAsistencias().size() : 0;
		case 4:
			return contarPresentes(clase);
		}
		return null;
	}

	private int contarPresentes(ClaseModelo clase) {
		if (clase.getAsistencias() == null) return 0;
		int presentes = 0;
		for (AsistenciaModelo asistencia : clase.getAsistencias()) {
			if (asistencia.getEstado() == EstadoAsistencia.PRESENTE) presentes++;
		}
		return presentes;
	}

}
