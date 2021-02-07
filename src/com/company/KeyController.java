package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController implements KeyListener {

    private final CubeRubikModel cubeRubikModel;

    public KeyController(CubeRubikModel cubeRubikModel) {
        this.cubeRubikModel = cubeRubikModel;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // nothing
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        CubeCommand command = null;

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_A -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.x, true);
            case KeyEvent.VK_D -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.x, false);
            case KeyEvent.VK_W -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.y, true);
            case KeyEvent.VK_S -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.y, false);
            case KeyEvent.VK_E -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.z, true);
            case KeyEvent.VK_Q -> command = new CommandRotateCube(cubeRubikModel, CubeRubikModel.Direction.z, false);

            case KeyEvent.VK_SHIFT -> command = new CommandRotateEdge(
                    cubeRubikModel,
                    cubeRubikModel.getSelectedEdge(),
                    keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT
            );

            case KeyEvent.VK_SPACE -> cubeRubikModel.changeSelectedEdge(CubeRubikModel.Edge.Action.changeDirection);
            case KeyEvent.VK_CONTROL -> cubeRubikModel.selectionOnOff();

            case KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT -> cubeRubikModel.changeSelectedEdge(CubeRubikModel.Edge.Action.inc);
            case KeyEvent.VK_UP, KeyEvent.VK_LEFT -> cubeRubikModel.changeSelectedEdge(CubeRubikModel.Edge.Action.dec);

        }

        if (command != null) {
            command.execute();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // nothing
    }
}
