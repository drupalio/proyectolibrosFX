package controlador;

import controlador.eventos.FrmAlumnoController;
import datos.AlumnoDAO;
import entidades.ClassAlumno;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import negocio.Variables;

public class AlumnoVistaController implements Initializable {

    private ObservableList<ClassAlumno> items; //instanciamos un objeto tipo arrayList especial para JavaFX
    private AlumnoDAO datos;   //instanciamos la clase AlumnoDAO la cual gestiona las acciones hacia nuestra BD
    private static Scene scene;   //variable de clase Scene donde se produce la acción con los elementos creados
    private static Stage stage;   //variable de clase Stage que es la ventana actual
    private double[] posicion;    //posición de la ventana en eje X-Y
    private JMetro jMetro;  //variable para cambiar la vista de la escena
    private ClassAlumno copiaAlumno;  //objeto donde guardar datos de la tabla

    @FXML
    private TextField txtFiltrarAlumnoTabla;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Label lblRegistros;
    @FXML
    private TableView<ClassAlumno> tablaAlumno;
    @FXML
    private TableColumn<ClassAlumno, Integer> colRegistro;
    @FXML
    private TableColumn<ClassAlumno, String> ColDni;
    @FXML
    private TableColumn<ClassAlumno, String> ColNombre;
    @FXML
    private TableColumn<ClassAlumno, String> colApellido1;
    @FXML
    private TableColumn<ClassAlumno, String> colApellido2;
    @FXML
    private Label lblInfoAlumno;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        offOnBotones(true);
        datos = new AlumnoDAO();  //instanciamos un objeto para hacer consultas a la BD
        this.cargarTabla(""); //cargamos la tabla de alumnos
    }

    @FXML
    private void pulsoEnter(KeyEvent event) {
        //keyPressed: cuando se pulsa ENTER en la caja de textoBuscar hacemos la acción de buscar
        Object evt = event.getSource();
        if (evt.equals(txtFiltrarAlumnoTabla)) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.cargarTabla(txtFiltrarAlumnoTabla.getText());
            }
        }
    }

    @FXML
    private void posicionTeclaTabla(KeyEvent event) {
        //cuando nos desplazamos con el cursor por la tabla capturamos la información de la fila
        ClassAlumno claseAlumno = tablaAlumno.getSelectionModel().getSelectedItem();
        if (claseAlumno != null) {  //si no es NULL capturamos los datos de la fila
            /*
            **** Para copiar los datos de una clase a otra usábamos esto, pero lo vamos a cambiar por la nueva forma Patrón Prototype ****
            registro = claseAlumno.getIdRegistro();
            dni = claseAlumno.getDni();
            nombre = claseAlumno.getNombre();
            apellido1 = claseAlumno.getApellido1();
            apellido2 = claseAlumno.getApellido2();
            //creamos un nuevo objeto con los datos capturados
            copiaAlumno = new ClassAlumno(registro, dni, nombre, apellido1, apellido2);
             */
            //**** Hacemos uso del nuevo método Clonar para copiar clases ******
            copiaAlumno = (ClassAlumno) claseAlumno.clonar();
        }
        offOnBotones(false);
        //si se pulsa ENTER en algún registro de la tabla y offOnbotones está en 1 y el objeto no es nulo
        if (event.getCode().equals(KeyCode.ENTER) && (Variables.getOffBotonesAlumnos() == 1) && (claseAlumno != null)) {
            guardarDatosAlumno();  //Esta condición solo se cumple cunando viene de Préstamo
            cerrarVentana();
            //PROCESAMOS COMO EDITAR si se pulsa ENTER en algún registro de la tabla y el objeto no es nulo
        } else if (event.getCode().equals(KeyCode.ENTER) && (Variables.getOffBotonesAlumnos() == 0) && (claseAlumno != null)) {
            Variables.setTextoFrm("EDITAR ALUMNO");  //Lo procesamos como Editar
            this.cargarFrmAlumno();
        }
    }

    @FXML
    private void posicionRatonTabla(MouseEvent event) {
        //cuando pulsamos con el ratón en algún registro de la tabla capturamos la información de la fila
        ClassAlumno claseAlumno = tablaAlumno.getSelectionModel().getSelectedItem();
        if (claseAlumno != null) {  //si no es NULL capturamos los datos de la fila
            /*
            **** Para copiar los datos de una clase a otra usábamos esto, pero lo vamos a cambiar por la nueva forma Patrón Prototype ****
            registro = claseAlumno.getIdRegistro();
            dni = claseAlumno.getDni();
            nombre = claseAlumno.getNombre();
            apellido1 = claseAlumno.getApellido1();
            apellido2 = claseAlumno.getApellido2();
            creamos un nuevo objeto con los datos capturados
            copiaAlumno = new ClassAlumno(registro, dni, nombre, apellido1, apellido2);
             */
            //**** Hacemos uso del nuevo método Clonar para copiar clases ******
            copiaAlumno = (ClassAlumno) claseAlumno.clonar();
        }
        offOnBotones(false);
        //Si se pulsa 2 veces en un registro y la variable offOnBotonesAlumno está en 1
        if (event.getClickCount() == 2 && (Variables.getOffBotonesAlumnos() == 1) && (claseAlumno != null)) {
            guardarDatosAlumno();  //Esta condición solo se cumple cunando viene de Préstamo
            cerrarVentana();
            //PROCESAMOS COMO EDITAR si se pulsa 2 click con el ratón en algún registro de la tabla y el objeto no es nulo
        } else if (event.getClickCount() == 2 && (Variables.getOffBotonesAlumnos() == 0) && (claseAlumno != null)) {
            Variables.setTextoFrm("EDITAR ALUMNO");  //Lo procesamos como Editar
            this.cargarFrmAlumno();
        }
    }

    @FXML
    private void buscarAlumnoTabla(ActionEvent event
    ) {
        offOnBotones(true);
        this.cargarTabla(txtFiltrarAlumnoTabla.getText());
    }

    @FXML
    private void limpiarAlumnoTabla(ActionEvent event) {
        this.limpiarVista();
        this.cargarTabla("");
    }

    @FXML
    private void nuevoAlumnoTabla(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar.
        Variables.setTextoFrm("CREAR ALUMNO");  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @FXML
    private void editarAlumnoTabla(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar
        Variables.setTextoFrm("EDITAR ALUMNO");  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @FXML
    private void eliminarAlumnoTabla(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar.
        Variables.setTextoFrm("ELIMINAR ALUMNO");  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @SuppressWarnings("unchecked")
    public void cargarTabla(String filtro
    ) {
        //asignamos a cada columna de la tabla el valor de su campo referenciado en ClassAlumno
        this.colRegistro.setCellValueFactory(new PropertyValueFactory("idRegistro"));
        this.ColDni.setCellValueFactory(new PropertyValueFactory("dni"));
        this.ColNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colApellido1.setCellValueFactory(new PropertyValueFactory("apellido1"));
        this.colApellido2.setCellValueFactory(new PropertyValueFactory("apellido2"));
        items = datos.listar(filtro);  //llamamos al método "listar" dentro de la clase AlumnoDAO
        this.tablaAlumno.refresh();  //refrescamos los datos de la tabla (sobre todo es interesante cuando actualizamos)
        this.tablaAlumno.setItems(items); //mostramos las columnas de la tabla
        lblRegistros.setText("Mostrando " + Variables.getRegistrosMostrados() + " de un total de " + datos.total() + " registros");
    }

    private void cargarFrmAlumno() {
        try {
            //cargamos la vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FrmAlumno.fxml"));
            //instanciamos y cargamos el FXML en el padre
            Parent root = loader.load();
            //instanciamos al controlador FrmAlumnoNuevo haciendo uso del nuevo método getController
            FrmAlumnoController ctrFrmAlumno = loader.getController();
            //creamos la nueva escena que viene del padre
            scene = new Scene(root);
            stage = new Stage();    //creamos la nueva ventana
            stage.setTitle("Crud de Alumnos"); //ponemos un título
            stage.initModality(Modality.APPLICATION_MODAL);  //hacemos que la escena nueva tome el foco y no permita cambiarse de ventana
            stage.setScene(scene); //establecemos la escena
            //Activamos el estilo JMetro, hemos importado la librería que mejora la visualización
            jMetro = new JMetro(jfxtras.styles.jmetro.Style.LIGHT);
            jMetro.setScene(scene);
            //posicionamos la nueva ventana
            this.ventanaPosicion();
            //cambiamos la opacidad de la ventana anterior
            this.cambiarOpacidad(0.5);
            stage.setResizable(false); //no permitimos que la ventana cambie de tamaño
            stage.initStyle(StageStyle.UTILITY); //desactivamos maximinar y minimizar
            //Pasamos los datos a la nueva ventana FrmAlumno mientras sea distinto a CREAR ALUMNO (se usará para EDITAR/ELIMINAR)
            if (!"CREAR ALUMNO".equals(Variables.getTextoFrm())) {
                ctrFrmAlumno.pasarDatos(copiaAlumno);
            }
            stage.showAndWait(); //mostramos la nueva ventana y esperamos
            //El programa continua en esta línea cuando la nueva ventana se cierre
            this.limpiarVista();
            this.cargarTabla("");

        } catch (IOException ex) {
            System.err.println("Error en el inicio validado " + ex);
        }
    }

    //este método obtiene la posición de la actual ventana en coordenadas x, y
    //vamos a usar estos datos para posicionar la ventana correctamente
    public double[] obtenPosicionX_Y() {
        double[] posicionxy = new double[2];
        //creamos una nueva ventana temporal capturando de cualquier btn/lbl la escena y ventana
        //se entiende que los btn o lbl forman parte de la ventana que deseamos obtener datos
        Stage myStage = (Stage) this.lblRegistros.getScene().getWindow();
        posicionxy[0] = myStage.getX();
        posicionxy[1] = myStage.getY();
        return posicionxy;
    }

    public void ventanaPosicion() {
        posicion = new double[2];
        posicion = obtenPosicionX_Y();
        posicion[0] = posicion[0] + 165;
        posicion[1] = posicion[1] + 105;
        stage.setX(posicion[0]);
        stage.setY(posicion[1]);
    }

    public void cambiarOpacidad(double valor) {
        Stage myStage = (Stage) this.lblRegistros.getScene().getWindow();
        myStage.setOpacity(valor);
    }

    private void limpiarVista() {
        this.cambiarOpacidad(1);
        offOnBotones(true);
        this.txtFiltrarAlumnoTabla.setText("");
        Variables.setTextoFrm("");  //el texto superior que aparece al entrar en FrmAlumno
    }

    private void offOnBotones(boolean estado) {
        if (Variables.getOffBotonesAlumnos() == 0) {
            this.btnEditar.setDisable(estado);
            this.btnEliminar.setDisable(estado);
            lblInfoAlumno.setText("Haz doble clic o [ENTER] sobre el registro para Editar");
        } else {
            this.btnNuevo.setDisable(true);
            this.btnEditar.setDisable(true);
            this.btnEliminar.setDisable(true);
            lblInfoAlumno.setText("Haz doble clic o [ENTER] sobre el registro a seleccionar");
        }
    }

    private void cerrarVentana() {
        Stage myStage = (Stage) this.lblRegistros.getScene().getWindow();
        myStage.close();
    }

    private void guardarDatosAlumno() {
        Variables.setCodigoAlumno(String.valueOf(copiaAlumno.getIdRegistro()));
        Variables.setDni(copiaAlumno.getDni());
        Variables.setNombre(copiaAlumno.getNombre());
        Variables.setApellido1(copiaAlumno.getApellido1());
        Variables.setApellido2(copiaAlumno.getApellido2());
    }

}
