package model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.RandomAccess;

public abstract class Game {

    private String referee;
    private ArrayList<String> participantList; // for recording the athletes' id
    private static int scount = 0, ccount = 0, rcount = 0;
    private String gameType;
    private String gameID;
    private boolean[] flgType = new boolean[] {true, true, true};
    private ArrayList<Result> results;


    public Game(String referee, String gameType)
    {
        this.referee = referee;
        this.createGameType(gameType);
        this.gameID = this.createGameID();
        this.participantList = new ArrayList<>();
        results = new ArrayList<>();

    }

    public void createGameType(String gameType)
    {
    	
        
        
        if(gameType.equalsIgnoreCase("s"))
        {
        	scount = initGameId("file/scount.oid");
            this.gameType = "s";
            scount++;
            flgType[0] = false;
        }
        else if(gameType.equalsIgnoreCase("r"))
        {
        	rcount = initGameId("file/rcount.oid");
            this.gameType = "r";
            rcount++;
            flgType[1] = false;
        }
        else if(gameType.equalsIgnoreCase("c"))
        {
        	ccount = initGameId("file/ccount.oid");
            this.gameType = "c";
            ccount++;
            flgType[2] = false;
        }
    }

    private int initGameId(String filename) {
    	int i = 0;
    	try{
	    	FileReader file = new FileReader(filename);
			BufferedReader br = new BufferedReader(file);
			String s = br.readLine().trim();
			i = Integer.parseInt(s);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
		return i;
    }
    
    private void overwriteGameId(String filename,int num){
    	try {
    		File files =new File(filename);
    		FileWriter fileWriter =new FileWriter(files);
    		fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
    		
			RandomAccessFile file = new RandomAccessFile(filename,"rw");
//			file.writeInt(num);
			FileWriter writer = new FileWriter(filename, false); 
			file.writeBytes(String.valueOf(num));
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public String createGameID()
    {
        String id = null;
    	
//        FileReader file = new FileReader("file/data.txt");
//		BufferedReader br = new BufferedReader(file);
//		String s = "";
//		while((s = br.readLine() ) != null ){
        
        for(int i = 0; i < flgType.length; i++)
        {
            if(!flgType[i])
            {
                if( i == 0)
                {
                   overwriteGameId("file/scount.oid",scount);
                   return "sg" + scount;
                }else if(i == 1)
                {
                   overwriteGameId("file/rcount.oid",rcount);
                   return "rg" + rcount;
                }
                else if(i == 2)
                {
                   overwriteGameId("file/ccount.oid",ccount);
                   return "cg" + ccount;
                }
            }
        }
        
        

        return id;
    }


    public void addParticipant(String id, int result, int point)
    {
        this.participantList.add("AthleteID: "+ id +"\tPoint: "+ point);
        results.add(new Result(id, result)); //add id and result in results array list for sorting them
    }


    public String getParticipantIform(int index)
    {
        return participantList.get(index);
    }

    public ArrayList<String> getParticipantList()
    {
        return participantList;
    }

    /*
    * the compare method is try to compare the result of each participant and sort them in ascending order*/
    public void compare()
    {
        Collections.sort(results, new ResultComparator());
    }

    public ArrayList<Result> getResults()
    {
        return this.results;
    }

    public String getGameID()
    {
        return gameID;
    }

    public String getGameType()
    {
        return gameType;
    }

    public String getReferee()
    {
        return referee;
    }

    public void setReferee(String referee)
    {
        this.referee = referee;
    }
}