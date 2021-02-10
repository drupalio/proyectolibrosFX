package controlador;

import controlador.eventos.FrmAlumnoNuevoController;
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

    private static ObservableList<ClassAlumno> items; //instanciamos un objeto tipo arrayList especial para JavaFX
    private static AlumnoDAO datos;   //instanciamos la clase AlumnoDAO la cual gestiona las acciones hacia nuestra BD
    private int posicionAlumnoTabla;  //guardaremos la posición de la fila de la tabla
    private static Scene scene;   //variable de clase Scene donde se produce la acción con los elementos creados
    private static Stage stage;   //variable de clase Stage que es la ventana actual
    private double[] posicion;    //posición de la ventana en eje X-Y
    private JMetro jMetro;  //variable para cambiar la vista de la escena
    private int registro;   //variable donde guardar datos de la tabla
    private String dni, nombre, apellido1, apellido2;  //variable donde guardar datos de la tabla
    private ClassAlumno copiaAlumno;  //objeto donde guardar datos de la tabla

    @FXML
    private TextField txtFiltrar;
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.btnEditar.setDisable(true);
        this.btnEliminar.setDisable(true);
        datos = new AlumnoDAO();  //instanciamos un objeto para hacer consultas a la BD
    }

    @FXML
    private void buscar(ActionEvent event) {
        this.btnEditar.setDisable(true);
        this.btnEliminar.setDisable(true);
        this.cargarTabla(txtFiltrar.getText());
    }

    @FXML
    private void limpiar(ActionEvent event) {
        this.limpiarVista();
        this.cargarTabla("");
    }

    @FXML
    private void editar(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar
        Variables.textoFrmAlumno = "EDITAR ALUMNO";  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar.
        Variables.textoFrmAlumno = "CREAR ALUMNO";  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @FXML
    private void eliminar(ActionEvent event) {
        //guardamos en la variable el valor de la acción a ejecutar.
        Variables.textoFrmAlumno = "ELIMINAR ALUMNO";  //Se usará posteriormente en el controlador FrmAlumno
        this.cargarFrmAlumno();
    }

    @FXML
    private void pulsoEnter(KeyEvent event) {
        //keyPressed: cuando se pulsa ENTER en la caja de textoBuscar hacemos la acción de buscar
        Object evt = event.getSource();
        if (evt.equals(txtFiltrar)) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.cargarTabla(txtFiltrar.getText());
            }
        }
    }

    @FXML
    private void posicionTeclaTabla(KeyEvent event) {
        //cuando nos desplazamos con el cursor por la tabla capturamos la información de la fila
        ClassAlumno claseAlumno = tablaAlumno.getSelectionModel().getSelectedItem();
        if (claseAlumno != null) {  //si no es NULL capturamos los datos de la fila
            registro = claseAlumno.getIdRegistro();
            dni = claseAlumno.getDni();
            nombre = claseAlumno.getNombre();
            apellido1 = claseAlumno.getApellido1();
            apellido2 = claseAlumno.getApellido2();
            //creamos un nuevo objeto con los datos capturados
            copiaAlumno = new ClassAlumno(registro, dni, nombre, apellido1, apellido2);
        }
        this.btnEditar.setDisable(false);
        this.btnEliminar.setDisable(false);
    }

    @FXML
    private void posicionRatonTabla(MouseEvent event) {
        //cuando pulsamos con el ratón en algún registro de la tabla capturamos la información de la fila
        ClassAlumno claseAlumno = tablaAlumno.getSelectionModel().getSelectedItem();
        if (claseAlumno != null) {  //si no es NULL capturamos los datos de la fila
            registro = claseAlumno.getIdRegistro();
            dni = claseAlumno.getDni();
            nombre = claseAlumno.getNombre();
            apellido1 = claseAlumno.getApellido1();
            apellido2 = claseAlumno.getApellido2();
            //creamos un nuevo objeto con los datos capturados
            copiaAlumno = new ClassAlumno(registro, dni, nombre, apellido1, apellido2);
        }
        this.btnEditar.setDisable(false);
        this.btnEliminar.setDisable(false);
    }

    @SuppressWarnings("unchecked")
    public void cargarTabla(String filtro) {
        //asignamos a cada columna de la tabla el valor de su campo referenciado en ClassAlumno
        this.colRegistro.setCellValueFactory(new PropertyValueFactory("idRegistro"));
        this.ColDni.setCellValueFactory(new PropertyValueFactory("dni"));
        this.ColNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colApellido1.setCellValueFactory(new PropertyValueFactory("apellido1"));
        this.colApellido2.setCellValueFactory(new PropertyValueFactory("apellido2"));
        items = datos.listar(filtro);  //llamamos al método "listar" dentro de la clase AlumnoDAO
        this.tablaAlumno.refresh();  //refrescamos los datos de la tabla (sobre todo es interesante cuando actualizamos)
        this.tablaAlumno.setItems(items); //mostramos las columnas de la tabla
        lblRegistros.setText("Mostrando " + Variables.registrosMostrados + " de un total de " + datos.total() + " registros");
    }

    private void cargarFrmAlumno() {
        try {
            //cargamos la vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FrmAlumnoNuevo.fxml"));
            //instanciamos y cargamos el FXML en el padre
            Parent root = loader.load();
            //instanciamos al controlador FrmAlumnoNuevo haciendo uso del nuevo método getController
            FrmAlumnoNuevoController ctrFrmAlumno = loader.getController();
            //creamos la nueva escena que viene del padre
            scene = new Scene(root);
            stage = new Stage();    //creamos la nueva ventana
            stage.setTitle("Alta de Alumnos"); //ponemos un título
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
            if (!"CREAR ALUMNO".equals(Variables.textoFrmAlumno)) {
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
        this.btnEditar.setDisable(true);
        this.btnEliminar.setDisable(true);
        this.txtFiltrar.setText("");
        Variables.textoFrmAlumno = "";
    }

    
}
