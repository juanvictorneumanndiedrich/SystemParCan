package tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.InscripcionModelo;
import utilidades.FechaUtil;

/** Tabla de solo lectura para la pantalla de Informe de Inscripcion (menu "Informes"). */
public class ModeloTablaInformeInscripcion extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "Fecha", "Catequizando", "Documento", "Grupo", "Estado" };
	private List<InscripcionModelo> inscripciones = new ArrayList<InscripcionModelo>();

	public void setLista(List<InscripcionModelo> lista) {
		inscripciones = lista;
		fireTableDataChanged();
	}

	public List<InscripcionModelo> getLista() {
		return inscripciones;
	}

	@Override
	public int getRowCount() {
		return inscripciones.size();
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

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		InscripcionModelo inscripcion = inscripciones.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return inscripcion.getInscrip_fecha() != null ? FechaUtil.fechaAString(inscripcion.getInscrip_fecha())
					: "";
		case 1:
			return inscripcion.getCatequizando() != null
					? inscripcion.getCatequizando().getCatz_apellido() + ", "
							+ inscripcion.getCatequizando().getCatz_nombre()
					: "";
		case 2:
			return inscripcion.getCatequizando() != null ? inscripcion.getCatequizando().getCatz_documento() : "";
		case 3:
			return inscripcion.getGrupoCatequesis() != null ? inscripcion.getGrupoCatequesis().getGrup_nombre() : "";
		case 4:
			return Boolean.TRUE.equals(inscripcion.isInscrip_estado()) ? "Activo" : "Inactivo";
		}
		return null;
	}

}
