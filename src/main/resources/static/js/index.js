/*
<script src="/js/index.js"></script>
원하는 곳에서 $("#id") 부여해주면 됨

<div th:with='categories=${
data
}'>
$("#resultDiv").replaceWith(data);
append()
html()

*/

// 파일 분리하기 !!!!!!
var main = {
        init : function () {
            var _this = this;

            $(document).on('click', '#cate-list', function(){
                _this.categorylist();
            });

            $(document).on('click', '#post-save', function(){
                _this.save();
            });

            $(document).on('click', '#post-update', function(){
                _this.update();
            });

            $(document).on('click', '#post-delete', function(){
                _this.delete();
            });

            $(document).on('click', '#post-list', function(){
                _this.list();
            });

            $(document).on('click', '#post-mylist', function(){
                _this.mylist();
            });

            $(document).on('click', '#post-best', function(){
                _this.bestlist();
            });

            $(document).on('click', '#like-push', function(){
                _this.like();
            });
        },

        categorylist : function(){
            $.ajax({
                type: 'GET',
                url: '/api/v1/category',
                dataType: 'json'

            }).done(function(data){
                console.log(data);

                var categoryList = "";
                $.each(data, function(index, category){
                    categoryList += "<a class='nav-link' href='/post/list?cateId="
                    + category.id + "'>" + category.name + "</a>";
                });

                $("#categoryList").replaceWith(categoryList);

            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        },

        save : function () {
            var data = {
                categoryId: $('#categoryId').val(),
                writerId: $('#userId').val(),
                title: $('#title').val(),
                content: $('#content').val(),
                isPublic: $('#isPublic').val()
            };

            $.ajax({
                type: 'POST',
                url: '/api/v1/posts',
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
                data: JSON.stringify(data)

            }).done(function() {
                alert('글이 등록되었습니다.');
                window.location.href = '/';

            }).fail(function (error) {
                alert(JSON.stringify(error));
            });

        },


        update : function(){
                var data = {
                    title: $('#title').val(),
                    content: $('#content').val(),
                    isPublic: $('#isPublic').val()
                };
                var id = $('#id').val();

            $.ajax({
                type: 'PUT',
                url: '/api/v1/posts/' + id,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)

            }).done(function(){
                alert('글이 수정되었습니다.');
                window.location.href= '/post/detail/' + id;

            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        },


        delete : function(){
            var id = $('#postId').val();
            var categoryId = $("#categoryId").val();

            $.ajax({
                type: 'DELETE',
                url: '/api/v1/posts/'+id,
                dataType: 'json'

            }).done(function(){
                alert('글이 삭제되었습니다.');
                window.location.href='/post/list?cateId='+categoryId;

            }).fail(function(error){
                alert(JSON.stringify(error));
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
                var table = "<tbody id='tableBody'>";
                $.each(data["list"], function(index, post){
                    var i = parseInt(index) + 1;
                    table += "<tr><th scope='row'>" + i + "</th>"
                    + "<td><a href='/post/detail/" + post.id + "'>"
                    + post.title + "</a></td>"
                    + "<td>"+post.writer+"</td></tr>";
                });
                table += "</tbody>";

                var cnt;
                if(data["count"] % 10 == 0){
                    cnt = data["count"] / 10;
                }else{
                    cnt = parseInt(data["count"] / 10) + 1;
                }

                var pages = "<span id='pages'>";
                for(var i = 1; i < cnt + 1; i++){
                    pages += "<input type='button' name='page' value='" + i + "'/>";
                }
                pages += "</span>"

                $("#tableBody").replaceWith(table);
                $("#pages").replaceWith(pages);


            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        },

        mylist : function(){
            var cateId = $('#cateId').val();
            var userId = $('#userId').val();

            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/category/'+cateId+'/writer/'+userId,
                dataType: 'json'

            }).done(function(data){
                console.log(data);
                var table = "<tbody id='tableBody'>";
                $.each(data["list"], function(index, post){
                    var i = parseInt(index) + 1;
                    table += "<tr><th scope='row'>" + i + "</th>"
                    + "<td><a href='/post/detail/" + post.id + "'>"
                    + post.title + "</a></td>"
                    + "<td>"+post.writer+"</td></tr>";
                });
                table += "</tbody>";

                var cnt;
                if(data["count"] % 10 == 0){
                    cnt = data["count"] / 10;
                }else{
                    cnt = parseInt(data["count"] / 10) + 1;
                }

                var pages = "<span id='pages'>";
                for(var i = 1; i < cnt + 1; i++){
                    pages += "<input type='button' name='page' value='" + i + "'/>";
                }
                pages += "</span>";

                $("#tableBody").replaceWith(table);
                $("#pages").replaceWith(pages);

            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        },

        bestlist : function(){
            var cateId = $('#cateId').val();

            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/best-likes/category/' + cateId,
                dataType: 'json'

            }).done(function(data){
                console.log(data);
                var table = "<tbody id='bastTableBody'>";
                $.each(data, function(index, post){
                    var i = parseInt(index) + 1;
                    table += "<tr><th scope='row'>" + i + "</th>"
                    + "<td><a href='/post/detail/" + post.id + "'>"
                    + post.title + "</a></td>"
                    + "<td>" + post.writer + "</td>"
                    + "<td>" + post.likeCount + "</td></tr>";
                });
                table += "</tbody>";

                $("#bestTableBody").replaceWith(table);

            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        },

        like : function(){
            var data = {
                postId: $('#postId').val(),
                userId: $('#userId').val()
            };
            var id = $('#postId').val();

            $.ajax({
                type: 'POST',
                url: '/api/v1/likes',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)

            }).done(function(){
                window.location.href = '/post/detail/' + id;

            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        }


};

main.init();
