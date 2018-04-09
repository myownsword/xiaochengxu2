// /**
//  * 命名空间
//  */
// jQuery.namespace = function(){
//     var a=arguments, o=null, i, j, d, rt;
//     for (i=0; i<a.length; ++i) {
//         d=a[i].split(".");
//         rt = d[0];
//         eval('if (typeof ' + rt + ' == "undefined"){' + rt + ' = {};} o = ' + rt + ';');
//         for (j=1; j<d.length; ++j) {
//             o[d[j]]=o[d[j]] || {};
//             o=o[d[j]];
//         }
//     }
// }


/**
 * 日期格式化
 */
(function($){  
    $.formatDate = function(pattern,date){  
        //如果不设置，默认为当前时间  
        if(!date) date = new Date();  
        if(typeof(date) ==="string" || typeof(date) ==="number"){  
             if(date==""){
            	 date = new Date()
             } else {
	             date = new Date((date+"").replace(/-/g,"/")*1);
             }  
             
        }     
        /*补00*/  
        var toFixedWidth = function(value){  
             var result = 100+value;  
             return result.toString().substring(1);  
        };  
          
        /*配置*/  
        var options = {  
                regeExp:/(yyyy|M+|d+|h+|m+|s+|ee+|ws?|p)/g,  
                months: ['January','February','March','April','May',  
                         'June','July', 'August','September',  
                          'October','November','December'],  
                weeks: ['Sunday','Monday','Tuesday',  
                        'Wednesday','Thursday','Friday',  
                            'Saturday']  
        };  
          
        /*时间切换*/  
        var swithHours = function(hours){  
            return hours<12?"AM":"PM";  
        };  
          
        /*配置值*/  
        var pattrnValue = {  
                "yyyy":date.getFullYear(),                      //年份  
                "MM":toFixedWidth(date.getMonth()+1),           //月份  
                "dd":toFixedWidth(date.getDate()),              //日期  
                "hh":toFixedWidth(date.getHours()),             //小时  
                "mm":toFixedWidth(date.getMinutes()),           //分钟  
                "ss":toFixedWidth(date.getSeconds()),           //秒  
                "ee":options.months[date.getMonth()],           //月份名称  
                "ws":options.weeks[date.getDay()],              //星期名称  
                "M":date.getMonth()+1,  
                "d":date.getDate(),  
                "h":date.getHours(),  
                "m":date.getMinutes(),  
                "s":date.getSeconds(),  
                "p":swithHours(date.getHours())  
        };  
          
        return pattern.replace(options.regeExp,function(){  
               return  pattrnValue[arguments[0]];  
        });  
    };  
      
})(jQuery);  

/**
 * json对象load到form上
 */
$.fn.loadFormData = function(data){
    return this.each(function(){
        var input, name;
        if(data == null){this.reset(); return; }
        for(var i = 0; i < this.length; i++){  
            input = this.elements[i];
            //checkbox的name可能是name[]数组形式
            name = (input.type == "checkbox")? input.name.replace(/(.+)\[\]$/, "$1") : input.name;
            if(data[name] == undefined) continue;
            switch(input.type){
                case "checkbox":
                    if(data[name] == ""){
                        input.checked = false;
                    }else{
                        //数组查找元素
                        if(data[name].indexOf(input.value) > -1){
                            input.checked = true;
                        }else{
                            input.checked = false;
                        }
                    }
                break;
                case "radio":
                    if(data[name] == ""){
                        input.checked = false;
                    }else if(input.value == data[name]){
                        input.checked = true;
                    }
                break;
                case "button": break;
                default: input.value = data[name];
            }
        }
    });
};

/**
 * 公共方法
 */
