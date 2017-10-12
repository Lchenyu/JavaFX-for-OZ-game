package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Cycling;
import model.Cyclist;
import model.Game;
import model.IDlist;
import model.Official;
import model.Person;
import model.Running;
import model.Sprinter;
import model.SuperAthlete;
import model.Swimmer;
import model.Swimming;

public class Driver
{
    private int gameType;
    private String predict;
    private int participantNum;
    private String type;
    private static int count = 0 , idListCount = 0;
    private ArrayList<Game> games = new ArrayList<>();
    private ArrayList<Person> swimmers = new ArrayList<>();
    private ArrayList<Person> runners = new ArrayList<>();
    private ArrayList<Person> cyclists = new ArrayList<>();
    private ArrayList<Person> superAthletes = new ArrayList<>();
    private ArrayList<Official> referees = new ArrayList<>();
    private int select;
    private boolean run = true;
    private String flg;
    private ArrayList<IDlist> idList = new ArrayList<>();

    private String firstID,secondID,thirdID;

    public Driver()
    {
    }

    public void select()
    {
        boolean input = false;
        do
        {
            try
            {
                Scanner key = new Scanner(System.in);
                this.select = key.nextInt();
                System.out.print("\n");
                if(this.select > 5)
                {
                    System.out.println("there is no option " + this.select + " \nPlease re-enter!");
                }else {
                    input = true;
                }
            }catch (InputMismatchException ime)
            {
                System.out.println("Please enter a number!");
            }
        }while (!input);
        switch (select)
        {
            case 1: //game type selection
                this.option1();
                break;
            case 2: // predict winner
                this.option2();
                break;
            case 3: // start game
                this.option3();
                break;
            case 4: // review history of all games
                this.printGameHistory();
                break;
            case 5: // display points of all athletes
                System.out.println("Olympic Game\n\tAthletes Information");
                System.out.println("========================");
                this.printAthletesList(select);
                break;
            case 0:
            	//****************************************************
            	overwriteGameId("file/result.txt"); // need changes**********************************************************
            	//***************************************************
                this.exit();
                break;
        }
    }

    
   
    public void option1()
    {
        this.printGameType();
        Scanner scan = new Scanner(System.in);
        boolean input = false;
        do
        {
            try   // check the input
            {
                int ty = scan.nextInt();
                if(ty <= 3 && ty > 0)
                {
                    this.setGameType(ty); // choose game type as a integer number
                    System.out.print("\n");
                    input = true;
                }else {
                    System.out.println("there is no option for " + ty);
                }
            }
            catch (InputMismatchException ime)
            {
                System.out.println("please enter a number");
            }
            scan.nextLine(); //clean scan and avoid from infinite loop
        }while (!input); // jump do-while loop when input is true
        this.initiateGame();
    }

    public void option2()
    {
        if(count == 0)
        {
            System.out.println("You need set game first!");
        }else
        {
            System.out.println("========================");
            System.out.println("Olympic Game\n\tWinner Prediction");
            System.out.println("========================");
            this.printAthletesList(select);
            this.setPredict();
            System.out.print("\n");
        }

    }

    public void option3()
    {

        System.out.println("Olympic Game");
        System.out.println("========================");
        System.out.println("Game Running...... ");
        this.firstGameCheck();
    }



    /*Check if a existing game for re-run, if not, the app cannot run star a game before setting*/
    public void firstGameCheck()
    {
        if(count > 0)
        {
            this.gameStart();
            System.out.println("Result:");
            this.printGameResult();
            System.out.print("\n");
        }else
        {
            System.out.println("\nGame Running Failed! \n >>>>>>>>>>>>>> Please setting game first\n");
        }
    }

    /*for option 0 to exit game*/
    public boolean exit()
    {
        if(select == 0)
        {
            return true;
        }
        else
            return false;
    }

    public void printMenu()
    {
        System.out.println("========================");
        System.out.println("Olympic Game");
        System.out.println("========================");
        System.out.println("1\tSelect a game");
        System.out.println("2\tPredict the winner of the game");
        System.out.println("3\tStart the game");
        System.out.println("4\tDisplay the final results of all games");
        System.out.println("5\tDisplay the points of all athletes");
        System.out.println("0\tExit");
        System.out.print("Please Enter Your Option: ");
    }


    public void printGameType()
    {
        System.out.println("Olympic Game\n\tGame Selection");
        System.out.println("========================");
        System.out.println("1\tSwimming");
        System.out.println("2\tRunning");
        System.out.println("3\tCycling");
        System.out.print("Please Enter Your Option: ");
    }



