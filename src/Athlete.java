public class Athlete {
    private String athleteID;
    private String name;
    private String type;
    private int point;

    public Athlete(String athleteID, String name, String type){
        this.athleteID = athleteID;
        this.name = name;
        this.type =type;
        this.point = 0;
    }

    public String getAthleteID() {
        return athleteID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPoint() {
        return point;
    }
}
