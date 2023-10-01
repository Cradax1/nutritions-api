function getRootUrl() {
  return "http://localhost:8080"
}

function getFoodUrl(name) {
    return getRootUrl() + "/catalogue/food/" + name
}

function getFoodSearchUrl(namePart) {
    return getRootUrl() + "/catalogue/food/search/" + namePart
}

function getAddMealUrl() {
    return getRootUrl() + "/catalogue/meal/";
}

function getPutMealUrl() {
    return getRootUrl() + "/catalogue/meal/";
}

function getDeleteMealUrl(name) {
    return getRootUrl() + "/catalogue/meal/" + name;
}

$(document.body).ready(function(){
  $("#dish-search-input").on("input", function() {
    var value = $(this).val().toLowerCase();
    $("#dish-catalogue-table-body tr").filter(function() {
      $(this).toggle($(this).find(".dish-search-value").text().toLowerCase().indexOf(value) > -1)
    });
  });
});

$("#food-search-input").on("input", function() {
    var input = $(this).val().trim();
    if (input !== "") {
        fetch(getFoodSearchUrl(input))
                .then(response => response.json())
                .then(responseJson => insertFoodDropdown(responseJson))
                .catch(error => console.log(error));
    } else {
        clearFoodSearchDropdown();
    }
});

function insertFoodDropdown(json) {
    clearFoodSearchDropdown();
    $(json).each(function () {
        $("#food-search-dropdown-list").append(getFoodSearchDropdownItem($(this)[0]['name']));
    });
}

function clearFoodSearchDropdown() {
    $("#food-search-dropdown-list").empty();
}

function getFoodSearchDropdownItem(name) {
    return $('<li>', {
        class: 'food-search-dropdown-item dropdown-item btn',
        text: name,
        tabindex: 0
    });
}

$(document.body).on('click', '.food-search-dropdown-item', function(){
    $("#food-search-input").val($(this).text());
    toggleFoodSearchDropdown();
    $("#food-search-input").next("input").focus();
});

function toggleFoodSearchDropdown() {
    $('#food-search-dropdown').dropdown('toggle');
}

$("#add-food-btn").click(function(event) {

    event.preventDefault();

    var inputFood = $("#food-search-input").val().trim();
    var inputAmount =  $("#food-search-amount-input").val().trim();
    if (inputFood === "") {
        addFoodErrorText("Please enter a ingredient name");
        return;
    }
    if (!$.isNumeric(inputAmount)) {
        addFoodErrorText("Please enter a valid gram number");
        return;
    }
    fetch(getFoodUrl(inputFood)).then(response => {
        if (response.ok) {
            $("#add-food-error-text").attr('hidden', true);
            $("#add-food-error-text").after(getIngredientElement(inputFood, inputAmount));
             clearFoodSearchInputs();
             return;
        }
            throw new Error(response.status);
    })
    .catch(error => {
        if (error.message === "404") {
            addFoodErrorText("There is no food catalogued with the entered name")
            return;
        }
        addFoodErrorText('An unexpected error occurred')
    })
});

function addFoodErrorText(text) {
    $("#add-food-error-text").text(text);
    $("#add-food-error-text").removeAttr('hidden');
}

function clearFoodSearchInputs() {
    $("#food-search-input").val("");
    $("#food-search-amount-input").val("");
}

function getIngredientElement(name, amount) {
    var h5 = $('<h5>', {
        class: 'added-food-wrapper'
    })
    var p = $('<p>', {
        class: 'added-food badge bg-secondary mb-0',
        text: amount + 'g' + ' ' + name,
        amount: amount,
        name: name
    })
    p.append('<button type="button" class="add-food-delete-btn btn-close btn-close-white ms-2" aria-label="Close"></button>');
    h5.append(p);
    return h5;
}

$(document.body).on('click', '.add-food-delete-btn', function(){
    $(this).parent().parent().remove();
})

