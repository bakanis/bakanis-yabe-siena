package models;

import java.util.*;
import play.Logger;
import play.data.binding.*;
import play.data.validation.*;
import siena.*;
import siena.core.batch.Batch;

@Table("posts")
public class Post extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;
    @Required
    @Column("title")
    public String title;
    @Required
    @DateTime
    @As("yyyy-MM-dd")
    @Column("postedAt")
    public Date postedAt;
    @Required
    @MaxSize(10000)
    @Column("content")
    public String content;
    @Required
    @Column("author")
    public User author;
//    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    public transient List<Comment> comments;
//    @ManyToMany(cascade=CascadeType.PERSIST)
    public transient Set<Tag> tags;

    public Post() {
    }

    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.tags = new TreeSet();
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }

    public static Query<Post> all() {
        return Model.all(Post.class);
    }

    public static Batch<Post> batch() {
        return Model.batch(Post.class);
    }

    public Post addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content);
        newComment.save();
        //this.comments.add(newComment);
        //this.save();
        return this;
    }

    public List<Comment> getComments() {
        return Comment.all().filter("post", this).fetch();
    }

    public List<Tag> getTags() {
        return Tag.all().filter("post", this).fetch();
    }

    public User getAuthor() {
        return User.all().filter("id", this.author.id).get();
    }

    public Date getPostedAt() {
        return new Date();
    }

    public Post previous() {
        //return Post.find("postedAt < ? order by postedAt desc", postedAt).first();
        return Post.all().filter("postedAt >", postedAt).order("-postedAt").get();
    }

    public Post next() {
        //return Post.find("postedAt > ? order by postedAt asc", postedAt).first();
        return Post.all().filter("postedAt >", postedAt).order("postedAt").get();
    }

    public Post tagItWith(String name) {
        //tags.add(Tag.findOrCreateByName(name));
        return this;
    }

//    public static List<Post> findTaggedWith(String tagName) {
//        return Post.find(
//            "select distinct p from Post p join p.tags as t where t.name = ?",
//            tag
//        ).fetch();
//
//    }

    public static List<Post> findTaggedWith(String... tags) {
//        return Post.find(
//            "select distinct p.id from Post p join p.tags as t where t.name in (:tags) group by p.id having count(t.id) = :size"
//        ).bind("tags", tags).bind("size", tags.length).fetch();
        List<Post> posts = new ArrayList<Post>();
        for (String tag : tags) {
            posts.add(Post.all().filter("tag", tag).get());
        }
        return posts;
    }

    @Override
    public String toString() {
        return title;
    }
}
