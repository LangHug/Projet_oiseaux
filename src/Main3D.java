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
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main3D extends Application
{   
    // Constantes
    private final int LARGEUR_FENETRE = 1250;
    private final int HAUTEUR_FENETRE = 600;
    public static final int LARGEUR_ECRAN = 1000;
    public static final int HAUTEUR_ECRAN = 600;
    public static final int PROFONDEUR_ECRAN = 1000;
    private static final int DISTANCE_CAMERA = 1700;
    private final int nb_oiseaux_init = 100;
    private final double avoid_parameter_max = 10;
    private final double alignment_parameter_max = 10;
    private final double cohesion_parameter_max = 0.01;
    private final int nb_oiseaux_max = 600;
    private final double wind_parameter_init = 0;
    private final double wind_parameter_max = 5;
    private final int nb_predateurs_init = 0;
    private final double theta_init = 3*Math.PI/4;
    private final double avoid_parameter_init = 1;
    private final double alignment_parameter_init = 1;
    private final double cohesion_parameter_init = 0.001;
    

    // Variables
    private int nb_oiseaux;
    private int nb_predateurs;       



    // Transformations pour la caméra
    private final Rotate rotate_x = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotate_y = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -DISTANCE_CAMERA);

    private ArrayList<Boids3D> boids=new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        // Paramètres à leur valeur initiale
        this.nb_oiseaux = nb_oiseaux_init;
        this.nb_predateurs = nb_predateurs_init;
        Boids3D.avoid_parameter = avoid_parameter_init;
        Boids3D.alignment_parameter = alignment_parameter_init;
        Boids3D.cohesion_parameter = cohesion_parameter_init;
        Boids3D.wind_parameter = wind_parameter_init;
        Boids3D.theta = theta_init;
        

        // Lumière
        AmbientLight light = new AmbientLight(Color.LIGHTGREY);
        PointLight light2 = new PointLight(Color.LIGHTGREY);

        
        // Ecran où sont les boids
        Group ecran = new Group();
        ecran.getChildren().addAll(light, light2);
        

        // Boids en 3D
        Random random=new Random();
        
        for (int n=0;n<=nb_oiseaux_init-1;n++){
            boids.add(new Boids3D((-1+2*random.nextDouble())*LARGEUR_ECRAN/2,(-1+2*random.nextDouble())*HAUTEUR_ECRAN/2,(-1+2*random.nextDouble())*PROFONDEUR_ECRAN/2));
            ;
            ecran.getChildren().add(boids.get(n).getShape());
            light.getScope().add(boids.get(n).getShape());
            light2.getScope().add(boids.get(n).getShape());
        }
        


        // On ajoute les commandes à droite

        Label titre_oiseaux = new Label("Nombre d'oiseaux : " + nb_oiseaux);
        titre_oiseaux.setTranslateX(LARGEUR_ECRAN/2);
        titre_oiseaux.setTranslateY(-HAUTEUR_ECRAN/2);
        titre_oiseaux.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_oiseaux);

        Slider slider_oiseaux = new Slider(0, nb_oiseaux_max, nb_oiseaux_init);
        slider_oiseaux.setTranslateX(LARGEUR_ECRAN/2);
        slider_oiseaux.setTranslateY(-HAUTEUR_ECRAN/2+20);
        slider_oiseaux.setTranslateZ(-PROFONDEUR_ECRAN/2);
        slider_oiseaux.setMajorTickUnit(100);
        slider_oiseaux.setShowTickMarks(true);
        slider_oiseaux.setShowTickLabels(true);
        slider_oiseaux.getStyleClass().add("slider-nboiseaux");
        ecran.getChildren().add(slider_oiseaux);

        Label titre_evitement = new Label("Evitement : " + String.format("%.2f",Boids3D.avoid_parameter));
        titre_evitement.setTranslateX(LARGEUR_ECRAN/2);
        titre_evitement.setTranslateY(-HAUTEUR_ECRAN/2+70);
        titre_evitement.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_evitement);

        Slider evitement = new Slider(0, avoid_parameter_max, Boids3D.avoid_parameter);
        evitement.setTranslateX(LARGEUR_ECRAN/2);
        evitement.setTranslateY(-HAUTEUR_ECRAN/2+90);
        evitement.setTranslateZ(-PROFONDEUR_ECRAN/2);
        evitement.setMajorTickUnit(2);
        evitement.setShowTickMarks(true);
        evitement.setShowTickLabels(true);
        evitement.getStyleClass().add("slider-parameter");
        ecran.getChildren().add(evitement);

        Label titre_alignement = new Label("Alignement : " + String.format("%.2f",Boids.alignment_parameter));
        titre_alignement.setTranslateX(LARGEUR_ECRAN/2);
        titre_alignement.setTranslateY(-HAUTEUR_ECRAN/2+140);
        titre_alignement.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_alignement);
        
        Slider alignement = new Slider(0, alignment_parameter_max, Boids3D.alignment_parameter);
        alignement.setMajorTickUnit(2);
        alignement.setShowTickMarks(true);
        alignement.setShowTickLabels(true);
        alignement.getStyleClass().add("slider-parameter");
        alignement.setTranslateX(LARGEUR_ECRAN/2);
        alignement.setTranslateY(-HAUTEUR_ECRAN/2+160);
        alignement.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(alignement);

        Label titre_cohesion = new Label("Cohesion : " + String.format("%.4f",Boids3D.cohesion_parameter));
        titre_cohesion.setTranslateX(LARGEUR_ECRAN/2);
        titre_cohesion.setTranslateY(-HAUTEUR_ECRAN/2+210);
        titre_cohesion.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_cohesion);

        Slider cohesion = new Slider(0, cohesion_parameter_max, Boids3D.cohesion_parameter);
        cohesion.setMajorTickUnit(0.002);
        cohesion.setShowTickMarks(true);
        cohesion.setShowTickLabels(true);
        cohesion.getStyleClass().add("slider-parameter");
        cohesion.setTranslateX(LARGEUR_ECRAN/2);
        cohesion.setTranslateY(-HAUTEUR_ECRAN/2+230);
        cohesion.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(cohesion);        

        // Vent
        Label titre_wind_value = new Label("Vitesse du vent : " + String.format("%.2f",Boids3D.wind_parameter));
        titre_wind_value.setTranslateX(LARGEUR_ECRAN/2);
        titre_wind_value.setTranslateY(-HAUTEUR_ECRAN/2+280);
        titre_wind_value.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_wind_value);

        Slider force_vent = new Slider (0, wind_parameter_max, wind_parameter_init);
        force_vent.setMajorTickUnit(1);
        force_vent.setShowTickMarks(true);
        force_vent.setShowTickLabels(true);
        force_vent.getStyleClass().add("slider-wind");
        force_vent.setTranslateX(LARGEUR_ECRAN/2);
        force_vent.setTranslateY(-HAUTEUR_ECRAN/2+300);
        force_vent.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(force_vent);

        // Sens du vent
        Label titre_theta = new Label("Direction du vent (rad) : " + String.format("%.2f",Boids3D.theta));
        titre_theta.setTranslateX(LARGEUR_ECRAN/2);
        titre_theta.setTranslateY(-HAUTEUR_ECRAN/2+350);
        titre_theta.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_theta);
        
        Slider direction_vent = new Slider(0, 2*Math.PI, theta_init);
        direction_vent.setMajorTickUnit(2*Math.PI);
        direction_vent.setShowTickMarks(true);
        direction_vent.setShowTickLabels(true);
        direction_vent.getStyleClass().add("slider-wind");
        direction_vent.setTranslateX(LARGEUR_ECRAN/2);
        direction_vent.setTranslateY(-HAUTEUR_ECRAN/2+370);
        direction_vent.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(direction_vent);

        /*
        // Prédateurs
        Label titre_predateurs = new Label("Nombre de prédateurs : " + nb_predateurs);
        titre_predateurs.setTranslateX(LARGEUR_ECRAN/2);
        titre_predateurs.setTranslateY(-HAUTEUR_ECRAN/2+420);
        titre_predateurs.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(titre_predateurs);

        Button plus_predateur = new Button("Ajouter un prédateur");
        plus_predateur.getStyleClass().add("bouton-predateur");
        plus_predateur.setTranslateX(LARGEUR_ECRAN/2);
        plus_predateur.setTranslateY(-HAUTEUR_ECRAN/2+450);
        plus_predateur.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(plus_predateur);

        Button moins_predateur = new Button("Retirer un prédateur");
        moins_predateur.getStyleClass().add("bouton-predateur");
        moins_predateur.setTranslateX(LARGEUR_ECRAN/2);
        moins_predateur.setTranslateY(-HAUTEUR_ECRAN/2+490);
        moins_predateur.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(moins_predateur);
        */


        // Réinitialise les paramètres
        Button reinit = new Button("Réinitialiser les paramètres");
        reinit.getStyleClass().add("bouton-rei3D");
        reinit.setTranslateX(LARGEUR_ECRAN/2);
        reinit.setTranslateY(-HAUTEUR_ECRAN/2+420);
        reinit.setTranslateZ(-PROFONDEUR_ECRAN/2);
        ecran.getChildren().add(reinit);

        
        // Scène
        Scene scene = new Scene(ecran, LARGEUR_FENETRE, HAUTEUR_FENETRE, true); // true to use depth buffer
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("sliderStyles.css").toExternalForm());


        // Caméra
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(rotate_x, rotate_y, translate);
        scene.setCamera(camera);

        

        // Action des boutons et sliders

        slider_oiseaux.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    if(newValue.intValue() > nb_oiseaux)
                    {
                        for(int n=nb_oiseaux;n<=newValue.intValue()-1;n++)
                        {
                            boids.add(new Boids3D((-1+2*random.nextDouble())*(LARGEUR_ECRAN/2),(-1+2*random.nextDouble())*(HAUTEUR_ECRAN/2),(-1+2*random.nextDouble())*PROFONDEUR_ECRAN/2));
                            ecran.getChildren().add(boids.get(n).getShape());
                            light.getScope().add(boids.get(n).getShape());
                            light2.getScope().add(boids.get(n).getShape());
                        };
                    }
                    else
                    {
                        for(int n=nb_oiseaux-1;n>=newValue.intValue();n--)
                        {
                            ecran.getChildren().remove(boids.get(n).getShape());
                            boids.remove(n);
                        };
                    }

                    nb_oiseaux = newValue.intValue();
                    titre_oiseaux.setText("Nombre d'oiseaux : " + nb_oiseaux);

                }
            }
        );


        evitement.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    Boids3D.avoid_parameter = newValue.doubleValue();
                    titre_evitement.setText("Evitement : " + String.format("%.2f",Boids3D.avoid_parameter));
                }
            }
        );


        alignement.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    Boids3D.alignment_parameter = newValue.doubleValue();
                    titre_alignement.setText("Alignement : " + String.format("%.2f",Boids3D.alignment_parameter));
                }
            }
        );

        cohesion.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    Boids3D.cohesion_parameter = newValue.doubleValue();
                    titre_cohesion.setText("Cohésion : " + String.format("%.4f",Boids3D.cohesion_parameter));
                }
            }
        );



        force_vent.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    Boids3D.wind_parameter = newValue.intValue();
                    titre_wind_value.setText("Vitesse du vent : " + String.format("%.2f",Boids3D.wind_parameter));
                }
            }
        );


        
        direction_vent.valueProperty().addListener
        (
            new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
                {
                    Boids3D.theta = newValue.doubleValue();
                    titre_theta.setText("Direction du vent (rad) : " + String.format("%.2f",Boids3D.theta));
                }
            }
        );
        

        
        reinit.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                nb_oiseaux = nb_oiseaux_init;
                //nb_predateurs = nb_predateurs_init;
                Boids3D.avoid_parameter = avoid_parameter_init;
                Boids3D.alignment_parameter = alignment_parameter_init;
                Boids3D.cohesion_parameter = cohesion_parameter_init;
                Boids3D.wind_parameter = wind_parameter_init;
                Boids3D.theta = theta_init;

                titre_oiseaux.setText("Nombre d'oiseaux : " + nb_oiseaux);
                slider_oiseaux.adjustValue(nb_oiseaux);
                //titre_predateurs.setText("Nombre de prédateurs : " + nb_predateurs);
                // predateurs.clear();
                titre_evitement.setText("Evitement : " + String.format("%.2f",Boids.avoid_parameter));
                evitement.adjustValue(Boids.avoid_parameter);
                titre_alignement.setText("Alignement : " + String.format("%.2f",Boids.alignment_parameter));
                alignement.adjustValue(Boids.alignment_parameter);
                titre_cohesion.setText("Cohésion : " + String.format("%.4f",Boids.cohesion_parameter));
                cohesion.adjustValue(Boids.cohesion_parameter);
                titre_wind_value.setText("Vitesse du vent : " + String.format("%.2f",Boids.wind_parameter));
                force_vent.adjustValue(Boids.wind_parameter);
                titre_theta.setText("Direction du vent (rad) : " + String.format("%.2f",Boids.theta));
                direction_vent.adjustValue(Boids.theta);

                boids.clear();
                ecran.getChildren().clear();
                ecran.getChildren().addAll(light, light2);

                
                for (int n=0;n<=nb_oiseaux-1;n++)
                {
                    boids.add(new Boids3D((-1+2*random.nextDouble())*(LARGEUR_ECRAN/2),(-1+2*random.nextDouble())*(HAUTEUR_ECRAN/2),(-1+2*random.nextDouble())*PROFONDEUR_ECRAN/2));
                    ecran.getChildren().add(boids.get(n).getShape());
                    light.getScope().add(boids.get(n).getShape());
                    light2.getScope().add(boids.get(n).getShape());
                }


                // On ajoute les commandes à droite

                // Avec prédateurs
                //ecran.getChildren().addAll(titre_oiseaux,slider_oiseaux,titre_evitement,evitement,titre_alignement,alignement,titre_cohesion,cohesion,titre_wind_value,force_vent,titre_theta,direction_vent,titre_predateurs,plus_predateur,moins_predateur,reinit);
                
                // Sans prédateurs
                ecran.getChildren().addAll(titre_oiseaux,slider_oiseaux,titre_evitement,evitement,titre_alignement,alignement,titre_cohesion,cohesion,titre_wind_value,force_vent,titre_theta,direction_vent,reinit);


            }
        });




        
        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Boids3D boid : boids){
                    boid.move(boids,LARGEUR_ECRAN,HAUTEUR_ECRAN,PROFONDEUR_ECRAN);
                }
            }
        };
        timer.start();
        

        primaryStage.setTitle("Simulation d'une nuée d'oiseaux");
        primaryStage.setScene(scene);
        primaryStage.show();

        
    }
    public static void main(String[] args) {
        launch(args);
    }
}


