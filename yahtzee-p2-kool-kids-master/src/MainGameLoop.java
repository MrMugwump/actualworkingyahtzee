import EntitiesAndModels.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Color;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;

public class MainGameLoop {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
    };

    private int[] indices = {
            0,1,3,
            3,1,2
    };

    private Entity[] dice = new Entity[5];
    private boolean[] held = new boolean[] {false, false,false,false,false,false };

    public static void main(String[] args) throws LWJGLException {
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        Display.create();
        Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));

        GL11.glViewport(0,0,WIDTH,HEIGHT);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glViewport(0,0,WIDTH,HEIGHT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        new MainGameLoop();

    }

    private MainGameLoop(){
        mainLoop();
    }

    private void mainLoop(){
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };


        RawModel model = loader.loadToVao(vertices, textureCoords, indices);

        //MousePicker mouse = new MousePicker();
        //Yes, x is redundant, I dont feel like editing
        float x = 0;
        float y = (float)(-1/3);
        float scale = 0.25f;

        for (int i = 0; i < 5; i++) {
            if (i < 3)
                x = (float)(-0.86/(3))*(i+1);
            else
                x = (float)(0.86/3)*(i-2);
            ModelTexture texture = new ModelTexture(loader.loadTexture(""+(i+1)));
            TexturedModel texturedModel = new TexturedModel(model,texture);
            dice[i] = new Entity(texturedModel, new Vector3f(x,y,0), 0,0,scale);
        }
        ModelTexture texture = new ModelTexture(loader.loadTexture("Start"));
        TexturedModel texturedModel = new TexturedModel(model,texture);
        Entity rollButton = new Entity(texturedModel, new Vector3f(0,0,0), 0,0, scale);
        //dice[0].setPosition(new Vector3f(0.86f,0,0));
        //Entity die = new Entity(texturedModel, new Vector3f(x,y,0), 0,0,scale);

        texture = new ModelTexture(loader.loadTexture("10"));
        texturedModel = new TexturedModel(model,texture);
        Entity leftScore = new Entity(texturedModel, new Vector3f(-0.86f,0.86f,0), 0,0, scale);

        texture = new ModelTexture(loader.loadTexture("10"));
        texturedModel = new TexturedModel(model,texture);
        Entity middleScore = new Entity(texturedModel, new Vector3f((float)-0.57,0.86f,0), 0,0, scale);

        texture = new ModelTexture(loader.loadTexture("10"));
        texturedModel = new TexturedModel(model,texture);
        Entity rightScore = new Entity(texturedModel, new Vector3f((float)(-0.86/(3))*(1),0.86f,0), 0,0, scale);

        texture = new ModelTexture(loader.loadTexture("EndTurn"));
        texturedModel = new TexturedModel(model,texture);
        Entity endTurn = new Entity(texturedModel, new Vector3f(0,(float)(-0.86/(3))*(2),0), 0,0, scale);



        ArrayList<Entity> checkBoxes = new ArrayList<>();
        ArrayList<Integer> indexForChosenOption = new ArrayList<>();

        //int[] possibilities;

        Gameplay gameplay = new Gameplay();
        gameplay.setUp();
        for (int i = 0; i < 5; i++) {
            //System.out.println(gameplay.setter()[i]);
            texture = new ModelTexture(loader.loadTexture(""+(gameplay.setter()[i])));
            dice[i].setModel(new TexturedModel(model,texture));
        }
        int score = 0;

        int turn = 0;

        init();
        boolean pickScore = false;
        int[] possibilities = new int[]{0};
        boolean isButtonHeld = false;
        boolean isChoosen = false;

        boolean[] excludes;

        int chosenOption = -1;
        while (true) {
            if (!pickScore) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                init();

                render(score);

                shader.start();
                //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                //render();

                if (Mouse.isButtonDown(0)) {

                    for (int i = 0; i < 5; i++) {

                        if (detectIfClicked(dice[i])) {
                            //System.out.println("YAy");

                            dice[i].move();
                            if(held[i]){
                                held[i] = false;
                                System.out.println("Not Held");
                            }
                            else{
                                held[i] = true;
                                System.out.println("held");
                            }
                            gameplay.setReroll(held);
                            //System.out.println(held[i]);
                        }

                    }
                    if (detectIfClicked(rollButton) && !isButtonHeld) {
                    /*for ( boolean bool: held
                         ) {
                        //System.out.println(bool);
                    }*/
                        gameplay.setReroll(held);
                        gameplay.reroller();
                        int[] diceNum = gameplay.setter();

                        for (int i = 0; i < 5; i++) {
                            //System.out.println(gameplay.setter()[i]);
                            if (!held[i]) {
                                texture = new ModelTexture(loader.loadTexture("" + (diceNum[i])));
                                dice[i].setModel(new TexturedModel(model, texture));
                            }
                        }
                        /*
                        while (Mouse.isButtonDown(0)) {

                        }*/
                        isButtonHeld = true;
                        turn++;
                        //System.out.println("Yay");
                    }

                    if (detectIfClicked(endTurn)) {
                        turn = 3;
                    }
                }
                else {
                    isButtonHeld = false;
                }
                for (int i = 0; i < 5; i++) {
                    renderer.render(dice[i], shader);
                }
                renderer.render(rollButton, shader);
                renderer.render(endTurn, shader);

                //System.out.println(asdf.substring(1, 2));
                /*
                if (score > 99) {

                    texture = new ModelTexture(loader.loadTexture("1" + (int) (Math.floor(score / 100))));
                    leftScore.setModel(new TexturedModel(model, texture));
                    renderer.render(leftScore, shader);

                    //int placeholder = (int) (Math.floor(score / 10) - Math.floor(Math.floor(score / 10) / 10) * 10);
                    texture = new ModelTexture(loader.loadTexture("1" + (asdf.substring(1, 2))));
                    //System.out.println("1" + (asdf.substring(2, 3)));
                    middleScore.setModel(new TexturedModel(model, texture));
                    renderer.render(middleScore, shader);


                    texture = new ModelTexture(loader.loadTexture("1" + (asdf.substring(2, 3))));
                    rightScore.setModel(new TexturedModel(model, texture));
                    renderer.render(rightScore, shader);
                } else if (score > 9) {
                    texture = new ModelTexture(loader.loadTexture("1" + (asdf.substring(0, 1))));
                    middleScore.setModel(new TexturedModel(model, texture));
                    renderer.render(middleScore, shader);
                    //System.out.println("sup");
                    texture = new ModelTexture(loader.loadTexture("1" + (Integer.parseInt(asdf.substring(1, 2)))));
                    rightScore.setModel(new TexturedModel(model, texture));
                    renderer.render(rightScore, shader);
                } else {
                    texture = new ModelTexture(loader.loadTexture("1" + (Integer.parseInt(asdf.substring(0, 1)))));
                    rightScore.setModel(new TexturedModel(model, texture));
                    //renderer.render(rightScore, shader);
                }*/

                shader.stop();/*
            Display.update();
            Display.sync(60);*/

                if (turn == 3) {

                    //score += gameplay.getScore();
                    if (gameplay.endGame()) {
                        System.out.println("Final Score: " + score);
                        break;
                    } else {
                        possibilities = gameplay.getPossibilities();
                        indexForChosenOption = new ArrayList<>();
                        scale = 0.05f;
                        moveAllToBottom();

                        excludes = gameplay.getExcludes();

                        for (int i = 0; i < possibilities.length; i++) {
                            if (!excludes[i]) {
                                texture = new ModelTexture(loader.loadTexture("Unchosen"));
                                texturedModel = new TexturedModel(model, texture);
                                Entity checkBox = new Entity(texturedModel, new Vector3f((float) (0.025 - 1), (float) (0.95 - (i * 0.065)), 0), 0, 0, scale);
                                checkBoxes.add(checkBox);
                                indexForChosenOption.add(i);
                                System.out.println("Index " + i);
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            //System.out.println(gameplay.setter()[i]);
                            if (!held[i]) {
                                texture = new ModelTexture(loader.loadTexture("" + (gameplay.setter()[i])));
                                dice[i].setModel(new TexturedModel(model, texture));
                            }
                        }

                        pickScore = true;
                    }

                    turn = 0;
                    gameplay.setUp();
                }
            }
            else {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                //render();
                renderChooseText(possibilities);
                //System.out.println("Hello");
                shader.start();
                //scale = 0.05f;







                texture = new ModelTexture(loader.loadTexture("endChooseMode"));
                texturedModel = new TexturedModel(model, texture);
                Entity endButton = new Entity(texturedModel, new Vector3f((float)(0.75), 0, 0), 0, 0, 0.25f);

                ModelTexture chosenTexture = new ModelTexture(loader.loadTexture("Unchosen"));

                if (Mouse.isButtonDown(0)){

                    for (int i = 0; i < checkBoxes.size(); i++) {
                        if (detectIfClicked(checkBoxes.get(i))){
                            texture = new ModelTexture(loader.loadTexture("Chosen"));
                            Entity checkBox = checkBoxes.get(i);
                            checkBox.getModel().setTexture(texture);
                            checkBoxes.set(i, checkBox);
                            chosenOption = i;
                            System.out.println("Index: " + indexForChosenOption.get(i) + "Option: " + possibilities[indexForChosenOption.get(i)]);

                            for (int j = 0; j < checkBoxes.size(); j++) {
                                if (!checkBoxes.get(j).getModel().getTexture().equals(chosenTexture)){
                                    if (j != i){
                                        //texture = new ModelTexture(loader.loadTexture("Unchosen"));
                                        //texturedModel = new TexturedModel(model, texture);
                                        checkBox = checkBoxes.get(j);
                                        checkBox.getModel().setTexture(chosenTexture); //You see now that I figured out a better way to reassign textures. Oh well.
                                        checkBoxes.set(j,checkBox);
                                    }
                                }
                            }
                            isButtonHeld =true;
                            isChoosen = true;
                        }
                    }

                    if (detectIfClicked(endButton) && isChoosen){


                        System.out.println("Index: " + indexForChosenOption.get(chosenOption));
                        System.out.println("Option: " + possibilities[indexForChosenOption.get(chosenOption)]);
                        //System.out.println("Score total: "+ );

                        score += possibilities[indexForChosenOption.get(chosenOption)];
                        if (chosenOption != 11){
                            gameplay.excludeOption(indexForChosenOption.get(chosenOption));
                        }
                        pickScore = false;
                        isButtonHeld = true;

                        checkBoxes.clear();

                            //ModelTexture texture = new ModelTexture(loader.loadTexture(""+(i+1)));
                            //TexturedModel texturedModel = new TexturedModel(model,texture);
                        reset();
                        gameplay.setReroll(held);
                        gameplay.reroller();
                        int[] diceNum = gameplay.setter();

                        for (int i = 0; i < 5; i++) {
                            //System.out.println(gameplay.setter()[i]);
                            if (!held[i]) {
                                texture = new ModelTexture(loader.loadTexture("" + (diceNum[i])));
                                dice[i].setModel(new TexturedModel(model, texture));
                            }
                        }

                    }

                }
                else {
                    isButtonHeld = false;
                }



                for (int i = 0; i < checkBoxes.size(); i++) {
                    renderer.render(checkBoxes.get(i), shader);
                }
                for (Entity entity: dice) {
                    renderer.render(entity,shader);
                }
                renderer.render(endButton, shader);
                shader.stop();
            }



            Display.update();
            Display.sync(100);

            if (Display.isCloseRequested()) {

                loader.cleanUp();
                shader.cleanUp();
                Display.destroy();
                System.exit(0);
            }
        }
    }

    private boolean detectIfClicked(Entity entity){
        //Calculates whether or not you are clicking something
        MousePicker mouse = new MousePicker();
        boolean detected = false;
        float[] coordinates = mouse.calculateMouseRay();
        float[] XY = new float[]{entity.getPosition().x, entity.getPosition().y};
        float maximumX = -10;
        float minimumX = 10;
        for (int j = 0; j < vertices.length; j += 3) {
            if (vertices[j] >= maximumX) {
                maximumX = vertices[j];
            }
            if (vertices[j] <= minimumX) {
                minimumX = vertices[j];
            }
        }
        float maximumY = -10;
        float minimumY = 10;
        for (int j = 0; j < vertices.length; j += 3) {
            if (vertices[j] >= maximumY) {
                maximumY = vertices[j];
            }
            if (vertices[j] <= minimumY) {
                minimumY = vertices[j];
            }
        }
        maximumX = maximumX * entity.getScale();
        maximumY = maximumY * entity.getScale();
        minimumX = minimumX * entity.getScale();
        minimumY = minimumY * entity.getScale();
        maximumX += XY[0];
        minimumX += XY[0];
        maximumY += XY[1];
        minimumY += XY[1];
        if (coordinates[0] >= minimumX) {
            if (coordinates[0] <= maximumX) {
                if (coordinates[1] >= minimumY) {
                    if (coordinates[1] <= maximumY) {
                        detected = true;
                    }
                }
            }
        }
        return detected;
    }

    //copied
    private TrueTypeFont font;
    //TrueTypeFont font2;

    private void init() {
        // load a default java font
        Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
        font = new TrueTypeFont(awtFont, false);


        // load font from a .ttf file
        /*
        try {
            /*
            InputStream inputStream = ResourceLoader.getResourceAsStream("OpenSans-Light.ttf");

            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(24f); // set font size
            font2 = new TrueTypeFont(awtFont2, false);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    //copied
    private void render(int score) {
        Color.white.bind();

        font.drawString(2, 200, "Score: "+score, Color.yellow);
        //font2.drawString(100, 100, "NICE LOOKING FONTS!", Color.green);
    }

    private void renderChooseText(int[] possibilities){
        Color.white.bind();
        String[] textOptions = new String[]{
                "Aces","Twos","Threes","Fours","Fives","Sixes","Three Of A Kind",
                "Four Of A Kind","Full House","Small Straight","Large Straight","Yahtzee"
        };
        //System.out.println(possibilities.length);

        for (int i = 0; i < (possibilities.length); i++) {
                font.drawString(50, 2 + (i*35), ""+textOptions[i]+" Score gained: "+ possibilities[i], Color.yellow);
        }
    }

    private void printScore(int score){
        font.drawString(2, 400, String.valueOf(score));
    }

    private void reset(){
        for (int i = 0; i < dice.length; i++) {
            dice[i].move();
        }
        held = new boolean[] {false, false,false,false,false,false };



    }

    private void moveAllToBottom(){
        for (int i = 0; i < dice.length; i++) {
            dice[i].moveToBottom();
        }
    }

}
