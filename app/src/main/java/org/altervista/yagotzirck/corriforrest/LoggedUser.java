package org.altervista.yagotzirck.corriforrest;

/* LoggedUser: singleton che conterrà l'username che ha effettuato il login, accessibile
 ** da ogni activity
 */
public class LoggedUser {
    private static LoggedUser instance = null;
    private static String username;

    private LoggedUser(){};

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
            username = "";
        }
        return instance;
    }

    public String get(){ return username; }
    public void set(String username){ LoggedUser.username = username; }
}