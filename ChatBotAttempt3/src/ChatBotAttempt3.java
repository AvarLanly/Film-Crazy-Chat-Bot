import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;


public class ChatBotAttempt3 {


    //Record to store rankings.
    public static class Ranking{

        String firstPlace;
        String secondPlace;
        String thirdPlace;
    }



    //Method to capitalise every first letter of a word in an input (includes - and space)
    public static String capitaliseInput(String input){

        char[] sentenceAsArray = input.toCharArray();
        
        //Stringbuilder object to create a string from the character array.
        StringBuilder sentenceRebuilt = new StringBuilder();
        String upperCasedInput;
        int counter;
        char letter;

        //First letter always capitalised.
        letter = sentenceAsArray[0];
        if(Character.isAlphabetic(letter)){

            letter = Character.toUpperCase(letter);
        }
        sentenceRebuilt.append(letter);
                

        //For loop to go through the rest of the array and check if any letters should be capitalised.
        for(counter = 1; counter < sentenceAsArray.length; counter++){

            letter = sentenceAsArray[counter];


            //Checks if character before the current one was a space or a dash
            if(sentenceAsArray[counter-1] == ' ' || sentenceAsArray[counter-1] == '-' ){

                //Checks if the character is alphabetic or not
                //Cannot capitalise a non-alphabetic character
                if(Character.isAlphabetic(letter)){

                    letter = Character.toUpperCase(letter);
                } 
            }

            sentenceRebuilt.append(letter);
        }

        upperCasedInput = sentenceRebuilt.toString();
            
        return upperCasedInput;

    }



