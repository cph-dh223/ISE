package util;

import game.Letter;
import processing.core.*;
import board.Board;
import board.Multiplier;
import board.Tile;

import java.util.ArrayList;
import java.util.List;

public class GUI extends PApplet implements IUI {
    private static GUI instanse;
    private String inputText;
    private final int sizeOfText = 40;
    private PGraphics textBox;
    private PGraphics menuGraphic;
    private PGraphics msgGraphic;
    private PGraphics handGraphic;

    private PImage boardImage;
    private int finalMouseX;
    private int finalMouseY;
    private boolean clicked;
    private int tileSize = -1;


    public GUI(){
        instanse = this;

    }
    public static GUI getInstance(){
        return instanse;
    }

    public void settings(){
        fullScreen();

    }

    public void setup() {
        textBox = createGraphics(width,height);
        noLoop();
        menuGraphic = createGraphics(width/2,height/2);
        msgGraphic = createGraphics(width,height/10);
        handGraphic = createGraphics(width/2, height/5);
    }

    public void draw() {
        background(255);
        fill(0);
        image(textBox,0,0);
        image(menuGraphic,20,50);
        image(msgGraphic,20,(3 * height)/4);
        image(handGraphic,width/2,height - sizeOfText * 2);
        if(boardImage != null){
            image(boardImage,width/2,0);
        }
    }

    @Override
    public void displayMessage(String msg) {
        // Visually show message on screen
        msgGraphic.beginDraw();
        msgGraphic.background(255);
        msgGraphic.fill(0);
        msgGraphic.textSize(sizeOfText);
        msgGraphic.text(msg, 0,50);
        msgGraphic.endDraw();

        redraw();
        // noLoop
    }

    /**
     * This method can display any menu. The options are desplayed in the middle of the screen
     * @param menuFields the list of menu options
     */
    @Override
    public void displayMenu(String[] menuFields) {
        if(msgGraphic.image != null){
            //msgGraphic.clear();
        }
        menuGraphic.beginDraw();
        menuGraphic.background(255);
        menuGraphic.fill(0);
        menuGraphic.textSize(sizeOfText);
        menuGraphic.strokeWeight(15);
        int spasing = sizeOfText + (int)(sizeOfText*0.2);
        for (int i = 0; i < menuFields.length; i++) {
            int posY = (spasing * menuFields.length/2) + (spasing * i) ;
            menuGraphic.text(menuFields[i], 0, posY);
        }
        menuGraphic.endDraw();
        redraw();
    }

    @Override
    public String getInput(String msg) {
        clicked = false;
        String option = "";
        displayMessage(msg);
        while(!clicked) {

            if(keyPressed) {
                option = getInputTextBox();
                break;
            }
            else if(mousePressed) {
                try {
                    option = getMouseInputPlaceLetter();
                } catch (IllegalArgumentException e) {
                    getInput(msg);
                }
            }
            delay(10);
        }
        clicked = false;
        return option;

    }

    private String getInputCoordinates() {
        String coordinates = "mouseX: ";
        while(true) {
            System.out.println("Waiting for input"); // TODO: Some line has to be here for some reason??
            if (mousePressed) {
                coordinates += mouseX + " mouseY: " + mouseY; // Generic method for getting exact input
                System.out.println(coordinates);
                return coordinates;
            }
        }
    }

    // Visual TextBox as a replacement for console
    private String getInputTextBox() {
        inputText = "";
        String tmpText;
        while(true) {
            if (keyPressed) {
                if (keyCode == ENTER) {
                    tmpText = inputText;
                    textBox.beginDraw();
                    textBox.clear();
                    textBox.endDraw();
                    return tmpText;
                }
            }
            displayMessage(""); // TODO: Some line has to be here for some reason??
        }
    }


    private void displayTextBox() {
        textBox.beginDraw();
        textBox.fill(0);
        textBox.textSize(30);
        textBox.text(inputText, width/4, height-60);
        textBox.endDraw();
        redraw();
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
        PGraphics boardGraphic = createGraphics(width/2,width/2);

        tileSize = boardGraphic.width/board.getWidth();
        int strokeWeight = 5;
        boardGraphic.beginDraw();
        boardGraphic.stroke(0);
        boardGraphic.strokeWeight(strokeWeight);
        boardGraphic.fill(255);
        boardGraphic.textSize(tileSize);
        boardGraphic.textAlign(3,3);// Center,Center
        for(int i = 0; i < board.getWidth(); i++){
            for (int j = 0; j < board.getHeight(); j++) {
                Tile currentTile = board.getTile(i, j);
                boardGraphic.fill(255);
                if(currentTile.getMultiplier() == Multiplier.DOUBLE_LETTER){
                    boardGraphic.fill(150,150,255);
                }
                if(currentTile.getMultiplier() == Multiplier.DOUBLE_WORD){
                    boardGraphic.fill(255,150,150);
                }
                if(currentTile.getMultiplier() == Multiplier.TRIPLE_LETTER){
                    boardGraphic.fill(0,0,255);
                }
                if(currentTile.getMultiplier() == Multiplier.TRIPLE_WORD){
                    boardGraphic.fill(255,0,0);
                }
                
                boardGraphic.rect(i * tileSize + strokeWeight/2,j*tileSize + strokeWeight/2,tileSize,tileSize);
                if(board.getTile(i, j).getLetter() != null) {
                    boardGraphic.fill(0);
                    char tileChar = board.getTile(i, j).getLetterChar();
                    boardGraphic.text(tileChar, i * tileSize + tileSize/2 + strokeWeight,j*tileSize + tileSize/2 - strokeWeight);
                    boardGraphic.stroke(0);
                    boardGraphic.fill(255);
                }
            }
        }
        boardGraphic.endDraw();
        boardImage = boardGraphic;
        redraw();
    }

    @Override
    public void keyPressed() {

        if (key >= 'A' && key <= 'z' || key >= '0' && key <= '9') {
            inputText += key;
        }
        displayTextBox();
    }

    public String getMouseInputPlaceLetter() throws IllegalArgumentException{
        int x = -1;
        int y = -1;

        if(clicked) {
            x = (int)map(finalMouseX, width/2, width, 0, 15);
            y = (int)map(finalMouseY, 0, width/2, 0, 15);
            if (x < 0 || x > 14 || y < 0 || y > 14) {
                throw new IllegalArgumentException();
            }
        }
        return ""+x+","+y;
    }

    public void mouseClicked() {
        finalMouseX = mouseX;
        finalMouseY = mouseY;
        clicked = true;
        System.out.println(getMouseInputPlaceLetter());
    }

    public void displayHand(List<Letter> letters) {
        handGraphic.beginDraw();
        handGraphic.background(255);
        handGraphic.textSize(sizeOfText);
        handGraphic.strokeWeight(3);
        handGraphic.textAlign(3,102);//center,bottom
        for (int i = 0; i < letters.size(); i++) {
            char letter = letters.get(i).getLetter();
            handGraphic.fill(255);
            handGraphic.rect(i * sizeOfText + (sizeOfText/2), (sizeOfText / 5), sizeOfText , sizeOfText);
            handGraphic.fill(0);
            handGraphic.text(letter, i * sizeOfText + sizeOfText,50);
        }
        handGraphic.endDraw();
        redraw();

    }
}