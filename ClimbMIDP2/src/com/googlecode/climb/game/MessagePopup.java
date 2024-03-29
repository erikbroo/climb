package com.googlecode.climb.game;

import java.util.Vector;
import javax.microedition.lcdui.*;

final class MessagePopup
{
    private final static int BOX_WIDTH = 116;
    private final static int BOX_HEIGHT = 20;
    private final static int BOX_XPOS = (World.WORLD_VIEW_WIDTH - BOX_WIDTH) / 2;
    private final static int BOX_YPOS = 50;
    
    private final Vector messageQueue = new Vector(10);
    private long showedTime;
    private short showedCount;
    
    private int pointAdds = -1;
    private int pointAddsAt = 0;
    
    private String comboString;
    private long comboShowedTime;
    private int comboX;
    private int comboY;
    
    private final GameControl game;
    private final World world;
    private final Spot spot;
    
    
    private final static int[] ALPHA_BOX;
    
    static {
        ALPHA_BOX= new int[BOX_WIDTH * BOX_HEIGHT];
        for (int i = 0; i < ALPHA_BOX.length; i++) {
            ALPHA_BOX[i] = 0x99222222;
        }
    }
    
    
    MessagePopup(GameControl game)
    {
        this.game = game;
        this.world = this.game.world;
        this.spot = this.world.spot;
    }
    
    
    final void registerPointAdds(int adds)
    {
        this.pointAdds = adds;
        this.pointAddsAt = 0;
    }
    
    
    final void registerComboString(String msg)
    {
        this.comboString = msg;
        this.comboY = this.spot.getYPosition();
        this.comboX = this.spot.getXPosition();
        this.comboShowedTime = System.currentTimeMillis();
    }
    
    final void registerMSG(String msg, int color)
    {
        this.messageQueue.addElement(new Message(color,msg));
    }
    
    final void renderTo(Graphics g)
    {
        if (this.pointAdds != -1) {
            g.setColor(20,140,250);
            g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
            g.drawString(this.pointAdds + "",66,27-this.pointAddsAt,Graphics.TOP|Graphics.RIGHT);
            this.pointAddsAt+=2;
            if (27-this.pointAddsAt <= 7) {
                this.pointAdds = -1;
            }
        }
        
        if (this.comboString != null) {
            final int y = this.world.calculateViewPosition(comboY);
            
            g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_SMALL));
            g.setColor(30,80,1);
            g.drawString(this.comboString, comboX+2, y+12, Graphics.TOP|Graphics.HCENTER);
            g.drawString(this.comboString, comboX-2, y+12, Graphics.TOP|Graphics.HCENTER);
            g.drawString(this.comboString, comboX+2, y+8, Graphics.TOP|Graphics.HCENTER);
            g.drawString(this.comboString, comboX-2, y+8, Graphics.TOP|Graphics.HCENTER);
            g.setColor(250,180,40);
            g.drawString(this.comboString, comboX, y+10, Graphics.TOP|Graphics.HCENTER);
            if (System.currentTimeMillis()-this.comboShowedTime >= 500) {
                this.comboString = null;
            }
        }
        
        
        if (this.messageQueue.isEmpty()) {
            return;
        }
        if (this.showedCount == 5) {
            this.showedCount = 0;
            this.messageQueue.removeElementAt(0);
            return;
        }
        
        if (this.showedTime == 0L) {
            this.showedTime = System.currentTimeMillis();
        }
        g.drawRGB(ALPHA_BOX, 0, BOX_WIDTH, BOX_XPOS, BOX_YPOS, BOX_WIDTH, BOX_HEIGHT, true);
        g.setColor(0xFFFFFF);
        g.drawRect(30, 50, 116, 20);
        if (this.showedCount % 2 != 0) {
            if ((int)(System.currentTimeMillis() - this.showedTime) > 70) {
                this.showedTime = 0;
                this.showedCount += 1;
            }
            else {
                return;
            }
        }
        g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
        String s = ((Message)this.messageQueue.firstElement()).message;
        int c = ((Message)this.messageQueue.firstElement()).color;
        g.setColor(0x400505);
        g.drawString(s,87,52,Graphics.HCENTER|Graphics.TOP);
        g.setColor(c);
        g.drawString(s,88,53,Graphics.HCENTER|Graphics.TOP);
        if ((int)(System.currentTimeMillis() - this.showedTime) > 1000) {
            this.showedTime = 0;
            this.showedCount += 1;
        }
    }
        
    private class Message
    {
        int color;
        String message;
        
        Message(int color, String s)
        {
            this.color = color;
            message = s;
        }
    }
}