    /*
    * initiateGame :
    * is the method to select athletes and add them in the game
    * also it translate the integer game type to String
    *  1 for swimming as 's'
    *  2 for running as 'r'
    *  3 for cycling as 'c'
    *  those String values will help to create gameID which depends on their game type
    *  */

    public void initiateGame()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("========================");
        System.out.println("Olympic Game\n\tGame Selection >>>>>> Game Initiation");
        System.out.println("========================");
        System.out.println("How many athletes in this game? from 4 to 8");
        boolean input = false;
        do
        {
            try   // check the input
            {
                this.participantNum = scan.nextInt();
                if(this.participantNum >= 4  && this.participantNum <= 8)  //check the number of participant between 4 and 8
                {
                    input = true;
                }else
                {
                    System.out.println("please enter a number from 4 to 8");
                }
            }
            catch (InputMismatchException ime)
            {
                System.out.println("please enter a number");
            }
            scan.nextLine();        //clean scan and avoid from infinite loop
        }while (!input);            // jump do-while loop when input is true

        System.out.print("\n");
        System.out.println("Add Participants Randomly? Y/N ");
        this.matchType();           // match integer game type with String
        this.createGame();          // add new game in game array list
        this.addParticipant();      // add athletes in the new game

    }

    /*
    *  1 for swimming
    *  2 for running
    *  3 for cycling
    *  */
    public void matchType()
    {
        if(this.getGameType() == 1)
        {
            this.setType("s");
        }
        else if(this.getGameType() == 2)
        {
            this.setType("r");
        }
        else if(this.getGameType() == 3)
        {
            this.setType("c");
        }
    }


    public void createGame()
    {
        count = count + 1; // count for games array list, help to find correct index for current games
        // create new game according to game type
        if(getType().equalsIgnoreCase("s"))
        {
            this.games.add(new Swimming(referees.get(0).getId(),"s"));
        }
        else if(getType().equalsIgnoreCase("c"))
        {
            this.games.add(new Cycling(referees.get(2).getId(),"c"));
        }
        else if(getType().equalsIgnoreCase("r"))
        {
            this.games.add(new Running(referees.get(1).getId(),"r"));
        }
    }


    /*add athletes in the game*/
    public void addParticipant()
    {
        boolean addFlg = false;
        do
        {
            Scanner scan = new Scanner(System.in);
            flg = scan.nextLine();
            if(flg.equalsIgnoreCase("y") || flg.equalsIgnoreCase("Y"))
            {
                this.autoAdd();
                addFlg = true;
            } else if(flg.equalsIgnoreCase("n")|| flg.equalsIgnoreCase("N"))
            {
                System.out.println("Athletes ID list: ");
                switch (this.getGameType())   // print out every athletes who can join the games
                {
                    case 1:
                        for(int j = 0; j < swimmers.size(); j++)
                        {
                            System.out.println(swimmers.get(j).getId());
                        }
                        break;
                    case 2:
                        for(int j = 0; j < runners.size(); j++)
                        {
                            System.out.println(runners.get(j).getId());
                        }
                        break;
                    case 3:
                        for(int j = 0; j < cyclists.size(); j++)
                        {
                            System.out.println(cyclists.get(j).getId());
                        }
                        break;
                }
                for(int j = 0; j < superAthletes.size(); j++)
                {
                    System.out.println(superAthletes.get(j).getId());
                }
                addFlg = true;
                this.manualAdd();
            }else {
                System.out.println("Invalid Typing");
            }
        }while (!addFlg);
    }

    /*
    * add athletes automatically and the numbers dependent on */
    public void autoAdd()
    {
        switch (this.getGameType())
        {
            case 1:
                for(int i = 0; i < this.participantNum; i++)
                {
                    games.get(count - 1).addParticipant(swimmers.get(i).getId(),swimmers.get(i).compete(),swimmers.get(i).getPoints());
                }
                break;
            case 2:
                for(int i = 0; i < this.participantNum; i++)
                {
                    games.get(count - 1).addParticipant(runners.get(i).getId(),runners.get(i).compete(),runners.get(i).getPoints());
                }
                break;
            case 3:
                for(int i = 0; i < this.participantNum; i++)
                {
                    games.get(count - 1).addParticipant(cyclists.get(i).getId(),cyclists.get(i).compete(),cyclists.get(i).getPoints());
                }
                break;
        }

    }


    /*database is the place that store all athletes information*/
    public void database()
    {
//        swimmers.add(new Swimmer("s001","Joey", 20,"VIC"));
//        swimmers.add(new Swimmer("s002","Rocky", 16,"VIC"));
//        swimmers.add(new Swimmer("s003","Ricky", 24,"ACT  "));
//        swimmers.add(new Swimmer("s004","Jame", 22,"SA"));
//        swimmers.add(new Swimmer("s005","Haro", 21,"VIC"));
//        swimmers.add(new Swimmer("s006","Glonery", 19,"WA"));
//        swimmers.add(new Swimmer("s007","Roll", 18,"ACT"));
//        swimmers.add(new Swimmer("s008","Peach", 22,"TAS"));
//
//        runners.add(new Sprinter("r001","Joey", 20,"VIC"));
//        runners.add(new Sprinter("r002","Rocky", 16,"VIC"));
//        runners.add(new Sprinter("r003","Ricky", 24,"ACT  "));
//        runners.add(new Sprinter("r004","Jame", 22,"SA"));
//        runners.add(new Sprinter("r005","Haro", 21,"VIC"));
//        runners.add(new Sprinter("r006","Glonery", 19,"WA"));
//        runners.add(new Sprinter("r007","Roll", 18,"ACT"));
//        runners.add(new Sprinter("r008","Peach", 22,"TAS"));

    	FileReader file = null;
		try {
			file = new FileReader("file/data.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(file);

		String s = "";
		try {
			while((s = br.readLine() ) != null ){
					String[]  prop = s.split(",");
//				System.out.println(prop[0]);
//				Person  p = (Person) Class.forName(prop[0]).newInstance();
//				Class classType = Person.class;
//				Constructor<Person> pm = classType.getConstructor(String.class,  String.class,
//		                int.class,String.class);

					if ("Swimmer".equals(prop[0])){
						swimmers.add(new Swimmer(prop[1],prop[2], Integer.parseInt(prop[3]),prop[4]));
//					System.out.println(swimmer.getAge());
					}
					if ("Sprinter".equals(prop[0])){
						runners.add(new Sprinter(prop[1],prop[2], Integer.parseInt(prop[3]),prop[4]));
//					System.out.println(swimmer.getAge());
					}
					if ("Cyclist".equals(prop[0])){
						cyclists.add(new Cyclist(prop[1],prop[2], Integer.parseInt(prop[3]),prop[4]));
//					System.out.println(swimmer.getAge());
					}
					if ("SuperAthlete".equals(prop[0])){
						superAthletes.add(new SuperAthlete(prop[1], prop[2], Integer.parseInt(prop[3]),prop[4]));
//					System.out.println(swimmer.getAge());\
					}
					if ("Official".equals(prop[0])){
						referees.add(new Official(prop[1],prop[2]));
					}
				}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*
        cyclists.add(new Cyclist("c001","Joey", 20,"VIC"));
        cyclists.add(new Cyclist("c002","Rocky", 16,"VIC"));
        cyclists.add(new Cyclist("c003","Ricky", 24,"ACT  "));
        cyclists.add(new Cyclist("c004","Jame", 22,"SA"));
        cyclists.add(new Cyclist("c005","Haro", 21,"VIC"));
        cyclists.add(new Cyclist("c006","Glonery", 19,"WA"));
        cyclists.add(new Cyclist("c007","Roll", 18,"ACT"));
        cyclists.add(new Cyclist("c008","Peach", 22,"TAS"));

        superAthletes.add(new SuperAthlete("su001","Joey", 20,"VIC"));
        superAthletes.add(new SuperAthlete("su002","Rocky", 16,"VIC"));
        superAthletes.add(new SuperAthlete("su003","Haro", 21,"VIC"));
        superAthletes.add(new SuperAthlete("su004","Riadscky", 24,"ACT"));
        superAthletes.add(new SuperAthlete("su005","Rolqwel", 18,"ACT"));
        superAthletes.add(new SuperAthlete("su006","Hafgsro", 21,"VIC"));
        superAthletes.add(new SuperAthlete("su007","Ricasdfy", 24,"ACT"));
        superAthletes.add(new SuperAthlete("su008","lsqweoll", 18,"ACT"));

        referees.add(new Official("re001","Vincent"));
        referees.add(new Official("re002","Chritis"));
        referees.add(new Official("re003","Vincent"));
*/
    }


    public void manualAdd()
    {
        System.out.println("Please enter ID which you want to add");
        Scanner enter = new Scanner(System.in);
        String addID;
        idList.add(new IDlist());
        idListCount = idListCount + 1;
        for(int i = 0; i < participantNum; i++)
        {
            boolean input = false;
            do
            {
                ArrayList<Person> temp = new ArrayList<>();  // arraylist for temp use
                System.out.print("add ID: ");
                addID = enter.nextLine();
                switch (this.getGameType())
                {
                    case 1:
                        temp = swimmers;
                        break;
                    case 2:
                        temp = runners;
                        break;
                    case 3:
                        temp = cyclists;
                        break;
                }
                if(idList.get(idListCount - 1).getAddIDList().contains(addID))  // check duplication ID
                {
                    System.out.println("repeat ID");
                }else {
                    for(int k = 0; k < temp.size(); k++)
                    {

                        if(addID.equalsIgnoreCase(temp.get(k).getId()) || addID.equalsIgnoreCase(superAthletes.get(k).getId()))
                        {
                            idList.get(idListCount - 1).setAddIDList(addID); // store the ID which users added, and it will be used in re-run game
                            this.addAthletesByManual(addID);
                            input = true;
                        }
                    }
                }
                if(!input)
                    System.out.println("Your Input is Invalid, Please enter an ID");
            }while (!input);
        }
    }


    public void addAthletesByManual(String addID)
    {
        switch (this.getGameType())
        {
            case 1:
                this.addSwimmers(addID);
                break;
            case 2:
                this.addRunners(addID);
                break;
            case 3:
                this.addCyclists(addID);
                break;
        }
    }

    public void addSwimmers(String addID)
    {
        for(int k = 0; k < swimmers.size(); k++)
        {
            if(addID.equalsIgnoreCase(swimmers.get(k).getId()))
            {
                games.get(count - 1).addParticipant(swimmers.get(k).getId(),swimmers.get(k).compete(),swimmers.get(k).getPoints());
            }
        }
        for(int j = 0; j < superAthletes.size(); j++)
        {
            if(addID.equalsIgnoreCase(superAthletes.get(j).getId()))
            {
                SuperAthlete sa = (SuperAthlete)superAthletes.get(j);
                sa.setGameType(1);
                games.get(count - 1).addParticipant(superAthletes.get(j).getId(),superAthletes.get(j).compete(),superAthletes.get(j).getPoints());
            }
        }
    }

    public void addRunners(String addID)
    {
        for(int k = 0; k < runners.size(); k++)
        {
            if(addID.equalsIgnoreCase(runners.get(k).getId()))
            {

                games.get(count - 1).addParticipant(runners.get(k).getId(),runners.get(k).compete(),runners.get(k).getPoints());
            }
        }
        for(int j = 0; j < superAthletes.size(); j++)
        {
            if(addID.equalsIgnoreCase(superAthletes.get(j).getId()))
            {
                SuperAthlete sa = (SuperAthlete)superAthletes.get(j);
                sa.setGameType(2);
                games.get(count - 1).addParticipant(superAthletes.get(j).getId(),superAthletes.get(j).compete(),superAthletes.get(j).getPoints());
            }
        }
    }

    public void addCyclists(String addID)
    {
        for(int k = 0; k < cyclists.size(); k++)
        {
            if(addID.equalsIgnoreCase(cyclists.get(k).getId()))
            {
                games.get(count - 1).addParticipant(cyclists.get(k).getId(),cyclists.get(k).compete(),cyclists.get(k).getPoints());
            }
        }
        for(int j = 0; j < superAthletes.size(); j++)
        {
            if(addID.equalsIgnoreCase(superAthletes.get(j).getId()))
            {
                SuperAthlete sa = (SuperAthlete)superAthletes.get(j);
                sa.setGameType(3);
                games.get(count - 1).addParticipant(superAthletes.get(j).getId(),superAthletes.get(j).compete(),superAthletes.get(j).getPoints());
            }
        }
    }

    public void printAthletesList(int selection)
    {

        if(selection == 2) // list all athletes for this game
        {
            for(int i = 0; i < games.get(count - 1).getParticipantList().size(); i++)
            {
                System.out.println(games.get(count - 1).getParticipantIform(i));
            }
        }
        else if(selection == 5) // list all athletes with points
        {
            for(int i = 0; i < swimmers.size(); i++)
            {
                System.out.println("ID: "+swimmers.get(i).getId() +"\tPoint: " + swimmers.get(i).getPoints());
            }
            for(int i = 0; i < runners.size(); i++)
            {
                System.out.println("ID: "+runners.get(i).getId() +"\tPoint: " + runners.get(i).getPoints());
            }
            for(int i = 0; i < cyclists.size(); i++)
            {
                System.out.println("ID: "+cyclists.get(i).getId() +"\tPoint: " +cyclists.get(i).getPoints());
            }
            for(int i = 0; i < superAthletes.size(); i++)
            {
                System.out.println("ID: "+superAthletes.get(i).getId() +"\tPoint: " +superAthletes.get(i).getPoints());
            }
        }
    }

    public void printGameResult()
    {
        for(int i = 0, k = 0; i < participantNum; i++)
        {
            System.out.println("No."+ (++k)+ " " +"ID: " +games.get(count - 1).getResults().get(i).getId() + " " +"Result: " + games.get(count - 1).getResults().get(i).getRe());
        }


        if(this.getPredict() != null &&this.getPredict().equalsIgnoreCase(games.get(count - 1).getResults().get(0).getId()))
        {
            System.out.println("Congratulate! You Are Win!");
        }else
        {
            System.out.println("Sorry, You Are Loss!");
        }
    }

    public void gameStart()
    {
//        if(!run)  //for re-run the game
//        {
//            this.createGame();
//            if(flg.equalsIgnoreCase("y") || flg.equalsIgnoreCase("Y"))
//            {
//                this.autoAdd();
//            }
//            else if(flg.equalsIgnoreCase("n")|| flg.equalsIgnoreCase("N"))
//            {
//                for(int i = 0; i < idList.get(idListCount - 1 ).getAddIDList().size(); i++)
//                {
//                    this.addAthletesByManual(idList.get(idListCount - 1).getAddIDList().get(i));
//                }
//            }
//        }
        games.get(count - 1).compare(); //compare the result and rank sort them in right order

        this.firstID = games.get(count - 1).getResults().get(0).getId();
        this.secondID = games.get(count - 1).getResults().get(1).getId();
        this.thirdID = games.get(count - 1).getResults().get(2).getId();

        if(getGameType() == 1)
        {
            this.CalculatePoints(swimmers);
        }
        if(getGameType() == 2)
        {
            this.CalculatePoints(runners);
        }
        if(getGameType() == 3)
        {
            this.CalculatePoints(cyclists);
        }
        run = false; //for re-run the game
        
    }
//
    public void overwriteGameId(String filename)
    {
    	try(FileWriter fw = new FileWriter(filename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
		{
    		//appends the string to the file
			    for(int i = 0; i < games.size(); i++) 
			    {
			    	fw.write("Game: " + games.get(i).getGameID()+ "\t" + "Referee: "+ games.get(i).getReferee() + "\n");
		            for (int k = 0; k < games.get(i).getResults().size(); k++)
		            {
		            	fw.write("\tParticipant ID: " + games.get(i).getResults().get(k).getId() + "\t"+"Result: "+ games.get(i).getResults().get(k).getRe() + "\n");
		            }
		        }
		} catch (IOException e) {
				System.err.println(e);
		}
    }
//

    public void CalculatePoints(ArrayList<Person> temp)
    {
        for(int s = 0; s < temp.size(); s++)
        {
            if(this.firstID.equalsIgnoreCase(temp.get(s).getId()))
            {
                temp.get(s).setPoints(5);
            }

            if(this.secondID.equalsIgnoreCase(temp.get(s).getId()))
            {
                temp.get(s).setPoints(2);
            }
            if(this.thirdID.equalsIgnoreCase(temp.get(s).getId()))
            {
                temp.get(s).setPoints(1);
            }
        }
    }

    public void printGameHistory()
    {
        for(int i = 0; i < games.size(); i++) {
            System.out.println("Game: " + games.get(i).getGameID());
            for (int k = 0; k < games.get(i).getResults().size(); k++)
            {
                System.out.println("\tParticipant ID: " + games.get(i).getResults().get(k).getId() + "\t"+"Result: "+ games.get(i).getResults().get(k).getRe());
            }
            System.out.println("Referee: "+ games.get(i).getReferee());
        }
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getGameType()
    {
        return gameType;
    }

    public void setGameType(int gameType)
    {
        this.gameType = gameType;
    }

    public String getPredict()
    {
        return predict;
    }

    public void setPredict()
    {

        boolean input = false;
        do
        {
            System.out.println("Please enter ID for winner prediction: ");
            Scanner key = new Scanner(System.in);
            this.predict =  key.nextLine();
            System.out.println("\n");
            for(int i = 0; i < games.get(count - 1).getResults().size(); i++)
            {
                if(predict.equalsIgnoreCase(games.get(count - 1).getResults().get(i).getId()))
                {
                    input = true;
                }
            }
            if(!input)
            {
                System.out.println("Your input is not an Athlete's ID or the Athlete does not join in this game!\n");
            }
        }while (!input);

    }

	public ArrayList<Person> getSwimmers() {
		return swimmers;
	}

	public ArrayList<Person> getRunners() {
		return runners;
	}

	public ArrayList<Person> getCyclists() {
		return cyclists;
	}

	public ArrayList<Person> getSuperAthletes() {
		return superAthletes;
	}

	public ArrayList<Game> getGames() {
		return games;
	}















}