$("#add-modal-add-dish-btn").click(function(event) {

    event.preventDefault();

    var meal = getAddMeal();

    if (meal["name"] === "") {
        addDishErrorText("Please enter a dish name");
        return;
    }
    if (meal['ingredients'].length === 0) {
        addDishErrorText("Please add ingredients");
        return;
    }
    fetch(getAddMealUrl(), {
        method: "POST",
        headers: {
            'Content-type': 'application/json',
        },
        body: JSON.stringify(meal)
        }).then(response => {
        if (response.ok) {
            console.log("successfully added item: " + meal['name']);
            $("#add-modal").modal('hide');
            removeAddedFood();
            $("#action-success-text").text("Successfully added " + meal["name"]);
            $("#action-success-modal").modal('show');
             return;
        }
        throw new Error(response.status);
    })
    .catch(error => {
        console.log("Failed to add item with status:" + error);
        if (error.message === "409") {
            addDishErrorText("A dish with this name already exists. Please choose a different name.")
            return;
        }
        if (error.message === "404") {
            addDishErrorText("One or more ingredients could not be found")
            return;
        }
        addDishErrorText('An unexpected error occurred')
    })
});

function getAddMeal() {
    var meal = {}
    meal['name'] = $('#add-modal-input-name').val().trim();

    var ingredients = []
    $('.added-food').each(function () {
        var ingredient = {}
        ingredient['name'] = $(this).attr('name');
        ingredient['gram'] = $(this).attr('amount');
        ingredients.push(ingredient);
    })

    meal['ingredients'] = ingredients;
    return meal;
}

function addDishErrorText(text) {
    $("#add-dish-error-text").text(text);
    $("#add-dish-error-text").removeAttr('hidden');
}

function removeAddedFood() {
    $('.added-food-wrapper').remove();
}

$('#dish-select-all-checkbox').click(function(e){
    var table= $(e.target).closest('table');
    $('input:checkbox',table).prop('checked',this.checked);
});

$('.edit-modal-add-ingredient-btn').click(function(e) {

    e.preventDefault();

    var lastElement = $(this).parent().parent().find('.edit-modal-ingredients-inner-wrapper').last();
    var iteration = $(lastElement).attr('iteration');
    var ingredientIteration = parseInt($(lastElement).attr('ingredientIteration')) + 1;

    console.log(iteration);
    console.log(ingredientIteration);

    $(this).parent().parent().find('.edit-modal-ingredients-wrapper').append(getEditModalIngredientElement(iteration, ingredientIteration));
})

function getEditModalIngredientElement(iteration, ingredientIteration) {
    var element = "<div id='edit-modal-ingredients-inner-wrapper-" + iteration + "-" + ingredientIteration + "' iteration=" + iteration + " ingredientIteration=" + ingredientIteration + " class='edit-modal-ingredients-inner-wrapper edit-modal-new-ingredient pb-2'>" +
                                "<input type='text' class='edit-modal-ingredient-input-name form-control w-50 d-inline me-1' id='edit-modal-ingredient-input-name-" + iteration + "-" + ingredientIteration + "' placeholder='...' required> " +
                                "<input type='number' step='0.01' min='0' class='edit-modal-ingredient-input-gram form-control w-25 d-inline me-1' id='edit-modal-ingredient-input-gram-" + iteration + "-" + ingredientIteration + "' placeholder='...' required> " +

                                "<a href='javascript:void(0);' class='delete-ingredient-modal px-2 text-danger' data-bs-target='#delete-ingredient-modal-" + iteration + "-" + ingredientIteration + "' data-bs-placement='top' title='Delete' tabindex='-1'><i class='bx bx-trash-alt font-size-18'></i></a>" +

                                "<div class='delete-ingredient-modal modal fade rounded-3 shadow' id='delete-ingredient-modal-" + iteration + "-" + ingredientIteration + "' tabindex='-1' aria-labelledby='delete-ingredient-modal-label' aria-hidden='true'>" +
                                    "<div class='modal-dialog modal-sm modal-dialog-centered'>" +
                                        "<div class='modal-content rounded-3 shadow'>" +
                                            "<div class='modal-body p-4 text-center'>" +
                                                "<h5 class='mb-0'>Delete new ingredient?</h5>" +
                                            "</div>" +
                                            "<p id='delete-ingredient-error-text-" + iteration + "-" + ingredientIteration + "' class='delete-ingredient-error-text text-danger text-center' hidden></p>" +
                                            "<div class='modal-footer flex-nowrap p-0'>" +
                                                "<button type='button' class='btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0 border-end' data-bs-dismiss='modal'>No</button>" +
                                                "<button type='button' class='delete-ingredient-modal-delete-btn btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0 text-danger' hide='#edit-modal-ingredients-inner-wrapper-" + iteration + "-" + ingredientIteration + "' close='#delete-ingredient-modal-" + iteration + "-" + ingredientIteration + "'>Yes</button>" +
                                            "</div>" +
                                        "</div>" +
                                   "</div>" +
                               "</div>" +
                           "</div>";
    return element;
}

