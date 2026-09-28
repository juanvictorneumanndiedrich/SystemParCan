package controlador;

import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import dao.CatequistaDAO;
import dao.CatequizandoDAO;
import dao.EtapaDAO;
import dao.GrupoCatequesisDAO;
import dao.SacramentoDAO;
import net.sf.jasperreports.engine.JRException;
import reportes.GeneradorReportes;
import reportes.ReporteEtapaDTO;
import reportes.ReporteGrupoDTO;
import reportes.ReportePersonaDTO;
import reportes.ReporteSacramentoDTO;
import utilidades.ConexionJasper;
import vista.CatequistaVista;
import vista.CatequizandoVista;
import vista.EtapaVista;
import vista.GrupoCatequesisVista;
import vista.InscripcionVista;
import vista.SacramentoVista;
import vista.PantallaPrincipalVista;
import vista.ClasesVista;
import vista.AsistenciasVista;
import vista.InformeAsistenciaVista;
import vista.InformeInscripcionVista;
import vista.InformeTransferenciaVista;
import vista.InformeClaseVista;
import controlador.CatequizandoController;
import controlador.CatequistaController;

public class PantallaPrincipalController {

    private PantallaPrincipalVista vista;

    public PantallaPrincipalController(PantallaPrincipalVista vista) {
        this.vista = vista;
        setAcciones();
    }

    private void setAcciones() {
        this.vista.getMntmprsnlzdCatequizando().addActionListener(e -> abrirCatequizando());
        this.vista.getMntmprsnlzdCatequista().addActionListener(e -> abrirCatequista());
        this.vista.getMntmprsnlzdEtapa().addActionListener(e -> abrirEtapa());
        this.vista.getMntmprsnlzdGrupocatequesis().addActionListener(e -> abrirGrupoCatequesis());
        this.vista.getMntmprsnlzdSacramentos().addActionListener(e -> abrirSacramentos());
        this.vista.getMntmprsnlzdInscripcion().addActionListener(e -> abrirInscripcion());

        // Menu "Listados": un listado (reporte Jasper) por cada ABM de
        // registro (Catequista, Catequizando, Etapa, Grupo de Catequesis,
        // Sacramento). Quedan afuera las ABMs que son mas evento/transaccion
        // (Clase, Inscripcion, Transferencia, Asistencia).
        this.vista.getMntmprsnlzdCatequizando_1().addActionListener(e -> abrirListadoCatequizandos());
        this.vista.getMntmprsnlzdCatequistas().addActionListener(e -> abrirListadoCatequistas());
        this.vista.getMntmprsnlzdGrupocatequesis_1().addActionListener(e -> abrirListadoGrupos());
        this.vista.getMntmprsnlzdEtapa_1().addActionListener(e -> abrirListadoEtapas());
        this.vista.getMntmprsnlzdSacramentos_1().addActionListener(e -> abrirListadoSacramentos());

        // Menu "Informes": un informe (filtro + grilla + Jasper) por cada
        // ABM que es evento/transaccion (Asistencia, Inscripcion,
        // Transferencia, Clase).
        this.vista.getMntmprsnlzdAsistencia().addActionListener(e -> abrirInformeAsistencia());
        this.vista.getMntmprsnlzdInscripcion_1().addActionListener(e -> abrirInformeInscripcion());
        this.vista.getMntmprsnlzdTransferencia_1().addActionListener(e -> abrirInformeTransferencia());
        this.vista.getMntmprsnlzdClase_1().addActionListener(e -> abrirInformeClase());

        // Botones de acceso rapido de la pantalla principal. Reusan los
        // mismos metodos de apertura que los items del menu, salvo Clases y
        // Asistencia, que ahora abren sus propias pantallas de acceso
        // directo (ClasesVista / AsistenciasVista), pensadas para no
        // depender de un Grupo/Clase ya elegido de antemano.
        this.vista.getBtncsdrctClases().addActionListener(e -> abrirClases());
        this.vista.getBtncsdrctCatequista().addActionListener(e -> abrirCatequista());
        this.vista.getBtncsdrctCatequizando().addActionListener(e -> abrirCatequizando());
        this.vista.getBtncsdrctInscripcion().addActionListener(e -> abrirInscripcion());
        this.vista.getBtncsdrctAsistencia().addActionListener(e -> abrirAsistencias());
        this.vista.getBtncsdrctGrupocatequesis().addActionListener(e -> abrirGrupoCatequesis());
    }

    private void abrirCatequizando() {
        CatequizandoVista catequizandoVista = new CatequizandoVista();
        new CatequizandoController(catequizandoVista);
        catequizandoVista.setLocationRelativeTo(this.vista); // centra respecto a la pantalla principal
        catequizandoVista.setVisible(true);
    }

    private void abrirCatequista() {
        CatequistaVista catequistaVista = new CatequistaVista();
        new CatequistaController(catequistaVista);
        catequistaVista.setLocationRelativeTo(this.vista);
        catequistaVista.setVisible(true);
    }

    private void abrirEtapa() {
    	EtapaVista etapaVista = new EtapaVista();
    	new EtapaController(etapaVista);
    	etapaVista.setLocationRelativeTo(this.vista);
    	etapaVista.setVisible(true);
    }

    private void abrirGrupoCatequesis() {
    	GrupoCatequesisVista grupoCatequesisVista = new GrupoCatequesisVista();
    	new GrupoCatequesisController(grupoCatequesisVista);
    	grupoCatequesisVista.setLocationRelativeTo(this.vista);
    	grupoCatequesisVista.setVisible(true);
    }

