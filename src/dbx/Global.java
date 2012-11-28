/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbx;

/**
 *
 * @author villafan
 */
public class Global {
    /**
     * Remember whether the system administrator is logged in.
     */
    public static boolean is_admin_authn = false;

    /**
     * Remember whether a player is logged in.
     */
    public static boolean is_player_authn = false;
    
    /**
     * If a player is logged in, remember the player id.
     */
    public static long authn_player_id;
}
