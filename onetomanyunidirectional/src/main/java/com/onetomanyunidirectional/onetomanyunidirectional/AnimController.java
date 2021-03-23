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
@RequestMapping("/api/v1/anim")
public class AnimController {

    private final AnimRepository animRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AnimController(AnimRepository animRepository, BookRepository bookRepository) {
        this.animRepository=animRepository;
        this.bookRepository = bookRepository;

    }

    @PostMapping
    public ResponseEntity<Anim> create(@RequestBody @Valid Anim anim) {
        Optional<Book> optionalBook = bookRepository.findById(anim.getBook().getId());
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        anim.setBook(optionalBook.get());

        Anim savedAnim = animRepository.save(anim);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedAnim.getId()).toUri();

        return ResponseEntity.created(location).body(savedAnim);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anim> update(@RequestBody @Valid Anim anim, @PathVariable Integer id) {
        Optional<Book> optionalBook = bookRepository.findById(anim.getBook().getId());
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Anim> optionalAnim = animRepository.findById(id);
        if (!optionalAnim.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        anim.setBook(optionalBook.get());
        anim.setId(optionalAnim.get().getId());
        animRepository.save(anim);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Anim> delete(@PathVariable Integer id) {
        Optional<Anim> optionalAnim =animRepository.findById(id);
        if (!optionalAnim.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        animRepository.delete(optionalAnim.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Anim>> getAll(Pageable pageable) {
        return ResponseEntity.ok(animRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anim> getById(@PathVariable Integer id) {
        Optional<Anim> optionalAnim = animRepository.findById(id);
        if (!optionalAnim.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalAnim.get());
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<Anim>> getByBookId(@PathVariable Integer bookId, Pageable pageable) {
        return ResponseEntity.ok(animRepository.findByBookId(bookId, pageable));
    }
}
