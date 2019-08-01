package coeo.ibuki.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import coeo.ibuki.dao.AdminMapper;
import coeo.ibuki.entity.Admin;
import coeo.ibuki.service.AdminService;
import coeo.ibuki.util.MD5Util;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Resource
	private AdminMapper adminMapper;

	@Override
	public Admin login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminMapper.login(userName, passwordMd5);
    }

	@Override
	public Admin getUserDetailById(Long loginUserId) {
        return adminMapper.selectByPrimaryKey(loginUserId);
    }

	@Override
	public Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword) {
        Admin adminUser = adminMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //比较原密码是否正确
            if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5);
                if (adminMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public Boolean updateName(Long loginUserId, String loginUserName, String nickName) {
        Admin adminUser = adminMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新密码并修改
            adminUser.setLoginName(loginUserName);
            adminUser.setAdminNickName(nickName);
            if (adminMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

}
