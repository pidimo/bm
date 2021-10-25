angular.module('starter.webmailcontrollers', ['starter.webmailservices','starter.constantsWebmail'])

/**
 * CONTROLLER FOLDERS
 */
.controller('MailsCtrl', function(PopupFactory,$state,$scope,Webmal_read_forlders,COLOR_VIEW,PATH_WEBMAIL_READ_FOLDERS,
  FOLDER_INBOX_ID,FOLDER_INBOX_NAME,FOLDER_SENT_ID,FOLDER_SENT_NAME,FOLDER_DRAFT_ID,
  FOLDER_DRAFT_NAME,FOLDER_TRASH_ID,FOLDER_TRASH_NAME,FOLDER_OUTBOX_ID,FOLDER_OUTBOX_NAME,
  FOLDER_INBOX_TYPE,FOLDER_SENT_TYPE,FOLDER_DRAFT_TYPE,FOLDER_TRASH_TYPE,FOLDER_OUTBOX_TYPE,
  FOLDER_DEFAULT_TYPE) {

  // COLOR DEFAULT
  $scope.colorFont = COLOR_VIEW;
  // $('icon').css({"color":COLOR_2});
  
  //  CALL SERVICES WEBMAIL FOLDERS
  console.log("==CONTROLLER WEBMAIL== get query list FOLDERS");
  $scope.newFolders = Webmal_read_forlders.query();
  $scope.folders = [];

  // PROMISE
  $scope.newFolders.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    console.log("==CONTROLLER WEBMAIL== get query list FOLDERS success OK",results['mainData']);
    var data = ((results['mainData'])['systemFolders']);

    // CONVERT DATA OF FOLDERS DEFAULT IN OBJECT
    var object_folder_inbox = {folderId: data[FOLDER_INBOX_ID],folderName:'Inbox',type:FOLDER_INBOX_TYPE};
    var object_folder_sent = {folderId: data[FOLDER_SENT_ID],folderName:'SentItems',type:FOLDER_SENT_TYPE};
    var object_folder_draft = {folderId: data[FOLDER_DRAFT_ID],folderName:'DraftItems',type:FOLDER_DRAFT_TYPE};
    var object_folder_trash = {folderId: data[FOLDER_TRASH_ID],folderName:'Trash',type:FOLDER_TRASH_TYPE};
    var object_folder_outbox = {folderId: data[FOLDER_OUTBOX_ID],folderName:'Outbox',type:FOLDER_OUTBOX_TYPE};
    
    //  PUSH OBJECT
    $scope.folders.push(object_folder_inbox);
    $scope.folders.push(object_folder_sent);
    $scope.folders.push(object_folder_draft);
    $scope.folders.push(object_folder_trash);
    $scope.folders.push(object_folder_outbox);

    //  PUSH CUSTOM FOLDERS
    var custom_folders = ((results['mainData'])['customFolderArray']);    
    custom_folders.forEach(function(folder) {
      var custom_folder = folder;
      custom_folder["type"] = FOLDER_DEFAULT_TYPE;
      $scope.folders.push(custom_folder);
    });

  });

  $scope.doRefresh = function() {
    $scope.folders = [];
    //  CALL SERVICES WEBMAIL FOLDERS
    console.log("==CONTROLLER WEBMAIL== do refresh FOLDERS");
    $scope.newFolders = Webmal_read_forlders.query();

    // PROMISE
    $scope.newFolders.$promise.then(function (results){

      // call factory 
      PopupFactory.getPopup($scope,results);

      console.log("==CONTROLLER WEBMAIL== get query refresh list FOLDERS success OK",results['mainData']);
      var data = ((results['mainData'])['systemFolders']);

      // CONVERT DATA OF FOLDERS DEFAULT IN OBJECT
      var object_folder_inbox = {folderId: data[FOLDER_INBOX_ID],folderName:data[FOLDER_INBOX_NAME],type:FOLDER_INBOX_TYPE};
      var object_folder_sent = {folderId: data[FOLDER_SENT_ID],folderName:data[FOLDER_SENT_NAME],type:FOLDER_SENT_TYPE};
      var object_folder_draft = {folderId: data[FOLDER_DRAFT_ID],folderName:data[FOLDER_DRAFT_NAME],type:FOLDER_DRAFT_TYPE};
      var object_folder_trash = {folderId: data[FOLDER_TRASH_ID],folderName:data[FOLDER_TRASH_NAME],type:FOLDER_TRASH_TYPE};
      var object_folder_outbox = {folderId: data[FOLDER_OUTBOX_ID],folderName:data[FOLDER_OUTBOX_NAME],type:FOLDER_OUTBOX_TYPE};
      
      //  PUSH OBJECT
      $scope.folders.push(object_folder_inbox);
      $scope.folders.push(object_folder_sent);
      $scope.folders.push(object_folder_draft);
      $scope.folders.push(object_folder_trash);
      $scope.folders.push(object_folder_outbox);

      //  PUSH CUSTOM FOLDERS
      var custom_folders = ((results['mainData'])['customFolderArray']);    
      custom_folders.forEach(function(folder) {
        var custom_folder = folder;
        custom_folder["type"] = FOLDER_DEFAULT_TYPE;
        $scope.folders.push(custom_folder);
      });

      $scope.$broadcast('scroll.refreshComplete');  

    });
    
  };
  
  //  RETURN CLASS ICON OF FOLDER
  $scope.getClassImage = function(item){
    var response;
    var value = item.type;

    switch (value) {
        case FOLDER_INBOX_TYPE:
            response = "icon ion-filing";
            break;
        case FOLDER_SENT_TYPE:
            response = "icon ion-paper-airplane";;
            break;
        case FOLDER_DRAFT_TYPE:
            response = "icon ion-briefcase";;
            break;    
        case FOLDER_TRASH_TYPE:
            response = "icon ion-trash-b";;
            break;    
        case FOLDER_OUTBOX_TYPE:
            response = "icon ion-ios7-box";;
            break;            
        default:
            response = "icon ion-folder"
    }
    return response;
  };
})

