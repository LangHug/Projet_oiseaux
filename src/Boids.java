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
    private double rayon_sep=30;
    private double rayon_cohesion=150;
    private double avoid_parameter=10;
    private double alignment_parameter=0.03;
    private double cohesion_parameter=0.02;


    public Boids(double x,double y){
        this.x=x;
        this.y=y;
        this.vx = -20;
        this.vy = -20;
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
    public double getvx(){
        return vx;
    }
    public double getvy(){
        return vy;
    }
    public Polygon getShape() {
        return shape;
    }
    public void move(ArrayList<Boids> boids,int longueur,int largeur){

        // Mettre à jour la position et la rotation de la forme
        shape.setTranslateX(x);
        shape.setTranslateY(y);
         // séparation
        double Fsx=0; //force séparation selon x
        double Fsy=0; //force séparation selon y
        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance=Math.sqrt(dx*dx+dy*dy);
                if (distance<rayon_sep){
                    // somme pour i dans l'ensemble de séparation du vecteur PPi selon x et selon y
                    Fsx += dx;
                    Fsy += dy;
                    
                }
            }
        }
        
        
        vx += avoid_parameter*Fsx;
        vy += avoid_parameter*Fsy;
        //alignement
        double Fax=0; //force alignement selon x
        double Fay=0; //force alignement selon y
        double average_vx=0; //vitesse moyenne selon x
        double average_vy=0; //vitesse moyenne selon y

        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                
                    average_vx+=boid.getvx();
                    average_vy+=boid.getvy();
                
            }
        }
        Fax=average_vx-this.vx;
        Fay=average_vy-this.vy;
        vx+=alignment_parameter*Fax;
        vy+=alignment_parameter*Fay;
        
        //cohésion
        double Fcx=0;
        double Fcy=0;
        double average_x=0;
        double average_y=0;
        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance=Math.sqrt(dx*dx+dy*dy);
                if (rayon_sep<distance && distance<rayon_cohesion){
                    // calcul le barycentre des positions des boids dans le cercle de cohesion selon x et y
                    average_x+=boid.getx();
                    average_y+=boid.gety();
                }
            }
        }
        Fcx+=average_x-this.x;
        Fcy+=average_y-this.y;
        vx+=cohesion_parameter*Fcx;
        vy+=cohesion_parameter*Fcy;


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
        double angle = Math.toDegrees(Math.atan2(vy, vx)) +90; // +90 pour aligner avec la pointe
        shape.setRotate(angle);
    }

}
