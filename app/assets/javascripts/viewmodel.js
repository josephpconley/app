ko.bindingHandlers.numericValue = {
   init : function(element, valueAccessor, allBindingsAccessor) {
       var underlyingObservable = valueAccessor();
       var interceptor = ko.dependentObservable({
           read: underlyingObservable,
           write: function(value) {
               if (!isNaN(value)) {
                   underlyingObservable(parseFloat(value));
               }
           }
       });
       ko.bindingHandlers.value.init(element, function() { return interceptor }, allBindingsAccessor);
   },
   update : ko.bindingHandlers.value.update
};

function ExpressCodeRequest(data){
    var self = this;
    var q = ko.mapping.fromJS(data, {}, self);
    q.pending = ko.observable(false);
    q.show = ko.computed(function() {
        return jsRoutes.controllers.ect.Comdata.transaction(self.id()).url;
    });
    q.pending.subscribe(function(pending){
        if(pending){
            $("#refreshModal").modal("show");
        }else{
            $("#refreshModal").modal("hide");
        }
    });

    return q;
}

function ExpressCodeList(){
    var self = this;
    self.loadCodes = function(page, pageSize, sortBy, sortDir){
        var items = [];
        var url = jsRoutes.controllers.ect.Comdata.list(page, pageSize).url;
        if(sortBy.length > 0 && sortDir.length > 0){
            url = jsRoutes.controllers.ect.Comdata.list(page, pageSize, sortBy, sortDir).url;
        }

        $.ajax({
            url: url,
            async: false,
            success: function(data){
                items = ko.mapping.fromJS(data, {
                    create: function(options) {
                        return new ExpressCodeRequest(options.data);
                    }
                });
            },
            error:function(){
                console.log("Could not load data");
            }
         });
        return items;
    }
    self.pager = ko.observable(new Pager(true, 10, self.loadCodes, jsRoutes.controllers.ect.Comdata.count()));
    self.pager().sortBy("product");
    self.pager().sortDir("desc");

    self.status = function(expressCheck){
        expressCheck.pending(true);
        jsRoutes.controllers.ect.Comdata.status(expressCheck.id()).ajax({
            success: function(json){
                console.log(json);
                location.href = expressCheck.show();
            },
            error: function(json){
                console.log(json);
                $('.modal').modal('hide');
                error("Unable to check status, please try again momentarily.")
            }
        });
    }

    self.cancel = function(expressCheck){
        expressCheck.pending(true);
        jsRoutes.controllers.ect.Comdata.update(expressCheck.id(), "C").ajax({
            success: function(json){
                console.log(json);
                location.href = expressCheck.show();
            },
            error: function(json){
                console.log(json);
                $('.modal').modal('hide');
                error("Unable to cancel express code, please try again momentarily.")
            }
        });
    }
}

function User(data){
    var self = this;
    var q = ko.mapping.fromJS(data, {}, self);
    q.show = ko.computed(function() {
        return jsRoutes.controllers.ect.Users.edit(self.userId()).url;
    });
    return q;
}

function UserList(){
    var self = this;
    self.loadUsers = function(page, pageSize, sortBy, sortDir){
        var items = [];
        var url = jsRoutes.controllers.ect.Users.list(page, pageSize).url;
        if(sortBy.length > 0 && sortDir.length > 0){
            url = jsRoutes.controllers.ect.Users.list(page, pageSize, sortBy, sortDir).url;
        }

        $.ajax({
            url: url,
            async: false,
            success: function(data){
                items = ko.mapping.fromJS(data, {
                    create: function(options) {
                        return new User(options.data);
                    }
                });
            },
            error:function(){
                console.log("Could not load data");
            }
         });
        return items;
    }
    self.pager = ko.observable(new Pager(true, 10, self.loadUsers, jsRoutes.controllers.ect.Users.count()));
    self.pager().sortBy("lastName");
}