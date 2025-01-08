
public class Post {
    private String title;
    private String content;
    private User user;
    private final int id;

    Post(User user, String title, String content, int id) throws IllegalArgumentException {
        if (user == null){
            throw new IllegalArgumentException("Attempted to create a post without a user");
        }
        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("Title field cannot be empty");
        }
        if (content == null || content.trim().isEmpty()){
            throw new IllegalArgumentException("Content field cannot be empty");
        }
        this.user = user;
        this.title = title.trim();
        this.content = content.trim();
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public User getUser(){
        return user;
    }

    public String getUsername(){
        return user.getUsername();
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    @Override
    public String toString(){
        return String.format("Post #%d\nCreated By: %s (@%s)\nTitle: %s\n%s", id, user.getName(), user.getUsername(), getTitle(), getContent());
    }
}
