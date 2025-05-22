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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Main3D extends Application
{   
    // Transformations pour la caméra
    private final Rotate rotate_x = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotate_y = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -2000);

    private ArrayList<Boids3D> boids=new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        /*
        // Sphère
        Sphere sphere = new Sphere(100);
        sphere.setTranslateX(0);
        sphere.setTranslateY(0);
        sphere.setTranslateZ(0);
        */
        

        AmbientLight light = new AmbientLight(Color.LIGHTGREY);

        PointLight light2 = new PointLight(Color.LIGHTGREY);
         
        
        
        Group root = new Group();
        root.getChildren().addAll(light, light2);
        

        // Boids en 3D
        Random random=new Random();
        
        
        for (int n=0;n<=50;n++){
            boids.add(new Boids3D((-1+2*random.nextDouble())*600,(-1+2*random.nextDouble())*300,(-1+2*random.nextDouble())*300));
            ;
            root.getChildren().add(boids.get(n).getShape());
            light.getScope().add(boids.get(n).getShape());
            light2.getScope().add(boids.get(n).getShape());
        }
        

        

        Scene scene = new Scene(root, 1200, 600, true); // true to use depth buffer
        scene.setFill(Color.BLACK);

        // Point de vue de la scène
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(rotate_x, rotate_y, translate);
        scene.setCamera(camera);
        
        /*
        Random random=new Random();
        for (int n=0;n<=50;n++){
            boids.add(new Boids3D((-1+2*random.nextDouble())*600,(-1+2*random.nextDouble())*300, 100));
            root.getChildren().add(boids.get(n).getShape());
        }
        
        */


        
        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Boids3D boid : boids){
                    boid.move(boids,1200,600);
                }
            }
        };
        timer.start();
        

        primaryStage.setTitle("Simulation d'une nuée d'oiseaux");
        primaryStage.setScene(scene);
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


