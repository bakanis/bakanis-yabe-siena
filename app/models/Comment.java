package models;

import java.util.*;
import play.data.validation.*;
import siena.*;
import siena.core.batch.Batch;

@Table("comments")
public class Comment extends Model {
	
    @Id(Generator.AUTO_INCREMENT)
    public Long id;
    
    @Required
    @Column("author")
    public String author;
    
    @Required
    @Column("postedAt")
    public Date postedAt;
    
    @Required
    @MaxSize(10000)
    @Column("content")
    public String content;
    
    //@ManyToOne
    @Required
    @Column("post")
    public Post post;

    public Comment() {
    }

    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

    public static Query<Comment> all() {
        return Model.all(Comment.class);
    }

    public static Batch<Comment> batch() {
        return Model.batch(Comment.class);
    }

    @Override
    public String toString() {
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