$(document.body).on('input', '.edit-modal-ingredient-input-name', function() {
    var input = $(this).val().trim();
    if (input !== "") {
        fetch(getFoodSearchUrl(input))
                .then(response => response.json())
                .then(responseJson => insertEditFoodDropdown($(this), responseJson))
                .catch(error => console.log(error));
    } else {
        clearEditFoodSearchDropdown($(this).parent());
    }
});

function insertEditFoodDropdown(searchInput, json) {
    clearEditFoodSearchDropdown(searchInput.parent());
    $(json).each(function () {
        $(searchInput).parent().find(".edit-food-search-dropdown-list").append(getEditFoodSearchDropdownItem($(this)[0]['name']));
    });
}

function clearEditFoodSearchDropdown(wrapper) {
    $(wrapper).find(".edit-food-search-dropdown-list").empty();
}

function getEditFoodSearchDropdownItem(name) {
    return $('<li>', {
        class: 'edit-food-search-dropdown-item dropdown-item btn',
        text: name,
        tabindex: 0
    });
}

$(document.body).on('click', '.edit-food-search-dropdown-item', function(){
    var wrapper = $(this).parent().parent().parent();
    wrapper.find(".edit-modal-ingredient-input-name").val($(this).text());
    $(wrapper).find(".edit-modal-ingredient-input-name").next("input").focus();
});

$(document.body).on('click', '.delete-ingredient-modal', function() {
    $($(this).attr('data-bs-target')).modal({
        backdrop: 'static',
        keyboard: false,
        show: true
    });
    $($(this).attr('data-bs-target')).modal("show");
});

$(document.body).on('click', '.delete-ingredient-modal-delete-btn', function() {
    var editModalIngredient = $(this).attr('hide');
    if ($(editModalIngredient).hasClass('edit-modal-new-ingredient')) {
        $(editModalIngredient).find('.edit-modal-ingredient-input-name').removeAttr('required');
        $(editModalIngredient).find('.edit-modal-ingredient-input-gram').removeAttr('required');
    }
    $(editModalIngredient).attr('hidden', 'true');
    $($(this).attr('close')).modal("hide");
})

$(".editCatalogueItemForm").submit(function( event ) {

    event.preventDefault();

    if(!hasUserInputs($(this)) && !hasDeletedIngredients($(this))) {
        editModalErrorText($(this), "Please add, edit or delete ingredients");
        return;
    }

    var dishItem = getEditModalInputs($(this))

    fetch(getPutMealUrl(), {
        method: "PUT",
        headers: {
            'Content-type': 'application/json',
        },
        body: JSON.stringify(dishItem)
    }).then(response => {
        if (response.ok) {
            console.log("successfully edited item: " + dishItem['name']);
            $(".edit-modal").modal('hide');
            $("#action-success-text").text("Successfully edited " + dishItem['name']);
            $("#action-success-modal").modal('show');
            return;
        }
        throw new Error(response.status);
    })
    .catch(error => {
        console.log("Failed to delete item with status: " + error);
        if (error.message === "404") {
            editModalErrorText($(this), "Please use only ingredients of your food catalogue");
            return;
        }
        editModalErrorText($(this), "An unexpected error occurred");
    })
});

function hasUserInputs(form){
    var hasUserInputs = false;
    form.find(".edit-modal-ingredients-inner-wrapper").each(function() {
        if($(this).find('.edit-modal-ingredient-input-name').val() !== "" || $(this).find('.edit-modal-ingredient-input-gram').val() !== "") {
            hasUserInputs = true;
        }
    });
    return hasUserInputs;
};

