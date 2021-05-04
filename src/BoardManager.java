import java.util.Random;
import java.util.Scanner;

public class BoardManager {

    public static String[] unitIndex = new String[]{"1", "2", "3", "4"};

    static int unit1Row = 14;
    static int unit1Col = 11;
    static int unit2Row = 14;
    static int unit2Col = 12;
    static int unit3Row = 14;
    static int unit3Col = 13;
    static int unit4Row = 14;
    static int unit4Col = 14;

    static int enemyUnitRow = 0;
    static int enemyUnitCol = 0;

    static boolean isTankDead = false;
    static boolean isSniperDead = false;
    static boolean isSaboteurDead = false;
    static int unitsAlive = 4;
    static boolean hasEnemyKilledSomeone = false;
    static int enemyVACount = 0;

    static int bombRowHelper = 0;
    static int bombColHelper = 0;
    public static int bombCount = 0;

    public static boolean isBombPlanted = false;
    public static boolean isSmallBuildingDestroyed = false;
    public static boolean isMediumBuildingDestroyed = false;
    public static boolean isLargeBuildingDestroyed = false;

    static String leaderChoice = "1";
    static String[] units = unitQueueOrganizer(leaderChoice);

    public static int bombRowTemp;
    public static int bombColTemp;

    public static boolean isMediumStructurePlantedBomb = false;
    public static boolean isLargeStructurePlantedBomb = false;

    //startup process
    public static String[][] bootstrapField() {
        String[][] field = new String[Utility.FIELD_NUMBER_OF_ROWS][Utility.FIELD_NUMBER_OF_COLS];
        generateFreeSpace(field);
        generateBuildings(field);
        generatePlayableUnits(field);
        generatingEnemy(field);
        return field;
    }