var Util = {
		/**
		 * 通过 from 中的条件分页检索数据列表
		 * @param data
		 */
		getPageObjListByForm : function(data){
			if(!data || !data.formId){
				Msg.warning('参数异常：请通过参数formId 指定from的ID。');
				return  ;
			}
			
			var _from = $("#"+data.formId) ;
			if(!_from.attr("action")){
				Msg.warning('参数异常：id为'+data.formId+'的form尚未配置action属性。');
				return  ;
			}

			var param = {} ;
			var arr = _from.serializeArray();
			$(arr).each(function(i,o){
				if(o.value!=''){
					if(param[o.name]){
						param[o.name] = param[o.name]+","+o.value ;
					}else{
						param[o.name] = o.value ;
					}
				}
			});
			data['data'] = $.extend(data.data,param);
			data['url'] = _from.attr("action") ;
			this.getPageObjList(data);
		} ,
		//通过指定参数{}分页获取数据
		getPageObjList : function(data){
			if(!data || !data.url){
				Msg.warning('getPageObjList方法中尚未配置url请求地址。');
				return ;
			}
			data.param = data.data||{} ;
			data.param['pageNum'] = data.param['pageNum']||1  ;
			data.param['pageSize'] = data.param['pageSize']||10 ;
			this.ajax(data);
		},
		/**
		 * 与服务端交互
		 * @param data
		 */
		ajax : function(data){
			if(!data || !data.url){
				Msg.warning('ajax方法中尚未配置url请求地址。');
				return ;
			}
			var loading = "" ;
			$.ajax({
			    type: data.method || "POST",  
			    url: data.url,  
			    async : data.async==undefined?true:data.async ,
			    data: JSON.stringify(data.param||{}),
			    dataType : data.dataType || "json",  
			    contentType : 'application/json;charset=utf-8',
			    success: function(result){  
			    	if(data.success && typeof(data.success) == "function"){ 
			    		data.success(result); 
			    	}
			    },  
			    error: function(err){
			    	if(err.status==200 && err.statusText=="OK" && data.success && typeof(data.success) == "function"){ 
			    		data.success(err.responseText); 
			    		return ;
			    	}
			    	if(data.error && typeof(data.error) == "function"){ 
			    		data.error(err); 
			    	} 
			    } ,
			    beforeSend : function(){
			    	if(data.loading){
			    		loading = Msg.load();//发送请求之前显示loading
			    	}
			    } ,
			   complete: function( xhr ){
			        if(loading) Msg.close(loading);
			        if(xhr.status == '0' && xhr.statusText=='error'){
			        	setTimeout( "Msg.reloadPage()" , 5000);//5秒后提示过期
					}
		       }
			});
		},

		/**
		 * 判断对象是否为空
		 * 
		 * @param {Object}
		 *            v
		 * @return {Boolean} 不为空返回true，否则返回false。
		 */
		isNotEmpty : function(v){
			if(typeof (v)=="object"){
				if($.isEmptyObject(v)){
					return false;
				}else{
					return true;
				}
			}else{
				if (v == null || typeof (v) == 'undefined' || v == "" || v == "unknown") {
					return false;
				} else {
					return true;
				}
			}
		} ,
		/**
		 * 空对象转换
		 * 
		 * @param {Object}
		 *            v
		 * @return {String} 不为空返回本身，否则返回"无"。
		 */
		nvlToStr : function(v){
			if (v == null || typeof (v) == 'undefined' || v == "" || v == "unknown") {
				return "无";
			} else {
				return v;
			}
		} ,
		/**
		 * 空对象转换
		 * 
		 * @param {Object}
		 *            v
		 * @return {String} 不为空返回本身，否则返回""。
		 */
		nvlToNull : function(v){
			if (v == null || typeof (v) == 'undefined'|| v == "undefined" || v == "" || v == "unknown") {
				return "";
			} else {
				return v;
			}
		} ,
		/**
		 * 
		 * @param str 字符串
		 * @param num	保留长度
		 * @returns
		 */
		subStr : function(str , length){
			if(str && str.length>length){
				return str.substring(0,length)+"...";
			}
			return str ;
		}
}

// /**
// * 自定义方法被artTemplate引用，对日期格式化
// */
// template.helper('dateFormat', function (date, format) {
// 	if(!date){
// 		return ;
// 	}
//     date = new Date(date);
//     var map = {
//         "M": date.getMonth() + 1, //月份
//         "d": date.getDate(), //日
//         "h": date.getHours(), //小时
//         "m": date.getMinutes(), //分
//         "s": date.getSeconds(), //秒
//         "q": Math.floor((date.getMonth() + 3) / 3), //季度
//         "S": date.getMilliseconds() //毫秒
//     };
//     format = format.replace(/([yMdhmsqS])+/g, function(all, t){
//         var v = map[t];
//         if(v !== undefined){
//             if(all.length > 1){
//                 v = '0' + v;
//                 v = v.substr(v.length-2);
//             }
//             return v;
//         }
//         else if(t === 'y'){
//             return (date.getFullYear() + '').substr(4 - all.length);
//         }
//         return all;
//     });
//     return format;
// });
//
// /**
//  * IP转换
//  */
// template.helper('int2iP', function (data) {
//     return Util.int2iP(data);
// });
//
// /**
//  * 截取字符串
//  */
// template.helper('subStr', function (str , length) {
//     return Util.subStr(str , length);
// });

