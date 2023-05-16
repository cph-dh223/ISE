package util;

import processing.core.PApplet;
import board.Board;
import processing.core.PGraphics;
import processing.core.PShape;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class GUI extends PApplet implements IUI {

    private final int width;
    private final int height;
    private PGraphics pg;
    private static GUI instanse;
    private final int sizeOfText = 50;


    public GUI(){
        instanse = this;
        width = 800;
        height = 800;
    }
    public static GUI getInstance(){
        return instanse;
    }

    public void settings(){
        size(width,height);
    }

    public void setup() {
        noLoop();
        pg = createGraphics(width,height);
    }

    public void draw() {
        background(255);
        fill(0);
        image(pg,0,0);
    }
    
    @Override
    public void displayMessage(String msg) {
        // Visually show message on screen
        pg.beginDraw();
        pg.fill(0);
        pg.text(msg,100,100);
        pg.endDraw();

        redraw();
        // noLoop
    }

    /**
     * This method can display any menu. The options are desplayed in the middle of the screen
     * @param menuFields the list of menu options
     */
    @Override
    public void displayMenu(String[] menuFields) {
        pg.beginDraw();
        pg.background(255);
        pg.fill(0);
        pg.textSize(sizeOfText);
        pg.strokeWeight(15);
        int posX = (width/2) - (int)textWidth(menuFields[0])*2;
        int spasing = sizeOfText + (int)(sizeOfText*0.2);
        for (int i = 0; i < menuFields.length; i++) {
            int posY = (height/2) - (spasing * menuFields.length/2) + (spasing * i) ;
            pg.text(menuFields[i], posX, posY);
        }
        pg.endDraw();
        redraw();
    }

    @Override
    public String getInput(String msg) {
        // Returns key pressed as a String(1, 2, 3 or other...)

        System.out.println(msg);
        String option = String.valueOf(getInputMainMenu());
        return option;

        /*
        // returns String with coordinates
        String coordinates = "mouseX: ";
        while(true) {
            System.out.println("Waiting for input"); // TODO: Some line has to be here for some reason??
            if(mousePressed) {
                coordinates += mouseX + " mouseY: " + mouseY; // Generic method for getting exact input
                System.out.println(coordinates);
                return coordinates;
            }
        }

         */
    }

    private char getInputMainMenu() {
        while (true) {
            System.out.println("Waiting for input"); // TODO: Some line has to be here for some reason??
            if(keyPressed){
                return key;
            }
        }
    }

    @Override
    public void displayBoard(Board board) {

    }
}
