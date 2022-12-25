"use strict";

let isAdmin = false;

async function getPrincipal() {
    let response = await fetch("http://localhost:8080/user/api/users");
    if(response.status === 500) {
        window.location.replace("http://localhost:8080/logout");
    }
    return await response.json();
}

updateFieldsPrincipal(true);
updateStylesByRole();
updateAllUsers();

async function updateStylesByRole() {
    let user = await getPrincipal();
    $.each(user.roles, function (i, value) {
        if (value.role === 'ROLE_ADMIN') {
            isAdmin = true;
        }
    });
    if (isAdmin) {
        $('#adminButton').removeAttr('hidden');
        $('#adminPanel').removeAttr('hidden');
        $('#userButton').removeClass('active');
        $('#userInfo').removeClass('active');
    }
}

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

//для админа

async function getAllUsers() {
    let response = await fetch('http://localhost:8080/admin/api/users');
    return await response.json();
}

async function getAllRoles() {
    let response = await fetch('http://localhost:8080/admin/api/users/roles');
    return await response.json();
}

async function updateAllUsers() {
    let roles = await getAllRoles();
    let allUsers = await getAllUsers();
    $('.optionNew').remove()
    $('#tbodyAllUsers').remove()
    $('#usersTable').append('<tbody id="tbodyAllUsers"></tbody>')
    $.each(allUsers, function (i, value) {
        let roles = '';
        $.each(value.roles, function (i, value) {
            roles += value.role.substring(5, value.role.length) + ' ';
        });
        $('#tbodyAllUsers').append('<tr>' +
            '<td>' + value.id + '</td>' +
            '<td>' + value.username + '</td>' +
            '<td>' + value.email + '</td>' +
            '<td>' + roles + '</td>' +
            '<td><button type="button" class="btn btn-info text-white editBtn" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>' +
            '<td><button type="button" class="btn btn-danger text-white deleteBtn" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>'
            + '</tr>')
    });
    $('.editBtn').toArray().forEach(btn => {
        onEditButton(btn);
    });
    $('.deleteBtn').toArray().forEach(btn => {
        onDeleteButton(btn);
    });
    $.each(roles, function (i, value) {
        $('.form-select').append('<option class="optionNew" value="' + value.role + '">' + value.role.substring(5, value.role.length) + '</option>');
    });

}

function onEditButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        let tr = button.parentNode.parentNode;
        $('#editId').val((tr.children[0]).innerHTML);
        $('#editUsername').val((tr.children[1]).innerHTML);
        $('#editEmail').val((tr.children[2]).innerHTML);
        $('#editModal').modal('show');
    })
}

function onDeleteButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        let tr = button.parentNode.parentNode;
        $('#deleteId').val((tr.children[0]).innerHTML);
        $('#deleteUsername').val((tr.children[1]).innerHTML);
        $('#deleteEmail').val((tr.children[2]).innerHTML);
        $('#deleteModal').modal('show');
    })
}

async function onSignInButton() {
    let selected = listOfRoles($('.optionNew'))
    let user = {
        username: $('#usernameReg').val(),
        email: $('#emailReg').val(),
        password: $('#passwordReg').val(),
        roles: []
    };
    $.each(selected, function (i, value) {
        let role = {
            role: value
        }
        user.roles.push(role);
    })
    let response = await fetch('http://localhost:8080/admin/api/users', {
        method: 'POST',
        headers: {'Content-Type': 'application/json;charset=utf-8'},
        body: JSON.stringify(user)
    });
    let result = await response.json();
    if(response.status === 400) {
        $('#formNewError').text(result.message)
    }
    if(response.status === 200) {
        updateAllUsers();
        activateTab('userTable');
        resetAddUserForm();
        $('#formNewError').text('')
    }
}

function listOfRoles(options) {
    let res = [];
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            res.push(options[i].value);
        }
    }
    return res;
}

function activateTab(tab){
    $('.nav-tabs a[href="#' + tab + '"]').tab('show');
};

function resetAddUserForm() {
    $('#signInForm')[0].reset();
}

async function onDeleteBtnForm() {
    let id = $('#deleteId').val()
    let response = await fetch('http://localhost:8080/admin/api/users/'+ id, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json;charset=utf-8'},
    });
    let result = await response.json()
    if(response.status === 404) {
        $('#formDeleteError').text(result.message)
    }
    if(response.status === 200) {
        updateAllUsers();
        $('#deleteModal').modal('hide');
    }
}

async function onEditBtnForm() {
    let selected = listOfRoles($('.optionNew'))
    let user = {
        id: $('#editId').val(),
        username: $('#editUsername').val(),
        email: $('#editEmail').val(),
        password: $('#editPassword').val(),
        roles: []
    };
    $.each(selected, function (i, value) {
        let role = {
            role: value
        }
        user.roles.push(role);
    })
    let response = await fetch('http://localhost:8080/admin/api/users', {
        method: 'PATCH',
        headers: {'Content-Type': 'application/json;charset=utf-8'},
        body: JSON.stringify(user)
    });
    let result = await response.json();
    if(response.status === 400) {
        $('#formEditError').text(result.message)
    }
    if(response.status === 200) {
        updateAllUsers();
        updateFieldsPrincipal(false);
        $('#editModal').modal('hide');
    }
}













