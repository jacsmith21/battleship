/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-10.
 */
public class Ship {

    private String name;
    private Coordinate[] coordinates;
    private boolean state;

    public Ship(String[] coords){

        name = coords[0];
        coordinates = new Coordinate[coords.length - 1];

        for(int i = 0; i < coords.length - 1; i++){

            coordinates[i] = new Coordinate(coords[i+1]);

        }

    }

    public String getName(){
        return name;
    }

    public boolean checkIfHit(String torpedo){

        boolean result = false;

        for(int i = 0; i < coordinates.length; i++){
            if(coordinates[i].checkIfHit(torpedo)){
                result = true;
                break;
            }
        }
        return result;

    }

    public boolean checkIfSunk(){

        boolean result = true;

        for(int i = 0; i < coordinates.length; i++){

            if(!coordinates[i].isHit()){
                result = false;
                break;
            }

        }

        if(result){
            updateState();
        }

        return result;

    }

    private void updateState(){
        state = false;
    }

}
