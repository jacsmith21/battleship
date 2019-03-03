import java.util.Date;

/**
 * Created by Tris10 on 2017-03-17.
 */
public class Player {

    private Date date;
    private String name;
    private int score;

    public Player(Date date, String name, int score){
        this.date = date;
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return name;
    }

    public Date getDate(){
        return date;
    }

    public int getScore(){
        return score;
    }
}
