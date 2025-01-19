import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class Boids {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private final Polygon shape;
    private double vitessemax=3;
    private double rayon_sep=40;
    private double rayon_align=45;
    private double avoid_parameter=1;
    private double alignement_parameter=0.1;
    private double vx_avg=0;
    private double vy_avg=0;
    private int n_sep=1;

    public Boids(double x,double y){
        this.x=x;
        this.y=y;
        this.vx = 1;
        this.vy = 1;
        // Créer une forme triangulaire pour représenter le boid
        this.shape = new Polygon();
        this.shape.getPoints().addAll(
            0.0, -10.0,  // Pointe avant
            -5.0, 5.0,   // Coin gauche
            0.0,0.0,
            5.0, 5.0     // Coin droit
        );
        this.shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        this.shape.setTranslateX(x);
        this.shape.setTranslateY(y);

    }
    public double getx(){
        return x;
    }
    public double gety(){
        return y;
    }
    public Polygon getShape() {
        return shape;
    }
    public void move(ArrayList<Boids> boids,int longueur,int largeur){

        // Mettre à jour la position et la rotation de la forme
        shape.setTranslateX(x);
        shape.setTranslateY(y);
         // séparation
        double somme_distsepX = 0;
        double somme_distsepY = 0;
        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance=Math.sqrt(dx*dx+dy*dy);
                if (distance<rayon_sep){
                    // somme pour i dans l'ensemble de séparation du vecteur PPi selon x et selon y
                    somme_distsepX += dx;
                    somme_distsepY += dy;
                    
                }
            }
        }
        
        
        vx += avoid_parameter*somme_distsepX;
        vy += avoid_parameter*somme_distsepY;



        double vitesse=Math.sqrt(vx*vx+vy*vy);
        if (vitesse>vitessemax){
            vx=(vx/vitesse)*vitessemax;
            vy=(vy/vitesse)*vitessemax;
        }
        x=x+vx;
        y=y+vy;
        if (x<0) {x+=longueur;}
        if (x>longueur){x=x-longueur;}
        if (y<0){y+=largeur;}
        if (y>largeur){y=y-largeur;}
        // Mettre à jour la position et la rotation de la forme
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        double angle = Math.toDegrees(Math.atan2(vy, vx)) + 90; // +90 pour aligner avec la pointe
        shape.setRotate(angle);
    }

}
