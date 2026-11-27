(function () {
    var serverUrl = $('#pi-config').attr('data-server-url');

    function render(view) {
        $('.pi-mcp-url').text(view.mcpUrl);
        $('.pi-notes-url').text(view.notesUrl);
        $('.pi-variant').val(view.variant);
        $('.pi-guard').prop('checked', view.argumentGuard);
        $('.pi-pinned').prop('checked', view.pinned);
        $('.pi-tools-json').text(JSON.stringify({tools: view.tools}, null, 2));
        renderLog(view.calls);
    }

    function renderLog(calls) {
        var logs = $('.pi-log').empty();
        if (!calls.length) {
            logs.append($('<p class="pi-empty">').text('No tool calls yet.'));
            return;
        }
        var table = $('<table class="table table-sm pi-log-table">').append(
            $('<thead>').append($('<tr>').append(
                $('<th>').text('Time'), $('<th>').text('Tool'),
                $('<th>').text('Arguments'), $('<th>').text('Result'))));
        var body = $('<tbody>');
        calls.slice().reverse().forEach(function (call) {
            var result = call.refused ? 'refused: token in arguments'
                : call.leaked ? 'accepted: token leaked' : 'ok';
            body.append($('<tr>').addClass(call.refused ? 'pi-refused' : call.leaked ? 'pi-leaked' : '').append(
                $('<td>').text(call.time.replace('T', ' ').replace(/\.\d+Z$/, 'Z')),
                $('<td>').text(call.tool),
                $('<td>').append($('<code>').text(JSON.stringify(call.arguments))),
                $('<td>').text(result)));
        });
        logs.append(table.append(body));
    }

    function load() {
        $.ajax({url: serverUrl, cache: false}).done(render);
    }

    function update(data) {
        $.post(serverUrl, data).done(render);
    }

    $(document).off('.promptinjection');
    $(document).on('change.promptinjection', '.pi-variant', function () {
        update({variant: $(this).val()});
    });
    $(document).on('change.promptinjection', '.pi-guard', function () {
        update({argumentGuard: $(this).is(':checked')});
    });
    $(document).on('change.promptinjection', '.pi-pinned', function () {
        update({pinned: $(this).is(':checked')});
    });
    $(document).on('click.promptinjection', '.pi-refresh', load);
    $(document).on('submit.promptinjection', '#pi-task1-form, #pi-task2-form', function () {
        setTimeout(load, 300);
    });

    load();
})();
