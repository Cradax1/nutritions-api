function getRootUrl() {
  return "http://localhost:8080"
}

function getFoodByBarcodeUrl(barcode) {
  return getRootUrl() + "/food/" + barcode;
}

function getAddFoodByBarcodeUrl() {
  return getRootUrl() + "/catalogue/food/external";
}

function getAddFoodUrl() {
  return getRootUrl() + "/catalogue/food";
}

function getEditFoodUrl() {
  return getRootUrl() + "/catalogue/food";
}

function getDeleteFoodUrl(foodName) {
  return getRootUrl() + "/catalogue/food/" + foodName;
}

$(document).ready(function(){
  $("#food-search-input").on("input", function() {
    var value = $(this).val().toLowerCase();
    $("#food-catalogue-table-body tr").filter(function() {
      $(this).toggle($(this).find(".food-search-value").text().toLowerCase().indexOf(value) > -1)
    });
  });
});

$("#add-catalogue-item-by-barcode-form").submit(function( event ) {

  event.preventDefault();

  var barcode = $(this).find("input[id='add-by-barcode-modal-input-barcode']").val()

  $.ajax({
    url: getFoodByBarcodeUrl(barcode),
    type: "GET",
    success: function (response) {
      console.log("successfully found item with barcode: " + barcode);
      $("#add-by-barcode-modal").modal('hide');
      fillShowBarcodeItemModal(barcode, response);
      $("#show-barcode-item-modal").modal('show');
    },
    error: function (response) {
      console.log("Failed creating item with status: " + response.status);
      if (response.status == 404) {
        $("#add-food-by-barcode-error-text").text('Could not find item with barcode: ' + barcode);
      }
      else {
        $("#add-food-by-barcode-error-text").text('An unexpected error occurred when searching for the item');
      }
      $("#add-food-by-barcode-error-text").removeAttr('hidden');
    }
  })
});

function fillShowBarcodeItemModal(barcode, item) {
    $("#show-barcode-item-modal-barcode").val(barcode);
    $("#show-barcode-item-modal-original-name").val(item.name);
    $("#show-barcode-item-modal-brands").val(item.brands);
    $("#show-barcode-item-modal-calories").val(item.nutriments.calories);
    $("#show-barcode-item-modal-carbohydrates").val(item.nutriments.carbohydrates);
    $("#show-barcode-item-modal-proteins").val(item.nutriments.proteins);
    $("#show-barcode-item-modal-fat").val(item.nutriments.fat);
    $("#show-barcode-item-modal-fiber").val(item.nutriments.fiber);
}

$("#show-barcode-item-modal-form").submit(function( event ) {

  event.preventDefault();

  var barcode = $(this).find("input[id='show-barcode-item-modal-barcode']").val();
  var foodItem = getShowBarcodeItemModalInputs($(this), barcode);
  var jsonStr = JSON.stringify(foodItem);

  $.ajax({
    url: getAddFoodByBarcodeUrl(barcode),
    type: "POST",
    data: jsonStr,
    dataType: "json",
    contentType: "application/json",
    success: function (response) {
      console.log("successfully created item: " + jsonStr);
      $("#show-barcode-item-modal").modal('hide');
      $("#action-success-text").text("Successfully added " + foodItem["name"]);
      $("#action-success-modal").modal('show');
    },
    error: function (response) {
      console.log("Failed creating item with status: " + response.status);
      if (response.status == 409) {
        $("#show-barcode-item-modal-error-text").text(response.responseText);
      }
      else {
        $("#show-barcode-item-modal-error-text").text('An unexpected error occurred when searching for the item');
      }
      $("#show-barcode-item-modal-error-text").removeAttr('hidden');
    }
  })
});

function getShowBarcodeItemModalInputs(form, barcode) {
  var foodItem = {}
  foodItem["barcode"] = barcode;

  var inputName = form.find("input[id='show-barcode-item-modal-input-name']");
  if (inputName.val() !== "") {
    console.log("input name: " + inputName.val());
    foodItem["name"] = inputName.val();
  } else {
    console.log("original name: " + form.find("input[id='show-barcode-item-modal-original-name']").val());
    foodItem["name"] = form.find("input[id='show-barcode-item-modal-original-name']").val();
  }

  return foodItem;
}

$("#addCatalogueItemForm").submit(function( event ) {

  event.preventDefault();

  var foodItem = getAddModalInputs($(this))
  var jsonStr = JSON.stringify(foodItem)

  $.ajax({
    url: getAddFoodUrl(),
    type: "POST",
    data: jsonStr,
    dataType: "json",
    contentType: "application/json",
    success: function (response) {
      console.log("successfully created item: " + jsonStr);
      $("#addModal").modal('hide');
      $("#action-success-text").text("Successfully added " + foodItem["name"]);
      $("#action-success-modal").modal('show');
    },
    error: function (response) {
      console.log("Failed creating item with status: " + response.status);
      if (response.status == 409) {
        $("#add-food-error-text").text('An item with this name is already catalogued');
      }
      else {
        $("#add-food-error-text").text('An unexpected error occurred when attempting to catalogue the new food');
      }
      $("#add-food-error-text").removeAttr('hidden');
    }
  })
});

function getAddModalInputs(form) {
  var foodItem = {}
  foodItem["name"] = form.find( "input[id='addModalInputName']" ).val()
  foodItem["brands"] = form.find( "input[id='addModalInputBrands']" ).val()

  var nutriments = {}
  nutriments["calories"] = form.find( "input[id='addModalInputCalories']" ).val()
  nutriments["carbohydrates"] = form.find( "input[id='addModalInputCarbohydrates']" ).val()
  nutriments["proteins"] = form.find( "input[id='addModalInputProteins']" ).val()
  nutriments["fat"] = form.find( "input[id='addModalInputFat']" ).val()
  nutriments["fiber"] = form.find( "input[id='addModalInputFiber']" ).val()

  foodItem["nutriments"] = nutriments;

  return foodItem;
}

