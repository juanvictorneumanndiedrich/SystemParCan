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
	/**
	 * Titulos de las columnas, en el orden en que se muestran.
	 */
	private String[] columnas = { "Fecha", "Grupo", "Descripcion", "Asistencias Registradas", "Presentes" };
	/**
	 * Clases que muestra la tabla.
	 */
	private List<ClaseModelo> clases = new ArrayList<ClaseModelo>();

	/**
	 * Reemplaza los datos que muestra la tabla y avisa a la vista para que se redibuje.
	 *
	 * @param lista nueva lista de registros a mostrar
	 */
	public void setLista(List<ClaseModelo> lista) {
		clases = lista;
		fireTableDataChanged();
	}

	/**
	 * @return la lista de registros que muestra actualmente la tabla
	 */
	public List<ClaseModelo> getLista() {
		return clases;
	}

	/**
	 * @return cantidad de filas (una por registro de la lista)
	 */
	@Override
	public int getRowCount() {
		return clases.size();
	}

	/**
	 * @return cantidad de columnas de la tabla
	 */
	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	/**
	 * @return el titulo de la columna indicada
	 *
	 * @param column indice de la columna (desde 0)
	 */
	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	/**
	 * Indica si una celda se puede editar.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	// Las columnas de resumen son numericas: declarar Integer.class hace
	// que el renderer por defecto de JTable las alinee a la derecha, en
	// vez de a la izquierda como el texto.
	/**
	 * Las columnas de resumen (3 y 4) son numericas: declarar {@code Integer} hace que JTable las alinee a la derecha.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 3 || columnIndex == 4) return Integer.class;
		return String.class;
	}

	/**
	 * Devuelve el valor que se muestra en una celda, tomado del registro de esa fila.
	 *
	 * @param rowIndex indice de la fila
	 * @param columnIndex indice de la columna
	 */
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

	/**
	 * Cuenta cuantas asistencias de la clase estan marcadas como presente.
	 *
	 * @param clase clase a resumir
	 */
	private int contarPresentes(ClaseModelo clase) {
		if (clase.getAsistencias() == null) return 0;
		int presentes = 0;
		for (AsistenciaModelo asistencia : clase.getAsistencias()) {
			if (asistencia.getEstado() == EstadoAsistencia.PRESENTE) presentes++;
		}
		return presentes;
	}

}
