import java.lang.reflect.Parameter;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application
{   
    private final int LARGEUR_FENETRE = 1250;
    private final int HAUTEUR_FENETRE = 700;
    public static final int LARGEUR_ECRAN = 1025;
    public static final int HAUTEUR_ECRAN = 700;
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

    private int nb_oiseaux;
    private int nb_predateurs;

    private ArrayList<Boids> boids=new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{

        this.nb_oiseaux = nb_oiseaux_init;
        this.nb_predateurs = nb_predateurs_init;
        Boids.avoid_parameter = avoid_parameter_init;
        Boids.alignment_parameter = alignment_parameter_init;
        Boids.cohesion_parameter = cohesion_parameter_init;
        Boids.wind_parameter = wind_parameter_init;
        Boids.theta = theta_init;

        HBox root = new HBox(LARGEUR_ECRAN);
        Scene sc=new Scene(root,LARGEUR_FENETRE,HAUTEUR_FENETRE);

        sc.getStylesheets().add(getClass().getResource("sliderStyles.css").toExternalForm());

        // Création et gestion de l'écran où apparaissent les oiseaux
        Pane ecran = new Pane();
        ecran.setTranslateX(LARGEUR_ECRAN/2);
        ecran.setTranslateY(HAUTEUR_ECRAN/2);

        Random random=new Random();
        for (int n=0;n<=nb_oiseaux_init-1;n++)
        {
            boids.add(new Boids((-1+2*random.nextDouble())*(LARGEUR_ECRAN/2),(-1+2*random.nextDouble())*(HAUTEUR_ECRAN/2)));
            ecran.getChildren().add(boids.get(n).getShape());
        }

        // Création du tableau de commande où sont les boutons
        VBox cb = new VBox(10);


        Slider slider_oiseaux = new Slider(0, nb_oiseaux_max, nb_oiseaux_init);
        slider_oiseaux.setMajorTickUnit(100);
        slider_oiseaux.setShowTickMarks(true);
        slider_oiseaux.setShowTickLabels(true);
        slider_oiseaux.getStyleClass().add("slider-nboiseaux");

        Label titre_oiseaux = new Label("Nombre d'oiseaux : " + nb_oiseaux);


        Slider evitement = new Slider(0, avoid_parameter_max, Boids.avoid_parameter);
        evitement.setMajorTickUnit(2);
        evitement.setShowTickMarks(true);
        evitement.setShowTickLabels(true);
        evitement.getStyleClass().add("slider-parameter");

        Label titre_evitement = new Label("Evitement : " + String.format("%.2f",Boids.avoid_parameter));

        Slider alignement = new Slider(0, alignment_parameter_max, Boids.alignment_parameter);
        alignement.setMajorTickUnit(2);
        alignement.setShowTickMarks(true);
        alignement.setShowTickLabels(true);
        alignement.getStyleClass().add("slider-parameter");

        Label titre_alignement = new Label("Alignement : " + String.format("%.2f",Boids.alignment_parameter));

        Slider cohesion = new Slider(0, cohesion_parameter_max, Boids.cohesion_parameter);
        cohesion.setMajorTickUnit(0.002);
        cohesion.setShowTickMarks(true);
        cohesion.setShowTickLabels(true);
        cohesion.getStyleClass().add("slider-parameter");

        Label titre_cohesion = new Label("Cohesion : " + String.format("%.4f",Boids.cohesion_parameter));


        // Vent
        Slider force_vent = new Slider (0, wind_parameter_max, wind_parameter_init);
        force_vent.setMajorTickUnit(1);
        force_vent.setShowTickMarks(true);
        force_vent.setShowTickLabels(true);
        force_vent.getStyleClass().add("slider-wind");

        Label titre_wind_value = new Label("Vitesse du vent : " + String.format("%.2f",Boids.wind_parameter));


        // Sens du vent
        
        Slider direction_vent = new Slider(0, 2*Math.PI, theta_init);
        direction_vent.setMajorTickUnit(2*Math.PI);
        direction_vent.setShowTickMarks(true);
        direction_vent.setShowTickLabels(true);
        direction_vent.getStyleClass().add("slider-wind");

        Label titre_theta = new Label("Direction du vent (rad) : " + String.format("%.2f",Boids.theta));
        


        // Prédateurs
        
        Button plus_predateur = new Button("Ajouter un prédateur");
        plus_predateur.getStyleClass().add("bouton-predateur");
        Button moins_predateur = new Button("Retirer un prédateur");
        moins_predateur.getStyleClass().add("bouton-predateur");
        Label titre_predateurs = new Label("Nombre de prédateurs : " + nb_predateurs);
        

        Button reinit = new Button("Réinitialiser les paramètres");
        reinit.getStyleClass().add("bouton-rei");


        // Quelques séparateurs pour y voir plus clair
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();   // Pour la partie prédateurs
        Separator sep4 = new Separator();


        // Sans les prédateurs
        cb.getChildren().addAll(titre_oiseaux,slider_oiseaux, sep1, titre_wind_value,force_vent,titre_theta,direction_vent, sep2, titre_evitement,evitement,titre_alignement,alignement,titre_cohesion,cohesion,sep4, reinit);

        // Avec les prédateurs
        //cb.getChildren().addAll(titre_oiseaux,slider_oiseaux, sep1, titre_wind_value,force_vent,titre_theta,direction_vent, sep2, titre_predateurs,plus_predateur,moins_predateur, sep3, titre_evitement,evitement,titre_alignement,alignement,titre_cohesion,cohesion,sep4, reinit);


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
                            boids.add(new Boids((-1+2*random.nextDouble())*(LARGEUR_ECRAN/2),(-1+2*random.nextDouble())*(HAUTEUR_ECRAN/2)));
                            ecran.getChildren().add(boids.get(n).getShape());
                        };
                    }
                    else
                    {
                        for(int n=nb_oiseaux-1;n>=newValue.intValue();n--)
                        {
                            ecran.getChildren().remove(boids.get(n).getShape());
                            boids.remove(n);
                        };
                        // Remarque : Au moment d'ajouter les prédateurs, vérifier que ceux-ci ne sont pas retirés de l'écran
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
                    Boids.avoid_parameter = newValue.doubleValue();
                    titre_evitement.setText("Evitement : " + String.format("%.2f",Boids.avoid_parameter));
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
                    Boids.alignment_parameter = newValue.doubleValue();
                    titre_alignement.setText("Alignement : " + String.format("%.2f",Boids.alignment_parameter));
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
                    Boids.cohesion_parameter = newValue.doubleValue();
                    titre_cohesion.setText("Cohésion : " + String.format("%.4f",Boids.cohesion_parameter));
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
                    Boids.wind_parameter = newValue.intValue();
                    titre_wind_value.setText("Vitesse du vent : " + String.format("%.2f",Boids.wind_parameter));
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
                    Boids.theta = newValue.doubleValue();
                    titre_theta.setText("Direction du vent (rad) : " + String.format("%.2f",Boids.theta));
                }
            }
        );
        

        
        reinit.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                nb_oiseaux = nb_oiseaux_init;
                titre_oiseaux.setText("Nombre d'oiseaux : " + nb_oiseaux);
                slider_oiseaux.adjustValue(nb_oiseaux);
                boids.clear();
                ecran.getChildren().clear();
                for (int n=0;n<=nb_oiseaux-1;n++)
                {
                    boids.add(new Boids((-1+2*random.nextDouble())*(LARGEUR_ECRAN/2),(-1+2*random.nextDouble())*(HAUTEUR_ECRAN/2)));
                    ecran.getChildren().add(boids.get(n).getShape());
                }
                nb_predateurs = nb_predateurs_init;
                //titre_predateurs.setText("Nombre de prédateurs : " + nb_predateurs);
                // predateurs.clear();
                Boids.avoid_parameter = avoid_parameter_init;
                titre_evitement.setText("Evitement : " + String.format("%.2f",Boids.avoid_parameter));
                evitement.adjustValue(Boids.avoid_parameter);
                Boids.alignment_parameter = alignment_parameter_init;
                titre_alignement.setText("Alignement : " + String.format("%.2f",Boids.alignment_parameter));
                alignement.adjustValue(Boids.alignment_parameter);
                Boids.cohesion_parameter = cohesion_parameter_init;
                titre_cohesion.setText("Cohésion : " + String.format("%.4f",Boids.cohesion_parameter));
                cohesion.adjustValue(Boids.cohesion_parameter);
                Boids.wind_parameter = wind_parameter_init;
                titre_wind_value.setText("Vitesse du vent : " + String.format("%.2f",Boids.wind_parameter));
                force_vent.adjustValue(Boids.wind_parameter);
                Boids.theta = theta_init;
                titre_theta.setText("Direction du vent (rad) : " + String.format("%.2f",Boids.theta));
                direction_vent.adjustValue(Boids.theta);

            }
        });

        // On place les éléments dans la fenêtre (scène)
        root.getChildren().addAll(ecran,cb);


        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Boids boid : boids){
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

