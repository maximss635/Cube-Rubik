package com.company;

abstract class CubeCommand {

    protected final CubeRubikModel cubeRubikModel;

    public CubeCommand(CubeRubikModel cubeRubikModel) {
        this.cubeRubikModel = cubeRubikModel;
    }

    abstract void execute();
}

class CommandRotateCube extends CubeCommand {

    CubeRubikModel.Direction direction;
    boolean clockWise;

    public CommandRotateCube(CubeRubikModel cubeRubikModel, CubeRubikModel.Direction direction, boolean clockWise) {
        super(cubeRubikModel);

        this.direction = direction;
        this.clockWise = clockWise;
    }

    @Override
    void execute() {
        switch (direction) {
            case x -> {
                if (!clockWise) {
                    cubeRubikModel.rotateLeft(false);
                    cubeRubikModel.rotateLeft(false);
                }
                cubeRubikModel.rotateLeft(true);
            }
            case y -> {
                if (!clockWise) {
                    cubeRubikModel.rotateUp(false);
                    cubeRubikModel.rotateUp(false);
                }
                cubeRubikModel.rotateUp(true);
            }
            case z -> {
                if (!clockWise) {
                    cubeRubikModel.rotateClockWise(false);
                    cubeRubikModel.rotateClockWise(false);
                }
                cubeRubikModel.rotateClockWise(true);
            }
        }
    }
}

class CommandRotateEdge extends CubeCommand {

    private final CubeRubikModel.Edge edge;
    private boolean clockWise;

    public CommandRotateEdge(CubeRubikModel cubeRubikModel, CubeRubikModel.Edge edge, boolean clockWise) {
        super(cubeRubikModel);

        this.edge = edge;
        this.clockWise = clockWise;
    }

    @Override
    void execute() {
        switch (edge.direction) {
            case x, y -> clockWise = !clockWise;
        }

        if (null == edge) {
            return;
        }

        switch (edge.direction) {
            case x:
                switch (edge.number) {
                    case 2 -> {
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(true);
                    }
                    case 1 -> {
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        if (!clockWise) {
                            cubeRubikModel.rotateLeft(false);
                            cubeRubikModel.rotateLeft(false);
                        }
                        cubeRubikModel.rotateLeft(true);
                    }
                    case 0 -> {
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(clockWise, false);
                        cubeRubikModel.rotateUp(true);
                    }
                }
                break;

            case y:
                switch (edge.number) {
                    case 0 -> {
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateLeft(true);
                    }
                    case 1 -> {
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.moveFrontSide(clockWise, false);
                        cubeRubikModel.rotateLeft(false);
                        if (!clockWise) {
                            cubeRubikModel.rotateUp(false);
                            cubeRubikModel.rotateUp(false);
                        }
                        cubeRubikModel.rotateUp(true);
                    }
                    case 2 -> {
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.moveFrontSide(clockWise, false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.rotateLeft(false);
                        cubeRubikModel.rotateLeft(true);
                    }
                }
                break;

            case z:
                switch (edge.number) {
                    case 2 -> cubeRubikModel.moveFrontSide(clockWise, true);
                    case 1 -> {
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        if (!clockWise) {
                            cubeRubikModel.rotateClockWise(false);
                            cubeRubikModel.rotateClockWise(false);
                        }
                        cubeRubikModel.rotateClockWise(true);
                    }
                    case 0 -> {
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.moveFrontSide(!clockWise, false);
                        cubeRubikModel.rotateUp(false);
                        cubeRubikModel.rotateUp(true);
                    }
                }

        }
    }
}