package src.renderer;

import java.awt.*;
import javax.swing.JFrame;

import src.renderer.input.ClickType;
import src.renderer.input.Mouse;
import src.renderer.point.PointConverter;
import src.renderer.point.ZaPoint;
import src.renderer.shapes.*;
import java.awt.image.BufferStrategy;

public class Display extends Canvas implements Runnable{
    private static final long serialVersionID = 1L;
    private Thread thread;
    private JFrame frame;
    private static String title = "DDD renderer";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;
    private ZaSquare Square;
    private Mouse mouse;
    
    public Display(){
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
    
        this.mouse = new Mouse();

        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void main(String[] args){
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.start();
        

    }
    
    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "display");
        this.thread.start();
    }
    public synchronized void stop(){
        running = false;
        try{
            this.thread.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ms = 1000000000 / 60;
        double delta = 0;
        int frames = 0;
        
        init();
        
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ms;
            lastTime = now;
            while(delta >= 1)
            {
                update();
                delta--;
                render();
                frames++;
            }
            
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }
        stop();
    }
    
    private void init(){
        int s = 150;
        ZaPoint p1 = new ZaPoint(s/2, -s/2, -s/2);
        ZaPoint p2 = new ZaPoint(s/2, s/2, -s/2);
        ZaPoint p3 = new ZaPoint(s/2, s/2, s/2);
        ZaPoint p4 = new ZaPoint(s/2, -s/2, s/2);
        ZaPoint p5 = new ZaPoint(-s/2, -s/2, -s/2);
        ZaPoint p6 = new ZaPoint(-s/2, s/2, -s/2);
        ZaPoint p7 = new ZaPoint(-s/2, s/2, s/2);
        ZaPoint p8 = new ZaPoint(-s/2, -s/2, s/2);
        this.Square = new ZaSquare(
        new ZaPolygon(Color.blue,p1,p2,p3,p4),
        new ZaPolygon(Color.green,p5, p6, p7, p8),
        new ZaPolygon(Color.pink,p1, p2 ,p6 ,p5),
        new ZaPolygon(Color.red,p1, p5 ,p8 ,p4),
        new ZaPolygon(Color.yellow, p2, p6, p7, p3),
        new ZaPolygon(Color.cyan,p4, p3, p7, p8));  
    
    }
    
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH, HEIGHT);
       
        Square.render(g);
        g.dispose();
        bs.show();

    }
    int InitialX, InitialY;
    ClickType prevMouse = ClickType.Unknown;
    double sense = 2.5;
    boolean rotating = false;
    private void update(){
        
        if (this.mouse.getButton() == ClickType.ScrollClick && rotating){
            rotating = false;
            this.mouse.resetClick();
        }
        if (this.mouse.getButton() == ClickType.ScrollClick && !rotating){
            rotating = true;
            this.mouse.resetClick();
        }
        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if(this.mouse.getButton() == ClickType.LeftClick){
            int xdif = x - InitialX;
            int ydif = y - InitialY;
            this.Square.rotate(true, 0, -ydif / sense, -xdif / sense);
            }
            else if (this.mouse.getButton() == ClickType.RigthClick){
               int xdif = x - InitialX;
                this.Square.rotate(true, xdif/ sense ,0 ,0);
            
            }
            else if(rotating){
                this.Square.rotate(true, 1,0.25,2.5);
            }
            if(this.mouse.isScrollingUp()){
                PointConverter.zoomIn();
            }
            
            else if(this.mouse.isScrollingDown()){
                PointConverter.zoomOut();
            }
            
            this.mouse.resetScroll();
            
            InitialX = x;
            InitialY = y;
        }
        
    }