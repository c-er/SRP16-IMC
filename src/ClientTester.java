import srp16.imc.client.IMCClient;

import java.util.Scanner;

/**
 * Created by uday on 8/7/16.
 */
public class ClientTester {
    static Scanner sc = null;
    public static void main(String[] args) throws Exception {
        sc = new Scanner(System.in);
        boolean success = false;
        IMCClient client = new IMCClient("dummy");
        while(!success) {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            client.setName(name);
            System.out.println("Connecting . . .");
            success = client.connect();
            if(!success) {
                System.out.println("That name seems to be taken; try again.");
            }
            System.out.println();
        }
        System.out.println("Operations (interactive if arguments are required): ");
        System.out.println("c: get set of connected clients");
        System.out.println("s: send a message");
        System.out.println("g: get a message");
        System.out.println("d: disconnect and quit");
        System.out.println();

        while(true) {
            System.out.print("Enter operation: ");
            String op = sc.nextLine();
            if(op.equalsIgnoreCase("c")) {
                System.out.println(client.getClients());
                System.out.println();
            } else if(op.equalsIgnoreCase("s")) {
                System.out.print("To whom: ");
                String dest = sc.nextLine();
                System.out.println("Enter message: ");
                String mes = sc.nextLine();
                success = client.sendMessage(mes, dest);
                if(!success) {
                    System.out.println("\nMessage sending failed. Perhaps said client does not exist?\n");
                }
            } else if(op.equalsIgnoreCase("g")) {
                System.out.println(client.getMessage());
                System.out.println();
            } else if(op.equalsIgnoreCase("d")) {
                client.disconnect();
                System.out.println();
                break;
            } else {
                System.out.println("Unrecognized message");
            }
        }
    }
}
