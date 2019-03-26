function myFunction(){
    $.ajax({
             type:"GET",
             url:"http://localhost:8080/api/getTable/items",
             //提交的数据
             // data:{Name:"sanmao",Password:"sanmaoword"},
             
             dataType: 'json',
             //在请求之前调用的函数
            // beforeSend:function(){$("#msg").html("logining");},
             //成功返回之后调用的函数             
             success:function(data){
                
                console.log(data);
             }   ,
             //调用执行后调用的函数
             complete: function(XMLHttpRequest, textStatus){
                alert(XMLHttpRequest.responseText);
                alert(textStatus);
                 //HideLoading();
             },
             //调用出错执行的函数
             error: function(){
                 //请求出错处理
             }         
          });
   }