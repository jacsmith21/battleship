import java.util.ArrayList;

/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class Data {

    public static final int MIN_ROW = 1;
    public static final int MAX_ROW = 10;
    public static final char MIN_COL = 'A';
    public static final char MAX_COL = 'J';

    public static ArrayList<String[]> splitData(String input){

        String[] data = input.split(",");
        ArrayList<String[]> splitData = new ArrayList<>();

        for(int i = 0; i < data.length; i ++){
            String[] temp = data[i].split(" ");
            splitData.add(temp);
        }

        return splitData;
    }

    public static boolean validateData(ArrayList<String[]> input){
        boolean result = true;

        if(input.size() != 4){
            result = false;
        }

        for(int i = 0; i < input.size(); i++){
            String previous = "null";
            for(int j = 1; j < input.get(i).length; j++){
                if(!isValidCoordinate(input.get(i)[j])){
                    result = false;
                    break;
                }
                char column = input.get(i)[j].charAt(0);
                int row = Integer.parseInt(input.get(i)[j].substring(1));
                if(!previous.equals("null")){
                    char prevColumn = previous.charAt(0);
                    int prevRow = Integer.parseInt(previous.substring(1));
                    if(( (row == prevRow + 1) || (row == prevRow - 1) ) == ( (column == prevColumn + 1) || (column == prevColumn - 1) )){
                        result = false;
                    }
                }
                previous = input.get(i)[j];
            }
        }
        return result;
    }

    public static boolean isValidCoordinate(String input){
        boolean result = true;

        String col = input.substring(0,1);
        String r = input.substring(1);

        char column = col.charAt(0);
        int row = Integer.parseInt(r);

        if(column < MIN_COL || column > MAX_COL || row < MIN_ROW || row > MAX_ROW){
            result = false;
        }

        return result;
    }

}
