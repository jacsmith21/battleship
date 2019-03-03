import java.util.ArrayList;

/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class Board {

    private Ship[] ships;

    public Board(ArrayList<String[]> locations){

        ships = new Ship[locations.size()];

        for(int i = 0; i < ships.length; i++){
            ships[i] = new Ship(locations.get(i));
        }

    }

    public String checkIfHit(String torpedo){

        String result = "miss";

        for(int i = 0; i < ships.length; i++){
            if(ships[i].checkIfHit(torpedo)){
                result = "hit";
                if(ships[i].checkIfSunk()){
                    result = "sunk " + ships[i].getName();
                    if(checkIfLoss()){
                        result = "sunk " + ships[i].getName() + ",win";
                    }
                }
                break;
            }
        }

        return result;

    }

    public boolean checkIfLoss(){

        boolean result = true;

        for(int i = 0; i < ships.length; i++){
            if(!ships[i].checkIfSunk()){
                result = false;
                break;
            }
        }

        return result;

    }

}
