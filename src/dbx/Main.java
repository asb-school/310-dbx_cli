/**
 * - Course: CPTR310 Database Application Programming
 * - Institution: Andrews University
 * - Semester: Fall 2011
 * - Instructor: Dr. Roy Villafane
 *
 * - Item: ?
 *
 * - Student: ?
 *
 * - Description: >
 *     Command line CRUD database application using Java.
 *     CRUD is create (C), read (R), update (U) and delete (D).
 */

package dbx;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Main class for running the program.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            DBX dbx = new DBX();
            InputStream is = System.in;
            Scanner sc = new Scanner(is);
            String username = null;
            String password = null;

            // Connect to the database
            dbx.dbConnect();
            
            // MOTD
            System.out.println("Welcome to the interactive command line MMO interface.");
            System.out.println("Please log in.");
            
            // Login
            System.out.print("Username: ");
            username = sc.next();
            
            System.out.print("Password: ");
            password = sc.next();
            
            // Authenticate user
            dbx.authenticateUser(username, dbx.hashMD5(password));
            
            // Result
            if(Global.is_player_authn)
            {
                System.out.println("Player " + Global.authn_player_id + " is logged in");
            }

            if(Global.is_admin_authn)
            {
                System.out.println("Admin " + Global.authn_player_id + " is logged in");
            }

            // Process commands
            CommandParser cp = new CommandParser();
            cp.setDbx(dbx);
            cp.processAllCommands();

            // Disconnect from the database
            dbx.dbDisconnect();
            
        }
        catch(Exception e)
        {
            System.out.println("ERROR: main: exception: " + e);
            e.printStackTrace();

        }
    }    
}
