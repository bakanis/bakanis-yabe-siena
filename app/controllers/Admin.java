package controllers;

import play.data.validation.Validation;
import play.mvc.*;
import java.util.*;
import models.*;

public class Admin extends Controller {

    @Before
    static void setConnectedUser() {
        if (Security.isConnected()) {
            //User user = User.find("byEmail", Security.connected()).first();
            User user = User.all().filter("email", "bob@gmail.com").get();
            renderArgs.put("user", user.fullname);
        }
    }

    public static void index() {
        //List<Post> posts = Post.find("author.email", Security.connected()).fetch();
        User author = User.all().filter("email", "bob@gmail.com").get();
        List<Post> posts = Post.all().filter("author", author).fetch();
        render(posts);
    }

    public static void form(Long id) {
        if (id != null) {
            //Post post = Post.findById(id);
            Post post = Post.all().filter("id", id).get();
            render(post);
        }
        render();
    }

    public static void save(Long id, String title, String content, String tags) {
        Post post;
        if (id == null) {
            // Create post
            //User author = User.find("byEmail", Security.connected()).first();
            User author = User.all().filter("email", "bob@gmail.com").get();
            post = new Post(author, title, content);
        } else {
            // Retrieve post
            //post = Post.findById(id);
            post = Post.all().filter("id", id).get();
            post.title = title;
            post.content = content;
            //post.tags.clear();
            Tag.all().filter("post", post).delete();
        }
        // Set tags list
        for (String tag : tags.split("\\s+")) {
            if (tag.trim().length() > 0) {
                Tag.findOrCreateByName(tag, post);
            }
        }
        // Validate
        validation.valid(post);
        if (Validation.hasErrors()) {
            render("@form", post);
        }
        // Save
        post.save();
        index();
    }
}