// MAILLISTS IN FORDER (REFRESH / LOADMORE)
.controller('MailsListCtrl',function(PopupFactory,$filter,$ionicPopup,$scope,COLOR_VIEW,apiUrlLocal, Mail,$timeout,$ionicLoading,$resource,$stateParams){
  // COLOR DEFAULT
  $scope.colorFont = COLOR_VIEW;

  console.log('==CONTROLLER WEBMAIL== STARTING');

  // NUMBER PAGE EQUAL 1 -- TOTAL PAGES 
  $scope.page = 1; 
  $scope.totalPages; 
  $scope.apiUrlLocal = apiUrlLocal;

  $scope.folderName = $stateParams.folderName;
  console.log("folderName======== ",$scope.folderName);

  //  CALL SERVICES WITH (PAGE NUMBER AND FOLDER ID)
  console.log("==CONTROLLER WEBMAIL== get query list mails first time");
  $scope.newMailList = Mail.query({'pageParam(pageNumber)':$scope.page,'folderId':$stateParams.folderId});
  $scope.mailList = [];

  // REFRESH FALSE IF THERE ARE NOT MORE MAILS
  $scope._doRefresh = false;

  $scope.realDate = 

  // PROMISE
  $scope.newMailList.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

      console.log('==CONTROLLER WEBMAIL== get query list mails success OK',results['mainData']);
      
      //  SAVE PAGE NUMBER
      $scope.page = parseInt((results['mainData'])['pageInfo']['pageNumber']);

      // SAVE TOTAL PAGES
      $scope.totalPages = parseInt(results['mainData']['pageInfo']['totalPages']);

      // RECOVER LIST MAIL FOR VIEW
      $scope.mailList = (results['mainData'])['list'];

      // REFRESH = TRUE IF LIST > 0 AND TOTAL PAGES > PAGE ACTUAL
      if ($scope.mailList.length > 0 && $scope.totalPages>$scope.page) {
          $scope._doRefresh = true;  
      };

      // if no exists items show message
      if ($scope.mailList.length == 0) {
        // An alert dialog
        var message = $filter('translate')('NoItems');
        var messageSelect = $filter('translate')('SelectAnother');

        console.log("==CONTROLLER CONTACTS== alert if no exists items");
        var alertPopup = $ionicPopup.alert({
            title: message,
            template: messageSelect
        });
      }
      
  });

  $scope.getDateMilli22 = function(milliseconds){
    // var offset = -4;
    // var aaaa = new Date( new Date().getTime() + offset * 3600 * 1000).toUTCString().replace( / GMT$/, "" )

    // console.log("-----asdf",aaaa);
  
    var daa = new Date(+milliseconds);

    console.log("------tz.name()",daa.getTimezoneOffset()); 
    return daa;
  }

  $scope.getDateMilli = function(milliseconds){
    
    var day_Send = new Date(+milliseconds);
    
    var dd = day_Send.getDate();
    var mm = day_Send.getMonth();
    var yy = day_Send.getFullYear();
    
    var date = new Date();
    
    if ( (dd == date.getDate()) && (mm == (date.getMonth())) && (yy == date.getFullYear()) ) {
      
      var hhh = day_Send.getHours();
      var mmmm = (day_Send.getMinutes()).toString();
      
      var mmm = (mmmm).length < 2 ? "0"+mmmm : mmmm;
      // var item.contactPersonAddressId ==='' ?  : ;
      return (hhh+":"+mmm);  
    }
    else {
      var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds
      // var firstDate = new Date(date.getFullYear(),date.getMonth()+1,date.getDate());
      // var secondDate = new Date(yy,mm,dd);

      var diffDays = Math.round(Math.abs((date.getTime() - day_Send.getTime())/(oneDay)));
      // return diffDays;
      if ( diffDays <= 6  ) {

        var Monday = $filter('translate')('Monday');
        var Tuesday = $filter('translate')('Tuesday');
        var Wednesday = $filter('translate')('Wednesday');
        var Thursday = $filter('translate')('Thursday');
        var Friday = $filter('translate')('Friday');
        var Saturday = $filter('translate')('Saturday');
        var Sunday = $filter('translate')('Sunday');

                
        var weekdays = new Array(7);
        weekdays[4] = Thursday;
        weekdays[5] = Friday;
        weekdays[6] = Saturday;
        weekdays[0] = Sunday;
        weekdays[1] = Monday;
        weekdays[2] = Tuesday;
        weekdays[3] = Wednesday;

        console.log("00000-=-=-",weekdays);
        
        var day = day_Send.getDay();
        
        return weekdays[day];
      }
      else{// "28/05/2015 23:25"
        var d = (dd.toString()).length < 2 ? "0"+dd : dd;
        var m = (mm.toString()).length < 2 ? "0"+mm : mm;
        
        var ddddd = d+"-"+m+"-"+(yy.toString()).substring(2, 4);
        return ddddd;
      }
    }


    
    
    // return date.getDate();
  }
  
  $scope.doRefresh = function() {
    $scope.page = 1; 

    //  CALL SERVICES WITH (PAGE NUMBER AND FOLDER ID)
    console.log('==CONTROLLER WEBMAIL== do refresh');
    $scope.newMailList = Mail.query({'pageParam(pageNumber)':$scope.page,'folderId':$stateParams.folderId});

    // PROMISE
    $scope.newMailList.$promise.then(function (results){

      // call factory 
      PopupFactory.getPopup($scope,results);

      console.log("==CONTROLLER WEBMAIL==  query doRefresh success OK data: ",results['mainData']);

      //  MAIL LIST
      $scope.mailList = (results['mainData'])['list'];
      $scope.$broadcast('scroll.refreshComplete');  

      
      if ($scope.mailList.length > 0 && $scope.totalPages>$scope.page) {
        $scope._doRefresh = true;  
      };  
    });
  }

  $scope.loadMore = function() {
      $scope.page = $scope.page + 1;

      //  CALL SERVICES WITH (PAGE NUMBER AND FOLDER ID)
      console.log('==CONTROLLER WEBMAIL== load more');
      $scope.newMails = Mail.query({'pageParam(pageNumber)':$scope.page,'folderId':$stateParams.folderId});

      // PROMISE
      $scope.newMails.$promise.then(function(results){
        // call factory 
        PopupFactory.getPopup($scope,results);

        console.log("==CONTROLLER WEBMAIL==  query loadMore success OK data: ",results['mainData']);
        $scope.mailList = $scope.mailList.concat((results['mainData'])['list']);
        $scope.$broadcast('scroll.infiniteScrollComplete');

        // REFRESH = FALSE IF TOTAL PAGES <= PAGE ACTUAL ++
        if ($scope.totalPages<=$scope.page+1) {
          $scope._doRefresh = false;  
        };
      });  
    }
  })

