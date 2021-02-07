package com.company;

import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import javax.swing.*;


public class GUI extends JFrame implements Observer {

    GUI(String mainString, KeyListener keyListener) {
        super(mainString);

        addKeyListener(keyListener);

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void update(Observable observable, Object o) {
        JPanel panel = new MainDrawing((CubeRubikModel) observable);
        this.add(panel);
        panel.paint(this.getGraphics());

    }


    static class MainDrawing extends JPanel {
        private final CubeRubikModel cubeRubikModel;

        public MainDrawing(CubeRubikModel cubeRubikModel) {
            this.cubeRubikModel = cubeRubikModel;
        }

        @Override
        public void paint(Graphics g) {
            // paint Volumetric and flat cubes

            new VolumetricCubeDrawing(
                    100, 100, 200, g,
                    cubeRubikModel.getFrontSide(),
                    cubeRubikModel.getBackSide(),
                    cubeRubikModel.getUpSide(),
                    cubeRubikModel.getDownSide(),
                    cubeRubikModel.getLeftSide(),
                    cubeRubikModel.getRightSide(),
                    cubeRubikModel.getSelectedEdge()
            ).paint();

            new FlatCubeDrawing(
                    600, 100, 100, g,
                    cubeRubikModel.getFrontSide(),
                    cubeRubikModel.getBackSide(),
                    cubeRubikModel.getUpSide(),
                    cubeRubikModel.getDownSide(),
                    cubeRubikModel.getLeftSide(),
                    cubeRubikModel.getRightSide(),
                    cubeRubikModel.getSelectedEdge()
            ).paint();

        }

    }

    abstract static class CubeDrawing {
        protected Graphics g;

        Color[][] frontSide;
        Color[][] backSide;
        Color[][] upSide;
        Color[][] downSide;
        Color[][] leftSide;
        Color[][] rightSide;

        CubeRubikModel.Edge selectedEdge;

        protected int x0, y0;
        protected int sideSize;

        public CubeDrawing(int x0, int y0, int sideSize, Graphics graphics,
                           Color[][] frontSide, Color[][] backSide,
                           Color[][] upSide, Color[][] downSide,
                           Color[][] leftSide, Color[][] rightSide,
                           CubeRubikModel.Edge selectedEdge) {
            this.x0 = x0;
            this.y0 = y0;
            this.sideSize = sideSize;

            this.frontSide = frontSide;
            this.backSide = backSide;
            this.leftSide = leftSide;
            this.rightSide = rightSide;
            this.downSide = downSide;
            this.upSide = upSide;

            this.g = graphics;

            this.selectedEdge = selectedEdge;
        }

        public void paint() {
            if (null != selectedEdge) {
                darkerSelectedSize();
            }

            drawColors();
            drawBorder();
        }

        abstract protected void drawBorder();
        abstract protected void drawColors();

        private void darkerSelectedSize() {
            switch (selectedEdge.direction) {
                case x -> {
                    darkerHorizontal(frontSide, selectedEdge.number);
                    darkerHorizontal(leftSide, selectedEdge.number);
                    darkerHorizontal(rightSide, selectedEdge.number);
                    darkerHorizontal(backSide, selectedEdge.number);
                    if (0 == selectedEdge.number) {
                        darkAllSide(upSide);
                    } else if (2 == selectedEdge.number) {
                        darkAllSide(downSide);
                    }
                }
                case y -> {
                    darkerVertical(frontSide, selectedEdge.number);
                    darkerVertical(upSide, selectedEdge.number);
                    darkerVertical(backSide, 2 - selectedEdge.number);
                    darkerVertical(downSide, selectedEdge.number);
                    if (0 == selectedEdge.number) {
                        darkAllSide(leftSide);
                    } else if (2 == selectedEdge.number) {
                        darkAllSide(rightSide);
                    }
                }
                case z -> {
                    darkerHorizontal(upSide, selectedEdge.number);
                    darkerVertical(leftSide, selectedEdge.number);
                    darkerHorizontal(downSide, 2 - selectedEdge.number);
                    darkerVertical(rightSide, 2 - selectedEdge.number);
                    if (0 == selectedEdge.number) {
                        darkAllSide(backSide);
                    } else if (2 == selectedEdge.number) {
                        darkAllSide(frontSide);
                    }
                }
            }

        }

        private void darkerHorizontal(Color[][] side, int n) {
            for (int i = 0; i < side[0].length; ++i) {
                side[n][i] = side[n][i].darker();
            }
        }

        private void darkerVertical(Color[][] side, int n) {
            for (int i = 0; i < side.length; ++i) {
                side[i][n] = side[i][n].darker();
            }
        }

        private void darkAllSide(Color[][] side) {
            for (int i = 0; i < side.length; ++i) {
                for (int j = 0; j < side[0].length; ++j) {
                    side[i][j] = side[i][j].darker();
                }
            }
        }

    }

    static class FlatCubeDrawing extends CubeDrawing {

        Rectangle[][][] rectangles = new Rectangle[6][3][3];

        public FlatCubeDrawing(int x0, int y0, int sideSize, Graphics graphics,
                               Color[][] frontSide, Color[][] backSide,
                               Color[][] upSide, Color[][] downSide,
                               Color[][] leftSide, Color[][] rightSide,
                               CubeRubikModel.Edge selectedEdge) {
            super(
                    x0, y0, sideSize, graphics,
                    frontSide, backSide,
                    upSide, downSide,
                    leftSide, rightSide,
                    selectedEdge
            );

            final int rectangleSize = sideSize / 3;

            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < rectangles[0].length; ++j) {
                    for (int k = 0; k < rectangles[0][0].length; ++k) {
                        rectangles[i][j][k] = new Rectangle(
                                x0 + k * rectangleSize,
                                y0 + j * rectangleSize + i * sideSize,
                                rectangleSize,
                                rectangleSize
                        );
                    }
                }
            }

            for (int j = 0; j < rectangles[0].length; ++j) {
                for (int k = 0; k < rectangles[0][0].length; ++k) {
                    rectangles[4][j][k] = new Rectangle(
                            x0 + k * rectangleSize - sideSize,
                            y0 + j * rectangleSize + sideSize,
                            rectangleSize,
                            rectangleSize
                    );
                }
            }

            for (int j = 0; j < rectangles[0].length; ++j) {
                for (int k = 0; k < rectangles[0][0].length; ++k) {
                    rectangles[5][j][k] = new Rectangle(
                            x0 + k * rectangleSize + sideSize,
                            y0 + j * rectangleSize + sideSize,
                            rectangleSize,
                            rectangleSize
                    );
                }
            }
        }


