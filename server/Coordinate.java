/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class Coordinate {

    private String coordinate;
    private boolean state;

    public Coordinate(String coord) {

        coordinate = coord;
        state = false;

    }

    public boolean checkIfHit(String torpedo){

        if(coordinate.equals(torpedo)){
            state = true;
            return true;
        }
        else {
            return false;
        }

    }

    public boolean isHit(){
        return state;
    }

}
