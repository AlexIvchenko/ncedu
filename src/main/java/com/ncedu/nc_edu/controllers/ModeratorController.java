package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.UUID;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {
    private final RecipeService recipeService;

    @Autowired
    public ModeratorController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public ResponseEntity<?> moderatorRoot() {
        return ResponseEntity.ok("md rt");
    }

    @PostMapping("/recipes/{id}/approve")
    public ResponseEntity<Void> approveRecipeOrChanges(@PathVariable UUID id) {
        boolean status = this.recipeService.moderatorApprove(id);

        return this.getResponseEntityByStatus(status);
    }

    @PostMapping("/recipes/{id}/changesNeeded")
    public ResponseEntity<Void> requestForRecipeChanges(@PathVariable UUID id) {
        boolean status = this.recipeService.moderatorRequestForChanges(id);

        return this.getResponseEntityByStatus(status);
    }

    @PutMapping("/recipes/{id}/comment")
    public ResponseEntity<Void> recipeComment(@PathVariable UUID id, @RequestParam @Size(max = 512) String message) {
        boolean status = this.recipeService.moderatorComment(id, message);

        return this.getResponseEntityByStatus(status);
    }

    @PostMapping("/recipes/{id}/delete")
    public ResponseEntity<Void> deleteRecipe(@PathVariable UUID id) {
        boolean status = this.recipeService.removeById(id);

        return this.getResponseEntityByStatus(status);
    }

    @PostMapping("/recipes/{id}/decline")
    public ResponseEntity<Void> declineChangesOrApproval(@PathVariable UUID id) {
        boolean status = this.recipeService.moderatorDecline(id);

        return this.getResponseEntityByStatus(status);
    }

    @PostMapping("/recipes/{id}/cloneChanges")
    public ResponseEntity<Void> cloneRecipeChanges(@PathVariable UUID id) {
        boolean status = this.recipeService.moderatorCloneChanges(id);

        return this.getResponseEntityByStatus(status);
    }

    private ResponseEntity<Void> getResponseEntityByStatus(boolean status) {
        if (status) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
