/*
 * SplashScreenText.java
 *
 * Created on 21. Dezember 2002, 00:10
 * by Fatih Coskun
 */
package com.googlecode.climb.utils;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.game.GameCanvas;
import com.googlecode.climb.game.GameControl;




/** Diese Klasse repräsentiert einen "Splashscreen",
 * den man zB als Anwendungs- Intro benutzen kann. Die Klasse besitzt
 * einige Methoden, welche die Darstellung des Splashscreens manipulieren.
 * @author <a href="mail:coskun@informatik.uni-muenchen.de">Fatih Coskun</a>
 *
 * @version 1.0
 */
public class SplashScreen extends GameCanvas implements Runnable
{
    private Display display;
    private Displayable nextScreen;
    
    // gegenwärtige Farbintensität des baseText
    private int cI;
    
    // gegenwärtige xyPosition und xLänge des underlines
    //private int x,y,xL;
    
    private int delay;
    private String[] baseText;
    private String current;
    private boolean disposeIt;
    private boolean isDisposed;
   
    /** Konstruiert einen neuen SplashScreen welcher auf dem angegebenem Display
     * angezeigt werden soll.
     * @param display Auf diesem Display wird der SplashScreen angezeigt.
     */    
    public SplashScreen(Display display)
    {
        super(true);
        this.display = display;
        this.cI = 1;
        this.delay = 2000;
        this.current = "";
        super.setFullScreenMode(true);
    }
        
    /** Zeigt den SplashScreen mit eingestellten Optionen an.
     * Der SplashScreen wird {@code delay} Millisekunden lang angezeigt.
     * Danach wird der nächste Screen {@link nextScreen} eingeblendet.
     *
     * Diese Methode erzeugt einen neuen Thread welcher mit Aufruf von <CODE>run</CODE>
     * die Einblendung des SplashScreens regelt.
     * Diese Methode kehrt sofort nach dem Aufruf zurück.
     * @param base Der im SplashScreen anzuzeigende Haupttext.
     * @param next Der nach dem SplashScreen einzublendende Screen
     */    
    public void show(String[] base, Displayable next/*, int splashType*/)
    {
        this.baseText = base;
        this.nextScreen = next;
        //this.prepareOffScreen();
        this.display.setCurrent(this);
        new Thread(this).start();
    }
    
    /*private void prepareOffScreen()
    {
        this.offScreen = Image.createImage(getWidth() + 1, getHeight());
        this.paintOffScreen(offScreen.getGraphics());
    }*/
    
    /** Setzt den {@link delay}
     * @param delay Der zu setzende delay
     */    
    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    /** Regelt die Einblendung des SplashScreens. */    
    public void run() 
    {
        for (int i=0; i<baseText.length;i++) {
            current = baseText[i];
            while (true) {
                long start = System.currentTimeMillis();
                
                if (i==baseText.length-1) {
                    lineEnd += 5;
                    if (lineEnd >= 100) {
                        lineStart += 5;
                    }
                }
                
                if (disposeIt) {
                    this.dispose();
                    return;
                }
                if (cI >= 250) {
                    break;
                }
                cI += 10;
                this.paintIt();
                while( System.currentTimeMillis() - start < 25 ) {
                    Thread.yield();
                }
            }
        
            this.paintIt();
            try {
               Thread.sleep(delay);
            } catch (InterruptedException exp) {}
        
        
            while (true) {
                long start = System.currentTimeMillis();
                if (disposeIt) {
                    this.dispose();
                    return;
                }
                if (cI <= 1)
                    break;
                cI -= 10;
                this.paintIt();
                while( System.currentTimeMillis() - start < 50 ) {
                    Thread.yield();
                }
            }
        }
        if(!isDisposed) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException exp) {}
            dispose();
        }
    }
    
    private void dispose()
    {
        isDisposed = true;
        display.setCurrent((Displayable)nextScreen);
        this.cleanUp();
    }
    
    private void paintIt()
    {
        //this.paintOffScreen(offScreen.getGraphics());
        this.repaint();
    }
    
    /*private void paintOffScreen(Graphics g)
    {
        g.setColor(1,1,1);
        g.fillRect(0, 0, getWidth() + 1,  getHeight());
        g.setColor(cI, cI, cI);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString(current, getWidth() / 2, getHeight() / 2, g.BASELINE|g.HCENTER);
    }*/
    
    /** ï¿½bernimmt die Aufgabe der Darstellung des SplashScreens.
     * Diese Methode Zeichnet alles benï¿½tigte von einem OffScreenImage ab.
     * @param g Die Graphics Instanz auf der gezeichnet wird.
     */    
    public void paint(Graphics g)
    {
        //g.drawImage(offScreen, 0, 0, g.TOP|g.LEFT);
        g.setColor(1,1,1);
        g.fillRect(0, 0, getWidth() + 1,  getHeight());
        g.setColor(cI, cI, cI);
        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
        g.drawString(current, getWidth() / 2, getHeight() / 2, g.BASELINE|g.HCENTER);
//        g.setColor(0xFFFFFF);
//        g.drawLine(lineStart, getHeight() / 2 + 20, lineEnd, getHeight() / 2 + 20);
    }
    
    int lineEnd = -1;
    int lineStart = -1;
    
    private void cleanUp()
    {
        this.baseText = null;
        this.display = null;
        this.nextScreen = null;
        //this.offScreen = null;
    }
    
    protected void keyPressed(int key)
    {
        this.disposeIt = true;
    }
}