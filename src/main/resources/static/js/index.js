/*
<script src="/js/index.js"></script>
원하는 곳에서 $("#id") 부여해주면 됨

$("#resultDiv")
replaceWith(data)
append()
html()
*/

var main = {
    init : function () {
        var _this = this;

        $(document).ready(function(){
            _this.categorylist();
        });

        $(document).on('click', '#cate-list', function(){
            _this.categorylist();
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

        }).fail(function (request, error) {
            // JSON.stringify(error)

            window.location.href='/error';
        });
    }
};

main.init();
