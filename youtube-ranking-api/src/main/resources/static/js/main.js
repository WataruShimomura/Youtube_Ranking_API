$(document).ready(function(){

    $('#search').on('click',function(){
    	$("#main-content").empty();
        let keyword = $('#keyword').val();
        let date =  $("input[name='radio']:checked").val();
          $.ajax({
              url:'http://http://160.16.231.123:8080//test?'+'query='+keyword+'&date='+date,
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
});