package com.me.way;

import com.jcraft.jsch.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UploadImg {
    public static void setImg(MultipartFile file) throws Exception {
        String ip = "47.97.67.235";
        //用户名
        String user = "root";
        //密码
        String password = "Yah981210";
        //服务器端口 默认22
        int port = 22;
        //上传文件到指定服务器的指定目录 自行修改
        String path = "/images";
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }
        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }
        //设置登陆主机的密码
        session.setPassword(password);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);
        OutputStream outstream = null;
        try {
            //创建sftp通信通道
            channel = session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;
            //进入服务器指定的文件夹
            sftp.cd(path);
            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            String random=getRandom();
            outstream = sftp.put(random+file.getOriginalFilename());
            outstream.write(file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流操作
            if(outstream != null){
                outstream.flush();
                outstream.close();
            }
            if(session != null){
                session.disconnect();
            }
            if(channel != null){
                channel.disconnect();
            }
        }
    }
    public static String getRandom(){
        // 获得当前时间
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        // 转换为字符串
        String formatDate = format.format(new Date());
        // 随机生成文件编号
        int random = new Random().nextInt(10000);
        return new StringBuffer().append(formatDate).append(
                random).toString();
    }
    public static void uploadVerImg(byte[] imagedata, File file) throws Exception {
        String ip = "47.97.67.235";
        String user = "root";
        String password = "Yah981210";
        int port = 22;
        String path = "/images/verification/";
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        session = jsch.getSession(user, ip ,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);
        channel = session.openChannel("sftp");
        channel.connect(1000);
        OutputStream outstream = null;
        ChannelSftp sftp = (ChannelSftp) channel;
        sftp.cd(path);
        outstream = sftp.put(file.getName());
        outstream.write(imagedata);
    }
    public static void main(String[] args) {
        System.out.println(getRandom());
    }
}
