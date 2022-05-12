package com.revers.rec.controller;

import com.alibaba.fastjson.JSON;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.config.optionConfig;
import com.revers.rec.domain.*;
import com.revers.rec.domain.json.JsonGroup;
import com.revers.rec.domain.json.JsonUser;
import com.revers.rec.domain.vo.MakeFriend;
import com.revers.rec.domain.vo.NetAddress;
import com.revers.rec.domain.vo.NodeVo;
import com.revers.rec.domain.vo.SendMessagesToStranger;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.service.group.GroupService;
import com.revers.rec.service.message.MessageService;
import com.revers.rec.service.user.UserService;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoutingTable routingTable;


    //
    //    /**
    //     * @description 删除好友
    //     * @param friendId
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/removeFriend"), method = Array(RequestMethod.POST))
    //    def removeFriend(@RequestParam("friendId") friendId: Integer,request: HttpServletRequest): String = {
    //        val user = request.getSession.getAttribute("user").asInstanceOf[User]
    //        val result = userService.removeFriend(friendId, user.getId)
    //        gson.toJson(new ResultSet(result))
    //    }
    //
    //    /**
    //     * @description 移动好友分组
    //     * @param groupId 新的分组id
    //     * @param userId 被移动的好友id
    //     * @param request
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/changeGroup"), method = Array(RequestMethod.POST))
    //    def changeGroup(@RequestParam("groupId") groupId: Integer, @RequestParam("userId") userId: Integer
    //        ,request: HttpServletRequest): String = {
    //        val user = request.getSession.getAttribute("user").asInstanceOf[User]
    //        val result = userService.changeGroup(groupId, userId, user.getId)
    //        if (result)
    //            return gson.toJson(new ResultSet(result))
    //        else
    //            gson.toJson(new ResultSet(SystemConstant.ERROR, SystemConstant.ERROR_MESSAGE))
    //    }
    //
    //    /**
    //     * @description 拒绝添加好友
    //     * @param request
    //     * @param messageBoxId 消息盒子的消息id
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/refuseFriend"), method = Array(RequestMethod.POST))
    //    def refuseFriend(@RequestParam("messageBoxId") messageBoxId: Integer,request: HttpServletRequest): String = {
    //        val result = userService.updateAddMessage(messageBoxId, 2)
    //        gson.toJson(new ResultSet(result))
    //    }
    //
    //    /**
    //     * @description 添加好友
    //     * @param uid 对方用户ID
    //     * @param fromGroup 对方设定的好友分组
    //     * @param group 我设定的好友分组
    //     * @param messageBoxId 消息盒子的消息id
    //     * @return String
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/agreeFriend"), method = Array(RequestMethod.POST))
    //    def agreeFriend(@RequestParam("uid") uid: Integer,@RequestParam("from_group") fromGroup: Integer,
    //        @RequestParam("group") group: Integer, @RequestParam("messageBoxId") messageBoxId: Integer,
    //        request: HttpServletRequest): String = {
    //        val user = request.getSession.getAttribute("user").asInstanceOf[User]
    //        val result = userService.addFriend(user.getId, group, uid, fromGroup, messageBoxId)
    //        gson.toJson(new ResultSet(result))
    //    }
    //
    //    /**
    //     * @description 查询消息盒子信息
    //     * @param uid
    //     * @param page
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/findAddInfo"), method = Array(RequestMethod.GET))
    //    def findAddInfo(@RequestParam("uid") uid: Integer, @RequestParam("page") page: Int): String = {
    //        PageHelper.startPage(page, SystemConstant.ADD_MESSAGE_PAGE)
    //        val list = userService.findAddInfo(uid)
    //        val count = userService.countUnHandMessage(uid, null).toInt
    //        val pages = if (count < SystemConstant.ADD_MESSAGE_PAGE) 1 else count / SystemConstant.ADD_MESSAGE_PAGE + 1
    //        gson.toJson(new ResultPageSet(list, pages)).replaceAll("Type", "type")
    //    }
    //
    //    /**
    //     * @description 分页查找好友
    //     * @param page 第几页
    //     * @param name 好友名字
    //     * @param sex 性别
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/findUsers"), method = Array(RequestMethod.GET))
    //    def findUsers(@RequestParam(value = "page",defaultValue = "1") page: Int,
    //                  @RequestParam(value = "name", required = false) name: String,
    //                  @RequestParam(value = "sex", required = false) sex: Integer): String = {
    //    		val count = userService.countUsers(name, sex)
    //				val pages = if (count < SystemConstant.USER_PAGE) 1 else (count / SystemConstant.USER_PAGE + 1)
    //    		PageHelper.startPage(page, SystemConstant.USER_PAGE)
    //    		val users = userService.findUsers(name, sex)
    //    		var result = new ResultPageSet(users)
    //    		result.setPages(pages)
    //        gson.toJson(result)
    //    }
    //
    //    /**
    //     * @description 分页查找群组
    //     * @param page 第几页
    //     * @param name 群名称
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/findGroups"), method = Array(RequestMethod.GET))
    //    def findGroups(@RequestParam(value = "page",defaultValue = "1") page: Int,
    //                  @RequestParam(value = "name", required = false) name: String): String = {
    //    		val count = userService.countGroup(name)
    //				val pages = if (count < SystemConstant.USER_PAGE) 1 else (count / SystemConstant.USER_PAGE + 1)
    //    		PageHelper.startPage(page, SystemConstant.USER_PAGE)
    //    		val groups = userService.findGroup(name)
    //    		var result = new ResultPageSet(groups)
    //    		result.setPages(pages)
    //        gson.toJson(result)
    //    }
    //
    /**
     * @description 获取聊天记录
     * @param id 与谁的聊天记录id
     */
    @ResponseBody
    @RequestMapping(value = "/chatLog", method = RequestMethod.POST)
    public String chatLog(@RequestParam("id") String id,@RequestParam("page") Integer page) {
        log.info("获取聊天记录，id=" + id + ",page=" + page);

        List<ChatHistory> list = messageService.chatLog(id,page);

        return JSON.toJSONString(new Result(list));
    }
    /**
     * @description 弹出聊天记录页面
     * @param id 与谁的聊天记录id
     */
    @RequestMapping(value = "/chatLogIndex", method = RequestMethod.GET)
    public String chatLogIndex(@RequestParam("id") String id, Model model, HttpServletRequest request) {
        model.addAttribute("id", id);
        model.addAttribute("Type", "friend");
        Integer pages = messageService.countMessage(id);
        System.out.println("pages" + pages);
        pages =  (pages < ConstantUtil.SYSTEM_PAGE) ? 1 :(pages / ConstantUtil.SYSTEM_PAGE + 1);
        model.addAttribute("pages", pages);
        return "chatLog";
    }

    /**
     * @description 添加网络
     */
    @ResponseBody
    @RequestMapping(value = "/connectNetwork", method = RequestMethod.POST)
    public String connectNetwork(@RequestBody NetAddress netAddress) {
        String msg = "连接失败";
        String ip = netAddress.getIp();
        String port = netAddress.getPort();
        if ("".equals(ip) || "".equals(port) || ip == null || port == null) {
            return "地址存在空值";
        }
        try {
            if (ClientOperation.handShake(ip, Integer.valueOf(port)).getFlag() == ConstantUtil.SUCCESS) {
                msg = "连接成功";
                log.info("握手成功");
            } else {
                msg = "连接失败";
                log.info("握手失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "连接失败";
            log.info("连接失败");
        }

        return msg;
    }

    /**
     * @description 添加好友
     */
    @ResponseBody
    @RequestMapping(value = "/makeFriend", method = RequestMethod.POST)
    public String makeFriend(@RequestBody MakeFriend makeFriend) {
        String publicKey = makeFriend.getMekeFriendPublicKey();
        String name = makeFriend.getMekeFriendName();
        String groupName = makeFriend.getMakeFriendGroupName();

        Group group = groupService.findGroupByName(groupName);
        if (group == null) {
            groupService.insertGroup(groupName);
            group = groupService.findGroupByName(groupName);
        }
        String groupId = group.getGroupId();

        log.info("添加好友，publicKey=" + publicKey + ",name=" + name);
        if ("".equals(publicKey) || "".equals(name) || publicKey == null || name == null) {
            return "添加好友时存在空值";
        }
        Friend friendByFriendPublicKey = friendService.findFriendByFriendPublicKey(publicKey);
        if(friendByFriendPublicKey != null){
            return "该好友公钥已经存在";
        }
        Friend friendByName = friendService.findFriendByName(name);
        if(friendByName != null){
            return "该好友昵称已经存在";
        }
        friendService.addFriend(name,publicKey,groupId);
        return "添加好友成功";
    }


    /**
     * @description 查询路由表
     */
    @ResponseBody
    @RequestMapping(value = "/findRoutingTable", method = RequestMethod.GET)
    public String findRoutingTable(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit) {
        log.info("查询路由表");

        List<NodeVo> allNodeVo = routingTable.getAllNodeVo();

        HashMap<String,String> map = new HashMap<>();
        map.put("code","0");
        map.put("msg","");
        map.put("count",String.valueOf(allNodeVo.size()));
        map.put("data",JSON.toJSONString(allNodeVo));

        return JSON.toJSONString(map).replace("\\","")
                .replace("\"}\"","\"}")
                .replace("\"{\"","{\"")
                .replace("\"[","[")
                .replace("]\"","]");
    }


    /**
     * @description 发送消息给陌生人
     */
    @ResponseBody
    @RequestMapping(value = "/sendMessagesToStranger", method = RequestMethod.POST)
    public String sendMessagesToStranger(@RequestBody SendMessagesToStranger sendMessagesToStranger) {
        String publicKey = sendMessagesToStranger.getPublicKey();
        String message = sendMessagesToStranger.getMessage();
        if ("".equals(publicKey) || "".equals(message) || publicKey == null || message == null) {
            return "发送消息时存在空值";
        }

        Result communicate = null;
        try {
            communicate = ClientOperation.communicate(publicKey, message);
            if(communicate != null && communicate.getFlag() == ConstantUtil.SUCCESS){
                if(ConstantUtil.COMMUNICATE_SUCCESS.equals(((Data)communicate.getData()).getData())){
                    //存储消息
                    messageService.saveMessage(message,publicKey,true);
                    log.info("已接收");
                    return "已接收";
                }else {
                    log.info("未接收");
                    return "未接收";
                }
            }else {
                log.info("发送失败");
                return "发送失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送失败");
            return "发送失败";
        }
    }
    //    /**
    //     * @description 获取离线消息
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/getOffLineMessage"), method = Array(RequestMethod.POST))
    //    def getOffLineMessage(request: HttpServletRequest): String = {
    //        val user = request.getSession.getAttribute("user").asInstanceOf[User]
    //        		LOGGER.info("查询 uid = " + user.getId + " 的离线消息")
    //        val receives: List[Receive] = userService.findOffLineMessage(user.getId, 0)
    //        JavaConversions.collectionAsScalaIterable(receives).foreach { receive => {
    //            val user = userService.findUserById(receive.getId)
    //            receive.setUsername(user.getUsername)
    //            receive.setAvatar(user.getAvatar)
    //        } }
    //        gson.toJson(new ResultSet(receives)).replaceAll("Type", "type")
    //    }
    //
    //
//        /**
//         * @description 更新签名
//         * @param sign
//         *
//         */
//        @ResponseBody
//        @RequestMapping(value = Array("/updateSign"), method = Array(RequestMethod.POST))
//        def updateSign(request: HttpServletRequest, @RequestParam("sign") sign: String): String = {
//            val user:User = request.getSession.getAttribute("user").asInstanceOf[User]
//            user.setSign(sign)
//            if(userService.updateSing(user)) {
//                gson.toJson(new ResultSet)
//            } else {
//                gson.toJson(new ResultSet(SystemConstant.ERROR, SystemConstant.ERROR_MESSAGE))
//            }
//        }
    /**
     * @description 更新签名
     * @param sign
     */
    @ResponseBody
    @RequestMapping(value = "/updateSign", method = RequestMethod.POST)
    public String updateSign(@RequestParam("sign") String sign) {
        userService.setSign(sign);
        AccountConfig.setSign(sign);
        Result result = new Result();
        result.setMsg("修改成功");
        result.setFlag(ConstantUtil.SUCCESS);
        return JSON.toJSONString(result);
    }


    /**
    * @description 注册
    * @param indexer
    */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody Indexer indexer) throws NoSuchAlgorithmException {
        String username = indexer.getUsername();
        String password = indexer.getPassword();

        HashMap data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return JSON.toJSONString(new Result(ConstantUtil.ERROR,username+ " 用户名或密码为空"));
        }

        Result flag = userService.register(username, password);
        data.put("msg",flag.getMsg());
        if (flag.getFlag() == ConstantUtil.SUCCESS) {
            return JSON.toJSONString(new Result(ConstantUtil.SUCCESS,ConstantUtil.REGISTER_SUCCESS));
        } else {
            return JSON.toJSONString(new Result(ConstantUtil.ERROR,flag.getMsg()));
        }
    }

    private boolean isEmpty(String username,String password){
        return "".equals(username) || "".equals(password);
    }

    /**
     * @description 登陆
     * @param indexer
     *
     */
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody Indexer indexer) throws InterruptedException {
        String username = indexer.getUsername();
        String password = indexer.getPassword();

        Map data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return JSON.toJSONString(new Result(ConstantUtil.SUCCESS,username+ " 用户名或密码为空"));
        }

        Result flag = userService.login(username,password);
        data.put("msg",flag.getMsg());
        if (flag.getFlag() == ConstantUtil.SUCCESS) {
            return JSON.toJSONString(new Result(ConstantUtil.SUCCESS,ConstantUtil.LOGIN_SUCCESS));
        } else {
            return JSON.toJSONString(new Result(ConstantUtil.ERROR,flag.getMsg()));
        }

    }

        /**
         * @description  初始化主界面数据
         *
         */
        @ResponseBody
        @RequestMapping(value = "/init", method = RequestMethod.POST)
        public String init(){
            HashMap<String, JsonGroup> groupMap = new HashMap<>();// <id,jsGroup[]>

            Group stranger = groupService.findGroupByName("陌生人");
            if(stranger == null){
                groupService.insertGroup("陌生人");
                stranger = groupService.findGroupByName("陌生人");
            }
            groupMap.put(stranger.getGroupId(),new JsonGroup(stranger.getGroupName(),stranger.getGroupId(),"online",new ArrayList<>()));

            for(Friend friend : friendService.findAllFriend()){
                JsonUser user = new JsonUser(friend.getFriendName(),friend.getFriendId(),friend.getStatus(),friend.getSign(),friend.getPortrait());
                if(friend.getGroupId() == null || "".equals(friend.getGroupId())){
                    groupMap.get(stranger.getGroupId()).getList().add(user);
                }
                if(groupMap.containsKey(friend.getGroupId())){
                    JsonGroup group = groupMap.get(friend.getGroupId());

                    group.getList().add(user);
                }else{
                    Group group = groupService.findGroupById(String.valueOf(friend.getGroupId()));
                    groupMap.put(friend.getGroupId(),new JsonGroup(group.getGroupName(),group.getGroupId(),"online",new ArrayList<>()));
                    groupMap.get(friend.getGroupId()).getList().add(user);
                }
            }

            ArrayList<JsonGroup> jsonGroups = new ArrayList<>();
            for(Map.Entry<String,JsonGroup> entry : groupMap.entrySet()){
                jsonGroups.add(entry.getValue());
            }

            JsonUser mine = new JsonUser();
            mine.setUsername(AccountConfig.getUsername());
            mine.setId(AccountConfig.getId());
            mine.setAvatar(AccountConfig.getPortrait());
            mine.setSign(AccountConfig.getSign());
            mine.setStatus(AccountConfig.getStatus());
            mine.setPublicKey(AccountConfig.getPublicKey());
            mine.setPort(String.valueOf(optionConfig.getServerListenPort()));

            HashMap<String,Object> data = new HashMap<>();
            data.put("friend",jsonGroups);
            data.put("mine",mine);

            HashMap<String, Object> map = new HashMap<>();
            map.put("code", 0);
            map.put("msg", "success");
            map.put("data", data);

            return JSON.toJSONString(map);
        }

    //    /**
    //     * @description 客户端上传图片
    //     * @param file
    //     * @param request
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/upload/image"), method = Array(RequestMethod.POST))
    //    def uploadImage(@RequestParam("file") file:MultipartFile,request: HttpServletRequest): String = {
    //        if (file.isEmpty()) {
    //            return gson.toJson(new ResultSet(SystemConstant.ERROR, SystemConstant.UPLOAD_FAIL))
    //        }
    //        val path = request.getServletContext.getRealPath("/")
    //        val src = FileUtil.upload(SystemConstant.IMAGE_PATH, path, file)
    //        var result = new HashMap[String, String]
    //        //图片的相对路径地址
    //        result.put("src", src)
    //        LOGGER.info("图片" + file.getOriginalFilename + "上传成功")
    //        gson.toJson(new ResultSet[HashMap[String, String]](result))
    //    }
    //
    //    /**
    //     * @description 客户端上传文件
    //     * @param file
    //     * @param request
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/upload/file"), method = Array(RequestMethod.POST))
    //    def uploadFile(@RequestParam("file") file:MultipartFile,request: HttpServletRequest): String = {
    //        if (file.isEmpty()) {
    //            return gson.toJson(new ResultSet(SystemConstant.ERROR, SystemConstant.UPLOAD_FAIL))
    //        }
    //        val path = request.getServletContext.getRealPath("/")
    //        val src = FileUtil.upload(SystemConstant.FILE_PATH, path, file)
    //        var result = new HashMap[String, String]
    //        //文件的相对路径地址
    //        result.put("src", src)
    //        result.put("name", file.getOriginalFilename)
    //        LOGGER.info("文件" + file.getOriginalFilename + "上传成功")
    //        gson.toJson(new ResultSet[HashMap[String, String]](result))
    //    }
    //
    //    /**
    //     * @description用户更新头像
    //     * @param file
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/updateAvatar"), method = Array(RequestMethod.POST))
    //    def updateAvatar(@RequestParam("avatar") avatar: MultipartFile, request: HttpServletRequest): String = {
    //        val user = request.getSession.getAttribute("user").asInstanceOf[User]
    //        val path = request.getServletContext.getRealPath(SystemConstant.AVATAR_PATH)
    //        val src = FileUtil.upload(path, avatar)
    //        userService.updateAvatar(user.getId, src)
    //        var result = new HashMap[String, String]
    //        result.put("src", src)
    //        gson.toJson(new ResultSet(result))
    //    }
    //
    /**
     * @description 跳转主页
     * @param
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(){
        if(AccountConfig.getId() != null && !"".equals(AccountConfig.getId())) {
            return new ModelAndView("index");
        }else {
            HashMap data = new HashMap();
            data.put("msg","用户未登录");
            return new ModelAndView("error",data);
        }
    }
    //
    //    /**
    //     * @description 根据id查找用户信息
    //     * @param id
    //     * @return
    //     */
    //    @ResponseBody
    //    @RequestMapping(value = Array("/findUser"), method = Array(RequestMethod.POST, RequestMethod.GET))
    //    def findUserById(@RequestParam("id") id: Integer): String = {
    //        gson.toJson(new ResultSet(userService.findUserById(id)))
    //    }
    //


}
