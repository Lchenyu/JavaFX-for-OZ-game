public class Game {
    private String id;
    private String referee;

    public Game(){

    }

    public Game(String id, String referee){
        this.id = id;
        this.referee = referee;
    }

    public String getId() {
        return id;
    }

    public String getReferee() {
        return referee;
    }
}
