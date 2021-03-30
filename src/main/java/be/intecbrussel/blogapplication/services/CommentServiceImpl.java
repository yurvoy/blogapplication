package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Comment;
import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.CommentRepository;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public Comment findById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (!commentOptional.isPresent()) {
            throw new RuntimeException("comment not found");
        }
        return commentOptional.get();
    }

    @Override
    public Comment saveComment(Long postId, Long userId, CreateCommentDto newComment) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        Comment comment = new Comment();
        comment.setCommentText(newComment.getCommentText());
        comment.setCommentTimeStamp(LocalDateTime.now(Clock.systemUTC()));
        // This code is a change to use the actual computer from the computer. Otherwise it uses UTC
        // which is one hour less than belgium
        //comment.setCommentTimeStamp(LocalDateTime.now(Clock.systemDefaultZone()));
        comment.setPost(post);
        comment.setUser(user);

        List<Comment> comments = post.getComments();
        comments.add(0,comment);
        post.setComments(comments);

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAll() {
        List<Comment> comments = commentRepository.findAll();

        if (comments.size() == 0){
            System.out.println("There are no comments yet");
        }
        return comments;
    }

    @Override
    public void deleteById(Long commentId) {

        System.out.println("Deleting post: " + commentId);

        Optional<Comment> commentOptional = Optional.ofNullable(commentRepository.findById(commentId).get());

        Optional<User> userOptional = Optional.of(commentOptional.get().getUser());

        if(userOptional.isPresent()){
            User user = userOptional.get();
            System.out.println("found user");

            if(commentOptional.isPresent()){
                System.out.println("Comment found");
                log.debug("Comment found");
                Comment commentToDelete = commentOptional.get();

                Post post = commentToDelete.getPost();
                post.getComments().remove(commentToDelete);
                user.getPosts().remove(commentToDelete);
                commentRepository.delete(commentToDelete);

                System.out.println(commentToDelete.getId());
                postService.save(post);
                userService.save(user);
            }
        } else {
            log.debug("User Id Not found. Id:" + commentId);
        }

    }

}