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

            System.out.println("Server says: " + in.readLine());
            welcomeMessage();
            myPrinter.printSampleGameBoardWithNumbers(3, 3);

            String answerFromServerAsAString;

            int moveOfAPlayer = 1;
            boolean endOfGame = false;

            while (!endOfGame) {
                answerFromServerAsAString = in.readLine();
                try {
                    if (answerFromServerAsAString.length() == 1)
                        moveOfAPlayer = 0;
                } catch (NullPointerException e) {
                } //no need to do whatever with this exception

                switch (moveOfAPlayer) {

                    case 0: //draw or win
                        System.out.println("End of the game!");
                        try {
                            myPrinter.printInfo(Integer.parseInt(answerFromServerAsAString));
                        } catch (NumberFormatException e) {
                        } //no need to do whatever with this exception
                        client.close();
                        endOfGame = true;
                        break;


                    case 1: //player 2

                        myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
                        myPrinter.printMessageToPlyerOnConsole(2);
                        out.write(scanner.nextInt());
                        moveOfAPlayer = 2;
                        break;


                    case 2: //player 1
                        myPrinter.printTable(parseStringToIntTable(answerFromServerAsAString));
                        moveOfAPlayer = 1;
                        break;
                }
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


    public static void welcomeMessage() {
        System.out.println(ANSI_RED + "Hello in TicTacToe game!" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_CYAN + "Let's start the game!" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_RED + "Here is board with numbers." + ANSI_RESET);
    }
}
