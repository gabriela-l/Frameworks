package com.example.demo.controller;

import com.example.demo.model.Framework;
import com.example.demo.repository.FrameworkRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FrameworkController {
    @Autowired
    private FrameworkRepository repo;

    @Autowired
    public FrameworkController(FrameworkRepository repository) {
        this.repo = repository;
    }

    @GetMapping("/frameworks")
    public Iterable<Framework> frameworks() {
        return repo.findAll();
    }

    @PostMapping("/frameworks/create")
    public Framework createFramework(@RequestBody Framework framework) {
        return repo.save(framework);
    }

    @PutMapping("/frameworks/update/{id}")
    public void updateFramework(@PathVariable(value="id") Integer id, @RequestBody Framework frameworkOriginal) throws NotFoundException {
        Framework framework = repo.findById(id).get();
        framework.setName(frameworkOriginal.getName());
        framework.setDeprecationDate(frameworkOriginal.getDeprecationDate());
        framework.setHypeLevel(frameworkOriginal.getHypeLevel());
        repo.save(framework);
    }

    @DeleteMapping("/frameworks/delete/{id}")
    public void deleteFramework(@PathVariable(value="id") Integer id) throws NotFoundException {
        if (repo.findById(id).isPresent()) {
            throw new NotFoundException("Framework doesn't exist.");
        }
        repo.deleteById(id);
    }

    @GetMapping("/frameworks/find/{id}")
    public Framework findById(@PathVariable(value="id") Integer id) throws NotFoundException {
        if (repo.findById(id).isPresent()) {
            throw new NotFoundException("Framework not found.");
        }
        return repo.findById(id).get();
    }
}

