
function moveOptionSelected(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);
    var i = 0;
    while (i < sourceBox.options.length) {
        var opt = sourceBox.options[i];
        if (opt.selected) {
            var nOpt = new Option();
            nOpt.text = opt.text;
            nOpt.value = opt.value;
            targetBox.options[targetBox.options.length] = nOpt;
            sourceBox.remove(i);
        } else {
            i = i + 1;
        }
    }

    fillUserAddressAccessRight();
}

function fillUserAddressAccessRight() {
    var userGroupIds = '';

    $("#selectedUserGroup_Id").find("option").each(function() {
        if(userGroupIds.length > 0) {
            userGroupIds = userGroupIds + ",";
        }
        userGroupIds = userGroupIds + $(this).val();
    });

    //set in hidden dto property
    $("#accessUserGroupIds_Id").val(userGroupIds);
}
