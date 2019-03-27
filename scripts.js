function myFunction(){
    var tableName = document.getElementById("tableName").value;
    $.ajax({
             type:"GET",
             url:"api/getTable/transaction_items",
             //提交的数据
             // data:{Name:"sanmao",Password:"sanmaoword"},
             headers: {
                'Access-Control-Allow-Origin': '*'
            },
             dataType: 'json',
             //在请求之前调用的函数
            // beforeSend:function(){$("#msg").html("logining");},
             //成功返回之后调用的函数             
             success:function(data){
                
                // var obj = jQuery.parseJSON(data);
                console.log(data.fields);
                var ffhtml = '<tr>';
                data.fields.forEach(function(entry) {
                    ffhtml = ffhtml + '<th>' + entry +'</th>';
                });
                ffhtml+='</tr>';
                $('#records_table').append(ffhtml);
                var trHTML = '';
        $.each(data.values, function (i, item) {
            trHTML += '<tr>';
            // 如何遍历一个 object 的 values ，参考：
            // https://javascript.info/keys-values-entries
            // https://stackoverflow.com/questions/9329446/for-each-over-an-array-in-javascript 
            for (let value of Object.values(item)){
                trHTML += '<td>'+value +'</td>';
            }
            trHTML += '/<tr>';
            // trHTML += '<tr><td>' + item.id + '</td><td>' + item.price  + '</td></tr>';
        });
        $('#records_table').append(trHTML);
             }   ,
             //调用执行后调用的函数
             complete: function(XMLHttpRequest, textStatus){
                // alert(XMLHttpRequest.responseText);
                // alert(textStatus);
                 //HideLoading();
             },
             //调用出错执行的函数
             error: function(){
                 //请求出错处理
             }         
          });
   }

   $(document).ready(function(){
    // click on button submit
    $("#submit").on('click', function(){
        var str=document.getElementById("authors").value;
        console.log("---");
        console.log(str);
         var tempResult = str.split(";");
         var authorsList =  tempResult.map(function (x) { 
            return parseInt(x, 10); 
          });

        var article = {
            magazine:parseInt(document.getElementById("magazine").value),
            volumeNumber:parseInt(document.getElementById("volumenumber").value),
            title:document.getElementById("title").value,
            pages:parseInt(document.getElementById("pages").value),
            authors:authorsList
        }

        console.log(article);
        // send ajax
        $.ajax({
            url: 'http://localhost:8080/api/create/article', // url where to submit the request
            type : "POST", // type of action POST || GET
            dataType : 'json', // data type
            contentType: "application/json",
           // data : $("#form").serialize(), // post data || get data
           data:JSON.stringify(article),
            success : function(result) {
                // you can see the result from the console
                // tab of the developer tools
                console.log(result);
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        })
    });
});