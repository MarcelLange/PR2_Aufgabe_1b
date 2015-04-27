package porsche911gt2rs;

import jgame.*;
import jgame.JGColor;
import jgame.JGPoint;
import jgame.platform.JGEngine;

public class Simulation extends JGEngine {

    private double lastFrameTime;
    private Porsche911GT2RS porsche;
    
        // Tilde-State
    private double tilde = 0.0;
    
    public Simulation() {
        super();
        this.initEngineApplet();
    }

    public Simulation(JGPoint window_size) {
        super();
        this.initEngine(window_size.x, window_size.y);
    }

    @Override
    public void initCanvas() {
        this.setCanvasSettings(1000, 580, 1, 1, JGColor.black, JGColor.white, null);
    }
    
     @Override
    public void initGame() {
        
        // Framerate festlegen
        setFrameRate(35, 2);
        
        // Media Table Laden
        defineMedia("media.tbl");
        
        // Background
        JGObject road = new JGObject("Background", true, 0, 0, 1, "background");
        
        
        // Porsche erstellen und initialisieren
        porsche = new Porsche911GT2RS(1445.0,456.0,330.0);
        porsche.reset();
        //JGObject car = new JGObject("porsche", true, 0, 0, 2, "porsche");
        
        setGameState("Wait");
    }
    
        public void doFrameWait() {
        if (getKey(KeyEnter) || getKey(' ')) {
            setGameState("Simulation");
        }
    }
        
    public void paintFrameWait() {
        drawString("Car simulation", pfWidth() / 2, 140, 0, new JGFont("Arial", 1, 55), JGColor.white); // / (position) , new JFront(schriftart,besonderheit(zb Kursiv),größe), JGColor.Farbe
        drawString("Press 'ENTER' to start.", pfWidth() / 2, 250, 0, new JGFont("Arial", 1, 25), JGColor.white);
    }    
    
    public void doFrameSimulation() {
        
        // Zeitdifferenz berechnen
        double brakelevel, level, now = System.currentTimeMillis() / 1000.0;
        double diff = (lastFrameTime == 0) ? lastFrameTime : (now - lastFrameTime);
        
        // Reset wenn -R- gedrückt
        if (getKey(82)) {porsche.reset();}
        
        
        // Untergrund ändern?
        if (getKey(81)) {porsche.setGround(1.0);}
        else if (getKey(87)) {porsche.setGround(0.7);}
        else if (getKey(69)) {porsche.setGround(0.1);}
        
        // Hebelstände berechnen
        //throttle.step(diff, getKey(KeyUp));
        //brake.step(diff, getKey(KeyShift));
        
                // Sind wir abgeflogen? Steuerung sperren
        if (tilde == 0.0) {
            level = 1.0;
            brakelevel = 0.0;
            //level = throttle.getLevel();
            //brakelevel = brake.getLevel();
        } else {
            level = 0.0;
            brakelevel = 0.0;
        }
        
        // Berechnung des Autos aktualisieren
        porsche.step(diff , 1.0);
        
        // Zeitstempel auf aktuelle Systemzeit setzen
        lastFrameTime = now;
        
        
    }
    
    
    
    public void paintFrameSimulation() {
         
         // Farb-Konstanten
         final JGColor c_green = new JGColor(0,8*16+5,0);
         final JGColor c_red = new JGColor(10*16+6,12,0);
         
         final JGColor c_wet = new JGColor(0x42,0x84,0xd3);
         final JGColor c_dry = new JGColor(0x23,0xd2,0x2e);
         final JGColor c_icy = new JGColor(0xaa,0xd7,0xff);
         
         // Offset für HUD (position)
         int xoff = 100, yoff = 20;
         
         drawString(("Level: "), xoff + 520, yoff + 0, 1, new JGFont("Arial", JGFont.BOLD,10), JGColor.yellow);
         drawLine( xoff + 500, yoff + 20, xoff + 700, yoff + 20, 13, JGColor.white);
         
         
         // Brake
         drawString(("Brake: "), xoff + 520, yoff + 40, 1, new JGFont("Arial", JGFont.BOLD,10), JGColor.yellow);
         drawLine( xoff + 500, yoff + 60, xoff + 700, yoff + 60, 13, JGColor.white);

          
         // Steuerungsinfo
         drawString(("Gas: Up // Bremse: Shift // ABS 1(an)/2(aus)  // ASR 3(an)/4(aus)  //  "), 170, 560, 0, new JGFont("Arial", JGFont.BOLD,10), JGColor.yellow);
         
         
         // ABS Status
         int xoff2 = 900, yoff2 = 560;
         
         drawLine( xoff2 - 57 , yoff2, xoff2 - 46, yoff2, 15, ((porsche.getGround() == 1.0) ? c_dry : ((porsche.getGround() == 0.7) ? c_wet : c_icy)));
         drawString("Road: " + ((porsche.getGround() == 1.0) ? "DRY" : ((porsche.getGround() == 0.7) ? "WET" : "ICY")), xoff2 - 95, yoff2 - 4, -1, new JGFont("Arial", JGFont.BOLD,11), JGColor.black);
         
         drawString("ABS:", xoff2 - 22, yoff2 - 4, 0, new JGFont("Arial", JGFont.BOLD,11), JGColor.black);
         
    }   
    
}