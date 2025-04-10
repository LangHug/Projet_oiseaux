import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Boids {
    private final Polygon shape;
    private double angle;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double rayon_sep=40;
    private double rayon_cohesion=100;
    private double rayon_alignement = 50;
    public double avoid_parameter=1; 
    public double alignment_parameter=1;  
    public double cohesion_parameter=0.0005;
    private double wind_parameter=0.1;
    private double theta=0;


    public Boids(double x,double y){
        this.x=x;
        this.y=y;
        Random random = new Random();
        this.angle = random.nextDouble(-2*Math.PI, 2*Math.PI);
        this.vx = 2 * Math.cos(this.angle);
        this.vy = 2 * Math.sin(this.angle);
        // Créer une forme triangulaire pour représenter le boid
        this.shape = new Polygon();
        this.shape.getPoints().addAll(
            0.0, -10.0,  // Pointe avant
            -5.0, 5.0,   // Coin gauche
            0.0,0.0,
            5.0, 5.0     // Coin droit
        );
        this.shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        this.shape.setTranslateX(this.x);
        this.shape.setTranslateY(this.y);

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
        double average_dx=0;
        double average_dy=0;
        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance=Math.sqrt(dx*dx+dy*dy);
                if (distance<rayon_sep){
                    // somme pour i dans l'ensemble de séparation du vecteur PPi selon x et selon y
                    average_dx += dx;
                    average_dy += dy;
                }
            }
        }
        Fsx=average_dx;
        Fsy=average_dy;
  
        /*vx += avoid_parameter*Fsx;
        vy += avoid_parameter*Fsy;*/

        //alignement
        double compteur_al = 1;
        double Fax=0; //force alignement selon x
        double Fay=0; //force alignement selon y
        double average_vx=0; //vitesse moyenne selon x
        double average_vy=0; //vitesse moyenne selon y

        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance = Math.sqrt(dx*dx+dy*dy);
                if (rayon_sep<distance && distance< rayon_alignement){
                    compteur_al += 1;
                    average_vx+=boid.getvx();
                    average_vy+=boid.getvy();
            }
        }
    }
        Fax=(average_vx)/compteur_al;
        Fay=(average_vy)/compteur_al;

        //vx+=alignment_parameter*Fax;
        //vy+=alignment_parameter*Fay;
        
        //cohésion
        double compteur = 1;
        double Fcx=0;
        double Fcy=0;
        double average_x=0;
        double average_y=0;
        for (Boids boid : boids){
            if (this==boid) {continue;}
            else {
                double dx=this.x-boid.getx();
                double dy=this.y-boid.gety();
                double distance = Math.sqrt(dx*dx+dy*dy);
                if (distance<rayon_cohesion){
                    // calcul le barycentre des positions des boids dans le cercle de cohesion selon x et y
                    compteur +=1;
                    average_x+=boid.getx();
                    average_y+=boid.gety();
                }
            }
        }
        Fcx+=(average_x)/compteur-this.x;
        Fcy+=(average_y)/compteur-this.y;
        /*vx+=cohesion_parameter*Fcx;
        vy+=cohesion_parameter*Fcy;*/

    
        double Wx=Math.cos(theta);
        double Wy=Math.sin(theta);     
        
        

        double Fx = avoid_parameter * Fsx + alignment_parameter * Fax + cohesion_parameter * Fcx + wind_parameter*Wx;
        double Fy = avoid_parameter * Fsy + alignment_parameter * Fay + cohesion_parameter * Fcy + wind_parameter*Wy;
        double F = Math.sqrt(Fx*Fx+Fy*Fy);
        double vitesse = Math.sqrt(vx*vx+vy*vy);
        if (F!=0)
        {vx=vitesse*Fx/F;
        vy=vitesse*Fy/F;}
        /*if (vitesse>vitessemax){
            vx=(vx/vitesse)*vitessemax;
            vy=(vy/vitesse)*vitessemax;
        }*/
        x=x+vx;
        y=y+vy;
        if (x<-600) {x+=longueur ;}
        if (x>600){x=x-longueur;}
        if (y<-300){y+=largeur ;}
        if (y>300){y=y-largeur;}
        // Mettre à jour la position et la rotation de la forme
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        double angle = Math.toDegrees(Math.atan2(vy, vx)) +90; // +90 pour aligner avec la pointe
        shape.setRotate(angle);
    }

}
