$.ajax({
    url:"http://localhost:8080/novel/novelList"
    ,type: "POST"
    ,success:function(novelData){
        $("#div1").html(novelData);
        var html = "";
        for (var i = 0 ; i<2 ; i++){
            html += '<li><a href="'+"/"+novelData[i][novel_url]+"/"+'" target="_blank">'+novelData[i][novel_title]+'</a> novelData[i][novel_name]</li>';
        }
        $(".tit ul").append(html);
    }});