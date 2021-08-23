package org.altervista.yagotzirck.corriforrest;

public class User {
    private String  username;
    private String  password;
    private int     weight;

    public User(String username, String password, int weight){
        this.username = username;
        this.password = password;
        this.weight = weight;
    }

    // Costruttore da usare per contains() nelle liste
    public User(String username){ this.username = username; }

    public boolean checkPassword(String password){ return this.password.equals(password); }

    // getters
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public int getWeight(){ return weight; }

    // setters
    public void setPassword(String password){ this.password = password; }
    public void setWeight(int weight){ this.weight = weight; }

    // methods for list item management
    @Override
    public int hashCode(){ return username.hashCode(); }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        // field comparison
        return username.equals(user.getUsername());
    }
}
