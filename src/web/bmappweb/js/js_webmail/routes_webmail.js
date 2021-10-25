
angular.module('starter.webmailroutes', ['starter.webmailcontrollers'])


.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider

  // ROUTE MAIL DETAIL
  .state('app.details-mail', {  
      url: '/mail-detail?mailId&folderId&imageFrom&fromImageId',
      cache: true,
      views: {
        'menuContent': {
          templateUrl: 'templates/views_webmail/mail-detail.html',
          controller: 'MailDetailCtrl'
        }
      }
  })

  //  ROUTE MAILLIST IN ANY FOLDER
  .state('app.mail-items', {
      url: '/mail-list?pageParam&folderId&folderName',
      cache: true,
      views: {
        'menuContent': {
          templateUrl: 'templates/views_webmail/mails-list.html',
          controller: 'MailsListCtrl'
        }
      }
  })

   // ROUTE LIST FOLDERS
   .state('app.mailboxes', {
      url: '/mailboxes',
      cache: false,
      views: {
        'menuContent': {
          templateUrl: 'templates/views_webmail/sub-menu-mails.html',
          controller: 'MailsCtrl'
        }
      }
    })

   // ROUTE NEW MAIL
   .state('app.newmail', {
      url: '/newmail?to',
      cache: true,
      views: {
        'menuContent': {
          templateUrl: 'templates/views_webmail/mail-new.html',          
          controller: 'NewMail'
        }
      }
    })

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/login');
});
