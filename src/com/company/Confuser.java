package com.company;

public class Confuser {
    private Confuser() {
        throw new AssertionError();
    }

    public static void confuse(CubeRubikModel cubeRubikModel, int n) {
        for (int i = 0; i < n; ++i) {
            genCommand(cubeRubikModel).execute();
        }
    }

    private static CommandRotateEdge genCommand(CubeRubikModel cubeRubikModel) {
        return new CommandRotateEdge(
                cubeRubikModel,
                new CubeRubikModel.Edge(genDirection(), (int) (10 * Math.random() % 3)),
                genBoolean()
        );
    }

    private static CubeRubikModel.Direction genDirection() {
        switch (((int) (10 * Math.random()) % 3)) {
            case 0 -> {
                return CubeRubikModel.Direction.x;
            }
            case 1 -> {
                return CubeRubikModel.Direction.y;
            }
            case 2 -> {
                return CubeRubikModel.Direction.z;
            }
        }

        return null;
    }

    private static boolean genBoolean() {
        switch (((int) (10 * Math.random()) % 2)) {
            case 0 -> {
                return false;
            }
            case 1 -> {
                return true;
            }
        }

        return false;
    }

}
