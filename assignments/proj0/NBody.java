/** 
 * draws an animation of bodies floating around in space tugging on each other with the power of gravity.
 * NBody is a class that will actually run your simulation. 
 * This class will have NO constructor. 
 * The goal of this class is to simulate a universe specified in one of the data files.
 */



public class NBody {

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] Planets = readPlanets(filename);
        String background = imgfolder + "starfield.jpg";

        /** Draw the Background. */
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, background);

        /** Draw Plantes to the background. */
        for (Planet planet : Planets) {
            StdDraw.picture(planet.xxPos, planet.yyPos, imgfolder + planet.imgFileName);
        }
        
        StdDraw.enableDoubleBuffering();

        double time = 0;
        while (time < T) {
            /** Update the Plants */
            double[] xForce = new double[Planets.length];
            double[] yForce = new double[Planets.length];
            for (int i = 0; i < xForce.length; i++) {
                xForce[i] = Planets[i].calcNetForceExertedByX(Planets);
                yForce[i] = Planets[i].calcNetForceExertedByY(Planets);
                Planets[i].update(dt, xForce[i], yForce[i]);
            }

            /** Draw picture after update the Planets */
            StdDraw.picture(0, 0, background);
            for (Planet planet : Planets) {
                StdDraw.picture(planet.xxPos, planet.yyPos, imgfolder + planet.imgFileName);
            }

            StdDraw.show();
            StdDraw.pause(10);

            time += dt;
        }




        /* Shows the drawing to the screen, and waits 2000 milliseconds. */
        StdDraw.show();
        StdDraw.pause(2000);

        StdOut.printf("%d\n", Planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < Planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          Planets[i].xxPos, Planets[i].yyPos, Planets[i].xxVel,
                          Planets[i].yyVel, Planets[i].mass, Planets[i].imgFileName);   
        }

    }

    public static String imgfolder = "./images/";
    
    /** Get the radius of the universe in the file */
    public static double readRadius(String filename){
        In in = new In(filename);
        int unused = in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    /** Read in a planet */
    public static Planet readPlanet(In in){
        return new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
    }

    /** Get an array of Planets corresponding to the planets in the file */
    public static Planet[] readPlanets(String filename){
        In in = new In(filename);
        Planet[] Planets = new Planet[in.readInt()];
        double radius = in.readDouble();
        for (int i = 0; i < Planets.length; i++) {
            Planets[i] = readPlanet(in);
        }
        return Planets;
    }
    
}
