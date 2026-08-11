package controlador;

import vista.CatequistaVista;
import vista.CatequizandoVista;
import vista.EtapaVista;
import vista.GrupoCatequesisVista;
import vista.InscripcionVista;
import vista.SacramentoVista;
import vista.PantallaPrincipalVista;
import vista.ClasesVista;
import vista.AsistenciasVista;
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

}