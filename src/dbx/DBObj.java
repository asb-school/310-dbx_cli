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

/**
 * Common functionality to be provided by an object that can be stored to a database.
 * 
 * @author ?
 */
interface DBObj {
    /**
     * Set the object used for database access operations.
     * @param dbx Database access operation object.
     */
    void setDbx(DBX dbx);
    
    /**
     * Get the object used for database access operations.
     * @return Database access operation object.
     */
    DBX getDbx();
    
    /**
     * ======================================================================
     * Functionality for managing a single object.
     * ======================================================================
     */   
    
    /**
     * Create the object on the database.
     * @throws Exception 
     */
    void db_create() throws Exception;
    
    /**
     * Read the object data from the database.
     * @throws Exception 
     */
    void db_read() throws Exception;
    
    /**
     * Modify the object data on the database to match the current object.
     * 
     * @throws Exception 
     */
    void db_update() throws Exception;
    
    /**
     * Delete the object from the database.
     * 
     * @throws Exception 
     */
    void db_delete() throws Exception;

    /**
     * ======================================================================
     * Functionality for iterating over several objects.
     * ======================================================================
     */   
    
    /**
     * Initialize iterator and perform database query and other database
     * operations that enable iterating over several objects.
     * @throws Exception 
     */
    void db_iter_begin() throws Exception;
    
    /**
     * Retrieve the data for the next object from the database and place
     * it on the current object.
     * 
     * @return True if retrieval was successful, false if there are no more
     * objects to retrieve.
     * 
     * @throws Exception 
     */
    boolean db_iter_next() throws Exception;
    
    /**
     * End iteration and release associated resources.
     * @throws Exception 
     */
    void db_iter_end() throws Exception;
    
}
