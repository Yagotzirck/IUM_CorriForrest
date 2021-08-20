package org.altervista.yagotzirck.corriforrest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* Users: singleton che conterrà la lista di utenti creati durante la sessione, accessibili
 ** da ogni activity
 */
public class Users {
    private static Users instance = null;

    private static List<User> userList;

    private Users(){}

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
            userList = new ArrayList<>();
            userList.add(new User("a", "a", 80));  // debug, rimuovere
        }
        return instance;
    }

    public void add(User user){ userList.add(user); }

    public boolean contains(String username){ return userList.contains(new User(username)); }
    public boolean contains(User u){ return userList.contains(u); }



    public boolean checkPassword(String username, String password){
        return getByName(username).checkPassword((password));
    }

    public User get(String username){
        return getByName(username);
    }

    private User getByName(String username){
        int index = userList.indexOf(new User(username));

        if(index == -1)
            throw new ElementNotContainedInListException("L'utente \"" + username + "\" non è stato trovato all'interno della lista.");
        return userList.get(index);
    }


}
