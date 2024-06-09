package org.ergea.master.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ergea.master.dto.UserRequest;
import org.ergea.master.dto.base.BaseResponse;
import org.ergea.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Tag(name = "User")
@RestController
@RequestMapping("v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(BaseResponse.success(userService.create(userRequest), "Success Create User"));
    }

    @GetMapping()
    public ResponseEntity<?> findAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String emailAddress
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.findAll(pageable, username, emailAddress), "Success Get All Users"));
    }

    @PutMapping
    public ResponseEntity<?> update(Principal principal, @RequestBody UserRequest request) {
        return ResponseEntity.ok(BaseResponse.success(userService.update(principal, request), "Success Update User"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(userService.delete(id), "Success Delete User"));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(userService.findById(id), "Success Get Detail User"));
    }
}
