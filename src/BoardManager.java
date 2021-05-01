public class BoardManager {

    static int unit1Row = 14;
    static int unit1Col = 11;
    static String unit1Index = "1";
    static int unit2Row = 14;
    static int unit2Col = 12;
    static String unit2Index = "2";
    static int unit3Row = 14;
    static int unit3Col = 13;
    static String unit3Index = "3";
    static int unit4Row = 14;
    static int unit4Col = 14;
    static String unit4Index = "4";
    public static String[] positionInQueue = new String[]{"1", "2", "3", "4"};

    public static String[][] bootstrapField() {
        String[][] field = new String[Utility.FIELD_NUMBER_OF_ROWS][Utility.FIELD_NUMBER_OF_COLS];
        field = generateBuildings(field);
        field = generatePlayableUnits(field);
        return field;
    }

    public static String[][] generateFreeSpace(String[][] field) {
        for (int row = 0; row < Utility.FIELD_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Utility.FIELD_NUMBER_OF_COLS; col++) {
                field[row][col] = Utility.TERRAIN_UNIT;
            }
        }
        return field;
    }

    public static String[][] generatePlayableUnits(String[][] field){

        field[unit1Row][unit1Col] = positionInQueue[0] + unit1Index;
        field[unit2Row][unit2Col] = positionInQueue[1] + unit2Index;
        field[unit3Row][unit3Col] = positionInQueue[2] + unit3Index;
        field[unit4Row][unit4Col] = positionInQueue[3] + unit4Index;
        return field;
    }

    public static String[][] renderField(String[][] field) {

        for (int row = 0; row < Utility.FIELD_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Utility.FIELD_NUMBER_OF_COLS; col++) {
                System.out.print(" " + field[row][col]);
            }
            System.out.println();
        }

        return field;
    }

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

       int largeBuildingPlacementRow =  8;
       int largeBuildingPlacementCol = 2;

       int largeBuildingGenerationEnd = largeBuildingPlacementRow+3;

        for (int row = largeBuildingPlacementRow; row < largeBuildingGenerationEnd; row++) {
            for (int col = largeBuildingPlacementCol; col < largeBuildingGenerationEnd-6; col++) {
                field[row][col] = Utility.LARGE_BUILDING_SQUARE_MARKER;
            }
        }
        return field;
    }
    public static String leaderPositionOrganizer(String [][] field, String leaderChoice){

        String currentLeader = unit1Index;
        String temp;

        if (leaderChoice.equals("1")) {
            temp = currentLeader;
            currentLeader = unit1Index;
            leaderChoice = temp;
        }

        if (leaderChoice.equals("2")){
            temp = currentLeader;
            currentLeader = unit2Index;
            leaderChoice = temp;

        }
        if (leaderChoice.equals("3")){

        }
        if (leaderChoice.equals("4")){

        }
        return currentLeader + "/" + leaderChoice;
    }

    public static  String[][] unitMovement(String[][] field, String directionOfMovement){

        String  leaderChoice = null;
        String currentLeader = unit1Index;

        int unit1CurrentRow = unit1Row;
        int unit1CurrentCol = unit1Col;

        int unit2CurrentRow = unit2Row;
        int unit2CurrentCol = unit2Col;

        int unit3CurrentRow = unit3Row;
        int unit3CurrentCol = unit3Col;

        if (directionOfMovement.equals("c")){
           currentLeader = leaderPositionOrganizer(field,leaderChoice);
          if (currentLeader == "1" && leaderChoice.charAt(0) == '2' ){
              field[unit1Row][unit1Col] = positionInQueue[0] + unit2Index;
          }
        }


        if (directionOfMovement.equals(Utility.DIRECTION_FORWARD)){
            if (unit1CurrentRow == 0) {
                unit1CurrentRow = 14;
            } else unit1CurrentRow--;
        }
        if (directionOfMovement.equals(Utility.DIRECTION_BACKWARDS)){
            if (unit1CurrentRow == 14){
                unit1CurrentRow = 0;
            } else unit1CurrentRow++;
        }
        if (directionOfMovement.equals(Utility.DIRECTION_LEFT)){
            if (unit1CurrentCol == 0){
                unit1CurrentCol = 14;
            } else unit1CurrentCol--;
        }

        if (directionOfMovement.equals(Utility.DIRECTION_RIGHT)){
            if(unit1CurrentCol == 14){
                unit1CurrentCol = 0;
            } else unit1CurrentCol++;
        }


        int tempRow = unit1Row;
        int tempCol = unit1Col;

        field[unit1Row][unit1Col] = positionInQueue[1]; // на мястото на 1 става 2
        field[unit2Row][unit2Col] = positionInQueue[2];
        field[unit3Row][unit3Col] = positionInQueue[3];
//        for (int row = 2; row < 4; row++) {
//            for (int col = 2; col < 4; col++) {
//                if (field[row][col].
//                        Utility.SMALL_BUILDING_SQUARE_MARKER;
//            }
//        }

        field[unit4Row][unit4Col] = Utility.TERRAIN_UNIT;


        unit1Row = unit1CurrentRow;   //ново място за 1
        unit1Col = unit1CurrentCol;

        unit2Row = tempRow;     //имах проблем с присвояването и това помогна
        unit2Col = tempCol;

        unit3Row = unit2CurrentRow;
        unit3Col = unit2CurrentCol;

        unit4Row = unit3CurrentRow;
        unit4Col = unit3CurrentCol;

//
//        String currentLeader;
        field[unit1Row][unit1Col] = positionInQueue[0];  // новото място става 1



        return field;
    }
}
