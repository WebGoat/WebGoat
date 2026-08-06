/*
 * Backbone model that fetches the full user list from the admin REST endpoint.
 * The URL maps to GET /service/admin/users defined in AdminController.
 */
define(['backbone'], function (Backbone) {
  return Backbone.Collection.extend({
    url: 'service/admin/users'
  });
});
