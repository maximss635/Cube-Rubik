package com.company;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Observable;

public class CubeRubikModel extends Observable {

    private final int N = 3;

    private final Color[][] frontSide;
    private final Color[][] backSide;
    private final Color[][] upSide;
    private final Color[][] downSide;
    private final Color[][] leftSide;
    private final Color[][] rightSide;

    enum Direction {
        x, y, z;
    }

    CubeRubikModel() {
        frontSide = new Color[N][N];
        backSide = new Color[N][N];
        upSide = new Color[N][N];
        downSide = new Color[N][N];
        leftSide = new Color[N][N];
        rightSide = new Color[N][N];

        paintSide(frontSide, Color.orange.darker());
        paintSide(backSide, Color.red);
        paintSide(leftSide, Color.green);
        paintSide(rightSide, Color.blue);
        paintSide(upSide, Color.yellow);
        paintSide(downSide, Color.white);

//        paintSide(frontSide, Color.white);
//        paintSide(backSide, Color.white);
//        paintSide(leftSide, Color.white);
//        paintSide(rightSide, Color.white);
//        paintSide(upSide, Color.white);
//        paintSide(downSide, Color.white);


//        downSide[0][0] = Color.green;
//        downSide[0][1] = Color.yellow;
//        downSide[0][2] = Color.red;
//        downSide[1][0] = Color.orange;
//        downSide[1][1] = Color.pink;
//        downSide[1][2] = Color.cyan;
//        downSide[2][0] = Color.blue;
//        downSide[2][1] = Color.lightGray;
//        downSide[2][2] = Color.white;

        selectedEdge = new Edge(Direction.x, 0);
    }

