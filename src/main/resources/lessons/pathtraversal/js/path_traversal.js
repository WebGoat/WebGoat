webgoat.customjs.profileUpload = function () {

    var picture = document.getElementById("uploadedFile").files[0];
    var formData = new FormData();
    formData.append("uploadedFile", picture);
    formData.append("fullName", $("#fullName").val());
    formData.append("email", $("#email").val());
    formData.append("password", $("#password").val());
    return formData;
}

webgoat.customjs.profileUploadCallback = function () {
    $.get("PathTraversal/profile-picture", function (result, status) {
        document.getElementById("preview").src = "data:image/png;base64," + result;
    });
}

webgoat.customjs.profileUploadFix = function () {
    var picture = document.getElementById("uploadedFileFix").files[0];
    var formData = new FormData();
    formData.append("uploadedFileFix", picture);
    formData.append("fullNameFix", $("#fullNameFix").val());
    formData.append("emailFix", $("#emailFix").val());
    formData.append("passwordFix", $("#passwordFix").val());
    return formData;
}

webgoat.customjs.profileUploadCallbackFix = function () {
    $.get("PathTraversal/profile-picture", function (result, status) {
        document.getElementById("previewFix").src = "data:image/png;base64," + result;
    });
}


webgoat.customjs.profileUploadRemoveUserInput = function () {
    var picture = document.getElementById("uploadedFileRemoveUserInput").files[0];
    var formData = new FormData();
    formData.append("uploadedFileRemoveUserInput", picture);
    formData.append("fullName", $("#fullNameRemoveUserInput").val());
    formData.append("email", $("#emailRemoveUserInput").val());
    formData.append("password", $("#passwordRemoveUserInput").val());
    return formData;
}

webgoat.customjs.profileUploadCallbackRemoveUserInput = function () {
    $.get("PathTraversal/profile-picture", function (result, status) {
        document.getElementById("previewRemoveUserInput").src = "data:image/png;base64," + result;
    });
}


webgoat.customjs.profileUploadCallbackRetrieval = function () {
    $.get("PathTraversal/profile-picture", function (result, status) {
        document.getElementById("previewRetrieval").src = "data:image/png;base64," + result;
    });
}

function newRandomPicture() {
    $.get("PathTraversal/random-picture", function (result, status) {
        document.getElementById("randomCatPicture").src = "data:image/png;base64," + result;
    });
}

webgoat.customjs.profileZipSlip = function () {
    var picture = document.getElementById("uploadedFileZipSlip").files[0];
    var formData = new FormData();
    formData.append("uploadedFileZipSlip", picture);
    formData.append("fullName", $("#fullNameZipSlip").val());
    formData.append("email", $("#emailZipSlip").val());
    formData.append("password", $("#passwordZipSlip").val());
    return formData;
}
