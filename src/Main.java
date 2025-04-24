

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application
{   

    private ArrayList<Boids> boids=new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
        root.setTranslateX(600);
        root.setTranslateY(300);

        Scene sc=new Scene(root,1200,600);
        Random random=new Random();
        for (int n=0;n<=50;n++){
            boids.add(new Boids((-1+2*random.nextDouble())*600,(-1+2*random.nextDouble())*300));
            root.getChildren().add(boids.get(n).getShape());
        }
        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Boids boid : boids){
                    boid.move(boids,1200,600);
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

