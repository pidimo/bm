angular.module('starter.rolescontrollers', ['starter.rolesservices'])

// CONTROLLER APP
.controller('AppCtrl', function($localstorage,$scope, $ionicModal, $timeout, $ionicPopup,COLOR_VIEW,COLOR_2) {
  $scope.colorFont = COLOR_VIEW;
  $('ion-nav-bar').css({"color":COLOR_2});

  $scope.accessRight = $localstorage.getObject('accessRight');  

  $scope.accessRightMail = $scope.accessRight.MAIL.VIEW;
  $scope.accessRightAppointment = $scope.accessRight.APPOINTMENT.VIEW;
  $scope.accessRightContact = $scope.accessRight.CONTACT.VIEW;
  $scope.accessRightPerson = $scope.accessRight.CONTACT.VIEW;
  
  //var firstUse = $localstorage.get("starter",null);
  var firstUse = null;
  if(firstUse == null){ 
  
  }
  else{

  }

})

.controller('ControlStartPage', function($localstorage,$scope, $ionicModal, $timeout, $ionicPopup,COLOR_VIEW,COLOR_2) {
  $scope.colorFont = COLOR_VIEW;
  $('ion-nav-bar').css({"color":COLOR_2});

  

})



//  CONTROLLER LOGIN

.controller('LoginController', function (LogoutService,COLOR_VIEW,$filter,$localstorage,$translate,$templateCache,$window,LoginService,apiUrlLocal,pathLogon,$ionicPopup,$scope,$ionicModal, AuthenticationService,$state,$http,$ionicLoading) {

    'use strict';

    $scope.callResult = LogoutService.query({});
     
    $scope.callResult.$promise.then(function (results){
      console.log("==CONTROLLER ROLES== logout",results);
    })

    delete $http.defaults.headers.common.Authorization;

    $scope.colorFont = COLOR_VIEW;


    if(typeof $localstorage.getObject("rememberUsername") == "string")
    {
      $scope.data = {username: $localstorage.getObject("rememberUsername") ,password: "", company: $localstorage.getObject("rememberCompany")};

      var request = $http({
        method: "get",        
        url: apiUrlLocal+"/bmapp/Logon/Company/DownloadLogoImage.do?dto(companyLogin)="+$localstorage.getObject("rememberCompany")+"&isCompanyLogo=true",                        
      });      
      request.success(
        function(data, status, headers, config) {          
          if(data.forward != "Fail")
          {
            $scope.imageUrlCompany= "/bmapp/Logon/Company/DownloadLogoImage.do?dto(companyLogin)="+$localstorage.getObject("rememberCompany")+"&isCompanyLogo=true";
            $scope.imageUrlCompany=apiUrlLocal+$scope.imageUrlCompany;            
          }
          
        }
      );      
    }
    else
      $scope.data= {};
         
    $scope.iframeHeight = $(window).height();
    $scope.iframeWidth = $(window).width();
    
    $ionicModal.fromTemplateUrl('templates/login.html', {
      scope: $scope
    }).then(function(modal) {
      $scope.modal = modal;
    });

    $scope.ChangeLanguage = function(lang){
      $translate.use(lang);
    }

    $scope.searchCadeco = function(){
      
      // $scope.data = {username: "admin" ,password: "administrador", company: "cadeco"}
      $scope.dataa = {username: "admin" ,password: "administrador", company: "cadeco"}
      // Simple POST request
        $http({
          method: 'POST',
          url: apiUrlLocal+""+pathLogon,
          data: {"dto(login)":$scope.dataa.username, "dto(companyLogin)":$scope.dataa.company, "dto(password)":$scope.dataa.password, "dto(language)":"en","dto(rememberInfo)":true}
        }).success(function(data, status, headers, config) {
          
          console.log('==CONTROLLER LOGIN== REQUEST SUCCESS OK',data);
          
          if( data.mainData)
          {
            // cadeco only see contacts
            var accessRight = data.mainData.accessRight;
            accessRight.APPOINTMENT.VIEW = "false";
            accessRight.MAIL.VIEW = "false";

            $localstorage.setObject('accessRight',accessRight);
            $localstorage.setObject('userInfo',data.mainData.userInfo);

            var lenguage = data.mainData.userInfo.locale;
            console.log("==CONTROLLER LOGIN==  lenguage: ",lenguage );
            $scope.ChangeLanguage(lenguage);
            // AuthenticationService.login({name: $scope.data.username, company: $scope.data.company});
            
            if (data.mainData.accessRight.CONTACT.VIEW == "true") {
              $state.go('app.contactsCadeco');
            }
          }
          else
          {
            // popup credencial failed
            var message = $filter('translate')('Messagefailed');
            var alertPopup = $ionicPopup.alert({
               title: message
            });
            
            $state.go('login');
            
          }
        }).
        error(function(data, status, headers, config) {
         console.log('==CONTROLLER LOGIN== REQUEST SUCCESS ERROR', data);
         
        });
    }

    // logout
    $scope.closeLogin = function() {

      console.log('==CONTROLLER ROLES== logout', $scope.data);
      
      // $state.go('login');
      // $scope.modal.hide();

      // console.log('==CONTROLLER ROLES== logout', $scope.data);      
      // $state.go('login');
      // $scope.modal.hide();

    };

    // Session.destroy();
    $scope.closeLogin();

    // login
    $scope.doLogin = function() {     
      console.log('==CONTROLLER ROLES== data from UI:', $scope.data);
      
      // do nothing if data es null
      if ($scope.data.username != null && $scope.data.password != null && $scope.data.company != null) {
        
        // Simple POST request
        $http({
          method: 'POST',
          url: apiUrlLocal+""+pathLogon,
          data: {"dto(login)":$scope.data.username, "dto(companyLogin)":$scope.data.company, "dto(password)":$scope.data.password, "dto(language)":"en","dto(rememberInfo)":true}
        }).success(function(data, status, headers, config) {
          
          console.log('==CONTROLLER LOGIN== REQUEST SUCCESS OK',data);

          
          if( data.forward != "Fail")
          {
            $localstorage.setObject('accessRight',data.mainData.accessRight);
            $localstorage.setObject('userInfo',data.mainData.userInfo);
            $localstorage.setObject('rememberUsername',$scope.data.username);
            $localstorage.setObject('rememberCompany',$scope.data.company);

            var lenguage = data.mainData.userInfo.locale;
            console.log("==CONTROLLER LOGIN==  lenguage: ","-"+lenguage+"-" );
            $scope.ChangeLanguage(lenguage);
            AuthenticationService.login({name: $scope.data.username, company: $scope.data.company});
            
            if (data.mainData.accessRight.CONTACT.VIEW == "true") {
              $state.go('app.contacts');
            }
            else{
              if (data.mainData.accessRight.APPOINTMENT.VIEW == "true") {
                $state.go('app.schedulerDay');
              }
              else{
                if (data.mainData.accessRight.MAIL.VIEW == "true") {
                  $state.go('app.mailboxes');
                }
                else{
                  $state.go('app.startPage');      
                }
              }
            }
          }
          else
          {
            // popup credencial failed
            var message = $filter('translate')('Messagefailed');
            var alertPopup = $ionicPopup.alert({
               title: message
            });
            
            $state.go('login');
            
          }
        }).
        error(function(data, status, headers, config) {
         console.log('==CONTROLLER LOGIN== REQUEST SUCCESS ERROR', data);
        });  
      }
      else
      {
        // popup credencial failed
            var message = $filter('translate')('MessageRequired');
            var alertPopup = $ionicPopup.alert({
               title: message
            });

            $state.go('login');
      }
      
    };

})

