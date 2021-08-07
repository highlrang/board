var main = {
    init : function () {
        var _this = this;

        $(document).on('click', '#post-list', function(){
            _this.list();
        });

        $(document).on('click', '#post-mylist', function(){
            _this.mylist();
        });

        $(document).on('click', '#post-best', function(){
            _this.bestlist();
        });
    },

    list : function(){
        var id = $('#cateId').val();
        var offset = $('#offset').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/category/'+id+"?offset="+offset,
            dataType: 'json'

        }).done(function(data){
            console.log(data);
            listAfter(data);

        }).fail(function(error){
            alert(JSON.stringify(error));
        });

    },

    mylist : function(){
        var cateId = $('#cateId').val();
        var userId = $('#userId').val();
        var offset = $('#offset').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/category/'+cateId+'/writer/'+userId+"?offset="+offset,
            dataType: 'json'

        }).done(function(data){
            console.log(data);
            listAfter(data);

        }).fail(function(error){
            alert(JSON.stringify(error));
        });

    },

    bestlist : function(){
        var cateId = $('#cateId').val();
        var offset = $('#offset').val();

        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/best-likes/category/' + cateId + "?offset=" + offset,
            dataType: 'json'

        }).done(function(data){
            console.log(data);

            var table = "<tbody id='bestTableBody'>";
            $.each(data["list"], function(index, post){
                var i = parseInt(index) + 1;
                table += "<tr><th scope='row'>" + i + "</th>"
                + "<td><a href='/post/detail/" + post.id + "'>"
                + post.title + "</a></td>"
                + "<td>" + post.writer + "</td>"
                + "<td>" + post.likeCount + "</td></tr>";
            });
            table += "</tbody>";

            $("#bestTableBody").replaceWith(table);


            var cnt;
            if(data["count"] % 10 == 0){
                cnt = data["count"] / 10;
            }else{
                cnt = parseInt(data["count"] / 10) + 1;
            }
            $("#bestPostPages").val(cnt);
            var pages = "";
            if(cnt > 10) cnt = 10;
            for(var i = 1; i < cnt + 1; i++){
                pages += "<li class='page-item'>" +
                "<input type='button' class='page-link' name='bestPostPage' value='" +
                i + "'></li>";
            }
            $("#bestPostPage").replaceWith(pages);


            if(cnt > 1) {
                $("#bestPostPages").css('display', 'block');
            }else{
                $("#bestPostPages").css('display', 'none');
            }

            if(data["count"] == 0){
                $("#bestTable").css('display', 'none');

            }else{
                $("#bestTable").css('display', 'block');
            }

        }).fail(function(error){
            alert(JSON.stringify(error));
        });

    }

};
main.init();

function listAfter(data){
    var table = "<tbody id='tableBody'>";
    $.each(data["list"], function(index, post){
        var i = parseInt(index) + 1;
        table += "<tr><th scope='row'>" + i + "</th>"
        + "<td><a href='/post/detail/" + post.id + "'>"
        + post.title + "</a></td>"
        + "<td>"+post.writer+"</td></tr>";
    });
    table += "</tbody>";

    $("#tableBody").replaceWith(table);


    var cnt;
    if(data["count"] % 10 == 0){
        cnt = data["count"] / 10;
    }else{
        cnt = parseInt(data["count"] / 10) + 1;
    }
    $("#pages").val(cnt);
    var pages = "";
    if(cnt > 10) cnt = 10;
    for(var i = 1; i < cnt + 1; i++){
        pages += "<li class='page-item'>" +
        "<input type='button' class='page-link' name='page' value='" +
        i + "'></li>";
    }
    $("#page").replaceWith(pages);


    if(cnt > 1) {
        $("#pages").css('display', 'block');
    }else{
        $("#pages").css('display', 'none');
    }

    if(data["count"] == 0){
        $("#postTable").css('display', 'none');
    }else{
        $('#postTable').css('display', 'block');
    }
}