    private void paintSide(Color[][] side, Color color) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                side[i][j] = color;
            }
        }
    }

    public static void main(String[] args) {
        CubeRubikModel cubeRubikModel = new CubeRubikModel();
        KeyListener keyListener = new KeyController(cubeRubikModel);
        GUI gui = new GUI("Cube Rubik", keyListener);

        cubeRubikModel.addObserver(gui);

        Confuser.confuse(cubeRubikModel, 1000);


    }



    // getters for drawing
    Color[][] getFrontSide() {
        return makeCopy(frontSide);
    }

    Color[][] getBackSide() {
        return makeCopy(backSide);
    }

    Color[][] getUpSide() {
        return makeCopy(upSide);
    }

    Color[][] getDownSide() {
        return makeCopy(downSide);
    }

    Color[][] getLeftSide() {
        return makeCopy(leftSide);
    }

    Color[][] getRightSide() {
        return makeCopy(rightSide);
    }



    private void rotate(Color[][][] sideArray, boolean viewUpdate) {
        Color[][] tmp = new Color[3][3];

        System.arraycopy(sideArray[0], 0, tmp, 0, 3);
        System.arraycopy(sideArray[1], 0, sideArray[0], 0, 3);
        System.arraycopy(sideArray[2], 0, sideArray[1], 0, 3);
        System.arraycopy(sideArray[3], 0, sideArray[2], 0, 3);
        System.arraycopy(tmp, 0, sideArray[3], 0, 3);

        if (viewUpdate) {
            this.setChanged();
            this.notifyObservers();
        }
    }

    private void rotate(Color[][] side, boolean clockWise, boolean viewUpdate) {
        Color[][] tmp = new Color[3][3];

        for (int i = 0; i < 3; ++i) {
            System.arraycopy(side[i], 0, tmp[i], 0, 3);
        }

        if (clockWise) {
            side[0][0] = tmp[2][0];
            side[0][1] = tmp[1][0];
            side[0][2] = tmp[0][0];
            side[1][0] = tmp[2][1];
            side[1][1] = tmp[1][1];
            side[1][2] = tmp[0][1];
            side[2][0] = tmp[2][2];
            side[2][1] = tmp[1][2];
            side[2][2] = tmp[0][2];
        } else {
            side[0][0] = tmp[0][2];
            side[0][1] = tmp[1][2];
            side[0][2] = tmp[2][2];
            side[1][0] = tmp[0][1];
            side[1][1] = tmp[1][1];
            side[1][2] = tmp[2][1];
            side[2][0] = tmp[0][0];
            side[2][1] = tmp[1][0];
            side[2][2] = tmp[2][0];
        }

        if (viewUpdate) {
            this.setChanged();
            this.notifyObservers();
        }
    }

    public void rotateLeft(boolean viewUpdate) {
        rotate(new Color[][][]{frontSide, rightSide, backSide, leftSide}, false);
        rotate(upSide, true, false);
        rotate(downSide, false, viewUpdate);
    }

    public void rotateUp(boolean viewUpdate) {
        rotate(new Color[][][]{frontSide, downSide, backSide, upSide}, false);
        rotate(rightSide, true, false);
        rotate(leftSide, false, false);

        rotate(backSide, true, false);
        rotate(backSide, true, false);

        rotate(downSide, true, false);
        rotate(downSide, true, viewUpdate);
    }

    public void rotateClockWise(boolean viewUpdate) {
        rotate(new Color[][][]{upSide, leftSide, downSide, rightSide}, false);

        rotate(rightSide, true, false);
        rotate(downSide, true, false);
        rotate(leftSide, true, false);
        rotate(upSide, true, false);

        rotate(frontSide, true, false);
        rotate(backSide, false, viewUpdate);
    }


    public static class Edge {

        public Edge(Direction direction, int number) {
            this.direction = direction;
            this.number = number;
        }

        enum Action {
            inc, dec, changeDirection
        }

        public Direction direction;
        public int number;   // 0, 1, 2

        public void next() {
            number = Math.min(number + 1, 2);
        }

        public void prev() {
            number = Math.max(number - 1, 0);
        }

        public void changeDirection() {
            switch (direction) {
                case x -> direction = Direction.y;
                case y -> direction = Direction.z;
                case z -> direction = Direction.x;
            }
        }


    }

    private final Edge selectedEdge;
    private boolean selectionMode = true;

    public void selectionOnOff() {
        selectionMode = !selectionMode;

        this.setChanged();
        this.notifyObservers();
    }

    public void changeSelectedEdge(Edge.Action action) {
        switch (action) {
            case inc -> selectedEdge.next();
            case dec -> selectedEdge.prev();
            case changeDirection -> selectedEdge.changeDirection();
        }

        this.setChanged();
        this.notifyObservers();
    }


    // moves
    public void moveFrontSide(boolean clockWise, boolean update) {
        Color[][] tmp = new Color[3][3];
        for (int i = 0; i < N; ++i) {
            System.arraycopy(upSide[i], 0, tmp[i], 0, N);
        }

        if (clockWise) {
            upSide[2][0] = leftSide[2][2];
            upSide[2][1] = leftSide[1][2];
            upSide[2][2] = leftSide[0][2];

            leftSide[2][2] = downSide[0][2];
            leftSide[1][2] = downSide[0][1];
            leftSide[0][2] = downSide[0][0];

            downSide[0][2] = rightSide[0][0];
            downSide[0][1] = rightSide[1][0];
            downSide[0][0] = rightSide[2][0];

            rightSide[0][0] = tmp[2][0];
            rightSide[1][0] = tmp[2][1];
            rightSide[2][0] = tmp[2][2];

            rotate(frontSide, clockWise, update);
        }
        else {
            moveFrontSide(true, false);
            moveFrontSide(true, false);
            moveFrontSide(true, update);

        }

        if (update) {
            this.setChanged();
            this.notifyObservers();
        }
    }

    public Edge getSelectedEdge() {
        return selectionMode ? selectedEdge : null;
    }


    private Color[][] makeCopy(Color[][] side) {
        Color[][] copy = new Color[N][N];
        for (int i = 0; i < N; ++i) {
            System.arraycopy(side[i], 0, copy[i], 0, N);
        }

        return copy;
    }

}
