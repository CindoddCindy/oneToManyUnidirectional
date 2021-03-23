package com.onetomanyunidirectional.onetomanyunidirectional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comics")
public class ComicsController {

    private final  ComicRepository comicRepository;
    private final LibraryRepository libraryRepository;

    @Autowired
    public ComicsController(ComicRepository comicRepository, LibraryRepository libraryRepository) {
        this.comicRepository = comicRepository;
        this.libraryRepository = libraryRepository;
    }

    @PostMapping
    public ResponseEntity<Comics> create(@RequestBody @Valid Comics comics) {
        Optional<Library> optionalLibrary = libraryRepository.findById(comics.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comics.setLibrary(optionalLibrary.get());

        Comics savedComics = comicRepository.save(comics);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedComics.getId()).toUri();

        return ResponseEntity.created(location).body(savedComics);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comics> update(@RequestBody @Valid Comics comics, @PathVariable Integer id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(comics.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Comics> optionalComics = comicRepository.findById(id);
        if (!optionalComics.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comics.setLibrary(optionalLibrary.get());
        comics.setId(optionalComics.get().getId());
        comicRepository.save(comics);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comics> delete(@PathVariable Integer id) {
        Optional<Comics> optionalComics = comicRepository.findById(id);
        if (!optionalComics.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comicRepository.delete(optionalComics.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Comics>> getAll(Pageable pageable) {
        return ResponseEntity.ok(comicRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comics> getById(@PathVariable Integer id) {
        Optional<Comics> optionalComics = comicRepository.findById(id);
        if (!optionalComics.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalComics.get());
    }

    @GetMapping("/library/{libraryId}")
    public ResponseEntity<Page<Comics>> getByLibraryId(@PathVariable Integer libraryId, Pageable pageable) {
        return ResponseEntity.ok(comicRepository.findByLibraryId(libraryId, pageable));
    }
}
