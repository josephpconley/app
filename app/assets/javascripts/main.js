function getNextSibling(n){
    y=n.nextSibling;
    while (y.nodeType!=1){
      y=y.nextSibling;
    }
    return y;
}

function error(msg){
    if(msg){
        $('#flash').html('<div class="alert alert-error"><button type="button" class="close" data-dismiss="alert">x</button><strong>' + msg + '</strong></div>');
    }
}

function warning(msg){
    if(msg){
        $('#flash').html('<div class="alert alert-warning"><button type="button" class="close" data-dismiss="alert">x</button><strong>' + msg + '</strong></div>');
    }
}

function success(msg){
    if(msg){
        $('#flash').html('<div class="alert alert-success"><button type="button" class="close" data-dismiss="alert">x</button><strong id="successMsg">' + msg + '</strong></div>');
    }
}

function toPrice(price){
    if(parseFloat(price) != 'NaN'){
        return parseFloat(price).toFixed(2);
    }else{
        return price;
    }
}

function toPriceFmt(price){
    if(price){
        return "$" + price.toFixed(2);
    }else{
        return "";
    }
}

function dateFmt(dateLong){
    if(dateLong){
        var d = new Date(dateLong);
        return d.toLocaleDateString();
    }else{
       return "";
    }
}

function timeFmt(dateLong){
    if(dateLong){
        var d = new Date(dateLong);
        return d.toLocaleDateString() + " " + d.toLocaleTimeString();
    }else{
       return "";
    }
}

function setClickable(){
    //clickable table MUST have thead and tbody defined
    $('.clickable > tbody > tr').click(function() {
        location.href = $(this).find("input").val();
    });
}