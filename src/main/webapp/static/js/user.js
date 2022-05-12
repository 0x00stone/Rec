layui.use(['jquery', 'layer', 'form', 'upload'], function() {
	var $ = layui.jquery,
	layim = parent.layim,
	form = layui.form,
	layer = layui.layer;
	var upload = layui.upload;

	//屏蔽右键菜单
	$(document).bind("contextmenu",function(e){
        return false;
    });


	//点击复制id
	$("#copyId").click(function(){
		const input = document.createElement('input');
		input.style.cssText = 'opacity:0;;';
		input.type = 'text';
		input.value = $("#nodeId").val();
		document.body.appendChild(input);
		input.select();
		document.execCommand('Copy');
		layer.msg("复制成功");
	});

	//点击复制公钥
	$("#copyPublicKey").click(function(){
		const input = document.createElement('input');
		input.style.cssText = 'opacity:0;;';
		input.type = 'text';
		input.value = $("#publicKey").val();
		document.body.appendChild(input);
		input.select();
		document.execCommand('Copy');
		layer.msg("复制成功");
	});

    //修改头像
    upload.render({
        url: "/user/updateAvatar"
        ,title: '修改头像'
        ,ext: 'jpg|png|gif'
        ,before: function(input) {
        	console.log("before upload!");
        }
        ,done: function(res, input){
            if(0 == res.code){
                $("#LAY_demo_upload").attr('src', res.data.src);
                $("#user_avatar").val(res.data.src);
                layer.msg("修改成功!", {time:2000});
            }else{
                layer.msg(res.msg, {time:2000});
            }
        }
    });
	
    //从缓存中初始化数据
	$(document).ready(function(){
		var mine = layim.cache().mine;
		console.log(mine);
		$("#username").val(mine.username);
		$("#publicKey").val(mine.publicKey);
		$("#sign").val(mine.sign);
		$("#nodeId").val(mine.id);
		$("#listenPort").val(mine.port);
		$("#LAY_demo_upload").attr("src", mine.avatar);
		if (mine.sex == "0") {
			$("input[type='radio']").eq(0).attr("checked",true);
		} else {
			$("input[type='radio']").eq(1).attr("checked",true);
		}
	});
	
    //提交修改项
    $("#btn").click(function(){
        layer.ready(function(){
            var username = $("#username").val();
            var email = $("#email").val();
            var sex = $("input[type='radio']:checked").val();
            if('' == username){
                layer.tips('用户名不能为空', '#username');
                return ;
            }
            if('' == email){
                layer.tips('邮箱不能为空!', '#email');
                return ;
            }
            
            var oldpwd = $("#oldpwd").val(); //旧密码
            var pwd = $("#pwd").val();
            var repwd = $("#repwd").val();
            if('' != oldpwd){
            	if('' == pwd){
            		layer.tips('新密码不能为空', '#pwd');
            		return ;
            	}
            	if('' != pwd && '' == repwd){
            		layer.tips('重复密码不能为空', '#repwd');
            		return ;
            	}
            	if(!/^[\S]{6,12}$/.test(oldpwd)){
            		layer.tips('密码必须6到12位', '#oldpwd');
            		return ;
            	}
            	if('' != pwd && '' != repwd && '' == oldpwd){
            		layer.tips('必须输入旧密码', '#oldpwd');
            		return ;
            	}                
            	if('' != pwd && '' != repwd && '' != oldpwd && pwd != repwd){
            		layer.tips('两次密码不一致', '#pwd');
            		return ;
            	}
            	if('' != pwd && '' != repwd && '' != oldpwd && pwd == repwd){
            		if(!/^[\S]{6,12}$/.test(pwd)){
            			layer.tips('密码必须6到12位', '#pwd');
            			return ;
            		}
            		if(!/^[\S]{6,12}$/.test(repwd)){
            			layer.tips('密码必须6到12位', '#repwd');
            			return ;
            		}
            	}
            }
                    
        });
    });
});