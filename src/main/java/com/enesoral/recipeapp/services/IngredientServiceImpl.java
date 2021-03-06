package com.enesoral.recipeapp.services;

import com.enesoral.recipeapp.converters.IngredientConverter;
import com.enesoral.recipeapp.domain.Ingredient;
import com.enesoral.recipeapp.domain.Recipe;
import com.enesoral.recipeapp.dto.IngredientDto;
import com.enesoral.recipeapp.exceptions.NotFoundException;
import com.enesoral.recipeapp.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    IngredientRepository ingredientRepository;
    IngredientConverter ingredientConverter;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientConverter ingredientConverter) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    public Ingredient findById(Long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if (!ingredientOptional.isPresent()) {
            throw new NotFoundException("Ingredient not found for id value: " + id);
        }
        return ingredientOptional.get();
    }

    @Override
    public IngredientDto findDtoById(Long id) {
        return ingredientConverter.convertToIngredientDto(findById(id));
    }

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public IngredientDto saveIngredientDto(IngredientDto ingredientDto) {
        Ingredient ingredient = saveIngredient(ingredientConverter.convertToIngredient(ingredientDto));
        return ingredientConverter.convertToIngredientDto(ingredient);
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }
}
