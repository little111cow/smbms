//时间
function fn(){
    var time = new Date();
    var str= "";
    var div = document.getElementById("time");
//    console.log(time);
    var year = time.getFullYear();
    var mon = time.getMonth()+1;
    var day = time.getDate();
    var h = time.getHours();
    var m = time.getMinutes();
    var s = time.getSeconds();
    var week = time.getDay();
    switch (week){
        case 0:week="Sunday";
            break;
        case 1:week="Monday";
            break;
        case 2:week="Tuesday";
            break;
        case 3:week="Wednesday";
            break;
        case 4:week="Thursday";
            break;
        case 5:week="Friday";
            break;
        case 6:week="Saturday";
            break;
    }
    str = year +"-"+totwo(mon)+"-"+totwo(day)+"&nbsp;"+totwo(h)+":"+totwo(m)+":"+totwo(s)+"&nbsp;"+"The Day:"+week;
    div.innerHTML = str;
}
fn();
setInterval(fn,1000);
function totwo(n){
    if(n<=9){
        return n = "0"+n;
    }
    else{
        return n =""+n;
    }
}