    //initialization of the field
    public static String[][] generateFreeSpace(String[][] field) {
        for (int row = 0; row < Utility.FIELD_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Utility.FIELD_NUMBER_OF_COLS; col++) {
                field[row][col] = Utility.TERRAIN_UNIT;
            }
        }
        return field;
    }

    //Spawning the playable units
    public static String[][] generatePlayableUnits(String[][] field) {

        String[] units = unitQueueOrganizer(leaderChoice);

        field[unit1Row][unit1Col] = units[0];
        field[unit2Row][unit2Col] = units[1];
        field[unit3Row][unit3Col] = units[2];
        field[unit4Row][unit4Col] = units[3];
        return field;
    }

    //displaying the playing field
    public static String[][] renderField(String[][] field) {

        for (int row = 0; row < Utility.FIELD_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Utility.FIELD_NUMBER_OF_COLS; col++) {
                System.out.print("  " + field[row][col]);
            }
            System.out.println();
        }

        return field;
    }

    //building creation at the start of the game
    public static String[][] generateBuildings(String[][] field) {

        for (int row = 2; row < 4; row++) {
            for (int col = 2; col < 4; col++) {
                field[row][col] = Utility.SMALL_BUILDING_SQUARE_MARKER;
            }
        }


        for (int row = 6; row < 8; row++) {
            for (int col = 9; col < 12; col++) {
                field[row][col] = Utility.MEDIUM_BUILDING_SQUARE_MARKER;
            }
        }


        for (int row = 8; row < 11; row++) {
            for (int col = 2; col < 5; col++) {
                field[row][col] = Utility.LARGE_BUILDING_SQUARE_MARKER;
            }
        }

        return field;
    }

    //Organizer for the leader position
    public static String[] unitQueueOrganizer(String leaderChoice) {


        String temp = "";
        String[] positionInQueue = new String[]{"1", "2", "3", "4"};
        String[] units = new String[4];

        if (leaderChoice.equals("1")) {

            int leaderPosition = 0;
            for (int i = 0; i < 4; i++) {
                if (unitIndex[i].equals("1")) {
                    leaderPosition = i;
                }
            }
            temp = unitIndex[0];
            unitIndex[0] = unitIndex[leaderPosition];
            unitIndex[leaderPosition] = temp;
        }

        if (leaderChoice.equals("2")) {

            int currentPosition = 0;
            for (int i = 0; i < 4; i++) {
                if (unitIndex[i].equals("2")) {
                    currentPosition = i;
                }
            }
            temp = unitIndex[0];
            unitIndex[0] = unitIndex[currentPosition];
            unitIndex[currentPosition] = temp;
        }

        if (leaderChoice.equals("3")) {

            int currentPosition = 0;
            for (int i = 0; i < 4; i++) {
                if (unitIndex[i].equals("3")) {
                    currentPosition = i;
                }
            }
            temp = unitIndex[0];
            unitIndex[0] = unitIndex[currentPosition];
            unitIndex[currentPosition] = temp;

        }

        if (leaderChoice.equals("4")) {

            int currentPosition = 0;
            for (int i = 0; i < 4; i++) {
                if (unitIndex[i].equals("4")) {
                    currentPosition = i;
                }
            }
            temp = unitIndex[0];
            unitIndex[0] = unitIndex[currentPosition];
            unitIndex[currentPosition] = temp;
        }

        for (int i = 0; i < 4; i++) {
            units[i] = positionInQueue[i] + unitIndex[i];
        }

        return units;
    }

    //restavration of buildings after movement
    public static String[][] buildingRespawnAfterMovement(String[][] field) {

        for (int row = 2; row < 4; row++) {
            for (int col = 2; col < 4; col++) {
                if (field[row][col].equals(Utility.TERRAIN_UNIT) && !isSmallBuildingDestroyed)
                    field[row][col] = Utility.SMALL_BUILDING_SQUARE_MARKER;
            }
        }

        for (int row = 6; row < 8; row++) {
            for (int col = 9; col < 12; col++) {
                if (field[row][col].equals(Utility.TERRAIN_UNIT) && !isMediumStructurePlantedBomb)
                    field[row][col] = Utility.MEDIUM_BUILDING_SQUARE_MARKER;
            }
        }

        for (int row = 8; row < 11; row++) {
            for (int col = 2; col < 5; col++) {
                if (field[row][col].equals(Utility.TERRAIN_UNIT) && !isLargeStructurePlantedBomb)
                    field[row][col] = Utility.LARGE_BUILDING_SQUARE_MARKER;
            }
        }


        return field;
    }

    //movement system of the playable units
    public static String[][] unitMovement(String[][] field, String directionOfMovement) {

        Scanner scanner = new Scanner(System.in);

        String[] units;

        int unit1CurrentRow = unit1Row;
        int unit1CurrentCol = unit1Col;

        int unit2CurrentRow = unit2Row;
        int unit2CurrentCol = unit2Col;

        int unit3CurrentRow = unit3Row;
        int unit3CurrentCol = unit3Col;

        if (directionOfMovement.equals("c")) {
            System.out.println("Choose leader from 1 to 4");
            BoardManager.leaderChoice = scanner.nextLine();
            units = unitQueueOrganizer(leaderChoice);
        }



        if (directionOfMovement.equals(Utility.DIRECTION_FORWARD)) {
            if (unit1Row == 8 && unit1Col == 10){
                System.out.println("you can't pass because fuck you");
            }
            else {
                if (unit1Row == 0) {
                    unit1CurrentRow = 14;
                } else {
                    unit1CurrentRow--;
                }
            }
        }

        if (directionOfMovement.equals(Utility.DIRECTION_BACKWARDS)) {
            if (unit1Row == 5 && unit1Col == 10){
                System.out.println("you can't pass because fuck you");
            }
            else {
                if (unit1Row == 14) {
                    unit1CurrentRow = 0;
                } else {
                    unit1CurrentRow++;
                }
            }
        }

        if (directionOfMovement.equals(Utility.DIRECTION_LEFT)) {
            if (unit1Row == 6 && unit1Col == 11 || unit1Row == 7 && unit1Col == 11) {
                System.out.println("you can't pass because fuck you");
            } else {
                if (unit1Col == 0) {
                    unit1CurrentCol = 14;
                } else {
                    unit1CurrentCol--;
                }
            }
        }

        if (directionOfMovement.equals(Utility.DIRECTION_RIGHT)) {
            if (unit1Row == 6 && unit1Col == 9 || unit1Row == 7 && unit1Col == 9) {
                System.out.println("you can't pass because fuck you");
            } else {
                if (unit1Col == 14) {
                    unit1CurrentCol = 0;
                } else {
                    unit1CurrentCol++;
                }
            }
        }


        int tempRow = unit1Row;
        int tempCol = unit1Col;

        units = unitQueueOrganizer(leaderChoice);

        if (unitsAlive == 4) {


            field[unit1Row][unit1Col] = units[1]; // на мястото на 1 става 2
            field[unit2Row][unit2Col] = units[2];
            field[unit3Row][unit3Col] = units[3];
            field[unit4Row][unit4Col] = Utility.TERRAIN_UNIT;


            unit1Row = unit1CurrentRow;   //ново място за 1
            unit1Col = unit1CurrentCol;

            unit2Row = tempRow;     //имах проблем с присвояването и това помогна
            unit2Col = tempCol;

            unit3Row = unit2CurrentRow;
            unit3Col = unit2CurrentCol;

            unit4Row = unit3CurrentRow;
            unit4Col = unit3CurrentCol;

            field[unit1Row][unit1Col] = units[0];  // новото място става 1
            field[unit2Row][unit2Col] = units[1];
            field[unit3Row][unit3Col] = units[2];
            field[unit4Row][unit4Col] = units[3];

        }
        if (unitsAlive == 3) {


            field[unit1Row][unit1Col] = units[1]; // на мястото на 1 става 2
            field[unit2Row][unit2Col] = units[2];
            field[unit3Row][unit3Col] = Utility.TERRAIN_UNIT;


            unit1Row = unit1CurrentRow;   //ново място за 1
            unit1Col = unit1CurrentCol;

            unit2Row = tempRow;     //имах проблем с присвояването и това помогна
            unit2Col = tempCol;

            unit3Row = unit2CurrentRow;
            unit3Col = unit2CurrentCol;


            field[unit1Row][unit1Col] = units[0];  // новото място става 1
            field[unit2Row][unit2Col] = units[1];
            field[unit3Row][unit3Col] = units[2];

        }
        if (unitsAlive == 2) {

            field[unit1Row][unit1Col] = units[1]; // на мястото на 1 става 2
            field[unit2Row][unit2Col] = Utility.TERRAIN_UNIT;


            unit1Row = unit1CurrentRow;   //ново място за 1
            unit1Col = unit1CurrentCol;

            unit2Row = tempRow;     //имах проблем с присвояването и това помогна
            unit2Col = tempCol;


            field[unit1Row][unit1Col] = units[0];  // новото място става 1
            field[unit2Row][unit2Col] = units[1];


        }
        if (unitsAlive == 1) {

            field[unit1Row][unit1Col] = Utility.TERRAIN_UNIT; // на мястото на 1 става 2

            unit1Row = unit1CurrentRow;       //new 1
            unit1Col = unit1CurrentCol;

            field[unit1Row][unit1Col] = units[0];  // новото място става 1

        }

        field = buildingRespawnAfterMovement(field);

        return field;
    }

    //creation of the Enemy unit at the start of the game
    public static String[][] generatingEnemy(String[][] field){

        Random random = new Random();
        enemyUnitRow = random.nextInt(14);
        if(enemyUnitRow == 0){
            enemyUnitRow++;
        }
        enemyUnitCol = random.nextInt(14);
        if(enemyUnitCol== 0){
            enemyUnitCol++;
        }
        if (enemyUnitRow > 7 && enemyUnitCol > 10) {
            generatingEnemy(field);
        }
        else field[enemyUnitRow][enemyUnitCol] = Utility.ENEMY_UNIT_MARKER;

        return field;
    }

    //system for the movement of the enemy unit
    public static String[][] enemyUnitMovement(String[][] field){

        Random random = new Random();

        if(hasEnemyKilledSomeone){
            enemyVACount++;
            field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;
            if(enemyVACount % 4 == 0){
                hasEnemyKilledSomeone = false;
            }
        }

        if(!hasEnemyKilledSomeone) {


            int enemyCurrentRowPosition = enemyUnitRow;
            int enemyCurrentColPosition = enemyUnitCol;

            int randomDirection = random.nextInt(4);
            if (randomDirection == 0) {
                randomDirection = 1;
            }

            //Посоките на врага
            if (randomDirection == 1) {
                enemyCurrentRowPosition--;
            }
            if (randomDirection == 2) {
                enemyCurrentRowPosition++;
            }
            if (randomDirection == 3) {
                enemyCurrentColPosition--;
            }
            if (randomDirection == 1) {
                enemyCurrentColPosition++;
            }

            //Проверка дали врага излиза от мапа
            if (enemyCurrentRowPosition == -1) {
                enemyCurrentRowPosition = 14;
            }
            if (enemyCurrentColPosition == -1) {
                enemyCurrentColPosition = 14;
            }
            if (enemyCurrentRowPosition == 15) {
                enemyCurrentRowPosition = 0;
            }
            if (enemyCurrentColPosition == 15) {
                enemyCurrentColPosition = 0;
            }


            field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;

            enemyUnitRow = enemyCurrentRowPosition;
            enemyUnitCol = enemyCurrentColPosition;

            field[enemyUnitRow][enemyUnitCol] = Utility.ENEMY_UNIT_MARKER;

            field = buildingRespawnAfterMovement(field);
        }
        return field;
    }

    //detection of playable units by the enemy
    public static boolean isShootingPossible(String[][] field){

            String[] unit = unitQueueOrganizer(leaderChoice);

            int tempRow = enemyUnitRow;
            int tempCol = enemyUnitCol;

            switch (tempRow) {
                case 0 -> tempRow++;
                case 14 -> tempRow--;
            }
            switch (tempCol) {
                case 0 -> tempCol++;
                case 14 -> tempCol--;
            }

            for (int row = tempRow - 1; row < tempRow + 2; row++) {
                for (int col = tempCol - 1; col < tempCol + 2; col++) {
                    for (int i = 0; i < 4; i++) {
                        if (field[row][col].equals(unit[i])) {
                            return true;
                        }
                    }
                }
            }
            return false;
    }

    //system for killing players with the enemy unit
    public static String[][] killingSystem(String[][] field) {

        Random random = new Random();
        int randomKillerNumberTwo = 0;
        int randomKillerNumber = random.nextInt(24);
        String[] units = unitQueueOrganizer(leaderChoice);

        if(randomKillerNumber == 0){
            randomKillerNumber = 1;
        }


        if(randomKillerNumber % 11 == 0) {

            if(isSniperDead == false) {
                randomKillerNumberTwo = random.nextInt(2);
            }
            if(randomKillerNumberTwo == 0){
                randomKillerNumber = 1;
            }

            if(randomKillerNumberTwo == 1) {
                if (isTankDead == true) {
                    if (units[0].charAt(1) == '4')    //equals("4 ")) {
                        units[0] = units[1];
                        units[1] = units[2];
                        field[unit3Row][unit3Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isSaboteurDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                    if (units[0].charAt(1) == '3') {
                        units[0] = units[1];
                        units[1] = units[2];
                        field[unit3Row][unit3Col] = Utility.TERRAIN_UNIT;
                       unitsAlive--;
                        isSmallBuildingDestroyed = true;
                        hasEnemyKilledSomeone = true;
                    }
                    if (units[0].charAt(1) == '2') {
                        units[0] = units[1];
                        units[1] = units[2];
                        field[unit3Row][unit3Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isSniperDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                }

                if (isTankDead == false) {
                    if (units[0].charAt(1) == '1') {
                        units[0] = units[1];
                        units[1] = units[2];
                        units[2] = units[3];
                        field[unit4Row][unit4Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isTankDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                    if (units[1].charAt(1) == '1') {
                        units[1] = units[2];
                        units[2] = units[3];
                        field[unit3Row][unit3Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isTankDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                    if (units[2].charAt(1) == '1') {
                        units[2] = units[3];
                        field[unit2Row][unit2Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isTankDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                    if (units[3].charAt(1) == '1') {
                        field[unit4Row][unit4Col] = Utility.TERRAIN_UNIT;
                        unitsAlive--;
                        isTankDead = true;
                        hasEnemyKilledSomeone = true;
                    }
                }
            }


        if(randomKillerNumber % 11 != 0 || randomKillerNumberTwo == 2) {
            int randomCornerNumber = random.nextInt(3);
            if(randomCornerNumber == 0) {
                field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;
                enemyUnitRow = 0;
                enemyUnitCol = 0;
                field[enemyUnitRow][enemyUnitRow] = Utility.ENEMY_UNIT_MARKER;
            }
            if(randomCornerNumber == 1) {
                field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;
                enemyUnitRow = 0;
                enemyUnitCol = 14;
                field[enemyUnitRow][enemyUnitCol] = Utility.ENEMY_UNIT_MARKER;
            }
            if(randomCornerNumber == 2) {
                field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;
                enemyUnitRow = 14;
                enemyUnitCol = 0;
                field[enemyUnitRow][enemyUnitCol] = Utility.ENEMY_UNIT_MARKER;
            }
            if(randomCornerNumber == 3) {
                field[enemyUnitRow][enemyUnitCol] = Utility.TERRAIN_UNIT;
                enemyUnitRow = 14;
                enemyUnitCol = 14;
                field[enemyUnitRow][enemyUnitCol] = Utility.ENEMY_UNIT_MARKER;
            }
        }

        return field;
    }

    //bomb placement
    public static String[][] bombPlacement(String[][] field){

        String[] units = unitQueueOrganizer(leaderChoice);

        int bombRow = unit1Row;
        int bombCol = unit1Col;

        if(units[0].charAt(1) == '4' ){

            for(int row = 0; row < 15; row++){
                for(int col = 0; col < 15; col++){
                    if(bombRow == row && bombCol == col) {
                        field[bombRow][bombCol] = " ? ";
                        isBombPlanted = true;
                    }
                }
            }

            bombRowTemp = bombRow;
            bombColTemp = bombCol;
        }
        else{
            System.out.println("Този герой няма специално действие");
        }

        return field;
    }

    //Проверка дали бомбата е заложена в малката сграда
    public static boolean hasBombBeenPlacedInSmallStructure(String[][] field) {

        for(int row = 2; row < 4; row++){
            for(int col = 2; col < 4; col++) {
                if(field[row][col].equals(" ? ")){
                    return true;
                }
            }
        }

        return false;
    }

    //Проверка дали бомбата е заложена в средната сграда
    public static boolean hasBombBeenPlacedInMediumStructure(String[][] field) {

        for(int row = 6; row < 8; row++){
            for(int col = 9; col < 12; col++) {
                if(field[row][col].equals(" ? ")){
                    return true;
                }
            }
        }

        return false;
    }

    //Проверка дали бомбата е заложена в голямата сграда
    public static boolean hasBombBeenPlacedInLargeStructure(String[][] field) {

        for(int row = 8; row < 11; row++){
            for(int col = 2; col < 5; col++) {
                if(field[row][col].equals(" ? ")){
                    return true;
                }
            }
        }

        return false;
    }


    public static String[][] buildingDestruction(String[][] field) {
        field[bombRowTemp][bombColTemp] = " ? ";

        if(bombCount == 6) {

            if(hasBombBeenPlacedInSmallStructure(field)){
                for(int row = 2; row < 4; row++){
                    for(int col = 2; col < 4; col++) {
                        field[row][col] = Utility.TERRAIN_UNIT;
                        isBombPlanted = false;
                        bombCount = 0;
                        isSmallBuildingDestroyed = true;
                    }
                }
            }

            if(hasBombBeenPlacedInMediumStructure(field)) {
                for (int row = 6; row < 8; row++) {
                    for (int col = 9; col < 12; col++) {
                        if (row == bombRowTemp && col == bombColTemp) {

                            field[row][col] = Utility.TERRAIN_UNIT;
                            isBombPlanted = false;
                            bombCount = 0;
                            isMediumStructurePlantedBomb = true;
                        }

                        if (field[6][9].equals(Utility.TERRAIN_UNIT) && field[7][11].equals(Utility.TERRAIN_UNIT)) {

                            for (row = 6; row < 8; row++) {
                                for (col = 9; col < 12; col++) {
                                    field[row][col] = Utility.TERRAIN_UNIT;
                                    isBombPlanted = false;
                                    bombCount = 0;
                                    isMediumBuildingDestroyed = true;
                                }
                            }
                        }

                        if (field[7][9].equals(Utility.TERRAIN_UNIT) && field[6][11].equals(Utility.TERRAIN_UNIT)) {

                            for (row = 6; row < 8; row++) {
                                for (col = 9; col < 12; col++) {
                                    field[row][col] = Utility.TERRAIN_UNIT;
                                    isBombPlanted = false;
                                    bombCount = 0;
                                    isMediumBuildingDestroyed = true;
                                }
                            }
                        }

                    }
                }
            }

            if(hasBombBeenPlacedInLargeStructure(field) == true){
                for (int row = 8; row < 11; row++) {
                    for (int col = 2; col < 5; col++) {
                        if (row == bombRowTemp && col == bombColTemp) {
                            field[row][col] = Utility.TERRAIN_UNIT;
                            isBombPlanted = false;
                            bombCount = 0;
                            isLargeStructurePlantedBomb = true;
                        }
                        if(field[8][2].equals(Utility.TERRAIN_UNIT) && field[8][4].equals(Utility.TERRAIN_UNIT) &&
                                field[10][2].equals(Utility.TERRAIN_UNIT) && field[10][4].equals(Utility.TERRAIN_UNIT) &&
                                field[9][3].equals(Utility.TERRAIN_UNIT)) {

                            for (row = 8; row < 11; row++) {
                                for (col = 2; col < 5; col++) {
                                    field[row][col] = Utility.TERRAIN_UNIT;
                                    isBombPlanted = false;
                                    bombCount = 0;
                                    isLargeBuildingDestroyed = true;
                                }
                            }

                        }
                    }
                }
            }

            field[bombRowTemp][bombColTemp] = Utility.TERRAIN_UNIT;
            isBombPlanted = false;
            bombCount = 0;
        }
        bombCount++;

        return field;
    }

}


