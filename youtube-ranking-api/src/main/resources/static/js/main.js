$(document).ready(function(){

    $('#search').on('click',function(){
    	$("#main-content").empty();
        let keyword = $('#keyword').val();
        let date =  $("input[name='radio']:checked").val();
          $.ajax({
              url:'http://160.16.231.123:8080/test?'+'query='+keyword+'&date='+date,
              type:'GET',
              async: false,
              dataType: "json",
              //ajax通信成功
              success : function(response) {
                  $.each(response, function(i, item) {
                      var html = '<div>'
                          html = html + '<label>'+ item.title +'</label><br>';
                          html = html + '<a href="'+ item.videoUrl +'">'+ item.videoUrl +'</a><br>';
                          html = html + '<img src="'+ item.thumbnail +'"><br>';
                          html = html + '</div>';
                      $('#main-content').append(html);
                    });
              }
          });
    });
    $('input[name=radio]').on('click',function(){
    	    if ($('input[name=radio]:eq(0)').prop('checked')) {
    	        $('input[name=radio]:eq(0)').prop('checked', true);
    	    	$('input[name=radio]:eq(1)').prop('checked', false);
    	    	$('input[name=radio]:eq(2)').prop('checked', false);
    	    } else if ($('input[name=radio]:eq(1)').prop('checked')) {
    	        $('input[name=radio]:eq(0)').prop('checked', false);
    	    	$('input[name=radio]:eq(1)').prop('checked', true);
    	    	$('input[name=radio]:eq(2)').prop('checked', false);
    	    }else{
    	    	$('input[name=radio]:eq(0)').prop('checked', false);
        	    $('input[name=radio]:eq(1)').prop('checked', false);
        	    $('input[name=radio]:eq(2)').prop('checked', true);

    	    }
    });
});