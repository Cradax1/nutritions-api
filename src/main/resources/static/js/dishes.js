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

function getDeleteMealUrl(name) {
    return getRootUrl() + "/catalogue/meal/" + name;
}

$("#food-search-input").on("input", function() {
    var input = $(this).val().trim();
    if (input !== "") {
        fetch(getFoodSearchUrl(input))
                .then(response => response.json())
                .then(responseJson => insertFoodDropdown(responseJson))
                .catch(error => console.log(error));
    } else {
        closeFoodSearchDropdown();
        clearFoodSearchDropdown();
    }
});

function insertFoodDropdown(json) {
    clearFoodSearchDropdown();
    $(json).each(function () {
        $("#food-search-dropdown-list").append(getFoodSearchDropdownItem($(this)[0]['name']));
    });
    openFoodSearchDropdown();
}

function clearFoodSearchDropdown() {
    $("#food-search-dropdown-list").empty();
}

function openFoodSearchDropdown() {
    if ($('#food-search-dropdown').find('.dropdown-menu').is(":hidden")){
        $('#food-search-dropdown').find('.dropdown-menu').toggle();
    }
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
    closeFoodSearchDropdown();
    $("#food-search-input").next("input").focus();
});

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

let dropDownMouseDown = false;

$(document).mousedown(function (e) {
    if ($(e.target).hasClass('food-search-dropdown-item')) {
        dropDownMouseDown = true;
    } else {
        dropDownMouseDown = false;
    }
})

$("#food-search-input").focusout(function () {
    if(!dropDownMouseDown) {
        closeFoodSearchDropdown();
    }
})

function closeFoodSearchDropdown() {
    if ($('#food-search-dropdown').find('.dropdown-menu').not(":hidden")){
        $('#food-search-dropdown').find('.dropdown-menu').toggle();
    }
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

$('#delete-modal').on('hidden.bs.modal', function (e) {
  $(this).find('#add-food-error-text').attr('hidden', true);
})

$('#action-success-modal').on('hidden.bs.modal', function (e) {
  location.reload();
})