class User {
    private String name;
    private String username;

    User (String name, String username) throws IllegalArgumentException{
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name field cannot be empty");
        }
        this.name = name.trim();
        if (username == null || username.trim().isEmpty()){
            this.username = this.name.split("\\s+")[0].toLowerCase();
        }
        else{
            this.username = username.trim().toLowerCase();
        }
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }
}
