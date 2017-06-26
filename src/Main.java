/**
 * Created by kornel on 25.06.17.
 */

import model.Game;
import view.Printer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String BLINK = "\u001B[4m";

    public static void main(String[] args) {

        String serverName = "127.0.0.1";
        int port = 6666;
        final int ROWS = 3;
        final int COLS = 3;

        Printer myPrinter = new Printer();
        Game game = new Game(ROWS, COLS);
        Scanner scanner = new Scanner(System.in);


        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();

            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeBytes("Hello from " + client.getLocalSocketAddress() + "\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            boolean myTurn = true;
            int tmpInt;


            System.out.println("Server says: " + in.readLine());
            welcomeMessage();
            myPrinter.printSampleGameBoardWithNumbers(3, 3);


            String previousAnswerFromServerAsAString = "";
            String answerFromServerAsAString;

            while (true) {
                answerFromServerAsAString = in.readLine();


                if  (answerFromServerAsAString != "1" || answerFromServerAsAString != "2" || answerFromServerAsAString != "3")
                {
                    if (answerFromServerAsAString.equals(previousAnswerFromServerAsAString)) {
                        myPrinter.printMessageToPlyerOnConsole(2);
                        tmpInt = scanner.nextInt();             //scanning for answer
                        out.write(tmpInt);              //push it to server
                    }

                    else {
                        myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString)); //prints table

                        if (myTurn) {
                            myPrinter.printMessageToPlyerOnConsole(2);          //prints Player O:
                            out.write(scanner.nextInt());
                            myTurn = false;
                            previousAnswerFromServerAsAString  = answerFromServerAsAString;
                        }

                        else {
                            answerFromServerAsAString = in.readLine();
                            myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
                            myTurn = true;
                            previousAnswerFromServerAsAString=answerFromServerAsAString;
                            myPrinter.printMessageToPlyerOnConsole(2);
                            out.write(scanner.nextInt());


                        }
                    }
                }
                else  {
                    System.out.println("End of game!");
                    client.close();
                }
//                    if (answerFromServerAsAString.equals(previousAnswerFromServerAsAString)) {
//                        myPrinter.printMessageToPlyerOnConsole(2);
//                        tmpInt = scanner.nextInt();             //scanning for answer
//                        out.write(tmpInt);              //push it to server
//                    }

//                    else if (answerFromServerAsAString == "1" || answerFromServerAsAString == "2" || answerFromServerAsAString == "3"){
//                        answerFromServerAsAString = in.readLine();
//                        System.out.println("End of game!");
//                        myPrinter.printInfo(in.read());
//                        client.close();
//                    }

//                    else
//                        {
//                        myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString)); //prints table
//                        if (myTurn) {
//                            myPrinter.printMessageToPlyerOnConsole(2);          //prints Player O:
//                            tmpInt = scanner.nextInt();
//                            out.write(tmpInt);
//                            myTurn = false;
//                            previousAnswerFromServerAsAString = answerFromServerAsAString;
//                            answerFromServerAsAString = in.readLine();
//                            myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
//                        }
//                        else
//                        {
//                            answerFromServerAsAString = in.readLine();
//                            myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
//                            myTurn = true;
//                            previousAnswerFromServerAsAString = answerFromServerAsAString;
//                            myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
//                        }
//                    }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }






    public static int[] parseStringToIntTable(String s) {
        int[] tmpTable = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            tmpTable[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return tmpTable;
    }





    public static String stringToReturn(int number, int[] table, int playerId){
        for (int i = 0; i < table.length; i++) {
            if (table[i] == number-1)
                table[i] = playerId;
        }

        String stringToRetun = "";
        for (int i = 0; i < table.length; i++) {
            stringToRetun += table[i];
        }

        return stringToRetun;
    }





    public static int parseStringToInt(String receivedNumber) {

        return Integer.parseInt(receivedNumber);
    }







    public static void welcomeMessage() {
        System.out.println(ANSI_RED + "Hello in TicTacToe game!" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_CYAN + "Let's start the game!" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_RED + "Here is board with numbers." + ANSI_RESET);
    }
}
