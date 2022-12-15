package kr.co.ooweat.springScheduler.common;

import com.google.gson.Gson;
import kr.co.ooweat.springScheduler.model.AlertModel;
import lombok.extern.slf4j.Slf4j;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
@Slf4j
public class Alert {
    //NOTE: 카카오 알림톡 프로세스 - API
    public static void kakaoAlert(AlertModel alertModel) {
        final String apiUrl = "http://api.ooweat.co.kr/common/alert/kakao";
        final String token = "KJK4X3RRppSuk25XciTr/DjSBI0deoB/dxT/4qi75NCWSnv1XmfS6ZPXjHOrn1DyOjj3Ig==";
        final String company = "1";

        //파라미터를 담을 배열 생성
        HttpURLConnection conn;

        try {

            conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;utf-8");
            conn.setRequestProperty("token", token);
            conn.setRequestProperty("company", company);
            conn.setRequestProperty("Accept-Encoding", "html/text");
            conn.setDoOutput(true);

            //스트림을 통한 GSON(Json) 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            Gson gson = new Gson();
            bw.write((String) gson.toJson(alertModel));
            bw.flush();
            bw.close();

            //서버에서 보낸 응답 데이터 수신 받기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String returnMsg = br.readLine();
            System.out.println("응답메시지 : " + returnMsg);

            //HTTP 응답 코드 수신
            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                System.out.println("400 : 명령을 실행 오류");
            } else if (responseCode == 500) {
                System.out.println("500 : 서버 에러.");
            } else { //정상 . 200 응답코드 . 기타 응답코드
                System.out.println("알림톡 전송완료");
            }
        } catch (IOException ie) {
            System.out.println("IOException " + ie.getCause());
            ie.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception " + e.getCause());
            e.printStackTrace();
        }

    }

    public static void smtp(AlertModel alertModel) throws MessagingException, UnsupportedEncodingException {
        log.info("alertModel: {}", alertModel.toString());
        final String user = "@gmail.com";   //발신메일 (이 사람 계정으로 보내겠다.)
        final String name = "[ooweat 배치시스템]";
        //final String password = "ooweat7880";   //기존 패스워드
        final String password = "!@#$";     //암호화된 키
        final String port = "587";
        final String bodyEncoding = "UTF-8"; //콘텐츠 인코딩

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        //메시지 포맷을 담기위해 생성
        MimeMessage message = new MimeMessage(session);
        // 메일 콘텐츠 설정
        Multipart mParts = new MimeMultipart();
        MimeBodyPart mTextPart = new MimeBodyPart();
        //이메일 헤더 설정
        message.setHeader("content-Type", "text/html");
        //발신자 설정
        message.setFrom(new InternetAddress(user, name, bodyEncoding));
        // MIME 타입 설정
        MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(MailcapCmdMap);

        //NOTE: 수신자메일 / 부서별로 나누어 볼 수 있었으면 좋겠다.
        // 영업팀 / 고객센터 / 개발팀 ..etc
        InternetAddress[] toSales = {
                new InternetAddress("sales@ooweat.co.kr"),
        };
        InternetAddress[] toCs = {
                new InternetAddress("cs@ooweat.co.kr"),
        };
        InternetAddress[] toSystem = {
                new InternetAddress("system@ooweat.co.kr"),
        };
        InternetAddress[] toTest = {
                new InternetAddress("ooweat@ooweat.co.kr"),
        };

        InternetAddress[] toTarget = {new InternetAddress(alertModel.getReceiver()+"@ooweat.co.kr")};
        StringBuffer sb = new StringBuffer();

        //메시징 예외처리
        try {
            //NOTE: 메일제목
            message.setSubject("※스케줄러 발송알림[" + alertModel.getService() + "] " + alertModel.getTitle());

            //HINT: Header
            sb.append(
                    "<div style=\"padding:40px; margin:0 auto; width:456px;\">\n" +
                            "    <table align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:456px; padding:0; margin:0 auto; font-size:14px; font-family:'Malgun Gothic', '맑은고딕', dotum,'돋움',sans-serif; color:#666; background:#fff; border:1px solid #6777ef;\">\n" +
                            "        <tbody>" +
                            "            <tr>" +
                            "               <td>\n" +
                            "                <table>\n" +
                            "                    <tbody>" +
                            "                       <tr>" +
                            "                           <td colspan=\"2\" style=\"margin:0; width: 456px; height: 40px; padding:0; background-color: white;text-align: center; \"></td>" +
                            "                       </tr>" +
                            "                       <tr style=\"height:100px;\">" +
                            "                           <td align=\"center;\">\n" +
                            "                               <a href=\"#\" title=\"새창열림\" target=\"_blank\" rel=\"noreferrer noopener\">\n" +
                            "                               \t<img src=\"https://lh3.googleusercontent.com/wNuZO8qGHWZUuDepTo2N7tRHFlj2z7jmJ_2b7Z6EswVgBY16ao_bcVdE5VrLpTMRQ1jxfMjaQHaHpg4TKgOP87GedEop9FsU48Ukh6s=s660\" " +
                            "                               alt=\"Logo\" style=\"border:0; vertical-align:middle\" loading=\"lazy\">\n" +
                            "                               </a>\n" +
                            "                           </td>" +
                            "                       </tr>" +
                            "                       <tr>" +
                            "                            <td>\n" +
                            "                            <table>\n" +
                            "                                <colgroup><col width=\"35\"><col width=\"386\"><col width=\"35\"></colgroup>\n" +
                            "                                <tbody><tr><td style=\"margin:0; padding:0; background-color: white;\">&nbsp;</td><td style=\"margin:0; padding:0; background-color: white;\">\n" +
                            "                                        <table>\n" +
                            "                                            <colgroup><col width=\"386\"></colgroup>\n" +
                            "                                            <tbody><tr><td style=\"font-size:14px; letter-spacing:-1px; line-height:24px;\">\n" +
                            "                                                    안녕하세요. 배치시스템 자동발송 메일입니다.<br><br>\n"
            );

            //HINT: Body
            if(alertModel.getService().equalsIgnoreCase("check")){
                alertModel.setTitle("[스케쥴러 배치알림]");
                sb.append(
                        "스케줄러 시스템에서 " + alertModel.getSendDate() + " 시경에 <br> \n"
                                + "확인 요청이 접수되었습니다.\n " +
                                "</td>" +
                            "</tr>" +
                            "<br>"
                          + "<tr>"
                              + "<td style=\"margin:0; padding:0; height:24px; background-color: white;\"></td>" +
                             "</tr>" +
                          "<tr>" +
                                "<td style=\"margin:0; background-color: white; padding:0 0 0; font-size:14px; letter-spacing:-1px; color:#000000; font-family:'Malgun Gothic', '맑은고딕', dotum,'돋움',sans-serif; line-height:24px;\">\n" +
                                "													   "
                                + "감사합니다." +
                                "<br>\n");
            }

            //HINT: Footer
            sb.append("                                                </td></tr><tr><td style=\"margin:0; padding:0; height:34px; background-color: white;\"></td></tr><tr><td style=\"margin:0; background-color: white; padding:0 0 0; font-size:14px; letter-spacing:-1px; color:#000000; font-family:'Malgun Gothic', '맑은고딕', dotum,'돋움',sans-serif; line-height:24px;\">\n" +
                    "                                                <div style=\"background-color: #fb9306;padding: 5% 0%;\" align=\"center\">\n" +
                    "                                                \t<span style=\"text-align: center;font-weight: bold;color: white;font-size: 2em;\">" +
                    "SYSTEM : " + alertModel.getService() + "</span>\n" +
                    "                                                </div>\n" +
                    "                                                </td></tr><tr><td style=\"margin:0; padding:0; height:53px; background-color: white;\"></td></tr><tr><td style=\"margin:0; text-align: center; background-color: white; padding:0 0 0; font-size:14px; letter-spacing:-1px; font-weight:bold; color:#005aab; line-height:24px;\">\n" +
                    "                                                    ooweat ⓒ All Rights Reserved.\n" +
                    "                                                </td></tr><tr><td style=\"margin:0; padding:0; height:32px; background-color: white;\"></td></tr></tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td><td style=\"margin:0; padding:0; background-color: white; \">&nbsp;</td></tr></tbody><tbody></tbody></table>\n" +
                    "                        </td></tr></tbody>\n" +
                    "                </table>\n" +
                    "            </td></tr></tbody>\n" +
                    "    </table>\n" +
                    "</div>");



            if(Util.stringUtil(alertModel.getReceiver()).in("sales", "cs", "mn", "system", "welpoint", "test")) {
                switch (alertModel.getReceiver()) {
                    case "sales":
                        message.addRecipients(Message.RecipientType.TO, toSales); //받는 이
                        message.addRecipients(Message.RecipientType.CC, toSystem); //참조
                        break;
                    case "cs":
                        message.addRecipients(Message.RecipientType.TO, toCs); //받는 이
                        message.addRecipients(Message.RecipientType.CC, toSystem); //참조
                        break;
                    case "system":
                        message.addRecipients(Message.RecipientType.TO, toSystem); //받는 이
                        break;
                    case "test":
                        message.addRecipients(Message.RecipientType.TO, toTest); //받는 이
                        break;
                }
            }else{
                message.addRecipients(Message.RecipientType.TO, toTarget);
            }

            mTextPart.setText(sb.toString(), bodyEncoding, "html");
            mParts.addBodyPart(mTextPart);
            message.setContent(mParts);
            Transport.send(message);
            log.info("SEND: {}", "OK");

        } catch (SendFailedException e) {
            message.addRecipients(Message.RecipientType.TO, toSystem); //받는 이
            message.setSubject("※메일발송실패[" + alertModel.getService() + "] " + alertModel.getTitle());
            sb.append(
                    alertModel.toString() + " <br> \n"
                            + "수신자의 사유로 메일 발송을 실패하였습니다. <br> \n"
                            +"</td></tr>"
                            + "<tr>"
                            + "<td style=\"margin:0; padding:0; height:24px; background-color: white;\"></td></tr><tr><td style=\"margin:0; background-color: white; padding:0 0 0; font-size:14px; letter-spacing:-1px; color:#000000; font-family:'Malgun Gothic', '맑은고딕', dotum,'돋움',sans-serif; line-height:24px;\">\n<br>\n"
            );
            mTextPart.setText(sb.toString(), bodyEncoding, "html");
            mParts.addBodyPart(mTextPart);
            message.setContent(mParts);
            Transport.send(message);
            log.error("SEND: {}", "FAIL // 수신자의 사유로 발송되지 않습니다.");
        } catch (AddressException e) {
            e.printStackTrace();
            log.error("SEND: {}", "FAIL // 주소가 올바르지 않습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