// layer.config({
// 	extend: 'extend/layer.ext.js'
// });
var Msg = {
		/**
		 * 操作成功提示
		 * @param m
		 */
		success : function(m){
			layer.msg(Util.htmlEncode(m)||"操作成功", {
			  icon: 1,
			  time: 2000 //2s后自动关闭
			});
		},
		/**
		 * 操作失败提示
		 * @param m
		 */
		error : function(m){
			layer.msg(Util.htmlEncode(m)||"操作失败", {
			  icon: 11,
			  time: 2000 //2s后自动关闭
			});
		},
		/**
		 * 警示
		 * @param m
		 */
		warning : function(m,callback,callback1){
//			layer.alert(m, {
//			  title: '系统提示',
//			  icon: 0
//			},function(index){
//				layer.close(index);
//				if(typeof callback == "function"){
//					callback();
//				}
//			});
			var layerObj = layer.confirm(Util.htmlEncode(m), {
		         title: '系统提示',
		         btn: ['确认','取消'] //按钮
		    }, function(){
			      layer.close(layerObj);
			     if(typeof callback == "function"){
			      callback();
			     }
		    }, function(){
			     layer.close(layerObj);
			     if(typeof callback1 == "function"){
			      callback1();
			     }
		    });
		},
		/**
		 * 提示
		 * @param m
		 */
		info : function(m,callback,callback1){
//			layer.alert(m, {
//			  title: '系统提示',
//			  icon: 5
//			});
			var layerObj = layer.confirm(Util.htmlEncode(m), {
		         title: '系统提示',
		         btn: ['确认','取消'] //按钮
		    }, function(){
			      layer.close(layerObj);
			     if(typeof callback == "function"){
			      callback();
			     }
		    }, function(){
			     layer.close(layerObj);
			     if(typeof callback1 == "function"){
			      callback1();
			     }
		    });
		},
		/**
		 * 确认
		 * @param m
		 */
		confirm : function(m,callback,callback1){
			   var layerObj = layer.confirm(Util.htmlEncode(m), {
			         title: '系统提示',
			      btn: ['确认','取消'] //按钮
			    }, function(){
			      layer.close(layerObj);
			     if(typeof callback == "function"){
			      callback();
			     }
			    }, function(){
			     layer.close(layerObj);
			     if(typeof callback1 == "function"){
			      callback1();
			     }
			    });
		},
		prompt : function(param){
			var obj = $.extend({
				  formType: 2,
				  title: '系统提示'
				} , param);
			var index = layer.prompt(obj, function(value, index, elem){
				if($.isFunction(param.callback)){
					param.callback(value, index, elem);
				}
			  layer.close(index);
			});
		} ,
		/**
		 * 加载
		 * @param m
		 */
		load : function(config){
			//如果加载层存在则不需要再次创建
			if($("div[type='loading']").length==0){
				return layer.load(1, $.extend({
					time : 1000,//1秒后默认关闭
					shade: [0.1,'#000'], //0.1透明度的白色背景
					content: '<div class="exam-loader-centerbox"><div class="loader"><div class="loader-inner line-scale"><div></div><div></div><div></div><div></div><div></div></div></div><div class="exam-loader-text">请稍等，数据正在加载中！</div></div>'
				},config));
			}
		},
		/**
		 * 关闭
		 * 传入的obj为空时关闭所有层，
		 * 传入的obj='dialog' 关闭信息框
		 * 传入的obj='page' 关闭所有页面层
		 * 传入的obj='iframe' 关闭所有的iframe层
		 * 传入的obj='loading' 关闭加载层
		 * 传入的obj='tips' 关闭所有的tips层 
		 * @param m
		 */
		close : function(obj){
			if(Util.isNotEmpty(obj)){
				layer.close(obj);
			} else {
				layer.closeAll();
			}
		}
}
