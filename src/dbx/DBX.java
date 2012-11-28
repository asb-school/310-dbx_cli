/**
 * - Course: CPTR310 Database Application Programming
 * - Institution: Andrews University
 * - Semester: Fall 2011
 * - Instructor: Dr. Roy Villafane
 *
 * - Item: ?
 *
 * - Student: Andrew Breja
 *
 * - Description: >
 *     Command line CRUD database application using Java.
 *     CRUD is create (C), read (R), update (U) and delete (D).
 */

package dbx;

import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

/**
 * Database access class.
 * 
 * @author ?
 */
public class DBX 
{
    //=========================================================================
    //=========================================================================
    // Member variables
    //=========================================================================
    //=========================================================================

    /**
     * Properties object for configuration information.
     */
    private Properties prop;

    /**
     * Properties object file name.
     */
    private String propFileName = "dbx.properties";

    /**
     * Database driver to use. For example, com.dbvendor.jdbc.Driver.
     */
    private String dbDriver;
    private String dbDriverPropName = "db_driver";

    /**
     * JDBC URL used to connect to the database server. For example,
     * jdbc:dbvendor://hostname.tld/dbname?user=username&password=secret
     */
    private String dbUrl;
    private String dbUrlPropName = "db_url";
    
    /**
     * Database connection.
     */
    private Connection conn;

    /**
     * Command parser.
     */
    CommandParser cp;
    
    //=========================================================================
    //=========================================================================
    // Database connection functionality
    //=========================================================================
    //=========================================================================

    /**
     * Provide access to the JDBC database connection object.
     * @return JDBC connection object.
     */
    Connection getConnection()
    {
        return conn;
    }
    
    /**
     * Return status of database connection
     */
    public boolean isDbConnected()
    {
        // is the database connection established?
        boolean isConn;
        if(conn == null)
        {
            isConn = false;
        }
        else
        {
            isConn = true;
        }
        return isConn;
    }
    
    /**
     * Connect to the database. Initializes properties object to get
     * connection information.
     */
    public void dbConnect() throws Exception
    {
        prop = new Properties();

        prop.load(new FileInputStream(propFileName));
        dbDriver = prop.getProperty(dbDriverPropName);
        dbUrl = prop.getProperty(dbUrlPropName);

        // Load database driver
        Class.forName( dbDriver );

        // Make the database connection to the specified URL
        if(conn != null)
        {
            throw new Exception("ERROR: DBX.connect: attempting to connect when connection already exists");
        }
        conn = DriverManager.getConnection( dbUrl );

        // Disable auto commit, to enable explicit transaction control
        conn.setAutoCommit(false);
    }
    
    /**
     * Disconnect from the database
     */
    public void dbDisconnect() throws Exception
    {
        if(conn == null)
        {
            throw new Exception("ERROR: DBX.disconnect: attempting to disconnect from a null database connection");
        }
       conn.close();
       conn = null;
    }

    //=========================================================================
    //=========================================================================
    // Authentication functionality
    //=========================================================================
    //=========================================================================

    
    
     /**
     * Hash a string using MD5
     * @s String to hash
     * @return MD5 hash of input string
     */
    public String hashMD5(String s) throws Exception
    {
        String plainPassword = s;
        String hashedPassword;
        byte[] digest;

        MessageDigest md = MessageDigest.getInstance("MD5");
        digest = md.digest(plainPassword.getBytes());
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < digest.length; ++i)
        {
          sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        hashedPassword = sb.toString();
        return hashedPassword;
    }  
    
    
    /**
     * Verify if the credentials provided by the user match what is available
     * in the authentication store.
     *
     * @param username User name
     * @param encryptedPassword Encrypted password
     * @return True if the user name and password are valid, false otherwise.
     */
    public boolean authenticateUser(String username, String encryptedPassword) throws Exception
    {
        boolean isAuthenticated = false;

        String sqlStr; // SQL query text
        PreparedStatement pstmt; // SQL statement object
        ResultSet rs; // Results returned from the query

        // SQL query
        sqlStr = "SELECT password, id, admin FROM users WHERE username = ?";

        // Create the SQL statement object and prepare the statement
        pstmt = conn.prepareStatement(sqlStr);

        // Set the parameter values, with the index corresponding to
        // the appropriate question mark sequence in the query string.
        pstmt.setString(1, username);

        // Execute the query
        rs = pstmt.executeQuery();

        // Access the result (should only be 1 row in this case)
        if(!rs.next())
        {
            throw new Exception("ERROR: DBX.authenticateUser: cannot access user");
        }

        // Get the password from the first column (matching the SQL statement)
        String p = rs.getString(1);
        int player_id = rs.getInt(2);
        int admin = rs.getInt(3);

        // Access the result (should only be 1 row in this case)
        if(rs.next())
        {
            throw new Exception("ERROR: DBX.authenticateUser: user is not unique");
        }

        if(encryptedPassword.equals(p))
        {
            isAuthenticated = true;
            
            // Set globals
            Global.authn_player_id = player_id;
            
            // Check if admin
            if (admin == 1)
            {
                Global.is_admin_authn = true;
            }
        }

        rs.close();
        pstmt.close();
        conn.commit();
        return isAuthenticated;
    }