//  CONTROLLER LOGOUT
.controller('logoutController', function(LogoutService,$translate,$localstorage,apiUrlLocal,pathLogon,$rootScope,$http,$scope, $state,AuthenticationService){
    'use strict';
    console.log("-------111111111---------");

    $scope.callResult = LogoutService.query({});

    $scope.callResult.$promise.then(function (results){
      console.log("==aaaa=======logout==",$scope.results);
    })
    
    // delete $http.defaults.headers.common.Authorization;
    // Session.destroy();
    // AuthenticationService.logout();

$scope.data = {};
    // login
    $scope.doLogin = function() {
      console.log('==CONTROLLER ROLES== data from UI:', $scope.data);
      console.log('====================:', $scope.data.username);
      
      // do nothing if data es null
      if ($scope.data.username != null && $scope.data.password != null && $scope.data.company != null) {
        
        // Simple POST request
        $http({
          method: 'POST',
          url: apiUrlLocal+""+pathLogon,
          data: {"dto(login)":$scope.data.username, "dto(companyLogin)":$scope.data.company, "dto(password)":$scope.data.password, "dto(language)":"en","dto(rememberInfo)":true}
        }).success(function(data, status, headers, config) {
          
          console.log('==CONTROLLER LOGIN== REQUEST SUCCESS OK',data);

          
          if( data.mainData)
          {
            $localstorage.setObject('accessRight',data.mainData.accessRight);
            $localstorage.setObject('userInfo',data.mainData.userInfo);

            var lenguage = data.mainData.userInfo.locale;
            console.log("==CONTROLLER LOGIN==  lenguage: ",lenguage );
            $scope.ChangeLanguage(lenguage);
            AuthenticationService.login({name: $scope.data.username, company: $scope.data.company});
            $scope.closeLogin();
            
            if (data.mainData.accessRight.CONTACT.VIEW == "true") {
              $state.go('app.contacts');
            }
            else{
              if (data.mainData.accessRight.APPOINTMENT.VIEW == "true") {
                $state.go('app.schedulerDay');
              }
              else{
                if (data.mainData.accessRight.MAIL.VIEW == "true") {
                  $state.go('app.mailboxes');
                }
                else{
                  $state.go('app.startPage');      
                }
              }
            }
          }
          else
          {
            // popup credencial failed
            var message = $filter('translate')('Messagefailed');
            var alertPopup = $ionicPopup.alert({
               title: message
            });
            
            $state.go('login');
            
          }
        }).
        error(function(data, status, headers, config) {
         console.log('==CONTROLLER LOGIN== REQUEST SUCCESS ERROR', data);
        });  
      }
      else
      {
        // popup credencial failed
            var message = $filter('translate')('MessageRequired');
            var alertPopup = $ionicPopup.alert({
               title: message
            });

            $state.go('login');
      }
      
    };

    $scope.ChangeLanguage = function(lang){
      $translate.use('en');
    }
});
