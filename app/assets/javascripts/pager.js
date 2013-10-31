function Pager(pagingMode, pageSize, listUrl, countUrl){
    var self = this;
    self.pagingMode = ko.observable(pagingMode);
    self.items = ko.observableArray([]);
    self.currentPage = ko.observable();
    self.pageSize = ko.observable(pageSize);
    self.itemCount = ko.observable();
    self.sortBy = ko.observable();
    self.sortDir = ko.observable();

    countUrl.ajax({
        success:function(data){
            self.itemCount(data.count);
        },
        error:function(){
            console.log("Could not load count");
        }
    })

    self.pageCount = ko.computed(function(){
        var pageCount = self.itemCount() / self.pageSize();
        if(self.itemCount() % self.pageSize() == 0){
            return pageCount;
        }else{
            return parseInt(pageCount) + 1;
        }
    })

    self.params = ko.computed(function(){
        var page = self.pagingMode() == true ? self.currentPage() : '';
        var pageSize = self.pagingMode() == true ? self.pageSize() : '';
        var sortBy = self.sortBy() ? self.sortBy() : '';
        var sortDir = self.sortDir() ? self.sortDir() : '';
        return {page: page, pageSize: pageSize, sortBy: sortBy, sortDir: sortDir}
    })

    self.sort = function(column){
        if(self.sortBy() == column && self.sortDir() != "desc"){
            self.sortDir("desc");
        }else{
            self.sortDir("asc");
        }
        self.sortBy(column);
        self.loadItems();
    }

    self.sortClass = function(column){
        var icon = "icon-sort";
        if(self.sortBy() == column){
            if(self.sortDir() == "desc"){
                icon += "-down";
            }else{
                icon += "-up"
            }
        }
        return icon;
    }

    self.loadItems = function(){
        var sortBy = self.sortBy() ? self.sortBy() : '';
        var sortDir = self.sortDir() ? self.sortDir() : '';
        var items = listUrl(self.currentPage(), self.pageSize(), sortBy, sortDir);
        self.items(items());
        setClickable();
    }

    self.currentPage.subscribe(function(){
        self.loadItems();
    })

    if(pagingMode){
        self.currentPage(0);
    }else{
        self.loadItems();
    }

    self.first = function(){
        self.currentPage(0);
    }

    self.prev = function(){
        self.currentPage(self.currentPage() - 1);
    }

    self.pageLabel = ko.computed(function(){
        return (self.currentPage() + 1) + "/" + self.pageCount() + " of " + self.itemCount();
    })

    self.next = function(){
        self.currentPage(self.currentPage() + 1);
    }

    self.last = function(){
        self.currentPage(self.pageCount() - 1);
    }
}