/* PROBLEM: Have to catch multiple async responses to display error in good way (and not all after another pops up)
$("#delete-all-selected-item").click(function( event ) {

  var foodNames = getSelectedItemNames();
  var responses = [];

  $.each(foodNames, function (index, item) {
  console.log(item);
    var responseArr = [item, deleteFood(item)];
    responses.push(responseArr);
  })

  $.each(responses, function (index, item) {
    console.log(item[0]);
    console.log(item[1]);
  })
});

function getSelectedItemNames() {
    var foodNames = []
    $("#food-catalogue-table-body").find("input:checkbox").each(function() {
      if($(this).is(":checked")){
        foodNames.push($(this).closest("tr").find(".food-name:first").text());
      }
    });
    return foodNames;
}

function deleteFood(name) {
  $.ajax({
    url: getDeleteFoodUrl(name),
    type: "DELETE",
    success: function (response) {
      return response.status;
    },
    error: function (response) {
      return response.status;
    }
  })
}
*/

$('#food-select-all-checkbox').click(function(e){
    var table= $(e.target).closest('table');
    $('input:checkbox',table).prop('checked',this.checked);
});

$( ".editCatalogueItemForm" ).submit(function( event ) {

  event.preventDefault();

  var editModalId = $(this).attr('iteration');

  if(!hasUserInputs($(this))) {
    $("#edit-food-error-text" + editModalId).text('Please fill out at least one attribute');
    $("#edit-food-error-text" + editModalId).removeAttr('hidden');
    return;
  }

  var foodItem = getEditModalInputs($(this), editModalId)
  var jsonStr = JSON.stringify(foodItem)

  $.ajax({
    url: getEditFoodUrl(),
    type: "PUT",
    data: jsonStr,
    dataType: "json",
    contentType: "application/json",
    success: function (response) {
      console.log("successfully edited item: " + jsonStr);
      $(".edit-modal").modal('hide');
      $("#action-success-text").text("Successfully changed " + foodItem["name"]);
      $("#action-success-modal").modal('show');
    },
    error: function (response) {
      console.log("Failed to edit item with status: " + response.status);
      $("#edit-food-error-text" + editModalId).text('An unexpected error occurred when attempting to catalogue the new food');
      $("#edit-food-error-text" + editModalId).removeAttr('hidden');
    }
  })
});

function hasUserInputs(form){
    var hasUserInputs = false;
    form.find(".user-input").each(function() {
      if($(this).val() !== "") {
        hasUserInputs = true;
      }
    });
    return hasUserInputs;
};

function getEditModalInputs(form, editModalId) {
  var foodItem = {}
  foodItem["name"] = form.find( "input[id='editModalInputName" + editModalId + "']" ).val()
  foodItem["brands"] = getEditModalInput(form, editModalId, 'Brands')

  var nutriments = {}
  nutriments["calories"] = getEditModalInput(form, editModalId, 'Calories')
  nutriments["carbohydrates"] = getEditModalInput(form, editModalId, 'Carbohydrates')
  nutriments["proteins"] = getEditModalInput(form, editModalId, 'Proteins')
  nutriments["fat"] = getEditModalInput(form, editModalId, 'Fat')
  nutriments["fiber"] = getEditModalInput(form, editModalId, 'Fiber')

  foodItem["nutriments"] = nutriments;

  return foodItem;
}

function getEditModalInput(form, editModalId, inputIdSuffix) {
  var input = form.find( "input[id='editModalInput" + inputIdSuffix + editModalId + "']" )
  if (input.val() !== "") {
    return input.val();
  } else {
    return input.attr('placeholder');
  }
}

$( ".delete-modal-delete-btn" ).click(function( event ) {

  event.preventDefault();

  var foodName = $(this).attr('food-name');
  var itemId = $(this).attr('iteration')

  $.ajax({
    url: getDeleteFoodUrl(foodName),
    type: "DELETE",
    success: function (response) {
      console.log("successfully deleted item: " + foodName);
      $(".delete-modal").modal('hide');
      $("#action-success-text").text("Successfully deleted " + foodName);
      $("#action-success-modal").modal('show');
    },
    error: function (response) {
      console.log("Failed to edit item with status: " + response.status);
      $("#delete-food-error-text" + itemId).text('An unexpected error occurred when attempting to catalogue the new food');
      $("#delete-food-error-text" + itemId).removeAttr('hidden');
    }
  })
});

$('#add-by-barcode-modal').on('hidden.bs.modal', function (e) {
  $(this)
    .find("input")
       .val('')
       .end()
    .find('#add-food-by-barcode-error-text').attr('hidden', true);
})

$('#show-barcode-item-modal').on('hidden.bs.modal', function (e) {
  $(this)
    .find("input")
       .val('')
       .end()
    .find('#show-barcode-item-modal-error-text').attr('hidden', true);
})

$('#addModal').on('hidden.bs.modal', function (e) {
  $(this)
    .find("input")
       .val('')
       .end()
    .find('#add-food-error-text').attr('hidden', true);
})

$('.edit-modal').on('hidden.bs.modal', function (e) {
  $(this)
    .find("input").not(".editModalInputName")
       .val('')
       .end();
  $(this).find('.edit-food-error-text').attr('hidden', true);
})

$('.delete-modal').on('hidden.bs.modal', function (e) {
  $(this).find('.delete-food-error-text').attr('hidden', true);
})

$('#action-success-modal').on('shown.bs.modal', function() {
  $(this).find('.btn').focus();
});

$('#action-success-modal').on('hidden.bs.modal', function (e) {
  location.reload();
})