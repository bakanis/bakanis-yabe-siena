package models;

import java.util.*;
import play.data.validation.*;

import siena.*;
import siena.core.batch.Batch;

@Table("tags")
public class Tag extends Model implements Comparable<Tag> {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;
    
    @Required
    @Column("name")
    public String name;
    
    @Column("post")
    public Post post;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public static Query<Tag> all() {
        return Model.all(Tag.class);
    }

    public static Batch<Tag> batch() {
        return Model.batch(Tag.class);
    }

    public static Tag findOrCreateByName(String name, Post post) {
        Tag tag = Tag.all().filter("name", name).get();
        if (tag == null) {
            tag = new Tag(name);
            tag.post = post;
            tag.save();
        }
        return tag;
    }

    public static List<Map> getCloud() {
//        List<Map> result = Tag.find(
//            "select new map(t.name as tag, count(p.id) as pound) from Post p join p.tags as t group by t.name"
//        ).fetch();
//TODO: Too difficult JPA query for me. I just simply fetch all :)

        List<Map> result = new ArrayList(Tag.all().fetch());        
        return result;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public int compareTo(Tag otherTag) {
        return name.compareTo(otherTag.name);
    }
}