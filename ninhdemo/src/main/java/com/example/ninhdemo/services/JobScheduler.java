package com.example.ninhdemo.services;

import com.example.ninhdemo.entities.Bill;
import com.example.ninhdemo.entities.User;
import com.example.ninhdemo.repo.BillRepo;
import com.example.ninhdemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class JobScheduler {
    @Autowired
    UserRepo userRepo;

    @Autowired
    BillRepo billRepo;

    @Autowired
    MailService mailService;

    // Lên lịch quét 5 phút 1 lần, xem có đơn hàng mới ko, thì gửi mặc định về tài
    // khoản email của mình,
    // (Đơn hàng mới là ngày tạo > ngày hiện tại - 5 phút )
    // gợi ý: Viết hàm jpql tìm bill theo buyDate > :date
    @Scheduled(fixedDelay = 1000 * 60)
    public void sendAdminEmail() {
        // chi gio hien tai - 5 phut
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date date = cal.getTime();

        List<Bill> bills =
                billRepo.searchByDate(date);

        for (Bill b : bills) {
            System.out.println(b.getId());

            mailService.sendEmail("letheanh8810@gmail.com",
                    "BILL ID " + b.getId(), "aa");
        }

    }

    // GUI EMAIL SINH NHAT
//	@Scheduled(fixedDelay = 5000)
    // QUARZT SCHEDULER
    // GIAY - PHUT - GIO - NGAY - THANG - THU
    @Scheduled(cron = "0 0 9 * * *")
    // https://crontab.guru/
    public void sendEmail() {
        System.out.println("HELLO JOB");
        // chi lay ngay
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        List<User> users = userRepo.searchByBirthdate(today);

        for (User u : users) {
            System.out.println(u.getName());
            mailService.sendEmail(u.getEmail(), "HPBD", "aa");
        }
    }
}