    private void abrirSacramentos() {
    	SacramentoVista sacramentoVista = new SacramentoVista();
    	new SacramentoController(sacramentoVista);
    	sacramentoVista.setLocationRelativeTo(this.vista);
    	sacramentoVista.setVisible(true);
    }

    private void abrirInscripcion() {
    	InscripcionVista inscripcionVista = new InscripcionVista();
    	new InscripcionController(inscripcionVista);
    	inscripcionVista.setLocationRelativeTo(this.vista);
    	inscripcionVista.setVisible(true);
    }

    // Pantalla de Clases de acceso directo: a diferencia de la que se abre
    // desde adentro de Grupo de Catequesis, aca el grupo de cada clase se
    // elige con un combo en el propio formulario (ver ClasesController).
    private void abrirClases() {
    	ClasesVista clasesVista = new ClasesVista();
    	new ClasesController(clasesVista);
    	clasesVista.setLocationRelativeTo(this.vista);
    	clasesVista.setVisible(true);
    }

    // Pantalla de Asistencia de acceso directo: a diferencia de la que se
    // abre desde adentro de Clases, aca la clase se elige con dos combos en
    // cascada (Grupo -> Clase) dentro de la propia pantalla (ver
    // AsistenciasController).
    private void abrirAsistencias() {
    	AsistenciasVista asistenciasVista = new AsistenciasVista();
    	new AsistenciasController(asistenciasVista);
    	asistenciasVista.setLocationRelativeTo(this.vista);
    	asistenciasVista.setVisible(true);
    }

    // ===================== Listados (menu "Listados") =====================
    // Cada metodo arma su lista de datos via GeneradorReportes y se la pasa
    // a ConexionJasper, que compila el .jrxml del mismo nombre (ultimo
    // parametro de generarReporte) y lo muestra en una ventana modal.

    private void abrirListadoCatequistas() {
        try {
            List<ReportePersonaDTO> lista = GeneradorReportes.listarCatequistas(new CatequistaDAO(), new GrupoCatequesisDAO());
            ConexionJasper<ReportePersonaDTO> conexion = new ConexionJasper<>();
            conexion.generarReporte(lista, new HashMap<>(), "ListadoCatequistas");
            conexion.ventanaReporte.setLocationRelativeTo(this.vista);
            conexion.ventanaReporte.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.vista, "No se pudo generar el listado de catequistas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListadoCatequizandos() {
        try {
            List<ReportePersonaDTO> lista = GeneradorReportes.listarCatequizandos(new CatequizandoDAO());
            ConexionJasper<ReportePersonaDTO> conexion = new ConexionJasper<>();
            conexion.generarReporte(lista, new HashMap<>(), "ListadoCatequizandos");
            conexion.ventanaReporte.setLocationRelativeTo(this.vista);
            conexion.ventanaReporte.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.vista, "No se pudo generar el listado de catequizandos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListadoGrupos() {
        try {
            List<ReporteGrupoDTO> lista = GeneradorReportes.listarGrupos(new GrupoCatequesisDAO());
            ConexionJasper<ReporteGrupoDTO> conexion = new ConexionJasper<>();
            conexion.generarReporte(lista, new HashMap<>(), "ListadoGrupos");
            conexion.ventanaReporte.setLocationRelativeTo(this.vista);
            conexion.ventanaReporte.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.vista, "No se pudo generar el listado de grupos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListadoEtapas() {
        try {
            List<ReporteEtapaDTO> lista = GeneradorReportes.listarEtapas(new EtapaDAO());
            ConexionJasper<ReporteEtapaDTO> conexion = new ConexionJasper<>();
            conexion.generarReporte(lista, new HashMap<>(), "ListadoEtapas");
            conexion.ventanaReporte.setLocationRelativeTo(this.vista);
            conexion.ventanaReporte.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.vista, "No se pudo generar el listado de etapas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListadoSacramentos() {
        try {
            List<ReporteSacramentoDTO> lista = GeneradorReportes.listarSacramentos(new SacramentoDAO());
            ConexionJasper<ReporteSacramentoDTO> conexion = new ConexionJasper<>();
            conexion.generarReporte(lista, new HashMap<>(), "ListadoSacramentos");
            conexion.ventanaReporte.setLocationRelativeTo(this.vista);
            conexion.ventanaReporte.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.vista, "No se pudo generar el listado de sacramentos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== Informes (menu "Informes") =====================
    // Cada uno abre su propia pantalla de filtro + grilla; el filtrado y la
    // generacion del Jasper viven en el Controller de cada informe (ver
    // controlador.InformeXController), no aca.

    private void abrirInformeAsistencia() {
        InformeAsistenciaVista informeVista = new InformeAsistenciaVista();
        new InformeAsistenciaController(informeVista);
        informeVista.setLocationRelativeTo(this.vista);
        informeVista.setVisible(true);
    }

    private void abrirInformeInscripcion() {
        InformeInscripcionVista informeVista = new InformeInscripcionVista();
        new InformeInscripcionController(informeVista);
        informeVista.setLocationRelativeTo(this.vista);
        informeVista.setVisible(true);
    }

    private void abrirInformeTransferencia() {
        InformeTransferenciaVista informeVista = new InformeTransferenciaVista();
        new InformeTransferenciaController(informeVista);
        informeVista.setLocationRelativeTo(this.vista);
        informeVista.setVisible(true);
    }

    private void abrirInformeClase() {
        InformeClaseVista informeVista = new InformeClaseVista();
        new InformeClaseController(informeVista);
        informeVista.setLocationRelativeTo(this.vista);
        informeVista.setVisible(true);
    }

}