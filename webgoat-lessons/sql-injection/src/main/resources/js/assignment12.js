$(function () {
    $('.col-check').hide();
    $('#btn-admin').on('click', function () {
        if ($("#toolbar-admin").is(":visible")) {
            $("#toolbar-admin").hide();
            $(".col-check").hide();
        }
        else {
            $("#toolbar-admin").show();
            $(".col-check").show();
        }
    });

    $('#btn-online').on('click', function () {
        $('table tr').filter(':has(:checkbox:checked)').find('td').parent().removeClass().addClass('success');
        $('table tr').filter(':has(:checkbox:checked)').find('td.status').text('online');
    });
    $('#btn-offline').on('click', function () {
        $('table tr').filter(':has(:checkbox:checked)').find('td').parent().removeClass().addClass('warning');
        $('table tr').filter(':has(:checkbox:checked)').find('td.status').text('offline');
    });
    $('#btn-out-of-order').on('click', function () {
        $('table tr').filter(':has(:checkbox:checked)').find('td').parent().removeClass().addClass('danger');
        $('table tr').filter(':has(:checkbox:checked)').find('td.status').text('out of order');
    });

});

$(document).ready(function () {
    getServers('id');
});

var html = '<tr class="STATUS">' +
    '<td class="col-check"><input type="checkbox" class="form-check-input"/></td>' +
    '<td>HOSTNAME</td>' +
    '<td>IP</td>' +
    '<td>MAC</td>' +
    '<td class="status">ONLINE</td>' +
    '<td>DESCRIPTION</td>' +
    '</tr>';

function getServers(column) {
    $.get("SqlInjection/servers?column=" + column, function (result, status) {
        $("#servers").empty();
        for (var i = 0; i < result.length; i++) {
            var server = html.replace('ID', result[i].id);
            var status = "success";
            if (result[i].status === 'offline') {
                status = "danger";
            }
            server = server.replace('ONLINE', status);
            server = server.replace('STATUS', status);
            server = server.replace('HOSTNAME', result[i].hostname);
            server = server.replace('IP', result[i].ip);
            server = server.replace('MAC', result[i].mac);
            server = server.replace('DESCRIPTION', result[i].description);
            $("#servers").append(server);
        }

    });
}