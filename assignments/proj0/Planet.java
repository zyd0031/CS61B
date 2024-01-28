import java.util.regex.Matcher;

public class Planet {
    public static final double G = 6.67e-11; 
    /** Its current x position */
    public double xxPos;

    /** Its current y position */
    public double yyPos;

    /** Its current velocity in the x direction */
    public double xxVel;

    /** Its current velocity in the y direction */
    public double yyVel;

    /** Its mass */
    public double mass;

    /** The name of the file that corresponds to the image that depicts the planet */
    public String imgFileName;

    /** Planet constructor */
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    /** Create a copy of the Plant object P */
    public Planet(Planet P){
        this(P.xxPos, P.yyPos, P.xxVel, P.yyVel, P.mass, P.imgFileName);
    }

    /** 
     * Calculate the distance from the Planet P.
     */
    public double calcDistance(Planet P){
        double dx = this.xxPos - P.xxPos;
        double dy = this.yyPos - P.yyPos;
        double rsquared = dx * dx + dy * dy;
        return Math.sqrt(rsquared);

    }
    /**
     * takes in a planet, and returns a double describing the force exerted on this planet by the given planet.
     */
    public double calcForceExertedBy(Planet P){
        double r = this.calcDistance(P);
        double rsquared = r * r;
        double force = G * this.mass * P.mass / rsquared;
        return force;
    }
    /** describe the force exerted in the X directions */
    public double calcForceExertedByX(Planet P){
        double dx = P.xxPos - this.xxPos;
        return calcForceExertedBy(P) * dx / calcDistance(P);
    }

    /** describe the force exerted in the Y directions */
    public double calcForceExertedByY(Planet P){
        double dy = P.yyPos - this.yyPos;
        return calcForceExertedBy(P) * dy / calcDistance(P);
    }

    /**
     * take in an array of Planets and calculate the net X force exerted by all planets in that array upon the current Planet.
     * @param P an array of Planets
     * @return The net X force exerted by all planets in the input array
     */
    public double calcNetForceExertedByX(Planet[] P){
        double netx = 0;
        for (Planet planet : P) {
            if (this.equals(planet)){
                continue;
            }
            netx += calcForceExertedByX(planet);
        }
        return netx;
    }

    /**
     * take in an array of Planets and calculate the net Y force exerted by all planets in that array upon the current Planet.
     * @param P an array of Planets
     * @return The net Y force exerted by all planets in the input array
     */
    public double calcNetForceExertedByY(Planet[] P){
        double nety = 0;
        for (Planet planet : P) {
            if (this.equals(planet)){
                continue;
            }
            nety += calcForceExertedByY(planet);
        }
        return nety;
    }

    /**
     * A method  determines how much the forces exerted on the planet will cause that planet to accelerate, 
     * and the resulting change in the planet’s velocity and position in a small period of time dt.
     * @param dt time period
     * @param xxforce x-force
     * @param yyforce y-force
     */
    public void update(double dt, double xxforce, double yyforce){
        double ax = xxforce / mass;
        double ay = yyforce / mass;
        xxVel += dt * ax;
        yyVel += dt * ay;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }




}
