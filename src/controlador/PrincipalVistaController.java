package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;

/**
 * FXML Controller class
 *
 * @author telev
 */
public class PrincipalVistaController implements Initializable {

    private static Scene scene;   //variable de clase Scene donde se produce la acción con los elementos creados
    private static Stage stage;   //variable de clase Stage que es la ventana actual
    private JMetro jMetro;
    private double[] posicion;  //posición de la ventana en eje X-Y

    @FXML
    private Menu mnuAlumno;
    @FXML
    private Font x3;
    @FXML
    private Label lblTextoInferior;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void crearAlumno(ActionEvent event) {
        cargarVistaAlumno(posicion);
    }

    @FXML
    private void buscarAlumno(ActionEvent event) {
        cargarVistaAlumno(posicion);
    }

    @FXML
    private void editarAlumno(ActionEvent event) {
        cargarVistaAlumno(posicion);
    }

    @FXML
    private void eliminarAlumno(ActionEvent event) {
        cargarVistaAlumno(posicion);
    }

    @FXML
    private void cerrarAlumno(ActionEvent event) {
        if (stage != null) {
            stage.close();
        }

    }

    @FXML
    private void cerrarApliacion(ActionEvent event) {
        Platform.exit(); //Es ideal para cuando se cierre la aplicación se ejecute el proceso stop()
    }

    public void cargarVistaAlumno(double[] posicion) {
        try {
            //cargamos la vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/AlumnoVista.fxml"));
            //instanciamos y cargamos el FXML en el padre
            Parent root = loader.load();
            //instanciamos al controlador alumno haciendo uso del nuevo método getController
            AlumnoVistaController controladorAlumno = loader.getController();
            //creamos la nueva escena que viene del padre
            scene = new Scene(root);
            stage = new Stage();    //creamos la nueva ventana
            stage.setTitle("Gestión de Alumnos"); //ponemos un título
            stage.initModality(Modality.APPLICATION_MODAL);  //hacemos que la escena nueva tome el foco y no permita cambiarse de ventana
            stage.setScene(scene); //establecemos la escena
            //Activamos el estilo JMetro, hemos importado la librería que mejora la visualización
            //jMetro = new JMetro(jfxtras.styles.jmetro.Style.LIGHT);
            //jMetro.setScene(scene);
            //posicionamos la nueva ventana
            posicion = posicionX_Y();
            posicion[0] = posicion[0] + 90;
            posicion[1] = posicion[1] + 105;
            stage.setX(posicion[0]);
            stage.setY(posicion[1]);
            //mostramos la nueva ventana y esperamos
            
            controladorAlumno.cargarTabla(""); //cargamos y mostramos la tabla de alumnos
            stage.showAndWait();
            
        } catch (IOException ex) {
            System.err.println("Error en el inicio validado " + ex);
        }
    }

    //este método obtiene la posición de la actual ventana en coordenadas x, y
    //vamos a usar estos datos para posicionar la ventana correctamente
    public double[] posicionX_Y() {
        double[] posicionxy = new double[2];
        //creamos una nueva ventana temporal capturando de cualquier btn/lbl la escena y ventana
        //se entiende que los btn o lbl forman parte de la ventana que deseamos obtener datos
        Stage myStage = (Stage) this.lblTextoInferior.getScene().getWindow();
        posicionxy[0] = myStage.getX();
        posicionxy[1] = myStage.getY();
        return posicionxy;
    }

}
