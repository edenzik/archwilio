angular.module('ngResource', ['ng']).
  provider('$resource', function() {
    var PROTOCOL_AND_DOMAIN_REGEX = /^https?:\/\/[^\/]*/;
    var provider = this;

    this.defaults = {
      // Strip slashes by default
      stripTrailingSlashes: true,

      // Default actions configuration
      actions: {
        'get': {method: 'GET'},
        'save': {method: 'POST'},
        'query': {method: 'GET', isArray: true},
        'remove': {method: 'DELETE'},
        'delete': {method: 'DELETE'}
      }
  };