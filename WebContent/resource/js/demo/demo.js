$(function(){

    Util.ajax({
        url : contextpath + "/demo/list" ,//后台的请求路径
        param : {
            mapping : "getIdentityTypeList" //对应后台XML文件中的SQL
        },
        success : function(data){
            for(var i=0;i<data.length;i++) {
                alert(data[i].CODENAME);
            }
        }
    })
});