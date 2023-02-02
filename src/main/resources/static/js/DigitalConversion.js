//数字转中文
function GetCN(number) {
    let num= parseInt(number);
    if (num.toString().length===1){
        return Assignment(GetBits(num));
    }else if (num.toString().length===2){
        return Assignment(GetTen(num));
    }else if (num.toString().length===3){
        return Assignment(GetHundreds(num));
    }else if (num.toString().length===4){
        return Assignment(GetThousand(num));
    }else if (num.toString().length===5){
        return Assignment(GetTenThousand(num));
    }else if (num.toString().length>5&&num.toString().length<=8){
        return Assignment(GetMoreMust(num));
    }else if (num.toString().length>8&&num.toString().length<=16){
        return Assignment(GetBillion(num));
    }
}
function Assignment(num) {
    var ass=document.getElementsByClassName("data-Assignment-ordinary");
    var bss=document.getElementsByClassName("data-Assignment-form");
    for (var a=0;a<ass.length;a++){
        ass[a].innerHTML=num;
    }
    for (var b=0;b<bss.length;b++){
        bss[b].setAttribute("value",num);
    }
    return true;
}
function GetBits(Bits) {
    switch (Bits.toString()) {
        case "0":
            return "零";
        case "1":
            return "一";
        case "2":
            return "二";
        case "3":
            return "三";
        case "4":
            return "四";
        case "5":
            return "五";
        case "6":
            return "六";
        case "7":
            return "七";
        case "8":
            return "八";
        case "9":
            return "九";
    }
}
function GetTen(Ten) {
    var bits=GetBits(Ten.toString().substring(Ten.toString().length-1,Ten.toString().length));
    var ten=GetBits(Ten.toString().substring(Ten.toString().length-2,Ten.toString().length-1));
    var connect="十";
    if (bits==="零"){
        bits="";
    }
    if (ten==="零"){
        ten="";
        connect="零";
    }
    if (ten===""&&bits===""){
        connect="";
    }
    return ten+connect+bits;
}
function GetHundreds(Hundred) {
    var hundred= GetBits(Hundred.toString().substring(Hundred.toString().length-3,Hundred.toString().length-2));
    var ten=GetTen(Hundred);
    var connect="百";
    if (hundred==="零"){
        hundred="";
        connect="零";
        if (ten===""||ten.toString().substring(0,1)==="零"){
            connect="";
        }
    }
    return hundred+connect+ten;
}
function GetThousand(Thousand) {
    var thousand=GetBits(Thousand.toString().substring(Thousand.toString().length-4,Thousand.toString().length-3));
    var hundred=GetHundreds(Thousand);
    var connect="千";
    if (thousand==="零"){
        thousand="";
        connect="零";
        if (hundred===""||hundred.toString().substring(0,1)==="零"){
            connect="";
        }
    }
    return thousand+connect+hundred;
}
function GetTenThousand(TenThousand) {
    var tenThousand=GetBits(TenThousand.toString().substring(TenThousand.toString().length-5,TenThousand.toString().length-4));
    var thousand=GetThousand(TenThousand);
    var connect="万";
    if (tenThousand==="零"){
        tenThousand="";
        connect="零";
        if (thousand===""||thousand.toString().substring(0,1)==="零"){
            connect="";
        }
    }
    return tenThousand+connect+thousand;
}
//万到千万
function GetMoreMust(MoreMust) {
    var thousand=GetThousand(MoreMust);
    var moreMust=null;
    var connect="万";
    switch (MoreMust.toString().length.toString()) {
        case "6":
            moreMust=GetTen(MoreMust.toString().substring(0,MoreMust.toString().length-4));
            break;
        case "7":
            moreMust=GetHundreds(MoreMust.toString().substring(0,MoreMust.toString().length-4));
            break;
        case "8":
            moreMust=GetThousand(MoreMust.toString().substring(0,MoreMust.toString().length-4));
            break;
    }
    if (moreMust===null||moreMust===""){
        connect="";
        if (thousand!==null&&thousand!==""){
            connect="零";
        }
    }
    return moreMust+connect+thousand;
}
//亿
function GetBillion(Billion) {
    var moreMust=GetMoreMust(Billion.toString().slice(-8));
    var billion;
    var connect="亿";
    switch (Billion.toString().length.toString()) {
        case "9":
            billion=GetBits(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "10":
            billion=GetTen(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "11":
            billion=GetHundreds(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "12":
            billion=GetThousand(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "13":
            billion=GetTenThousand(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "14":
            billion=GetMoreMust(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "15":
            billion=GetMoreMust(Billion.toString().substring(0,Billion.toString().length-8));
            break;
        case "16":
            billion=GetMoreMust(Billion.toString().substring(0,Billion.toString().length-8));
            break;
    }
    return billion+connect+moreMust;
}