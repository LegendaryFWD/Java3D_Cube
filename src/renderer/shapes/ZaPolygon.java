package src.renderer.shapes;

import src.renderer.point.PointConverter;
import src.renderer.point.ZaPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Point;

public class ZaPolygon {
    
    private ZaPoint[] points;
    private Color color;
    
    public ZaPolygon(Color color, ZaPoint... points){
        this.color = color;
        this.points = new ZaPoint[points.length];
        for(int i = 0; i < points.length; i++){
            ZaPoint p = points[i];
            this.points[i] = new ZaPoint(p.x, p.y, p.z);
        }
    }
    public ZaPolygon(ZaPoint... points){
        this.color = Color.white;
        this.points = new ZaPoint[points.length];
        for(int i = 0; i < points.length; i++){
            ZaPoint p = points[i];
            this.points[i] = new ZaPoint(p.x, p.y, p.z);
        }
    }
    public void render(Graphics g){
        Polygon poly = new Polygon();
        for(int i = 0; i < this.points.length; i++){
            Point p = PointConverter.ConvertPoint(this.points[i]);
            poly.addPoint(p.x, p.y);
            
        }        
        g.setColor(color);
        g.fillPolygon(poly);
    }
    
    public void rotate(boolean dir, double xDegrees, double yDegrees, double zDegrees){
        for(ZaPoint p : this.points){
            PointConverter.rotateAxisX(p, dir, xDegrees);
            PointConverter.rotateAxisY(p, dir, yDegrees);
            PointConverter.rotateAxisZ(p, dir, zDegrees);
        }
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    public double GetAverageDepth(){
        double sum = 0;
        for(ZaPoint p : this.points){
            sum += p.x;
        }
        return sum / this.points.length;
    }

    public static ZaPolygon[] sortPolygons(ZaPolygon[] polygons){
        List<ZaPolygon> polygonList = new ArrayList<ZaPolygon>();
        for(ZaPolygon p : polygons){
            polygonList.add(p);
        }
        Collections.sort(polygonList, new Comparator<ZaPolygon>(){
        @Override
        public int compare(ZaPolygon p1, ZaPolygon p2){
            return p2.GetAverageDepth() - p1.GetAverageDepth() < 0 ? 1 : -1;
        }
    });
    for(int i = 0; i != polygons.length; i++){
        polygons[i] = polygonList.get(i);
    }
    return polygons;
    }
}