function hasDeletedIngredients(form) {
    var hasDeletedIngredients = false;
    form.find(".edit-modal-ingredients-inner-wrapper").not(".edit-modal-new-ingredient").each(function() {
        if($(this).is(':hidden')) {
            hasDeletedIngredients = true;
        }
    });
    return hasDeletedIngredients;
}

function editModalErrorText(form, text) {
    $(form).find(".edit-dish-error-text").text(text);
    $(form).find(".edit-dish-error-text").removeAttr('hidden');
}

function getEditModalInputs(form) {
    var dish = {}
    dish['name'] = form.find('.edit-modal-input-name').val();

    var ingredients = []
    $(form).find('.edit-modal-ingredients-inner-wrapper').each(function() {
        if ($(this).is(':visible')) {
            var ingredient = {}
            ingredient['name'] = getEditModalInput($(this), '.edit-modal-ingredient-input-name');
            ingredient['gram'] = getEditModalInput($(this), '.edit-modal-ingredient-input-gram');
            ingredients.push(ingredient);
        }
    })

    dish['ingredients'] = ingredients;
    return dish;
}

function getEditModalInput(form, searchValue) {
    var input = form.find(searchValue);
    if (input.val().trim() !== "") {
        return input.val().trim();
    } else {
        return input.attr('placeholder');
    }
}

$( ".delete-modal-delete-btn" ).click(function( event ) {

  event.preventDefault();

  var dishName = $(this).attr('dish-name');
  var itemId = $(this).attr('iteration')

  fetch(getDeleteMealUrl(dishName), {
      method: "DELETE"
      }).then(response => {
      if (response.ok) {
          console.log("successfully deleted item: " + dishName);
          $(".delete-modal").modal('hide');
          $("#action-success-text").text("Successfully deleted " + dishName);
          $("#action-success-modal").modal('show');
          return;
      }
      throw new Error(response.status);
      })
      .catch(error => {
          console.log("Failed to delete item with status: " + error);
          deleteDishErrorText(itemId, 'An unexpected error occurred')
      })
});

$( ".delete-modal-delete-btn" ).click(function( event ) {

  event.preventDefault();

  var dishName = $(this).attr('dish-name');
  var itemId = $(this).attr('iteration')

  fetch(getDeleteMealUrl(dishName), {
      method: "DELETE"
      }).then(response => {
      if (response.ok) {
          console.log("successfully deleted item: " + dishName);
          $(".delete-modal").modal('hide');
          $("#action-success-text").text("Successfully deleted " + dishName);
          $("#action-success-modal").modal('show');
          return;
      }
      throw new Error(response.status);
      })
      .catch(error => {
          console.log("Failed to delete item with status: " + error);
          deleteDishErrorText(itemId, 'An unexpected error occurred')
      })
});

function deleteDishErrorText(itemId, text) {
    $("#delete-dish-error-text" + itemId).text(text);
    $("#delete-dish-error-text" + itemId).removeAttr('hidden');
}

$('#add-modal').on('hidden.bs.modal', function (e) {
  $(this)
    .find("input").val('').end()
    .find('#add-food-error-text').attr('hidden', true).end()
    .find('#add-dish-error-text').attr('hidden', true).end();
})

$('.edit-modal').on('hidden.bs.modal', function (e) {
    //if-statement is workaround because the overlapping delete modal also triggers the event for this modal
    if($(this).css('display') === 'none') {
        $(this)
            .find('.edit-modal-ingredients-inner-wrapper').attr('hidden', false).end()
            .find('.edit-modal-new-ingredient').remove().end()
            .find('.edit-modal-ingredient-input-name').val('').end()
            .find('.edit-modal-ingredient-input-gram').val('').end()
            .find('.delete-ingredient-error-text').attr('hidden', true).end()
            .find('.edit-dish-error-text').attr('hidden', true);
    }
})

$('#delete-modal').on('hidden.bs.modal', function (e) {
  $(this).find('#add-food-error-text').attr('hidden', true);
})

$('#action-success-modal').on('hidden.bs.modal', function (e) {
  location.reload();
})