package tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import modelo.CatequizandoModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;

// Tabla de catequizandos para el grupo de catequesis seleccionado. El Controller
// le pasa dos tipos de fila:
//   - candidatos: catequizandos sin ninguna inscripcion vigente (checkbox habilitado,
//     tildarlo los inscribe al grupo al guardar).
//   - ya inscriptos en ESTE grupo: aparecen tildados pero con el checkbox bloqueado,
//     para que se pueda ver quien ya integra el grupo sin poder sacarlo desde aca
//     por error.
// Un catequizando inscripto en OTRO grupo no aparece en esta tabla.
public class ModeloTablaInscripcion extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnas = { "Inscribir", "Nombre", "Apellido", "Documento" };
	private List<CatequizandoModelo> catequizandos = new ArrayList<CatequizandoModelo>();

	// Estado marcado/desmarcado por catz_id, para no perderlo al filtrar la tabla con el buscador
	private Map<Integer, Boolean> seleccionados = new HashMap<Integer, Boolean>();
	// catz_id de catequizandos ya inscriptos en el grupo seleccionado (checkbox bloqueado)
	private Set<Integer> bloqueados = new HashSet<Integer>();

	// Recalcula desde cero el estado de todas las filas en base a los datos reales
	// (catequizando.getInscripciones()) y el grupo actualmente seleccionado.
	public void cargar(List<CatequizandoModelo> lista, GrupoCatequesisModelo grupoSeleccionado) {
		this.catequizandos = lista;
		this.seleccionados.clear();
		this.bloqueados.clear();

		Integer grupoId = grupoSeleccionado != null ? grupoSeleccionado.getGrup_id() : null;

		for (CatequizandoModelo catequizando : lista) {
			boolean yaEnEsteGrupo = obtenerInscripcionEnGrupo(catequizando, grupoId) != null;

			seleccionados.put(catequizando.getCatz_id(), yaEnEsteGrupo);
			if (yaEnEsteGrupo) bloqueados.add(catequizando.getCatz_id());
		}

		fireTableDataChanged();
	}

	// Cambia que filas se muestran (resultado del buscador) sin tocar los mapas de
	// seleccion/bloqueo, para no perder los checkbox ya tildados por el usuario.
	public void filtrar(List<CatequizandoModelo> listaFiltrada) {
		this.catequizandos = listaFiltrada;
		fireTableDataChanged();
	}

	// Busca, entre TODAS las inscripciones del catequizando (puede haber mas de una
	// si quedaron datos de pruebas viejas), la que corresponde puntualmente al grupo
	// indicado. Antes esto se resolvia con inscripciones.get(0), que devolvia
	// cualquiera (a veces de otro grupo) y hacia que el catequizando desapareciera
	// de la tabla aunque si perteneciera al grupo que se estaba mirando.
	public static InscripcionModelo obtenerInscripcionEnGrupo(CatequizandoModelo catequizando, Integer grupoId) {
		if (grupoId == null) return null;
		List<InscripcionModelo> inscripciones = catequizando.getInscripciones();
		if (inscripciones == null) return null;

		for (InscripcionModelo inscripcion : inscripciones) {
			if (inscripcion.getGrupoCatequesis() != null
					&& grupoId.equals(inscripcion.getGrupoCatequesis().getGrup_id())) {
				return inscripcion;
			}
		}
		return null;
	}

	// true si el catequizando tiene alguna inscripcion cargada, sin importar en que grupo
	public static boolean tieneAlgunaInscripcion(CatequizandoModelo catequizando) {
		List<InscripcionModelo> inscripciones = catequizando.getInscripciones();
		return inscripciones != null && !inscripciones.isEmpty();
	}

	public boolean estaSeleccionado(Integer catzId) {
		return Boolean.TRUE.equals(seleccionados.get(catzId));
	}

	public boolean estaBloqueado(Integer catzId) {
		return bloqueados.contains(catzId);
	}

	public boolean estaBloqueadoEnFila(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= catequizandos.size()) return false;
		return estaBloqueado(catequizandos.get(rowIndex).getCatz_id());
	}

	public List<CatequizandoModelo> getCatequizandos() {
		return catequizandos;
	}

	@Override
	public int getRowCount() {
		return catequizandos.size();
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
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex == 0 ? Boolean.class : String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex != 0) return false;
		return !estaBloqueadoEnFila(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex != 0 || estaBloqueadoEnFila(rowIndex)) return;
		Integer catzId = catequizandos.get(rowIndex).getCatz_id();
		seleccionados.put(catzId, Boolean.TRUE.equals(aValue));
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CatequizandoModelo catequizando = catequizandos.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return estaSeleccionado(catequizando.getCatz_id());
		case 1:
			return catequizando.getCatz_nombre();
		case 2:
			return catequizando.getCatz_apellido();
		case 3:
			return catequizando.getCatz_documento();
		}
		return null;
	}

}