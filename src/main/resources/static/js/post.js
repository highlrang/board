var main = {
    init : function () {
        var _this = this;

        $(document).on('click', '#post-save', function(){
            _this.save();
        });

        $(document).on('click', '#post-update', function(){
            _this.update();
        });

        $(document).on('click', '#post-delete', function(){
            _this.delete();
        });

        $(document).on('click', '#like-push', function(){
            _this.like();
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

        }).done(function(data) {
            alert('글이 등록되었습니다.');
            window.location.href = '/post/detail/' + data["id"];

        }).fail(function (request, error) {
            window.location.href='/error';
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

        }).fail(function(request, error){
            window.location.href='/error';
        });

    },


    delete : function(){
        var id = $('#postId').val();
        var categoryId = $("#categoryId").val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id

        }).done(function(){
            alert('글이 삭제되었습니다.');
            window.location.href='/post/list?cateId='+categoryId;

        }).fail(function(request, error){
            window.location.href='/error';
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

        }).fail(function(request, error){
            window.location.href='/error';
        });
    }
};

function errorProcess(request, error, location){ // fail 시
    var url = "";
    var status = "";

    if(request.status==404 || request.status==500){
        status = request.status;
    }else{
        status = error;
    }

    window.location.href = "/errored?status=" + status + "&location=" + location;
    // return url;
}

main.init();
