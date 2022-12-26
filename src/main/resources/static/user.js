"use strict";

async function getPrincipal() {
    let response = await fetch("http://localhost:8080/user/api/users");
    if(response.status === 500) {
        window.location.replace("http://localhost:8080/logout");
    }
    return await response.json();
}

updateFieldsPrincipal(true);



async function updateFieldsPrincipal(app) {
    let user = await getPrincipal();
    $('.usernamePrincipal').text(user.username);
    $('.idPrincipal').text(user.id);
    $('.emailPrincipal').text(user.email);
    if(app) {
        $.each(user.roles, function (i, value) {
            $('.rolesPrincipal').append('<span> ' + value.role.substring(5, value.role.length) + ' </span>');
        });
    }
}