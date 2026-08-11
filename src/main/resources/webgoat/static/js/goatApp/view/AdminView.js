/*
 * Backbone view for the administration panel.
 * Follows the same pattern as ReportCardView.js.
 *
 * Fetches the user list from GET /service/admin/users via AdminModel,
 * renders the admin_panel.html Underscore template, and wires up:
 *   – username click  → load & display per-user lesson detail
 *   – Reset Password  → POST /service/admin/users/{username}/reset-password
 */
define([
    'jquery',
    'backbone',
    'underscore',
    'goatApp/model/AdminModel',
    'text!templates/admin_panel.html'
], function ($, Backbone, _, AdminModel, AdminTemplate) {

    return Backbone.View.extend({

        el: '#admin-panel-page',
        template: AdminTemplate,

        events: {
            'click .admin-reset-btn':   'resetPassword',
            'click .admin-detail-link': 'showDetail',
            'click .admin-detail-btn':  'showDetail'
        },

        initialize: function () {
            var _this = this;
            this.collection = new AdminModel();
            this.collection.fetch().then(function () {
                _this.render();
            }).fail(function () {
                _this.$el.html(
                    '<div class="alert alert-danger">Unable to load admin data. ' +
                    'Make sure you are logged in as an administrator.</div>'
                );
            });
        },

        render: function () {
            var t = _.template(this.template || AdminTemplate);
            this.$el.html(t({ users: this.collection.toJSON() }));
            return this;
        },

        // ── Detail view ──────────────────────────────────────────────────────

        showDetail: function (e) {
            e.preventDefault();
            var username = $(e.currentTarget).data('username');
            var $panel   = this.$el.find('#admin-detail-panel');
            var $catBody = this.$el.find('#admin-category-body');
            var $body    = this.$el.find('#admin-detail-body');
            var $heading = this.$el.find('#admin-detail-username');

            // Highlight selected row
            this.$el.find('#admin-user-table tr').removeClass('info');
            $(e.currentTarget).closest('tr').addClass('info');

            $heading.text((username || '').toLowerCase());
            $catBody.html('<div class="col-xs-12"><i>Loading categories…</i></div>');
            $body.html('<tr><td colspan="4"><i>Loading lessons…</i></td></tr>');
            $panel.show();

            // Scroll down to the detail panel smoothly
            $('html, body').animate({
                scrollTop: $panel.offset().top - 50
            }, 300);

            $.ajax({
                url: 'service/admin/users/' + encodeURIComponent(username),
                method: 'GET'
            }).done(function (data) {
                // Category Progress Cards
                var catHtml = '';
                _.each(data.categoryProgress, function (cat) {
                    var barClass = cat.percentage === 100 ? 'progress-bar-success' : 'progress-bar-info';
                    catHtml += '<div class="col-md-3 col-sm-6" style="margin-bottom:12px">' +
                        '<div style="border:1px solid #e7e7e7;padding:10px;border-radius:4px;background:#f9f9f9">' +
                        '<div><strong>' + _.escape(cat.category) + '</strong></div>' +
                        '<div style="font-size:12px;color:#666">' + cat.solvedLessons + ' / ' + cat.totalLessons + ' solved (' + cat.percentage + '%)</div>' +
                        '<div class="progress" style="margin-top:5px;margin-bottom:0;height:12px">' +
                        '<div class="progress-bar ' + barClass + '" role="progressbar" style="width:' + cat.percentage + '%;line-height:12px;font-size:10px">' +
                        (cat.percentage > 0 ? cat.percentage + '%' : '') +
                        '</div></div></div></div>';
                });
                $catBody.html(catHtml || '<div class="col-xs-12 text-muted">No category data.</div>');

                // Lesson Details Table
                var rows = '';
                _.each(data.lessonDetails, function (lesson) {
                    var solvedClass = lesson.solved ? 'success' : '';
                    rows += '<tr class="' + solvedClass + '">' +
                        '<td>' + _.escape(lesson.name)     + '</td>' +
                        '<td><span class="label label-info">' + _.escape(lesson.category) + '</span></td>' +
                        '<td>' + (lesson.solved ? '<span class="text-success">&#10003; Solved</span>' : '<span class="text-muted">Unsolved</span>') + '</td>' +
                        '<td>' + lesson.attempts            + '</td>' +
                        '</tr>';
                });
                $body.html(rows || '<tr><td colspan="4">No lesson data yet.</td></tr>');
            }).fail(function () {
                $catBody.html('<div class="col-xs-12 text-danger">Failed to load categories.</div>');
                $body.html('<tr><td colspan="4" class="text-danger">Failed to load detail.</td></tr>');
            });
        },

        // ── Password reset ───────────────────────────────────────────────────

        resetPassword: function (e) {
            var username  = $(e.currentTarget).data('username');
            var $result   = this.$el.find('#admin-reset-result');
            var $userSpan = this.$el.find('#admin-reset-username');
            var $pwSpan   = this.$el.find('#admin-reset-password');

            if (!confirm('Reset password for "' + username + '"?')) {
                return;
            }

            $.ajax({
                url:    'service/admin/users/' + encodeURIComponent(username) + '/reset-password',
                method: 'POST'
            }).done(function (data) {
                $userSpan.text(data.username);
                $pwSpan.text(data.temporaryPassword);
                $result.removeClass('alert-danger').addClass('alert-info').show();
            }).fail(function () {
                $userSpan.text(username);
                $pwSpan.text('(error – see server logs)');
                $result.removeClass('alert-info').addClass('alert-danger').show();
            });
        }

    });
});
