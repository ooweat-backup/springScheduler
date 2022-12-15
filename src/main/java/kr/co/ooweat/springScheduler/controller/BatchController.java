package kr.co.ooweat.springScheduler.controller;

import kr.co.ooweat.springScheduler.common.Util;
import kr.co.ooweat.springScheduler.mappers.oracle.manager.ManagerMapper;
import kr.co.ooweat.springScheduler.service.BatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * @author ooweat
 * @version 1.0.0
 * @class BatchController
 * @Desc : 기존 crontab을 대체할 Spring Scheduler
 * @Method #1.
 * #2. mariaNodeCheck
 * #3. morningCheck
 * #4. hoursCheck
 **/

@Slf4j
@CrossOrigin("*")
@EnableAutoConfiguration
@RestController
public class BatchController {

	@Autowired
	private BatchService batchService;

	@Autowired
	private ManagerMapper managerMapper;

	@Value("${charge.use.flag}")
	private String chargeFlag;

	/**
	 * 스케쥴러
	 * 1. 매일 00 시 00 분 05초에 조건에 따라 충전하는 스케쥴러
	 *     1: 초(0-59)
	 *     2: 분(0-59)
	 *     3: 시(0-23)
	 *     4: 일(1-31)
	 *     5: 월(1-12)
	 *     6: 요일(0-7) 0, 7 : 일요일 / 1: 월요일 / 6:토요일
	* */

	@Scheduled(cron="${batch.5min.crond}") // 5분마다
	public void t5minCheck() throws UnsupportedEncodingException, MessagingException {
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		batchService.processAliveCheck();
	}

	@Scheduled(cron="${batch.halfHours.crond}") // 30분마다
	public void halfHoursCheck() throws UnsupportedEncodingException, MessagingException {
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		if(workDayCheck()){	//작업일(오늘)이 영업일이면 진행~

		}
	}

	@Scheduled(cron="${batch.workHours.crond}") // 1시간마다(영업일)
	public void workHoursCheck(){
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		if(workDayCheck()){	//작업일(오늘)이 영업일이면 진행~

		}
	}

	@Scheduled(cron="${batch.morning.crond}") // 07시 40분 부터 09시 40분까지 1시간 간격(영업일)
	public void halfOf7to9Check() throws UnsupportedEncodingException, MessagingException {
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		if(workDayCheck()){	//작업일(오늘)이 영업일이면 진행~

		}
	}

	@Scheduled(cron="${batch.workHalfHours.crond}") // 9시부터 30분마다(영업일)
	public void workHalfHoursCheck() throws UnsupportedEncodingException, MessagingException {
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		if(workDayCheck()){	//작업일(오늘)이 영업일이면 진행~

		}
	}

	@Scheduled(cron="${batch.morning.oneOff.crond}") // 아침 9시 한 번
	public void oneOffOnMorning() throws UnsupportedEncodingException, MessagingException {
		log.info("Location::{}", Util.getMethodName());
		log.info("[{}]",Util.getCurrentDateTime());
		if(workDayCheck()){	//작업일(오늘)이 영업일이면 진행~

		}
	}

	private boolean workDayCheck(){
		return managerMapper.workDayCheck() == 1 ? true : false;
	}
}
