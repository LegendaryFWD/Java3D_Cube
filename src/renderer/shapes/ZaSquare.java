package src.renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;

public class ZaSquare {
    private ZaPolygon[] polygons;
    private Color color;

    public ZaSquare(Color color, ZaPolygon... polygons){
        this.color = color;
        this.polygons = polygons;
        this.SetPolygonColor();
    }
    
    public ZaSquare(ZaPolygon... polygons){
        this.color = Color.white;
        this.polygons = polygons;
    }
    
    public void render(Graphics g){
        for(ZaPolygon p : this.polygons){
            p.render(g);
        }
    }
    
    public void rotate(boolean dir, double xDegrees, double YDegrees, double ZDegrees){
        for(ZaPolygon p : this.polygons){
            p.rotate(dir, xDegrees, YDegrees, ZDegrees);
        }
        this.sortPolygons();
    }
    
    private void sortPolygons(){
        ZaPolygon.sortPolygons(this.polygons);
    }
    private void SetPolygonColor(){
        for(ZaPolygon p : this.polygons){
            p.setColor(this.color);
        }
    }
}
