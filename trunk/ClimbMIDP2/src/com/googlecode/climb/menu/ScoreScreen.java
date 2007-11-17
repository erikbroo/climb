/*
 * ScoreScreen.java
 *
 * Created on 17. Januar 2003, 22:00
 */

package com.googlecode.climb.menu;

import java.util.Vector;
import javax.microedition.rms.*;
import java.io.*;
import javax.microedition.lcdui.*;
import java.util.Calendar;
import java.util.Date;

public class ScoreScreen extends Form implements CommandListener
{
    private MainMenu menu;
    private Command toMain;
    private Vector list;
    private String currentName = null;
    
    public ScoreScreen(MainMenu menu)
    {
        super("Highscore");
        this.menu = menu;
        this.setCommandListener(this);
        this.toMain = new Command("Back", Command.SCREEN, 3);
        this.addCommand(toMain);
        
        this.list = new Vector(6);
        
        try2OpenRS();
    }
    
    private void try2OpenRS()
    {
        try {
            RecordStore rs = RecordStore.openRecordStore("score",false);
            rs.closeRecordStore();
        } catch (RecordStoreNotFoundException exp) 
          {
              //System.out.println("rs was empty");
              initFirstTimeRS();
          }
          catch (RecordStoreException exo) { }
    }
    
    private void initFirstTimeRS()
    {
        //System.out.println("test");
        list.removeAllElements();
        //System.out.println("size of list before first initiation: "+ list.size());
        list.addElement(new ScoreItem("Saban", 5000, 412, System.currentTimeMillis()));
        list.addElement(new ScoreItem("Osman", 2500, 380, System.currentTimeMillis()));
        list.addElement(new ScoreItem("Aslan", 500, 460, System.currentTimeMillis()));
        list.addElement(new ScoreItem("Moruk", 250, 120, System.currentTimeMillis()));
        list.addElement(new ScoreItem("Yabani", 100, 240, System.currentTimeMillis()));
        //System.out.println("size of list before after initiation: "+ list.size());
        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore("score",true);
            rs.closeRecordStore();
            save2RS();
        } catch (RecordStoreException exp) { }
    }
    
    public void initScreen()
    {
        readRS();
        addList2Screen();
    }
    
    public void readRS()
    {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore("score",true);
            byte[] byteA = new byte[100];
            
            list.removeAllElements();
            //System.out.println("size of list before reading out of rs: "+ list.size());
            for (int i=1;i<6;i++) {
                byteA = rs.getRecord(i);
                //System.out.println((char) byteA[0]);
                //System.out.println("1");
                ByteArrayInputStream bytes = new ByteArrayInputStream(byteA);
                //System.out.println("2");
                DataInputStream dataIn = new DataInputStream(bytes);
                //System.out.println("3");
                try {
                    list.addElement(new ScoreItem(dataIn.readUTF(),dataIn.readInt(),dataIn.readInt(),dataIn.readLong()));
                } catch (IOException exp) { }
            }
            //System.out.println("size of list after reading out of rs: "+ list.size());
            rs.closeRecordStore();
        } catch (RecordStoreException exp) {System.out.println("fehler: "+ exp.toString()); exp.printStackTrace();}
    }

    private void save2RS()
    {
        RecordStore rs = null;
        try {
            RecordStore.deleteRecordStore("score");
            rs = RecordStore.openRecordStore("score",true);
            
            try {
                for (int i=0;i<5;i++) {
                    ByteArrayOutputStream bytes;
                    DataOutputStream dataOut = new DataOutputStream(bytes = new ByteArrayOutputStream());
                    ScoreItem item = (ScoreItem) list.elementAt(i);
                    dataOut.writeUTF(item.name);
                    dataOut.writeInt(item.points);
                    dataOut.writeInt(item.level);
                    dataOut.writeLong(item.date);
                    byte[] byteA = bytes.toByteArray();
                    rs.addRecord(byteA,0,byteA.length);
                } 
            } catch (IOException exp) {RecordStore.deleteRecordStore("score");System.out.println("XXXXXXXXXXXXXXXXXXXXXX");}
            //System.out.println("size of rs after saving: "+ rs.getNumRecords());
            rs.closeRecordStore();
        } catch (RecordStoreException exp) {System.out.println("fehler: "+ exp.toString()); exp.printStackTrace();}
    }
    
    public void addList2Screen()
    {
        while (true) {
            try {
                this.delete(0);
            } catch (Exception exp) {break;}
        }
        System.out.println("aaa");
        for (int i=0;i<5;i++) {
            StringItem newItem;
            ScoreItem scoreItem;
            Calendar calendar;
            
            scoreItem = (ScoreItem) list.elementAt(i);
            calendar = Calendar.getInstance();
            calendar.setTime(new Date(scoreItem.date));
            String wochenTag = null;
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY :
                    wochenTag = "Monday";
                    break;
                case Calendar.TUESDAY :
                    wochenTag = "Tuesday";
                    break;
                case Calendar.WEDNESDAY:
                    wochenTag = "Wednesday";
                    break;
                case Calendar.THURSDAY :
                    wochenTag = "Thurday";
                    break;
                case Calendar.FRIDAY :
                    wochenTag = "Friday";
                    break;
                case Calendar.SATURDAY :
                    wochenTag = "Saturday";
                    break;
                case Calendar.SUNDAY :
                    wochenTag = "Sunday";
                    break;
            }
            
            String monat = null;
            switch (calendar.get(Calendar.MONTH)) {
                case Calendar.JANUARY :
                    monat = "Jan.";
                    break;
                case Calendar.FEBRUARY :
                    monat = "Feb.";
                    break;
                case Calendar.MARCH:
                    monat = "Mar.";
                    break;
                case Calendar.APRIL :
                    monat = "Apr.";
                    break;
                case Calendar.MAY :
                    monat = "May";
                    break;
                case Calendar.JUNE :
                    monat = "Jun.";
                    break;
                case Calendar.JULY :
                    monat = "Jul,";
                    break;
                case Calendar.AUGUST :
                    monat = "Aug.";
                    break;
                case Calendar.SEPTEMBER :
                    monat = "Sept.";
                    break;
                case Calendar.OCTOBER :
                    monat = "Oct.";
                    break;
                case Calendar.NOVEMBER :
                    monat = "Nov.";
                    break;
                case Calendar.DECEMBER :
                    monat = "Dec.";
                    break;
            }
            
            newItem = new StringItem(""+ (i+1) +": "+ scoreItem.name +"\n","Scored "+ scoreItem.points + " points\non platform "+ scoreItem.level + " on\n"+ wochenTag + " the "+ calendar.get(Calendar.DAY_OF_MONTH) +"."+ monat + calendar.get(Calendar.YEAR) +"\n");
            this.append(newItem);
            this.append(new StringItem(" ",""));
        }
    }
    
    
    
    public boolean check4newHighScore(int points, int levels)
    {
        boolean n = false;
        readRS();
        for (int i=0;i<5;i++) {
            ScoreItem item = (ScoreItem) list.elementAt(i);
            if (points > item.points) {
                if (currentName == null) {
                    menu.show(new Alert(null,"Neuer Score",null,AlertType.INFO),new NameInputField(points,levels,i+1));
                    n = true;
                }
                else {
                    list.insertElementAt(new ScoreItem(currentName,points,levels,System.currentTimeMillis()),i);
                    list.removeElementAt(5);
                    save2RS();
                    currentName = null;
                    //menu.abortGame();
                }
                return n;
            }
        }
        return n;
    }    
    
    public void commandAction(Command comm, Displayable disp)
    {
        if (comm == toMain)
            menu.show(menu);
    }
    
    public void resetScore()
    {
        initFirstTimeRS();
    }
    
    
    
    
    
    private class ScoreItem
    {
        String name;
        int points, level;
        long date;
        
        ScoreItem(String  name, int points, int level, long date)
        {
            this.name = name;
            this.points = points;
            this.level = level;
            this.date = date;
        }
    }
    
    private class NameInputField extends Form implements CommandListener
    {
        private StringItem stringItem;
        private TextField inputItem;
        private Command confirm;
        private Command abort;
        private int points, level;
        
        NameInputField(int points, int level, int place)
        {
            super("Highscore");
            this.points = points;
            this.level = level;
            stringItem = new StringItem("Yipiee", "You are on the highscore place: "+ place +"\n\n\n");
            inputItem = new TextField("Name:",null,20,TextField.ANY);
            this.append(stringItem);
            this.append(inputItem);
            
            this.setCommandListener(this);
            this.confirm = new Command("Confirm", Command.SCREEN, 3);
            this.addCommand(confirm);
            this.abort = new Command("Cancel", Command.SCREEN, 4);
            this.addCommand(abort);
        }
        
        public void commandAction(Command comm, Displayable disp)
        {
            if (comm == confirm) {
                currentName = inputItem.getString();
                if (currentName == null || currentName.equals(""))
                    currentName = "Anonym";
                check4newHighScore(points,level);
                menu.show(menu);
            }
            else if (comm == abort);
                menu.show(menu);
        }
    }
}