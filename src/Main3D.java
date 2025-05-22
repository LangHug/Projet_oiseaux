import java.lang.reflect.Parameter;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import javax.swing.GroupLayout.Group;




public class Main3D extends Application
{  
    // Variables
    private final int LARGEUR_FENETRE = 1250;
    private final int HAUTEUR_FENETRE = 600;
    public static final int LARGEUR_ECRAN = 1050;
    public static final int HAUTEUR_ECRAN = 600;
    public static final int PROFONDEUR_ECRAN = 500;
    private final int nb_oiseaux_init = 100;
    private final double avoid_parameter_max = 20;
    private final double alignment_parameter_max = 3;
    private final double cohesion_parameter_max = 0.1;
    private final double nb_oiseaux_max = 1000;
    private final double wind_value_init = 0;
    private final double wind_value_max = 100;
    private final int nb_predateurs_init = 0;
    private final double sens_vent_init = 0;
    private final double avoid_parameter_init = 1;
    private final double alignment_parameter_init = 1;
    private final double cohesion_parameter_init = 0.0005;   
    
    private int nb_oiseaux;
    private int nb_predateurs;


    // Transformations pour la caméra
    private final Rotate rotate_x = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotate_y = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -1500);


    private ArrayList<Boids3D> boids=new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
        // Paramètres à leur valeur initiale
        this.nb_oiseaux = nb_oiseaux_init;
        this.nb_predateurs = nb_predateurs_init;
        Boids3D.avoid_parameter = avoid_parameter_init;
        Boids3D.alignment_parameter = alignment_parameter_init;
        Boids3D.cohesion_parameter = cohesion_parameter_init;
        */


        




        
        // Lumière
        AmbientLight light = new AmbientLight(Color.LIGHTGREY);
        PointLight light2 = new PointLight(Color.LIGHTGREY);
         
        
        // Ecran (où les oiseaux volent)
        Group root = new Group();
        
        root.getChildren().addAll(light, light2);

        

        // Boids en 3D
        Random random=new Random();
        for (int n=0;n<=nb_oiseaux_init-1;n++){
            boids.add(new Boids3D((-1+2*random.nextDouble())*LARGEUR_ECRAN,(-1+2*random.nextDouble())*HAUTEUR_ECRAN,random.nextDouble()*PROFONDEUR_ECRAN));
            ;
            root.getChildren().add(boids.get(n).getShape());
            light.getScope().add(boids.get(n).getShape());
            light2.getScope().add(boids.get(n).getShape());
        }
       
        Scene sc=new Scene(root,LARGEUR_FENETRE,HAUTEUR_FENETRE, true); // true to use depth buffer
        sc.setFill(Color.BLACK);

        // Caméra
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(rotate_x, rotate_y, translate);
        sc.setCamera(camera);

        
        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Boids3D boid : boids){
                    boid.move(boids,LARGEUR_ECRAN,HAUTEUR_ECRAN);
                }
            }
        };
        timer.start();
        
        
        primaryStage.setTitle("Simulation d'une nuée d'oiseaux");
        primaryStage.setScene(sc);
        primaryStage.show();








        /*// Créer une animation avec Timeline
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(20), event -> oiseau.move(400,400)));
        // Répéter l'animation indéfiniment
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        Scene sc=new Scene(root,400,400);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Déplacement d'un oiseau");
        primaryStage.show();*/
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}


