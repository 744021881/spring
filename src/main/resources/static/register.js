$(function () {
    // $("#exampleModal").show();
    $("#u_remove").hide();
    $("input[name='username']").on("input",function () {
        if($(this).val().length > 0){
            $("#u_remove").show();
        }else {
            $("#u_remove").hide();
        }
    });
    $("span[title='清除帐号']").click("input",function () {
        $("#u_remove").prev().val("");
        $("#u_remove").hide();
    });

    $("#eye").hide();
    $("input[name='password']").on("input",function () {
        if($(this).val().length > 0){
            $("#eye").show();
        }else {
            $("#eye").hide();
        }
    });
    $("span[title='查看']").click(function () {
        if($("#pwd").val().length > 0){
            if($("#pwd").attr("type") == "password"){
                $("#pwd").attr("type","text");
                $("#eye").attr("class","glyphicon glyphicon-eye-close form-control-feedback");
            }else{
                $("#pwd").attr("type","password");
                $("#eye").attr("class","glyphicon glyphicon-eye-open form-control-feedback");
            }
        }
    });

    $("#eye2").hide();
    $("input[name='password2']").on("input",function () {
        if($(this).val().length > 0){
            $("#eye2").show();
        }else {
            $("#eye2").hide();
        }
    });
    $("span[title='查看二次密码']").click(function () {
        if($("#pwd2").val().length > 0){
            if($("#pwd2").attr("type") == "password"){
                $("#pwd2").attr("type","text");
                $("#eye2").attr("class","glyphicon glyphicon-eye-close form-control-feedback");
            }else{
                $("#pwd2").attr("type","password");
                $("#eye2").attr("class","glyphicon glyphicon-eye-open form-control-feedback");
            }
        }
    });
    //注册
});
