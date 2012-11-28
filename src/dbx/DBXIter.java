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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Container for objects used to manage iteration over a collection of items
 * in the database. Contains associated stateful objects such as result
 * sets and cursors.
 * 
 * This class functions as a container, so all data members are public
 * and there are no methods.
 * 
 * @author ?
 */
public class DBXIter
{
    public String sqlStr; // SQL query text

    public PreparedStatement pstmt; // SQL statement object

    public ResultSet rs; // Result set for entity read
}
