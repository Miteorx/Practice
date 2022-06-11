package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    private final PostRepository postRepository;


    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @GetMapping("/blog")
    public String blog(Model model) {
        Iterable<Post> post = postRepository.findAll();
        model.addAttribute("post", post);
        model.addAttribute("title", "Blog");
        return "blog";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        model.addAttribute("title", "Add State");
        return "blog-add";
    }

    @GetMapping("/blog/{id}")
    public String blogPage(@PathVariable(value = "id") long id, Model model) {
        if(!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Post edit = postRepository.findById(id).orElseThrow();
        edit.setViews(edit.getViews()+1);
        postRepository.save(edit);

        Optional<Post> optional = postRepository.findById(id);
        ArrayList<Post> post = new ArrayList<>();
        optional.ifPresent(post::add);
        model.addAttribute("post", post);
        model.addAttribute("title", "Blog");
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEditPage(@PathVariable(value = "id") long id, Model model) {
        if(!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> optional = postRepository.findById(id);
        ArrayList<Post> post = new ArrayList<>();
        optional.ifPresent(post::add);
        model.addAttribute("post", post);
        model.addAttribute("title", "Blog Edit");
        return "blog-edit";
    }

    @PostMapping("/blog/add")
    public String blogAddPost(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        model.addAttribute("title", "Add State");
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogUpdatePost(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(full_text);
        postRepository.save(post);
        model.addAttribute("title", "Add State");
        return "redirect:/blog/{id}";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogRemovePost(@PathVariable(value = "id") long id) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
