package src.renderer.point;

import java.awt.Point;

import src.renderer.Display;

public class PointConverter {
    
    private static double scale = 1;
    private static final double zoom = 1.2;
    
    public static void zoomIn(){
        scale *= zoom;
    }
    
    public static void zoomOut(){
        scale /= zoom;
    }
    public static Point ConvertPoint(ZaPoint point3d){
        double x3d = point3d.y * scale;
        double y3d = point3d.z * scale;
        double depth = point3d.x * scale;
        double[] newValue = scale(x3d, y3d, depth);
        int x2D = (int) (Display.WIDTH / 2 + newValue[0]);
        int y2D = (int) (Display.HEIGHT / 2 - newValue[1]);
        Point point2D = new Point(x2D, y2D);
        return point2D;
    }
    private static double[] scale(double x3D, double y3D, double depth){
        double dist = Math.sqrt(x3D * x3D + y3D * y3D);
        double theta = Math.atan2(y3D, x3D);
        double NewDepth = 15 - depth; 
        double LocalScale = Math.abs(1400/(NewDepth + 1400));
        dist *= LocalScale;
        double[] res = new double[2];
        res[0] = dist * Math.cos(theta);
        res[1] = dist * Math.sin(theta);
        return res;
    }
    public static void rotateAxisX(ZaPoint p, boolean dir, double degrees){
        double radius = Math.sqrt(p.y * p.y + p.z * p.z);
        double theta = Math.atan2(p.z, p.y);
        theta += 2 * Math.PI/360 * degrees * (dir ? -1 : 1);
        p.y = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }
    public static void rotateAxisY(ZaPoint p, boolean dir, double degrees){
        double radius = Math.sqrt(p.x * p.x + p.z * p.z);
        double theta = Math.atan2(p.x, p.z);
        theta += 2 * Math.PI/360 * degrees * (dir ? -1 : 1);
        p.x = radius * Math.sin(theta);
        p.z = radius * Math.cos(theta);
    }
    public static void rotateAxisZ(ZaPoint p, boolean dir, double degrees){
        double radius = Math.sqrt(p.y * p.y + p.x * p.x);
        double theta = Math.atan2(p.y, p.x);
        theta += 2 * Math.PI/360 * degrees * (dir ? -1 : 1);
        p.x = radius * Math.cos(theta);
        p.y = radius * Math.sin(theta);
    }
}
