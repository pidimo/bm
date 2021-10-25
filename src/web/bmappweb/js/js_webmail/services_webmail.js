angular.module('starter.webmailservices', [])
/**
 * A simple example service that returns some data.
 */

/**
 * SERVICES LIST MAILS IN WEBMAIL
 */
 .factory('Mail', function ($resource,apiUrlLocal,PATH_WEBMAIL) {
  
  var url = apiUrlLocal+PATH_WEBMAIL;
  console.log('==SERVICE WEBMAIL== URL',url);
  return $resource(url,{},{'query':{method:'GET', isArray:false}});
})


/**
 * SERVICES READ FORLDERS
 */
 .factory('Webmal_read_forlders', function ($resource,apiUrlLocal,PATH_WEBMAIL_READ_FOLDERS) {
  
  var url = apiUrlLocal+PATH_WEBMAIL_READ_FOLDERS;
  console.log('==SERVICE WEBMAIL== read forlders',url);
  return $resource(url,{},{'query':{method:'GET', isArray:false}});
})


/**
*SERVICES FOLDERS IN WEBMAIL
*/
.factory('MailsSubMenu', function($q,$timeout) {
  console.log("==SERVICES WEBMAIL== GET FOLDERS OF WEBMAIL TO BM");
  
 
  var getContacts = function() {
    var deferred = $q.defer();

    // $timeout( function(){
        deferred.resolve([
            {id: 'inbox',name: 'inbox',face:'img/android-archive.png'},
            {id: 'sentItems',name: 'sentItems',face:'img/paper-airplane.png'},
            {id: 'draftItems',name: 'draftItems',face:'img/android-mail.png'},
        ]);

    // }, 1500);
    return deferred.promise;
  };

  return {
      getContacts : getContacts
  }
});