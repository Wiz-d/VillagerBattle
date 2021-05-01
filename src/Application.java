import java.util.Scanner;

public class Application {

    public static void gameLoop(String[][] field){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Изберете посока за вървене:");
       String directionOfMovement = scanner.nextLine();
//       if (directionOfMovement.equals("c")){
//           System.out.println("Choose your leader! (1-4) ");
//           String leaderChoice = scanner.nextLine();
//           Utility.leaderPosition = leaderChoice;
//           BoardManager.(field,Utility.leaderPosition);
//           BoardManager.renderField(field);
//       }
        field = BoardManager.unitMovement(field,directionOfMovement);
        BoardManager.renderField(field);

        gameLoop(field);
    }

    public static void main(String[] args) {
        String[][] field = BoardManager.bootstrapField();
        BoardManager.generateFreeSpace(field);
        BoardManager.generateBuildings(field);
        BoardManager.generatePlayableUnits(field);
        BoardManager.renderField(field);
        gameLoop(field);
    }
}