    //Method to count the total number of lines in the file
    public static int countLines(File filePath) throws IOException {
        
        int lines = 0;
            
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            //While loop to loop through until there are no more lines to read.
            while (reader.readLine() != null) {

                lines++;
            }
        }
        return lines;
    }



    //Method to print empty line, so help space out wording in conversation
    public static void emptyLine(){

        System.out.println(" ");

        return;
    }



    // Method to Use Scanner object to return user input
    public static String inputString (String message){

        String userInput;

        Scanner scan = new Scanner(System.in);
        System.out.println(message);

        userInput = scan.nextLine();

        return userInput;
    }



    //Method to generate a random number with parameter upper bound
    public static int generateRandomNumber(int upperBound){

        int randomNumber;

        Random rand = new Random();
        randomNumber = rand.nextInt(upperBound);
        
        return randomNumber;
    }


    //Method that will choose if the response given will be good or bad based off chance
    //Returns a between 0 and 99 (50 50 chance)
    public static int pickGoodOrBadResponse(){

        int goodOrBadResponse;
        goodOrBadResponse = generateRandomNumber(100);

        return goodOrBadResponse;
    }


    //Method that will choose a random response after the type of response (good or bad) has already been chosen
    public static String pickResponse(int goodOrBadResponse) throws IOException{

        String response;
        String responseFile;

        if(goodOrBadResponse >= 50){

            responseFile = "botGoodResponses";
        }
        else{

            responseFile = "botBadResponses";
        }

        File file = new File("C:/Users/avarl/OneDrive/Uni Work/JavaProjects/ChatBotAttempt3/textFiles/"+responseFile+".txt");
        response = getRandomLine(file);

        return response;
    }


    // Method in which chatbot introduces itself and has basic conversation
    // with user. Returns name to be used in future methods.
    public static String introduction(){

        final String USERNAME;

        USERNAME = inputString("What is your name?: ");

        System.out.println("Its nice to meet you " + USERNAME + ".");
        emptyLine();

        return USERNAME;        
    }


    // Method where the general conversation with the chatbot will be held
    public static void generalConversation(String name) throws IOException{

        final String[] TOPICS = {"Favourite Movies", "Favourite Genres", "Favourite Actors", "Favourite Actresses", "Favourite Directors", "Favourite  Movie Series"};
        int topicChosen;
        String carryOnConvo = "null";

        
        //While loop keeps convo on with user until they decide they don't want to talk anymore
        while (!carryOnConvo.toLowerCase().equals("no")){

            carryOnConvo = inputString("Do you want to talk? (yes/no): ");

            if (carryOnConvo.toLowerCase().equals("yes")) {

                topicChosen = findConversationTopic(name, TOPICS);
                loadConversationTopic(topicChosen, name, TOPICS);
            }
            else if(!carryOnConvo.toLowerCase().equals("yes") && !carryOnConvo.toLowerCase().equals("no")){

                System.out.println("Please enter yes or no.");
            }
            
        }

        return;
    }


    // Method to find out which conversation the user wants to talk about with the bot
    public static int findConversationTopic(String name, String[] topics){
        
        String userTopicAnswer = "null";
        int counter = 0;


        System.out.println("So " + name + ", what topic do you wish to talk about?");


        // While loop to ensure the bot finds out what topic user wants to talk about, as we don't know how many times we need to loop
        while(!userTopicAnswer.equals("yes")){

            
            //Soft reset on loop in case user doesnt pick a topic by resetting counter to 0
            if(counter == topics.length){

                emptyLine();
                System.out.println("You have not said yes to any topic, I shall ask again.");
                counter = 0;
            }

            emptyLine();
            userTopicAnswer = inputString("Do you wish to talk about your " + topics[counter] + "? (yes/no): ");

            //If user says no, increment counter and go again.
            if (userTopicAnswer.equals("no")){

                counter++;
            }
            //Validation input - if user doesn't say yes or no then will ask again until it does.
            else if (!userTopicAnswer.equals("yes") && !userTopicAnswer.equals("no")){

                emptyLine();
                System.out.println("Please enter a valid input (yes/no).");
            }     

        }
        return counter;
    }


    //Method to take the values in the file and copy them to the ranking record
    public static Ranking copyRankingFileToRecord(File filePath, Ranking botRanking) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        
        botRanking = setRankFirstPlace(botRanking, reader.readLine());
        botRanking = setRankSecondPlace(botRanking, reader.readLine());
        botRanking = setRankThirdPlace(botRanking, reader.readLine());

        reader.close();

        return botRanking;

    }


    //Method to take the values in the record and override the file with these new values
    //Essentially it updates the file at the end to a new ranking if the bot has made any changes
    //Allows for continouous conversation in which the user can fully influence the bot forever
    public static void copyRecordToRankingFile(File filePath, Ranking botRanking) throws IOException{

        PrintWriter writer = new PrintWriter( new FileWriter(filePath));

        String firstPlace = getFirstPlace(botRanking);
        String secondPlace = getSecondPlace(botRanking);
        String thirdPlace = getThirdPlace(botRanking);

        writer.println(firstPlace);
        writer.println(secondPlace);
        writer.println(thirdPlace);

        writer.close();

        return;
    }



    //Method to give create record and put in "empty" intial values
    public static Ranking createRanking(){

        Ranking ranking = new Ranking();

        ranking.firstPlace = "";
        ranking.secondPlace = "";
        ranking.thirdPlace = "";

        return ranking;
    }


    // Method to call upon conversation method based off user's choice.
    public static void loadConversationTopic(int choice, String name, String[] topic) throws IOException{

        String topicChosen; 
        Ranking botOGRanking = createRanking();

        emptyLine();
        System.out.println("So, " + name + " you have made your choice.");
        System.out.println("We will talk about your " + topic[choice] + ".");


        //If statement to decide which text file should be used for the topic.
        if(choice == 0){

            topicChosen = "botFavMovies";
        }
        else if(choice == 1){

            topicChosen = "botFavGenres";
        }
        else if (choice == 2){

            topicChosen = "botFavActors";
        }
        else if(choice == 3){

            topicChosen = "botFavActresses";
        }

        else if(choice == 4){

            topicChosen = "botFavDirectors";
        }
        else{

            topicChosen = "botFavMovieSeries";
        }

        File file = new File("C:/Users/avarl/OneDrive/Uni Work/JavaProjects/ChatBotAttempt3/Rankings/"+topicChosen+".txt");
        botOGRanking = copyRankingFileToRecord(file, botOGRanking);

        rankingConversation(name, botOGRanking, choice+1, file, topic);

        return;
    }



    //Method to copy over the bot's og ranking into the bot's changed ranking
    public static Ranking fillInBotChangedRanking(Ranking botOGRanking){

        Ranking botChangedRanking = createRanking();

        String OGFirstPlace = getFirstPlace(botOGRanking);
        String OGSecondPlace = getSecondPlace(botOGRanking);
        String OGThirdPlace = getThirdPlace(botOGRanking);

        botChangedRanking = setRankFirstPlace(botChangedRanking, OGFirstPlace);
        botChangedRanking = setRankSecondPlace(botChangedRanking, OGSecondPlace);
        botChangedRanking = setRankThirdPlace(botChangedRanking, OGThirdPlace);

        return botChangedRanking;
    }



    //Method in which bot and user have a conversation about their rankings on a topic
    //User has to convince bot to change his ranking
    public static void rankingConversation(String name, Ranking botOGRanking, int topic, File file, String[] topics) throws IOException{

        //Create the other 2 ranking records
        //BotChanged and User
        Ranking botChangedRanking = fillInBotChangedRanking(botOGRanking);
        Ranking userRanking = createRanking();
        
        //Show off the original ranking from the bot.
        emptyLine();
        System.out.println("My " + topics[topic-1] + ": ");
        showRanking(botOGRanking);

        //Ask for user ranking
        askRanking(botChangedRanking, userRanking, topic);
        
        //Show off user ranking, and both bot rankings.
        emptyLine();
        System.out.println("Your Ranking");
        showRanking(userRanking);
        beforeAndAfterRankings(botOGRanking, botChangedRanking);


        //Update file with the bot's new ranking.
        copyRecordToRankingFile(file, botChangedRanking);
        
        return;
    }
    

    //Method to set a value to the first place value of the record
    public static Ranking setRankFirstPlace(Ranking ranking, String firstPlace){

        firstPlace = capitaliseInput(firstPlace);
        ranking.firstPlace = firstPlace;

        return ranking;
    }


    //Method to set a value to the second place value of the record
    public static Ranking setRankSecondPlace(Ranking ranking, String secondPlace){

        secondPlace = capitaliseInput(secondPlace);
        ranking.secondPlace = secondPlace;

        return ranking;
    }


    //Method to set a value to the third place value of the record
    public static Ranking setRankThirdPlace(Ranking ranking, String thirdPlace){

        thirdPlace = capitaliseInput(thirdPlace);
        ranking.thirdPlace = thirdPlace;

        return ranking;
    }



    //Method to get the first place value in the record
    public static String getFirstPlace(Ranking rank){

        return rank.firstPlace;
    }


    //Method to get the second place value in the record
    public static String getSecondPlace(Ranking rank){

        return rank.secondPlace;
    }


    //Method to get the third place value in the record
    public static String getThirdPlace(Ranking rank){

        return rank.thirdPlace;
    }



    //Method to check for any duplicates in the user ranking.
    //Ensures that every entry in the user ranking record is unique.
    public static boolean checkForDuplicates(Ranking ranking, int position){

        String firstPlace = getFirstPlace(ranking);
        String secondPlace = getSecondPlace(ranking);
        String thirdPlace = getThirdPlace(ranking);

        //First check is when second place is written (can't have duplicates at only first place)
        if (position == 1){

            //Compare second and first place
            if(secondPlace.equals(firstPlace)){

                System.out.println("You Have Entered A Duplicate.");

                return true;
            }

            else{

                return false;
            }
        }

        else{

            //Compare third and second place
            if(thirdPlace.equals(secondPlace)){

                System.out.println("You Have Entered A Duplicate.");

                return true;
            }

            //Compare third and first place
            else if(thirdPlace.equals(firstPlace)){

                System.out.println("You Have Entered A Duplicate.");

                return true;
            }

            else{

                return false;
            }
        }
    }


    //Method that will ask why the user has this ranking
    public static Ranking askWhyRanking(Ranking botRanking, String userInput, int topic, int counter) throws IOException{

        boolean answersMatch;

        answersMatch = checkIfBotAndUserRankingsMatch(botRanking, userInput, counter);

        //Only ask questions and try to change bot ranking if the answers don't match
        if(!answersMatch){

            askQuestionsOnAnswer(topic);
            botRanking = changeBotRanking(botRanking, userInput, counter);
        }

        return botRanking;
    }
    
    
    //Method that asks user for their ranking, and why, as well as attemtping to convince bot to change ranking
    public static void askRanking(Ranking botRanking, Ranking userRankingInputs, int topic) throws IOException{
        

        //Set second and third place strings to "null" as they are assigned values in the while loop
        //Values will not be null when passed through the ranking record, merely acts as a placeholder
        int position = 0;
        String userRankingFirst;
        String userRankingSecond = "null";
        String userRankingThird = "null";
        boolean duplicate = true;

        emptyLine();
        System.out.println("Provide me with your own personal ranking and try to change my opinion.");


        //Input and compare first place between user and bot
        userRankingFirst = inputString("1st: ");
        userRankingInputs = setRankFirstPlace(userRankingInputs, userRankingFirst);
        botRanking = askWhyRanking(botRanking, userRankingFirst, topic, position);
        position++;


        //Input and compare second place between user and bot
        while (duplicate){

            userRankingSecond = inputString("2nd: ");
            userRankingInputs = setRankSecondPlace(userRankingInputs, userRankingSecond);
            duplicate = checkForDuplicates(userRankingInputs, position);
        }    

        botRanking = askWhyRanking(botRanking, userRankingSecond, topic, position);
        position++;


        duplicate = true;


        //Input and compare third place between bot and user
        while(duplicate){

            userRankingThird = inputString("3rd: ");
            userRankingInputs = setRankThirdPlace(userRankingInputs, userRankingThird);
            duplicate = checkForDuplicates(userRankingInputs, position);

        }

        botRanking = askWhyRanking(botRanking, userRankingThird, topic, position);
        

        return;
    }

    

    //Method that will randomly choose to change bot ranking to match user input, with a response
    //Alternatively, the method may also swap ranks around based off the user input.
    public static Ranking changeBotRanking(Ranking botRanking, String userInput,int rankingPosition) throws IOException{
        
        int typeOfResponse;
        String actualResponse;
        String temporarySwap;

        String botFirstPlace;
        String botSecondPlace;
        String botThirdPlace;

        //Decide what type of response to give and output it.
        typeOfResponse = pickGoodOrBadResponse();
        actualResponse = pickResponse(typeOfResponse);
        System.out.println(actualResponse);

        userInput = capitaliseInput(userInput);

        //Only carry out changes if the response is positive.
        if(typeOfResponse >= 50){


            botFirstPlace = getFirstPlace(botRanking);
            botSecondPlace = getSecondPlace(botRanking);
            botThirdPlace = getThirdPlace(botRanking);

            //Check if the user's input already exists in the bot's first place
            //If true, then swaps will occur.
            if (botFirstPlace.equals(userInput)) {
                
                //Swap 1st and 2nd
                if (rankingPosition == 1) {

                    temporarySwap = botSecondPlace;
                    botRanking = setRankSecondPlace(botRanking, userInput);
                    botRanking = setRankFirstPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my first and second place.");

                } 

                //Swap 1st and 3rd
                else if (rankingPosition == 2) {

                    temporarySwap = botThirdPlace;
                    botRanking = setRankThirdPlace(botRanking, userInput);
                    botRanking = setRankFirstPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my first and third place.");
                }
            } 

            //Checks if the user's input already exists in the bot's second place
            else if (botSecondPlace.equals(userInput)) {

                //Swap 2nd and 1st
                if (rankingPosition == 0) { 

                    temporarySwap = botFirstPlace;
                    botRanking = setRankFirstPlace(botRanking, userInput);
                    botRanking = setRankSecondPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my second and first place.");
                } 

                //Swap 2nd and 3rd
                else if (rankingPosition == 2) { 

                    temporarySwap = botThirdPlace;
                    botRanking = setRankThirdPlace(botRanking, userInput);
                    botRanking = setRankSecondPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my second and 3rd place.");
                }
            } 

            //Checks if the user's input already exists in the bot's third place
            else if (botThirdPlace.equals(userInput)) {

                //Swap 3rd and 1st
                if (rankingPosition == 0) { 
                
                    temporarySwap = botFirstPlace;
                    botRanking = setRankFirstPlace(botRanking, userInput);
                    botRanking = setRankThirdPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my third and first place.");

                } 
                
                //Swap 3rd and 2nd
                else if (rankingPosition == 1) { 

                    temporarySwap = botSecondPlace;
                    botRanking = setRankSecondPlace(botRanking, userInput);
                    botRanking = setRankThirdPlace(botRanking, temporarySwap);

                    System.out.println("You've managed to make me swap my third and second place.");
                }
            }

            //If userinput is entirely new and different to the values in the bot rankings.
            else{

                //If statements to check which ranking the function should change
                if(rankingPosition == 0){

                    botRanking = setRankFirstPlace(botRanking, userInput);
                }

                if(rankingPosition == 1){

                    botRanking = setRankSecondPlace(botRanking, userInput);
                }

                if(rankingPosition == 2){

                    botRanking = setRankThirdPlace(botRanking, userInput);
                }
            }
        }
            
        return botRanking;
    }



    //Method to check if user and bot have the same thing in the same position in their own rankings
    public static boolean checkIfBotAndUserRankingsMatch(Ranking botRanking, String input, int rankingPosition){

        String firstPlace;
        String secondPlace;
        String thirdPlace;

        if(rankingPosition == 0){

            firstPlace = getFirstPlace(botRanking);

            //Again, if statements used to check where the function should compare.
            if(firstPlace.toLowerCase().equals(input.toLowerCase())){

                System.out.println("Wow we put the same thing here.");

                return true;
            }
            else{
                
                return false;
            }
        }

        else if(rankingPosition == 1){

            secondPlace = getSecondPlace(botRanking);

            if(secondPlace.toLowerCase().equals(input.toLowerCase())){

                System.out.println("Wow we put the same thing here.");

                return true;
            }
            else{

                return false;
            }

        }

        else{

            thirdPlace = getThirdPlace(botRanking);

            if(thirdPlace.toLowerCase().equals(input.toLowerCase())){

                System.out.println("Wow we put the same thing here.");

                return true;
            }

            else{ 

                return false;
            }
        }

    }


    //Method to retrieve a specific question by line number (passed as parameter)
    public static String getRandomLine(File filePath) throws IOException {

        int lines = countLines(filePath);
        int randomLine = generateRandomNumber(lines);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            for (int i = 0; i < randomLine; i++) {
        
                reader.readLine();
                
            }
            //Returns the question
            return reader.readLine(); 
        }
    }


    
    //Method to ask the user a question on to explain why they gave their answer.
    public static void askQuestionsOnAnswer(int topic) throws IOException{

        String questionToAsk;
        String userResponse;
        String fileName = "null";

        emptyLine();


       //If statements determine which textfile should be used.
        if (topic == 1){

            fileName = "questionsOnFavMovie";

        }
        else if(topic == 2){

            fileName = "questionsOnFavGenre";
           
        }
        else if(topic == 3){

            fileName = "questionsOnFavActor";
            
        }
        else if(topic == 4){

            fileName = "questionsOnFavActress";

        }
        else if(topic == 5){

            fileName = "questionsOnFavDirector";

        }
        else if(topic == 6){

            fileName = "questionsOnFavMovieSeries";
        
        }

        //Have user input a reason despite no further action on the userResponse variable
        //Makes it seem as if chatbot has a certain level of intelligence by responding to user input, despite input not influencing the output.

        File file = new File("C:/Users/avarl/OneDrive/Uni Work/JavaProjects/ChatBotAttempt3/textFiles/"+fileName+".txt");

        questionToAsk = getRandomLine(file);
        userResponse = inputString(questionToAsk); 

        return;

    }



    //Method that outputs a ranking
    public static void showRanking(Ranking ranking){

        String firstPlace = getFirstPlace(ranking);
        String secondPlace = getSecondPlace(ranking);
        String thirdPlace = getThirdPlace(ranking);

        emptyLine();

        System.out.println("1. " + firstPlace);
        System.out.println("2. " + secondPlace);
        System.out.println("3. " + thirdPlace);

        return;
    }



    //Method that outputs the original and changed (if there are any changes) rankings from the bot
    public static void beforeAndAfterRankings (Ranking botOGRankings, Ranking botChangedRankings) {

        int differences;

        emptyLine();

        //Show original ranking
        System.out.println("This is my original ranking:");
        showRanking(botOGRankings);

        emptyLine();

        //Show changed ranking
        System.out.println("This is my changed ranking");
        showRanking(botChangedRankings);


        //Show number of differences
        emptyLine();
        differences = numberOfChangesInBotRanking(botOGRankings, botChangedRankings);

        if (differences == 1){

            System.out.println("You have made " + differences + " change to my ranking.");
        }
        else{

            System.out.println("You have made " + differences + " changes to my ranking.");
        }
        

        return;
    }


    //Method that calculates and returns the number of changes between the original and changed ranking of the bot.
    public static int numberOfChangesInBotRanking(Ranking botOGRankings, Ranking botChangedRankings){

        String OGFirstPlace = getFirstPlace(botOGRankings);
        String OGSecondPlace = getSecondPlace(botOGRankings);
        String OGThirdPlace = getThirdPlace(botOGRankings);

        String changedFirstPlace = getFirstPlace(botChangedRankings);
        String changedSecondPlace = getSecondPlace(botChangedRankings);
        String changedThirdPlace = getThirdPlace(botChangedRankings);

        int differences = 0;

        //If statements to go through each rank and compare
        if(!OGFirstPlace.equals(changedFirstPlace)){

            differences++;
        }
        if(!OGSecondPlace.equals(changedSecondPlace)){

            differences++;
        }
        if(!OGThirdPlace.equals(changedThirdPlace)){

            differences++;
        }

        return differences;
    }


    

    //Method that prints out a final goodbye statement to the user when the conversation ends.
    public static void outro(){

        System.out.println("Well that's all the time we got for today.");
        System.out.println("See you someday again!");

        return;
    }


    //Method that calls everything in order
    public static void totalPackage() throws IOException{

        String name = introduction();
        generalConversation(name);
        outro();

        return;
    }
    

    // Main method which calls upon total package method
    public static void main(String[] args) throws IOException {

        totalPackage();
        return;
    }
}
