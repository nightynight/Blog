$(document).ready(function(){

    $("#button").click(function(){
        $(".ul-secondMenu").toggle(200);
    });
    $(".ic-pull").click(function(){
        
        if ($(this).attr('class').indexOf("show")<0) {
            $(this).css("transform","rotate(180deg)");
            $(this).css("transition","all 0.2s ease-in");
            $(this).css("-webkit-transform","rotate(180deg)");
            $(this).css("-webkit-transition","all 0.2s ease-in");
            $(this).css("-ms-transform","rotate(180deg)");
            $(this).css("-ms-transition","all 0.2s ease-in");
            $(this).css("-moz-transform","rotate(180deg)");
            $(this).css("-moz-transition","all 0.2s ease-in");
            $(this).css("-o-transform","rotate(180deg)");
            $(this).css("-o-transition","all 0.2s ease-in");
            $(this).addClass("show");
        }else{
            $(this).css("transform","rotate(0deg)");
            $(this).css("transition","all 0.2s ease-in");
            $(this).css("-webkit-transform","rotate(0deg)");
            $(this).css("-webkit-transition","all 0.2s ease-in");
            $(this).css("-ms-transform","rotate(0deg)");
            $(this).css("-ms-transition","all 0.2s ease-in");
            $(this).css("-moz-transform","rotate(0deg)");
            $(this).css("-moz-transition","all 0.2s ease-in");
            $(this).css("-o-transform","rotate(0deg)");
            $(this).css("-o-transition","all 0.2s ease-in");
            $(this).removeClass("show");
        }
        
        $(this).parent().next().toggle(200);
    });
});