// NEW MAIL 
.controller('NewMail',function($stateParams,$scope,COLOR_VIEW){
  $scope.data = {};
  $scope.colorFont = COLOR_VIEW;
  $scope.data.to = $stateParams.to;
  
  $scope.sendMail = function() {
    console.log('---send data new mail', $scope.data);
    // var mailAccountId = $scope.data.mailAccountId;
    // var to = $scope.data.to;
    // console.log("mailAccountId",mailAccountId);
    // console.log("to",to);
  }

})

// DETAILS MAIL
.controller('MailDetailCtrl', function(PopupFactory,$filter,$scope,$cordovaFileTransfer,$http,$sce,$ionicPopup,$ionicLoading,$stateParams,Mail,apiUrlLocal,PATH_WEBMAIL,BODY_TYPE_HTML,BODY_TYPE_HTML) {
  
    console.log("==WEBMAIL CONTROLLER DETAILS MAIL== start");

    //  CALL SERVICES WITH (PAGE NUMBER AND FOLDER ID)
    $scope.detail = Mail.query({'dto(mailId)': $stateParams.mailId,folderId: $stateParams.folderId});
    $scope.item = {};

    // PROMISE
    $scope.detail.$promise.then(function (results){

      // call factory 
      PopupFactory.getPopup($scope,results);

        console.log("==CONTROLLER WEBMAIL==  query detail success OK data: ",results['mainData']);
        $scope.item = (results['mainData'])['entity'];

        //SPLIT STRING TO ARRAY CC
        var cc = ((results['mainData'])['entity'])['cc'];
        $scope.arrayCC = [];
        $scope.arrayCC.push(cc);
      
        //SPLIT STRING TO ARRAY BCC
        var bcc = ((results['mainData'])['entity'])['bcc'];
        $scope.arrayBCC = [];
        $scope.arrayBCC.push(bcc);

        // this callback will be called asynchronously
        // CALL HTML BODY
        if (results['mainData']['entity']['bodyType'] == BODY_TYPE_HTML) {
            
            var newurl = results['mainData']['entity']['htmlBodyUrl'];
            $http.get(apiUrlLocal+newurl).

            success(function(data, status, headers, config) {

              // call factory 
              PopupFactory.getPopup($scope,data);

              var newHtml = data.split("<img").join(" <img class='img-class' ");
              // var newHtml = data.substr(0, pos+5) + "width='100%'" + data.substr(pos+5);
              $scope.thisCanBeusedInsideNgBindHtml = $sce.trustAsHtml(newHtml);
                // when the response is available
                console.log("==CONTROLLER WEBMAIL== html body",newHtml);
              }).
            error(function(data, status, headers, config) {
              // or server returns response with an error status.
            });
        }
        else{
          angular.element(document).ready(function () {
            console.log('page loading completed');
            var element = document.getElementById("page_content");
            element.style.height = element.scrollHeight + "px";
          });
        }
        

        $scope.iframeHeight = $(window).height();
        $scope.iframeWidth = $(window).width();

    });

    // DOWNLOAD FILE
    $scope.download = function(attach) {
        // $ionicLoading.show({
        //   template: 'Loading...'
        // });
        console.log("1111111");
        window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fs) {
            fs.root.getDirectory(
                "BMapp",
                {
                    create: true
                },
                function(dirEntry) {
                    console.log("==CONTROLLER WEBMAIL== download attach CREATE folder");
                    dirEntry.getFile(
                        attach.fileName, 
                        {
                            create: true, 
                            exclusive: false
                        }, 
                        function gotFileEntry(fe) {
                            console.log("==CONTROLLER WEBMAIL== download attach url");
                            var p = fe.toURL();
                            fe.remove();
                            ft = new FileTransfer();
                            ft.download(
                                // encodeURI("http://ionicframework.com/img/ionic-logo-blog.png"),
                                encodeURI(apiUrlLocal+attach.downloadUrl),
                                
                                p,
                                function(entry) {
                                    // $ionicLoading.hide();
                                    $scope.imgFile = entry.toURL();
                                },
                                function(error) {
                                    // $ionicLoading.hide();
                                    alert("Download Error Source -> " + error.source);
                                },
                                false,
                                null
                            );
                        }, 
                        function() {
                            // $ionicLoading.hide();
                            console.log("Get file failed");
                        }
                    );
                }
            );
        },
        function() {
            // $ionicLoading.hide();
            console.log("Request for filesystem failed");
        });
        //
    }


    // URL IMAGE
    $scope.imageF = function(){
      $scope.imageFrom = "img/user_default_unknown.png";

      if ($stateParams.imageFrom != null) {
        $scope.imageFrom = apiUrlLocal+$stateParams.imageFrom+'='+$stateParams.fromImageId;
      }
      return $scope.imageFrom;
    };

    $scope.group = {name: "grupo1"};
    $scope.group2 = {name: "grupo2"};
    $scope.group3 = {name: "grupo3"};

    // ACORDEON HELP
    $scope.toggleGroup = function(group) {
      if ($scope.isGroupShown(group)) {
        $scope.shownGroup = null;
      } else {
        $scope.shownGroup = group;
      }
    };

    // ACORDEON HELP
    $scope.isGroupShown = function(group) {
      return $scope.shownGroup === group;
    };

    $scope.updateEditor = function() {
      angular.element(document).ready(function () {
          console.log('page loading completed');
      });
      // var element = document.getElementById("page_content");
      // element.style.height = element.scrollHeight + "px";
    };

});


// angular.module('MyModule', [])
//     .controller('MyController', function ($scope) {
//     $scope.myfunction = function (data) {
//         alert("---" + data);
//     };
// });

window.onload = function () {
    angular.element(document.getElementById('page_content')).scope().updateEditor();
}