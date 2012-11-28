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
import java.io.PrintStream;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Parse and execute given commands.
 */
public class CommandParser
{
    /**
     * Database access object used as the target of commands.
     */
    private DBX dbx;
       
    /**
     * Source of input for commands.
     */
    private InputStream is = System.in;

    /**
     * Target of output for results.
     */
    private PrintStream ps = System.out;

    /**
     * Scanner for commands.
     */
    private Scanner sc = new Scanner(is);
    
    /**
     * Set the database access object.
     * @param d Database access object
     */
    public void setDbx(DBX d)
    {
        dbx = d;
    }

    /**
     * Get the database access object.
     * @return Database access object.
     */
    public DBX getDbx()
    {
        return dbx;
    }


    /**
     * Process a command. Assumes that a command is available and that
     * it is formatted correctly.
     *
     * @return False if command was exit command, otherwise true.
     */
    public boolean processOneCommand() throws Exception
    {
        boolean ret = true;
        String cmd;
        cmd = sc.next();
        if(cmd.equals("EXIT"))
        {
            // End the processing of commands
            ps.println("OUTPUT EXIT");
            ret = false;
        }
        else if(cmd.equals("CREATE"))
        {
            // Create an object and save it on the database.
            String subCmd;
            subCmd = sc.next();
            if(subCmd.equals("test"))
            {
                // Create a test entity.
                Test t = new Test(dbx);
                t.setName(sc.next());
                t.setVal(sc.nextLong());
                t.db_create(); // Create the object on the database.
                ps.println("OUTPUT CREATE test " + t.getId() + " " + t.getName() + " " + t.getVal());
            }
        }
        else if(cmd.equals("READ"))
        {
            // Read an object from the database.
            String subCmd;
            subCmd = sc.next();
            if(subCmd.equals("test"))
            {
                // Read a test entity.
                long id;
                Test t = new Test(dbx);
                id = sc.nextLong();
                t.setId(id);
                t.db_read(); // Read the object from the database.
                ps.println("OUTPUT READ test " + t.getId() + " " + t.getName() + " " + t.getVal());
            }
        }
        else if(cmd.equals("UPDATE"))
        {
            // Update an object's data on the database.
            String subCmd;
            subCmd = sc.next();
            if(subCmd.equals("test"))
            {
                // Update a test entity
                Test t = new Test(dbx);
                t.setId(sc.nextLong());
                t.setName(sc.next());
                t.setVal(sc.nextLong());
                t.db_update(); // Update the object data on the database.
                ps.println("OUTPUT UPDATE test " + t.getId() + " " + t.getName() + " " + t.getVal());
            }
        }
        else if(cmd.equals("DELETE"))
        {
            // Delete an object from the database.
            String subCmd;
            subCmd = sc.next();
            if(subCmd.equals("test"))
            {
                // Delete a test entity
                Test t = new Test(dbx);
                long id;
                id = sc.nextLong();
                t.setId(id);
                t.db_delete(); // Perform the database deletion operation.
                ps.println("OUTPUT DELETE test " + id);
            }
            else if(subCmd.equals("player"))
            {
                // Delete a test entity
                //Test t = new Test(dbx);
                long id;
                id = sc.nextLong();
                
                if(Global.is_admin_authn ||
                   (Global.is_player_authn && id == Global.authn_player_id))
                {
                    //t.setId(id);
                    //t.db_delete(); // Perform the database deletion operation.
                    ps.println("OUTPUT DELETE player " + id);
                }
                else
                {
                    ps.println("OUTPUT DELETE player FAILED: incorrect id or not logged in or other problem");
                }
            }
        }
        else if(cmd.equals("READ_ALL"))
        {
            // Read all objects from the database into a collection.
            String subCmd;
            subCmd = sc.next();
            if(subCmd.equals("npcs"))
            {
                // Read all test entities
                Collection<NPC> tc = new LinkedList<NPC>();
                
                // Populate the vector using the database object iterator.
                {
                    NPC t = new NPC(dbx);

                    t.db_iter_begin();
                    while(t.db_iter_next())
                    {
                        tc.add(new NPC(t));
                    }
                    t.db_iter_end();
                }
                
                for(NPC t: tc)
                {
                    ps.println("OUTPUT READ_ALL test " + t.getId() + " " + t.getName() + " " + t.getBenevolence());
                }
            }
        }
        else if(cmd.equals("READ_ALL_GLOBAL"))
        {
            // Read all objects from the database into a collection.
            Collection<DBObj> tc = new LinkedList<DBObj>();

            // Populate the vector using the database object iterator.
            {
                Test t = new Test(dbx);

                t.db_iter_begin();
                while(t.db_iter_next())
                {
                    tc.add(new Test(t));
                }
                t.db_iter_end();
            }

            for(DBObj t: tc)
            {
                ps.println("OUTPUT READ_ALL_GLOBAL item");
                //ps.println("OUTPUT READ_ALL_GLOBAL " + t.getId() + " " + t.getName() + " " + t.getVal());
            }
        }
        else if(cmd.equals("WORLD_DISPLAY"))
        {
            // Generates an SVG document with all the world contents
            dbx.world_display();
        }
   
        return ret;
    }

     
    
    /**
     * Process all commands until exit command is found
     */
    public void processAllCommands() throws Exception
    {
        boolean go = true;
        while(go)
        {
            go = processOneCommand();
        }
    }
}
