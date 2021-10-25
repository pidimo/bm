angular.module('starter.services', [])

.factory("PopupFactory", function ($ionicPopup,$state) {
  
  function getPopup(scope,result) {


      // if session Expired
      if (result.forward == "SessionExpired") {
        console.log("==SERVICE ERROR== session expired:",result);
        
        return $ionicPopup.show({
         
          title: "Error",
          template: "Session Expired",
          scope: scope,
          buttons: [
            { text: '<b>close</b>',
              type: 'button-positive',
              onTap: function(e) {
                $state.go('login');  
              }
            },
          ]
        })

        
      };
      
      if (result.errorsArray) {
        console.log("==SERVICE ERROR== errors array:",result);
        var message = result.errorsArray[0].error;
        for (var i = 1; i < result.errorsArray.length; i++) {
          message=message+"<br>"+result.errorsArray[i].error ;
        };
        return $ionicPopup.show({
         
         title: 'Error',
         template: message,
         scope: scope,
         buttons: [
           { text: '<b>OK</b>',
           type: 'button-positive'
         },
           
         ]
        })  
      };
      console.log("==SERVICE ERROR== all good:",result);
  }
       
   return {
       getPopup: getPopup
   };  

})

.factory('myHttpInterceptor', function($q,$location,$injector) {
  return {

    'request': function(config) {
     $injector.get("$ionicLoading").show({
      template: '<i class="icon ion-loading-d" style="font-size: 32px"></i>',
      animation: 'fade-in',
      noBackdrop: false
    });
     return config;
   },

     //  // optional method
     // 'requestError': function(rejection) {
     //    // do something on error
     //    if (canRecover(rejection)) {
     //      return responseOrNewPromise
     //    }
     //    return $q.reject(rejection);
     //  },



      // optional method
      'response': function(response) {
        $injector.get("$ionicLoading").hide();
        return response;
      },

      // optional method
      'responseError': function(rejection) {
        $injector.get("$ionicLoading").hide();
        // do something on error
        if (rejection.status == 302){
          $location.path('/login');
        }
        // if (canRecover(rejection)) {
        //   return responseOrNewPromise
        // }
        return $q.reject(rejection);
      }
    };
  });
