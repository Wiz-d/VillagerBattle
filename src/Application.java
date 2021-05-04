import java.util.Scanner;

public class Application {

    public static void gameLoop(String[][] field) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Изберете посока за вървене:");
        String directionOfMovement = scanner.nextLine();
        field = BoardManager.unitMovement(field, directionOfMovement);
        field = BoardManager.enemyUnitMovement(field);
        if (BoardManager.isShootingPossible(field)) {
            BoardManager.killingSystem(field);
            System.out.println("Units alive: " + BoardManager.unitsAlive);
        }
        if (BoardManager.isSaboteurDead) {
            System.out.println("GG WP");
            System.exit(0);
        }
        if (directionOfMovement.equals("f")) {
            BoardManager.bombPlacement(field);
        }
        if (BoardManager.isBombPlanted) {
            BoardManager.buildingDestruction(field);
        }
        if (BoardManager.isSmallBuildingDestroyed && BoardManager.isMediumBuildingDestroyed && BoardManager.isLargeBuildingDestroyed) {
            System.out.println("Печелиш милионите! ");
            System.exit(0);
        }
        BoardManager.renderField(field);
        gameLoop(field);
    }

    public static void main(String[] args) {
        String[][] field = BoardManager.bootstrapField();
        BoardManager.renderField(field);
        gameLoop(field);
    }
}