    /**
     * // Generates an SVG document with all the world contents
     */
    public void world_display() throws Exception
    {
        DBX dbx = this; // don't do this in real life; it's a waste
        
        String sqlStr; // SQL query text
        PreparedStatement pstmt; // SQL statement object
        ResultSet rs; // Result set for entity read

        // SQL query for players
        sqlStr = "select player.name as name, player.pos_x as pos_x, " +
                 "player.pos_y as pos_y, player.skill_level as skill_level " +
                 "from player, region " +
                 "where player.region_id = region.id and " +
                 "region.name = ?";

        // Create the SQL statement object and prepare the statement
        pstmt = dbx.getConnection().prepareStatement(sqlStr);

        // Set the parameter values, with the index corresponding to
        // the appropriate question mark sequence in the query string.
        pstmt.setString(1, "Middle Earth");

        // Execute the query
        rs = pstmt.executeQuery();
        
        // Read the entities from the result set.
        while(rs.next())
        {
            // <circle cx="350" cy="100" r="40" fill="#00ff00" stroke="#000000" stroke-width="1"/>
            String name = rs.getString("name");
            int pos_x = rs.getInt("pos_x");
            int pos_y = rs.getInt("pos_y");
            float skill_level = rs.getFloat("skill_level");
            int color = (int)((1.0-skill_level)*192.0);
            String svg_line;
            svg_line = String.format("<circle cx=\"%d\" cy=\"%d\" r=\"30\" fill=\"rgb(%d,255,%d)\" stroke=\"#000000\" stroke-width=\"1\"/>",
                                   pos_x, pos_y, color, color);
            System.out.println(svg_line);
        }
        rs.close();
        pstmt.close();
        
        
        // SQL query for benevolent npcs
        sqlStr = "SELECT npc_type.name as name, npc_type.benevolence as benevolence, npc.pos_x as pos_x, npc.pos_y as pos_y FROM npc, npc_type WHERE npc_type.id = npc.npc_type_id AND npc_type.benevolence > 0";
        
        // Create the SQL statement object and prepare the statement
        pstmt = dbx.getConnection().prepareStatement(sqlStr);

        // Execute the query
        rs = pstmt.executeQuery();
        
        // Read the entities from the result set.
        while(rs.next())
        {
            // <circle cx="350" cy="100" r="40" fill="#00ff00" stroke="#000000" stroke-width="1"/>
            String name = rs.getString("name");
            int pos_x = rs.getInt("pos_x");
            int pos_y = rs.getInt("pos_y");
            float aggressiveness = rs.getFloat("benevolence");
            int color = (int)((1.0-aggressiveness)*192.0);
            String svg_line;
            svg_line = String.format("<circle cx=\"%d\" cy=\"%d\" r=\"30\" fill=\"rgb(%d,255,%d)\" stroke=\"#000000\" stroke-width=\"1\"/>",
                                   pos_x, pos_y, color, color);
            System.out.println(svg_line);
        }
        rs.close();
        pstmt.close();
        
     
        
        // SQL query for aggressive npcs
        sqlStr = "SELECT npc_type.name as name, npc_type.aggressiveness as aggressiveness, npc.pos_x as pos_x, npc.pos_y as pos_y FROM npc, npc_type WHERE npc_type.id = npc.npc_type_id AND npc_type.aggressiveness > 0";
        
        // Create the SQL statement object and prepare the statement
        pstmt = dbx.getConnection().prepareStatement(sqlStr);

        // Execute the query
        rs = pstmt.executeQuery();
        
        // Read the entities from the result set.
        while(rs.next())
        {
            // <circle cx="350" cy="100" r="40" fill="#00ff00" stroke="#000000" stroke-width="1"/>
            String name = rs.getString("name");
            int pos_x = rs.getInt("pos_x");
            int pos_y = rs.getInt("pos_y");
            float aggressiveness = rs.getFloat("aggressiveness");
            int color = (int)((1.0-aggressiveness)*192.0);
            String svg_line;
            svg_line = String.format("<circle cx=\"%d\" cy=\"%d\" r=\"30\" fill=\"rgb(255,%d,%d)\" stroke=\"#000000\" stroke-width=\"1\"/>",
                                   pos_x, pos_y, color, color);
            System.out.println(svg_line);
        }
        rs.close();
        pstmt.close();
        
        
        dbx.getConnection().commit();
    }

}
