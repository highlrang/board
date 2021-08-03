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

            $(document).ready(function(){
                _this.categoryList();
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

        categoryList : function(){
            $.ajax({
                type: 'GET',
                url: '/api/v1/category',
                dataType: 'json'

            }).done(function(data){
                console.log(data);
                // var categoryList = "<div th:with='categories=${"+data+"}'>";
                // $("#header").append(categoryList); //replaceWith

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
                /*
                var table = "<h3>전체 게시글</h3><table><thead><tr>";

                console.log(data["count"]);
                // for(var key in data[0]){
                table += "<th>제목</th><th>작성자</th><th>열람</th></tr><tbody>";

                // $.each(object, function(index, alias){}
                $.each(data["list"], function(index, post){
                    table += "<tr><td>"+post.title+"</td><td>"+post.writer+
                    "</td><td><a href='#' onclick='goDetail(" + post.id +
                    ")'>열람</a></td></tr>";
                });

                table += "</tbody></thead></table><span>";

                var cnt;
                if(data["count"] % 10 == 0){
                    cnt = data["count"] / 10;
                }else{
                    cnt = parseInt(data["count"] / 10) + 1;
                }

                for(var i = 1; i < cnt + 1; i++){
                    table += "<input type='button' name='page' value='" + i + "'/>";
                }

                table += "</span>";
                $("#allTable").html(table);
                */

                var cnt;
                if(data["count"] % 10 == 0){
                    cnt = data["count"] / 10;
                }else{
                    cnt = parseInt(data["count"] / 10) + 1;
                }
                var pages = "<div th:with='pages=${"+cnt+"}'>";
                var postList = "<div th:with='postList=${"+data[list]+"}'>";
                $("#listView").append(pages);
                $("#listView").append(postList);


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
                /*
                var table = "<h3>나의 게시글</h3><table><thead><tr>";

                console.log(data);
                // for(var key in data[0]){
                table += "<th>제목</th><th>작성자</th><th>열람</th></tr><tbody>";


                // $.each(object, function(index, alias){}
                $.each(data, function(index, post){
                    table += "<tr><td>"+post.title+"</td><td>"+post.writer+
                    "</td><td><a href='#' onclick='goMyDetail(" + post.id +
                    ")'>열람</a></td></tr>";
                });

                table += "</tbody></thead></table>";

                $("#myTable").html(table);
                */

                var postList = "<div th:with='postList=${"+data[list]+"}'>";
                $("#listView").append(postList);

            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        },

        bestlist : function(){
            var cateId = $('#cateId').val();

            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/likes/category/' + cateId,
                dataType: 'json'

            }).done(function(data){
                /*
                var table = "<h3>베스트 게시글</h3><table><thead><tr>";

                console.log(data);
                table += "<th>제목</th><th>작성자</th><th>좋아요</th><th>열람</th></tr><tbody>";

                $.each(data, function(index, post){
                    table += "<tr><td>"+post.postTitle+"</td><td>"+post.writerName+
                    "</td><td>"+post.postLikeCount+
                    "</td><td><a href='#' onclick='goDetail(" + post.postId +
                    ")'>열람</a></td></tr>";
                });

                table += "</tbody></thead></table>";

                $("#bestTable").html(table);
                */
                var bestPosts = "<div th:with='bestPosts=${"+data+"}'>";
                $("#listView").append(bestPosts);

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
