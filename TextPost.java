
import java.util.*;

public class TextPost extends Post {
    private List<String> keywords;

    public TextPost(User user, String title, String content, int id){
        super(user, title, content, id);
        keywords = Arrays.asList(content.trim().toLowerCase().split("//s+"));
    }

    public boolean contains(String keyword){
        return keywords.contains(keyword.trim().toLowerCase());
    }

    public int getId(){
        return super.getId();
    }
}
