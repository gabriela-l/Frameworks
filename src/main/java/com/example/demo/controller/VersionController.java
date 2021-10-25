package com.example.demo.controller;

import com.example.demo.model.Version;
import com.example.demo.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VersionController {
    @Autowired
    private VersionRepository repo;

    @GetMapping("/versions")
    public Iterable<Version> listVersions() {
        return repo.findAll();
    }

    @PostMapping("/versions/save")
    public void saveVersion(Version version) {
        repo.save(version);
    }

    @PostMapping("/versions/create")
    public Version createVersion(@RequestBody Version version) {
        return repo.save(version);
    }

    @DeleteMapping("/versions/delete/{versionId}")
    public void deleteVersion(@PathVariable(value = "versionId") Integer versionId) {
        repo.deleteById(versionId);
    }
}