        @Override
        protected void drawBorder() {
            g.setColor(Color.black);

            for (Rectangle[][] rectangle3 : rectangles) {
                for (Rectangle[] rectangle2 : rectangle3) {
                    for (Rectangle rectangle : rectangle2) {
                        g.drawRect(
                                rectangle.x,
                                rectangle.y,
                                rectangle.width,
                                rectangle.height
                        );
                    }
                }
            }
        }

        @Override
        protected void drawColors() {
            for (int i = 0; i < rectangles.length; ++i) {
                for (int j = 0; j < rectangles[0].length; ++j) {
                    for (int k = 0; k < rectangles[0][0].length; ++k) {

                        switch (i) {
                            case 0 -> g.setColor(upSide[j][k]);
                            case 1 -> g.setColor(frontSide[j][k]);
                            case 2 -> g.setColor(downSide[j][k]);
                            case 3 -> g.setColor(backSide[2 - j][2 - k]);
                            case 4 -> g.setColor(leftSide[j][k]);
                            case 5 -> g.setColor(rightSide[j][k]);
                        }

                        g.fillRect(
                                rectangles[i][j][k].x,
                                rectangles[i][j][k].y,
                                rectangles[i][j][k].width,
                                rectangles[i][j][k].height
                        );
                    }
                }
            }
        }

    }

    static class VolumetricCubeDrawing extends CubeDrawing {


        public VolumetricCubeDrawing(int x0, int y0, int sideSize, Graphics graphics,
                                     Color[][] frontSide, Color[][] backSide,
                                     Color[][] upSide, Color[][] downSide,
                                     Color[][] leftSide, Color[][] rightSide,
                                     CubeRubikModel.Edge selectedEdge) {
            super(
                    x0, y0, sideSize, graphics,
                    frontSide, backSide,
                    upSide, downSide,
                    leftSide, rightSide,
                    selectedEdge
            );
        }

        @Override
        protected void drawBorder() {
            final int smallSquareSize = sideSize / 3;

            g.setColor(Color.black);

            g.drawLine(x0, y0, x0 + sideSize, y0);
            g.drawLine(x0 + sideSize, y0, x0 + 3 * sideSize / 2, y0 + sideSize / 2);
            g.drawLine(x0 + sideSize + sideSize / 2, y0 + sideSize / 2, x0 + 3 * sideSize / 2, y0 + 3 * sideSize / 2);
            g.drawLine(x0 + 3 * sideSize / 2, y0 + 3 * sideSize / 2, x0 + sideSize / 2, y0 + 3 * sideSize / 2);
            g.drawLine(x0 + sideSize / 2, y0 + 3 * sideSize / 2, x0, y0 + sideSize);
            g.drawLine(x0, y0 + sideSize, x0, y0);
            g.drawLine(x0, y0, x0 + sideSize / 2, y0 + sideSize / 2);
            g.drawLine(x0 + sideSize / 2, y0 + sideSize / 2, x0 + sideSize / 2, y0 + 3 * sideSize / 2);
            g.drawLine(x0 + sideSize / 2, y0 + sideSize / 2, x0 + 3 * sideSize / 2, y0 + sideSize / 2);

            g.drawLine(x0 + smallSquareSize, y0, x0 + sideSize / 2 + smallSquareSize, y0 + sideSize / 2);
            g.drawLine(x0 + 2 * smallSquareSize, y0, x0 + sideSize / 2 + 2 * smallSquareSize, y0 + sideSize / 2);
            g.drawLine(x0 + smallSquareSize / 2, y0 + smallSquareSize / 2, x0 + smallSquareSize / 2 + sideSize, y0 + smallSquareSize / 2);
            g.drawLine(x0 + 2 * smallSquareSize / 2, y0 + 2 * smallSquareSize / 2, x0 + 2 * smallSquareSize / 2 + sideSize, y0 + 2 * smallSquareSize / 2);

            g.drawLine(x0 + sideSize / 2 + smallSquareSize, y0 + sideSize / 2,x0 + sideSize / 2 + smallSquareSize, y0 + 3 * sideSize / 2);
            g.drawLine(x0 + sideSize / 2 + 2 * smallSquareSize, y0 + sideSize / 2,x0 + sideSize / 2 + 2 * smallSquareSize, y0 + 3 * sideSize / 2);
            g.drawLine(x0 + sideSize / 2, y0 + sideSize / 2 + smallSquareSize, x0 + 3 * sideSize / 2, y0 + sideSize / 2 + smallSquareSize);
            g.drawLine(x0 + sideSize / 2, y0 + sideSize / 2 + 2 * smallSquareSize, x0 + 3 * sideSize / 2, y0 + sideSize / 2 + 2 * smallSquareSize);

            g.drawLine(x0 + smallSquareSize / 2, y0 + smallSquareSize / 2, x0 + smallSquareSize / 2, y0 + smallSquareSize / 2 + sideSize);
            g.drawLine(x0 + 2 * smallSquareSize / 2, y0 + 2 * smallSquareSize / 2, x0 + 2 * smallSquareSize / 2, y0 + 2 * smallSquareSize / 2 + sideSize);
            g.drawLine(x0, y0 + smallSquareSize, x0 + sideSize / 2, y0 + smallSquareSize + sideSize / 2);
            g.drawLine(x0, y0 + 2 * smallSquareSize, x0 + sideSize / 2, y0 + 2 * smallSquareSize + sideSize / 2);
        }

        @Override
        protected void drawColors() {
            final int smallSquareSize = sideSize / 3;

            // draw squares from front side
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    g.setColor(frontSide[i][j]);
                    g.fillRect(x0 + sideSize / 2 + j * smallSquareSize,
                            x0 + sideSize / 2 + i * smallSquareSize,
                            smallSquareSize, smallSquareSize);
                }
            }

            // draw squares from up side
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    g.setColor(upSide[i][j]);

                    int y = y0 + i * smallSquareSize / 2;
                    int x = x0 + j * smallSquareSize + (y - y0);

                    g.fillPolygon(
                            new int[]{x, x + smallSquareSize, x + 3 * smallSquareSize / 2, x + smallSquareSize / 2},
                            new int[]{y, y, y + smallSquareSize / 2, y + smallSquareSize / 2},
                            4
                    );

                }
            }

            // draw squares from left side
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    g.setColor(leftSide[i][j]);

                    int x = x0 + j * smallSquareSize / 2;
                    int y = y0 + i * smallSquareSize + (x - x0);

                    g.fillPolygon(
                            new int[]{x, x + smallSquareSize / 2, x + smallSquareSize / 2, x},
                            new int[]{y, y + smallSquareSize / 2, y + 3 * smallSquareSize / 2, y + smallSquareSize},
                            4
                    );
                }
            }
        }

    }

}
