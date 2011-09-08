package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import lib.jobs.BootstrapJob;
import java.util.*;
import models.*;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class Application extends Controller {

    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static void index() {
        //Post frontPost = Post.find("order by postedAt desc").first();
        Post frontPost = Post.all().order("-postedAt").get();
        //List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
        List<Post> olderPosts = Post.all().order("-postedAt").fetch(10);
        render(frontPost, olderPosts);
    }

    public static void show(Long id) {
        //Post post = Post.findById(id);
        Post post = Post.all().filter("id", id).get();
        String randomID = Codec.UUID();
        render(post, randomID);
    }

    public static void postComment(
            Long postId,
            @Required(message = "Author is required") String author,
            @Required(message = "A message is required") String content,
            @Required(message = "Please type the code") String recaptcha_challenge_field,
            String recaptcha_response_field,
            String randomID) {
        //Post post = Post.findById(id);
        Post post = Post.all().filter("id", postId).get();
        boolean isCaptchaOk = false;
        isCaptchaOk = captcha(recaptcha_challenge_field, recaptcha_response_field);
        if (!Play.id.equals("test")) {
            validation.equals(isCaptchaOk, true).message("Invalid code. Please type it again");
        }

        if (Validation.hasErrors()) {
            render("Application/show.html", post, randomID);
        }

        post.addComment(author, content);
        flash.success("Thanks for posting %s", author);
        show(postId);
    }

    public static boolean captcha(String recaptcha_challenge_field, String recaptcha_response_field) {
        String remoteAddr = request.remoteAddress;
//        Logger.info("Remote address is: " + remoteAddr);
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("<paste_here_your_recaptcha_private_key>");//private key

//        Logger.info("parameters are: " + params.urlEncode());

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, recaptcha_challenge_field, recaptcha_response_field);

        if (reCaptchaResponse.isValid()) {
//            Logger.info("Answer was entered correctly!");
            return true;
        } else {
//            Logger.info("Answer is wrong");
            return false;
        }
    }

    public static void listTagged(String tag) {
        List<Tag> tags = Tag.all().filter("name", tag).fetch();
        List<Post> posts = new ArrayList<Post>();
        for(Tag t: tags){
            posts.add(Post.all().filter("id", t.post.id).get());
        }
        render(tag, posts);
    }

    public static void loadDataFromYaml() {
        if(User.all().count() <= 0)
            new BootstrapJob().doJob();
        index();
    }
}
