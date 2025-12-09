package com.TPQI.thai2learn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendCredentialsEmail(String to, String username, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply.tpqi.go.th@your-domain.com"); 
            message.setTo(to);
            message.setSubject("ข้อมูลการเข้าสู่ระบบสำหรับเทียบโอนประสบการณ์ (TPQI)");
            
            String emailBody = "สวัสดีครับ,\n\n"
                    + "ระบบได้สร้างข้อมูลสำหรับเข้าใช้งานระบบเทียบโอนประสบการณ์ของคุณเรียบร้อยแล้ว:\n\n"
                    + "Username: " + username + "\n"
                    + "Password: " + password + "\n\n"
                    + "กรุณาเปลี่ยนรหัสผ่านหลังจากเข้าสู่ระบบครั้งแรก\n\n"
                    + "ขอแสดงความนับถือ,\n"
                    + "สถาบันคุณวุฒิวิชาชีพ (องค์การมหาชน)";
            
            message.setText(emailBody);
            emailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("ไม่สามารถส่งอีเมลได้: " + e.getMessage());
        }
    }
}