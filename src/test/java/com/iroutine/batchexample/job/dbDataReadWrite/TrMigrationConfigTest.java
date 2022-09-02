package com.iroutine.batchexample.job.dbDataReadWrite;

import com.iroutine.batchexample.SpringBatchTestConfig;
import com.iroutine.batchexample.core.domain.accounts.AccountsRepository;
import com.iroutine.batchexample.core.domain.orders.Orders;
import com.iroutine.batchexample.core.domain.orders.OrdersRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
public class TrMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @AfterEach
    public void cleanUpEach() {
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void success_noData() throws Exception {

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0,accountsRepository.count());
    }

    @Test
    public void success_existData() throws Exception {
        // given
        Orders orders1 = Orders.builder().id(null).orderItem("kakao gift").price(15000).orderDate(new Date()).build();
        Orders orders2 = Orders.builder().id(null).orderItem("naver gift").price(15000).orderDate(new Date()).build();

        ordersRepository.save(orders1);
        ordersRepository.save(orders2);

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(),ExitStatus.COMPLETED);
        Assertions.assertEquals(2,accountsRepository.count());
    }

}